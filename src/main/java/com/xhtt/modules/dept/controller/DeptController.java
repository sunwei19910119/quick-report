package com.xhtt.modules.dept.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

import com.xhtt.modules.dept.entity.DeptEntity;
import com.xhtt.modules.dept.service.DeptService;



/**
 * 抄送单位
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2022-03-14 17:09:28
 */
@RestController
@RequestMapping("app/dept")
public class DeptController {
    @Autowired
    private DeptService deptService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Integer level){
        List<DeptEntity> list = deptService.selectDeptList(level);
        HashMap data = new HashMap<>();
        data.put("list",list);
        return R.ok().put("data",data);
    }


}
