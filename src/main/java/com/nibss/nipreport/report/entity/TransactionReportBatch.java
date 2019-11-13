/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.report.entity;

import com.nibss.nipreport.model.AbstractFlag;
import com.nibss.nipreport.model.XmlDateTimeAdapter;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author oogunjimi
 */
@Entity
@Table(name = "transaction_report_batch",
        uniqueConstraints
        = @UniqueConstraint(columnNames = {"batch_name"}))
@XmlRootElement
public class TransactionReportBatch implements Serializable, com.nibss.nipreport.model.Entity {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "transaction_report_batch_id")
    private Long transactionReportBatchId;
    @Basic(optional = false)
    @Column(name = "batch_name")
    private String batchName;
    @Basic(optional = false)
    @Column(name = "from_date")
    @Temporal(TemporalType.TIMESTAMP)
    @XmlJavaTypeAdapter(XmlDateTimeAdapter.class)
    private Date fromDate;
    @Basic(optional = false)
    @Column(name = "to_date")
    @Temporal(TemporalType.TIMESTAMP)
    @XmlJavaTypeAdapter(XmlDateTimeAdapter.class)
    private Date toDate;
    @Basic(optional = false)
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Basic(optional = false)
    @Column(name = "flag", length = 5)
    private String flag;
    @Basic(optional = false)
    @Column(name = "state")
    private int state;
    @JoinColumn(name = "transaction_report_type", referencedColumnName = "transaction_report_type_id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private TransactionReportType transactionReportType;

    public Long getTransactionReportBatchId() {
        return transactionReportBatchId;
    }

    public void setTransactionReportBatchId(Long transactionReportBatchId) {
        this.transactionReportBatchId = transactionReportBatchId;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @XmlTransient
    public TransactionReportType getTransactionReportType() {
        return transactionReportType;
    }

    public void setTransactionReportType(TransactionReportType transactionReportType) {
        this.transactionReportType = transactionReportType;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Objects.hashCode(this.transactionReportBatchId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TransactionReportBatch other = (TransactionReportBatch) obj;
        return Objects.equals(this.transactionReportBatchId, other.transactionReportBatchId);
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "[ transactionReportBatchId=" + transactionReportBatchId + " ]";
    }

    @XmlTransient
    @Override
    public Object getEntityId() {
        return transactionReportBatchId;
    }

    @XmlElement
    public String getFlagDesc() {
        return AbstractFlag.getFlagName(flag);
    }

    @XmlElement
    public String getStateDesc() {
        return AbstractFlag.getFlagName(state);
    }

    @XmlElement
    public String getTransactionReportTypeDesc() {
        return transactionReportType != null ? transactionReportType.getDescription() : null;
    }
    @Transient
    private Integer transactionReportTypeCode;

    @XmlElement
    public Integer getTransactionReportTypeCode() {
        if (transactionReportTypeCode == null) {
            return transactionReportType != null ? transactionReportType.getReportTypeCode() : null;
        }
        return transactionReportTypeCode;
    }

}
