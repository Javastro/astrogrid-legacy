/*
 * @(#)WorkflowDD.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.portal.workflow.design ;

/**
 * The <code>MySpaceManagerResponseDD</code> class represents 
 *
 * @author  Jeff Lusted
 * @version 1.0 09-Jun-2003
 * @see     java.lang.Object#toString()
 * @see     java.lang.StringBuffer
 * @since   AstroGrid 1.2
 */
public class WorkflowDD {
	
	public static final String
		RESULTS_ELEMENT = "result" ;
		
	public static final String
	    STATUS_ELEMENT_01 = "status" ;
	    
	public static final String
	    STATUS_ELEMENT_02 = "status",
	    DETAILS_ELEMENT_02 = "details" ;
        
    public static final String
        WORKFLOW_TEMPLATE =
        "<?xml version='1.0' encoding='UTF-8'?>" +        "<workflow name=\"{0}\">" +        "   <userid>{1}</userid>" +        "   <community>{2}</community>" +        "       {3}" +        "</workflow>" ;
        
    public static final String
        SEQUENCE_TEMPLATE =
        "<sequence >" +
        "   {0}" +
        "</sequence>" ;        
	    
    public static final String
        FLOW_TEMPLATE =
        "<flow >" +
        "   {0}" +
        "</flow>" ;                
        
    public static final String
        STEP_TEMPLATE =
        "<step name=\"{0}\" joinCondition=\"{1}\">" +
        "   {2}" +
        "   {3}" +
        "</step>" ;
        
    public static final String
        TOOL_TEMPLATE =
        "<tool>" +
        "</tool>" ;   
        
    public static final String
        RESOURCES_TEMPLATE =
        "<resources>" +        "   {0}" +        "</resources>" ;
          
    public static final String
        RESOURCE_TEMPLATE =
        "   <resource type=\"{0}\">" +
        "   </resource>" ;

}
