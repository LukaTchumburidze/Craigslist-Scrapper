package com.craigslist;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.*;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

public class DBSaver {

    private static XSSFWorkbook Output = null;
    private static File OutputFile;
    public static String Name;
    private static HashMap<String, String[]> SheetData = new HashMap<String, String[]>();

    public DBSaver (String name) {
        Name = Variables.ExcelFilePath + name + ".xlsx";

        ReadTheOutputFile();
    }

    public void UpdateData (LinkedList<String[]> Data, int ind) {
        XSSFSheet CurSheet = Output.getSheetAt(0);
        WriteData (Data, CurSheet, ind);
        AutoSizeColumns (3, CurSheet);

        try {
            OutputFile = new File (Name);
            FileOutputStream OutStream = new FileOutputStream(OutputFile);
            Output.write(OutStream);
            OutStream.close();
        } catch (Exception e) {

        }
    }

    private void ReadTheOutputFile () {
        try {
            OutputFile = new File (Name);
            FileInputStream InStream = new FileInputStream (OutputFile);
            Output = new XSSFWorkbook(InStream);
            XSSFSheet Sheet = Output.getSheetAt(0);
            for (int i = 1; i < Sheet.getLastRowNum(); i ++) {
                XSSFRow row = Sheet.getRow(i + 1);
                String[] Val = {row.getCell(0).getStringCellValue(), row.getCell(1).getStringCellValue()};
                SheetData.put(row.getCell(2).getStringCellValue(), Val);
            }
            InStream.close();
        } catch (IOException e) {
            Output = new XSSFWorkbook();
            XSSFSheet CurSheet = Output.createSheet();
            FillHeader (CurSheet);
        }
    }

    private void WriteData (LinkedList<String[]> Data, XSSFSheet Sheet, int ind) {
        int CurRowNumber = Sheet.getLastRowNum() + 1;
        for (int i = 0; i < Data.size(); i ++) {
            if (!SheetData.containsKey(Data.get(i)[2])) {
                String[] CurRowVal = Data.get(i);
                String[] Val = {CurRowVal[0], CurRowVal[1]};
                SheetData.put(CurRowVal[2], Val);

                XSSFRow CurRow = Sheet.createRow(CurRowNumber);
                CurRowNumber ++;
                for (int j = 0; j < 3; j ++) {
                    XSSFCell Cell = CurRow.createCell(j);
                    Cell.setCellValue(CurRowVal[j]);
                }

            }
        }
    }

    private void FillHeader (XSSFSheet Sheet) {
        XSSFCellStyle Style = Output.createCellStyle();
        Style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        Style.setFillForegroundColor (new XSSFColor(Variables.HeaderColor));
        Style.setLeftBorderColor(new XSSFColor(new Color(255, 255, 255)));
        Style.setBorderLeft(CellStyle.BORDER_THIN);
        XSSFRow HeadRow = Sheet.createRow(0);
        XSSFCell Cell_1 = HeadRow.createCell(0);
        Cell_1.setCellValue("Name");
        Cell_1.setCellStyle(Style);
        XSSFCell Cell_2 = HeadRow.createCell(1);
        Cell_2.setCellValue("URL");
        Cell_2.setCellStyle(Style);
        XSSFCell Cell_3 = HeadRow.createCell(2);
        Cell_3.setCellValue("Phone");
        Cell_3.setCellStyle(Style);
    }

    private void AutoSizeColumns (int NofColumns, XSSFSheet Sheet) {
        for (int i = 0; i < NofColumns; i ++) {
            Sheet.autoSizeColumn(i);
        }
    }
}

