/* <cvs:id> 
 * $Id: SessionKeys.java,v 1.2 2004/03/26 18:08:39 jdt Exp $
 * </cvs:id>
 * Created on Mar 26, 2004 by jdt 
 * 
 * Copyright (C) AstroGrid. All rights reserved.
 * 
 * This software is published under the terms of the AstroGrid Software License
 * version 1.2, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.astrogrid.portal.login.common;
/**
 * Merely a constant holder for keys used to access the 
 * portal's session.
 * 
 * @author jdt
 */
public final class SessionKeys {

    /**
     * Parameter names to look for in the session object.
     */
    public static final String CREDENTIAL = "credential";
    /**
     * Parameter names to look for in the session object.
     */
    public static final String COMMUNITY_NAME = "community_name";
    /**
     * Parameter names to look for in the session object.
     */
    public static final String COMMUNITY_ACCOUNT = "community_account";
    /**
     * Parameter names to look for in the session object.
     */
    public static final String USER = "user";
    
}


/*
 *  $Log: SessionKeys.java,v $
 *  Revision 1.2  2004/03/26 18:08:39  jdt
 *  Merge from PLGN_JDT_bz#275
 *
 *  Revision 1.1.2.1  2004/03/26 17:43:03  jdt
 *  Factored out the keys used to store the session into a separate
 *  class that everyone can access.
 *
 */