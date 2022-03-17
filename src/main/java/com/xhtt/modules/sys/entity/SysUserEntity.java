package com.xhtt.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 系统用户
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年9月18日 上午9:28:55
 */
@TableName("sys_user")
@Data
public class SysUserEntity {

    /**
     * 用户ID
     */
    @TableId(type = IdType.AUTO)
    private Long userId;

    @NotBlank(message = "用户名不能为空")
    private String name;//姓名昵称

    private String nickName;

    /**
     * 密码
     */
//	@NotBlank(message="密码不能为空", groups = AddGroup.class)
    private String password;

    /**
     * 盐
     */
    private String salt;

    /**
     * 邮箱
     */
//    @Email(message = "邮箱格式不正确", groups = {AddGroup.class, UpdateGroup.class})
    private String email;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 状态  0：禁用   1：正常
     */
    private Integer status;

    private Integer loginStatus;//登陆状态

    private String workNo;

    @TableField(exist = false)
    private String areaName;

    @TableField(exist = false)
    private String xzqyName;

    @TableField(exist = false)
    private String provinceCode;
    @TableField(exist = false)
    private String marketCode;
    @TableField(exist = false)
    private String countyCode;
    @TableField(exist = false)
    private String townCode;

    @TableField(exist = false)
    private String villageCode;

    /**
     * 创建者ID
     */
    private Integer createUserId;

    /**
     * 创建者ID
     */
    private Integer orgId;

    /**
     * 1-企业，2-政府,3-企业员工
     */
    private Integer type;

    // 级别 0区级 1市级
    private Integer level;

    private String citySysToken;

    private String userConnectId;

    private Integer role;

    /**
     * 部门代码
     */
    @TableField(value ="BMDM")
    private String bmdm;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    @TableField(update = "now()")
    private LocalDateTime updateTime;
    private LocalDateTime lastLoginDate;


    @TableField(exist = false)
    private String power;
}
