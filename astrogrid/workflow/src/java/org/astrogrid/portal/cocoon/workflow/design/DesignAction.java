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

import org.astrogrid.community.delegate.policy.PolicyServiceDelegate;
import org.astrogrid.community.policy.data.PolicyPermission ;

// import org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate;
import org.astrogrid.portal.workflow.*;
import org.astrogrid.portal.workflow.design.*;
import org.astrogrid.portal.workflow.design.activity.*;

import org.astrogrid.community.common.util.CommunityMessage;

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

    /** Compile-time switch used to turn certain debugging statements on/off. 
      * Set this to false to eliminate these debugging statements within the byte code.*/         
    private static final boolean 
        DEBUG_ENABLED = true ;

    private static Logger 
        logger = Logger.getLogger( DesignAction.class ) ; 
        
    private static final String
        ASTROGRIDERROR_WORKFLOW_PERMISSION_DENIED = "AGWKFE00050",
        ASTROGRIDERROR_PASSTHRU = "AGWKFE00100" ;  
        
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
	public static final String 
        CONFIRM_PARAM_TAG = "confirm" ;
	
    public static final String 
        USERID_COMMUNITY_SEPARATOR = "@" ;

	public static final String 
	    TEMPLATE_PARAM_TAG = "template", 
	    EMPTY_TEMPLATE = "none_selected" ;	
    
    public static final String
        HTTP_WORKFLOW_TAG = "workflow-tag" ,
        COMMUNITY_ACCOUNT_TAG = "community-account" ,
        CREDENTIAL_TAG = "credential",
        COMMUNITY_TOKEN_TAG = "community-token" ;
        
    public static final String
        WORKFLOW_NAME_PARAMETER = "workflow-name",  
	    WORKFLOW_DESCRIPTION_PARAMETER = "workflow-description" ;
        
    public static final String
        EDIT_CONDITION_PARAMETER = "edit-condition",
        ACTIVITY_KEY_PARAMETER = "activity-key",
        WORKFLOW_LIST_PARAMETER = "workflow-list",
        QUERY_LIST_PARAMETER = "query-list",
		STEP_KEY_PARAMETER = "step-key",
        ERROR_MESSAGE_PARAMETER = "ErrorMessage";
        
    public static final String
        QUERY_NAME_PARAMETER = "query-name",
        QUERY_DESCRIPTION_PARAMETER = "query-description" ;
    
    public static final String 
        ACTION_CREATE_WORKFLOW = "create-workflow" ,
        ACTION_SAVE_WORKFLOW = "save-workflow" ,
        ACTION_READ_WORKFLOW = "read-workflow" ,
        ACTION_DELETE_WORKFLOW = "delete-workflow" ,
        ACTION_CREATE_WORKFLOW_FROM_TEMPLATE = "create-workflow-from-template" ,
        ACTION_SUBMIT_WORKFLOW = "submit-workflow" ,
        ACTION_READ_WORKFLOW_LIST = "read-workflow-list" ,
        ACTION_CHOOSE_QUERY = "choose-query" ,
        ACTION_EDIT_JOINCONDITION = "edit-join-condition",
        ACTION_READ_QUERY = "read-query",
        ACTION_READ_QUERY_LIST = "read-query-list",
        ACTION_INSERT_QUERY_INTO_STEP = "insert-query-into-step" ;
        
    public static final String
        AUTHORIZATION_RESOURCE_WORKFLOW = "workflow" ,
        AUTHORIZATION_ACTION_EDIT = "edit" ;

    /**
    * Our action method.
    *
    */
    public Map act ( Redirector redirector
                   , SourceResolver resolver
                   , Map objectModel
                   , String source
                   , Parameters params ) {                  
        if( TRACE_ENABLED ) trace( "DesignAction.act() entry" ) ;  
        
        DesignActionImpl
            myAction = null ;
        Map
            retMap = null ;
        
        try { 
            debug( "About to check properties loaded") ;
            // Load the workflow config file and messages...
            WKF.getInstance().checkPropertiesLoaded() ;
            debug( "Properties loaded OK") ;
            
            myAction = new DesignActionImpl( redirector
                                           , resolver
                                           , objectModel
                                           , source
                                           , params ) ;
                                           
            retMap = myAction.act() ;    
                                          
        }
        catch ( AstroGridException agex ) {
            debug( agex.toString() ) ;
        }
        finally {
            if( TRACE_ENABLED ) trace( "DesignAction.act() exit" ) ;  
        }
                                        
        return retMap ; 
 
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
            community,
            group,
            token ;
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
            
                // Load current Workflow - if any - from our HttpSession.
                this.workflow = (Workflow) session.getAttribute( HTTP_WORKFLOW_TAG ) ;
            
                // Get user and community 
                this.retrieveUserDetails() ;
            
                this.action = request.getParameter( ACTION_PARAM_TAG ) ;
                this.bConfirm = new Boolean ( request.getParameter(CONFIRM_PARAM_TAG) ).booleanValue() ;
                    
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
					if ( template.equals( EMPTY_TEMPLATE ) ) {
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
                else if( action.equals( ACTION_READ_WORKFLOW_LIST ) ) {
                    this.readWorkflowList() ; 
                }
                else if( action.equals( ACTION_READ_QUERY_LIST ) ) {
                    this.readQueryList() ; 
                }
				else if( action.equals( ACTION_INSERT_QUERY_INTO_STEP ) ) {
					this.insertQueryIntoStep() ;                     								
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
                tag  = null,
                useridCommunity = null ;
            int
                ampersandIndex ;

            try {
        
                // JL Note: Iteration 3 way of doing things...
                useridCommunity = (String)session.getAttribute( COMMUNITY_ACCOUNT_TAG ) ;
                ampersandIndex = useridCommunity.indexOf( USERID_COMMUNITY_SEPARATOR ) ;
                this.userid = useridCommunity.substring(  0, ampersandIndex ) ;
                this.community = useridCommunity.substring( ampersandIndex + 1 ); 
                this.group = (String)session.getAttribute( CREDENTIAL_TAG ) ;
                this.token = (String)session.getAttribute( COMMUNITY_TOKEN_TAG ) ;
                
                if( this.workflow != null ) {
//                  workflow.setUserid( userid ) ;
//                  workflow.setCommunity( community ) ;
                    workflow.setGroup( group ) ;
                    workflow.setToken( token ) ;  
                }
 
/*          
                JL Note: This is PortalB Iteration 2 way of doing things,...
                    
                tag = params.getParameter( USER_PARAM_NAME ) ;
                this.userid = (String) session.getAttribute( tag ) ;
				tag = params.getParameter( COMMUNITY_PARAM_TAG ) ;
				this.community = (String) session.getAttribute( tag ) ;
                         
            }
            catch( ParameterException pex ) {
                ; // some logging here
            
*/
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
                    workflow = Workflow.readWorkflow( userid, community, communitySnippet(), name ) ; 
                }
                else if( workflow.isDirty() == false ) {
                    workflow = Workflow.readWorkflow( userid, community, communitySnippet(), name ) ; 
                }
                else if( bConfirm == true ) {
                    workflow = Workflow.readWorkflow( userid, community, communitySnippet(), name ) ;
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
                    Workflow.deleteWorkflow( userid, community, communitySnippet(), name ) ; 
                }
                else if( workflow.isDirty() == false ) {
                    Workflow.deleteWorkflow( userid, community, communitySnippet(), name ) ;  ; 
                }
                else if( bConfirm == true ) {
                    Workflow.deleteWorkflow( userid, community, communitySnippet(), name ) ;  ;
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
         
        } // end of createWorkflowFromTemplate()
        
        
        private void submitWorkflow() throws ConsistencyException {
            if( TRACE_ENABLED ) trace( "DesignActionImpl.submitWorkflow() entry" ) ;

            try {
                
                if( workflow == null ) {
                    ; // some logging here
                    throw new ConsistencyException() ; 
                }
                else {
                    Workflow.submitWorkflow( workflow ) ;
                }          
            }
            finally {
                if( TRACE_ENABLED ) trace( "DesignActionImpl.submitWorkflow() exit" ) ;
            }
         
        } // end of submitQuery()
        
        
        
        private void chooseQuery() throws ConsistencyException {
            if( TRACE_ENABLED ) trace( "DesignActionImpl.chooseQuery() entry" ) ;
              
            Step
                step = null;
            String
                activityKey = null,
                queryName = null ;
              
            try {
                
                if( workflow == null ) {
                     throw new ConsistencyException() ; 
                 }
                 
                activityKey = request.getParameter( ACTIVITY_KEY_PARAMETER ) ;
                queryName = request.getParameter( QUERY_NAME_PARAMETER ) ;
                    
                 if( activityKey == null || queryName == null ) {
                     ; // some logging here
                     throw new ConsistencyException() ;
                 }
             
                 Activity
                     activity = workflow.getActivity( activityKey ) ;
                    
                 if( activity instanceof Step ) {
                     step = (Step)activity ;
                     Workflow.insertQueryIntoStep( step, queryName ) ;
                 }
                 else {
                     throw new ConsistencyException() ;
                 }          
            }
            finally {
                if( TRACE_ENABLED ) trace( "DesignActionImpl.chooseQuery() exit" ) ;
            }
                    
        } // end of chooseQuery()
        
        
        private void editJoinCondition() throws ConsistencyException {
            if( TRACE_ENABLED ) trace( "DesignActionImpl.editJoinCondition() entry" ) ;
              
            Step
                step ;
            JoinCondition 
                joinCondition ;
              
            try {
                
                if( workflow == null ) {
                    throw new ConsistencyException() ; 
                }
                
                String
                    activityKey = request.getParameter( ACTIVITY_KEY_PARAMETER ),
                    editCondition = request.getParameter( EDIT_CONDITION_PARAMETER ) ;
                    
                if( activityKey == null || editCondition == null ) {
                    ; // some logging here
                    throw new ConsistencyException() ;
                }
                
                if( editCondition.equalsIgnoreCase( JoinCondition.ANY.toString() ) ) {
                    joinCondition =  JoinCondition.ANY ;
                }
                else if( editCondition.equalsIgnoreCase( JoinCondition.TRUE.toString() ) ) {
                    joinCondition =  JoinCondition.TRUE ;
                }
                else if( editCondition.equalsIgnoreCase( JoinCondition.FALSE.toString() ) ) {
                    joinCondition =  JoinCondition.FALSE ;
                }
                else {
                    ; // some logging here
                    throw new ConsistencyException() ; 
                }
             
                Activity
                    activity = workflow.getActivity( activityKey ) ;
                    
                if( activity instanceof Step ) {
                    step = (Step)activity ;
                    step.setJoinCondition( joinCondition ) ;
                }
                else {
                    throw new ConsistencyException() ;
                }
                               
            }
            finally {
                if( TRACE_ENABLED ) trace( "DesignActionImpl.editJoinCondition() exit" ) ;
            }
         
        } // end of editJoinCondition()
            
           
        private void readWorkflowList() {
            if( TRACE_ENABLED ) trace( "DesignActionImpl.readWorkflowList() entry" ) ;
              
            try {
                    
                // For the moment this is where we have placed the door.
                // If users cannot see a list, then they cannot do anything...
                this.checkPermissions( AUTHORIZATION_RESOURCE_WORKFLOW
                                     , AUTHORIZATION_ACTION_EDIT ) ;
                               
                //NB: The filter argument is ignored at present (Sept 2003).
                Iterator
                    iterator =  Workflow.readWorkflowList( userid
                                                         , community
                                                         , "*"
                                                         , communitySnippet() ) ;
                this.request.setAttribute( WORKFLOW_LIST_PARAMETER, iterator ) ;               
            }
            catch( WorkflowException wfex ) {
                
                this.request.setAttribute( ERROR_MESSAGE_PARAMETER, wfex.toString() ) ;
                
            }
            finally {
                if( TRACE_ENABLED ) trace( "DesignActionImpl.readWorkflowList() exit" ) ;
            }
                    
        } // end of readWorkflowList()   
           
   
        private void readQueryList() {
            if( TRACE_ENABLED ) trace( "DesignActionImpl.readQueryList() entry" ) ;
              
            try {
//              NB: The filter argument is ignored at present (Sept 2003).
                Iterator
                    iterator =  Query.readQueryList( userid, community, communitySnippet(), "*" ) ;
                this.request.setAttribute( QUERY_LIST_PARAMETER, iterator ) ;               
            }
            finally {
                if( TRACE_ENABLED ) trace( "DesignActionImpl.readQueryList() exit" ) ;
            }
                    
        } // end of readQueryList()   

           
		private void insertQueryIntoStep() throws ConsistencyException {
			if( TRACE_ENABLED ) trace( "DesignActionImpl.insertQueryIntoStep() entry" ) ;
			
			boolean 
				response = false;
              
			try {
				String
					queryName = request.getParameter( WORKFLOW_NAME_PARAMETER ) ;
					
				String
					stepKey = request.getParameter( STEP_KEY_PARAMETER ) ;
                
                if ( queryName == null ) {
                	debug( "queryName is null" ) ;
                }
                else if ( stepKey == null) {
                	debug( "stepKey is null" ) ;
                }
                else {
				    response = Workflow.insertQueryIntoStep( stepKey, queryName, workflow ) ;
                }   
				
				if ( response == false ) {
					; // some logging here
					throw new ConsistencyException() ;					          
				}
			}
			finally {
				if( TRACE_ENABLED ) trace( "DesignActionImpl.insertQueryIntoStep() exit" ) ;
			}
                    
		} // end of insertQueryIntoStep() 
        
        
        
        private void checkPermissions ( String someResource, String anAction ) throws WorkflowException {
            if( TRACE_ENABLED ) trace( "DesignActionImpl.checkPermission() entry" ) ;
                        
            PolicyServiceDelegate 
                ps = null ;
            String
                communityAccount = null,
                credential = null ;
            
            try {
                
                ps = new PolicyServiceDelegate() ;
                communityAccount =  (String) session.getAttribute( COMMUNITY_ACCOUNT_TAG ) ;
                credential = (String) session.getAttribute( CREDENTIAL_TAG ) ;
                boolean 
                    authorized = ps.checkPermissions( communityAccount
                                                    , credential
                                                    , someResource
                                                    , anAction ) ;             
               
                if( !authorized ) {
                    
                    PolicyPermission pp = ps.getPolicyPermission();
                    
                    String
                        reason = pp.getReason() ;
                        
                    AstroGridMessage
                        message = new AstroGridMessage( ASTROGRIDERROR_PASSTHRU
                                                      , "Community"
                                                      , reason ) ;
                     
                    throw new WorkflowException( message ) ;
                        
                }
               
            }
            catch( WorkflowException wfex ) {
                
                AstroGridMessage 
                    message = new AstroGridMessage( ASTROGRIDERROR_WORKFLOW_PERMISSION_DENIED
                                                  , WKF.getClassName( this.getClass() )
                                                  , wfex.getAstroGridMessage().toString() ) ;
                     
                throw new WorkflowException( message, (Exception)wfex ) ;
                
            }
            catch( Exception ex ) {
                
                if( DEBUG_ENABLED) ex.printStackTrace();  
               
                String
                    localizedMessage = ex.getLocalizedMessage() ;    
               
               AstroGridMessage
                   message = new AstroGridMessage( ASTROGRIDERROR_WORKFLOW_PERMISSION_DENIED
                                                 , WKF.getClassName( this.getClass() )
                                                 , (localizedMessage == null) ? "" : localizedMessage ) ;
                     
               throw new WorkflowException( message, ex ) ;
               
            }
            finally {
                if( TRACE_ENABLED ) trace( "DesignActionImpl.checkPermission() exit" ) ;  
            }
             
        } // end of checkPermission()
  		
        
        private String communitySnippet() {
        
            return CommunityMessage.getMessage(
                    (String)session.getAttribute( CREDENTIAL_TAG ),
                    (String)session.getAttribute( COMMUNITY_ACCOUNT_TAG ),
                    (String)session.getAttribute( CREDENTIAL_TAG ) );
                    
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