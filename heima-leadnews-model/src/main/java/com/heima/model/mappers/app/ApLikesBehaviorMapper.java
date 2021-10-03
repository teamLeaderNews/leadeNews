package com.heima.model.mappers.app;

import com.heima.model.behavior.pojos.ApLikesBehavior;

/**
 * 定义按照行为实体、点赞内容、点赞操作方式查询点赞信息
 */
public interface ApLikesBehaviorMapper {
    /**
     * 选择最后一条喜欢按钮
     * @return
     */
    ApLikesBehavior selectLastLike(String burst, Integer objectId, Integer entryId, Short type);
}
