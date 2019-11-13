/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.ws.rs.FormParam;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.jboss.resteasy.annotations.Form;

/**
 *
 * @author oogunjimi
 */
@XmlRootElement
@XmlType(name = "", propOrder = {
    "drawCount",
    "startIndex",
    "pageLength",
    "order",
    "search",
    "columns",
    "filters"
})
public class DataTableRequest {

    @XmlElement(name = "draw", required = true)
    private int drawCount;
    @XmlElement(name = "start", required = true)
    private int startIndex;
    @XmlElement(name = "length", required = true)
    private int pageLength;//
    @XmlElement(name = "order", required = true)
    private List<Order> order;
    @XmlElement(name = "search", required = true)
    private Search search;
    @XmlElement(name = "columns", required = true)
    private List<Column> columns;
    @XmlElement(name = "filters")
    private Map<String, Object> filters;

    public DataTableRequest() {
    }

    public int getDrawCount() {
        return drawCount;
    }

    public void setDrawCount(int drawCount) {
        this.drawCount = drawCount;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getPageLength() {
        return pageLength;
    }

    public void setPageLength(int pageLength) {
        this.pageLength = pageLength;
    }

    public List<Order> getOrder() {
        return order;
    }

    public void setOrder(List<Order> order) {
        this.order = order;
    }

    public Search getSearch() {
        return search;
    }

    public void setSearch(Search search) {
        this.search = search;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public Map<String, Object> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, Object> filters) {
        this.filters = filters;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "columnIndex",
        "dir"
    })
    public static class Order {

        @XmlElement(name = "column", required = true)
        private int columnIndex;
        @XmlElement(name = "dir", required = true)
        private String dir;

        public int getColumnIndex() {
            return columnIndex;
        }

        public void setColumnIndex(int columnIndex) {
            this.columnIndex = columnIndex;
        }

        public String getDir() {
            return dir;
        }

        public void setDir(String dir) {
            this.dir = dir;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 11 * hash + this.columnIndex;
            hash = 11 * hash + Objects.hashCode(this.dir);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Order other = (Order) obj;
            if (this.columnIndex != other.columnIndex) {
                return false;
            }
            return Objects.equals(this.dir, other.dir);
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "name",
        "data",
        "searchable",
        "orderable",
        "search"
    })
    public static class Column {

        @XmlElement(name = "name", required = true)
        private String name;
        @XmlElement(name = "data", required = true)
        private String data;
        @XmlElement(name = "searchable", required = true)
        private boolean searchable;
        @XmlElement(name = "orderable", required = true)
        private boolean orderable;
        @XmlElement(name = "search", required = true)
        private Search search;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public boolean isSearchable() {
            return searchable;
        }

        public void setSearchable(boolean searchable) {
            this.searchable = searchable;
        }

        public boolean isOrderable() {
            return orderable;
        }

        public void setOrderable(boolean orderable) {
            this.orderable = orderable;
        }

        public Search getSearch() {
            return search;
        }

        public void setSearch(Search search) {
            this.search = search;
        }

    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "regex",
        "value"
    })
    public static class Search {

        @XmlElement(name = "regex", required = true)
        private boolean regex;
        @XmlElement(name = "value", required = true)
        private String value;

        public boolean isRegex() {
            return regex;
        }

        public void setRegex(boolean regex) {
            this.regex = regex;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }
}
