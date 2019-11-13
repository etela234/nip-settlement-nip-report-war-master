/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.report.repository;

/**
 *
 * @author oogunjimi
 * @param <T>
 */
public abstract class AbstractFacade<T> extends com.nibss.nipreport.admin.repository.AbstractFacade<T> {

    public AbstractFacade(Class<T> entityClass) {
        super(entityClass);
    }

}
