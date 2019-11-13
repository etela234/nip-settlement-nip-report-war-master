/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.report.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author oogunjimi
 */
@Entity
@Table(name = "transaction_response")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TransactionResponse.findAll", query = "SELECT t FROM TransactionResponse t")})
public class TransactionResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "transaction_response_id")
    private Long transactionResponseId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "response_code")
    private String responseCode;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "description")
    private String description;

    public TransactionResponse() {
    }

    public TransactionResponse(Long transactionResponseId) {
        this.transactionResponseId = transactionResponseId;
    }

    public TransactionResponse(Long transactionResponseId, String responseCode, String description) {
        this.transactionResponseId = transactionResponseId;
        this.responseCode = responseCode;
        this.description = description;
    }

    public Long getTransactionResponseId() {
        return transactionResponseId;
    }

    public void setTransactionResponseId(Long transactionResponseId) {
        this.transactionResponseId = transactionResponseId;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (transactionResponseId != null ? transactionResponseId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TransactionResponse)) {
            return false;
        }
        TransactionResponse other = (TransactionResponse) object;
        if ((this.transactionResponseId == null && other.transactionResponseId != null) || (this.transactionResponseId != null && !this.transactionResponseId.equals(other.transactionResponseId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[ transactionResponseId=" + transactionResponseId + " ]";
    }
    
}
