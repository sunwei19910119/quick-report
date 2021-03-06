package com.xhtt.modules.event.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xhtt.common.utils.PageUtils;
import com.xhtt.common.utils.R;
import com.xhtt.modules.accident.entity.AccidentReportEntity;
import com.xhtt.modules.event.entity.EventReportEntity;
import com.xhtt.modules.sys.entity.SysUserEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * 事件快报（市级）
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2022-03-14 09:40:41
 */
public interface EventReportService extends IService<EventReportEntity> {

    PageUtils reportList(Map<String, Object> params, SysUserEntity sysUser);

    PageUtils signList(Map<String, Object> params, SysUserEntity sysUser);

    void deleteById(Integer id);

    void deleteBatch(Integer[] ids);

    R submit(int id,Integer status,String refuseReason);

    //区级签发上报市级
    void accidentReport(AccidentReportEntity accidentReport);

    //市级回退区级签发
    void accidentReportRefuse(EventReportEntity eventReportEntity);

    void getExportMap(Integer id, HttpServletResponse response, SysUserEntity user) throws UnsupportedEncodingException;

    String getExportMapList(List<Integer> ids, HttpServletResponse response, SysUserEntity user) throws Exception;

    R checkNumber(String number);
}

