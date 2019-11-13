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
public interface Flag {

    public static final String NEW = "N";
    public static final String ENABLED = "E";
    public static final String OPEN = "O";
    public static final String CLOSED = "C";
    public static final String DISABLED = "D";
    public static final String ACTIVE = "A";
    public static final String INACTIVE = "I";
    public static final String DELETED = "X";
    public static final String TRUE = "T";
    public static final String FALSE = "F";
    public static final String LOCKED = "LK";
    //
    public static final int BUSY = 1;
    public static final int IDLE = 0;
//
    public static final long NIBSS_INSTITUTION_TYPE = 1;
    public static final long BANK_INSTITUTION_TYPE = 2;
    public static final long THIRDPARTY_INSTITUTION_TYPE = 3;

    public void setFlag(String flag);

    public String getFlag();
}
