package com.zhangs.system.shiro;

import com.jfinal.aop.Duang;
import com.jfinal.plugin.activerecord.Record;
import com.zhangs.module.systemconfig.model.SysUserModel;
import com.zhangs.system.service.MenuService;
import com.zhangs.system.service.RoleService;
import com.zhangs.system.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class ShiroDbRealm extends AuthorizingRealm {

    static final UserService userService = Duang.duang(UserService.class);
    static final RoleService roleService = Duang.duang(RoleService.class);
    static final MenuService menuService = Duang.duang(MenuService.class);

    /**
     * 验证用户登录信息
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        // TODO Auto-generated method stub
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        String username = token.getUsername();
        String passwd = new String(token.getPassword());

        List<SysUserModel> list = userService.getUserByUsername(username);
        // 账号不存在
        if (CollectionUtils.isEmpty(list))
            throw new UnknownAccountException();//没找到帐号
        SysUserModel sysUser = list.get(0);
        return new SimpleAuthenticationInfo(sysUser.get("username"), sysUser.get("password"), ByteSource.Util.bytes(username + "_" + passwd), getName());
    }

    /**
     * 验证用户权限
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // TODO Auto-generated method stub
        String username = principals.getPrimaryPrincipal().toString();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        List<Record> roleRecordList = roleService.getUserRoleByUsername(username);
        List<String> roleNameList = new ArrayList<>();
        List<Integer> roleIdList = new ArrayList<>();
        for (Record record : roleRecordList) {
            roleNameList.add(record.getStr("role_name"));
            roleIdList.add(record.getInt("role_id"));
        }
        info.addRoles(roleNameList);

        List<Record> permRecordList = menuService.getPermissionByRoleId(roleIdList);
        List<String> permList = new ArrayList<>();
        for (Record permRecord : permRecordList) {
            permList.add(permRecord.getStr("permission"));
        }
        info.addStringPermissions(permList);
        return info;
    }
}
