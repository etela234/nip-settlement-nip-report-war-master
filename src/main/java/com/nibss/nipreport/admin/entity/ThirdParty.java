/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.admin.entity;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author oogunjimi
 */
@Entity
@Table(name = "third_party")
@XmlRootElement
@DiscriminatorValue("3")
@NamedQueries({
    @NamedQuery(name = "ThirdParty.findBySettlementBank", query = "SELECT t.institutionId FROM ThirdParty t where t.settlementBank.institutionId = :institutionId")})
public class ThirdParty extends Institution {

    private static final long serialVersionUID = 1L;
//    @NotBlank
//    @NotNull
//    @Size(max = 30)
    @Column(name = "third_party_code")
    private String thirdPartyCode;
    @JoinColumn(name = "settlement_bank", referencedColumnName = "institution_id")
    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    private Bank settlementBank;
    @Column(name = "third_party_type")
    private Integer thirdPartyType;

    public String getThirdPartyCode() {
        return thirdPartyCode;
    }

    public void setThirdPartyCode(String thirdPartyCode) {
        this.thirdPartyCode = thirdPartyCode;
    }

    public Integer getThirdPartyType() {
        return thirdPartyType;
    }

    public void setThirdPartyType(Integer thirdPartyType) {
        this.thirdPartyType = thirdPartyType;
    }

    @XmlTransient
    public Bank getSettlementBank() {
        return settlementBank;
    }

    public void setSettlementBank(Bank settlementBank) {
        this.settlementBank = settlementBank;
    }
    @Transient
    private Long SettlementBankId;

    public Long getSettlementBankId() {
        return SettlementBankId;
    }

    public void setSettlementBankId(Long SettlementBankId) {
        this.SettlementBankId = SettlementBankId;
    }

}
