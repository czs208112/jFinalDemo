package com.zhangs.system;

import com.jfinal.config.Routes;
import com.zhangs.demo.controller.IndexController;
import com.zhangs.module.systemconfig.controller.UserController;

public class DataRoute extends Routes {

    @Override
    public void config() {
        add("/", IndexController.class);
        add("/user", UserController.class);
    }
}
