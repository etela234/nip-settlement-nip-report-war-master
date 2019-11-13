/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.data;

import com.elixir.commons.util.DateUtil;
import com.opencsv.CSVWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

/**
 *
 * @author oogunjimi
 * @param <T>
 */
public class CSVExport<T> extends ELData<T> {

    private CSVWriter csvWriter;
    private Writer writer;

    public CSVExport() {
        this(new StringWriter());
    }

    public CSVExport(String... elVariables) {
        this(new StringWriter(), elVariables);
    }

    public CSVExport(Writer writer) {
        this(writer, (String[]) null);
    }

    public CSVExport(Writer writer, String... elVariables) {
        super(elVariables);
        this.writer = writer;
    }

    private CSVWriter getWriter() {
        if (csvWriter == null) {
            csvWriter = new CSVWriter(writer);
        }
        return csvWriter;
    }

    public void writeAll(List<T> data, String[] cols, String... titles) {
        writeTitle(titles);
        if (titles != null && titles.length > 0) {
            writeNewLine();
        }
        writeColumnTitle(cols);
        writeData(data);
    }

    private void writeNewLine() {
        getWriter().writeNext(new String[]{});
    }

    public void writeTitle(String... titles) {
        for (String title : titles) {
            if (title == null) {
                continue;
            }
            getWriter().writeNext(new String[]{title});
        }
    }

    public void writeColumnTitle(String... cols) {
        if (cols != null && cols.length > 0) {
            getWriter().writeNext(cols);
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
            String[] rowData = new String[values.length];
            for (int i = 0; i < values.length; i++) {
                Object value = convertData(values[i]);
                rowData[i] = (value != null) ? value.toString() : "";
            }
            this.getWriter().writeNext(rowData);
        }
    }

    private void writeCellReportData(ReportData reportData) {
        List dataList = reportData.getDataList();
        if (dataList != null) {
            String[] rowData = new String[dataList.size()];
            for (int i = 0; i < dataList.size(); i++) {
                Object value = convertData(dataList.get(i));
                rowData[i] = (value != null) ? value.toString() : "";
            }
            this.getWriter().writeNext(rowData);
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

    public void close() throws IOException {
        if (csvWriter != null) {
            csvWriter.close();
        }
    }

    public Object[] data(T data, int groupIndex) {
        return data(data);
    }

    //
    private DateUtil util;

    public DateUtil getDateUtil() {
        if (util == null) {
            util = new DateUtil();
        }
        return util;
    }

    public void reset() {
    }
    private DecimalFormat formatter;

    private DecimalFormat getFormatter() {
        if (formatter == null) {
            formatter = new DecimalFormat("#,##0.00;(#,##0.00)");
            formatter.setParseBigDecimal(true);
        }
        return formatter;
    }
}
