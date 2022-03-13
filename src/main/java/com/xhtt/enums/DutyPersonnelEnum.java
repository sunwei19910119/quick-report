package com.xhtt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 值班人员类型枚举
 *
 */
@Getter
@AllArgsConstructor
public enum DutyPersonnelEnum {

    ZBLD(2, "值班领导"),
    ZBZ(5, "值班长"),
    ZBRY(1, "值班人员"),
    ZBSJ(3,"值班司机"),
    ZBJS(4,"技术值班");
    /**
     * 编号
     */
    private final Integer code;
    /**
     * 名称
     */
    private final String name;

}
