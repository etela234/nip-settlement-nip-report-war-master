/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.data;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Oluwaseun Ogunjimi
 */
public class ReportData<T> {

    private int startIndex;
    private int styleCode;

    private List<T> dataList;

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getStyleCode() {
        return styleCode;
    }

    public void setStyleCode(int styleCode) {
        this.styleCode = styleCode;
    }

    public List<T> getDataList() {
        if (dataList == null) {
            dataList = new ArrayList<T>();
        }
        return dataList;
    }

    public void setDataAt(int index, T data) {
        rangeCheck(index);
        getDataList().set(index, data);
    }

    public void addData(T data) {
        getDataList().add(data);
    }

    public T getData(int index) {
        if (index < 0 || index >= getDataList().size()) {
            return null;
        }
        return getDataList().get(index);
    }

    private void rangeCheck(int index) {
        int size = getDataList().size();
        if (index > 0 && index >= size) {
            for (int i = 0; i < index; i++) {
                if (i >= size) {
                    getDataList().add(null);
                }
            }
        }
    }
}
