/* $Id: RegisteredUsers.java,v 1.1 2004/06/07 14:39:36 jdt Exp $
 * Created on Jun 7, 2004 by jdt@roe.ac.uk
 * The auto-integration project
 * Copyright (c) Astrigrid 2004.  All rights reserved. 
 *
 */
package org.astrogrid.portal.integration;

/**
 * Simply a struct to hold users known to be registered in community
 * 
 * @author jdt
 */
public final class RegisteredUsers {
    /**
     * The location of a registry must be set with the property
     * org.astrogrid.portal.registry.url, and this registry must
     * contain the community given below.
     */
    public static final String LOCAL_COMMUNITY = "org.astrogrid.localhost";
    /**
     * Password for a known registered user
     */
    public static final String PASSWORD = "qwerty";
    /**
     * A know registered user
     */
    public static final String USERNAME = "frog";

    /**
     * Constructor
     * 
     */
    private RegisteredUsers() {
        super();
    }
}


/*
 *  $Log: RegisteredUsers.java,v $
 *  Revision 1.1  2004/06/07 14:39:36  jdt
 *  Refactored out some common stuff
 *
 */