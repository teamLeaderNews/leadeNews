package com.heima.model.mappers.app;

import com.heima.model.article.pojos.ApAuthor;

/**
 * 定义按照作者ID
 */
public interface ApAuthorMapper {
    ApAuthor selectById(Integer id);
}
