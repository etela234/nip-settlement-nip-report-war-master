/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.report.entity;

import com.nibss.nipreport.admin.entity.Institution;
import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author oogunjimi
 */
@Entity
@Table(name = "transaction", uniqueConstraints
        = @UniqueConstraint(columnNames = {"session_id", "transaction_date", "amount", "response_code", "destination_account_number", "transaction_type"}))
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Transaction.findAll", query = "SELECT t FROM Transaction t")})
public class Transaction implements Serializable, com.nibss.nipreport.model.Entity {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "transaction_id")
    private Long transactionId;
//    @Size(max = 90)
//    @Column(name = "batch_id")
//    private String batchId;
//    @Size(max = 90)
//    @Column(name = "mandate_code")
//    private String mandateCode;
    @Basic(optional = true)
    @Column(name = "payment_reference")
    private String paymentReference;
    @Basic(optional = true)
    @Column(name = "originator_name")
    private String originatorName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "session_id")
    private String sessionId;
    @Basic(optional = true)
    @Column(name = "narration")
    private String narration;
    @Basic(optional = true)
    @Column(name = "source_account_name")
    private String sourceAccountName;
    @Basic(optional = true)
    @Column(name = "source_account_number")
    private String sourceAccountNumber;
    @JoinColumn(name = "source_institution", referencedColumnName = "institution_id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Institution sourceInstitution;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "destination_account_name")
    private String destinationAccountName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "destination_account_number")
    private String destinationAccountNumber;
    @JoinColumn(name = "destination_institution", referencedColumnName = "institution_id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Institution destinationInstitution;
    @Basic(optional = true)
    @Column(name = "flag")
    private String flag;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "amount")
    private BigDecimal amount;
    @Basic(optional = true)
    @Column(name = "fee")
    private BigDecimal fee;
    @Basic(optional = false)
    @NotNull
    @Column(name = "transaction_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date transactionDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "settlement_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date settlementDate;
    @JoinColumn(name = "channel", referencedColumnName = "channel_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Channel channel;
    @JoinColumn(name = "transaction_response", referencedColumnName = "transaction_response_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private TransactionResponse transactionResponse;
    @JoinColumn(name = "transaction_type", referencedColumnName = "transaction_type_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private TransactionType transactionType;
    @Basic(optional = true)
    @Column(name = "response_code")
    private String responseCode;
    @JoinColumn(name = "transaction_report_batch", referencedColumnName = "transaction_report_batch_id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private TransactionReportBatch transactionReportBatch;

    public Transaction() {
    }

    public Transaction(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public String getOriginatorName() {
        return originatorName;
    }

    public void setOriginatorName(String originatorName) {
        this.originatorName = originatorName;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getSourceAccountName() {
        return sourceAccountName;
    }

    public void setSourceAccountName(String sourceAccountName) {
        this.sourceAccountName = sourceAccountName;
    }

    public String getSourceAccountNumber() {
        return sourceAccountNumber;
    }

    public void setSourceAccountNumber(String sourceAccountNumber) {
        this.sourceAccountNumber = sourceAccountNumber;
    }

    public String getDestinationAccountName() {
        return destinationAccountName;
    }

    public void setDestinationAccountName(String destinationAccountName) {
        this.destinationAccountName = destinationAccountName;
    }

    public String getDestinationAccountNumber() {
        return destinationAccountNumber;
    }

    public void setDestinationAccountNumber(String destinationAccountNumber) {
        this.destinationAccountNumber = destinationAccountNumber;
    }

    @XmlTransient
    public Institution getSourceInstitution() {
        return sourceInstitution;
    }

    public void setSourceInstitution(Institution sourceInstitution) {
        this.sourceInstitution = sourceInstitution;
    }

    @XmlTransient
    public Institution getDestinationInstitution() {
        return destinationInstitution;
    }

    public void setDestinationInstitution(Institution destinationInstitution) {
        this.destinationInstitution = destinationInstitution;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Date getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(Date settlementDate) {
        this.settlementDate = settlementDate;
    }

    @XmlTransient
    public TransactionResponse getTransactionResponse() {
        return transactionResponse;
    }

    public void setTransactionResponse(TransactionResponse transactionResponse) {
        this.transactionResponse = transactionResponse;
    }

    @XmlTransient
    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    @XmlTransient
    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
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
        hash += (transactionId != null ? transactionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Transaction)) {
            return false;
        }
        Transaction other = (Transaction) object;
        if ((this.transactionId == null && other.transactionId != null) || (this.transactionId != null && !this.transactionId.equals(other.transactionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "[ transactionId=" + transactionId + " ]";
    }

    @XmlTransient
    @Override
    public Object getEntityId() {
        return transactionId;
    }

    public String getSourceInstitutionName() {
        return sourceInstitution == null ? null : sourceInstitution.getInstitutionName();
    }

    public String getDestinationInstitutionName() {
        return destinationInstitution == null ? null : destinationInstitution.getInstitutionName();
    }

    public String getResponseDesc() {
        return transactionResponse == null ? null : transactionResponse.getDescription();
    }

    @XmlElement
    public String getTransactionTypeDesc() {
        return transactionType != null ? transactionType.getDescription() : null;
    }

    @XmlElement
    public String getChannelDesc() {
        return channel != null ? channel.getDescription() : null;
    }
}
