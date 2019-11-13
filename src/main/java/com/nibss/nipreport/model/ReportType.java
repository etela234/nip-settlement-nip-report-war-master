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
public interface ReportType {

    public static final int INWARDS = 1;
    public static final int OUTWARDS = 2;
    public static final int INWARDS_SUCCESSFUL = 3;
    public static final int OUTWARDS_SUCCESSFUL = 4;
    public static final int INWARDS_UNSUCCESSFUL = 5;
    public static final int OUTWARDS_UNSUCCESSFUL = 6;
    public static final int INWARDS_LOG = 7;
    public static final int OUTWARDS_LOG = 8;
    public static final int DAILY_ACTIVITY = 9;
    public static final int SETTLEMENT = 10;
    public static final int BILLING = 11;
    //
    public static final String SINGLE_GROUP = "S";
    public static final String BATCH_GROUP = "B";

}
