package com.zhangs.module.systemconfig.model;

import com.jfinal.plugin.activerecord.Model;

public class GroupModel extends Model<GroupModel> {

    private static final long serialVersionUID = 1L;

    public static final GroupModel dao = new GroupModel().dao();
}
