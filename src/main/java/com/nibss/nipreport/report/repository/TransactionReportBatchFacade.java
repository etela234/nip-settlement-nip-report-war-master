/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.report.repository;

import com.nibss.nipreport.report.entity.TransactionReportBatch;
import javax.ejb.Stateless;

/**
 *
 * @author oogunjimi
 */
@Stateless
public class TransactionReportBatchFacade extends AbstractFacade<TransactionReportBatch> {

    public TransactionReportBatchFacade() {
        super(TransactionReportBatch.class);
    }

    public static TransactionReportBatchFacade instance() {
        return AbstractFacade.instance(TransactionReportBatchFacade.class);
    }
}
