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

// import org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate;
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
	 * Cocoon param for the user param in the session.
	 *
	 */
	public static final String COMMUNITY_PARAM_TAG = "community" ;

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
	
	/**
	 * Http request param for the action.
	 *
	 */
	public static final String TEMPLATE_PARAM_TAG = "template" ;	
    
    public static final String
        HTTP_WORKFLOW_TAG = "workflow-tag" ;
        
    public static final String
        WORKFLOW_NAME_PARAMETER = "workflow-name" ; 
    public static final String    
	    WORKFLOW_DESCRIPTION_PARAMETER = "workflow-description" ;

    public static final String 
        ACTION_CREATE_WORKFLOW = "create-workflow" ,
        ACTION_SAVE_WORKFLOW = "save-workflow" ,
        ACTION_READ_WORKFLOW = "read-workflow" ,
        ACTION_DELETE_WORKFLOW = "delete-workflow" ,
        ACTION_CREATE_WORKFLOW_FROM_TEMPLATE = "create-workflow-from-template" ,
        ACTION_SUBMIT_WORKFLOW = "submit-workflow" ,
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
		private String
			template ;            
        
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
            	
				debug( "action is: " + action ) ;
                
                this.consistencyCheck() ;
                    
                if( action == null ){
                    debug( "action is null") ;  
                }      
                if( action.equals( ACTION_CREATE_WORKFLOW ) ) {
					template = request.getParameter( TEMPLATE_PARAM_TAG ) ; 
					if ( template == null ) {
						this.createWorkflow() ;
					}
                    else {
						this.createWorkflowFromTemplate( template ) ; 
                    }
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
//                else if( action.equals( ACTION_CREATE_WORKFLOW_FROM_TEMPLATE ) ) {
//                    this.createWorkflowFromTemplate() ; 
//                }
                else if( action.equals( ACTION_SUBMIT_WORKFLOW ) ) {
                    this.submitWorkflow() ; 
                }
                else if( action.equals( ACTION_CHOOSE_QUERY ) ) {
                    this.chooseQuery() ; 
                }
                else if( action.equals( ACTION_EDIT_JOINCONDITION ) ) {
                    this.editJoinCondition() ; 
                }
                else {
                    debug( "unsupported action") ; 
                }
                
                if (workflow != null ){
	                // Save the workflow in the session object...
    	            debug( "about to set session attribute..." ) ;
        	        session.setAttribute( HTTP_WORKFLOW_TAG, workflow ) ;
            	    debug( session.getAttribute(HTTP_WORKFLOW_TAG).toString() ) ;
                }
            }
            catch( ConsistencyException cex ) {
                debug( "ConsistencyException occurred");
            }
            //JBL Note: these should only be here during testing...
            catch( Exception ex) {
                debug( "Exception: ex" ) ;
                ex.printStackTrace() ;
            }
            //JBL Note: these should only be here during testing...
            catch( Throwable th ) {
                debug( "Throwable th" ) ;
                th.printStackTrace() ;
            }
            finally {
                if( TRACE_ENABLED ) trace( "DesignActionImpl.act() exit" ) ;  
            }
    
            return results ;
            
        } // end of DesignActionImpl.act()
        
        
        private void retrieveUserDetails() {
            if( TRACE_ENABLED ) trace( "DesignActionImpl.retrieveUserDetails() entry" ) ;   
                     
            String 
                tag  = null ;

            try {
                tag = params.getParameter( USER_PARAM_NAME ) ;
                this.userid = (String) session.getAttribute( tag ) ;
				tag = params.getParameter( COMMUNITY_PARAM_TAG ) ;
				this.community = (String) session.getAttribute( tag ) ;
            }
            catch( ParameterException pex ) {
                ; // some logging here
            }
            finally {
                if( TRACE_ENABLED ) trace( "DesignActionImpl.retrieveUserDetails() exit" ) ;  
            }
                
        } // end of retrieveUserDetails()
        
        
        private void consistencyCheck() throws ConsistencyException {
			if( TRACE_ENABLED ) trace( "consistencyCheck() entry" ) ;
			debug( "userid: " + userid ) ;
			debug( "community: " + community ) ;
			debug( "name: "  ) ; 
			debug( "description: "  ) ; 
            
            
            
            if( userid == null ) {
                ; // redirection required 
                throw new ConsistencyException() ;
            }
            else if( action == null ) {
                
                debug( "action is null" ) ;
                // throw new ConsistencyException() ;
     
            }
			if( TRACE_ENABLED ) trace( "consistencyCheck()) exit" ) ;
        }
        
        
        private void createWorkflow() throws ConsistencyException {
            if( TRACE_ENABLED ) trace( "DesignActionImpl.createWorkflow() entry" ) ;
              
            try {
                
                String
                    name = request.getParameter( WORKFLOW_NAME_PARAMETER ) ;
                    
				String
					description = request.getParameter( WORKFLOW_DESCRIPTION_PARAMETER ) ;                    
                    
                if( name == null ) {
                    ; // some logging here
                    throw new ConsistencyException() ;
                }
                
				if( description == null ) {
					description = "no description entered" ;
				}                
                
                if( workflow == null ) {
                    workflow = Workflow.createWorkflow( userid, community, name, description ) ;
                    // workflow.setDescription( description ) ; 
                }
                else if( workflow.isDirty() && (bConfirm == true) ) {
                    workflow = Workflow.createWorkflow( userid, community, name, description ) ;
                    // workflow.setDescription( description ) ;
                }
                else if( !workflow.isDirty() ) {
                    workflow = Workflow.createWorkflow( userid, community, name, description ) ;
                    // workflow.setDescription( description ) ;
                }
                else {
                    debug( "Create ignored - bConfirm == false" ) ;
                }
        
            }
            finally {
                if( TRACE_ENABLED ) trace( "DesignActionImpl.createWorkflow() exit" ) ;
            }
            
        } // end of createWorkflow()
        
        
        private void saveWorkflow() throws ConsistencyException {
            if( TRACE_ENABLED ) trace( "DesignActionImpl.saveWorkflow() entry" ) ;
              
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
                if( TRACE_ENABLED ) trace( "DesignActionImpl.saveWorkflow() exit" ) ;
            }
         
        } // end of createWorkflow()
        
        
        private void readWorkflow() throws ConsistencyException {
            if( TRACE_ENABLED ) trace( "DesignActionImpl.readWorkflow() entry" ) ;
              
            try {
                
                String
                    name = request.getParameter( WORKFLOW_NAME_PARAMETER ) ;
                    
				String
					description = request.getParameter( WORKFLOW_DESCRIPTION_PARAMETER ) ;                    
                  
                // name = "jeff1" ;
                    
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
                debug( "userid: " + workflow.getUserid() ) ;
                debug( "community: " + workflow.getCommunity() ) ;
                debug( "name: " + workflow.getName() ) ; 
                debug( "description: " + workflow.getDescription() ) ;      
                    
            }
            finally {
                if( TRACE_ENABLED ) trace( "DesignActionImpl.readWorkflow() exit" ) ;
            }
         
        }
        
        
        private void deleteWorkflow() throws ConsistencyException {
            if( TRACE_ENABLED ) trace( "DesignActionImpl.deleteWorkflow() entry" ) ;
              
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
                if( TRACE_ENABLED ) trace( "DesignActionImpl.deleteWorkflow() exit" ) ;
            }
         
        }
        
        
        private void createWorkflowFromTemplate( String template ) throws ConsistencyException {
            if( TRACE_ENABLED ) trace( "DesignActionImpl.createWorkflowFromTemplate() entry" ) ;
            debug ( "template: " + template ) ;
              
            try {
				String
					name = request.getParameter( WORKFLOW_NAME_PARAMETER ) ;
				String
					description = request.getParameter( WORKFLOW_DESCRIPTION_PARAMETER ) ;                    
                    
				if( name == null ) {
					; // some logging here
					throw new ConsistencyException() ;
				}
                
				if( description == null ) {
					description = "no description entered" ;
				}                
       
				if( workflow == null ) {
					workflow = Workflow.createWorkflowFromTemplate( userid, community, name, description, template  ) ; 
				}
				else if( workflow.isDirty() && (bConfirm == true) ) {
				    workflow = Workflow.createWorkflowFromTemplate( userid, community, name, description, template ) ;
				}
				else if( !workflow.isDirty() ) {
					workflow = Workflow.createWorkflowFromTemplate( userid, community, name, description, template ) ;
				}
				else {
					debug( "Create ignored - bConfirm == false" ) ;
				}                           
            }
            finally {
                if( TRACE_ENABLED ) trace( "DesignActionImpl.createWorkflowFromTemplate() exit" ) ;
            }
         
        }
        
        
        private void submitWorkflow() {
            if( TRACE_ENABLED ) trace( "DesignActionImpl.submitWorkflow() entry" ) ;
              
            try {
                           
            }
            finally {
                if( TRACE_ENABLED ) trace( "DesignActionImpl.submitWorkflow() exit" ) ;
            }
         
        }
        
        
        
        private void chooseQuery() {
            if( TRACE_ENABLED ) trace( "DesignActionImpl.chooseQuery() entry" ) ;
              
            try {
                           
            }
            finally {
                if( TRACE_ENABLED ) trace( "DesignActionImpl.chooseQuery() exit" ) ;
            }
                    
        }
        
        
        private void editJoinCondition() throws ConsistencyException {
            if( TRACE_ENABLED ) trace( "DesignActionImpl.editJoinCondition() entry" ) ;
              
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
                if( TRACE_ENABLED ) trace( "DesignActionImpl.editJoinCondition() exit" ) ;
            }
         
        }
            
            
    } // end of inner class DesignActionImpl
    
    
    private class ConsistencyException extends Exception {
    }
    
    
    private void trace( String traceString ) {
        // logger.debug( traceString ) ;
        System.out.println( traceString ) ;
    }
    
    private void debug( String logString ){
        // logger.debug( logString ) ;
        System.out.println( logString ) ;
    }
             
            
} // end of class DesignAction 