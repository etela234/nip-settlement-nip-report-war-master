/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.report.repository;

import com.nibss.nipreport.report.entity.TransactionReport;
import javax.ejb.Stateless;

/**
 *
 * @author oogunjimi
 */
@Stateless
public class TransactionReportFacade extends AbstractFacade<TransactionReport> {

    public TransactionReportFacade() {
        super(TransactionReport.class);
    }

    public static TransactionReportFacade instance() {
        return AbstractFacade.instance(TransactionReportFacade.class);
    }
}
