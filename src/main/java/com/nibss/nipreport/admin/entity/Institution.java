/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.admin.entity;

import com.nibss.nipreport.model.AbstractFlag;
import com.nibss.nipreport.model.Flag;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author Oluwaseun Ogunjimi
 */
@Entity
@Table(name = "institution")
@XmlRootElement

@NamedQueries({
    @NamedQuery(name = "Institution.findAll", query = "SELECT i FROM Institution i"),
    @NamedQuery(name = "Institution.findInstitutionTypeId", query = "SELECT DISTINCT(i.institutionType.institutionTypeId) FROM Institution i where i.institutionId=:institutionId")})
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorValue("1")
@DiscriminatorColumn(name = "institution_type_id", discriminatorType = DiscriminatorType.INTEGER)
public class Institution extends AbstractFlag implements Serializable, com.nibss.nipreport.model.Entity {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "institution_id")
    private Long institutionId;
    @Basic(optional = false)
    @Size(min = 1, max = 3)
    @NotBlank
    @NotNull
    @Column(name = "institution_code")
    private String institutionCode;
    @Basic(optional = true)
    @Column(name = "nip_institution_code")
    private String nipInstitutionCode;
    @Basic(optional = false)
    @Size(min = 1, max = 150)
    @NotBlank
    @NotNull
    @Column(name = "institution_name")
    private String institutionName;
    @Column(name = "sort_code")
    private String sortCode;
    @Size(max = 5)
    //@NotBlank
    //@NotNull
    @Column(name = "flag", length = 5)
    private String flag = Flag.ENABLED;
    @JoinColumn(name = "institution_type", referencedColumnName = "institution_type_id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private InstitutionType institutionType;

    public Institution() {
    }

    public Long getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(Long institutionId) {
        this.institutionId = institutionId;
    }

    public String getInstitutionCode() {
        return institutionCode;
    }

    public void setInstitutionCode(String institutionCode) {
        this.institutionCode = institutionCode;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public String getNipInstitutionCode() {
        return nipInstitutionCode;
    }

    public void setNipInstitutionCode(String nipInstitutionCode) {
        this.nipInstitutionCode = nipInstitutionCode;
    }

    @Override
    public String getFlag() {
        return flag;
    }

    @Override
    public void setFlag(String flag) {
        this.flag = flag;
    }

    @XmlTransient
    public InstitutionType getInstitutionType() {
        return institutionType;
    }

    public void setInstitutionType(InstitutionType institutionType) {
        this.institutionType = institutionType;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (institutionId != null ? institutionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Institution)) {
            return false;
        }
        Institution other = (Institution) object;
        if ((this.institutionId == null && other.institutionId != null) || (this.institutionId != null && !this.institutionId.equals(other.institutionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getClass().getName() + "[ institutionId=" + institutionId + " ]";
    }

    @XmlTransient
    @Override
    public Object getEntityId() {
        return institutionId;
    }
    @XmlElement
    @Transient
    private Long institutionTypeId;

    public Long getInstitutionTypeId() {
        if (institutionTypeId == null && institutionType != null) {
            return institutionType.getInstitutionTypeId();
        }
        return institutionTypeId;
    }

    public String getInstitutionTypeName() {
        return this.getInstitutionType() != null ? this.getInstitutionType().getInstitutionTypeName() : null;
    }

}
