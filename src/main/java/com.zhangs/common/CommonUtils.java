package com.zhangs.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


/**
 * 工具类
 */
public class CommonUtils {

    public static void main(String[] args) {
        String timestamp = CommonUtils.getKey("u");
        System.out.println(timestamp);
    }

    public static String getTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String timestamp = sdf.format(new Date());
        return timestamp;
    }

    /**
     * 自动生成32位的UUid，对应数据库的主键id进行插入用。
     *
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 获取带前缀的32位uuid,作为数据库主键
     *
     * @param prefix
     * @return
     */
    public static String getKey(String prefix) {
        return prefix + UUID.randomUUID().toString().replace("-", "");
    }

}
