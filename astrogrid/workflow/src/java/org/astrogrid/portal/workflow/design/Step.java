/*
 * @(#)Step.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.3, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.portal.workflow.design;

import org.astrogrid.portal.workflow.design.activity.*;
import org.apache.log4j.Logger ;
import java.text.MessageFormat ;
import org.w3c.dom.* ;

/**
 * The <code>Step</code> class represents an individual job step with
 * an executable tool.
 * <p>
 * It is the basic unit of currency for a <code>Workflow</code>, since a <code>Workflow</code>, when
 * it is actually run as a job, must execute at least one <code>Step</code> in order to
 * achieve anything. In these terms the <code>Step</code> is the bottom leaf in a <code>Workflow</code> 
 * tree.
 * <p>
 * <code>Step</code> must contain a <code>Tool</code> in order to be viable as an executable unit
 * and must itself be contained in an <code>ActivityContainer</code> such as a <code>Sequence</code> 
 * or a <code>Flow</code> which are themselves attached to a Workflow. However, this state of 
 * consistency is an outcome of the Workflow design process which is facilitated by the overall 
 * Workflow object model. An isolated instance of a <code>Step</code> can be in an inconsistent
 * state until this process of Workflow design has completed.
 * <p><pre><blockquote>
 * A step has some predictable attributes: 
 * . name
 * . description
 * . joinCondition
 * . stepNumber
 * . sequenceNumber
 * </blockquote></pre><p>
 * As described above, it's role in life is to contain a <code>Tool</code>.
 * 
 * @author  Jeff Lusted
 * @version 1.0 25-Aug-2003
 * @see     org.astrogrid.portal.workflow.design.JoinCondition
 * @since   AstroGrid 1.3
 */
public class Step extends Activity {
    
    /** Compile-time switch used to turn tracing on/off. 
      * Set this to false to eliminate all trace statements within the byte code.*/         
    private static final boolean 
        TRACE_ENABLED = true ;
        
    private static Logger 
        logger = Logger.getLogger( Step.class ) ;  
    
    private String
        name = null,
        description = null ;
    private JoinCondition
        joinCondition ;
    private int
        stepNumber = 0,
        sequenceNumber = 0 ;
    private Tool
        tool ;


    /**
      * Creates an empty <code>Step</code> for a given parent <code>Activity</code>
      * <p> 
      * The parent should be a <code>Flow</code> or <code>Sequence</code>, which is
      * a good reason for developing other constructors in the future to make this 
      * typt safe.
      * <p>
      * The default join condition is JoinCondition.ANY
      * 
      **/       
    public Step( Activity parent ) {
        super( parent ) ;
        if( TRACE_ENABLED ) trace( " entry: Step(parent)") ;
        joinCondition = JoinCondition.ANY ; 
        if( TRACE_ENABLED ) trace( " exit: Step(parent)") ;
    }
    
      
    /**
      * Creates a <code>Step</code> for a given parent <code>Activity</code>
      * and fills out the step with details as held in the xml <code>Element</code>
      * <p> 
      * 
      **/       
    public Step( String communitySnippet
               , Element element
               , Activity parent  ) {
        super( parent ) ;
        if( TRACE_ENABLED ) trace( "entry: Step(communitySnippet,element,parent)") ; 
        
        try {
            
            this.name = element.getAttribute( WorkflowDD.STEP_NAME_ATTR ) ;
            
            String
                condition = element.getAttribute( WorkflowDD.STEP_JOINCONDITION_ATTR ) ;
             
            if( condition == null ) { 
                this.joinCondition = JoinCondition.ANY ;    
            }
            else {
                condition.trim() ;
                
                if( condition.equalsIgnoreCase("true") ) {
                    this.joinCondition = JoinCondition.TRUE ;
                }
                else if( condition.equalsIgnoreCase("false") ) {
                    this.joinCondition = JoinCondition.FALSE ;
                }
                else {
                    this.joinCondition = JoinCondition.ANY ; 
                }
                
            }
                      
            this.stepNumber = new Integer( element.getAttribute( WorkflowDD.STEP_STEPNUMBER_ATTR ).trim() ).intValue() ;
            this.sequenceNumber = new Integer( element.getAttribute( WorkflowDD.STEP_SEQUENCENUMBER_ATTR ).trim() ).intValue() ;  
                     
            NodeList
               nodeList = element.getChildNodes() ; 
                           
            for( int i=0 ; i < nodeList.getLength() ; i++ ) {           
                
                if( nodeList.item(i).getNodeType() == Node.ELEMENT_NODE ) {
                    
                    element = (Element) nodeList.item(i) ;
                
                    if ( element.getTagName().equals( WorkflowDD.TOOL_ELEMENT ) ) {
                        this.tool = new Tool( element ) ;   
                    }  
                    
                } // end if
                                
            } // end for        
           
        }
        finally {
            if( TRACE_ENABLED ) trace( "exit: Step(communitySnippet,element,parent)") ;
        }
        
    } // end of Step(communitySnippet,element,parent)
      
    
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setJoinCondition( JoinCondition joinCondition ) {
		this.joinCondition = joinCondition;
	}
    
    public JoinCondition getJoinCondition() {
        return this.joinCondition ;
    }

	public boolean isJoinConditionTrue() {
		return joinCondition == JoinCondition.TRUE ;
	}
    
    public boolean isJoinConditionFalse() {
        return joinCondition == JoinCondition.FALSE ;
    }
    
    public boolean isJoinConditionAny() {
        return joinCondition == JoinCondition.ANY ;
    }

	public void setTool( Tool tool ) {
		this.tool = tool;
	}

	public Tool getTool() {  
		return tool;
	}


    public String toXMLString() {
        if( TRACE_ENABLED ) trace( "Step.toXMLString() entry") ;  
          
        String 
           response = null ;
                                     
        try {
            
            Object []
               inserts = new Object[5] ;
            inserts[0] = this.getName() ;
            inserts[1] = this.getJoinCondition() ;
            inserts[2] = new Integer( this.getStepNumber() ) ;
            inserts[3] = new Integer( this.getSequenceNumber() ) ;
            inserts[4] = ( this.getTool() == null ) ? " " :  this.getTool().toXMLString() ;

            response = MessageFormat.format( WorkflowDD.STEP_TEMPLATE, inserts ) ;

        }
        finally {
            if( TRACE_ENABLED ) trace( "Step.toXMLString() exit") ;    
        }       
        
        return response ;        
        
    } // end toXMLString()
    

    public String toJESXMLString() {
        if( TRACE_ENABLED ) trace( "Step.toJESXMLString() entry") ;  
        
        //JBL Note. There should be some basis checking regarding
        // the existence of a tool ( tool is not null ).
          
        String 
           response = null ;
                                     
        try {
            
            Object []
               inserts = new Object[5] ;
            inserts[0] = this.getName() ;
            inserts[1] = this.getJoinCondition() ;
            inserts[2] = new Integer( this.getStepNumber() ) ;
            inserts[3] = new Integer( this.getSequenceNumber() ) ;
            inserts[4] = this.getTool().toJESXMLString() ;
            
            response = MessageFormat.format( WorkflowDD.JOBSTEP_TEMPLATE, inserts ) ;

        }
        finally {
            if( TRACE_ENABLED ) trace( "Step.toJESXMLString() exit") ;    
        }       
        
        return response ;        
        
    } // end toJESXMLString()

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
    
    private static void trace( String traceString ) {
        System.out.println( traceString ) ;
        // logger.debug( traceString ) ;
    }
    
    private static void debug( String logString ){
        System.out.println( logString ) ;
        // logger.debug( logString ) ;
    }

	public void setStepNumber(int stepNumber) {
		this.stepNumber = stepNumber;
	}

	public int getStepNumber() {
		return stepNumber;
	}

	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public int getSequenceNumber() {
		return sequenceNumber;
	}
   
} // end of class Step
