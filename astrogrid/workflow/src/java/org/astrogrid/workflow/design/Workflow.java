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

package org.astrogrid.workflow.design ;

import java.util.HashMap ;
import java.util.ListIterator;
import java.util.Map ;
import java.util.Collections ;
import java.text.MessageFormat ;

import org.apache.log4j.Logger ;

import org.astrogrid.i18n.*;
import org.astrogrid.workflow.*;
import org.astrogrid.AstroGridException ;
import org.w3c.dom.Document ;

/**
 * The <code>Workflow</code> class represents... 
 * <p>
 *
 * <p>
 * The class... 
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
        ASTROGRIDERROR_= " " ;
        
    static {
        
        try { 
            WKF.getInstance().checkPropertiesLoaded() ;
        }
        catch ( AstroGridException agex ) {
            ; // Message would have already been logged ;
        }
        
    }
        
        
    public static Workflow createWorkflow(  String userid, String community, String name  ) {

        
        return null ;
    }
        
        
    public static Workflow readWorkflow( String userid, String community, String name ) {
        String
            location = Workflow.locateMySpace( userid, community ) ;
            
        // (1) instantiate MySpaceDelegate here, with location    
        // (2) invoke
        
        
        
        return null ;
    }
    
    
    public static boolean deleteWorkflow( String userid, String community, String name  ) {
        String
            location = Workflow.locateMySpace( userid, community ) ;
            
        // (1) instantiate MySpaceDelegate here, with location    
        // (2) invoke
        
        return false ;
    }
    
    
    public static boolean saveWorkflow( Workflow workflow ) {
        String
            location = Workflow.locateMySpace( workflow.getUserid(), workflow.getCommunity() ) ;
            
        // (1) instantiate MySpaceDelegate here, with location    
        // (2) invoke
        return false ;
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
        
        
    public Workflow() {
        super() ;
        if( TRACE_ENABLED ) logger.debug( "Workflow() entry") ;  
        this.activities.put( this.getKey(), this ) ;
        if( TRACE_ENABLED ) logger.debug( "Workflow() exit") ;
    }
    
    
    public Workflow( String key ) {
        super( key ) ;
        if( TRACE_ENABLED ) logger.debug( "Workflow(String) entry") ;
        this.activities.put( this.getKey(), this ) ;
        if( TRACE_ENABLED ) logger.debug( "Workflow(String) exit") ;
    }
    
    
    public Activity getActivity( String key ) {
        if( TRACE_ENABLED ) logger.debug( "getActivity() entry") ; 
        try { 
            return (Activity)activities.get( key ) ;
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
 
 
} // end of class Workflow