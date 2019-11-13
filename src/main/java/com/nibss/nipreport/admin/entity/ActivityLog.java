/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.admin.entity;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
@Table(name = "activity_log")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ActivityLog.findAll", query = "SELECT a FROM ActivityLog a")})
public class ActivityLog implements Serializable, com.nibss.nipreport.model.Entity {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "activity_log_id")
    private Long activityLogId;
    @XmlTransient
    @Lob
    @Size(max = 2147483647)
    @Column(name = "data_log")
    private String dataLog;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "entity_class")
    private String entityClass;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "entity_id")
    private String entityId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "event_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date eventDate;
    @Size(max = 45)
    @Column(name = "event_ip")
    private String eventIp;
    @XmlTransient
    @JoinColumn(name = "branch", referencedColumnName = "branch_id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Branch branch;
    @XmlTransient
    @JoinColumn(name = "event_by", referencedColumnName = "user_id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private UserIdentity eventBy;
    @XmlTransient
    @JoinColumn(name = "event_status", referencedColumnName = "status_id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Status eventStatus;

    public ActivityLog() {
    }

    public Long getActivityLogId() {
        return activityLogId;
    }

    public void setActivityLogId(Long activityLogId) {
        this.activityLogId = activityLogId;
    }

    @XmlTransient
    public String getDataLog() {
        return dataLog;
    }

    public void setDataLog(String dataLog) {
        this.dataLog = dataLog;
    }

    @XmlTransient
    public String getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(String entityClass) {
        this.entityClass = entityClass;
    }

    public String getEntityID() {
        return entityId;
    }

    public void setEntityID(String entityId) {
        this.entityId = entityId;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventIp() {
        return eventIp;
    }

    public void setEventIp(String eventIp) {
        this.eventIp = eventIp;
    }

    @XmlTransient
    public UserIdentity getEventBy() {
        return eventBy;
    }

    public void setEventBy(UserIdentity eventBy) {
        this.eventBy = eventBy;
    }

    @XmlTransient
    public Status getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(Status eventStatus) {
        this.eventStatus = eventStatus;
    }

    @XmlTransient
    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (activityLogId != null ? activityLogId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ActivityLog)) {
            return false;
        }
        ActivityLog other = (ActivityLog) object;
        if ((this.activityLogId == null && other.activityLogId != null) || (this.activityLogId != null && !this.activityLogId.equals(other.activityLogId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getClass().getName() + "[ logId=" + activityLogId + " ]";
    }

    @XmlTransient
    @Override
    public Object getEntityId() {
        return this.activityLogId;
    }

    @XmlElement
    public String getBranchName() {
        if (branch != null) {
            return branch.getBranchName();
        } else {
            return eventBy != null ? eventBy.getBranch().getBranchName() : null;
        }
    }

    @XmlElement
    public String getInstitutionName() {
        if (branch != null) {
            return branch.getInstitution().getInstitutionName();
        } else {
            return eventBy != null ? eventBy.getBranch().getInstitution().getInstitutionName() : null;
        }
    }

    @XmlElement
    public String getEventByEmail() {
        return eventBy != null ? eventBy.getEmail() : null;
    }

    @XmlElement
    public String getEventDesc() {
        String dsc = eventStatus != null ? eventStatus.getDescription() : null;
        if (entityClass != null) {
            int index = entityClass.lastIndexOf(".");
            if (index > 0 && index + 1 < entityClass.length()) {
                dsc = dsc + "(" + entityClass.substring(index + 1) + ")";
            }
        }
        return dsc;
    }

    //++++++++++++++++++++++++
    public static ActivityLog newInstance(com.nibss.nipreport.model.Entity e) {
        ActivityLog activityLog = new ActivityLog();
        activityLog.setEventDate(new Date());
        activityLog.setEntityID(e.getEntityId() != null ? e.getEntityId().toString() : null);
        activityLog.setEntityClass(e.getClass().getName());
        return activityLog;
    }
}
