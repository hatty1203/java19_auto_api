package com.homework.day21;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.homework.constants.Constants;
import com.homework.pojo.CaseInfo;
import com.homework.pojo.WriteBackData;
import com.homework.utils.Authorization;
import com.homework.utils.ExcelUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

import java.io.FileInputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class BaseCase {

    private static Logger logger = Logger.getLogger(BaseCase.class);

    public int sheetIndex;

    /**
     * 在所有用例执行前，设置基础请求头
     */
    @BeforeSuite
    public void init() throws Exception {
        Constants.HEAGERS.put("X-Lemonban-Media-Type", "lemonban.v2");
        Constants.HEAGERS.put("Content-Type", "application/json");

        //创建prop对象，读取properties文件中的数据
        Properties prop = new Properties();
        String path = BaseCase.class.getClassLoader().getResource("./params.properties").getPath();
        FileInputStream fis = new FileInputStream(path);
        prop.load(fis);
        fis.close();
        //把prop中的内容放入VARS
        Authorization.VARS.putAll((Map)prop);
//        System.out.println("===="+Authorization.VARS);
    }

    @AfterSuite
    public void finish(){
        ExcelUtils.writeBack();
    }

    @BeforeClass
    @Parameters({"sheetIndex"})
    public void beforeClass(int sheetIndex){
        this.sheetIndex = sheetIndex;
    }

    /**
     * 创建回写对象，添加到批量回写集合中
     * @param rownum
     * @param cellnum
     * @param body
     */
    public void addWriteBackData(int rownum,int cellnum, String body) {
        WriteBackData wbd =
                new WriteBackData(sheetIndex, rownum, cellnum, body);
        //添加到回写集合
        ExcelUtils.wbdList.add(wbd);
    }

    /**
     * 断言
     * @param body
     * @param expectRet
     * @return
     */
    public boolean assertResponse(String body, String expectRet) {
        Map<String, Object> map = JSONObject.parseObject(expectRet, Map.class);
        boolean assertFlag = true;
        for (String expression : map.keySet()) {
            Object expectValue = map.get(expression);
            Object actualValue = JSONPath.read(body, expression);
            if(expectValue ==null && actualValue != null){
                assertFlag = false;
                break;
            }
            if(expectValue == null && actualValue == null){
                continue;
            }
            if(!expectValue.equals(actualValue)){
                assertFlag = false;
            }
        }
        System.out.println(assertFlag);
        return assertFlag;
    }

    /**
     *参数化替换
     * @param caseInfo
     */
    public void paramsReplace(CaseInfo caseInfo) {
        Set<String> keyset = Authorization.VARS.keySet();
        //遍历VARS中的参数
        for (String key : keyset) {
            String value = Authorization.VARS.get(key).toString();    //转成String
            if(StringUtils.isNoneBlank(caseInfo.getSql())) {
                //替换参数的占位符为实际值
                String sql = caseInfo.getSql().replace(key, value);
                //把替换后的值放回caseInfo对象中
                caseInfo.setSql(sql);
            }
            if(StringUtils.isNoneBlank(caseInfo.getParams())) {
                String params = caseInfo.getParams().replace(key, value);
                caseInfo.setParams(params);
            }
            if(StringUtils.isNoneBlank(caseInfo.getExpectRet())) {
                String exceptRet = caseInfo.getExpectRet().replace(key, value);
                caseInfo.setExpectRet(exceptRet);
            }

        }
    }
}
