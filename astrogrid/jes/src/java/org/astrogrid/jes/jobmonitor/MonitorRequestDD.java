/*
 * @(#)MonitorRequestDD.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.jes.jobmonitor;


public final class MonitorRequestDD {
	
	public static final String
	    JOB_ELEMENT = "job",
	    JOB_NAME_ATTR = "name",
	    JOB_URN_ATTR = "jobURN",
	    JOB_USERID_ATTR = "userid",
	    JOB_COMMUNITY_ATTR = "community",
	    JOB_TIMESTAMP_ATTR = "time";
	    
	public static final String
	    JOBSTEP_ELEMENT = "jobstep",
	    JOBSTEP_NAME_ATTR = "name",
	    JOBSTEP_NUMBER_ATTR = "stepNumber",
	    JOBSTEP_STATUS_ATTR = "status" ;
	    
	public static final String
	    COMMENT_ELEMENT = "comment" ;
	 
}
