/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.admin.repository;

import com.nibss.nipreport.admin.entity.ThirdParty;
import java.util.List;
import javax.ejb.Stateless;

/**
 *
 * @author oogunjimi
 */
@Stateless
public class ThirdPartyFacade extends AbstractFacade<ThirdParty> {

    public ThirdPartyFacade() {
        super(ThirdParty.class);
    }

    public static ThirdPartyFacade instance() {
        return AbstractFacade.instance(ThirdPartyFacade.class);
    }

    public List<Long> findBySettlementBank(Long institutionId) {
        return this.getEntityManager().createNamedQuery("ThirdParty.findBySettlementBank").setParameter("institutionId", institutionId).getResultList();
    }
}
