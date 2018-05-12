package com.zhangs.system.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.i18n.I18n;
import com.jfinal.i18n.Res;
import com.jfinal.kit.StrKit;
import com.jfinal.render.Render;
import com.zhangs.common.CommonConst;

public class I18nInterceptor implements Interceptor {
    private String localeParaName;
    private String resName;
    private boolean isSwitchView;

    public I18nInterceptor() {
        this.localeParaName = "_locale";
        this.resName = "_res";
        this.isSwitchView = false;
    }

    public I18nInterceptor(String localeParaName, String resName) {
        this.localeParaName = "_locale";
        this.resName = "_res";
        this.isSwitchView = false;
        if (StrKit.isBlank(localeParaName)) {
            throw new IllegalArgumentException("localeParaName can not be blank.");
        } else if (StrKit.isBlank(resName)) {
            throw new IllegalArgumentException("resName can not be blank.");
        } else {
            this.localeParaName = localeParaName;
            this.resName = resName;
        }
    }

    public I18nInterceptor(String localeParaName, String resName, boolean isSwitchView) {
        this(localeParaName, resName);
        this.isSwitchView = isSwitchView;
    }

    public I18nInterceptor(boolean isSwitchView) {
        this.localeParaName = "_locale";
        this.resName = "_res";
        this.isSwitchView = false;
        this.isSwitchView = isSwitchView;
    }

    protected String getLocaleParaName() {
        return this.localeParaName;
    }

    protected String getResName() {
        return this.resName;
    }

    protected String getBaseName() {
        return CommonConst.I18N_DEFAULTBASENAME;
    }

    public void intercept(Invocation inv) {
        Controller c = inv.getController();
        String localeParaName = this.getLocaleParaName();
        String locale = c.getPara(localeParaName);
        if (StrKit.notBlank(locale)) {
            c.setCookie(localeParaName, locale, 999999999);
        } else {
            locale = c.getCookie(localeParaName);
            if (StrKit.isBlank(locale)) {
                locale = CommonConst.I18N_DEFAULT_LOCALE;
            }
        }

        inv.invoke();
        if (this.isSwitchView) {
            this.switchView(locale, c);
        } else {
            Res res = I18n.use(this.getBaseName(), locale);
            c.setAttr(this.getResName(), res);
        }

    }

    public void switchView(String locale, Controller c) {
        Render render = c.getRender();
        if (render != null) {
            String view = render.getView();
            if (view != null) {
                if (view.startsWith("/")) {
                    view = "/" + locale + view;
                } else {
                    view = locale + "/" + view;
                }

                render.setView(view);
            }
        }

    }
}
