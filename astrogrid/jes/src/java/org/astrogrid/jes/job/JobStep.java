/*
 * @(#)JobStep.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.jes.job;

import org.w3c.dom.* ;
import org.apache.log4j.Logger;
import org.astrogrid.jes.jobcontroller.*;
// import org.astrogrid.jes.JES ;
import org.astrogrid.Configurator ;
// import org.astrogrid.jes.i18n.*;


public class JobStep {
	
	private static final boolean 
		TRACE_ENABLED = true ;
	
	private static Logger 
		logger = Logger.getLogger( JobStep.class ) ;
        
    private final static String 
        SUBCOMPONENT_NAME = Configurator.getClassName( Job.class );                          
		
	public static  final String
		STATUS_INITIALIZED = "INITIALIZED",  // Created but not yet running
		STATUS_RUNNING = "RUNNING",          // Currently executing
		STATUS_COMPLETED = "COMPLETED",      // Completed OK
		STATUS_IN_ERROR = "ERROR" ;          // Something bad happened
        
    public static final String
        JOINCONDITION_TRUE = "true",
        JOINCONDITION_FALSE = "false",
        JOINCONDITION_ANY = "any" ;
		
    private Integer
        stepNumber = null ;
	 
	private String
		name = null ;
	    
	private Query
		query = null ;
		
	private String
	    status = null,
	    comment = null ;
        
    //JBL added Iteration 3
    private Integer
        sequenceNumber = null ;
        
    //JBL added Iteration 3    
    private String
        joinCondition = null ;
		
	private Job
	    parent = null ;
	
	
	public JobStep( Job parent  ) {
		this.parent = parent ;
	}
		
		
	public JobStep( Job parent, Element element ) throws JobException {
		if( TRACE_ENABLED ) logger.debug( "JobStep(): entry") ; 
		
		this.parent = parent ;
        
        String
            attribute = null;
		
		try {
		 
		   setName(element.getAttribute( SubmissionRequestDD.JOBSTEP_NAME_ATTR )) ;
		   setStatus( STATUS_INITIALIZED ) ;
		   setComment( "" ) ;
           
           //JBL we should set the StepNumber from the XML here as well!!!
           attribute = element.getAttribute( SubmissionRequestDD.JOBSTEP_STEPNUMBER_ATTR ) ;
           logger.debug( "step number: " + attribute ) ;
           if( attribute != null ) {
               this.setStepNumber( new Integer( attribute.trim() ) ) ;
           }
           
           //JBL added iteration 3 - SequenceNumber and JoinCondition
           attribute = element.getAttribute( SubmissionRequestDD.JOBSTEP_SEQUENCENUMBER_ATTR ) ;
           logger.debug( "sequence number: " + attribute ) ;
           if( attribute != null ) {
               setSequenceNumber( new Integer( attribute.trim() ) ) ;
           }
           attribute = element.getAttribute( SubmissionRequestDD.JOBSTEP_JOINCONDITION_ATTR ) ;
           logger.debug( "join condition: " + attribute ) ;
           if( attribute != null && attribute.trim() != "" ) {
               setJoinCondition( attribute.trim() ) ;
           }
           else {
               setJoinCondition( JOINCONDITION_ANY ) ;
           }
           		
		   NodeList
			  nodeList = element.getChildNodes() ;
		   Element
			  queryChild = null ;
			   
		   for( int i=0 ; i < nodeList.getLength() ; i++ ) {
		   				
		   	   if( nodeList.item(i).getNodeType() == Node.ELEMENT_NODE ) {	
		   	   
			       queryChild = (Element) nodeList.item(i) ;
			       if( queryChild.getTagName().equals( SubmissionRequestDD.QUERY_ELEMENT ) ) 
			           query = new Query( this, queryChild ) ;
			           
			   }
			   
		   } // end for
		   
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "JobStep(): exit") ; 			 
		}

	} // end of constructor JobStep(Element)
		
    public Query getQuery(){ return this.query ; }
    public void setQuery(Query query){ this.query = query ; }

	public void setName(String name) { this.name = name; }
	public String getName() { return  ( name == null  ?  ""  :  name.trim() ) ; }

	public void setParent(Job parent) { this.parent = parent; }
	public Job getParent() { return parent; }

	public void setStepNumber(int stepNumber) {	this.stepNumber = new Integer( stepNumber ) ; }
	public void setStepNumber( Integer stepNumber ) { this.stepNumber = stepNumber ; }
	public Integer getStepNumber() { return stepNumber ; }

	public void setStatus(String status) { this.status = status; }
	public String getStatus() {	return status; }

	public void setComment(String comment) { this.comment = comment; }
	public String getComment() { return ( comment == null  ?  ""  :  comment.trim() ) ; }

    //JBL added iteration 3
	public void setSequenceNumber(int sequenceNumber) {	this.sequenceNumber = new Integer( sequenceNumber ) ; }
    public void setSequenceNumber(Integer sequenceNumber) { this.sequenceNumber = sequenceNumber; }
	public Integer getSequenceNumber() { return sequenceNumber;	}

    //JBL added iteration 3
	public void setJoinCondition( String joinCondition ) { this.joinCondition = joinCondition ; }
    public String getJoinCondition() { return joinCondition ; }

    
} // end of class JobStep 
