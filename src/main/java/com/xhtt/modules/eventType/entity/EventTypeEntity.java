package com.xhtt.modules.eventType.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2022-03-11 14:06:32
 */
@Data
@TableName("event_type")
public class EventTypeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer id;
	/**
	 * 0大类 1小类
	 */
	private Integer level;
	/**
	 * 
	 */
	private String name;
	/**
	 * 
	 */
	private Integer parentId;

}
