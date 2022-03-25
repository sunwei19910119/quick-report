package com.xhtt.modules.event.controller.vo;

import lombok.Data;

import java.util.Date;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2022-03-09 14:42:11
 */
@Data
public class EventReportSimpleVo {


	private Integer id;


	/**
	 * 编号
	 */
	private String number;

	/**
	 * 上报类型：0区级，1市级
	 */
	private Integer level;
	/**
	 * 事故单位名称
	 */
	private String companyName;
	/**
	 * 事故时间
	 */
	private Date accidentTime;
	/**
	 * 所在区县
	 */
	private String countyCode;

	private String countyName;
	/**
	 * 创建者
	 */
	private String createBy;
	/**
	 * 状态：0草稿，1待签发，2退回，3签发完成
	 */
	private Integer status;

	private Integer parentId;
	/**
	 * 事件等级：0 特别重大 1重大 2特大 3一般 4其他
	 */
	private Integer eventLevel;

	private String eventLevelString;

	public String getEventLevelString() {
		switch (eventLevel) {
			case 0 : return "特别重大";
			case 1 : return "重大";
			case 2 : return "特大";
			case 3 : return "一般";
			default : return "其他";
		}
	}

	public void setEventLevelString(String eventLevelString) {
		this.eventLevelString = eventLevelString;
	}

}
