/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.admin.repository;

import com.nibss.nipreport.admin.entity.Role;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author oogunjimi
 */
@Stateless
public class RoleFacade extends AbstractFacade<Role> {

    public RoleFacade() {
        super(Role.class);
    }

    public static RoleFacade instance() {
        return AbstractFacade.instance(RoleFacade.class);
    }

    public List<Role> findByInstitutionType(Long institutionTypeId) {
        Query query = getEntityManager().createNamedQuery("Role.findByInstitutionTypeId");
        query.setParameter("institutionTypeId", institutionTypeId);
        return (List<Role>) query.getResultList();
    }

    public List<String> findNameByRoleIds(List<Long> roleIds, Long institutionTypeId) {
        Query query = getEntityManager().createNamedQuery("Role.findNameByRoleIds");
        query.setParameter("institutionTypeId", institutionTypeId);
        query.setParameter("roleIds", roleIds);
    return (List<String>) query.getResultList();
    }
}
