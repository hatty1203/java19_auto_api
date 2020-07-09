package com.homework.utils;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.homework.constants.Constants;
import com.homework.pojo.CaseInfo;
import com.homework.pojo.WriteBackData;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtils {

    //批量回写集合
    public static List<WriteBackData> wbdList = new ArrayList<>();

    public static Object[] getDatas(int sheetIndex, int sheetNum, Class cls){
        try {
            List list= ExcelUtils.read(sheetIndex, sheetNum, cls);
            Object[] datas = list.toArray();
            return datas;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param sheetIndex  读取第几个sheet
     * @param sheetNum   每次读取的sheet数
     * @param cls
     * @return
     * @throws Exception
     */
    public static List read(int sheetIndex, int sheetNum, Class cls) throws Exception{
        String path = Constants.EXCEL_PATH;
        FileInputStream fis = new FileInputStream(path);

        ImportParams params = new ImportParams();
        params.setStartSheetIndex(sheetIndex);
        params.setSheetNum(sheetNum);

        List caseInfoList = ExcelImportUtil.importExcel(fis, cls, params);
        return caseInfoList;
    }

    public static void writeBack(){
        FileInputStream fis = null;
        FileOutputStream fos = null;

        try{
            fis = new FileInputStream(Constants.EXCEL_PATH);
            Workbook excel = WorkbookFactory.create(fis);

            for (WriteBackData writeBackData : wbdList) {
                //取出sheetIndex,行号,列号,回写内容
                int sheetIndex = writeBackData.getSheetIndex();
                int rownum = writeBackData.getRownum();
                int cellnum = writeBackData.getCellnum();
                String content = writeBackData.getContent();

                Sheet sheet = excel.getSheetAt(sheetIndex);
                Row row = sheet.getRow(rownum);
                Cell cell = row.getCell(cellnum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                cell.setCellValue(content);
            }

            fos = new FileOutputStream(Constants.EXCEL_PATH);
            excel.write(fos);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if(fos != null){
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
