package com.xhtt.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 接口url配置表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-03-12 08:56:28
 */
@Data
@TableName("sys_inter_version")
public class SysInterVersionEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    private Integer id;
    /**
     * 接口地址1
     */
    private String interUrl1;
    /**
     * 接口地址1
     */
    private String interUrl2;


    private String interUrl3;


}
