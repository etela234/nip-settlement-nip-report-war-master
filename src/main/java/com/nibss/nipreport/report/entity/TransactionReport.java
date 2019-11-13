/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.report.entity;

import com.nibss.nipreport.admin.entity.Institution;
import java.io.Serializable;
import java.util.Date;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author oogunjimi
 */
@Entity
@Table(name = "transaction_report")
@XmlRootElement
public class TransactionReport implements Serializable, com.nibss.nipreport.model.Entity {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "transaction_report_id")
    private Long transactionReportId;
    @Basic(optional = false)
    @Size(max = 255)
    @Column(name = "report_name")
    private String reportName;
    @Basic(optional = false)
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Basic(optional = false)
    @Column(name = "settlement_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date settlementDate;
    @JoinColumn(name = "institution", referencedColumnName = "institution_id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Institution institution;
    @JoinColumn(name = "transaction_report_batch", referencedColumnName = "transaction_report_batch_id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private TransactionReportBatch transactionReportBatch;

    public Long getTransactionReportId() {
        return transactionReportId;
    }

    public void setTransactionReportId(Long transactionReportId) {
        this.transactionReportId = transactionReportId;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(Date settlementDate) {
        this.settlementDate = settlementDate;
    }

    @XmlTransient
    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
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
        hash += (transactionReportId != null ? transactionReportId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TransactionReport)) {
            return false;
        }
        TransactionReport other = (TransactionReport) object;
        return !((this.transactionReportId == null && other.transactionReportId != null) || (this.transactionReportId != null && !this.transactionReportId.equals(other.transactionReportId)));
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "[ transactionReportId=" + transactionReportId + " ]";
    }

    @XmlTransient
    @Override
    public Object getEntityId() {
        return transactionReportId;
    }

    @XmlElement
    public String getInstitutionName() {
        return institution != null ? institution.getInstitutionName() : null;
    }

    @XmlElement
    public String getReportTypeName() {
        return transactionReportBatch != null ? transactionReportBatch.getTransactionReportType().getReportTypeName() : null;
    }

}
