/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.report.datatable;

import com.elixir.commons.web.datamodel.SortOrder;
import com.nibss.nipreport.context.AppConstant;
import com.nibss.nipreport.data.Exportable;
import com.nibss.nipreport.model.ReportResource;
import static com.nibss.nipreport.model.ReportResource.LIST_TRANSACTION_REPORT_BATCH;
import com.nibss.nipreport.report.entity.TransactionReportBatch;
import com.nibss.nipreport.report.repository.TransactionReportBatchFacade;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Path;

/**
 *
 * @author oogunjimi
 */
@RolesAllowed(LIST_TRANSACTION_REPORT_BATCH)
@Path(ReportResource.TRANSACTION_REPORT_BATCH + "/datatable")
@Named
@SessionScoped
public class TransactionReportBatchTableModel extends AbstractTableModel<TransactionReportBatch> {

    @Inject
    private TransactionReportBatchFacade transactionReportBatchFacade;

    @PostConstruct
    protected void init() {
        String filterNames[] = {"transactionReportType.description", "batchName", "createdDate", "fromDate", "toDate"};
        String filterIdNames[] = {"transactionReportType.reportTypeCode"};
        super.init(filterNames, filterIdNames);
    }

    @Override
    public Exportable export(String exportType, Map<String, SortOrder> sortOrderMap, Map<String, Object> filterMap) throws IOException {
        List<Object[]> data = transactionReportBatchFacade.search(sortOrderMap, filterMap, getFilterNames());
        if (data.size() > 0) {
            String[] cols = {"Report Type", "Batch Name", "Created Date", "From Date", "To Date"};
            return AppConstant.CSV_EXPORT_TYPE.equalsIgnoreCase(exportType) ? toCSV(data, cols, null) : toExcel(data, cols, null);
        }
        return null;
    }

    @Override
    protected List<TransactionReportBatch> load(int startIndex, int pageSize, Map<String, SortOrder> sortOrderMap, Map<String, Object> filterMap) {
        setRowCount(transactionReportBatchFacade.count(filterMap));
        return transactionReportBatchFacade.search(startIndex, pageSize, sortOrderMap, filterMap);
    }

    @Override
    protected Map<String, Object> preFilter(Map<String, Object> filterMap) {
        if (!isNibss()) {
            filterMap.put("transactionReportBatchId", -1);
        }
        preDateFilter(filterMap, "createdDate");
        preDateFilter(filterMap, "fromDate");
        preDateFilter(filterMap, "toDate");
        return filterMap;
    }

}
