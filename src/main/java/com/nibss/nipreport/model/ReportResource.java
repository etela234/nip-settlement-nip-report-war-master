/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.model;

/**
 *
 * @author oogunjimi
 */
public interface ReportResource extends Resource {

    public static final String TRANSACTION = "transaction";
    public static final String VIEW_TRANSACTION = "view-transaction";
    public static final String LIST_TRANSACTION = "list-transaction";
    //
    public static final String TRANSACTION_REPORT = "transaction-report";
    public static final String VIEW_TRANSACTION_REPORT = "view-transaction-report";
    public static final String LIST_TRANSACTION_REPORT = "list-transaction-report";
    //
    public static final String TRANSACTION_REPORT_BATCH = "transaction-report-batch";
    public static final String CREATE_TRANSACTION_REPORT_BATCH = "create-transaction-report-batch";
    public static final String VIEW_TRANSACTION_REPORT_BATCH = "view-transaction-report-batch";
    public static final String LIST_TRANSACTION_REPORT_BATCH = "list-transaction-report-batch";

    //
    public static final String TRANSACTION_REPORT_SUMMARY = "transaction-report-summary";
    public static final String LIST_TRANSACTION_REPORT_SUMMARY = "list-transaction-report-summary";

    public static final String SMARTDET = "smartdet";
    public static final String VIEW_SMARTDET = "view-transaction";
    public static final String LIST_SMARTDET = "list-smartdet";

}
