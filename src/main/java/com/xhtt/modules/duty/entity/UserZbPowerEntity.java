package com.xhtt.modules.duty.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户值班权限信息表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2022-03-08 10:24:07
 */
@Data
@TableName("user_zb_power")
public class UserZbPowerEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId
	private Integer powerId;
	/**
	 * 用户关联统一登录CONNECT_ID
	 */
	private String userConnectId;
	/**
	 * 是否是值班人员
	 */
	private Integer zbryKey;
	/**
	 * 是否是排班管理人员
	 */
	private Integer pbryKey;
	/**
	 * 是否是值班领导
	 */
	private Integer zbldKey;
	/**
	 * 是否是值班司机
	 */
	private Integer zbsjKey;
	/**
	 * 是否是值班技术
	 */
	private Integer zbjsKey;
	/**
	 * 是否是值班长
	 */
	private Integer zbzKey;
	/**
	 * 状态(正常200 删除-100)
	 */
	private Integer status;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 创建人
	 */
	private String creater;
	/**
	 * 更新时间
	 */
	private Date updateTime;
	/**
	 * 最后修改人
	 */
	private String lastUpdatedBy;

}
