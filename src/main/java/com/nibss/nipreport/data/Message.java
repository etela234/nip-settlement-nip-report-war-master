/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author oogunjimi
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Message implements Data {

    public static final String SEVERITY_INFO = "info";
    public static final String SEVERITY_WARNING = "warning";
    public static final String SEVERITY_SUCCESS = "success";
    public static final String SEVERITY_DANGER = "danger";
    //
    public static final String SEVERITY_ERROR = "error";
    public static final String SEVERITY_FATAL = "fatal";
    @XmlElement
    private String summary;
    @XmlElement
    private String detail;
    @XmlElement
    private String severity = SEVERITY_INFO;

    public Message() {
    }

    public Message(String summary) {
        this.summary = summary;
    }

    public Message(String summary, String severity) {
        this.summary = summary;
        this.severity = severity;
    }

    public Message(String summary, String detail, String severity) {
        this.summary = summary;
        this.detail = detail;
        this.severity = severity;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

}
