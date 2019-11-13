/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.report.repository;

import com.nibss.nipreport.report.entity.Transaction;
import javax.ejb.Stateless;

/**
 *
 * @author oogunjimi
 */
@Stateless
public class TransactionFacade extends AbstractFacade<Transaction> {

    public TransactionFacade() {
        super(Transaction.class);
    }

    public static TransactionFacade instance() {
        return AbstractFacade.instance(TransactionFacade.class);
    }
}
