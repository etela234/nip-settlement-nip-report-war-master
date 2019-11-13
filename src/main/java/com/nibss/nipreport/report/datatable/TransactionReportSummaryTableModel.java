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
import com.nibss.nipreport.report.entity.TransactionReportSummary;
import com.nibss.nipreport.report.repository.TransactionReportSummaryFacade;
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
@Path(ReportResource.TRANSACTION_REPORT_SUMMARY + "/datatable")
@Named
@SessionScoped
public class TransactionReportSummaryTableModel extends AbstractTableModel<TransactionReportSummary> {

    @Inject
    private TransactionReportSummaryFacade transactionReportSummaryFacade;

    @PostConstruct
    protected void init() {
        String filterNames[] = {"transactionType.description", "totalAmount", "successAmount", "totalCount", "successCount", "expectedCount", "duplicateCount"};
        String filterIdNames[] = {"transactionType"};
        super.init(filterNames, filterIdNames);
    }

    @Override
    public Exportable export(String exportType, Map<String, SortOrder> sortOrderMap, Map<String, Object> filterMap) throws IOException {
        List<Object[]> data = transactionReportSummaryFacade.search(sortOrderMap, filterMap, getFilterNames());
        if (data.size() > 0) {
            String[] cols = {"Transaction Type", "Total Amount", "Success Amount", "Total Count", "Success Count", "Expected Count", "Duplicate Count"};
            return AppConstant.CSV_EXPORT_TYPE.equalsIgnoreCase(exportType) ? toCSV(data, cols, null) : toExcel(data, cols, null);
        }
        return null;
    }

    @Override
    protected List<TransactionReportSummary> load(int startIndex, int pageSize, Map<String, SortOrder> sortOrderMap, Map<String, Object> filterMap) {
        setRowCount(transactionReportSummaryFacade.count(filterMap));
        return transactionReportSummaryFacade.search(startIndex, pageSize, sortOrderMap, filterMap);
    }

    @Override
    protected Map<String, Object> preFilter(Map<String, Object> filterMap) {
        return filterMap;
    }

}
