/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.context;

import com.nibss.nipreport.model.Resource;
import java.util.Set;

/**
 *
 * @author oogunjimi
 */
@javax.ws.rs.ApplicationPath(Resource.REPORT)
public class RestReportModule extends RestApplication {

    @Override
    protected void addClasses(Set<Class<?>> resources) {
        resources.add(com.nibss.nipreport.report.bean.TransactionBean.class);
        resources.add(com.nibss.nipreport.report.bean.TransactionReportBean.class);
        resources.add(com.nibss.nipreport.report.bean.TransactionReportBatchBean.class);
        resources.add(com.nibss.nipreport.report.bean.SmartdetBean.class);
        resources.add(com.nibss.nipreport.report.datatable.TransactionTableModel.class);
        resources.add(com.nibss.nipreport.report.datatable.TransactionReportTableModel.class);
        resources.add(com.nibss.nipreport.report.datatable.TransactionReportBatchTableModel.class);
        resources.add(com.nibss.nipreport.report.datatable.TransactionReportSummaryTableModel.class);
    }
}
