/*
 * @(#)DesignAction.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.3, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.portal.cocoon.workflow.design ;

import org.apache.log4j.Logger ;

import org.astrogrid.i18n.*;

// import org.astrogrid.workflow.*;
// import org.astrogrid.workflow.design.activity.*;

import org.astrogrid.AstroGridException ;

import org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate;
import org.astrogrid.portal.workflow.design.*;
import org.astrogrid.portal.workflow.design.activity.*;


import java.io.File;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import org.xml.sax.EntityResolver;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.parameters.ParameterException ;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.environment.ObjectModelHelper;

/**
 * A Cocoon action to handle our workflow design commands.
 *
 */
public class DesignAction extends AbstractAction {
    
    /** Compile-time switch used to turn tracing on/off. 
      * Set this to false to eliminate all trace statements within the byte code.*/         
    private static final boolean 
        TRACE_ENABLED = true ;
        
    private static Logger 
        logger = Logger.getLogger( DesignAction.class ) ; 
        
    private static final String
        ASTROGRIDERROR_SOMEMESSAGE = "AGWKFE00050" ; // none so far 


	/**
	 * Cocoon param for the user param in the session.
	 *
	 */
	public static final String USER_PARAM_NAME = "user-param" ;


	/**
	 * Http request param for the action.
	 *
	 */
	public static final String ACTION_PARAM_TAG = "action" ;

	/**
	 * Http request param to confirm an action.
	 *
	 */
	public static final String CONFIRM_PARAM_TAG = "confirm" ;
    
    public static final String
        HTTP_WORKFLOW_TAG = "" ;
        
    public static final String
        WORKFLOW_NAME_PARAMETER = "" ; 

    public static final String 
        ACTION_CREATE_WORKFLOW = "create-workflow" ,
        ACTION_SAVE_WORKFLOW = "save-workflow" ,
        ACTION_READ_WORKFLOW = "read-workflow" ,
        ACTION_DELETE_WORKFLOW = "delete-workflow" ,
        ACTION_CREATE_WORKFLOW_FROM_TEMPLATE = "create-workflow-from-template" ,
        ACTION_READ_WORKFLOW_LIST = "read-workflow-list" ,
        ACTION_CHOOSE_QUERY = "choose-query" ,
        ACTION_EDIT_JOINCONDITION = "edit-join-condition" ;


    /**
    * Our action method.
    *
    */
    public Map act ( Redirector redirector
                   , SourceResolver resolver
                   , Map objectModel
                   , String source
                   , Parameters params ) {
                   
        if( TRACE_ENABLED ) trace( "act() entry" ) ;  
        
        DesignActionImpl
            myAction = new DesignActionImpl( redirector
                                           , resolver
                                           , objectModel
                                           , source
                                           , params ) ;
                                           
        return myAction.act() ; 
 
    } // end of act() 

    private class DesignActionImpl {
        
        private Redirector
            redirector ;
        private SourceResolver
            resolver ;
        private Map
            objectModel,
            results ;
        private String
            source ;
        private Parameters
            params ;
        private Request
            request ;
        private Session
            session ;
        private String
            userid,
            community ;
        private String
            action ;
        private boolean
            bConfirm ;
        private Workflow
            workflow ;
        
        public DesignActionImpl( Redirector redirector
                               , SourceResolver resolver
                               , Map objectModel
                               , String source
                               , Parameters params ) {                                   
            if( TRACE_ENABLED ) trace( "DesignActionImpl() entry" ) ; 
            
            try {
          
                this.redirector = redirector ;
                this.resolver = resolver ;
                this.objectModel = objectModel ;
                this.source = source ;
                this.params = params ; 
            
                this.results = new HashMap() ;
            
                // Get current request and session.
                this.request = ObjectModelHelper.getRequest( objectModel ) ;
                this.session = request.getSession() ;
            
                // Get user and community 
                this.retrieveUserDetails() ;
            
                this.action = request.getParameter( ACTION_PARAM_TAG ) ; 
                this.bConfirm = new Boolean ( request.getParameter(CONFIRM_PARAM_TAG) ).booleanValue() ;
                
                // Load current Workflow - if any - from our HttpSession.
                this.workflow = (Workflow) session.getAttribute( HTTP_WORKFLOW_TAG ) ;
                
            }
            finally {
                if( TRACE_ENABLED ) trace( "DesignActionImpl() exit" ) ; 
            }
              
        } // end of DesignActionImpl()
        
        
        public Map act() {
            if( TRACE_ENABLED ) trace( "DesignActionImpl.act() entry" ) ;      
        
            try {
                
                this.consistencyCheck() ;
                       
                if( action.equals( ACTION_CREATE_WORKFLOW ) ) {
                    this.createWorkflow() ;
                }
                else if( action.equals( ACTION_SAVE_WORKFLOW ) ) { 
                    this.saveWorkflow() ;
                }
                else if( action.equals( ACTION_READ_WORKFLOW ) ) {
                    this.readWorkflow() ; 
                }
                else if( action.equals( ACTION_DELETE_WORKFLOW ) ) {
                    this.deleteWorkflow() ; 
                }
                else if( action.equals( ACTION_CREATE_WORKFLOW_FROM_TEMPLATE ) ) {
                    this.createWorkflowFromTemplate() ; 
                }
                else if( action.equals( ACTION_CHOOSE_QUERY ) ) {
                    this.chooseQuery() ; 
                }
                else if( action.equals( ACTION_EDIT_JOINCONDITION ) ) {
                    this.editJoinCondition() ; 
                }
                else {
                    ; // unsupported action
                }
            }
            catch( ConsistencyException cex ) {
                ;
            }
            finally {
                if( TRACE_ENABLED ) trace( "DesignActionImpl.act() exit" ) ;  
            }
    
            return results ;
            
        } // end of DesignActionImpl.act()
        
        
        private void retrieveUserDetails() {
            if( TRACE_ENABLED ) trace( "DesignActionImpl.act() entry" ) ;   
                     
            String 
                tag  = null ;

            try {
                tag = params.getParameter( USER_PARAM_NAME ) ;
                userid = (String) session.getAttribute( tag ) ;
                community = "leicester" ; //JBL ???????????????
            }
            catch( ParameterException pex ) {
                ; // some logging here
            }
            finally {
                if( TRACE_ENABLED ) trace( "DesignActionImpl.act() exit" ) ;  
            }
                
        } // end of retrieveUserDetails()
        
        
        private void consistencyCheck() throws ConsistencyException {
            
            if( userid == null ) {
                ; // redirection required 
                throw new ConsistencyException() ;
            }
            else if( action == null ) {
                
                throw new ConsistencyException() ;
     
            }

        }
        
        
        private void createWorkflow() throws ConsistencyException {
            if( TRACE_ENABLED ) trace( "DesignActionImpl.() entry" ) ;
              
            try {
                
                String
                    name = request.getParameter( WORKFLOW_NAME_PARAMETER ) ;
                    
                if( name == null ) {
                    ; // some logging here
                    throw new ConsistencyException() ;
                }
                
                if( workflow == null ) {
                    Workflow.createWorkflow( userid, community, name ) ; 
                }
                else if( workflow.isDirty() && (bConfirm == true) ) {
                    Workflow.createWorkflow( userid, community, name ) ;
                }
        
            }
            finally {
                if( TRACE_ENABLED ) trace( "DesignActionImpl.() exit" ) ;
            }
            
        } // end of createWorkflow()
        
        
        private void saveWorkflow() throws ConsistencyException {
            if( TRACE_ENABLED ) trace( "DesignActionImpl.() entry" ) ;
              
            try {

                if( workflow == null ) {
                    ; // some logging here
                    throw new ConsistencyException() ; 
                }
                else {
                    Workflow.saveWorkflow( workflow ) ;
                }            
            }
            finally {
                if( TRACE_ENABLED ) trace( "DesignActionImpl.() exit" ) ;
            }
         
        } // end of createWorkflow()
        
        
        private void readWorkflow() throws ConsistencyException {
            if( TRACE_ENABLED ) trace( "DesignActionImpl.() entry" ) ;
              
            try {
                
                String
                    name = request.getParameter( WORKFLOW_NAME_PARAMETER ) ;
                    
                if( name == null ) {
                    ; // some logging here
                    throw new ConsistencyException() ;
                }
                
                if( workflow == null ) {
                    workflow = Workflow.readWorkflow( userid, community, name ) ; 
                }
                else if( workflow.isDirty() == false ) {
                    workflow = Workflow.readWorkflow( userid, community, name ) ; 
                }
                else if( bConfirm == true ) {
                    workflow = Workflow.readWorkflow( userid, community, name ) ;
                }        
                     
            }
            finally {
                if( TRACE_ENABLED ) trace( "DesignActionImpl.() exit" ) ;
            }
         
        }
        
        
        private void deleteWorkflow() throws ConsistencyException {
            if( TRACE_ENABLED ) trace( "DesignActionImpl.() entry" ) ;
              
            try {
                
                String
                    name = request.getParameter( WORKFLOW_NAME_PARAMETER ) ;
                    
                if( name == null ) {
                    ; // some logging here
                    throw new ConsistencyException() ;
                }
                
                if( workflow == null ) {
                    Workflow.deleteWorkflow( userid, community, name ) ; 
                }
                else if( workflow.isDirty() == false ) {
                    Workflow.deleteWorkflow( userid, community, name ) ;  ; 
                }
                else if( bConfirm == true ) {
                    Workflow.deleteWorkflow( userid, community, name ) ;  ;
                }                              
            }
            finally {
                if( TRACE_ENABLED ) trace( "DesignActionImpl.() exit" ) ;
            }
         
        }
        
        
        private void createWorkflowFromTemplate() {
            if( TRACE_ENABLED ) trace( "DesignActionImpl.() entry" ) ;
              
            try {
                           
            }
            finally {
                if( TRACE_ENABLED ) trace( "DesignActionImpl.() exit" ) ;
            }
         
        }
        
        
        private void chooseQuery() {
            if( TRACE_ENABLED ) trace( "DesignActionImpl.() entry" ) ;
              
            try {
                           
            }
            finally {
                if( TRACE_ENABLED ) trace( "DesignActionImpl.() exit" ) ;
            }
                    
        }
        
        
        private void editJoinCondition() throws ConsistencyException {
            if( TRACE_ENABLED ) trace( "DesignActionImpl.() entry" ) ;
              
            try {
                
                if( workflow == null ) {
                    throw new ConsistencyException() ; 
                }
 /*               
                String
                    activityKey = request.getParameter( "" ),
                    editCondition = request.getParameter( "" ).toLowerCase() ;
                    
                if( activityKey == null || editCondition == null ) {
                    ; // some logging here
                    throw new ConsistencyException() ;
                }
                
                Activity
                    activity = workflow.getActivity( activityKey ) ;
                    
                if( activity == )
                
                if( workflow.isDirty() == false ) {
                    Workflow.deleteWorkflow( userid, community, name ) ;  ; 
                }
                else if( bConfirm == true ) {
                    Workflow.deleteWorkflow( userid, community, name ) ;  ;
                } 
*/                          
            }
            finally {
                if( TRACE_ENABLED ) trace( "DesignActionImpl.() exit" ) ;
            }
         
        }
            
            
    } // end of inner class DesignActionImpl
    
    
    private class ConsistencyException extends Exception {
    }
    
    
    private void trace( String traceString ) {
        logger.debug( traceString ) ;
    }
    
    private void debug( String logString ){
        logger.debug( logString ) ;
    }
             
            
} // end of class DesignAction 