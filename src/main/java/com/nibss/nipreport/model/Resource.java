/*
 * copyright 2014 Nibss
 * http://www.nibss-plc.com/
 * 
 */
package com.nibss.nipreport.model;

/**
 *
 * @author Oluwaseun Ogunjimi
 */
public interface Resource {

    public static final String CREATE = "create";
    public static final String EDIT = "edit";
    public static final String READONLY = "readonly";
    public static final String VIEW = "view";
    public static final String LIST = "list";
    //
    public static final String ADMIN = "admin";
    public static final String REPORT = "report";
    //
     public static final String CHANGE_PASSWORD = "change-password";

    void setResource(String resource);

    String getResource();
}
