/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.report.datatable;

import com.nibss.nipreport.model.Entity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author oogunjimi
 * @param <T>
 */
public abstract class AbstractTableModel<T extends Entity> extends com.nibss.nipreport.admin.datatable.AbstractTableModel<T> {

    private SimpleDateFormat dateFormat;

    public SimpleDateFormat getDateFormat() {
        if (dateFormat == null) {
            dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            dateFormat.setLenient(false);
        }
        return dateFormat;
    }

    protected void preDateFilter(Map<String, Object> filterMap, String dateField) {
        Object value = filterMap.get(dateField);
        if (value != null && !value.toString().isEmpty()) {
            try {
                Date date = getDateFormat().parse(value.toString());
                filterMap.remove(dateField);
                filterMap.put(dateRange(date, date, dateField), null);
            } catch (ParseException ex) {
                Logger.getLogger(AbstractTableModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
