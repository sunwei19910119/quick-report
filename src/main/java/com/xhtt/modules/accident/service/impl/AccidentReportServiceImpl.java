package com.xhtt.modules.accident.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xhtt.common.utils.PageUtils;
import com.xhtt.common.utils.Query;
import com.xhtt.common.utils.R;
import com.xhtt.common.utils.RedisUtils;
import com.xhtt.modules.accident.controller.vo.AccidentReportSimpleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.xhtt.modules.accident.dao.AccidentReportDao;
import com.xhtt.modules.accident.entity.AccidentReportEntity;
import com.xhtt.modules.accident.service.AccidentReportService;


@Service("accidentReportService")
public class AccidentReportServiceImpl extends ServiceImpl<AccidentReportDao, AccidentReportEntity> implements AccidentReportService {

    @Autowired
    AccidentReportDao accidentReportDao;

    @Autowired
    RedisUtils redis;

    public PageUtils queryPage(Map<String, Object> params) {
        Page<AccidentReportSimpleVo> page = new Query<AccidentReportSimpleVo>(params).getPage();
        List<AccidentReportSimpleVo> list = baseMapper.queryPage(page, params);
        list.forEach(this::convertTown);
        page.setRecords(list);
        return new PageUtils(page);
    }

    @Override
    public PageUtils list(Map<String, Object> params) {
        params.put("level","0");
        return this.queryPage(params);
    }


    @Override
    public void deleteAccidentReportById(Integer id) {
        accidentReportDao.deleteById(id);
    }

    @Override
    public void deleteBatch(Integer[] ids){
        accidentReportDao.deleteBatch(ids);
    }

    @Override
    public R submit(int id) {
        int result = accidentReportDao.submit(id);
        return result == 1 ? R.ok() : R.error("提交失败");
    }

    private void convertTown(AccidentReportSimpleVo accidentReportSimpleVo){
        accidentReportSimpleVo.setTownName(redis.get(accidentReportSimpleVo.getTownCode()));
    }
}