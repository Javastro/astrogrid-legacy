/*
 * @(#)ScheduleRequestDD.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.jes.jobscheduler;


public final class ScheduleRequestDD {
	
	public static final String
	    JOB_ELEMENT = "job",
	    JOB_NAME_ATTR = "name",
	    JOB_URN_ATTR = "jobURN",
	    JOB_USERID_ATTR = "userid",
	    JOB_COMMUNITY_ATTR = "community",
	    JOB_TIMESTAMP_ATTR = "time" ;
//	    JOB_MONITOR_URL = "jobMonitorURL";

    public static final String
        JOBTOOL_TEMPLATE =
        "<tool name=\"{0}\" >" +
        "<input>" +
        "{1}" +   // input parameters
        "</input>" +
        "<output>" +
        "{2}" +   // output parameters
        "</output>" +
        "</tool>" ;
        
    public static final String
        JOBPARAMETER_TEMPLATE =
        "<parameter name=\"{0}\" type=\"{1}\" " +
        "{2} >" +                         // possible location data
        "{3}" +                           // possible instream data
        "</parameter>" ;         
	 
}
