package com.xhtt.modules.duty.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 值班人员信息详情
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2022-03-08 10:24:06
 */
@Data
@TableName("zbxx_info")
public class ZbxxInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 值班信息ID
	 */
	@TableId
	private Long zbxxId;
	/**
	 * 值班人员姓名
	 */
	private String zbryName;
	/**
	 * 用户关联统一登录ID
	 */
	private String userConnectId;
	/**
	 * 值班单位ID
	 */
	private String zbdwId;
	/**
	 * 人员类型
	 */
	private Integer ryLx;
	/**
	 * 值班类型
	 */
	private Integer zblx;
	/**
	 * 值班开始时间
	 */
	private Date zbKssj;
	/**
	 * 值班结束时间
	 */
	private Date zbJssj;
	/**
	 * 是否短信开启
	 */
	private Integer messKey;
	/**
	 * 状态
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
	/**
	 * 是否推送至省厅
	 */
	private Integer isUpload;

}
