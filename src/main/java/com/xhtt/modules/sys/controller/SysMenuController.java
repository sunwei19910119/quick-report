/**
 * Copyright 2018 人人开源 http://www.flow.io
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.xhtt.modules.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xhtt.common.exception.RRException;
import com.xhtt.common.utils.Constant;
import com.xhtt.common.utils.R;
import com.xhtt.core.annotation.Login;
import com.xhtt.core.annotation.LoginUser;
import com.xhtt.modules.sys.entity.SysMenuEntity;
import com.xhtt.modules.sys.entity.SysUserEntity;
import com.xhtt.modules.sys.service.SysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * 系统菜单
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年10月27日 下午9:58:15
 */
@RestController
@RequestMapping("/app/sys/menu")
public class SysMenuController  {
    @Autowired
    private SysMenuService sysMenuService;

    /**
     * level 0区级 1市级
     */
    @GetMapping("/tree")
    @Login
    public R tree(@LoginUser SysUserEntity sysUser) {
        List<SysMenuEntity> list = sysMenuService.tree(sysUser.getLevel());

        return R.ok().put(Constant.DATA, list);
    }
}
