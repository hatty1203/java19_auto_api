package com.homework.pojo;

import cn.afterturn.easypoi.excel.annotation.Excel;

public class CaseInfo {
    //用例编号	用例描述	接口名称	请求方式	url	参数  参数类型  期望结果
    @Excel(name = "用例编号")
    private int id;
    @Excel(name = "接口名称")
    private String name;
    @Excel(name = "url")
    private String url;
    @Excel(name = "请求方式")
    private String method;
    @Excel(name = "参数")
    private String params;
    @Excel(name = "参数类型")
    private String contentType;
    @Excel(name = "期望结果")
    private String expectRet;
    @Excel(name = "sql")
    private String sql;


    public CaseInfo() {
    }

    public CaseInfo(int id, String name, String url, String method, String params, String contentType, String expectRet, String sql) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.method = method;
        this.params = params;
        this.contentType = contentType;
        this.expectRet = expectRet;
        this.sql = sql;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getExpectRet() {
        return expectRet;
    }

    public void setExpectRet(String expectRet) {
        this.expectRet = expectRet;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    @Override
    public String toString() {
        return "CaseInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", params='" + params + '\'' +
                ", contentType='" + contentType + '\'' +
                ", expectRet='" + expectRet + '\'' +
                ", sql='" + sql + '\'' +
                '}';
    }
}
