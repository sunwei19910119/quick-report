package com.xhtt.core.resolver;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xhtt.core.annotation.LoginUser;
import com.xhtt.core.interceptor.AuthorizationInterceptor;
import com.xhtt.modules.cfg.entity.CfgBaseRegionEntity;
import com.xhtt.modules.cfg.service.CfgBaseRegionService;
import com.xhtt.modules.sys.entity.SysGovernmentInfoEntity;
import com.xhtt.modules.sys.entity.SysUserEntity;
import com.xhtt.modules.sys.service.SysGovernmentInfoService;
import com.xhtt.modules.sys.service.SysUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 有@LoginUser注解的方法参数，注入当前登录用户
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-23 22:02
 */
@Component
public class LoginUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Autowired
    private SysUserService userService;
    @Autowired
    private SysGovernmentInfoService sysGovernmentInfoService;
    @Autowired
    private CfgBaseRegionService cfgBaseRegionService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(SysUserEntity.class) && parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer container,
                                  NativeWebRequest request, WebDataBinderFactory factory) throws Exception {
        //获取用户ID
        Object object = request.getAttribute(AuthorizationInterceptor.USER_KEY, RequestAttributes.SCOPE_REQUEST);
        if (object == null) {
            return null;
        }

        //获取用户信息
        SysUserEntity user = userService.getOne(new LambdaQueryWrapper<SysUserEntity>().eq(SysUserEntity::getUserId, object)
                .select(SysUserEntity::getUserId, SysUserEntity::getName, SysUserEntity::getWorkNo,
                        SysUserEntity::getMobile, SysUserEntity::getOrgId, SysUserEntity::getType,
                        SysUserEntity::getNickName, SysUserEntity::getUserConnectId,SysUserEntity::getLevel));
        if (user.getType() != null) {
            if (user.getType() == 2) {
                SysGovernmentInfoEntity sysGovernmentInfo = sysGovernmentInfoService.getById(user.getOrgId());
                if (sysGovernmentInfo != null) {
                    user.setProvinceCode(sysGovernmentInfo.getProvinceCode());
                    user.setMarketCode(sysGovernmentInfo.getMarketCode());
                    user.setCountyCode(sysGovernmentInfo.getCountyCode());
                    user.setTownCode(sysGovernmentInfo.getTownCode());
                    user.setVillageCode(sysGovernmentInfo.getVillageCode());
                    String areaName = "常州市";
                    String xzqyName = "常州市";
                    if (StringUtils.isNotEmpty(sysGovernmentInfo.getCountyCode())) {
                        CfgBaseRegionEntity cfgBaseRegion2 = cfgBaseRegionService.getInfo(sysGovernmentInfo.getCountyCode());
                        if (cfgBaseRegion2 != null) {
                            if (StringUtils.isNotEmpty(cfgBaseRegion2.getName())) {
                                areaName += cfgBaseRegion2.getName();
                                xzqyName = cfgBaseRegion2.getName();
                            }
                        }
                    }
                    if (StringUtils.isNotEmpty(sysGovernmentInfo.getTownCode())) {
                        CfgBaseRegionEntity cfgBaseRegion3 = cfgBaseRegionService.getInfo(sysGovernmentInfo.getTownCode());
                        if (cfgBaseRegion3 != null) {
                            if (StringUtils.isNotEmpty(cfgBaseRegion3.getName())) {
                                areaName += cfgBaseRegion3.getName();
                            }
                        }
                    }
                    user.setAreaName(areaName);
                    user.setXzqyName(xzqyName);
                }
            }
        }

        return user;
    }

}
