package com.homework.constants;


import java.util.HashMap;
import java.util.Map;

public class Constants {
    public static final String BASE_DIR = System.getProperty("user.dir");
    public static final String EXCEL_PATH = BASE_DIR + "/src/test/resources/cases_v3.xlsx";
    public static final Map<String, String> HEAGERS = new HashMap<>();
    public static final int REPSONSE_WRITE_BACK_CELLNUM = 8;
    public static final int ASSERT_WRITE_BACK_CELLNUM = 10;
    //数据库URL                                 数据库：//ip:port/ DataBase名
    public static final String JDBC_URL = "jdbc:mysql://api.lemonban.com:3306/futureloan?useUnicode=true&characterEncoding=utf-8";
    //数据库用户名
    public static final String JDBC_USERNAME = "future";
    //数据库密码
    public static final String JDBC_PASSWORD = "123456";
}
