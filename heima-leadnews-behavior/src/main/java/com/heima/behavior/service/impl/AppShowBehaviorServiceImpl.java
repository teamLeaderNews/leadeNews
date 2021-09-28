package com.heima.behavior.service.impl;

import com.heima.behavior.service.AppShowBehaviorService;
import com.heima.model.behavior.dtos.ShowBehaviorDto;
import com.heima.model.behavior.pojos.ApBehaviorEntry;
import com.heima.model.behavior.pojos.ApShowBehavior;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.mappers.app.ApShowBehaviorMapper;
import com.heima.model.mappers.app.AppBehaviorEntryMapper;
import com.heima.model.user.pojos.ApUser;
import com.heima.utils.threadlocal.AppThreadLocalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@SuppressWarnings("all")
public class AppShowBehaviorServiceImpl implements AppShowBehaviorService {

    @Autowired
    private AppBehaviorEntryMapper appBehaviorEntryMapper;

    @Autowired
    private ApShowBehaviorMapper apShowBehaviorMapper;



    @Override
    public ResponseResult saveShowBehavior(ShowBehaviorDto dto) {
        // 1.获取用户信息 & 获取设备信息
        ApUser user = AppThreadLocalUtils.getUser();
        // 2. 根据用户信息 || 设备信息，查询 行为实体    ap_behavior_entry
        if(user == null && dto.getEquipmentId() == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        Long userId = null;
        if(userId != null){
            userId = user.getId();
        }
        ApBehaviorEntry apBehaviorEntry = appBehaviorEntryMapper.selectByUserOrEquipmentId(userId,dto.getEquipmentId());
        if(apBehaviorEntry == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 3.获取前台传递过来的文章列表id
//        List<ApArticle> articleIds =  dto.getArticleIds();
        Integer[] articleIds = new Integer[dto.getArticleIds().size()];
        for (int i=0;i<articleIds.length;i++){
            articleIds[i] = dto.getArticleIds().get(i).getId();
        }
        // 4.根据行为实体id 和 文章列表id 查询行为表    ap_show_behavior
        List<ApShowBehavior> apShowBehavior = apShowBehaviorMapper.selectListByEntryIdAndArticleIds(apBehaviorEntry.getId(),articleIds);
        // 5.数据过滤，需要删除已经存在的文章id
        List<Integer> integers = Arrays.asList(articleIds);     // 将数组转到集合中
        if(!apShowBehavior.isEmpty()){
            apShowBehavior.forEach(itemm->{
                Integer articleId = itemm.getArticleId();
                integers.remove(articleId);
            });
        }
        // 6.保存操作
        if(!integers.isEmpty()){
            articleIds = new Integer[integers.size()];
            integers.toArray(articleIds);    //从集合转到数组中
            apShowBehaviorMapper.saveShowBehavior(articleIds,apBehaviorEntry.getId());

        }
        return ResponseResult.okResult(0);
    }
}
