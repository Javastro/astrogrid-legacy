/*
 * @(#)SubmissionRequestDD.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.jes.jobcontroller;


public final class SubmissionRequestDD {
	
	public static final String
	    JOB_ELEMENT = "job",
	    JOB_NAME_ATTR = "name",
	    JOB_URN_ATTR = "jobURN",
	    JOB_MONITOR_URL_ATTR = "jobMonitorURL";
	    
	public static final String
	    USERID_ELEMENT = "userid",
	    COMMUNITY_ELEMENT = "community",
        DESCRIPTION_ELEMENT = "description" ;
	    
	public static final String
	    JOBSTEP_ELEMENT = "jobstep",
	    JOBSTEP_NAME_ATTR = "name",
	    JOBSTEP_STEPNUMBER_ATTR = "stepNumber",
        JOBSTEP_SEQUENCENUMBER_ATTR = "sequenceNumber",
        JOBSTEP_JOINCONDITION_ATTR = "stepJoinCondition";
        
	public static final String    
	    QUERY_ELEMENT = "query";
	    
	public static final String 
	    FROM_ELEMENT = "from";
	    
	public static final String
	    CATALOG_ELEMENT = "catalog",
	    CATALOG_NAME_ATTR = "name" ;
	    
	public static final String
	    SERVICE_ELEMENT = "service",
	    SERVICE_NAME_ATTR = "name",
	    SERVICE_URL_ATTR = "url";
	    
	public static final String
	    TABLE_ELEMENT = "table",
	    TABLE_NAME_ATTR = "name" ;
	       
	public static final String
	    RETURN_ELEMENT = "return";
	        
	public static final String 
	    CRITERIA_ELEMENT = "criteria";  
	     
	public static final String
	    FIELD_ELEMENT = "field",
	    FIELD_NAME_ATTR = "name",
	    FIELD_TYPE_ATTR = "type",
	    FIELD_TYPE_UCD = "UCD",
	    FIELD_TYPE_COLUMN = "COLUMN" ;
	        
	public static final String
	    OP_ELEMENT = "operation",
	    OP_NAME_ATTR = "name", 
	    OP_NAME_AND = "AND",
	    OP_NAME_OR = "OR",
	    OP_NAME_NOT = "NOT",
	    OP_NAME_LT = "LESS_THAN",
	    OP_NAME_GT = "GREATER_THAN", 
	    OP_NAME_DIFFERENCE = "DIFFERENCE",
	    OP_NAME_AVERAGE = "AVERAGE",
	    OP_NAME_CONE = "CONE",
	    OP_NAME_EQUALS ="EQUALS",
	    OP_NAME_GTE = "GREATER_THAN_OR_EQUALS",
	    OP_NAME_LTE = "LESS_THAN_OR_EQUALS",
	    OP_NAME_IN = "IN",
	    OP_NAME_ANY = "ANY",
	    OP_NAME_ALL = "ALL" ;
        
    public static final String
        ADQL_SELECT_ELEMENT = "Select",
        ADQL_TARGETSERVICE_ELEMENT = "Service",
        ADQL_TS_TAG = "<" + ADQL_TARGETSERVICE_ELEMENT + ">",
        ADQL_TS_ENDTAG = "</" + ADQL_TARGETSERVICE_ELEMENT + ">" ;
        
    public static final String
        COMMUNITY_SNIPPET_ELEMENT = "community",
        COMMUNITY_TAG = "<" + COMMUNITY_SNIPPET_ELEMENT + ">",
        COMMUNITY_ENDTAG = "</" + COMMUNITY_SNIPPET_ELEMENT  + ">" ;
	 
}
