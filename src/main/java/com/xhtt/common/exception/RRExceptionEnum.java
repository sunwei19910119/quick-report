package com.xhtt.common.exception;

import lombok.Getter;

/**
 * 异常提示信息
 *
 * @Date 2019-01-21 上午 10:31
 */
@Getter
public enum RRExceptionEnum {

    PARAM_IS_EMPTY(1, "参数为空"),
    LABEL_NAME_EXIST(2, "标签名称已存在"),
    LABEL_CODE_EXIST(3, "标签编码已存在"),
    POINT_DEVICE_NAME_EXIST(4, "检点设备名称/检查项已存在"),
    LINE_NAME_EXIST(5, "线路名称已存在"),
    TASK_NAME_EXIST(6, "任务名称已存在"),
    NOT_SCHEDULING(7, "今天没有排班"),
    USER_NOT_BELONG_GROUP(8, "该用户不是组成员"),
    TASK_NOT_EXIST(9, "任务不存在"),
    CURRENT_TASK_NOT_EXIST(10, "当前任务不存在"),
    CURRENT_TASK_STARTED(11, "当前任务其他人正在巡检中"),
    CURRENT_TASK_FINISHED(12, "当前任务已完成"),
    CUSTOMER_NAME_EXIST(13, "客户名称已存在"),
    PRODUCT_NAME_EXIST(14, "产品名称已存在"),
    NO_POINT(15, "没有检点"),
    EXPORT_FAIL(16, "导出失败"),
    EXCEL_HEADER_ERROR(17, "上传文件的内容格式不正确"),
    NO_TEAM_LEADER(18, "没有组长"),
    DATE_IS_EMPTY(19, "日期为空"),
    POINT_CONTENT_EMPTY(20, "检点内容为空");

    private int code;

    private String msg;

    RRExceptionEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
