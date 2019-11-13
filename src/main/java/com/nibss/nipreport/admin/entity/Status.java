/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.admin.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Oluwaseun Ogunjimi
 */
@Entity
@Table(name = "status")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Status.findByCodeAndType", query = "SELECT DISTINCT(s) FROM Status s where s.statusCode=:statusCode AND  s.statusType=:statusType"),
    @NamedQuery(name = "Status.findByCode", query = "SELECT DISTINCT(s) FROM Status s where s.statusCode=:statusCode"),
    @NamedQuery(name = "Status.findByCodes", query = "SELECT s.statusId,s.description FROM Status s where s.statusCode IN :statusCodes"),
    @NamedQuery(name = "Status.findByType", query = "SELECT s.statusId,s.description FROM Status s where s.statusType=:statusType"),
    @NamedQuery(name = "Status.findByTypeAndCodes", query = "SELECT s.statusId,s.description FROM Status s where s.statusType=:statusType AND s.statusCode IN :statusCodes")
})
public class Status implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "status_id")
    private Long statusId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Column(name = "status_code")
    private int statusCode;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "status_type")
    private String statusType;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "status", fetch = FetchType.LAZY)
//    private List<Referee> refereeList;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "status", fetch = FetchType.LAZY)
//    private List<RefereeLog> refereeLogList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "eventStatus", fetch = FetchType.LAZY)
    private List<ActivityLog> activityLogList;

    public Status() {
    }

    public Status(Long statusId) {
        this.statusId = statusId;
    }

    public Status(Long statusId, String description, int statusCode, String statusType) {
        this.statusId = statusId;
        this.description = description;
        this.statusCode = statusCode;
        this.statusType = statusType;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusType() {
        return statusType;
    }

    public void setStatusType(String statusType) {
        this.statusType = statusType;
    }

//    @XmlTransient
//    public List<Referee> getRefereeList() {
//        return refereeList;
//    }
//
//    public void setRefereeList(List<Referee> refereeList) {
//        this.refereeList = refereeList;
//    }
//
//    @XmlTransient
//    public List<RefereeLog> getRefereeLogList() {
//        return refereeLogList;
//    }
//
//    public void setRefereeLogList(List<RefereeLog> refereeLogList) {
//        this.refereeLogList = refereeLogList;
//    }
    @XmlTransient
    public List<ActivityLog> getActivityLogList() {
        return activityLogList;
    }

    public void setActivityLogList(List<ActivityLog> activityLogList) {
        this.activityLogList = activityLogList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (statusId != null ? statusId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Status)) {
            return false;
        }
        Status other = (Status) object;
        if ((this.statusId == null && other.statusId != null) || (this.statusId != null && !this.statusId.equals(other.statusId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "[ statusId=" + statusId + " ]";
    }

}
