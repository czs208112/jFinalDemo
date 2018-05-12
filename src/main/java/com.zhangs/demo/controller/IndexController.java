package com.zhangs.demo.controller;

import com.jfinal.kit.LogKit;
import com.zhangs.common.base.BaseController;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;

import java.util.Locale;

public class IndexController extends BaseController {

    public void index() {
        LogKit.error("这只是一个log测试而已");
        Locale locale = this.getRequest().getLocale();
        setAttr("a", "ddddddddddd");
//        renderFreeMarker("index1.html");
        String _locale = getPara("_locale");
        render("/index.html");
//        render("/index.html?_locale=" + _locale);
    }

    public void portal() {
        render("/static/first.html");
    }

    /**
     * 跳转到登录页
     */
    public void login() {
        render("/static/login.html");
    }

    /**
     * 跳转到用户增删改查测试页面
     */
    public void toUserEdit() {
        render("/static/template/systemconfig/useredit.html");
    }

    /**
     * 用户登陆
     */
    public void doLogin() {
        if (!validateCaptcha("captchaCode")) {
            renderFailed("验证码错误");
            return;
        }

        String username = getPara("username");
        String password = getPara("password");

        Subject currentUser = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            currentUser.login(token);
//            ShiroSessionKit.addSessionRelation();
//            renderSuccess();

//            render("/static/first.html");
            redirect("/portal");
        } catch (UnknownAccountException exception) {
            renderFailed("账户不存在");
        } catch (IncorrectCredentialsException exception) {
            renderFailed("密码错误");
        } catch (LockedAccountException exception) {
            renderFailed("账号被锁定");
        }
    }

    public void captcha() {
        renderCaptcha();
    }

    @RequiresAuthentication
    public void logout() {
        Subject currentUser = SecurityUtils.getSubject();
        try {
            currentUser.logout();
            this.redirect("/login");
        } catch (Exception e) {
            LogKit.debug("登出发生错误", e);
        }
    }
}
