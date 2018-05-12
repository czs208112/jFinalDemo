package com.zhangs.system.service;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.zhangs.module.systemconfig.model.RoleModel;

import java.util.List;

public class RoleService {

    /**
     * 根据用户名获取角色,shiro会调用
     *
     * @param username
     * @return
     */
    public List<Record> getUserRoleByUsername(String username) {

        List<Record> roleLists = Db.find("SELECT role.role_id, role.role_name FROM sys_user suser left JOIN sys_user_role userrole " +
                "on userrole.user_id = suser.user_id left join sys_role role on role.role_id=userrole.role_id where suser.username = ?", username);

        return roleLists;
    }

    /**
     * 新增角色
     *
     * @param role 角色信息
     */
    public void addRole(RoleModel role) {
        role.save();
    }
}
