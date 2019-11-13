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
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
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

/**
 *
 * @author Oluwaseun Ogunjimi
 */
@Entity
@Table(name = "user_role")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserRole.findAll", query = "SELECT u FROM UserRole u"),
    @NamedQuery(name = "UserRole.containsUserRoleName", query = "SELECT COUNT(u) FROM UserRole u where u.userRoleName=:userRoleName AND u.institutionType.institutionTypeId=:institutionTypeId AND u.flag <> '" + Flag.DELETED + "' AND u.userRoleId <> :excludeUserRoleId"),
    @NamedQuery(name = "UserRole.findByInstitutionTypeId", query = "SELECT u.userRoleId,u.userRoleName FROM UserRole u where u.institutionType.institutionTypeId=:institutionTypeId AND u.flag <> '" + Flag.DELETED + "'"),
    @NamedQuery(name = "UserRole.findByInstitutionTypeIdExemptAdmin", query = "SELECT u.userRoleId,u.userRoleName FROM UserRole u where u.institutionType.institutionTypeId=:institutionTypeId AND (u.admin IS NULL OR u.admin <> '" + Flag.TRUE + "') AND u.flag <> '" + Flag.DELETED + "'")})
public class UserRole extends AbstractFlag implements Serializable, com.nibss.nipreport.model.Entity {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "user_role_id")
    private Long userRoleId;
    @Basic(optional = false)
    //@NotNull
    // @Size(min = 1, max = 5)
    @Column(name = "flag", length = 5)
    private String flag;
    //@XmlTransient
    @Basic(optional = true)
    @Size(min = 1, max = 5)
    @Column(name = "admin_flag", length = 5)
    private String admin;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "user_role_name")
    private String userRoleName;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "role_config")
    private String roleConfig;

    @JoinColumn(name = "institution_type", referencedColumnName = "institution_type_id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private InstitutionType institutionType;

    public UserRole() {
    }

    public UserRole(Long userRoleId) {
        this.userRoleId = userRoleId;
    }

    public UserRole(Long userRoleId, String flag) {
        this.userRoleId = userRoleId;
        this.flag = flag;
    }

    public Long getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(Long userRoleId) {
        this.userRoleId = userRoleId;
    }

    //@XmlTransient
    @Override
    public String getFlag() {
        return flag;
    }

    @Override
    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getRoleConfig() {
        return roleConfig;
    }

    public void setRoleConfig(String roleConfig) {
        this.roleConfig = roleConfig;
        config = null;
    }

//    @XmlTransient
//    public String getAdmin() {
//        return admin;
//    }

    public InstitutionType getInstitutionType() {
        return institutionType;
    }

    public void setInstitutionType(InstitutionType institutionType) {
        this.institutionType = institutionType;
    }

    public String getUserRoleName() {
        return userRoleName;
    }

    public void setUserRoleName(String userRoleName) {
        this.userRoleName = userRoleName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userRoleId != null ? userRoleId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserRole)) {
            return false;
        }
        UserRole other = (UserRole) object;
        if ((this.userRoleId == null && other.userRoleId != null) || (this.userRoleId != null && !this.userRoleId.equals(other.userRoleId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "[ userRoleId=" + userRoleId + " ]";
    }

    @XmlTransient
    @Override
    public Object getEntityId() {
        return userRoleId;
    }
    @Transient
    private Long institutionTypeId;

    public Long getInstitutionTypeId() {
        return institutionTypeId;
    }

    public String getInstitutionTypeName() {
        return institutionType != null ? institutionType.getInstitutionTypeName() : null;
    }

    @Transient
    private String[] config;

    @XmlTransient
    public boolean hasRoleId(String roleId) {
        if (config == null && roleConfig != null) {
            config = roleConfig.split(",");
        }
        if (config != null) {
            for (String c : config) {
                if (c != null && !c.isEmpty() && c.equals(roleId)) {
                    return true;
                }
            }
        }
        return false;
    }

    @XmlElement
    public boolean isAdmin() {
        return Flag.TRUE.equals(admin);
    }

    public void setAdmin(boolean _admin) {
        admin = _admin ? Flag.TRUE : Flag.FALSE;
    }

}
