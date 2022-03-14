package com.xhtt.modules.dept.service.impl;

import com.xhtt.common.utils.PageUtils;
import com.xhtt.common.utils.Query;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.xhtt.modules.dept.dao.DeptDao;
import com.xhtt.modules.dept.entity.DeptEntity;
import com.xhtt.modules.dept.service.DeptService;


@Service("deptService")
public class DeptServiceImpl extends ServiceImpl<DeptDao, DeptEntity> implements DeptService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<DeptEntity> page = this.page(
                new Query<DeptEntity>(params).getPage(),
                new QueryWrapper<DeptEntity>()
        );

        return new PageUtils(page);
    }

}