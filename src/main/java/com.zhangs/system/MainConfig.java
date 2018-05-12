package com.zhangs.system;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.github.jieblog.plugin.shiro.core.ShiroInterceptor;
import com.github.jieblog.plugin.shiro.core.ShiroPlugin;
import com.jfinal.config.*;
import com.jfinal.core.JFinal;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine;
import com.zhangs.common.CommonConst;
import com.zhangs.system.interceptor.GlobalInterceptor;
import com.zhangs.system.interceptor.I18nInterceptor;

public class MainConfig extends JFinalConfig {
    private Engine engine;

    @Override
    public void configConstant(Constants constants) {
        PropKit.use("config.properties");
        loadPropertyFile("config.properties");
        constants.setViewType(ViewType.JFINAL_TEMPLATE);
        constants.setDevMode(PropKit.getBoolean("config.devMode"));

        constants.setError401View(CommonConst.ERROR_PAGE_401);
        constants.setError403View(CommonConst.ERROR_PAGE_403);
        constants.setError404View(CommonConst.ERROR_PAGE_404);
        constants.setError500View(CommonConst.ERROR_PAGE_500);

    }

    @Override
    public void configRoute(Routes routes) {
//        routes.setBaseViewPath("/i18n");
        routes.add(new DataRoute());
    }

    @Override
    public void configEngine(Engine engine) {
        this.engine = engine;
//        String basePath = new CommonUtils().getAbsolutePath();
//        engine.addSharedObject("basePath", basePath);
        engine.addSharedObject("ctx", JFinal.me().getContextPath());
    }


    @Override
    public void configPlugin(Plugins plugins) {
        /******************* ActiveRecordPlugin start ********************/
        DruidPlugin druidPlugin = new DruidPlugin(getProperty("master.url"),
                getProperty("master.username"), getProperty("master.password")
                .trim(), getProperty("master.driver")).set(
                getPropertyToInt("druid.initialSize"),
                getPropertyToInt("druid.minIdle"),
                getPropertyToInt("druid.maxActive")).setMaxWait(
                getPropertyToLong("druid.maxWait"));
        druidPlugin.addFilter(new StatFilter());
        WallFilter wall = new WallFilter();
        wall.setDbType(getProperty("master.dbType"));
        druidPlugin.addFilter(wall);
        plugins.add(druidPlugin);
        druidPlugin.start();

        ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
        arp.setDialect(new MysqlDialect());///mysql
        //sql小写转化：false 是大写, true是小写, 不写是区分大小写
        arp.setContainerFactory(new CaseInsensitiveContainerFactory(false));
        arp.setShowSql(true);//输出sql
        plugins.add(arp);

        DbMappingKit.mapping(arp); //配置表和实体映射
        /******************* ActiveRecordPlugin end ********************/

        /******************* MongoJFinalPlugin start ********************/
//        MongoJFinalPlugin mongoJFinalPlugin = new MongoJFinalPlugin();
//        mongoJFinalPlugin.add("localhost", 27017);
//        mongoJFinalPlugin.setDatabase("test");
//        plugins.add(mongoJFinalPlugin);
        /******************* MongoJFinalPlugin end ********************/

        /******************* ShiroPlugin start ********************/
        ShiroPlugin shiroPlugin = new ShiroPlugin(this.engine);
        shiroPlugin.setLoginUrl("/index");
        shiroPlugin.setSuccessUrl("/index");
        shiroPlugin.setUnauthorizedUrl("/login");
        plugins.add(shiroPlugin);
        /******************* ShiroPlugin end ********************/

    }

    @Override
    public void configInterceptor(Interceptors me) {
        // TODO Auto-generated method stub
        me.add(new I18nInterceptor());
        me.add((new GlobalInterceptor()));
        me.add(new ShiroInterceptor());
    }

    @Override
    public void configHandler(Handlers handlers) {
    }
}
