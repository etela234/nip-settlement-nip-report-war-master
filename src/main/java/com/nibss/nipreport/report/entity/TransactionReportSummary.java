/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.report.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author oogunjimi
 */
@Entity
@Table(name = "transaction_report_summary")
@XmlRootElement
public class TransactionReportSummary implements Serializable, com.nibss.nipreport.model.Entity {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_report_summary_id")
    private Long transactionReportSummaryId;
    @Basic(optional = false)
    @Column(name = "total_amount")
    private BigDecimal totalAmount;
    @Basic(optional = false)
    @Column(name = "total_count")
    private int totalCount;
    @Basic(optional = false)
    @Column(name = "success_amount")
    private BigDecimal successAmount;
    @Basic(optional = false)
    @Column(name = "success_count")
    private int successCount;
    @Basic(optional = false)
    @Column(name = "expected_count")
    private int expectedCount;
    @Basic(optional = false)
    @Column(name = "failed_count")
    private int failedCount;
    @JoinColumn(name = "transaction_type", referencedColumnName = "transaction_type_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private TransactionType transactionType;
    @JoinColumn(name = "transaction_report_batch", referencedColumnName = "transaction_report_batch_id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private TransactionReportBatch transactionReportBatch;

    public TransactionReportSummary() {
    }

    public TransactionReportSummary(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public Long getTransactionReportSummaryId() {
        return transactionReportSummaryId;
    }

    public void setTransactionReportSummaryId(Long transactionReportSummaryId) {
        this.transactionReportSummaryId = transactionReportSummaryId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getExpectedCount() {
        return expectedCount;
    }

    public void setExpectedCount(int expectedCount) {
        this.expectedCount = expectedCount;
    }

    public BigDecimal getSuccessAmount() {
        return successAmount;
    }

    public void setSuccessAmount(BigDecimal successAmount) {
        this.successAmount = successAmount;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public int getFailedCount() {
        return failedCount;
    }

    public void setFailedCount(int failedCount) {
        this.failedCount = failedCount;
    }

    @XmlTransient
    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    @XmlTransient
    public TransactionReportBatch getTransactionReportBatch() {
        return transactionReportBatch;
    }

    public void setTransactionReportBatch(TransactionReportBatch transactionReportBatch) {
        this.transactionReportBatch = transactionReportBatch;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (transactionReportSummaryId != null ? transactionReportSummaryId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the transactionReportSummaryId fields are not set
        if (!(object instanceof TransactionReportSummary)) {
            return false;
        }
        TransactionReportSummary other = (TransactionReportSummary) object;
        if ((this.transactionReportSummaryId == null && other.transactionReportSummaryId != null) || (this.transactionReportSummaryId != null && !this.transactionReportSummaryId.equals(other.transactionReportSummaryId))) {
            return false;
        }
        return true;
    }

    @XmlTransient
    @Override
    public Object getEntityId() {
        return transactionReportSummaryId;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "[ transactionReportSummaryId=" + transactionReportSummaryId + " ]";
    }

    @XmlElement
    public String getTransactionTypeDesc() {
        return transactionType != null ? transactionType.getDescription() : null;
    }

}
