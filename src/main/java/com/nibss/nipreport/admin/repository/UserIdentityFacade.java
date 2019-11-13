/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.admin.repository;

import com.nibss.nipreport.admin.entity.UserIdentity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author oogunjimi
 */
@Stateless
public class UserIdentityFacade extends AbstractFacade<UserIdentity> {

    public UserIdentityFacade() {
        super(UserIdentity.class);
    }

    public static UserIdentityFacade instance() {
        return AbstractFacade.instance(UserIdentityFacade.class);
    }

     public List<UserIdentity> findByEmail(String email) {
        return getEntityManager().createNamedQuery("UserIdentity.findByEmail")
                .setParameter("email", email).getResultList();
    }

    public boolean containsEmail(String email, Long userId) {
        Query query = getEntityManager().createNamedQuery("UserIdentity.containsEmail");
        query.setParameter("email", email);
        query.setParameter("excludeUserId", userId == null ? -1 : userId);
        return ((Long) query.getSingleResult()).intValue() > 0;
    }

}
