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
 * @author Oluwaseun Ogunjimi
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class FieldMessage extends Message {

    @XmlElement
    private String fieldId;

    public FieldMessage() {

    }

    public FieldMessage(String fieldId, String summary, String severity) {
        super(summary, severity);
        this.fieldId = fieldId;
    }

    public FieldMessage(String fieldId, String summary, String detail, String severity) {
        super(summary, detail, severity);
        this.fieldId = fieldId;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

}
