package com.zhangs.common.base;

import com.jfinal.core.Controller;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jie on 2017/4/3.
 * 通用Controller
 * 2.获取查询条件封装
 * 3.插入操作日志
 */
public class BaseController extends Controller {

    /**
     * 返回成功
     *
     * @param msg 成功信息
     */
    protected void renderSuccess(String msg) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("result", Boolean.TRUE);
        result.put("msg", msg);
        renderJson(result);
    }

    /**
     * 返回成功
     */
    protected void renderSuccess() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("result", Boolean.TRUE);
        renderJson(result);
    }

    /**
     * 返回失败
     *
     * @param msg 失败描述
     */
    protected void renderFailed(String msg) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("result", Boolean.FALSE);
        result.put("msg", msg);
        renderJson(result);
    }

    /**
     * 返回失败
     */
    protected void renderFailed() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("result", Boolean.FALSE);
        renderJson(result);
    }

    /**
     * 返回查询对象
     *
     * @return 属性 符号 值的数组
     */
    protected Object[] getQueryParams() {
        List<String> properties = new ArrayList<String>();
        List<String> symbols = new ArrayList<String>();
        List<Object> values = new ArrayList<Object>();

        Map<String, String[]> paraMap = getParaMap();
        for (Map.Entry<String, String[]> entry : paraMap.entrySet()) {
            String prefix = "queryParams[";
            if (entry.getKey().startsWith(prefix)) {
                String field = entry.getKey().substring(prefix.length(), paraMap.entrySet().size() - 1);
                String symbol = "=";
                String value = entry.getValue()[0];

                //处理范围参数
                if (field.startsWith("_start_")) {
                    field = field.replaceAll("^_start_", "");
                    symbol = ">=";
                } else if (field.startsWith("_end_")) {
                    field = field.replaceAll("^_end_", "");
                    symbol = "<=";
                }

                //模糊搜索处理
                if (value.startsWith("*") && value.endsWith("*")) {
                    value = "%" + value.substring(1, value.length() - 1) + "%";
                    symbol = "like";
                } else if (value.startsWith("*")) {
                    value = "%" + value.substring(1);
                    symbol = "like";
                } else if (value.endsWith("*")) {
                    value = value.substring(0, value.length() - 1) + "%";
                    symbol = "like";
                }

                properties.add(field);
                symbols.add(symbol);
                values.add(value);
            }
        }
        return new Object[]{properties.toArray(new String[]{}), symbols.toArray(new String[]{}), values.toArray(new Object[]{})};
    }

    /**
     * 获取OrderBySQL
     *
     * @return
     */
    protected String getOrderBy() {
        StringBuilder sqlOrderBy = new StringBuilder();
        Map<String, String[]> paraMap = getParaMap();
        if (paraMap.get("sort") != null && paraMap.get("sort").length > 0) {
            String[] sort = paraMap.get("sort")[0].split(",");
            String[] order = paraMap.get("order")[0].split(",");
            sqlOrderBy.append(sort[0]).append(" ").append(order[0]);
            for (int i = 1; i < sort.length; i++) {
                sqlOrderBy.append(", ").append(sort[i]).append(" ").append(order[i]);
            }
        }
        return sqlOrderBy.toString();
    }

    /**
     * 增加操作日志
     *
     * @param opContent 操作内容
     */
    protected void addOpLog(String opContent) {
    }

    /**
     * 获取Subject 对象
     *
     * @return Subject
     */
    public Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    /**
     * 获取shiro session
     *
     * @return Session
     */
    public Session getShiroSession() {
        Subject subject = getSubject();
        return null != subject && null != subject.getSession() ? subject.getSession() : null;
    }

    /**
     * 获取当前用户名
     *
     * @return username
     */
    public String getUsername() {
        Object principal = getSubject().getPrincipal();
        return principal.toString();
    }
}


