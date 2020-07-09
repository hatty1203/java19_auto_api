package com.homework.day21;

import com.homework.constants.Constants;
import com.homework.pojo.CaseInfo;
import com.homework.utils.ExcelUtils;
import com.homework.utils.HttpUtils;
import com.homework.utils.SQLUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


public class RegisterCases extends BaseCase{
    //注册测试用例


    @Test(dataProvider = "datas")
    public void test(CaseInfo caseInfo){
        //参数化
        paramsReplace(caseInfo);
        //前置查询结果
        Object beforeSqlResult = SQLUtils.getSingleResult(caseInfo.getSql());

        HttpResponse response = HttpUtils.call(caseInfo.getMethod(),caseInfo.getUrl(),
                caseInfo.getParams(),caseInfo.getContentType(), Constants.HEAGERS);
        String body = HttpUtils.printResult(response);

        boolean assertResult = assertResponse(body, caseInfo.getExpectRet());
        //创建回写对象
        addWriteBackData(caseInfo.getId(),Constants.REPSONSE_WRITE_BACK_CELLNUM, body);

        //后置查询结果
        Object afterSqlResult = SQLUtils.getSingleResult(caseInfo.getSql());
        //数据库断言
        boolean assertSql = sqlAssert(caseInfo.getSql(), beforeSqlResult, afterSqlResult);

        //断言回写excel
        String assertRet = assertResult && assertSql ? "pass":"fail";
        addWriteBackData(caseInfo.getId(), Constants.ASSERT_WRITE_BACK_CELLNUM, assertRet);
    }

    /**
     * 数据库断言
     * @param sql  查询语句
     * @param beforeSqlResult  数据库前置查询结果
     * @param afterSqlResult  数据库后置查询结果
     * @return
     */
    public boolean sqlAssert(String sql, Object beforeSqlResult, Object afterSqlResult) {
        boolean flag = false;
        if(StringUtils.isNoneBlank(sql)) {
            if (beforeSqlResult == null || afterSqlResult == null) {
                System.out.println("数据库断言失败");
            } else {
                Long l1 = (Long) beforeSqlResult;
                Long l2 = (Long) afterSqlResult;
                if (l1 == 0 && l2 == 1) {
                    flag = true;
                    System.out.println("数据库断言成功");
                } else {
                    System.out.println("数据库断言失败");
                }
            }
        }else {
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
