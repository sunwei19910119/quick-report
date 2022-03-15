package com.xhtt.modules.cfg.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 政府用户基本信息
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2022-03-15 10:48:12
 */
@Data
@TableName("cz_base_zfyhjbxx")
public class CzBaseZfyhjbxxEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * UserID
	 */
	@TableId
	private String userId;
	/**
	 * UserName
	 */
	private String userName;
	/**
	 * AreaID
	 */
	private String areaId;
	/**
	 * GroupID
	 */
	private Integer groupId;
	/**
	 * Pwd
	 */
	private String pwd;
	/**
	 * IsAdmin
	 */
	private Integer isAdmin;
	/**
	 * Nickname
	 */
	private String nickName;
	/**
	 * Department
	 */
	private String department;
	/**
	 * Mobile
	 */
	private String mobile;
	/**
	 * 进局日期
	 */
	private Date jjrq;
	/**
	 * 是否部门负责人
	 */
	private Integer sfbmfzr;
	/**
	 * 是否注销
	 */
	private String sfzx;
	/**
	 * 办公电话
	 */
	private String bgdh;
	/**
	 * 部门代码
	 */
	private String bmdm;
	/**
	 * 序号
	 */
	private Long xh;
	/**
	 * 关联机构
	 */
	private String gljg;
	/**
	 * 身份证号
	 */
	private String sfzh;
	/**
	 * 性别
	 */
	private String xb;
	/**
	 * 民族
	 */
	private String mz;
	/**
	 * 政治面貌
	 */
	private String zzmm;
	/**
	 * 最高学历
	 */
	private String zgxl;
	/**
	 * 毕业院校
	 */
	private String byyx;
	/**
	 * 所学专业
	 */
	private String sxzy;
	/**
	 * 现职级
	 */
	private String xzj;
	/**
	 * 职务类别
	 */
	private String zwlb;
	/**
	 * 身份类别
	 */
	private String sflb;
	/**
	 * 职务层级
	 */
	private String zwcj;
	/**
	 * 职务名称
	 */
	private String zwmc;
	/**
	 * 考核分类
	 */
	private String khfl;
	/**
	 * create_date
	 */
	private Date createDate;
	/**
	 * 单位id
	 */
	private String dwid;
	/**
	 * 所在区县
	 */
	private String szqx;
	/**
	 * 所在乡镇
	 */
	private String szxz;
	/**
	 * 所在村委
	 */
	private String szcw;
	/**
	 * 政府单位名称
	 */
	private String zfdwmc;
	/**
	 * 是否市局相关职能部门
	 */
	private String sfsjxgznbm;
	/**
	 * 是否专项整治
	 */
	private String sfzxzz;
	/**
	 * 执法证号
	 */
	private String zfzh;

}
