/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.admin.repository;

import com.nibss.nipreport.admin.entity.InstitutionType;
import javax.ejb.Stateless;

/**
 *
 * @author oogunjimi
 */
@Stateless
public class InstitutionTypeFacade extends AbstractFacade<InstitutionType> {

    public InstitutionTypeFacade() {
        super(InstitutionType.class);
    }

    public static InstitutionTypeFacade instance() {
        return AbstractFacade.instance(InstitutionTypeFacade.class);
    }

}
