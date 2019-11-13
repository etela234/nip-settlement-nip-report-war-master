/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.admin.repository;

import com.nibss.nipreport.admin.entity.UserRole;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author oogunjimi
 */
@Stateless
public class UserRoleFacade extends AbstractFacade<UserRole> {

    public UserRoleFacade() {
        super(UserRole.class);
    }

    public static UserRoleFacade instance() {
        return AbstractFacade.instance(UserRoleFacade.class);
    }

    public boolean containsUserRoleName(String roleName, Long institutionTypeId, Long excludeUserRoleId) {
        Query query = getEntityManager().createNamedQuery("UserRole.containsUserRoleName");
        query.setParameter("userRoleName", roleName);
        query.setParameter("institutionTypeId", institutionTypeId);
        query.setParameter("excludeUserRoleId", excludeUserRoleId == null ? -1 : excludeUserRoleId);
        return ((Long) query.getSingleResult()).intValue() > 0;
    }

    public List<Object[]> findByInstitutionTypeId(Long institutionTypeId, boolean exemptAdmin) {
        Query query = getEntityManager().createNamedQuery(exemptAdmin ? "UserRole.findByInstitutionTypeIdExemptAdmin" : "UserRole.findByInstitutionTypeId");
        query.setParameter("institutionTypeId", institutionTypeId);
        return query.getResultList();
    }
    
}
