/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.admin.entity;

import com.elixir.commons.web.context.User;
import com.nibss.nipreport.admin.repository.RoleFacade;
import com.nibss.nipreport.model.AbstractFlag;
import com.nibss.nipreport.model.Flag;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
@Table(name = "user_identity")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserIdentity.findAll", query = "SELECT u FROM UserIdentity u"),
    @NamedQuery(name = "UserIdentity.findByEmail", query = "SELECT u FROM UserIdentity u where u.email=:email and u.flag <> '" + Flag.DELETED + "'"),
    @NamedQuery(name = "UserIdentity.containsEmail", query = "SELECT COUNT(u) FROM UserIdentity u where u.email=:email and u.flag <> '" + Flag.DELETED + "' AND u.userId <> :excludeUserId"),
    @NamedQuery(name = "UserIdentity.findByBranchAndRoles", query = "SELECT DISTINCT(u) FROM UserIdentity u where u.branch.branchId=:branchId and u.userRole.roleConfig LIKE :roleConfig and u.flag <> '" + Flag.DELETED + "'"),
    @NamedQuery(name = "UserIdentity.findEmailByBranchAndRoles", query = "SELECT DISTINCT(u.email) FROM UserIdentity u where u.branch.branchId=:branchId and u.userRole.roleConfig LIKE :roleConfig and u.flag <> '" + Flag.DELETED + "'"),
    @NamedQuery(name = "UserIdentity.UpdateExpiredUsers", query = "UPDATE UserIdentity u SET u.flag = '" + Flag.LOCKED + "' where u.lastAccessedDate < :expirationDate AND u.branch.institution.institutionType.institutionTypeId > " + Flag.NIBSS_INSTITUTION_TYPE + " and u.flag = '" + Flag.ENABLED + "'"),
    @NamedQuery(name = "UserIdentity.findExpiredUsers", query = "SELECT DISTINCT(u) FROM UserIdentity u where u.lastAccessedDate < :expirationDate and u.flag <> '" + Flag.DELETED + "'")})
public class UserIdentity extends AbstractFlag implements Serializable, User, com.nibss.nipreport.model.Entity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "user_id")
    private Long userId;
    //@Pattern(regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message = "Email is not well formed")//if the field contains email address consider using this annotation to enforce field validation
    //@Pattern(regexp = "", message = "Email is not well formed")
    @Basic(optional = false)
    @NotNull
    @NotBlank
    @Size(min = 1, max = 90)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @NotNull
    @NotBlank
    @Size(min = 1, max = 60)
    @Column(name = "first_name")
    private String firstName;
    @Basic(optional = false)
    @NotNull
    @NotBlank
    @Size(min = 1, max = 60)
    @Column(name = "last_name")
    private String lastName;

    @Size(max = 64)
    @Column(name = "password")
    private String passwordHash;

    @Size(max = 32)
    @Column(name = "phone_no")
    private String phoneNo;
    //
    @Basic(optional = false)
//    @NotNull
//    @Size(min = 1, max = 5)
    @Column(name = "flag", length = 5)
    private String flag;

    @Basic(optional = false)
    @NotNull
    @Column(name = "access_count")
    private int accessCount;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "first_accessed_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date firstAccessedDate;

    @Column(name = "last_accessed_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastAccessedDate;

    @Column(name = "last_password_change_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastPasswordChangeDate;

    @JoinColumn(name = "branch", referencedColumnName = "branch_id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Branch branch;

    @JoinColumn(name = "user_role", referencedColumnName = "user_role_id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private UserRole userRole;

    public UserIdentity() {
    }

    @Override
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    @XmlTransient
    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String password) {
        this.passwordHash = password;
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
    public int getAccessCount() {
        return accessCount;
    }

    public void setAccessCount(int accessCount) {
        this.accessCount = accessCount;
    }

    @XmlTransient
    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    @XmlTransient
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @XmlTransient
    public Date getFirstAccessedDate() {
        return firstAccessedDate;
    }

    public void setFirstAccessedDate(Date firstAccessedDate) {
        this.firstAccessedDate = firstAccessedDate;
    }

    @XmlTransient
    public Date getLastAccessedDate() {
        return lastAccessedDate;
    }

    public void setLastAccessedDate(Date lastAccessedDate) {
        this.lastAccessedDate = lastAccessedDate;
    }

    @XmlTransient
    public Date getLastPasswordChangeDate() {
        return lastPasswordChangeDate;
    }

    public void setLastPasswordChangeDate(Date lastPasswordChangeDate) {
        this.lastPasswordChangeDate = lastPasswordChangeDate;
    }

    @XmlTransient
    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    @XmlTransient
    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserIdentity)) {
            return false;
        }
        UserIdentity other = (UserIdentity) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "[ userId=" + userId + " ]";
    }
    //****************************
    @Transient
    private Long branchId;
    @Transient
    private Long userRoleId;

    public Long getBranchId() {
        return branchId;
    }

    public Long getUserRoleId() {
        return userRoleId;
    }

    @XmlElement
    public String getInstitutionName() {
        return branch != null ? branch.getInstitution().getInstitutionName() : null;
    }

    @XmlElement
    public String getBranchName() {
        return branch != null ? branch.getBranchName() : null;
    }

    @XmlElement
    public String getUserRoleName() {
        return userRole != null ? userRole.getUserRoleName() : null;
    }

    @XmlTransient
    @Override
    public Object getEntityId() {
        return userId;
    }
    @Transient
    private String sessionId;

    @XmlTransient
    @Override
    public String getSessionId() {
        return sessionId;
    }

    @Override
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    //**********************************************
    @Transient
    private List<String> roleNames;

    @XmlTransient
    public List<String> getRoleNames() {
        if (roleNames == null) {
            List<Long> roleConfigList = getRoleConfigList();
            if (roleConfigList != null && !roleConfigList.isEmpty() && userRole != null && userRole.getInstitutionType() != null) {
                roleNames = RoleFacade.instance().findNameByRoleIds(roleConfigList, userRole.getInstitutionType().getInstitutionTypeId());
            }
        }
        return roleNames;
    }
    @Transient
    private List<Long> configList;

    @XmlTransient
    private List<Long> getRoleConfigList() {
        String roleConfig = userRole != null ? userRole.getRoleConfig() : null;
        if (configList == null && roleConfig != null) {
            configList = new ArrayList<>();
            for (String roleId : roleConfig.split(",")) {
                if (roleId == null || roleId.isEmpty()) {
                    continue;
                }
                try {
                    configList.add(Long.valueOf(roleId));
                } catch (NumberFormatException e) {

                }
            }
        }
        return configList;
    }

//    @XmlTransient
//    public boolean hasRole(String roleName) {
//        if (roleName == null) {
//            return false;
//        }
//        List<String> _roleNames = getRoleNames();
//        return (_roleNames != null) ? _roleNames.contains(roleName) : false;
//    }
//
//    @XmlTransient
//    public boolean hasAnyRole(String[] roleNames) {
//        if (roleNames == null || roleNames.length == 0) {
//            return false;
//        }
//        List<String> _roleNames = getRoleNames();
//        if (_roleNames != null) {
//            for (String _roleName : _roleNames) {
//                if (_roleName != null) {
//                    for (String roleName : roleNames) {
//                        if (roleName!=null && _roleName.equals(roleName)) {
//                            return true;
//                        }
//                    }
//                }
//            }
//        }
//        return false;
//    }
//
//    @XmlTransient
//    public boolean likeAnyRole(String[] roleNames) {
//        if (roleNames == null || roleNames.length == 0) {
//            return false;
//        }
//        List<String> _roleNames = getRoleNames();
//        if (_roleNames != null) {
//            for (String _roleName : _roleNames) {
//                if (_roleName != null) {
//                    for (String roleName : roleNames) {
//                        if (roleName!=null && _roleName.contains(roleName)) {
//                            return true;
//                        }
//                    }
//                }
//            }
//        }
//        return false;
//    }
//
//
//    public boolean isNibss() {
//        try {
//            return Flag.NIBSS_INSTITUTION_TYPE.equals(this.getBranch().getInstitution().getInstitutionType().getInstitutionTypeId());
//        } catch (Exception e) {
//
//        }
//        return false;
//    }
}
