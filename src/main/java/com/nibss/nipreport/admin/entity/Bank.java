/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.admin.entity;

import com.nibss.nipreport.admin.repository.ThirdPartyFacade;
import com.nibss.nipreport.model.Flag;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Oluwaseun Ogunjimi
 */
@Entity
@Table(name = "bank")
@XmlRootElement
@DiscriminatorValue("2")
@NamedQueries({
    //@NamedQuery(name = "Bank.containsBankCode", query = "SELECT COUNT(b) FROM Bank b where b.bankCode=:bankCode AND b.flag <> '" + Flag.DELETED + "' AND b.institutionId <> :excludeInstitutionId"),
    @NamedQuery(name = "Bank.findAllByExclusion", query = "SELECT b.institutionId,b.institutionName FROM Bank b where b.flag <> '" + Flag.DELETED + "' AND b.institutionId <> :excludeInstitutionId ORDER BY b.institutionName ASC")})
public class Bank extends Institution {

    private static final long serialVersionUID = 1L;
    @Size(max = 30)
    @Column(name = "bank_code")
    private String bankCode;
    @Column(name = "settlement_account_no")
    private String settlementAccountNo;

    public Bank() {
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getSettlementAccountNo() {
        return settlementAccountNo;
    }

    public void setSettlementAccountNo(String settlementAccountNo) {
        this.settlementAccountNo = settlementAccountNo;
    }

    //
    @Transient
    private List<Long> settlementInstitutionList;

    @XmlTransient
    public List<Long> getSettlementInstitutionList() {
        if (settlementInstitutionList == null) {
            settlementInstitutionList=ThirdPartyFacade.instance().findBySettlementBank(getInstitutionId());
        }
        return settlementInstitutionList;
    }

}
