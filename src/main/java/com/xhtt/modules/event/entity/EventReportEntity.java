package com.xhtt.modules.event.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 事件快报（市级）
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2022-03-14 09:40:41
 */
@Data
@TableName("event_report")
public class EventReportEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer id;
	/**
	 * 上报类型：0区级，1市级
	 */
	private Integer level;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 事故单位名称
	 */
	private String companyName;
	/**
	 * 所在区县
	 */
	private String countyCode;
	/**
	 * 状态：0草稿，1待签发，2退回，3签发完成 4快报退回
	 */
	private Integer status;
	/**
	 * 签发人
	 */
	private String signer;
	/**
	 * 报告时间
	 */
	private Date reportTime;
	/**
	 * 接报信息时间
	 */
	private Date receiveTime;
	/**
	 * 接报信息途径
	 */
	private String receiveWay;
	/**
	 * 事故时间
	 */
	private Date accidentTime;
	/**
	 * 事故地点
	 */
	private String accidentSite;
	/**
	 * 涉险总人数
	 */
	private Integer totalPersonRiskCount;
	/**
	 * 死亡人数
	 */
	private Integer deathPersonCount;
	/**
	 * 失踪人数
	 */
	private Integer missingPersonCount;
	/**
	 * 被困人数
	 */
	private Integer trappedPersonCount;
	/**
	 * 受伤人数
	 */
	private Integer injuredPersonCount;
	/**
	 * 中毒人数
	 */
	private Integer poisoningPersonCount;
	/**
	 * 事故简况
	 */
	private String accidentDescription;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 报告人
	 */
	private String reporter;
	/**
	 * 创建者
	 */
	private String createBy;
	/**
	 * 科室负责人
	 */
	private String departmentHead;
	/**
	 * 联系电话
	 */
	private String phone;
	/**
	 * 删除 0否 1是
	 */
	private Integer isDelete;
	/**
	 * 续报父ID
	 */
	private Integer parentId;
	/**
	 * 事件等级：0 特别重大 1重大 2特大 3一般 4其他
	 */
	private Integer eventLevel;
	/**
	 * 上传图片
	 */
	private String uploadImage;
	/**
	 * 上传视频
	 */
	private String uploadVideo;
	/**
	 * 上传语音
	 */
	private String uploadVoice;
	/**
	 * 上传文件
	 */
	private String uploadFile;
	/**
	 * 事件类型ID
	 */
	private Integer eventTypeId;
	/**
	 * 上报单位
	 */
	private String reportUnit;
	/**
	 * 抄送单位
	 */
	private String copyForUnit;
	/**
	 * 核稿人
	 */
	private String checkPerson;
	/**
	 * 退回理由
	 */
	private String refuseReason;
	/**
	 * 经济类型 0 其他
	 */
	private Integer economicType;
	/**
	 * 编号
	 */
	private String number;
	/**
	 * 上报类型 0快报 1专报
	 */
	private Integer type;
	/**
	 * 所在乡镇
	 */
	private String townCode;
	/**
	 * 区级上报ID
	 */
	private Integer accidentReportId;

}
