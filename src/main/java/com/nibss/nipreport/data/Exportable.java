/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.data;

/**
 *
 * @author oogunjimi
 */
public class Exportable {

    private byte[] bytes;
    private String extension;
    private String mimeType;

    public Exportable(byte[] bytes) {
        this.bytes = bytes;
    }

    public Exportable(byte[] bytes, String extension) {
        this.bytes = bytes;
        this.extension = extension;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

}
