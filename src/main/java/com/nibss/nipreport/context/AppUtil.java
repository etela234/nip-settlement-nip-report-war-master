/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.context;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author oogunjimi
 */
public class AppUtil {

    public static void log(String msg) {
        System.out.println(msg);
    }

    public static void log(Throwable e) {
        if (e != null) {
            e.printStackTrace(System.err);
        }
    }
    public static void log(Class clazz,String msg) {
        System.out.println(msg);
    }

    public static void log(Class clazz,Throwable e) {
        if (e != null) {
            e.printStackTrace(System.err);
        }
    }

    public static boolean isAjaxRequest(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }

    private static SimpleDateFormat fileDateFormat;
    private static SimpleDateFormat fileTimestampFormat;
    private static SimpleDateFormat timestampFormat;

    public static synchronized String formatAsFileDate(Date date) {
        if (date == null) {
            return null;
        }
        if (fileDateFormat == null) {
            fileDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            fileDateFormat.setLenient(false);
        }
        return fileDateFormat.format(date);
    }

    public static synchronized String formatAsFileTimestamp(Date date) {
        if (date == null) {
            return null;
        }
        if (fileTimestampFormat == null) {
            fileTimestampFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
            fileTimestampFormat.setLenient(false);
        }
        return fileTimestampFormat.format(date);
    }

    public static synchronized String formatAsTimestamp(Date date) {
        if (date == null) {
            return null;
        }
        if (timestampFormat == null) {
            timestampFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            timestampFormat.setLenient(false);
        }
        return timestampFormat.format(date);
    }

//    private static SimpleDateFormat dateFormat;
//
//    public static synchronized String formatDate(Date date) {
//        if (date == null) {
//            return null;
//        }
//        if (dateFormat == null) {
//            dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//            dateFormat.setLenient(false);
//        }
//        return dateFormat.format(date);
//    }
}
