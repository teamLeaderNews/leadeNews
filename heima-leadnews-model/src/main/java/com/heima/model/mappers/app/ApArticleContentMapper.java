package com.heima.model.mappers.app;

import com.heima.model.article.pojos.ApArticleContent;

/**
 * 按照行为实体ID、收藏内容ID、和类型查询收藏
 */
public interface ApArticleContentMapper {
    ApArticleContent selectByArticleId(Integer articleId);
}
