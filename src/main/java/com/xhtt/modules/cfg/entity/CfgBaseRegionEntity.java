package com.xhtt.modules.cfg.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 行政区划代码信息
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-03-01 16:57:06
 */
@Data
@TableName("cz_base_region")
@NoArgsConstructor
public class CfgBaseRegionEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    public CfgBaseRegionEntity(String name) {
        this.name = name;
    }

    /**
     * 区域CODE
     */
    @TableId
    private String regionCode;
    /**
     * 父级区域CODE
     */
    private String parentCode;
    /**
     * 名称
     */
    private String name;
    /**
     * 类型
     */
    private String type;
    /**
     * 层级
     */
    private String level;
    /**
     * 排序
     */
    private Integer orderNum;
    @TableField(exist = false)
    private List<CfgBaseRegionEntity> children;

}
