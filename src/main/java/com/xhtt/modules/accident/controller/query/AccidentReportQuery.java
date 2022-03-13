package com.xhtt.modules.accident.controller.query;

import java.util.Date;

/**
 * @Author SunWei
 * @Date: 2022/3/9 16:50
 * @Description:
 */
public class AccidentReportQuery {

    /**
     * 事故单位名称
     */
    private String companyName;
    /**
     * 事故时间(起始)
     */
    private Date accidentTimeStart;
    /**
     * 事故时间(截止)
     */
    private Date accidentTimeEnd;

    /**
     * 所在区县
     */
    private String countyCode;

    /**
     * 状态：0草稿，1待签发，2退回，3签发完成
     */
    private Integer status;
}

