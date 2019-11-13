/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.report.datatable;

import com.elixir.commons.web.datamodel.SortOrder;
import com.nibss.nipreport.admin.entity.Bank;
import com.nibss.nipreport.admin.entity.Institution;
import com.nibss.nipreport.admin.repository.AbstractFacade;
import com.nibss.nipreport.admin.repository.ThirdPartyFacade;
import com.nibss.nipreport.context.AppConfig;
import com.nibss.nipreport.context.AppConstant;
import com.nibss.nipreport.context.AppUtil;
import com.nibss.nipreport.data.Exportable;
import com.nibss.nipreport.model.Flag;
import static com.nibss.nipreport.model.ReportResource.LIST_TRANSACTION_REPORT;
import static com.nibss.nipreport.model.ReportResource.TRANSACTION_REPORT;
import com.nibss.nipreport.model.ReportType;
import com.nibss.nipreport.report.entity.TransactionReport;
import com.nibss.nipreport.report.repository.TransactionReportFacade;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import org.apache.poi.util.IOUtils;

/**
 *
 * @author oogunjimi
 */
@RolesAllowed(LIST_TRANSACTION_REPORT)
@Path(TRANSACTION_REPORT + "/datatable")
@Named
@SessionScoped
public class TransactionReportTableModel extends AbstractTableModel<TransactionReport> {

    @Inject
    private TransactionReportFacade transactionReportFacade;

    @PostConstruct
    protected void init() {
        String filterNames[] = {"institution", "reportName", "settlementDate", "transactionReportBatch.transactionReportType.reportTypeName"};
        super.init(filterNames, null);
        //cachedFilterMap().put("settlementDate", getDateFormat().format(new Date()));
    }

    @Override
    public Exportable export(String exportType, Map<String, SortOrder> sortOrderMap, Map<String, Object> filterMap) throws IOException {
        List<Object[]> data = transactionReportFacade.search(sortOrderMap, filterMap, getFilterNames());
        if (data.size() > 0) {
            String[] cols = {"Institution", "Report Name", "Settlement Date", "Report Type"};
            return AppConstant.CSV_EXPORT_TYPE.equalsIgnoreCase(exportType) ? toCSV(data, cols, null) : toExcel(data, cols, null);
        }
        return null;
    }

    @Override
    protected List<TransactionReport> load(int startIndex, int pageSize, Map<String, SortOrder> sortOrderMap, Map<String, Object> filterMap) {
        setRowCount(transactionReportFacade.count(filterMap));
        return transactionReportFacade.search(startIndex, pageSize, sortOrderMap, filterMap);
    }

    @Override
    protected Map<String, Object> preFilter(Map<String, Object> filterMap) {
        if (!isNibss()) {
            Institution institution = this.getLoggedInUser().getBranch().getInstitution();
            if (institution instanceof Bank) {
                Bank bank = (Bank) institution;
                String inQuery = inQuery(bank.getSettlementInstitutionList().toArray());
                inQuery = inQuery == null ? String.valueOf(bank.getInstitutionId()) : inQuery + "," + bank.getInstitutionId();
                filterMap.put("institution.institutionId IN (" + inQuery + ")", null);
            } else {
                filterMap.put("institution.institutionId", this.getLoggedInUser().getBranch().getInstitution().getInstitutionId());
            }
        }

        filterMap.put("transactionReportBatch.flag = '" + Flag.CLOSED + "'", null);
        formatDateFilter(filterMap, "settlementDate");

        return filterMap;
    }

    @POST
    @GET
    @Path("download/{transactionReportId}")
    @Produces("application/octet-stream")
    public Response download(@PathParam("transactionReportId") Long transactionReportId) {
        try {

            if (transactionReportId == null) {
                throw new IllegalArgumentException();
            }
            TransactionReport transReport = transactionReportFacade.find(transactionReportId);
            if (transReport == null) {
                throw new IllegalArgumentException();
            }
            String reportDir = transReport.getTransactionReportBatch().getTransactionReportType().getReportTypeCode() == ReportType.BILLING
                    ? AppConfig.instance().getBillingReportDir() : AppConfig.instance().getSettlementReportDir();
            if (reportDir == null) {
                throw new IllegalArgumentException();
            }
//String fileName = settlementReportDir + File.separator + AppUtil.formatAsFileDate(transReport.getSettlementDate()) + File.separator + transReport.getReportName();
            File file = new File(new File(reportDir, AppUtil.formatAsFileDate(transReport.getSettlementDate()) + File.separator + AppUtil.formatAsFileTimestamp(transReport.getCreatedDate())), transReport.getReportName());
            if (!file.exists()) {
                log("file=" + file.getAbsolutePath());
                throw new IllegalArgumentException();
            }
            String mimeType = "application/octet-stream";

            FileInputStream is = new FileInputStream(file);
            byte[] stream = IOUtils.toByteArray(is);
            if (stream == null) {
                throw new IllegalArgumentException();
            }
            String reportName = transReport.getReportName();
            return Response.ok(stream, mimeType)
                    .header("Content-Disposition", "attachment; filename=\"" + reportName + "\"")
                    .cookie(new NewCookie(AppConstant.FILE_DOWNLOAD_COOKIE_NAME, "true", "/", null, null, -1, false))
                    .build();
        } catch (IOException | IllegalArgumentException e) {
            log(e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}
