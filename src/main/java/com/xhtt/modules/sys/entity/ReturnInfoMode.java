package com.xhtt.modules.sys.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ReturnInfoMode implements Serializable {

    private static final long serialVersionUID = -3890998115990166551L;
    Date ts;
    String code;
    String message;
    SsoUserInfoMode data;
}
