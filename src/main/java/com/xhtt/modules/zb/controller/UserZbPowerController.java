package com.xhtt.modules.zb.controller;

import java.util.Arrays;
import java.util.Map;

import com.xhtt.common.utils.PageUtils;
import com.xhtt.common.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xhtt.modules.zb.entity.UserZbPowerEntity;
import com.xhtt.modules.zb.service.UserZbPowerService;


/**
 * 用户值班权限信息表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2022-03-17 17:09:50
 */
@RestController
@RequestMapping("generator/userzbpower")
public class UserZbPowerController {
    @Autowired
    private UserZbPowerService userZbPowerService;



}
