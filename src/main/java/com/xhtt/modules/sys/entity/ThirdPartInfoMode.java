package com.xhtt.modules.sys.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class ThirdPartInfoMode implements Serializable {
    private static final long serialVersionUID = -3890998115990166551L;


    String szqx;
    String szxz;
    String szcw;
    String sfzh;
    String sfzxzz;
    String zfdwmc;
    String dwid;
    String companyId;
    String companyCode;
    String companyName;
    String companyPhone;
    String creditLevel;
    String securityLevel;
    String regCountyCode;
    String regTownCode;
    String regVillageCode;
    String regAddress;
    String principalPerson;
    String principalPersonPhone;
    String legalPerson;
    String legalPersonMobile;
    String address;
    String postCode;
    String adminLevel;
    String externalId;
    Date establishDate;
    String registerOrg;
    String registerLargeClass;
    String registerSmallClass;
    String economyType;
    String legalPersonPhone;
    String countyCode;
    String townCode;
    String villageCode;
    String category;
    String largeClass;
    String mediumClass;
    String smallClass;
    Integer disableType;
    Integer promiseApplied;
    Integer safetyDirector;
    Integer employeesNum;
    String bmdm;
    ThirdPartInfoMode companyInfo;
    String areaId;

}
