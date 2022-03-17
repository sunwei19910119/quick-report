package com.xhtt.modules.dept.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xhtt.common.utils.PageUtils;
import com.xhtt.modules.dept.entity.DeptEntity;

import java.util.List;
import java.util.Map;

/**
 * 抄送单位
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2022-03-14 17:09:28
 */
public interface DeptService extends IService<DeptEntity> {

    List<DeptEntity> selectDeptList(Integer level);

    List<DeptEntity> selectDeptListByIds(String[] ids);
}

