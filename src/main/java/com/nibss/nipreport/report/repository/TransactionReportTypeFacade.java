/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.report.repository;

import com.nibss.nipreport.context.AppUtil;
import com.nibss.nipreport.report.entity.TransactionReportType;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author oogunjimi
 */
@Stateless
public class TransactionReportTypeFacade extends AbstractFacade<TransactionReportType> {

    public TransactionReportTypeFacade() {
        super(TransactionReportType.class);
    }

    public static TransactionReportTypeFacade instance() {
        return AbstractFacade.instance(TransactionReportTypeFacade.class);
    }

    public TransactionReportType findByReportTypeCode(int reportTypeCode) {
        Query query = getEntityManager().createNamedQuery("TransactionReportType.findByReportTypeCode");
        query.setParameter("reportTypeCode", reportTypeCode);
        try {
            return (TransactionReportType) query.getSingleResult();
        } catch (Exception e) {
            AppUtil.log(e);
        }
        return null;
    }
}
