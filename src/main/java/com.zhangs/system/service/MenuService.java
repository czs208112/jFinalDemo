package com.zhangs.system.service;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

public class MenuService {

    /**
     * 根据角色id查询权限列表,shiro会调用
     *
     * @param roleIdList
     * @return
     */
    public List<Record> getPermissionByRoleId(List<Integer> roleIdList) {

        StringBuffer param = new StringBuffer();
        for (int i = 0; i < roleIdList.size(); i++) {
            if (i != roleIdList.size() - 1) {
                param.append(roleIdList.get(i) + ",");
            } else {
                param.append(roleIdList.get(i));
            }
        }

        List<Record> roleLists = Db.find("SELECT menu.permission,menu.menu_name from sys_menu menu " +
                "left JOIN sys_role_menu rolemenu on rolemenu.menu_id=menu.menu_id where rolemenu.role_id in (" + param + ")");

        return roleLists;
    }

}
