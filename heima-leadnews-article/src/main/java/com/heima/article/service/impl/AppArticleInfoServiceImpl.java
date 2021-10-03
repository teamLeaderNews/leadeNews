package com.heima.article.service.impl;

import com.heima.article.service.AppArticleInfoService;
import com.heima.model.article.pojos.ApArticleConfig;
import com.heima.model.article.pojos.ApArticleContent;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.crawler.core.parse.ZipUtils;
import com.heima.model.mappers.app.ApArticleConfigMapper;
import com.heima.model.mappers.app.ApArticleContentMapper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Getter
@Service
@SuppressWarnings("all")
public class AppArticleInfoServiceImpl implements AppArticleInfoService {

    @Autowired
    private ApArticleContentMapper apArticleContentMapper;
    @Autowired
    private ApArticleConfigMapper apArticleConfigMapper;

    /**
     * 加载文章详情内容
     * @param articleId
     * @return
     */
    public ResponseResult getArticleInfo(Integer articleId){
        // 参数无效
        if(articleId==null||articleId<1){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 根据文章id 查询config 信息
        ApArticleConfig config = apArticleConfigMapper.selectByArticleId(articleId);
        Map<String,Object> data = new HashMap<>();
        // 参数无效
        if(config==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }else if(!config.getIsDelete()){
            // 没删除的标识才返回给客户端
            ApArticleContent content = apArticleContentMapper.selectByArticleId(articleId);
            String gunzipContent = ZipUtils.gunzip(content.getContent());
            content.setContent(gunzipContent);
            data.put("content",content);
        }
        data.put("config",config);
        return ResponseResult.okResult(data);
    }
}
