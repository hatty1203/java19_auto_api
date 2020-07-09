package com.homework.day21;

import com.alibaba.fastjson.JSONPath;
import com.homework.constants.Constants;
import com.homework.pojo.CaseInfo;
import com.homework.utils.Authorization;
import com.homework.utils.ExcelUtils;
import com.homework.utils.HttpUtils;
import com.homework.utils.SQLUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;


public class RechargeCases extends BaseCase{


    @Test(dataProvider = "datas")
    public void test(CaseInfo caseInfo) {
        //参数化
        paramsReplace(caseInfo);

        //前置查询
        Object beforeSqlResult = SQLUtils.getSingleResult(caseInfo.getSql());

        //获取带token的请求头
        Map<String, String> headers = Authorization.getTokenHeader();
        //调用请求接口
        HttpResponse response = HttpUtils.call(caseInfo.getMethod(), caseInfo.getUrl(),
                caseInfo.getParams(), caseInfo.getContentType(), headers);
        String body = HttpUtils.printResult(response);
        //断言响应结果
        boolean assertResult = assertResponse(body, caseInfo.getExpectRet());
        //创建回写对象
        addWriteBackData(caseInfo.getId(), Constants.REPSONSE_WRITE_BACK_CELLNUM, body);

        //后置查询
        Object afterSqlResult = SQLUtils.getSingleResult(caseInfo.getSql());
        //数据库断言
        boolean assertSqlReslut = getSqlAssert(caseInfo, beforeSqlResult, afterSqlResult);

        //断言回写excel
        String assertRet = assertResult && assertSqlReslut ? "pass":"fail";
        addWriteBackData(caseInfo.getId(), Constants.ASSERT_WRITE_BACK_CELLNUM, assertRet);
    }


    public boolean getSqlAssert(CaseInfo caseInfo, Object beforeSqlResult, Object afterSqlResult) {
        boolean flag = false;
        if (StringUtils.isNoneBlank(caseInfo.getSql())) {
            if (beforeSqlResult == null || afterSqlResult == null) {
                System.out.println("数据库断言失败");
            } else {
                BigDecimal b1 = (BigDecimal) beforeSqlResult;
                BigDecimal b2 = (BigDecimal) afterSqlResult;
                BigDecimal ret = b2.subtract(b1);
                String amount = JSONPath.read(caseInfo.getParams(), "$.amount").toString();
                BigDecimal retAmount = new BigDecimal(amount);
                System.out.println(ret);
                System.out.println(retAmount);
                if (ret.compareTo(retAmount) == 0){
                    flag = true;
                    System.out.println("数据库断言成功");
                } else {
                    System.out.println("数据库断言失败");
                }
            }
        } else {
            System.out.println("sql为空，不需要断言");
        }
        return flag;
    }


    @DataProvider
    public Object[] datas(){
        Object[] datas = ExcelUtils.getDatas(sheetIndex,1,CaseInfo.class);
        return datas;
    }

}
