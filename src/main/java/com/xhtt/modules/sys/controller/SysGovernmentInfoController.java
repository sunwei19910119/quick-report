package com.xhtt.modules.sys.controller;


import com.xhtt.common.utils.Constant;
import com.xhtt.common.utils.R;
import com.xhtt.core.annotation.Login;
import com.xhtt.modules.sys.entity.SysGovernmentInfoEntity;
import com.xhtt.modules.sys.service.SysGovernmentInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 政府用户基本信息
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-03-01 18:31:53
 */
@RestController
@RequestMapping("app/sysgovernmentinfo")
public class SysGovernmentInfoController {
    @Autowired
    private SysGovernmentInfoService sysGovernmentInfoService;


    @GetMapping("/info/{id}")
    @Login
    public R info(@PathVariable("id") Integer id) {
        SysGovernmentInfoEntity sysGovernmentInfo = sysGovernmentInfoService.getById(id);
        return R.ok().put(Constant.DATA, sysGovernmentInfo);
    }
}
