package com.xhtt.modules.cfg.controller;

import com.xhtt.modules.cfg.entity.CfgBaseRegionEntity;
import com.xhtt.modules.cfg.service.CfgBaseRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author SunWei
 * @Date: 2022/3/9 16:40
 * @Description:
 */
@RestController
@RequestMapping("/app/region")
public class CfgRegionController {

    @Autowired
    CfgBaseRegionService cfgBaseRegionService;

    @GetMapping("/list")
    public List<CfgBaseRegionEntity> list(@RequestParam String type,@RequestParam String parentCode){
        return cfgBaseRegionService.getXzqyByType(type,parentCode);
    }

    @GetMapping("/listAll")
    public List<CfgBaseRegionEntity> listAll(){
        return cfgBaseRegionService.getXzqyAll();
    }
}

