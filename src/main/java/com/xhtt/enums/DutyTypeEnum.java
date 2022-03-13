package com.xhtt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 值班类型枚举
 *
 */
@Getter
@AllArgsConstructor
public enum DutyTypeEnum {

    PTZB(0, "普通值班", 0),
    GZRWB(1, "工作日晚班", 1),
    SXBB(2, "双休白班", 1),
    SXWB(3, "双休晚班", 1),
    JJRZB(4, "节假日值班", 1),
    JJRDB(5, "节假日带班", 2),
    ZDB(6, "周带班", 2),
    ZBZ_GZRWB(5, "节假日带班", 5),
    ZBZ_SXBB(6, "周带班", 5);

    /**
     * 编号
     */
    private final Integer code;
    /**
     * 名称
     */
    private final String name;

    private final Integer DutyPersonCode;
}
