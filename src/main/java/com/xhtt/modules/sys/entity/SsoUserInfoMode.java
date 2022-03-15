package com.xhtt.modules.sys.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class SsoUserInfoMode implements Serializable {

    private static final long serialVersionUID = -3890998115990166531L;
    Map<String, Object> userInfo;
    String username;
    List<Map<String, Object>> systems;
    ThirdPartInfoMode thirdPartInfo;
    String enabled;
    String salt;
    String loginType;
    String lastModifyPassTime;
    List<Map<String, Object>> authorities;
    String password;
    String name;
    String id;
    String cacheKey;
    String accountNonExpired;
    String accountNonLocked;
    String credentialsNonExpired;
    String userConnectId;
    String bmdm;

}
