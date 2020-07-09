package com.homework.utils;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;

public class SQLUtils {


    /**
     *获取SQL查询结果
     * @param sql 查询语句
     * @return  查询结果
     */
    public static Object getSingleResult(String sql) {
        if(StringUtils.isBlank(sql)){
            System.out.println("sql为空");
            return null;
        }
        Object result = null;
        QueryRunner runner = new QueryRunner();
        Connection coon = JDBCUtils.getConnection();

        try {
            ScalarHandler handler = new ScalarHandler();
            result = runner.query(coon, sql, handler);

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(coon);
        }
        return result;
    }

    public static void mapHandler() {
        QueryRunner runner = new QueryRunner();
        Connection coon = JDBCUtils.getConnection();

        try {
            String sql = "SELECT";
            MapHandler handler = new MapHandler();
            runner.query(coon, sql, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(coon);
        }
    }
}
