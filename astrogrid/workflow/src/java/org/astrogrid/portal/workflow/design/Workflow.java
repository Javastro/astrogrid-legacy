/*
 * @(#)Workflow.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.3, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
 
package org.astrogrid.portal.workflow.design;

import java.util.HashMap ;
import java.util.ListIterator;
import java.util.Map ;
import java.util.Collections ;
import java.text.MessageFormat ;

import org.apache.log4j.Logger ;

import org.astrogrid.i18n.*;
import org.astrogrid.workflow.*;
import org.astrogrid.workflow.design.activity.*;
import org.astrogrid.AstroGridException ;

import org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate;
import org.astrogrid.portal.workflow.*;
import org.astrogrid.portal.workflow.design.activity.*;
import org.w3c.dom.Document ;

/**
 * The <code>Workflow</code> class represents a complex tree of Activities.
 * The following is a crude representation of the inheritance hierarchy.
 * Once a Workflow is instantiated, it forms the root of an arbitrarilly
 * complex tree of Steps, Flows and Sequences.
 * 
 * 
 * Activity ( Abstract )
 * |
 * |
 * |__Step ( Can only be a bottom leaf )
 * |   
 * |
 * |__ActivityContainer ( Abstract. Can contain any number of any instances
 *    |                   of an Activity apart from a Workflow itself )
 *    |
 *    |__Flow
 *    |
 *    |
 *    |__Sequence 
 *       |
 *       |
 *       |__Workflow
 * 
 * 
 * Workflow contains some static factory methods for manipulating Workflows
 * on a persistent basis.
 * 
 * A Workflow instance contains sufficient methods to manipulate its internal 
 * structure (adding and subtracting an Activity, and so on).
 * 
 * There is one highly significant fact that is critical to navigating around
 * Workflow from the top level. Each instance of an Activity has a unique 
 * key and Workflow maintains a collection that maps key to Activity. If you
 * know the key, you can navigate to the Activity straight from the top level
 * irrespective of the complexity of the tree. @see getActivity() method. 
 * From an Activity it is relatively trivial to find parent and children in context.  
 * 
 *
 * @author  Jeff Lusted
 * @version 1.0 21-Aug-2003
 * @see     
 * @see     
 * @since   AstroGrid 1.3
 */
public class Workflow extends Sequence {
    
    /** Compile-time switch used to turn tracing on/off. 
      * Set this to false to eliminate all trace statements within the byte code.*/         
    private static final boolean 
        TRACE_ENABLED = true ;
        
    private static Logger 
        logger = Logger.getLogger( Workflow.class ) ; 
        
    private static final String
        ASTROGRIDERROR_SOMEMESSAGE = "AGWKFE00050" ; // none so far 
        
    private static MySpaceManagerDelegate
        mySpace ;
        
    static {
        
        try { 
            // Loads the workflow config file and messages...
            WKF.getInstance().checkPropertiesLoaded() ;
        }
        catch ( AstroGridException agex ) {
            ; // Message would have already been logged ;
        }
        
    }
        
        
    public static Workflow createWorkflow(  String userid, String community, String name  ) {
        if( TRACE_ENABLED ) logger.debug( "createWorkflow() entry") ;   
           
        Workflow
            workflow ;
            
        try {
            workflow = new Workflow() ;
            workflow.setUserid( userid) ;
            workflow.setCommunity( community ) ;
            workflow.setName( name ) ;
        }
        finally {
            if( TRACE_ENABLED ) logger.debug( "createWorkflow() exit") ; 
        }
 
        return workflow ;
        
    } // end createWorkflow()
        
        
    public static Workflow readWorkflow( String userid, String community, String name ) {
        if( TRACE_ENABLED ) logger.debug( "readWorkflow() entry") ; 
        
        Workflow
            workflow = null;
         
        try {
        
            MySpaceManagerDelegate
                mySpace = new MySpaceManagerDelegate( Workflow.locateMySpace( userid, community ) ) ;
                
            //JBL format`the MySpace request here
            
            String
                workflowXML = mySpace.lookupDataHolderDetails( "" ) ;
                
            workflow = new Workflow( workflowXML ) ;
        
        }
        catch ( Exception ex ) {
        }
        finally {
            if( TRACE_ENABLED ) logger.debug( "readWorkflow() exit") ; 
        }
       
        return workflow ;
        
    }
    
    
    public static boolean deleteWorkflow( String userid, String community, String name  ) {
        if( TRACE_ENABLED ) logger.debug( "deleteWorkflow() entry") ; 
        
        boolean
            retValue = false ;
         
        try {
        
            MySpaceManagerDelegate
                mySpace = new MySpaceManagerDelegate( Workflow.locateMySpace( userid, community ) ) ;
                
            //JBL format`the MySpace request here
            
            String
                responseXML = mySpace.deleteDataHolder( "" ) ;
                
            //JBL decode the response here...
        
        }
        catch ( Exception ex ) {
        }
        finally {
            if( TRACE_ENABLED ) logger.debug( "deleteWorkflow() exit") ; 
        }
        
        return retValue ;
        
    }
    
    
    public static boolean saveWorkflow( Workflow workflow ) {
        if( TRACE_ENABLED ) logger.debug( "saveWorkflow() entry") ; 
        
     boolean
         retValue = false ;
         
     try {
        
         //JBL serialize the Workflow to a suiteable XML file to be picked
         // up by MySpace. This is where we make use of the toXML() method
         // to pull the whole tree together...
        
        
         MySpaceManagerDelegate
             mySpace = new MySpaceManagerDelegate( Workflow.locateMySpace( workflow.getUserid()
                                                                         , workflow.getCommunity() ) ) ;
              
         //JBL format the MySpace request here
         
            
         String
             responseXML = mySpace.upLoad( "" ) ;
                
         //JBL decode the response here...
        
     }
     catch ( Exception ex ) {
     }
     finally {
         if( TRACE_ENABLED ) logger.debug( "saveWorkflow() exit") ; 
     }
        
     return retValue ;

    }
    
    
    //JBL Note: Is this a misnomer on my part? Should we be attempting this? Is it required?  
    public static ListIterator readWorkflowList() {
        return null ;
    }
    
     
    private String
        name,
        userid,
        community ;
        
    private Map 
        activities = Collections.synchronizedMap( new HashMap() ) ;
        
    /**
      * <p> 
      * Default constructor.
      * <p>
      * Gives a workflow with an empty sequence.
      * 
      **/           
    public Workflow() {
        super() ;
        if( TRACE_ENABLED ) logger.debug( "Workflow() entry") ;  
        this.activities.put( this.getKey(), this ) ;
        if( TRACE_ENABLED ) logger.debug( "Workflow() exit") ;
    }
    
    
    /**
      * <p> 
      * Constructor using XML.
      * <p>
      * Gives a workflow as described by the XML
      * 
      * 
      * @param workflowXML - An XML description. Could be from MySpace
      * or from a template loaded from a config file.
      * 
      * @see 
      **/        
    public Workflow( String workflowXML ) {
        super() ;
        if( TRACE_ENABLED ) logger.debug( "Workflow(String) entry") ;
        
        try{
            this.activities.put( this.getKey(), this ) ;
        
            //JBL todo: Parse XML string to document...
        
            //JBL todo: Fill object model from document... 
            
        }
        finally {
            if( TRACE_ENABLED ) logger.debug( "Workflow(String) exit") ;
        }
        
    }
    
    
    /**
      * <p> 
      * A navigational aid. Navigates straight to the Activity 
      * given a key without having to know how to traverse the Worlflow.
      * In effect, this gets you straight into context.
      * <p>
      * 
      * @param String key - the key of the activity
      * 
      * @see 
      **/     
    public Activity getActivity( String key ) {
        if( TRACE_ENABLED ) logger.debug( "getActivity() entry") ; 
        try { 
            return (Activity)activities.get( new ActivityKey( key ) ) ;
        }
        finally {
            if( TRACE_ENABLED ) logger.debug( "getActivity() exit") ;  
        }
    }
    
    
    
    
    
    
    
    public boolean putActivity( Activity activity ) {
        if( TRACE_ENABLED ) logger.debug( "putActivity() entry") ;  
               
        boolean
            retValue = false ;
        
        try {    
        
            if( !activities.containsKey( activity.getKey() ) ) {
                this.activities.put( activity.getKey(), activity ) ;
                retValue  = true ;
            }
       
        }
        finally {
            if( TRACE_ENABLED ) logger.debug( "putActivity() exit") ;  
        }
       
        return retValue ;
        
    } // end putActivity() 
   

    public boolean removeActivity( Activity activity ) {
        return ( activities.remove( activity.getKey() ) == null ? false : true ) ;
    }
    
    
    public String toXMLString() {
        if( TRACE_ENABLED ) logger.debug( "toXMLString() entry") ;  
          
        String 
           response = WKF.getProperty( WKF.WORKFLOW_CATEGORY
                                     , WKF.WORKFLOW_XML_TEMPLATE ) ;
                                     
        try {
            
            Object []
               inserts = new Object[4] ;
            inserts[0] = this.name ;
            inserts[1] = this.userid ;
            inserts[2] = this.community ;
            inserts[3] = super.toXMLString() ;
            
            response = MessageFormat.format( response, inserts ) ;

        }
        catch ( Exception ex ) {
            AstroGridMessage
                message = new AstroGridMessage( "" // ASTROGRIDERROR_FAILED_TO_FORMAT_RESPONSE
                                              , WKF.getClassName( this.getClass() ) ) ; 
            logger.error( message.toString(), ex ) ;
        } 
        finally {
            if( TRACE_ENABLED ) logger.debug( "toXMLString() exit") ;    
        }       
        
        return response ;                              
        
    } // end of toXMLString()


	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUserid() {
		return userid;
	}

	public void setCommunity(String community) {
		this.community = community;
	}

	public String getCommunity() {
		return community;
	}
    
    private static String locateMySpace( String userid, String community ) {
        if( TRACE_ENABLED ) logger.debug( "locateMySpace() entry") ;
        try {
            return WKF.getProperty( WKF.MYSPACE_URL, WKF.MYSPACE_CATEGORY ) ;
        }
        finally {
            if( TRACE_ENABLED ) logger.debug( "locateMySpace() exit") ;
        }
    }
    
    
/*     
    private Document parseRequest( String jobXML ) throws DatasetAgentException {   
        if( TRACE_ENABLED ) logger.debug( "parseRequest() entry") ;
        
        Document 
           queryDoc = null;
        DocumentBuilderFactory 
           factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder 
           builder = null;
           
        try {
                    
           factory.setValidating( Boolean.getBoolean( DTC.getProperty( DTC.DATASETAGENT_PARSER_VALIDATION
                                                                     , DTC.DATASETAGENT_CATEGORY )  )  ) ;      
           builder = factory.newDocumentBuilder();
           logger.debug( jobXML ) ;
           InputSource
              jobSource = new InputSource( new StringReader( jobXML ) );
           queryDoc = builder.parse( jobSource );
        }
        catch ( Exception ex ) {
            AstroGridMessage
                message = new AstroGridMessage( ASTROGRIDERROR_FAILED_TO_PARSE_JOB_REQUEST
                                              , this.getComponentName() ) ; 
            logger.error( message.toString(), ex ) ;
            throw new DatasetAgentException( message, ex );
        } 
        finally {
            if( TRACE_ENABLED ) logger.debug( "parseRequest() exit") ;  
        }
        
        return queryDoc ;

    } // end parseRequest()
*/
 
} // end of class Workflow