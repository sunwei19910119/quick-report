package com.xhtt.modules.cfg.dao;

import com.xhtt.modules.cfg.entity.CfgBaseRegionEntity;
import com.xhtt.modules.cfg.entity.CzBaseManageUserlevelEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 政府用户科室管理人员权限
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2022-03-15 09:56:31
 */
@Mapper
public interface CzBaseManageUserlevelDao extends BaseMapper<CzBaseManageUserlevelEntity> {


    CzBaseManageUserlevelEntity isManager(String deptCode,String userConnectId);


	List<CzBaseManageUserlevelEntity> selectListByDeptCodes(String[] deptCodes);
}
