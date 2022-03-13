package com.xhtt.modules.cfg.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.Data;

/**
 * 国民经济行业分类
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-03-02 15:28:08
 */
@Data
@TableName("cz_base_gmjjhyfl")
public class CfgBaseGmjjhyflEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 代码
     */
    @TableId
    private String dm;
    /**
     * 名称
     */
    private String mc;
    /**
     * 父类
     */
    private String fl;
    /**
     * id
     */
    private Integer id;
    /**
     * sid
     */
    private Integer sid;
    /**
     * 类别
     */
    private String lb;

}
