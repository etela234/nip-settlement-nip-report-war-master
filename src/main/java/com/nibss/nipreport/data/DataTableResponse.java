/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.data;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author oogunjimi
 * @param <T>
 */
@XmlRootElement
@XmlType(name = "", propOrder = {
    "drawCount",
    "totalRecords",
    "filteredRecords",
    "data",
    "error"
})
public class DataTableResponse<T> {

    @XmlElement(name = "draw", required = true)
    private int drawCount;
    @XmlElement(name = "recordsTotal", required = true)
    private int totalRecords;
    @XmlElement(name = "recordsFiltered", required = true)
    private int filteredRecords;
    @XmlElement(name = "data", required = true)
    private List<T> data;
    @XmlElement(name = "error", required = true)
    private String error;

    public DataTableResponse() {
    }

    public DataTableResponse(int drawCount, int totalRecords, List<T> data) {
        this.drawCount = drawCount;
        this.totalRecords = totalRecords;
        this.filteredRecords = totalRecords;
        this.data = data;
    }

    public int getDrawCount() {
        return drawCount;
    }

    public void setDrawCount(int drawCount) {
        this.drawCount = drawCount;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public int getFilteredRecords() {
        return filteredRecords;
    }

    public void setFilteredRecords(int filteredRecords) {
        this.filteredRecords = filteredRecords;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
