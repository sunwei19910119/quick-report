package com.xhtt.modules.base;


import com.xhtt.common.exception.RRException;
import com.xhtt.common.utils.Constant;
import com.xhtt.common.utils.R;
import com.xhtt.core.utils.JwtUtils;
import com.xhtt.modules.sys.entity.SysUserEntity;
import com.xhtt.modules.sys.service.SsoLoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 对接第三方登入
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-23 15:31
 */
@RestController
@RequestMapping("/app/third")
public class ThirdLoginController {

    @Autowired
    private SsoLoginService ssoLoginService;
    @Autowired
    private JwtUtils jwtUtils;




    /**
     * 对接第三方登录
     */
    @PostMapping(value = "/login")
    //市平台系统调用这个接口，修改要注意接口的兼容性
    public R httpGet(@RequestParam String token) throws RRException {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println("登入开始时间11111： " + df.format(LocalDateTime.now()));
        Map<String, Object> map = new HashMap<>();
        if (token != null && token != "") {
            SysUserEntity sysUser = ssoLoginService.ssoLogin(token);
            String authorization = null;
            //生成token
            if (sysUser != null) {
                authorization = jwtUtils.generateToken(sysUser.getUserId());
                map.put("token", authorization);
                map.put("level", sysUser.getLevel());
                map.put("orgId", sysUser.getOrgId());
                java.time.Duration duration = java.time.Duration.between(now, LocalDateTime.now());
                System.out.println("登入成功时间：" + df.format(LocalDateTime.now()) + " 本次登入花费时间：" + duration.toMillis() + " ms");
            }
            return R.ok().put(Constant.DATA, map);
        } else {
            return R.error("获取token失败");
        }
    }

}
