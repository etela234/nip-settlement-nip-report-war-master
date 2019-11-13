/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.admin.repository;

import com.nibss.nipreport.admin.entity.Institution;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author oogunjimi
 */
@Stateless
public class InstitutionFacade extends AbstractFacade<Institution> {

    public InstitutionFacade() {
        super(Institution.class);
    }

    public static InstitutionFacade instance() {
        return AbstractFacade.instance(InstitutionFacade.class);
    }

    public Long findInstitutionTypeId(Long institutionId){
        try {
            Query query = this.getEntityManager().createNamedQuery("Institution.findInstitutionTypeId");
            query.setParameter("institutionId", institutionId);
            return (Long) query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
