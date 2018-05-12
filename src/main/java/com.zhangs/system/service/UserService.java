package com.zhangs.system.service;

import com.zhangs.module.systemconfig.model.SysUserModel;
import com.zhangs.system.shiro.PasswordKit;

import java.util.List;
import java.util.Map;

public class UserService {

    /**
     * 获取用户,shiro会调用
     *
     * @param username String
     * @return sysUserList
     */
    public List<SysUserModel> getUserByUsername(String username) {
        return SysUserModel.dao.find("select * from sys_user where username= ? ", username);
    }

    /**
     * 新增用户
     *
     * @param sysUser 用户信息
     */
    public void addUser(SysUserModel sysUser) {
        //处理密码
        String passpwd = sysUser.get("password");
        String username = sysUser.get("username");
        Map<String, String> map = PasswordKit.encryptPassword(passpwd, username + "_" + passpwd);
        String password = map.get(PasswordKit.ENCRYPT_KEY);
        String salt = map.get(PasswordKit.ENCRYPT_SALT);
        sysUser.set("password", password);
//        sysUser.setSalt(salt);
        sysUser.save();
    }

}
