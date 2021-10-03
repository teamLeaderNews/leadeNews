package com.heima.model.mappers.app;

import com.heima.model.user.pojos.ApUserFollow;

/**
 * 按照用户ID、关注用户Id查询关注信息
 */
public interface ApUserFollowMapper {
    ApUserFollow selectByFollowId(String burst, Long userId, Integer followId);
}
