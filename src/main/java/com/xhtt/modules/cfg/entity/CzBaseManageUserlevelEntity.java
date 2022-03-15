package com.xhtt.modules.cfg.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 政府用户科室管理人员权限
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2022-03-15 09:56:31
 */
@Data
@TableName("cz_base_manage_userlevel")
public class CzBaseManageUserlevelEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
	private String id;
	/**
	 * 处室部门代码
	 */
	private String csDeptCode;
	/**
	 * 处室部门名称
	 */
	private String csDeptName;
	/**
	 * 处室负责人userid
	 */
	private String csUserid;
	/**
	 * 处室负责人姓名
	 */
	private String csUsername;
	/**
	 * 分管领导人代码
	 */
	private String fgDeptCode;
	/**
	 * 分管领导人姓名
	 */
	private String fgDeptName;
	/**
	 * 地区编码areaid
	 */
	private String areaid;
	/**
	 * 部门单位id
	 */
	private String deptGovid;
	/**
	 * 是否删除(已弃用)
	 */
	private Integer deleted;
	/**
	 * 是否删除
	 */
	private Integer disabled;
	/**
	 * 乐观锁
	 */
	private Integer version;
	/**
	 * 创建人
	 */
	private Long createdBy;
	/**
	 * 创建时间
	 */
	private Date creationDate;
	/**
	 * 最后修改人
	 */
	private Long lastUpdatedBy;
	/**
	 * 最后修改时间
	 */
	private Date lastUpdateDate;

}
