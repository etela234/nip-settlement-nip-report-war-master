/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.report.entity;

import com.nibss.nipreport.model.Flag;
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
@Table(name = "transaction_report_type")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TransactionReportType.findByReportTypeCode", query = "SELECT t FROM TransactionReportType t where t.reportTypeCode=:reportTypeCode AND t.flag <> '" + Flag.DELETED + "'")})
public class TransactionReportType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "transaction_report_type_id")
    private Long transactionReportTypeId;
    @Basic(optional = false)
    @Column(name = "report_type_code")
    private int reportTypeCode;
    @Basic(optional = false)
    @NotNull
    @Column(name = "report_type_name")
    private String reportTypeName;
    @Basic(optional = false)
    @Size(max = 255)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @Column(name = "flag", length = 5)
    private String flag;
    @Basic(optional = true)
    @Column(name = "group_type", length = 5)
    private String groupType;

    public Long getTransactionReportTypeId() {
        return transactionReportTypeId;
    }

    public void setTransactionReportTypeId(Long transactionReportTypeId) {
        this.transactionReportTypeId = transactionReportTypeId;
    }

    public int getReportTypeCode() {
        return reportTypeCode;
    }

    public void setReportTypeCode(int reportTypeCode) {
        this.reportTypeCode = reportTypeCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getReportTypeName() {
        return reportTypeName;
    }

    public void setReportTypeName(String reportTypeName) {
        this.reportTypeName = reportTypeName;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

  

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (transactionReportTypeId != null ? transactionReportTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the reportDescId fields are not set
        if (!(object instanceof TransactionReportType)) {
            return false;
        }
        TransactionReportType other = (TransactionReportType) object;
        if ((this.transactionReportTypeId == null && other.transactionReportTypeId != null) || (this.transactionReportTypeId != null && !this.transactionReportTypeId.equals(other.transactionReportTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "[ transactionReportTypeId=" + transactionReportTypeId + " ]";
    }

}
