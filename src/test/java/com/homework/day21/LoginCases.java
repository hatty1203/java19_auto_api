package com.homework.day21;

import com.homework.constants.Constants;
import com.homework.pojo.CaseInfo;
import com.homework.utils.Authorization;
import com.homework.utils.ExcelUtils;
import com.homework.utils.HttpUtils;
import org.apache.http.HttpResponse;
import org.testng.annotations.*;


public class LoginCases extends BaseCase{
    //登录测试用例


    @Test(dataProvider = "datas")
    public void test(CaseInfo caseInfo){
        //参数化
        paramsReplace(caseInfo);

        HttpResponse response = HttpUtils.call(caseInfo.getMethod(),caseInfo.getUrl(),
                caseInfo.getParams(),caseInfo.getContentType(),Constants.HEAGERS);
        String body = HttpUtils.printResult(response);
        //提取响应体中的token和member_id，并放入VARS中
        Authorization.json2Vars(body,"$.data.token_info.token","${token}");
        Authorization.json2Vars(body,"$.data.id","${member_id}");

        //断言
        boolean assertResult = assertResponse(body, caseInfo.getExpectRet());

        //创建回写对象
        addWriteBackData(caseInfo.getId(),Constants.REPSONSE_WRITE_BACK_CELLNUM, body);

        //断言回写excel
        String assertRet = assertResult ? "pass":"fail";
        addWriteBackData(caseInfo.getId(), Constants.ASSERT_WRITE_BACK_CELLNUM, assertRet);

    }


    @DataProvider
    public Object[] datas(){
        Object[] datas = ExcelUtils.getDatas(sheetIndex,1,CaseInfo.class);
        return datas;
    }

}
