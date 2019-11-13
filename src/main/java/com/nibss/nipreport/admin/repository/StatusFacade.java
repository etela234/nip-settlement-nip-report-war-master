/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.admin.repository;

import com.nibss.nipreport.admin.entity.Status;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author oogunjimi
 */
@Stateless
public class StatusFacade extends AbstractFacade<Status> {

    public StatusFacade() {
        super(Status.class);
    }

    public static StatusFacade instance() {
        return AbstractFacade.instance(StatusFacade.class);
    }

    public Status findByCodeAndType(int code, String type) {
        Query query = getEntityManager().createNamedQuery("Status.findByCodeAndType");
        query.setParameter("statusType", type);
        query.setParameter("statusCode", code);
        try {
            return (Status) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Status findByCode(int code) {
        Query query = getEntityManager().createNamedQuery("Status.findByCode");
        query.setParameter("statusCode", code);
        try {
            return (Status) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Object[]> findByCodes(int... code) {
        List<Integer> l = new ArrayList<>();
        for (int c : code) {
            l.add(c);
        }
        Query query = getEntityManager().createNamedQuery("Status.findByCodes");
        query.setParameter("statusCodes", l);
        return query.getResultList();
    }

    public List<Object[]> findByType(String type) {
        Query query = getEntityManager().createNamedQuery("Status.findByType");
        query.setParameter("statusType", type);
        return query.getResultList();
    }

    public List<Object[]> findByTypeAndCodes(String type, List<Integer> codes) {
        Query query = getEntityManager().createNamedQuery("Status.findByTypeAndCodes");
        query.setParameter("statusType", type);
        query.setParameter("statusCodes", codes);
        return query.getResultList();
    }
}
