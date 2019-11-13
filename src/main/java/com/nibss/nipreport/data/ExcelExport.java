/*
 */
package com.nibss.nipreport.data;

import com.elixir.commons.util.DateUtil;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author elixir
 */
public class ExcelExport<T> extends ELData<T> {

    private Workbook workbook;
    private Sheet currentSheet;
    private int currentRowIndex;
    private Map<Sheet, Integer> sheetMap;
    private final int rows;

    private final static int MAX_ROWS_2003_EXCEL = 65000;

    public ExcelExport() {
        this(0);
    }

    public ExcelExport(String... elVariables) {
        this(0, elVariables);
    }

    public ExcelExport(int rows) {
        this(rows, (String[]) null);
    }

    public ExcelExport(int rows, String... elVariables) {
        super(elVariables);
        this.rows = rows;
    }

    public String getExtension() {
        return getWorkbook() instanceof XSSFWorkbook ? "xlsx" : "xls";
    }

    private Workbook getWorkbook() {
        if (workbook == null) {
            workbook = rows > MAX_ROWS_2003_EXCEL ? new XSSFWorkbook() : new HSSFWorkbook();
        }
        return workbook;
    }

//    public void setCurrentSheet(String sheetName) {
//        if (this.currentSheet != null) {
//            getSheetMap().put(this.currentSheet, currentRowIndex);
//        }
//        Sheet sheet = getWorkbook().getSheet(sheetName);
//        Integer rowIndex=sheet!=null?getSheetMap().get(sheet):0;
//        this.currentSheet=sheet!=null?sheet:getWorkbook().createSheet(sheetName);
//        this.currentRowIndex=rowIndex!=null?rowIndex:0;
//        if (sheet != null) {
//            Integer rowIndex = getSheetMap().get(sheet);
////            if (rowIndex == null) {
////                rowIndex = 0;
////                //getSheetMap().put(sheet, rowIndex);
////            }
//            this.currentSheet = sheet;
//            this.currentRowIndex = (rowIndex == null)?0:rowIndex;
//        } else {
//            this.currentSheet = getWorkbook().createSheet(sheetName);
//            this.currentRowIndex = 0;
//            //getSheetMap().put(this.currentSheet, this.currentRowIndex);
//        }
//        //this.currentSheet = sheet != null ? sheet : getWorkbook().createSheet(sheetName);
//        //getSheetMap().get(sheet);
//        getSheetMap().put(this.currentSheet, currentRowIndex);
//    }
    public void setCurrentSheet(String sheetName) {
        if (this.currentSheet != null) {
            getSheetMap().put(this.currentSheet, currentRowIndex);
        }
        Sheet sheet = getWorkbook().getSheet(sheetName);
        Integer rowIndex = sheet != null ? getSheetMap().get(sheet) : 0;
        this.currentSheet = sheet != null ? sheet : getWorkbook().createSheet(sheetName);
        this.currentRowIndex = rowIndex != null ? rowIndex : 0;
        getSheetMap().put(this.currentSheet, currentRowIndex);
    }

    public String getCurrentSheet() {
        return this.currentSheet == null ? null : this.currentSheet.getSheetName();
    }

    public int getCurrentRowIndex() {
        return currentRowIndex;
    }

    public void setCurrentRowIndex(int currentRowIndex) {
        this.currentRowIndex = currentRowIndex;
    }

    private Sheet currentSheet() {
        if (this.currentSheet == null) {
            setCurrentSheet("report");
        }
        return this.currentSheet;
    }

//    public void writeAllx(List<T> data, String[] cols, String... titles) {
//        writeTitle(titles);
//        currentRowIndex++;
//        int columnTitleIndex = currentRowIndex;
//        if (cols != null && cols.length > 0) {
//            currentRowIndex++;
//        }
//        currentRowIndex++;
//        writeData(data);
//        writeColumnTitle(columnTitleIndex, 18f, cols);
//    }
    public void writeAll(List<T> data, String[] cols, String... titles) {
        if (this.currentSheet == null) {
            currentSheet();
        }
        writeTitle(titles);
        int columnTitleIndex = currentRowIndex++;
        writeData(data);
        writeColumnTitle(columnTitleIndex, 18f, cols);
    }

    public void writeTitle(String... titles) {
        writeTitle(14, 50f, titles);
    }

    private void writeTitle(int mergedCols, float height, String... titles) {
        for (String title : titles) {
            if (title == null) {
                continue;
            }
            Row row = currentSheet().createRow(currentRowIndex);
            Cell cell = row.createCell(0);
            cell.setCellStyle(getTitleStyle());
            cell.setCellValue(title);
            row.setHeightInPoints(height);
            currentSheet().addMergedRegion(new CellRangeAddress(currentRowIndex, currentRowIndex, 0, mergedCols));
            currentRowIndex++;
        }
    }

    public void writeColumnTitle(int index, float height, String... cols) {
        Row row = currentSheet().createRow(index);
        row.setHeightInPoints(height);
        for (int x = 0; x < cols.length; x++) {
            if (cols[x] == null) {
                continue;
            }
            Cell cell = row.createCell(x);
            cell.setCellStyle(getColumnTitleStyle());
            cell.setCellValue(cols[x]);
            currentSheet().autoSizeColumn(x);
        }
    }

    public void writeData(List<T> data) {
        writeData(data, null);
    }

    public void writeData(List<T> data, Integer groupIndex) {
        if (data == null || data.isEmpty()) {
            return;
        }

        for (T t : data) {
            writeCellData(t, groupIndex);
        }
    }

    public void writeData(T[] data, Integer groupIndex) {
        if (data == null || data.length == 0) {
            return;
        }

        for (T t : data) {
            writeCellData(t, groupIndex);
        }
    }

    private void writeCellData(T data, Integer groupIndex) {
        Object[] values = groupIndex == null ? data(data) : data(data, groupIndex);
        if (values == null) {
            if (data instanceof ReportData) {
                writeCellReportData((ReportData) data);
            }
        } else {
            Row row = currentSheet().createRow(currentRowIndex);
            for (int i = 0; i < values.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellStyle(getCellStyle());
                Object value = convertData(values[i]);
                if (value != null) {
                    cell.setCellValue(value.toString());
                }
            }
        }
        currentRowIndex++;
    }

    private void writeCellReportData(ReportData reportData) {
        int startIndex = reportData.getStartIndex();
        List dataList = reportData.getDataList();
        if (dataList != null) {
            Row row = currentSheet().createRow(currentRowIndex);
            for (int i = 0; i < dataList.size(); i++) {
                Cell cell = row.createCell(i + startIndex);
                switch (reportData.getStyleCode()) {
                    case 1:
                        cell.setCellStyle(getColumnTitleStyle());
                        break;
                    case 2:
                        cell.setCellStyle(getHighlightStyle());
                        break;
                    default:
                        cell.setCellStyle(getCellStyle());
                        break;
                }
                Object value = convertData(dataList.get(i));
                if (value != null) {
                    cell.setCellValue(value.toString());
                }
            }
        }
    }

    private Object convertData(Object value) {
        if (value instanceof Date) {
            value = getDateUtil().convertDateToString((Date) value);
        } else if (value instanceof BigDecimal) {
            value = getFormatter().format(((BigDecimal) value));
        }
        return value;
    }

    public Object[] data(T data, int groupIndex) {
        return data(data);
    }

    public void writeTo(OutputStream outStream) throws IOException {
        getWorkbook().write(outStream);
    }
    //**********
    private CellStyle titleStyle = null;
    private CellStyle columnTitleStyle = null;
    private CellStyle cellStyle = null;
    private CellStyle highlightStyle = null;
    public static final int HEADER_STYLE = 1;
    public static final int CELL_HIGHLIGHT_STYLE = 2;

    public Map<Sheet, Integer> getSheetMap() {
        if (sheetMap == null) {
            sheetMap = new HashMap<Sheet, Integer>();
        }
        return sheetMap;
    }

    private CellStyle getTitleStyle() {
        if (titleStyle == null) {
            Font font = getWorkbook().createFont();
            font.setFontHeightInPoints((short) 15);
            font.setFontName("Century Gothic");
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            font.setUnderline(Font.U_SINGLE);

            titleStyle = getWorkbook().createCellStyle();
            titleStyle.setAlignment(CellStyle.ALIGN_LEFT);
            titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            titleStyle.setFont(font);
        }
        return titleStyle;
    }

    private CellStyle getColumnTitleStyle() {
        if (columnTitleStyle == null) {
            Font font = getWorkbook().createFont();
            font.setFontHeightInPoints((short) 11);
            font.setFontName("Century Gothic");
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            //font.setUnderline(Font.U_SINGLE);

            columnTitleStyle = getWorkbook().createCellStyle();
            columnTitleStyle.setAlignment(CellStyle.ALIGN_CENTER);
            columnTitleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            columnTitleStyle.setFont(font);
            columnTitleStyle.setWrapText(true);
            columnTitleStyle.setFillBackgroundColor(HSSFColor.LIGHT_BLUE.index);
        }
        return columnTitleStyle;
    }

    private CellStyle getCellStyle() {
        if (cellStyle == null) {
            Font font = getWorkbook().createFont();
            font.setFontHeightInPoints((short) 10);
            font.setFontName("Century Gothic");
            cellStyle = getWorkbook().createCellStyle();
            cellStyle.setFont(font);
        }
        return cellStyle;
    }

    private CellStyle getHighlightStyle() {
        if (highlightStyle == null) {
            Font font = getWorkbook().createFont();
            font.setFontHeightInPoints((short) 11);
            font.setFontName("Century Gothic");
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            highlightStyle = getWorkbook().createCellStyle();
            highlightStyle.setFont(font);
        }
        return highlightStyle;
    }
    private DateUtil util;

    public DateUtil getDateUtil() {
        if (util == null) {
            util = new DateUtil();
        }
        return util;
    }

    public void reset() {
        workbook = null;
        currentSheet = null;
        currentRowIndex = 0;
        if (sheetMap != null) {
            sheetMap.clear();
        }
    }
    private DecimalFormat formatter;

    private DecimalFormat getFormatter() {
        if (formatter == null) {
            formatter = new DecimalFormat("#,##0.00;(#,##0.00)");
            formatter.setParseBigDecimal(true);
        }
        return formatter;
    }

    public void destroy() {
        super.destroy();
        workbook = null;
        currentSheet = null;
        currentRowIndex = 0;
        if (sheetMap != null) {
            sheetMap.clear();
        }
        sheetMap = null;
        titleStyle = null;
        columnTitleStyle = null;
        cellStyle = null;
        if (util != null) {
            util.destroy();
        }
        util = null;
        formatter = null;
    }
}
