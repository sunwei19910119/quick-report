package com.xhtt.modules.sys.service;

import com.xhtt.common.exception.RRException;
import com.xhtt.modules.sys.entity.SysUserEntity;


public interface SsoLoginService {

    /**
     * 处理第三方登录
     */
    SysUserEntity ssoLogin(String token) throws RRException;

}
