/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.data;

import javax.annotation.PostConstruct;
import javax.el.ELProcessor;

/**
 *
 * @author oogunjimi
 * @param <T>
 */
public abstract class ELData<T> {

    private String[] elVariables;
    private ELProcessor elProcessor;
    private static String DEFAULT_BEAN_NAME = "__";

    public ELData(String... elVariables) {
        this.elVariables = elVariables == null ? new String[0] : elVariables;
    }

    public final String[] getElVariables() {
        return elVariables;
    }

    public Object[] data(T data) {
        if((elVariables==null || elVariables.length==0) && data.getClass().isArray()){
            return (Object[])data;
        }
        Object[] rowdata = new Object[elVariables.length];
        for (int x = 0; x < elVariables.length; x++) {
            String el = elVariables[x];
            if (el != null) {
                getElProcessor().defineBean(DEFAULT_BEAN_NAME, data);
                rowdata[x] = getElProcessor().eval(DEFAULT_BEAN_NAME + "." + el);
            }
        }
        return rowdata;
    }

    public ELProcessor getElProcessor() {
        if (elProcessor == null) {
            elProcessor = new ELProcessor();
        }
        return elProcessor;
    }

    @PostConstruct
    public void destroy() {
        elProcessor = null;
        elVariables = null;
    }
}
