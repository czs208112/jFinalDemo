package com.zhangs.module.systemconfig.controller;

import com.jfinal.aop.Before;
import com.jfinal.aop.Duang;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.zhangs.common.CommonUtils;
import com.zhangs.module.systemconfig.model.SysUserModel;
import com.zhangs.system.service.UserService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户管理
 */
public class UserController extends Controller {
    static final UserService userService = Duang.duang(UserService.class);
    static SysUserModel sysUserModel = new SysUserModel();

    public void index() {
//        Record user = new Record().set("name", "James").set("id", 251111);
//        Db.save("sys_user", user);
        //只是一个数据库添加测试，使用db/record的方式，没有跳转至页面

        SysUserModel.dao.find("select * from sys_user where username= 'James'"); //只是一个查询的测试.使用dao的方式。
    }

    //    @RequiresPermissions("user:add")
    @Before(Tx.class)
    public void add() {
        sysUserModel = getModel(SysUserModel.class);
        sysUserModel.set("user_id", CommonUtils.getKey("u"));
        userService.addUser(sysUserModel);
    }

    @Before(Tx.class)
    public void edit() {
        sysUserModel = getModel(SysUserModel.class);
        sysUserModel.set("update_time", new Date());

        sysUserModel.update();
    }

    @Before(Tx.class)
    public void del() {
        sysUserModel = getModel(SysUserModel.class);
        sysUserModel.set("del_flag", "1");
        sysUserModel.update();
    }

    /**
     * 设置用户角色
     */
    @Before(Tx.class)
    public void configRole() {
        String userid = getPara("userid");
        String roleids = getPara("roleids");
        String[] roleidArr = roleids.split(",");
        List<SysUserModel> userList = new ArrayList<>();
        SysUserModel user = new SysUserModel();
        for (String roleid : roleidArr) {
            user.set("id", CommonUtils.getKey("ur"));
            user.set("user_id", userid);
            user.set("role_id", roleid);
            userList.add(user);
        }
        String sql = "insert into sys_user_role('id', 'user_id', 'role_id') values (?, ?, ?)";
        Db.batch(sql, "id,user_id,role_id", userList, 10);
    }

    /**
     * 分页查询用户列表
     *
     * @return
     */
    public void query() {
        Integer pageSize = getParaToInt("length");
        Integer pageNumber = (getParaToInt("start") + 1) / (pageSize + 1) + 1;
        Page<SysUserModel> userPage = sysUserModel.dao().paginate(pageNumber, pageSize, "select *", "from sys_user");
        renderJson(userPage);
    }
}
