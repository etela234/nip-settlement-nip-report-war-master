/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.admin.datatable;

import com.elixir.commons.util.DateUtil;
import com.elixir.commons.web.datamodel.PageDataModel;
import com.elixir.commons.web.datamodel.SortOrder;
import com.nibss.nipreport.admin.entity.UserIdentity;
import com.nibss.nipreport.annotation.LoggedIn;
import com.nibss.nipreport.context.AppConstant;
import com.nibss.nipreport.context.AppUtil;
import com.nibss.nipreport.data.CSVExport;
import com.nibss.nipreport.data.DataTableRequest;
import com.nibss.nipreport.data.DataTableRequest.Order;
import com.nibss.nipreport.data.DataTableResponse;
import com.nibss.nipreport.data.ExcelExport;
import com.nibss.nipreport.data.Exportable;
import com.nibss.nipreport.model.Entity;
import com.nibss.nipreport.model.Flag;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

/**
 *
 * @author oogunjimi
 * @param <T>
 */
public abstract class AbstractTableModel<T extends Entity> extends PageDataModel<T> implements Serializable {

    @Inject
    @LoggedIn
    private UserIdentity loggedInUser;
    @Inject
    private HttpServletRequest request;
    private boolean loadable = true;
    private boolean refresh;
    private boolean filterCollapsed;
    private boolean filterClosed;
    private Map<String, SortOrder> cachedSortOrderMap;
    private Map<String, Object> cachedFilterMap;

    private String[] filterNames;
    private String[] filterIdNames;

    protected void init(String[] filterNames, String[] filterIdNames) {
        this.filterNames = filterNames;
        this.filterIdNames = filterIdNames;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    protected UserIdentity getLoggedInUser() {
        return loggedInUser;
    }

    public boolean isLoadable() {
        return loadable;
    }

    public void setLoadable(boolean loadable) {
        this.loadable = loadable;
    }

    public boolean isRefresh() {
        return refresh;
    }

    public void setRefresh(boolean refresh) {
        this.refresh = refresh;
    }

    public boolean isFilterCollapsed() {
        return filterCollapsed;
    }

    @PUT
    @Path("filter-collapsed/{state}")
    public void setFilterCollapsed(@PathParam("state") boolean filterCollapsed) {
        this.filterCollapsed = filterCollapsed;
    }

    public boolean isFilterClosed() {
        return filterClosed;
    }

    public void setFilterClosed(boolean filterClosed) {
        this.filterClosed = filterClosed;
    }

    public String[] getFilterNames() {
        return filterNames;
    }

    public void setFilterNames(String[] filterNames) {
        this.filterNames = filterNames;
    }

    public String[] getFilterIdNames() {
        return filterIdNames;
    }

    public void setFilterIdNames(String[] filterIdNames) {
        this.filterIdNames = filterIdNames;
    }

    public Map<String, SortOrder> getCachedSortOrderMap() {
        return cachedSortOrderMap;
    }

    public Map<String, Object> getCachedFilterMap() {
        return cachedFilterMap;
    }

    public Map<String, Object> cachedFilterMap() {
        if(cachedFilterMap==null)
            cachedFilterMap=new HashMap<>();
        return cachedFilterMap;
    }
    @Override
    protected List<T> load(int first, int pageSize) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    protected Exportable export(String exportType, Map<String, SortOrder> sortOrderMap, Map<String, Object> filterMap) throws IOException {
        return null;
    }

    @POST
    @GET
    @Path("export")
    @Produces({"application/vnd.ms-excel", "text/csv"})
    public Response export(@QueryParam("exportType") String exportType) {
        try {
            String mimeType = AppConstant.CSV_EXPORT_TYPE.equalsIgnoreCase(exportType) ? "text/csv" : "application/vnd.ms-excel";
            Exportable export = export(exportType, this.getCachedSortOrderMap(), preFilter(getMutableFilterMap()));
            if (export == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("No Data Found to be Exported").build();
            }
            byte[] stream = export.getBytes();
            if (stream == null) {
                throw new IllegalArgumentException();
            }
            String ext = export.getExtension() != null ? export.getExtension() : (AppConstant.CSV_EXPORT_TYPE.equalsIgnoreCase(exportType) ? "csv" : "xls");
            String name = this.getClass().getSimpleName().replaceFirst("TableModel", "").toLowerCase();
            return Response.ok(stream, mimeType)
                    .header("Content-Disposition", "attachment; filename=\"nip-" + name + "-list." + ext + "\"")
                    .cookie(new NewCookie(AppConstant.FILE_DOWNLOAD_COOKIE_NAME, "true", "/", null, null, -1, false))
                    .build();
        } catch (IOException | IllegalArgumentException e) {
            log(e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

//    protected Exportable toExcel(List data, String[] cols, Integer flagIndex, String... title) throws IOException {
//        TableExcelReport excelReport = new TableExcelReport(data != null ? data.size() : 0);
//        excelReport.setFlagIndex(flagIndex);
//        excelReport.writeAll(data, cols, title);
//        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
//        excelReport.writeTo(outStream);
//        return new Exportable(outStream.toByteArray(), excelReport.getExtension());
//    }
//
//    protected Exportable toCSV(List data, String[] cols, Integer flagIndex, String... title) throws IOException {
//        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
//        OutputStreamWriter writer = new OutputStreamWriter(outStream);
//        TableCSVReport csvReport = new TableCSVReport(writer);
//        csvReport.setFlagIndex(flagIndex);
//        csvReport.writeAll(data, cols, title);
//        csvReport.close();
//        return new Exportable(outStream.toByteArray(), AppConstant.CSV_EXPORT_TYPE);
//    }
    protected Exportable toExcel(List data, String[] cols, String[] rowELData, String... title) throws IOException {
        ExcelExport excelExport = new ExcelExport(data != null ? data.size() : 0, rowELData);
        excelExport.writeAll(data, cols, title);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        excelExport.writeTo(outStream);
        excelExport.destroy();
        return new Exportable(outStream.toByteArray(), excelExport.getExtension());
    }

    protected Exportable toCSV(List data, String[] cols, String[] rowELData, String... title) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outStream);
        CSVExport csvExport = new CSVExport(writer, rowELData);
        csvExport.writeAll(data, cols, title);
        csvExport.close();
        csvExport.destroy();
        return new Exportable(outStream.toByteArray(), AppConstant.CSV_EXPORT_TYPE);
    }

    @GET
    @POST
    @Path("load")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    @Produces(MediaType.APPLICATION_JSON)
    public DataTableResponse<T> load(DataTableRequest dataRequest, @Context HttpServletRequest request) {
        List<T> dataList;
        if (loadable) {
            Map<String, Object> filterMap = toFilterMap(dataRequest.getFilters());
            Map<String, SortOrder> sortOrderMap = toSortOrder(dataRequest);
            int pageLength = dataRequest.getPageLength() > 1 ? dataRequest.getPageLength() : this.getPageSize();
            if ((cachedFilterMap != null && filterMap != null && filterMap.equals(cachedFilterMap)) || (cachedSortOrderMap != null && sortOrderMap != null && sortOrderMap.equals(cachedSortOrderMap))) {
                reset(pageLength);
                setRefresh(true);
            }
            this.cachedSortOrderMap = sortOrderMap;
            this.cachedFilterMap = filterMap;
            //this.cachedDataTableRequest = dataRequest;
            dataList = this.fetchPage(dataRequest.getStartIndex(), pageLength, sortOrderMap, preFilter(getMutableFilterMap())).getData();
        } else {
            this.setLoadable(true);
            reset(this.getPageSize());
            dataList = Collections.EMPTY_LIST;
        }
        setRefresh(false);
        return new DataTableResponse<>(dataRequest.getDrawCount(), this.getRowCount(), dataList);
    }

    protected Map<String, SortOrder> toSortOrder(DataTableRequest dataRequest) {//List<Order> orderList
        if (dataRequest.getOrder() == null || dataRequest.getColumns() == null) {
            return null;
        }
        Map<String, SortOrder> sortOrderList = new HashMap<>();
        for (Order order : dataRequest.getOrder()) {
            if (order == null) {
                continue;
            }
            int columnIndex = order.getColumnIndex();
            for (int x = 0; x < dataRequest.getColumns().size(); x++) {
                DataTableRequest.Column column = dataRequest.getColumns().get(x);
                if (column != null && columnIndex == x) {
                    String name = column.getName();
                    name = (name == null || name.trim().isEmpty()) ? column.getData() : name;
                    SortOrder sortOrder = SortOrder.ASCENDING.getOrderName().equals(order.getDir()) ? SortOrder.ASCENDING : (SortOrder.DESCENDING.getOrderName().equals(order.getDir()) ? SortOrder.DESCENDING : null);
                    if (name != null && !name.isEmpty() && sortOrder != null) {
                        sortOrderList.put(name, sortOrder);
                    }
                    break;
                }
            }
        }
        return sortOrderList;
    }

    protected Map<String, Object> getMutableFilterMap() {
        return cachedFilterMap != null ? new HashMap<>(cachedFilterMap) : new HashMap<String, Object>();
    }

    protected Map<String, Object> preFilter(Map<String, Object> filterMap) {
        return filterMap;
    }

    protected Map<String, Object> toFilterMap(Map<String, Object> filterMap) {
        Map<String, Object> map = new HashMap<>();
        if (filterMap != null) {
            for (Map.Entry<String, Object> entry : filterMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (filterable(map, key, value) && key != null && !key.trim().isEmpty() && (value == null || !value.toString().trim().isEmpty())) {
                    map.put(key, value);
                }
            }
        }
        if (filterIdNames != null) {
            for (String key : filterIdNames) {
                toIdValue(map, key);
            }
        }
        return map;
    }

    protected boolean filterable(Map filterMap, String key, Object value) {
        if (filterMap != null && key != null && key.equals("flag")) {
            filterMap.put("flag <> '" + Flag.DELETED + "'", null);
        }
        return true;
    }

    protected void toIdValue(Map<String, Object> map, String key) {
        if (key != null && map != null) {
            Object val = map.get(key);
            if (val != null) {
                try {
                    map.put(key, Long.valueOf(val.toString()));
                } catch (NumberFormatException e) {
                    map.remove(key);
                }
            }
        }
    }

    public boolean isNibss() {
        try {
            return getLoggedInUser().getBranch().getInstitution().getInstitutionType().getInstitutionTypeId().equals(Flag.NIBSS_INSTITUTION_TYPE);
        } catch (Exception e) {
            log(e);
        }
        return false;
    }

    protected void log(String msg) {
        AppUtil.log(msg);
    }

    protected void log(Throwable e) {
        AppUtil.log(e);
    }

    private static SimpleDateFormat dbDateFormat;

    private synchronized static String dbDateFormat(Date date) {
        if (date == null) {
            return null;
        }
        if (dbDateFormat == null) {
            dbDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        return dbDateFormat.format(date);
    }
    private SimpleDateFormat dateFormat;

    public SimpleDateFormat getDateFormat() {
        if (dateFormat == null) {
            dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            dateFormat.setLenient(false);
        }
        return dateFormat;
    }

    protected void formatDateFilter(Map<String, Object> filterMap, String dateField) {
        Object value = filterMap.get(dateField);
        if (value != null && !value.toString().isEmpty()) {
            try {
                filterMap.remove(dateField);
                String[] dateValue = value.toString().split("-");
                if (dateValue.length == 2) {
                    Date fromDate = getDateFormat().parse(dateValue[0]);
                    Date toDate = getDateFormat().parse(dateValue[1]);
                    filterMap.put(dateRange(fromDate, toDate, dateField), null);
                }
            } catch (ParseException ex) {
                log(ex);
            }
        }
    }

    public String dateRange(Date fromDateFilter, Date toDateFilter, String key) {
        if (key == null) {
            key = "";
        }
        if (fromDateFilter != null && toDateFilter != null) {
            Date from = fromDateFilter.before(toDateFilter) ? fromDateFilter : toDateFilter;
            Date to = toDateFilter.after(fromDateFilter) ? toDateFilter : fromDateFilter;
            if (from.equals(to)) {
                DateUtil dateUtil = new DateUtil();
                to = dateUtil.trimToEOD(to);
            }
            return key + " between '" + dbDateFormat(from) + "' and '" + dbDateFormat(to) + "'";
        } else if (fromDateFilter != null) {
            return key + " >= '" + dbDateFormat(fromDateFilter) + "'";
        } else if (toDateFilter != null) {
            return key + " <= '" + dbDateFormat(toDateFilter) + "'";
        }
        return null;
    }

    protected String inQuery(Object... fields) {
        if (fields == null || fields.length==0) {
            return null;
        }
        StringBuilder sb = null;
        for (Object field : fields) {
            sb = (sb == null) ? new StringBuilder() : sb.append(",");
            if (!(field instanceof Number)) {
                sb.append("'");
                sb.append(field);
                sb.append("'");
            } else {
                sb.append(field);
            }
        }
        return sb == null ? null : sb.toString();
    }
}
