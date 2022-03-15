package com.xhtt.modules.dept.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 抄送单位
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2022-03-14 17:09:28
 */
@Data
@TableName("dept")
public class DeptEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer id;
	/**
	 * 部门名称
	 */
	private String name;
	/**
	 * 所在区县
	 */
	private String szqx;


	private String bmdm;

}
