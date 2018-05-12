package com.zhangs.module.systemconfig.model;

import com.jfinal.plugin.activerecord.Model;

public class RoleModel extends Model<RoleModel> {

    private static final long serialVersionUID = 1L;

    public static final RoleModel dao = new RoleModel().dao();
}
