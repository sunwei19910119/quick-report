package com.xhtt.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 用户身份角色表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-02-23 16:03:54
 */
@Data
public class UserInfoModel implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 统一登录唯一标识字段
     */
    private String id;
    /**
     * 关联各自用户信息的主键ID
     */
    private String userConnectId;
    /**
     * 用户名（第一登录名）
     */
    private String userName;
    /**
     * 登录名（第二登录名）
     */
    private String loginName;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 手机号
     */
    private String userMobile;
    /**
     * 密码（必须符合统一登录模块密码生成规则）
     */
    private String password;
    /**
     * 数据更新时间（当数据发生变化时，此项必更新，用于同步更新依据）
     */
    private Date updateTime;
    /**
     * 验证用户是否能够登录（正常2000 注销 1000）
     */
    private Integer status;
    /**
     * 用户角色身份
     */
    private String userRole;
    /**
     * 来源系统ID
     */
    private String systemId;

}
