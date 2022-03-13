package com.xhtt.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 政府用户基本信息
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-03-01 18:31:53
 */
@Data
@TableName("sys_government_info")
public class SysGovernmentInfoEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    private Integer id;
    /**
     * 第三方单位ID
     */
    private String govOtherId;
    /**
     * 政府单位名称
     */
    private String govName;
    /**
     * 省
     */
    private String provinceCode;
    /**
     * 市
     */
    private String marketCode;
    /**
     * 区县
     */
    private String countyCode;
    /**
     * 乡镇
     */
    private String townCode;
    /**
     * 所在村委
     */
    private String villageCode;
    /**
     * 生产经营场所地址地址
     */
    private String address;
    /**
     * 是否市局相关职能部门
     */
    private Integer isDepartResponsible;
    /**
     * 是否专项整治
     */
    private Integer isSpecialTreat;
    /**
     * 身份证号
     */
    private String idNo;
    /**
     *
     */
    private Date createTime;
    /**
     *
     */
    private Date updateTime;
    /**
     *
     */
    private Integer createUserId;
    /**
     *
     */
    private Integer updateUserId;

}
