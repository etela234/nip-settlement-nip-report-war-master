/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.report.datatable;

import com.elixir.commons.web.datamodel.SortOrder;
import com.nibss.nipreport.admin.repository.AbstractFacade;
import com.nibss.nipreport.context.AppConstant;
import com.nibss.nipreport.data.Exportable;
import static com.nibss.nipreport.model.ReportResource.LIST_TRANSACTION;
import static com.nibss.nipreport.model.ReportResource.TRANSACTION;
import com.nibss.nipreport.report.entity.Transaction;
import com.nibss.nipreport.report.repository.TransactionFacade;
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
@RolesAllowed(LIST_TRANSACTION)
@Path(TRANSACTION + "/datatable")
@Named
@SessionScoped
public class TransactionTableModel extends AbstractTableModel<Transaction> {

    @Inject
    private TransactionFacade transactionFacade;

    @PostConstruct
    protected void init() {
        String filterNames[] = {"sessionId", "channel.channelName", "transactionType.description", "sourceInstitution.institutionName", "destinationInstitution.institutionName", "destinationAccountNumber", "destinationAccountName", "amount", "transactionDate", "paymentReference", "narration", "responseCode", "transactionResponse.description"};
        super.init(filterNames, null);
        this.setLoadable(false);
    }

    @Override
    public Exportable export(String exportType, Map<String, SortOrder> sortOrderMap, Map<String, Object> filterMap) throws IOException {
        List<Object[]> data = transactionFacade.search(sortOrderMap, filterMap, getFilterNames());
        if (data.size() > 0) {
            String[] cols = {"Session ID", "Channel", "Transaction Type", "Source Institution", "Destination Institution", "Destination Account Number", "Destination Account Name", "Amount", "Transaction Date", "Payment Reference", "Narration", "Response Code", "Response Description"};
            return AppConstant.CSV_EXPORT_TYPE.equalsIgnoreCase(exportType) ? toCSV(data, cols, null) : toExcel(data, cols, null);
        }
        return null;
    }

    @Override
    protected List<Transaction> load(int startIndex, int pageSize, Map<String, SortOrder> sortOrderMap, Map<String, Object> filterMap) {
        setRowCount(transactionFacade.count(filterMap));
        return transactionFacade.search(startIndex, pageSize, sortOrderMap, filterMap);
    }

    @Override
    public Map<String, Object> preFilter(Map<String, Object> filterMap) {
        if (!isNibss()) {
            Long institutionId = this.getLoggedInUser().getBranch().getInstitution().getInstitutionId();
            filterMap.put(String.format("(sourceInstitution.institutionId = %d OR destinationInstitution.institutionId = %d)", institutionId, institutionId), null);
        }
        formatDateFilter(filterMap, "transactionDate");
        orFilterMap(filterMap, "sourceInstitution.institutionName", "sourceInstitution.institutionCode");
        orFilterMap(filterMap, "destinationInstitution.institutionName", "destinationInstitution.institutionCode");
        return filterMap;
    }

    private void orFilterMap(Map<String, Object> filterMap, String key, String subKey) {
        Object value = filterMap.get(key);
        if (value != null && !value.toString().isEmpty()) {
            filterMap.remove(key);
            filterMap.put("(" + AbstractFacade.sqlLike(key, value.toString()) + "OR" + AbstractFacade.sqlLike(subKey, value.toString()) + ")", null);
        }
    }

}
