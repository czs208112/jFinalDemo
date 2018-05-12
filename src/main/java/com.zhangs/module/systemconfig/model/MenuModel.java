package com.zhangs.module.systemconfig.model;

import com.jfinal.plugin.activerecord.Model;

public class MenuModel extends Model<MenuModel> {

    private static final long serialVersionUID = 1L;

    public static final MenuModel dao = new MenuModel().dao();
}
