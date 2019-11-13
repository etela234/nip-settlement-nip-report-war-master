/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.report.repository;

import com.nibss.nipreport.report.entity.TransactionReportSummary;
import javax.ejb.Stateless;

/**
 *
 * @author oogunjimi
 */
@Stateless
public class TransactionReportSummaryFacade extends AbstractFacade<TransactionReportSummary> {

    public TransactionReportSummaryFacade() {
        super(TransactionReportSummary.class);
    }

    public static TransactionReportSummaryFacade instance() {
        return AbstractFacade.instance(TransactionReportSummaryFacade.class);
    }
}
