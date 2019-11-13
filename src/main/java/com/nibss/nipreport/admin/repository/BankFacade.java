/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.admin.repository;

import com.nibss.nipreport.admin.entity.Bank;
import javax.ejb.Stateless;

/**
 *
 * @author oogunjimi
 */
@Stateless
public class BankFacade extends AbstractFacade<Bank> {

    public BankFacade() {
        super(Bank.class);
    }

    public static BankFacade instance() {
        return AbstractFacade.instance(BankFacade.class);
    }

}
