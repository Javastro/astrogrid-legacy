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
        WORKFLOW_NAME_ATTR = "name",
        WORKFLOW_TEMPLATENAME_ATTR = "templateName";
        
    public static final String
        USERID_ELEMENT = "userid",
        COMMUNITY_ELEMENT = "jes_community",
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
        QUERY_ELEMENT = "query",
        QUERY_TYPE_ATTRIBUTE = "type",
        QUERY_TARGET_ATTRIBUTE = "target" ;
        
    public static final String
        NULL_TOOL_ELEMENT = "nulltool",
        RESOURCES_ELEMENT = "resources";
        
    public static final String
        SELECT_ELEMENT = "select",
        SELECT_NAME_ELEMENT = "queryName",
        SELECT_DESCRIPTION_ELEMENT = "queryDescription";
        
    public static final String       
        RESOURCE_ELEMENT = "resource",
        RESOURCE_TYPE_ATTR = "type" ;
        
    public static final String
        WORKFLOW_TEMPLATE =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +        "<workflow name=\"{0}\" {1}>" +        "   <userid>{2}</userid>" +        "   <jes_community>{3}</jes_community>" +
        "   <description>{4}</description>" +              "       {5}" +                      // sequence by default        "</workflow>" ;
        
    public static final String
        SEQUENCE_TEMPLATE =
        "<sequence>" +
        "   {0}" +                          // whatever an ActivityContainer contains
        "</sequence>" ;        
	    
    public static final String
        FLOW_TEMPLATE =
        "<flow>" +
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
        "<query>" +        "   {0}" +        "</query>" ;  
        
    public static final String
        RESOURCES_TEMPLATE =
        "<resources>" +        "   {0}" +                          // one or more resources        "</resources>" ;
          
    public static final String
        RESOURCE_TEMPLATE =
        "   <resource type=\"{0}\">" +
        "   </resource>" ;
        
    public static final String
        JOB_TEMPLATE =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
        "<job name=\"{0}\" >" +
        "   <userid>{1}</userid>" +
        "   <jes_community>{2}</jes_community>" +
        "   <description>{3}</description>" +        "   <AssignID>dummy</AssignID>" +        "   {4}" +       // the community snippet      
        "   {5}" +       // variable number of job steps               
        "</job>" ;        
        
    public static final String
        JOBSTEP_TEMPLATE =
        "<jobstep name=\"{0}\" " +
        "      joinCondition=\"{1}\" " +
        "      stepNumber=\"{2}\" " +
        "      sequenceNumber=\"{3}\">" +
        "   {4}" + 
        "</jobstep>" ;
        
    public static final String
        JOBQUERY_TEMPLATE =
        "<query>" +        "   {0}" +
        "</query>" ;          

} // end of class WorkflowDD
