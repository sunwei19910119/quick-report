package com.xhtt.modules.zb.dao;

import com.xhtt.modules.zb.entity.UserZbPowerEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户值班权限信息表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2022-03-17 17:09:50
 */
@Mapper
public interface UserZbPowerDao extends BaseMapper<UserZbPowerEntity> {

    UserZbPowerEntity selectByUserConnectId(String userConnectId);
}
