/**
 * Copyright 2018 人人开源 http://www.flow.io
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.xhtt.modules.sys.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单管理
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年9月18日 上午9:26:39
 */
@TableName("sys_menu")
@Data
public class SysMenuEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String MENUID = "menu_id";
    public static final String PARENTID = "parent_id";
    public static final String NAME = "name";
    public static final String MENUTYPE = "menu_type";
    public static final String TYPE = "type";
    public static final String LEVEL = "level";
    /**
     * 菜单ID
     */
    @TableId(type = IdType.AUTO)
    private Integer menuId;

    /**
     * 父菜单ID，一级菜单为0
     */
    private Integer parentId;

    /**
     * 父菜单名称
     */
    @TableField(exist = false)
    private String parentName;

    /**
     * 菜单名称
     */
    private String name;

    private Integer menuType;//1 web 2app

    /**
     * 菜单URL
     */
    private String url;

    /**
     * 授权(多个用逗号分隔，如：user:list,user:create)
     */
//	private String perms;

    /**
     * 类型     0：目录   1：菜单   2：按钮
     */
    private Integer type;

    // 0区级 1市级
    private Integer level;
    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 排序
     */
    private Integer orderNum;

    private String memo;

    /**
     * ztree属性
     */
    @TableField(exist = false)
    private Boolean open;

    @TableField(exist = false)
    private List<SysMenuEntity> list;

    @TableField(exist = false)
    private List<SysMenuEntity> children;
}
