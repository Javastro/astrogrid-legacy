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
        TOOL_ELEMENT = "tool",
        TOOL_NAME_ATTR = "name" ;
        
    public static final String
        INPUT_ELEMENT = "input",
        OUTPUT_ELEMENT = "output" ;
         
    public static final String       
        PAREMETER_ELEMENT = "parameter",
        PAREMETER_NAME_ATTR = "name", 
        PAREMETER_TYPE_ATTR = "type", 
        PAREMETER_LOCATION_ATTR = "location",
        PARAMETER_MIN_CARDINALITY = "min" ,
        PARAMETER_MAX_CARDINALITY = "max" ;
        
    public static final String       
         DOCUMENTATION_ELEMENT = "documentation" ;
        
    public static final String
        WORKFLOW_TEMPLATE =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +        "<workflow " +        "     xmlns=\"http://www.astrogrid.org/schema/AGWorkflow/\" " +
        "     xmlns:agpd=\"http://www.astrogrid.org/schema/AGParameterDefinition/\" " +
        "     xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
        "     xsi:schemaLocation=\"http://www.astrogrid.org/schema/Workflow.xsd\" " +        "     name=\"{0}\" " +        "     {1}>" +        "{2}" +                          // community snippet
        "<description>{3}</description>" +              "{4}" +                      // sequence by default        "</workflow>" ;
        
    public static final String
        SEQUENCE_TEMPLATE =
        "<sequence>" +
        "{0}" +                          // whatever an ActivityContainer contains
        "</sequence>" ;        
	    
    public static final String
        FLOW_TEMPLATE =
        "<flow>" +
        "{0}" +                          // whatever an ActivityContainer contains
        "</flow>" ;                
        
    public static final String
        STEP_TEMPLATE =
        "<step name=\"{0}\" joinCondition=\"{1}\" stepNumber=\"{2}\" sequenceNumber=\"{3}\">" +
        "{4}" +                          // some tool (e.g. query)
        "</step>" ;
        
    public static final String
        TOOL_TEMPLATE =
        "<tool name=\"{0}\" >" +        "<documentation>" +        "{1}" +        "</documentation>" +
        "<input>" +        "{2}" +   // input parameters        "</input>" +
        "<output>" +        "{3}" +   // output parameters        "</output>" +
        "</tool>" ;
        
    public static final String
        PARAMETER_TEMPLATE =
        "<parameter name=\"{0}\" type=\"{1}\" min=\"{2}\" max=\"{3}\" >" +        "<documentation>" +        "{4}" +        "</documentation>"+
        "{5}" +                           // possible instream data OR remote reference
        "</parameter>" ;        
        
    public static final String
        JOB_TEMPLATE =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
        "<job name=\"{0}\" >" +
        "<description>{1}</description>" +        "{2}" +       // the community snippet      
        "{3}" +       // variable number of job steps               
        "</job>" ;        
        
    public static final String
        JOBSTEP_TEMPLATE =
        "<jobstep name=\"{0}\" joinCondition=\"{1}\" stepNumber=\"{2}\" sequenceNumber=\"{3}\">" +
        "{4}" +    // tool
        "</jobstep>" ; 
        
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
        "<parameter name=\"{0}\" type=\"{1}\" >" +        "{2}" +                         // possible location data OR possible instream data
        "</parameter>" ;            

} // end of class WorkflowDD
