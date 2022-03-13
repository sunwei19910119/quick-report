//package com.xhtt.config;
//
//import com.xhtt.modules.sys.oauth2.OAuth2Filter;
//import com.xhtt.modules.sys.oauth2.OAuth2Realm;
//import org.apache.shiro.mgt.SecurityManager;
//import org.apache.shiro.session.mgt.SessionManager;
//import org.apache.shiro.spring.LifecycleBeanPostProcessor;
//import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
//import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
//import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
//import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.servlet.Filter;
//import java.util.HashMap;
//import java.util.LinkedHashMap;
//import java.util.Map;
//
///**
// * Shiro配置
// *
// * @author chenshun
// * @email sunlightcs@gmail.com
// * @date 2017-04-20 18:33
// */
//@Configuration
//public class ShiroConfig {
//
//    @Bean("sessionManager")
//    public SessionManager sessionManager(){
//        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
//        sessionManager.setSessionValidationSchedulerEnabled(true);
//        sessionManager.setSessionIdCookieEnabled(true);
//        return sessionManager;
//    }
//
//    @Bean("securityManager")
//    public SecurityManager securityManager(OAuth2Realm oAuth2Realm, SessionManager sessionManager) {
//        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
//        securityManager.setRealm(oAuth2Realm);
//        securityManager.setSessionManager(sessionManager);
//
//        return securityManager;
//    }
//
//    @Bean("shiroFilter")
//    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
//        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
//        shiroFilter.setSecurityManager(securityManager);
//
//        //oauth过滤
//        Map<String, Filter> filters = new HashMap<>();
//        filters.put("oauth2", new OAuth2Filter());
//        shiroFilter.setFilters(filters);
//
//        Map<String, String> filterMap = new LinkedHashMap<>();
//        filterMap.put("/webjars/**", "anon");
//        filterMap.put("/druid/**", "anon");
//        filterMap.put("/app/**", "anon");// 如果需要做权限验证，那就要用到shiro鉴权所有接口，这行就要注释掉，在controller接口上可以加权限注解（以下3个接口去掉鉴权验证=解开注释）
////        filterMap.put("/app/cfg/system/getInfo", "anon");//获取系统信息不需要验证登陆
////        filterMap.put("/app/captcha.jpg", "anon");//获取验证码不需要验证登陆
////        filterMap.put("/app/login", "anon");//登陆不需要验证登陆
//        filterMap.put("/ws/**", "anon");//websocket 去掉验证
//        filterMap.put("/swagger/**", "anon");
//        filterMap.put("/v2/api-docs", "anon");
//        filterMap.put("/swagger-ui.html", "anon");
//        filterMap.put("/swagger-ui/**", "anon");
//        filterMap.put("/swagger-resources/**", "anon");
//        filterMap.put("/**", "oauth2");
////        filterMap.put("/app/**", "oauth2");
//
//        shiroFilter.setFilterChainDefinitionMap(filterMap);
//
//        return shiroFilter;
//    }
//
//    @Bean("lifecycleBeanPostProcessor")
//    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
//        return new LifecycleBeanPostProcessor();
//    }
//
////    @Bean
////    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
////        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
////        proxyCreator.setProxyTargetClass(true);
////        return proxyCreator;
////    }
//
//    @Bean
//    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
//        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
//        advisor.setSecurityManager(securityManager);
//        return advisor;
//    }
//
//}
