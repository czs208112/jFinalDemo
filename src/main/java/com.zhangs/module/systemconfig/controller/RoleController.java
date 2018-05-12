package com.zhangs.module.systemconfig.controller;

import com.jfinal.aop.Before;
import com.jfinal.aop.Duang;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.zhangs.common.CommonUtils;
import com.zhangs.module.systemconfig.model.MenuModel;
import com.zhangs.module.systemconfig.model.RoleModel;
import com.zhangs.system.service.RoleService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 角色管理
 */
public class RoleController extends Controller {
    static final RoleService roleService = Duang.duang(RoleService.class);
    static RoleModel roleModel = new RoleModel();

    @Before(Tx.class)
    public void add() {
        roleModel = getModel(RoleModel.class);
        roleModel.set("role_id", CommonUtils.getKey("r"));
        roleService.addRole(roleModel);
    }

    @Before(Tx.class)
    public void edit() {
        roleModel = getModel(RoleModel.class);
        roleModel.set("update_time", new Date());
        roleModel.update();
    }

    @Before(Tx.class)
    public void del() {
        roleModel = getModel(RoleModel.class);
        roleModel.set("del_flag", "1");
        roleModel.update();
    }

    /**
     * 设置角色权限
     */
    @Before(Tx.class)
    public void configRole() {
        String roleid = getPara("roleid");
        String menuids = getPara("menuids");
        String[] menuidArr = menuids.split(",");
        List<MenuModel> menuList = new ArrayList<>();
        MenuModel menu = new MenuModel();
        for (String menuid : menuidArr) {
            menu.set("id", CommonUtils.getKey("ur"));
            menu.set("role_id", roleid);
            menu.set("menu_id", menuid);
            menuList.add(menu);
        }
        String sql = "INSERT INTO sys_role_menu('id', 'role_id', 'menu_id') VALUES (?,?,?)";
        Db.batch(sql, "id,role_id,menu_id", menuList, 20);
    }

    /**
     * 分页查询角色列表
     *
     * @return
     */
    public Page<RoleModel> query() {
        Integer pageNumber = getParaToInt("pageNumber");
        Integer pageSize = getParaToInt("pageSize");
        Page<RoleModel> page = roleModel.dao().paginate(pageNumber, pageSize, "select *", "from sys_role");
        return page;
    }
}
