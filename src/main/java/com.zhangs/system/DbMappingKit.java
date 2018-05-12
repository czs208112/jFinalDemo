
package com.zhangs.system;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.zhangs.module.systemconfig.model.SysUserModel;

public class DbMappingKit {

    public static void mapping(ActiveRecordPlugin arp) {
        arp.addMapping("sys_user", "user_id", SysUserModel.class);

    }
}
