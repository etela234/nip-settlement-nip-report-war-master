/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.data;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author oogunjimi
 */
@XmlAccessorType(XmlAccessType.NONE)
public class DataResponse<T extends Data> {

    @XmlElement
    private List<Message> messages;
    @XmlElement
    private boolean valid;
    @XmlElement
    private List<T> data;

    public DataResponse() {
    }

    public DataResponse(boolean valid) {
        this.valid = valid;
    }

    public DataResponse(boolean valid, String... msgs) {
        this.valid = valid;
        if (msgs != null) {
            for (String msg : msgs) {
                addMessage(new Message(msg, valid ? Message.SEVERITY_INFO : Message.SEVERITY_ERROR));
            }
        }
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    //
    public final void addMessage(Message msg) {
        if (msg == null) {
            return;
        }
        if (messages == null) {
            messages = new ArrayList<Message>();
        }
        messages.add(msg);
    }

    public void addData(T dataEntry) {
        if (dataEntry == null) {
            return;
        }
        if (data == null) {
            data = new ArrayList<T>();
        }
        data.add(dataEntry);
    }

    public boolean hasMessages() {
        return messages != null && !messages.isEmpty();
    }
}
