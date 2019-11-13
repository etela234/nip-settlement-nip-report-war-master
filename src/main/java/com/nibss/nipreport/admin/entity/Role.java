/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.admin.entity;

import com.nibss.nipreport.model.Flag;
import java.io.Serializable;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Oluwaseun Ogunjimi
 */
@Entity
@Table(name = "role")
//@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Role.findAll", query = "SELECT r FROM Role r"),
   // @NamedQuery(name = "Role.findByInstitutionTypeId", query = "SELECT r.roleId,r.roleName,r.parentRoleId FROM Role r where (r.institutionType IS NULL OR r.institutionType.institutionTypeId=:institutionTypeId) AND r.flag = '" + Flag.ENABLED + "'"),
    @NamedQuery(name = "Role.findByInstitutionTypeId", query = "SELECT r FROM Role r where (r.institutionType IS NULL OR r.institutionType.institutionTypeId=:institutionTypeId) AND r.flag = '" + Flag.ENABLED + "'"),
    @NamedQuery(name = "Role.findNameByRoleIds", query = "SELECT DISTINCT(r.roleName) FROM Role r where r.roleId IN :roleIds AND (r.institutionType IS NULL OR r.institutionType.institutionTypeId=:institutionTypeId) AND r.flag = '" + Flag.ENABLED + "'"),
    @NamedQuery(name = "Role.findByRoleIds", query = "SELECT DISTINCT(r) FROM Role r where r.roleId IN :roleIds AND (r.institutionType IS NULL OR r.institutionType.institutionTypeId=:institutionTypeId) AND r.flag = '" + Flag.ENABLED + "'"),
    @NamedQuery(name = "Role.findIdByRoleNames", query = "SELECT DISTINCT(r.roleId) FROM Role r where r.roleName IN :roleNames AND (r.institutionType IS NULL OR r.institutionType.institutionTypeId=:institutionTypeId) AND r.flag = '" + Flag.ENABLED + "'")})
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "role_id")
    private Long roleId;
    @Basic(optional = true)
    @Column(name = "parent_role_id")
    private Long parentRoleId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "flag")
    private String flag;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "role_name")
    private String roleName;
    @Column(name = "description")
    private String description;
    @JoinColumn(name = "institution_type", referencedColumnName = "institution_type_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private InstitutionType institutionType;

    public Role() {
    }

    public Role(Long roleId) {
        this.roleId = roleId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getParentRoleId() {
        return parentRoleId;
    }

    public void setParentRoleId(Long parentRoleId) {
        this.parentRoleId = parentRoleId;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public InstitutionType getInstitutionType() {
        return institutionType;
    }

    public void setInstitutionType(InstitutionType institutionType) {
        this.institutionType = institutionType;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roleId != null ? roleId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Role)) {
            return false;
        }
        Role other = (Role) object;
        if ((this.roleId == null && other.roleId != null) || (this.roleId != null && !this.roleId.equals(other.roleId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "[ roleId=" + roleId + " ]";
    }

}
