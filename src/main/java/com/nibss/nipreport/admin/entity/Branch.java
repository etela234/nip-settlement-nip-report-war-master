/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.admin.entity;

import com.nibss.nipreport.model.AbstractFlag;
import com.nibss.nipreport.model.Flag;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Oluwaseun Ogunjimi
 */
@Entity
@Table(name = "branch")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Branch.findAll", query = "SELECT b FROM Branch b"),
    @NamedQuery(name = "Branch.containsBranchCode", query = "SELECT COUNT(b) FROM Branch b where b.branchCode=:branchCode AND b.institution.institutionId=:institutionId AND b.flag <> '" + Flag.DELETED + "' AND b.branchId <> :excludeBranchId"),
    @NamedQuery(name = "Branch.findByInstitutionId", query = "SELECT b.branchId,b.branchName,b.branchCode FROM Branch b where b.institution.institutionId=:institutionId AND b.flag = '" + Flag.ENABLED + "' ORDER BY b.branchName ASC"),
    @NamedQuery(name = "Branch.findByInstitutionIdExempt", query = "SELECT b.branchId,b.branchName,b.branchCode FROM Branch b where b.institution.institutionId=:institutionId AND b.branchId <> :branchId AND b.flag <> '" + Flag.DELETED + "' ORDER BY b.branchName ASC")
})
public class Branch extends AbstractFlag implements Serializable, com.nibss.nipreport.model.Entity {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "branch_id")
    private Long branchId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "branch_name")
    private String branchName;

    @Basic(optional = false)
    //@NotNull
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Basic(optional = false)
    //@NotNull
    //@Size(min = 1, max = 5)
    @Column(name = "flag", length = 5)
    private String flag;

    @Size(max = 150)
    @Column(name = "location")
    private String location;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "branch_code")
    private String branchCode;
    @JoinColumn(name = "institution", referencedColumnName = "institution_id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Institution institution;
    @XmlTransient
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "branch", fetch = FetchType.LAZY)
    private List<UserIdentity> userIdentityList;

    public Branch() {
    }

    public Branch(Long branchId) {
        this.branchId = branchId;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    @XmlTransient
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String getFlag() {
        return flag;
    }

    @Override
    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    @XmlTransient
    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (branchId != null ? branchId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Branch)) {
            return false;
        }
        Branch other = (Branch) object;
        if ((this.branchId == null && other.branchId != null) || (this.branchId != null && !this.branchId.equals(other.branchId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[ branchId=" + branchId + " ]";
    }

    @XmlTransient
    @Override
    public Object getEntityId() {
        return branchId;
    }

    @Transient
    private Long institutionId;

    public Long getInstitutionId() {
        return institutionId;
    }

    @XmlElement
    public String getInstitutionName() {
        return getInstitution() != null ? getInstitution().getInstitutionName() : null;
    }

}
