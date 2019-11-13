/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.model;

/**
 *
 * @author oogunjimi
 */
public interface AdminResource extends Resource {

    public static final String INSTITUTION = "institution";
    public static final String CREATE_INSTITUTION = "create-institution";
    public static final String VIEW_INSTITUTION = "view-institution";
    public static final String EDIT_INSTITUTION = "edit-institution";
    public static final String LIST_INSTITUTION = "list-institution";
    public static final String DELETE_INSTITUTION = "delete-institution";
    //
    public static final String BANK = "institution/bank";
    public static final String CREATE_BANK = "create-bank";
    public static final String VIEW_BANK = "view-bank";
    public static final String EDIT_BANK = "edit-bank";
    //public static final String LIST_BANK = "institution/list-bank";
    //
    public static final String THIRD_PARTY = "institution/third-party";
    public static final String CREATE_THIRD_PARTY = "create-third-party";
    public static final String VIEW_THIRD_PARTY = "view-third-party";
    public static final String EDIT_THIRD_PARTY = "edit-third-party";
    //public static final String LIST_THIRD_PARTY = "institution/list-third-party";
    //
    public static final String BRANCH = "branch";
    public static final String CREATE_BRANCH = "create-branch";
    public static final String VIEW_BRANCH = "view-branch";
    public static final String EDIT_BRANCH = "edit-branch";
    public static final String LIST_BRANCH = "list-branch";
    public static final String DELETE_BRANCH = "delete-branch";
    //
    public static final String USER_ROLE = "user-role";
    public static final String CREATE_USER_ROLE = "create-user-role";
    public static final String VIEW_USER_ROLE = "view-user-role";
    public static final String EDIT_USER_ROLE = "edit-user-role";
    public static final String LIST_USER_ROLE = "list-user-role";
    public static final String DELETE_USER_ROLE = "delete-user-role";
    //
    public static final String USER = "user";
    public static final String CREATE_USER = "create-user";
    public static final String VIEW_USER = "view-user";
    public static final String EDIT_USER = "edit-user";
    public static final String LIST_USER = "list-user";
    public static final String DELETE_USER = "delete-user";
    //
    public static final String ACTIVITY_LOG = "activity-log";
    public static final String LIST_ACTIVITY_LOG = "list-activity-log";
    public static final String VIEW_ACTIVITY_LOG = "view-activity-log";
}
