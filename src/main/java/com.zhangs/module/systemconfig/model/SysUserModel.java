package com.zhangs.module.systemconfig.model;

import com.jfinal.plugin.activerecord.Model;

public class SysUserModel extends Model<SysUserModel> {

    private static final long serialVersionUID = 1L;

    public static final SysUserModel dao = new SysUserModel().dao();
}
