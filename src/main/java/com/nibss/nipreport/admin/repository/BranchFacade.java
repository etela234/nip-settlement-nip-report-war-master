/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.admin.repository;

import com.nibss.nipreport.admin.entity.Branch;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author oogunjimi
 */
@Stateless
public class BranchFacade extends AbstractFacade<Branch> {

    public BranchFacade() {
        super(Branch.class);
    }

    public static BranchFacade instance() {
        return AbstractFacade.instance(BranchFacade.class);
    }

    public boolean containsBranchCode(String branchCode, Long institutionId, Long branchId) {
        Query query = getEntityManager().createNamedQuery("Branch.containsBranchCode");
        query.setParameter("branchCode", branchCode);
        query.setParameter("institutionId", institutionId);
        query.setParameter("excludeBranchId", branchId == null ? -1 : branchId);
        return ((Long) query.getSingleResult()).intValue() > 0;
    }

    public List<Object[]> findByInstitutionId(Long institutionId) {
        Query query = getEntityManager().createNamedQuery("Branch.findByInstitutionId");
        query.setParameter("institutionId", institutionId);
        return query.getResultList();
    }

    public List<Object[]> findByInstitutionId(Long institutionId, Long excemptBranchId) {
        Query query = getEntityManager().createNamedQuery("Branch.findByInstitutionIdExempt");
        query.setParameter("institutionId", institutionId);
        query.setParameter("branchId", excemptBranchId);
        return query.getResultList();
    }
}
