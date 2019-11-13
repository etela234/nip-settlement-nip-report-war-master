/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nibss.nipreport.data;

/**
 *
 * @author elixir
 */
public class MessageException extends Exception {

    private Message message;

    /**
     * Constructs an instance of <code>MessageException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public MessageException(String msg) {
        this(new Message(msg, Message.SEVERITY_ERROR));
    }

    public MessageException(Message msg) {
        super(msg.getSummary());
        message = msg;
    }

    public MessageException(String msg, Throwable cause) {
        super(msg, cause);
        message = new Message(msg, Message.SEVERITY_ERROR);
    }

    public Message message() {
        return message;
    }

}
