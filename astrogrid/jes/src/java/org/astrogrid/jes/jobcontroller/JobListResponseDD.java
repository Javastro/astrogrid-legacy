/*
 * @(#)JobListResponseDD.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.jes.jobcontroller;


public final class JobListResponseDD {
	
	public static final String
	    JOBLIST_REPONSE = "reponse" ;
	    
    public static final String 
        ARGUMENT_ELEMENT = "message" ;
	 
    public static final String
        JOBLIST_ELEMENT = "joblist",
        JOBLIST_USERID_ATTR = "userid",
        JOBLIST_COMMUNITY_ATTR = "community" ; 

    public static final String
        JOB_ELEMENT = "job",
        JOB_NAME_ATTR = "name" ;
        
    public static final String 
        DESCRIPTION_ELEMENT = "description",
        STATUS_ELEMENT = "status",
        TIME_ELEMENT = "time",
        JOBID_ELEMENT = "jobid";
        
    public static final String
        RESPONSE_TEMPLATE = 
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +            "<response>" +            "   <message>{0}</message>" +            "   <joblist userid=\"{1}\" community=\"{2}\" >" +            "       {3}" +            "   </joblist>" +            "</response>" ;
        
    public static final String
        JOB_TEMPLATE = 
            "<job name=\"{0}\" >" +            "   <description>{1}</description>" +            "   <status>{2}</status>" +            "   <time>{3}</time>" +            "   <jobid>{4}</jobid>" +            "</job>" ;        
   
}
