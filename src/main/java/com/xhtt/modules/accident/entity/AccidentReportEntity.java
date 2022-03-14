package com.xhtt.modules.accident.entity;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xhtt.common.file.FileInfoModel;
import com.xhtt.common.utils.DateUtils;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2022-03-09 14:42:11
 */
@Data
@TableName("accident_report")
public class AccidentReportEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	private Integer id;

	private Integer level;

	/**
	 * 标题
	 */
	@NotBlank
	private String title;

	/**
	 * 事故单位名称
	 */
	@NotBlank
	private String companyName;
	/**
	 * 所在区县
	 */
	private String countyCode;
	/**
	 * 状态：0草稿，1待签发，2退回，3签发完成, 4快报退回
	 */
	private Integer status;
	/**
	 * 签发人
	 */
	private String signer;
	/**
	 * 报告时间
	 */
	@NotNull
	@DateTimeFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date reportTime;
	/**
	 * 接报信息时间
	 */
	@NotNull
	@DateTimeFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date receiveTime;
	/**
	 * 接报信息途径
	 */
	@NotBlank
	private String receiveWay;
	/**
	 * 事故时间
	 */
	@NotNull
	@DateTimeFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date accidentTime;
	/**
	 * 事故地点
	 */
	@NotBlank
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
	@NotBlank
	private String accidentDescription;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 报告人
	 */
	@NotBlank
	private String reporter;
	/**
	 * 创建者
	 */
	private String createBy;
	/**
	 * 科室负责人
	 */
	@NotBlank
	private String departmentHead;
	/**
	 * 联系电话
	 */
	@NotBlank
	private String phone;
	/**
	 * 删除 0否 1是
	 */
	@JsonIgnore
	private Integer isDelete;

	private Integer parentId;

	/**
	 * 事件等级：0 特别重大 1重大 2特大 3一般 4其他
	 */
	private Integer eventLevel;

	private String refuseReason;

	private String uploadImage;

	private String uploadVideo;

	private String uploadVoice;

	private String uploadFile;

	/**
	 * 事件类型ID
	 */
	private String eventTypeId;

	/**
	 * 上报单位
	 */
	private String reportUnit;

	/**
	 * 抄送单位(逗号分隔)
	 */
	private String copyForUnit;

	/**
	 * 抄送单位ids(逗号分隔)
	 */
	private String copyForUnitIds;

	/**
	 * 核稿人
	 */
	private String checkPerson;

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
	private Integer townCode;

	/**
	 * 经济类型 0其他
	 */
	private Integer economicType;


	/**
	 * 签发时间
	 */
	private Date issueDate;


	@TableField(exist = false)
	private List<FileInfoModel> uploadImageList;

	@TableField(exist = false)
	private List<FileInfoModel> uploadVideoList;

	@TableField(exist = false)
	private List<FileInfoModel> uploadVoiceList;

	@TableField(exist = false)
	private List<FileInfoModel> uploadFileList;

	public List<FileInfoModel> getUploadImageList()  {
		if (null != uploadImageList) {
			return uploadImageList;
		} else if (StringUtils.isNotBlank(uploadImage)) {
			return JSON.parseArray(uploadImage, FileInfoModel.class);
		} else {
			return new ArrayList<>();
		}
	}

	public List<FileInfoModel> getUploadVideoList()  {
		if (null != uploadVideoList) {
			return uploadVideoList;
		} else if (StringUtils.isNotBlank(uploadVideo)) {
			return JSON.parseArray(uploadVideo, FileInfoModel.class);
		} else {
			return new ArrayList<>();
		}
	}

	public List<FileInfoModel> getUploadVoiceList() {
		if (null != uploadVoiceList) {
			return uploadVoiceList;
		} else if (StringUtils.isNotBlank(uploadVoice)) {
			return JSON.parseArray(uploadVoice, FileInfoModel.class);
		} else {
			return new ArrayList<>();
		}
	}

	public List<FileInfoModel> getUploadFileList() {
		if (null != uploadFileList) {
			return uploadFileList;
		} else if (StringUtils.isNotBlank(uploadFile)) {
			return JSON.parseArray(uploadFile, FileInfoModel.class);
		} else {
			return new ArrayList<>();
		}
	}
}
