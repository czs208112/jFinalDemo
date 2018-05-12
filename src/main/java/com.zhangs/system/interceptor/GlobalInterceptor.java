package com.zhangs.system.interceptor;


import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

import javax.servlet.http.HttpServletRequest;

public class GlobalInterceptor implements Interceptor {

    @Override
    public void intercept(Invocation inv) {
        Controller c = inv.getController();
        HttpServletRequest request = c.getRequest();
        String contextPath = request.getContextPath();
        int port = request.getServerPort();
        String basePath;
        if (port == 80) {
            basePath = request.getScheme() + "://" + request.getServerName() + contextPath;
        } else {
            basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath;
        }
        c.setAttr("basePath", basePath); //全局项目路径
        inv.invoke();
    }
}