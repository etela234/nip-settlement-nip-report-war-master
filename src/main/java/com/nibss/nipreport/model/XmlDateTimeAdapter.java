/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author Oluwaseun Ogunjimi
 */
public class XmlDateTimeAdapter extends XmlAdapter<String, Date> {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");

    @Override
    public String marshal(Date v) throws Exception {
        return v == null ? null : dateFormat.format(v);
    }

    @Override
    public Date unmarshal(String v) throws Exception {
        return v == null || v.isEmpty() ? null : dateFormat.parse(v);
    }
}
