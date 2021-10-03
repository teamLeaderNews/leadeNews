package com.heima.model.mappers.app;

import com.heima.model.behavior.pojos.ApUnlikesBehavior;

/**
 * 按照行为实体ID、文章ID查询不喜欢最有一条信息的方法：
 */
public interface ApUnlikesBehaviorMapper {
    /**
     * 选择最后一条不喜欢数据
     * @return
     */
    ApUnlikesBehavior selectLastUnLike(Integer entryId, Integer articleId);
}
