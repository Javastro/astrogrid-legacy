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
 * The <code>WorkflowDD</code> class represents 
 *
 * @author  Jeff Lusted
 * @version 1.0 09-Jun-2003
 * @see     java.lang.Object#toString()
 * @see     java.lang.StringBuffer
 * @since   AstroGrid 1.2
 */
public class WorkflowDD {
    
    public static final String
        WORKFLOW_ELEMENT = "workflow",
        WORKFLOW_NAME_ATTR = "name" ;
        
    public static final String
        USERID_ELEMENT = "userid",
        COMMUNITY_ELEMENT = "community",
        DESCRIPTION_ELEMENT = "description",
        SEQUENCE_ELEMENT = "sequence",
        FLOW_ELEMENT = "flow" ;
        
    public static final String
        STEP_ELEMENT = "step",    
        STEP_NAME_ATTR = "name",
        STEP_STEPNUMBER_ATTR = "stepNumber",
        STEP_SEQUENCENUMBER_ATTR = "sequenceNumber",
        STEP_JOINCONDITION_ATTR = "stepJoinCondition";
        
    public static final String
        QUERY_ELEMENT = "select",
        QUERY_NAME_ELEMENT = "queryName",
        QUERY_DESCRIPTION_ELEMENT = "queryDescription",
        NULL_TOOL_ELEMENT = "nulltool",
        RESOURCES_ELEMENT = "resources";
        
    public static final String       
        RESOURCE_ELEMENT = "resource",
        RESOURCE_TYPE_ATTR = "type" ;
        
    public static final String
        WORKFLOW_TEMPLATE =
        "<?xml version='1.0' encoding='UTF-8'?>" +        "<workflow name=\"{0}\">" +        "   <userid>{1}</userid>" +        "   <community>{2}</community>" +
        "   <description>{3}</description>" +                "       {4}" +                      // sequence by default        "</workflow>" ;
        
    public static final String
        SEQUENCE_TEMPLATE =
        "<sequence >" +
        "   {0}" +                          // whatever an ActivityContainer contains
        "</sequence>" ;        
	    
    public static final String
        FLOW_TEMPLATE =
        "<flow >" +
        "   {0}" +                          // whatever an ActivityContainer contains
        "</flow>" ;                
        
    public static final String
        STEP_TEMPLATE =
        "<step name=\"{0}\" " +        "      joinCondition=\"{1}\" " +        "      stepNumber=\"{2}\" " +        "      sequenceNumber=\"{3}\">" +
        "   {4}" +                          // some tool (e.g. query)
        "   {5}" +                          // resources
        "</step>" ;
        
    public static final String
        QUERY_TEMPLATE =
        "<select>" +        "</select>" ;  
        
    public static final String
        RESOURCES_TEMPLATE =
        "<resources>" +        "   {0}" +                          // one or more resources        "</resources>" ;
          
    public static final String
        RESOURCE_TEMPLATE =
        "   <resource type=\"{0}\">" +
        "   </resource>" ;

} // end of class WorkflowDD
