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

package com.xhtt.modules.sys.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xhtt.common.utils.Constant;
import com.xhtt.common.utils.MapUtils;
import com.xhtt.modules.sys.dao.SysMenuDao;
import com.xhtt.modules.sys.entity.SysMenuEntity;
import com.xhtt.modules.sys.entity.SysUserEntity;
import com.xhtt.modules.sys.service.SysMenuService;
import com.xhtt.modules.sys.service.SysUserService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service("sysMenuService")
public class SysMenuServiceImpl extends ServiceImpl<SysMenuDao, SysMenuEntity> implements SysMenuService {

    @Override
    public List<SysMenuEntity> queryListParentId(Integer parentId, List<Integer> menuIdList) {
        List<SysMenuEntity> menuList = queryListParentId(parentId);
        if (menuIdList == null || menuIdList.size() == 0) {
            return menuList;
        }

        List<SysMenuEntity> userMenuList = new ArrayList<>();
        for (SysMenuEntity menu : menuList) {
            if (menuIdList.contains(menu.getMenuId())) {
                userMenuList.add(menu);
            }
        }
        userMenuList.sort(Comparator.comparing(SysMenuEntity::getOrderNum));
        return userMenuList;
    }

    @Override
    public List<SysMenuEntity> queryListParentId(Integer parentId) {
        return baseMapper.queryListParentId(parentId);
    }

    @Override
    public List<SysMenuEntity> queryNotButtonList() {
        return baseMapper.queryNotButtonList();
    }


    @Override
    public void delete(Integer menuId) {
        //删除菜单
        this.removeById(menuId);
    }

    @Override
    public List<SysMenuEntity> tree(int level) {
        List<SysMenuEntity> list;
        List<SysMenuEntity> dbList = this.list(new QueryWrapper<SysMenuEntity>().eq(SysMenuEntity.LEVEL ,level));
        //获取一级的先
        list = dbList.stream().filter(sysMenuEntity -> sysMenuEntity.getParentId() == 0).collect(Collectors.toList());
        for (SysMenuEntity p : list) {
            getChildren(p, dbList);
        }

        return list;
    }



    @Override
    public List<Integer> getAllChildMenuIds(int menuId) {
        String str = baseMapper.getAllChildMenuIds(menuId);
        List<Integer> ids = Arrays.stream(str.split(",")).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
        return ids;
    }


    private void getChildren(SysMenuEntity p, List<SysMenuEntity> dbList) {
        List<SysMenuEntity> childs = dbList.stream().filter(sysMenuEntity -> sysMenuEntity.getParentId().intValue() == p.getMenuId().intValue()).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(childs)) {
            p.setChildren(childs);
            for (SysMenuEntity child : childs) {
                if (child.getType() == 0) {//是目录，继续往下递归
                    getChildren(child, dbList);
                }
            }
        }

    }

    /**
     * 获取所有菜单列表
     */
    private List<SysMenuEntity> getAllMenuList(List<Integer> menuIdList) {
        //查询根菜单列表
        List<SysMenuEntity> menuList = queryListParentId(0, menuIdList);
        //递归获取子菜单
        getMenuTreeList(menuList, menuIdList);

        return menuList;
    }

    /**
     * 递归
     */
    private List<SysMenuEntity> getMenuTreeList(List<SysMenuEntity> menuList, List<Integer> menuIdList) {
        List<SysMenuEntity> subMenuList = new ArrayList<SysMenuEntity>();

        for (SysMenuEntity entity : menuList) {
            if (!menuIdList.contains(entity.getMenuId())) {
                continue;
            }
            //目录
            if (entity.getType() == Constant.MenuType.CATALOG.getValue()) {
                entity.setList(getMenuTreeList(queryListParentId(entity.getMenuId(), menuIdList), menuIdList));
            }
            subMenuList.add(entity);
        }

        return subMenuList;
    }
}
