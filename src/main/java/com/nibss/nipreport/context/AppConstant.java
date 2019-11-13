/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.context;

/**
 *
 * @author oogunjimi
 */
public interface AppConstant {

    public static final String MAIN_PAGE = "/main.jspx";
    public static final String HOME_PAGE = "WEB-INF/views/home.jspx";
    public static final String ERROR_PAGE = "/WEB-INF/views/error.jspx";
    public static final String HOME_DIR_NAME = "nip-report";
    public static final String REMOTE_IP_PARAM = "com.nibss.nipreport.remoteIP";
    public static final String USER_PARAM = "com.nibss.nipreport.loggedInUser";
    public static final String LAST_ACCESSED_DATE_PARAM = "com.nibss.nipreport.lastAccessedDate";
    public static String MAIL_CONFIG_PARAM = "com.nibss.nipreport.mailConfig";
    public static final String CONTENT_PATH_PARAM = "com.nibss.nipreport.contentPath";
    public final String FILE_DOWNLOAD_COOKIE_NAME = "fileDownload";
    //
    public static String CSV_EXPORT_TYPE = "csv";
    public static String EXCEL_EXPORT_TYPE = "excel";
}
