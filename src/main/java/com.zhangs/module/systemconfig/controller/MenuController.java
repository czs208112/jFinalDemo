package com.zhangs.module.systemconfig.controller;

import com.jfinal.aop.Before;
import com.jfinal.aop.Duang;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.zhangs.common.CommonUtils;
import com.zhangs.module.systemconfig.model.MenuModel;
import com.zhangs.system.service.RoleService;

import java.util.Date;

/**
 * 权限管理controller
 */
public class MenuController extends Controller {
    static final RoleService roleService = Duang.duang(RoleService.class);
    static MenuModel model = new MenuModel();

    @Before(Tx.class)
    public void add() {
        model = getModel(MenuModel.class);
        model.set("menu_id", CommonUtils.getKey("m"));
        model.save();
    }

    @Before(Tx.class)
    public void edit() {
        model = getModel(MenuModel.class);
        model.set("update_time", new Date());
        model.update();
    }

    @Before(Tx.class)
    public void del() {
        model = getModel(MenuModel.class);
        model.set("del_flag", "1");
        model.update();
    }

    /**
     * 分页查询权限列表
     *
     * @return
     */
    public Page<MenuModel> query() {
        Integer pageNumber = getParaToInt("pageNumber");
        Integer pageSize = getParaToInt("pageSize");
        Page<MenuModel> page = model.dao().paginate(pageNumber, pageSize, "select *", "from sys_menu");
        return page;
    }
}
