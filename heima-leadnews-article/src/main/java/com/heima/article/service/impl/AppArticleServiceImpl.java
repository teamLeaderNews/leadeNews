package com.heima.article.service.impl;

import com.aliyuncs.utils.StringUtils;
import com.heima.article.service.AppArticleService;
import com.heima.common.common.article.constans.ArticleConstans;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.mappers.app.ApArticleMapper;
import com.heima.model.mappers.app.ApUserArticleListMapper;
import com.heima.model.user.pojos.ApUser;
import com.heima.model.user.pojos.ApUserArticleList;
import com.heima.utils.threadlocal.AppThreadLocalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AppArticleServiceImpl implements AppArticleService {

    private static final short MAX_PAGE_SIZE = 50;

    @Override
    public ResponseResult load(ArticleHomeDto dto, Short type) {
        // 校验参数是否为空
        if(dto == null){
            dto = new ArticleHomeDto();
        }

        // 时间校验
        if(dto.getMaxBehotTime() == null){
            dto.setMaxBehotTime(new Date());
        }

        if(dto.getMinBehotTime() == null){
            dto.setMinBehotTime(new Date());
        }

        // 分页参数校验
        Integer size = dto.getSize();
        if(size == null || size <= 0){
            size = 20;
        }
        size = Math.min(size,MAX_PAGE_SIZE);
        dto.setSize(size);

        // 文章频道参数校验
        if(StringUtils.isEmpty(dto.getTag())){
            dto.setTag(ArticleConstans.DEFAULT_TAG);
        }

        if (!type.equals(ArticleConstans.LOADTYPE_LOAD_MORE) && !type.equals(ArticleConstans.LOADTYPE_LOAD_NEW)){
            type = ArticleConstans.LOADTYPE_LOAD_MORE;
        }

        // 获取用户信息
        ApUser user = AppThreadLocalUtils.getUser();
        if(user != null) {
            // 已存在用户信息，加载最新文章
            List<ApArticle> apArticleList = getUserArticle(user,dto,type);
            return ResponseResult.okResult(apArticleList);

        }else{
            // 不存在登陆 加载默认文章
            List<ApArticle> apArticleList = getDefaultArticle(dto,type);
            return ResponseResult.okResult(apArticleList);
        }
    }

    @Autowired
    private ApArticleMapper apArticleMapper;


    /**
     * 查询 默认 文章信息
     * @param user
     * @param dto
     * @param type
     * @return
     */
    private List<ApArticle> getDefaultArticle(ArticleHomeDto dto, Short type) {

        return apArticleMapper.loadArticleListByLocation(dto,type);
    }

    @Autowired
    private ApUserArticleListMapper apUserArticleListMapper;

    /**
     * 从用户的推荐表中，查询文章信息，如果没有，再从默认的文章中获取信息
     * @param user
     * @param dto
     * @param type
     * @return
     */
    private List<ApArticle> getUserArticle(ApUser user, ArticleHomeDto dto, Short type) {
        List<ApUserArticleList> list = apUserArticleListMapper.loadArticleIdListByUser(user,dto,type);
        if(!list.isEmpty()){
            List<ApArticle> apArticles =  apArticleMapper.locaArticleListByList(list);
            return apArticles;
        }else{
            return getDefaultArticle(dto,type);
        }
    }
}
