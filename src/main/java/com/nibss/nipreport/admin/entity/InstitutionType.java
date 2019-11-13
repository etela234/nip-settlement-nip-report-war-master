/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.admin.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Oluwaseun Ogunjimi
 */
@Entity
@Table(name = "institution_type")
public class InstitutionType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "institution_type_id")
    private Long institutionTypeId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "institution_type_name")
    private String institutionTypeName;

    public Long getInstitutionTypeId() {
        return institutionTypeId;
    }

    public void setInstitutionTypeId(Long institutionTypeId) {
        this.institutionTypeId = institutionTypeId;
    }

    public String getInstitutionTypeName() {
        return institutionTypeName;
    }

    public void setInstitutionTypeName(String institutionTypeName) {
        this.institutionTypeName = institutionTypeName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (institutionTypeId != null ? institutionTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InstitutionType)) {
            return false;
        }
        InstitutionType other = (InstitutionType) object;
        if ((this.institutionTypeId == null && other.institutionTypeId != null) || (this.institutionTypeId != null && !this.institutionTypeId.equals(other.institutionTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "[ institutionTypeId=" + institutionTypeId + " ]";
    }

}
