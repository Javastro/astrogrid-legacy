/*
 * @(#)JobDD.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.portal.workflow.jes ;

/**
 * The <code>JobDD</code> class represents 
 *
 * @author  Jeff Lusted
 * @version 1.0 09-Jun-2003
 *
 * @since   AstroGrid 1.2
 * @modified NWW made package private.
 */
class JobDD {
    
    public static final String
        RESPONSE_ELEMENT = "response",
        MESSAGE_ELEMENT = "message",
        JOBLIST_ELEMENT = "joblist",       
        JOB_ELEMENT = "job",
        DESCRIPTION_ELEMENT = "description",
        STATUS_ELEMENT = "status",
        TIME_ELEMENT = "time",
        JOBID_ELEMENT = "jobid" ;
        
    public static final String  
        NAME_ATTR = "name",
        USERID_ATTR = "userid",
        COMMUNITY_ATTR = "community" ;
        
} // end of class JobDD
