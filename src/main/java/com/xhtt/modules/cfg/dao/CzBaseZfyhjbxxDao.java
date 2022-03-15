package com.xhtt.modules.cfg.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xhtt.modules.cfg.entity.CzBaseZfyhjbxxEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 政府用户基本信息
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2022-03-15 10:48:12
 */
@Mapper
public interface CzBaseZfyhjbxxDao extends BaseMapper<CzBaseZfyhjbxxEntity> {
    List<CzBaseZfyhjbxxEntity> selectListByBmdm(String bmdm);
}
