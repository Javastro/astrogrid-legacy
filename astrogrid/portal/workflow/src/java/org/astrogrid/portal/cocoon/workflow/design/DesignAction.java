/*
 * @(#)DesignAction.java   1.5
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.3, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.portal.cocoon.workflow.design;

import org.apache.log4j.Logger;  
   
import org.astrogrid.portal.myspace.filesystem.Tree;
import org.astrogrid.portal.common.session.AstrogridSessionFactory ;
import org.astrogrid.portal.common.session.AstrogridSession ;
import org.astrogrid.portal.common.session.AttributeKey ;

import org.astrogrid.portal.workflow.intf.*;
import org.astrogrid.portal.cocoon.workflow.WorkflowHelper;

import org.astrogrid.applications.beans.v1.parameters.ParameterValue;

import org.astrogrid.workflow.beans.v1.AbstractActivity;
import org.astrogrid.workflow.beans.v1.ActivityContainer;
import org.astrogrid.workflow.beans.v1.Catch;
import org.astrogrid.workflow.beans.v1.Else;
import org.astrogrid.workflow.beans.v1.Flow;
import org.astrogrid.workflow.beans.v1.For;
import org.astrogrid.workflow.beans.v1.If;
import org.astrogrid.workflow.beans.v1.Parfor;
import org.astrogrid.workflow.beans.v1.Sequence;
import org.astrogrid.workflow.beans.v1.Set;
import org.astrogrid.workflow.beans.v1.Script;
import org.astrogrid.workflow.beans.v1.Scope;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Then;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Try;
import org.astrogrid.workflow.beans.v1.Unset;
import org.astrogrid.workflow.beans.v1.While;
import org.astrogrid.workflow.beans.v1.Workflow;

import org.astrogrid.applications.beans.v1.Interface;
import org.astrogrid.applications.beans.v1.InterfacesType;

import org.astrogrid.community.beans.v1.Credentials;
import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.community.beans.v1.Group;

import org.astrogrid.config.Config;
import org.astrogrid.config.SimpleConfig;

import org.astrogrid.community.client.policy.service.PolicyServiceDelegate;
import org.astrogrid.community.client.policy.service.PolicyServiceMockDelegate;
import org.astrogrid.community.client.policy.service.PolicyServiceSoapDelegate;

import org.astrogrid.community.common.policy.data.PolicyPermission  ;
import org.astrogrid.community.common.policy.data.PolicyCredentials ;
import org.astrogrid.community.common.ivorn.CommunityIvornParser;
import org.astrogrid.community.common.exception.CommunityIdentifierException;
import org.astrogrid.community.common.exception.CommunityServiceException;
import org.astrogrid.community.common.exception.CommunityPolicyException;

import org.astrogrid.community.User;
import org.astrogrid.store.Ivorn;

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Enumeration;
import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.environment.ObjectModelHelper;

// import com.sun.rsasign.s;


/**
 * A Cocoon action to handle our workflow design commands.
 *
 */
public class DesignAction extends AbstractAction {

  /** Compile-time switch used to turn tracing on/off. 
    * Set this to false to eliminate all trace statements within the byte code.*/
  private static final boolean TRACE_ENABLED = true;

  /** Compile-time switch used to turn certain debugging statements on/off. 
    * Set this to false to eliminate these debugging statements within the byte code.*/
  private static final boolean DEBUG_ENABLED = true;

	private static Logger logger = Logger.getLogger(DesignAction.class);
        
	private static final String ASTROGRIDERROR_WORKFLOW_PERMISSION_DENIED =
		"AGWKFE00050",
		ASTROGRIDERROR_PASSTHRU = "AGWKFE00100";  
        
    /**
     * Name of JNDI property holding security delegate endpoint URL
     */
    public static final String ORG_ASTROGRID_PORTAL_COMMUNITY_URL = "org.astrogrid.portal.community.url";        
        
	/**
	 * Cocoon param for the user param in the session.
	 *
	 */
	public static final String USER_PARAM_NAME = "user-param";

	/**
	 * Cocoon param for the user param in the session.
	 *
	 */
	public static final String COMMUNITY_PARAM_TAG = "community_account";

	/**
	 * Http request param for the action.
	 *
	 */
	public static final String ACTION_PARAM_TAG = "action";

	/**
	 * Http request param to confirm an action.
	 *
	 */
	public static final String CONFIRM_PARAM_TAG = "confirm";
	
    public static final String USERID_COMMUNITY_SEPARATOR = "@";

	public static final String TEMPLATE_PARAM_TAG = "template", 
	    EMPTY_TEMPLATE = "none_selected";	
    
    public static final String
        HTTP_WORKFLOW_TAG = "workflow-tag",
        HTTP_TOOL_TAG = "tool-tag",
        USER_TAG = "user",
        COMMUNITY_ACCOUNT_TAG = "community_account",
        COMMUNITY_NAME_TAG = "community_name",
        CREDENTIAL_TAG = "credential",
        COMMUNITY_TOKEN_TAG = "token";
        
    public static final String
        WORKFLOW_NAME_PARAMETER = "workflow-name",
		WORKFLOW_NEW_NAME_PARAMETER = "workflow-new-name",  
	    WORKFLOW_DESCRIPTION_PARAMETER = "workflow-description",
        WORKFLOW_IVORN_PARAMETER = "workflow-ivorn";
        
    public static final String
        EDIT_CONDITION_PARAMETER = "edit_condition",
        WORKFLOW_LIST_PARAMETER = "workflow-list",
        QUERY_LIST_PARAMETER = "query-list",
	    TOOL_LIST_PARAMETER = "tool-list",
	    USER_TOOL_LIST_PARAMETER = "user-tool-list",
	    TOOL_NAME_PARAMETER = "tool_name",
		STEP_KEY_PARAMETER = "step-key",
        ERROR_MESSAGE_PARAMETER = "ErrorMessage",
        LOCATION_PARAMETER = "location",
	    PARAM_NAME_PARAMETER = "param-name",
	    PARAM_VALUE_PARAMETER = "param-value",
        ORIG_PARAM_VALUE_PARAMETER = "original-param-value",
        IVORN_VALUE_PARAMETER = "ivorn-value",        
        DIRECTION_PARAMETER = "direction",
	    SET_VALUE_PARAMETER = "set-value",
	    SET_VAR_PARAMETER = "set-var",
	    UNSET_VAR_PARAMETER = "unset-var",
	    STEP_NAME_PARAMETER = "step_name",
	    STEP_VAR_PARAMETER = "step_var",
	    STEP_DESCRIPTION_PARAMETER = "step_description",
	    SCRIPT_DESCRIPTION_PARAMETER = "script_description",
	    SCRIPT_BODY_PARAMETER = "script_body",
	    PARAM_INDIRECT = "param_indirect",
	    FOR_ITEMS_PARAMETER = "for_item",
	    FOR_VAR_PARAMETER = "for_get",
	    IF_TEST_PARAMETER = "if_test",
	    WHILE_TEST_PARAMETER = "while_test",
	    COPY_ACTIVITY_KEY = "copy_activity_key";
        
    public static final String
        TOOLS_CACHE = "tools-cache-",
        WORKFLOW_SUBMIT_MESSAGE = "workflow_submit_message";
        
    public static final String
        FLOW_KEY_PARAMETER = "flow-key",
        SEQUENCE_KEY_PARAMETER = "sequence-key",
        ACTIVITY_INDEX_PARAMETER = "activity_index_key",
	    ACTIVITY_KEY_PARAMETER = "activity_key",
	    INSERTED_ACTIVITY_KEY_PARAMETER = "inserted_activity_key",
	    PARENT_ACTIVITY_KEY_PARAMETER = "parent_activity_key",	    
	    ACTIVITY_ORDER_PARAMETER = "activity_index_order",
	    ACTIVITY_TYPE_PARAMETER = "activity_type",
	    INSERT_ACTIVITY_TYPE_PARAMETER = "insert_activity_type",
	    PARAMATER_SEPARATOR_PARAM = "++" ;
        
    public static final String
        QUERY_NAME_PARAMETER = "query-name",
        QUERY_DESCRIPTION_PARAMETER = "query-description";
    
    public static final String 
        ACTION_CREATE_WORKFLOW = "create-workflow",
        ACTION_SAVE_WORKFLOW = "save-workflow",
        ACTION_READ_WORKFLOW = "read-workflow",
        ACTION_DELETE_WORKFLOW = "delete-workflow",
        ACTION_CREATE_WORKFLOW_FROM_TEMPLATE = "create-workflow-from-template",
        ACTION_SUBMIT_WORKFLOW = "submit-workflow",
	    ACTION_EDIT_WORKFLOW = "edit-workflow",
        ACTION_READ_WORKFLOW_LIST = "read-workflow-list",
        ACTION_READ_TOOL_LIST = "read-tool-list",        
		ACTION_COPY_WORKFLOW = "copy-workflow",
        ACTION_CHOOSE_QUERY = "choose-query",
		ACTION_ADD_NAME_DESCRIPTION = "update workflow details",
        ACTION_READ_QUERY = "read-query",
        ACTION_READ_QUERY_LIST = "read-query-list",
	    ACTION_CREATE_TOOL = "create-tool-for-step",
        ACTION_INSERT_TOOL_INTO_STEP = "insert-tool-into-step",
        ACTION_INSERT_STEP = "insert-step",
	    ACTION_INSERT_SCRIPT = "insert-script",
	    ACTION_INSERT_SCRIPT_DETAILS = "update script details",
	    ACTION_INSERT_ACTIVITY = "insert_activity",
	    ACTION_INSERT_FOR_DETAILS = "update for loop details",
	    ACTION_INSERT_IF_DETAILS = "update if details",
	    ACTION_INSERT_WHILE_DETAIL = "update while loop details",
	    ACTION_INSERT_PARFOR_DETAILS = "update parallel for loop details",	
	    ACTION_INSERT_SET_DETAILS = "update set details", 
	    ACTION_INSERT_UNSET_DETAILS = "update unset details",   
	    ACTION_INSERT_STEP_DETAILS = "update step details",	         
		ACTION_INSERT_PARAMETER = "insert-parameter-value",
	    ACTION_INSERT_INPUT_PARAMETER_INTO_TOOL = "insert-input-parameter-into-tool",
	    ACTION_INSERT_OUTPUT_PARAMETER_INTO_TOOL = "insert-output-parameter-into-tool",
	    ACTION_INSERT_INPUT_PARAMETER = "insert-input-value",
	    ACTION_INSERT_OUTPUT_PARAMETER = "insert-output-value",
	    ACTION_RESET_PARAMETER = "reset-parameter",
	    ACTION_REMOVE_TOOL_FROM_STEP = "remove-tool-from-step", 	    
        ACTION_READ_LISTS = "read-lists",
        ACTION_REMOVE_WORKFLOW_FROM_SESSION = "remove-workflow-from-session",
        ACTION_RESET_WORKFLOW = "reset-workflow",
        ACTION_REMOVE_ACTIVITY = "remove_activity",
	    SAVE_WORKFLOW_IVORN_PARAMETER = "save-workflow-ivorn",
	    OPEN_WORKFLOW_IVORN_PARAMETER = "open-workflow-ivorn";
        
    public static final String
        AUTHORIZATION_RESOURCE_WORKFLOW = "workflow" ,
        AUTHORIZATION_ACTION_EDIT = "edit" ;

	public static final String        
	    INPUT_PARAMETER_COUNT_PARAM = "input_param_count",
	    OUTPUT_PARAMETER_COUNT_PARAM = "output_param_count";

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
            
            myAction = new DesignActionImpl( redirector
                                           , resolver
                                           , objectModel
                                           , source
                                           , params ) ;
                                           
            retMap = myAction.act() ;    
                                          
        }
        catch ( Exception ex ) {
            ex.printStackTrace();
        }
        finally {
            if( TRACE_ENABLED ) trace( "DesignAction.act() exit" ) ;  
        }
                                        
        return retMap ; 
 
    } // end of act() 

    private class DesignActionImpl {
        
        private Redirector redirector;
        private SourceResolver resolver;
        private Map objectModel, results;
        private String source;
        private Parameters params;
        private Request request;
        private Session session;
        private String userid, community, group, token;
        private String action;
        private boolean bConfirm;
        private WorkflowManager workflowManager;
        private Workflow workflow;
		private String template; 
        private Credentials credentials;
        private User user;         
        
        public DesignActionImpl( Redirector redirector
                               , SourceResolver resolver
                               , Map objectModel
                               , String source
                               , Parameters params ) {                                   
            if( TRACE_ENABLED ) trace( "DesignActionImpl() entry" ) ; 
            
            try {
          
                this.redirector = redirector;
                this.resolver = resolver;
                this.objectModel = objectModel;
                this.source = source;
                this.params = params; 
            
                this.results = new HashMap();
            
                // Get current request and session.
                this.request = ObjectModelHelper.getRequest( objectModel );
                this.session = request.getSession();
            
                // Load current Workflow - if any - from our HttpSession.
                this.workflow = (Workflow) session.getAttribute( HTTP_WORKFLOW_TAG );

                // Load Worflow manager in case we should need it...  
//                Config conf = SimpleConfig.getSingleton();
//                conf.setProperty(WorkflowManagerFactory.WORKFLOW_STORE_KEY,"file");
//                
//                conf.setProperty(WorkflowManagerFactory.WORKFLOW_APPLIST_KEY,"xml");
//                conf.setProperty(WorkflowManagerFactory.WORKFLOW_APPLIST_XML_URL_KEY,"file://localhost:8080/home/jl99/downloads/test-tool-list.xml");
//                
//                
//                
//                conf.setProperty(WorkflowManagerFactory.WORKFLOW_JES_ENDPOINT_KEY,"urn:test");
//                assert(conf != null);
//                WorkflowManagerFactory fac = new WorkflowManagerFactory(conf);
//                assert(fac != null);
//                WorkflowManager man = fac.getManager();
//                assert(man != null );
////                assert(man.getJobExecutionService() != null );
//                assert(man.getToolRegistry() != null );
//                assert(man.getWorkflowBuilder() != null );
//                assert(man.getWorkflowStore() != null );                    
                WorkflowManagerFactory wmFactory = new WorkflowManagerFactory();
                this.workflowManager = wmFactory.getManager() ;
            
                // Get user and community 
                this.retrieveUserDetails();
            
                this.action = request.getParameter( ACTION_PARAM_TAG );
                this.bConfirm = new Boolean ( request.getParameter(CONFIRM_PARAM_TAG) ).booleanValue();
                    
            }
            catch( WorkflowInterfaceException wix ) {
                wix.printStackTrace();
            }
            finally {
                if( TRACE_ENABLED ) trace( "DesignActionImpl() exit" ); 
            }
              
        } // end of DesignActionImpl()
        
        
        public Map act() {
            if( TRACE_ENABLED ) trace( "DesignActionImpl.act() entry" );      
        
            try {
            	
				String errorMessage = null;
            	
                if( action == null ) {
                    action = ACTION_CREATE_WORKFLOW;
                }
				debug( "action is: " + action );
                
                this.consistencyCheck();
                    
                if( action == null ){
                    debug( "action is null");  
                }      
                else if( action.equals( ACTION_CREATE_WORKFLOW ) ) {
                    this.createWorkflow();
                }
                else if( action.equals( ACTION_SAVE_WORKFLOW ) ) { 
                    this.saveWorkflow();                   
                }
                else if( action.equals( ACTION_READ_WORKFLOW ) ) {
                    this.readWorkflow(); 
                }
				else if( action.equals( ACTION_EDIT_WORKFLOW ) ) {
					this.readWorkflow(); 
				}                
                else if( action.equals( ACTION_SUBMIT_WORKFLOW ) ) {
                    this.submitWorkflow(); 
                }
				else if( action.equals( ACTION_ADD_NAME_DESCRIPTION ) ) {
					this.addWorkflowNameAndDescription(); 
				}			
                else if( action.equals( ACTION_READ_TOOL_LIST ) ) {
                    this.readToolList(); 
                }
				else if( action.equals( ACTION_READ_LISTS ) ) {
					this.readLists(); 
				}
			    else if( action.equals( ACTION_INSERT_INPUT_PARAMETER ) ) {
					this.insertInputValue();         	
				}                
				else if( action.equals( ACTION_INSERT_OUTPUT_PARAMETER ) ) {
					this.insertOutputValue();
				}
				else if( action.equals( ACTION_INSERT_PARAMETER ) ) {
					this.insertMultipleValue();
				}
				else if( action.equals( ACTION_RESET_PARAMETER ) ) {
				    this.resetParameter();         	
    			}
    			else if( action.equals( ACTION_REMOVE_TOOL_FROM_STEP )){
    				this.removeToolFromStep() ;
    			}
				else if( action.equals( ACTION_CREATE_TOOL ) ) {
					this.createTool();                     								
				}
				else if( action.equals( ACTION_INSERT_STEP_DETAILS )){
					this.insertStepDetails();  
				}
				else if( action.equals( ACTION_REMOVE_WORKFLOW_FROM_SESSION )){
					this.removeWorkflow();  
				}
				else if( action.equals( ACTION_RESET_WORKFLOW )){
					this.removeWorkflow() ;
					this.createWorkflow() ;  
				}																
				else if( action.equals( ACTION_INSERT_FOR_DETAILS ) ) {
					this.insertForDetails();                                                    
				}
				else if( action.equals( ACTION_INSERT_IF_DETAILS ) ) {
					this.insertIfDetails();                                                    
				}
				else if( action.equals( ACTION_INSERT_PARFOR_DETAILS ) ) {
					this.insertParforDetails();                                                    
				}
				else if( action.equals( ACTION_INSERT_SET_DETAILS ) ) {
					this.insertSetDetails();                                                    
				}
				else if( action.equals( ACTION_INSERT_UNSET_DETAILS ) ) {
					this.insertUnsetDetails();                                                    
				}
				else if( action.equals( ACTION_INSERT_SCRIPT_DETAILS ) ) {
					this.insertScriptDetails();                                                    
				}
				else if( action.equals( ACTION_INSERT_WHILE_DETAIL ) ) {
					this.insertWhileDetails();                                                    
				}				
				else if( action.equals( ACTION_INSERT_ACTIVITY ) ) {
						this.insertActivity();                                                    
				}																													
                else if( action.equals( ACTION_REMOVE_ACTIVITY ) ) {
                    this.removeActivity();                                                    
                }       			                
                else {
                    results = null;
                    debug( "unsupported action"); 
                    throw new UnsupportedOperationException( action + " no longer supported");
                }
                				
				this.readToolList();
                
                if (workflow != null ){
	                // Save the workflow in the session object...
					debug( "==========================================" ) ;
    	            debug( "about to add workflow to session object..." ) ;
					debug( "Using workflow: " + workflow.getName() ) ;
        	        session.setAttribute( HTTP_WORKFLOW_TAG, workflow ) ;
            	    debug( session.getAttribute(HTTP_WORKFLOW_TAG).toString() ); 
                }
                
//				this.readLists() ; // Ensure request object contains latest Workflow/Query

            }
            catch( ConsistencyException cex ) {
                results = null;
                debug( "ConsistencyException occurred");
            }
            //JBL Note: these should only be here during testing...
            catch( Exception ex) {
                results = null;
                debug( "Exception: ex" );
                ex.printStackTrace();
            }
            //JBL Note: these should only be here during testing...
            catch( Throwable th ) {
                results = null;
                debug( "Throwable th" );
                th.printStackTrace();
            }
            finally {
                if( TRACE_ENABLED ) trace( "DesignActionImpl.act() exit" );  
            }
    
            return results;
            
        } // end of DesignActionImpl.act()
        
        
        private void retrieveUserDetails() {
            if( TRACE_ENABLED ) trace( "DesignActionImpl.retrieveUserDetails() entry" );   
                     
            try {
                
                // 22nd April 2004.JBL note. This is Jeff's quick fix...
                // fullUserid contains something like...
                // ivo://org.atrogrid.localhost/frog
                String fullUserid = (String)session.getAttribute( USER_TAG );
                this.userid = fullUserid.substring( fullUserid.lastIndexOf('/')+1 );
                this.community = fullUserid.substring( fullUserid.indexOf('/')+2, fullUserid.lastIndexOf('/') );
                this.group = this.community;
             
                debug( "userid: " + this.userid );
//                this.community = (String)session.getAttribute( COMMUNITY_NAME_TAG );
                debug( "community: " + this.community );
//                this.group = (String)session.getAttribute( CREDENTIAL_TAG );
                debug( "group: " + this.group ); 
//                SecurityToken secToken =
//                   (SecurityToken)session.getAttribute( COMMUNITY_TOKEN_TAG );
//                this.token = secToken.getToken();
                debug( "token: " + this.token ); 
                            
                Account account = new Account();
                account.setName( userid );
                account.setCommunity( community );
                
                Group group = new Group();
                group.setName( this.userid );
                group.setCommunity( community );
                        
                this.credentials = new Credentials();
                credentials.setAccount( account );
                credentials.setGroup( group );
                credentials.setSecurityToken( "dummy" );
                
                //JL Late change - 26/04/2004
                this.user = new User( this.userid, this.community, this.group, this.token );
                     
            }
            finally {
                if( TRACE_ENABLED ) trace( "DesignActionImpl.retrieveUserDetails() exit" );  
            }
                
        } // end of retrieveUserDetails()
        
        
        private void consistencyCheck() throws ConsistencyException {
			if( TRACE_ENABLED ) trace( "consistencyCheck() entry" );
			debug( "userid: " + this.userid );
			debug( "community: " + this.community ); 
            
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
                
                WorkflowBuilder workflowBuilder = workflowManager.getWorkflowBuilder();
                String
                    name = request.getParameter( WORKFLOW_NAME_PARAMETER ),
					description = request.getParameter( WORKFLOW_DESCRIPTION_PARAMETER ) ;                    
                    
                if( name == null ) {
                    name = "new workflow";
                }
                
				if( description == null ) {
					description = "no description entered" ;
				}                
                
                if( workflow == null ) {
                    workflow = workflowBuilder.createWorkflow( credentials, name, description ) ;
                }
                else if( bConfirm == true ) {
                    workflow = workflowBuilder.createWorkflow( credentials, name, description ) ;
                }
                else {
                    debug( "Use workflow that already exists in session" ) ;
					// workflow = workflowBuilder.createWorkflow( credentials, name, description ) ;
                }
        
            }
            catch( WorkflowInterfaceException wix ) {
                wix.printStackTrace();
            }
            finally {
                if( TRACE_ENABLED ) trace( "DesignActionImpl.createWorkflow() exit" ) ;
            }
            
        } // end of createWorkflow()
        
        
        private void saveWorkflow() throws ConsistencyException {
            if( TRACE_ENABLED ) trace( "DesignActionImpl.saveWorkflow() entry" ) ;
            
            String ivornName = "ivo://" + request.getParameter( SAVE_WORKFLOW_IVORN_PARAMETER ) ;
			debug( "ivornName: " + ivornName );
            Ivorn ivorn = null;
            AstrogridSession agSession = AstrogridSessionFactory.getSession(request.getSession(true));
                                       
            try {

                if( (workflow == null) || (ivornName == null) ) {
                    ; // some logging here
                    throw new ConsistencyException() ; 
                }
                else {
                    ivorn = (new CommunityIvornParser(ivornName)).getIvorn();
                    debug( "user: " + user );
                    debug( "ivorn: " + ivorn );
                    debug( "workflow: " + workflow );
                    WorkflowStore wfStore = this.workflowManager.getWorkflowStore();
                    debug( "wfStore: " + wfStore );
                    wfStore.saveWorkflow( user, ivorn, workflow ) ;
                    
                    // We need to force the tree to reload this directory...
                    // Find the full directory path in Tree terms...
                    String directoryPath = "home/" + request.getParameter( SAVE_WORKFLOW_IVORN_PARAMETER ) ;
                    int separatorIndex = directoryPath.lastIndexOf( '/' ) ;
                    directoryPath = directoryPath.substring( 0, separatorIndex + 1) ;
                    Tree tree = (Tree)agSession.getAttribute( AttributeKey.MYSPACE_TREE ) ;
                    tree.refresh( tree.getDirectory(directoryPath) ) ;
                } 
                               
            }
            catch( CommunityIdentifierException cix ) {
                cix.printStackTrace();
            }         
            catch( WorkflowInterfaceException wix ) {            	
				session.setAttribute( WORKFLOW_SUBMIT_MESSAGE, "An error has occured whilst trying to save your workflow (possibly because your workflow failed to validate - e.g. do all steps contain tools)" ) ;
                wix.printStackTrace();
               }
            finally {
                if( TRACE_ENABLED ) trace( "DesignActionImpl.saveWorkflow() exit" ) ;
            }
         
        } // end of saveWorkflow()
        
   
        private void removeWorkflow() throws ConsistencyException {
            if( TRACE_ENABLED ) trace( "DesignActionImpl.removeWorkflow() entry" ) ;
              
            try {
                session.removeAttribute( HTTP_WORKFLOW_TAG );
                workflow = null ;
            }
            finally {
               if( TRACE_ENABLED ) trace( "DesignActionImpl.removeWorkflow() exit" ) ;
            }
        } // end of removeWorkflow()  
       
         
              
        private void readWorkflow() throws ConsistencyException {
            if( TRACE_ENABLED ) trace( "DesignActionImpl.readWorkflow() entry" ) ;
              
            String name = request.getParameter( WORKFLOW_NAME_PARAMETER );
            String ivornName = "ivo://" + request.getParameter( OPEN_WORKFLOW_IVORN_PARAMETER ) ;
            debug("workflow name: " + name) ;
            debug("ivorn name: " + ivornName) ;
            Ivorn ivorn = null;
              
            try {
                                 
                if( (name == null) || (ivornName == null) ) {
                    ; // some logging here
                    throw new ConsistencyException() ; 
                }
                
                ivorn = (new CommunityIvornParser(ivornName)).getIvorn();
                
//                if( workflow == null  || bConfirm == true ) {
				if( true ) {
                    WorkflowStore wfStore = this.workflowManager.getWorkflowStore();
                    workflow = wfStore.readWorkflow( user, ivorn ) ;
 
                } 
                
                if( workflow != null ) {   
                    debug( "account: " + workflow.getCredentials().getAccount() ) ;
                    debug( "name: " + workflow.getName() ) ; 
                    debug( "description: " + workflow.getDescription() ) ;         
                }
                    
            }
            catch( CommunityIdentifierException cix ) {
                cix.printStackTrace();
            }
            catch( WorkflowInterfaceException wix ) {
                wix.printStackTrace(); //JBL note
            }
            finally {
                if( TRACE_ENABLED ) trace( "DesignActionImpl.readWorkflow() exit" ) ;
            }
         
        }

        private void submitWorkflow() throws ConsistencyException {
            if( TRACE_ENABLED ) trace( "DesignActionImpl.submitWorkflow() entry" ) ;
            
            String ivornName = request.getParameter( WORKFLOW_IVORN_PARAMETER ) ;
            String submitWorkflowMessage = "Workflow successfully submitted to JES;";
            Ivorn ivorn = null;
                
            try {

               if( workflow == null ) {
                  ; // some logging here
                  throw new ConsistencyException() ; 
               }
               else {
                  JobExecutionService jes = this.workflowManager.getJobExecutionService();
                  jes.submitWorkflow( workflow ) ;
               }
                          
            }

            catch( WorkflowInterfaceException wix ) {
                wix.printStackTrace(); // JBL note
				submitWorkflowMessage =  "Unable to submit workflow: " + wix.getMessage() ;
            }                        
            finally {
				this.session.setAttribute( WORKFLOW_SUBMIT_MESSAGE, submitWorkflowMessage ) ;
                if( TRACE_ENABLED ) trace( "DesignActionImpl.submitWorkflow() exit" ) ;
            }
         
        } // end of submitQuery()
        
        
        private void createTool() throws ConsistencyException {
           if( TRACE_ENABLED ) trace( "DesignActionImpl.createTool() entry" ) ;
           try {
            
              this.createTool( request.getParameter( TOOL_NAME_PARAMETER ) );
                    
           }
           finally {
              if( TRACE_ENABLED ) trace( "DesignActionImpl.createTool() exit" ) ;
           }                             
        } // end of createTool()
    

        private Tool createTool( String toolName ) throws ConsistencyException {
		   if( TRACE_ENABLED ) trace( "DesignActionImpl.createTool(toolName) entry" ) ;
           
           Tool tool = null;
           
		   try
		   {
               if( toolName == null ) {
			   throw new ConsistencyException() ;		   	
		   }
			
			trace("Toolname: " + toolName) ;  
			// createToolFromDefaultInterface() if no interface present
			if (toolName.indexOf("#") == -1)
			{
				tool = this.locateDescription(toolName).createToolFromDefaultInterface();
			}
			else
			{
				boolean intFound = false ;
				String interfaceName = toolName.substring(toolName.indexOf("#")+1).trim() ;
				String toolNewName = toolName.substring(0 , toolName.indexOf("#")).trim() ;
				
				ApplicationDescription appDesc = this.locateDescription( toolNewName ) ;
				InterfacesType intTypes = appDesc.getInterfaces() ;
				Interface intf = null ;
				for (int i=0 ; i < intTypes.get_interfaceCount() ; i++) 
				{
					if ( intTypes.get_interface(i).getName().equalsIgnoreCase(interfaceName)) 
					{
					    intf = intTypes.get_interface(i) ;
						intFound = true ;
					    break ;
					}
				}
				if (intFound)
				{
					tool = appDesc.createToolFromInterface( intf );
				}
				else
				{
					// an interface should always be found - in case not use default interface
					tool = appDesc.createToolFromDefaultInterface();
				}
			}              
				                           
			  this.request.setAttribute( HTTP_TOOL_TAG, tool ) ;
		   }

           catch( WorkflowInterfaceException wix ) {
               wix.printStackTrace();
           }
	       finally {
		      if( TRACE_ENABLED ) trace( "DesignActionImpl.createTool(toolName) exit" ) ;
		   }		
           
           return tool;
           			         
        } // end of createTool(toolName)
           
           
        private void readToolList() {
           if( TRACE_ENABLED ) trace( "DesignActionImpl.readToolList() entry" ) ;
           
		   LinkedList tools = new LinkedList();
		   if (session.getAttribute(TOOL_LIST_PARAMETER) != null) {
			   tools = (LinkedList) session.getAttribute(TOOL_LIST_PARAMETER) ;				
		   }
              
           try {
                         
              if( tools.size() <= 0 ) 
              {
                  ApplicationRegistry toolRegistry = workflowManager.getToolRegistry();
				  ApplicationDescriptionSummary[] appDescSum = toolRegistry.listUIApplications();
				  				
				  for (int i = 0 ; i < appDescSum.length ; i++)													
				  {
				      String UIName = appDescSum[i].getUIName() ;					
					  String Name = appDescSum[i].getName() ;
					  String[] intNames = appDescSum[i].getInterfaceNames() ;
				  }
				  for (int i=0;i < appDescSum.length ;i++)
				  {
				      tools.add(appDescSum[i]) ;							
					  if (i > 14)
					      break;
				  }
 
                  this.session.setAttribute( TOOL_LIST_PARAMETER, tools ) ;                     
              }
              this.session.setAttribute( TOOL_LIST_PARAMETER, tools ) ; 
       
           }
           catch( WorkflowInterfaceException wix ) {
                debug( "wix exception: " + wix.toString() );
                this.request.setAttribute( ERROR_MESSAGE_PARAMETER, wix.toString() ) ;
           }           
           finally {
              if( TRACE_ENABLED ) trace( "DesignActionImpl.readToolList() exit" ) ;
           }
           
        } // end of readQueryList()  
  
			private void updateUserToolList(String toolName) {
				if( TRACE_ENABLED ) trace( "DesignActionImpl.updateUserToolList() entry" ) ;
           
				LinkedList tools = new LinkedList();
				boolean toolPresent = false;
				
				try 
				{				    
				    if (session.getAttribute(TOOL_LIST_PARAMETER) != null) 
				    {
					    tools = (LinkedList) session.getAttribute(TOOL_LIST_PARAMETER) ;				
				    }
				    else 
				    {
						ApplicationRegistry toolRegistry = workflowManager.getToolRegistry();
						ApplicationDescriptionSummary[] appDescSum = toolRegistry.listUIApplications();
					    for (int i=0;i < appDescSum.length ;i++)
					    {
						    tools.add(appDescSum[i]) ;							
						    if (i > 14)
						        break;
					    }
				    }
                          
					for (int i=0; i < tools.size(); i++)
					{
						if (toolName.indexOf("#") != -1)
						{
							toolName = toolName.substring(0 , toolName.indexOf("#")).trim() ;					
						}
						if ( ((ApplicationDescriptionSummary)tools.get(i)).getName().equalsIgnoreCase(toolName) )
						{
							toolPresent = true;
							ApplicationDescriptionSummary a = (ApplicationDescriptionSummary)tools.get(i) ;
							tools.remove(i);
							tools.addFirst(a);
							break;      
						}
					}
					if (!toolPresent)
					{
						ApplicationRegistry toolRegistry = workflowManager.getToolRegistry();
						ApplicationDescriptionSummary[] appDescSum = toolRegistry.listUIApplications();

						for (int i=0 ; i < appDescSum.length ; i++ )
						{
							if(appDescSum[i].getName().equalsIgnoreCase(toolName))
							{
								tools.addFirst(appDescSum[i]) ;
								break ;
							}
						}
											
						if (tools.size() > 15)
							tools.removeLast() ;
					}			    					

					this.session.setAttribute( TOOL_LIST_PARAMETER, tools ) ;   												        									
				}
				catch( WorkflowInterfaceException wix ) {
						 debug( "wix exception: " + wix.toString() );
						 this.request.setAttribute( ERROR_MESSAGE_PARAMETER, wix.toString() ) ;
				}          
				finally {
					if( TRACE_ENABLED ) trace( "DesignActionImpl.updateUserToolList() exit" ) ;
				}
           
			} // end of updateUserToolList()          
        

		private void readLists() {
			if( TRACE_ENABLED ) trace( "DesignActionImpl.readLists() entry" ) ;
              
			try {
                    
					// For the moment this is where we have placed the door.
					// If users cannot see a list, then they cannot do anything...
//					this.checkPermissions( AUTHORIZATION_RESOURCE_WORKFLOW
//									     , AUTHORIZATION_ACTION_EDIT ) ;
//                     
//                    this.readWorkflowList();          
//                    this.readQueryList();
                    this.readToolList() ;

				}
				catch( Exception ex ) {              
					this.request.setAttribute( ERROR_MESSAGE_PARAMETER, "permission denied" ) ;
				}
				finally {
					if( TRACE_ENABLED ) trace( "DesignActionImpl.readLists() exit" ) ;
				}
                    
			} // end of readLists()

           
        private void removeToolFromStep() throws ConsistencyException {
           if( TRACE_ENABLED ) trace( "DesignActionImpl.removeToolFromStep() entry" ) ;
			
           Step step = null;
              
           try {		
                 step = locateStep( workflow, request.getParameter( ACTIVITY_KEY_PARAMETER ) );       
                 step.setTool( null );   	
           }
           finally {
              if( TRACE_ENABLED ) trace( "DesignActionImpl.removeToolFromStep() exit" ) ;
            }
                    
		} // end of removeToolFromStep() 

		private void insertMultipleValue() throws ConsistencyException {
			if( TRACE_ENABLED ) trace( "DesignActionImpl.insertMultipleValue() entry" ) ;
			try {		
				int input_param_count = new Integer(request.getParameter( INPUT_PARAMETER_COUNT_PARAM )).intValue();
				int output_param_count = new Integer(request.getParameter( OUTPUT_PARAMETER_COUNT_PARAM )).intValue();
				int total_param_count = input_param_count + output_param_count ;
                
				for (int i = 0 ; i < input_param_count; i++)
				{
					insertInputValue(i) ;				
				}
				for (int i = input_param_count ; i < total_param_count; i++)
				{
					insertOutputValue(i) ;				
				}				 
			}
			finally {
				if( TRACE_ENABLED ) trace( "DesignActionImpl.insertMultipleValue() exit" ) ;
			}
				}

		private void insertValue() throws ConsistencyException {
			if( TRACE_ENABLED ) trace( "DesignActionImpl.insertValue() entry" ) ;
			try {		
				String direction = request.getParameter( DIRECTION_PARAMETER ) ;
				
				if (direction.equalsIgnoreCase("input")) 
				{
					insertInputValue() ;
				}
				else if (direction.equalsIgnoreCase("output"))
				{
					insertOutputValue() ;
				}
				else
				{
					debug("Direction of parameter not available");
				}
			}
			finally {
				if( TRACE_ENABLED ) trace( "DesignActionImpl.insertValue() exit" ) ;
			}
        }  	  	


      private void insertInputValue() throws ConsistencyException {
         if( TRACE_ENABLED ) trace( "DesignActionImpl.insertInputValue() entry" ) ;
			
         Step step = null;
         Tool tool = null ;
         ParameterValue p = null ;
		 boolean parameterIndirect = false;
              
         try {
            // Tool should already have been inserted into step
			
            String oldParameterValue = request.getParameter( ORIG_PARAM_VALUE_PARAMETER );						
			String parameterName = request.getParameter( PARAM_NAME_PARAMETER ) ;				    					
			String parameterValue = request.getParameter( PARAM_VALUE_PARAMETER ) ;
			String activityKey = request.getParameter( ACTIVITY_KEY_PARAMETER ) ;
			String ivornValue = request.getParameter( IVORN_VALUE_PARAMETER ) ;
//		Bug #560: PARAM_INDIRECT is now a radio button			
			if (request.getParameter( PARAM_INDIRECT ).equalsIgnoreCase("on") )
			  parameterIndirect = true;
//			boolean parameterIndirect = new Boolean(request.getParameter( PARAM_INDIRECT ) ).booleanValue() ;
			debug( "ivornValue: " + ivornValue );
			debug( "parameterName:" + parameterName ) ;
			debug( "parameterValue: " + parameterValue ) ;
			debug( "oldParameterValue: " + oldParameterValue ) ;
			debug( "activityKey: " + activityKey ) ;
			debug( "parameterIndirect: " + parameterIndirect  ) ;   
                            
			if ( parameterName == null) {
				debug( "parameterName is null" ) ;
			}
			else if ( parameterValue == null) {
				debug( "parameterValue is null" ) ;
			}
			else if ( activityKey == null) {
				debug( "activityKey is null" ) ;
			}
			else if (ivornValue != null && ivornValue.length() > 0) {
				debug( "setting parameterValue to equal ivornValue" ) ;
				parameterValue = ivornValue ;          
				debug( "parameterValue now: " + parameterValue ) ;
			}

            step = locateStep( workflow, request.getParameter( ACTIVITY_KEY_PARAMETER ) );
            tool = step.getTool() ;
            ApplicationRegistry applRegistry = workflowManager.getToolRegistry();
            ApplicationDescription applDescription = applRegistry.getDescriptionFor( tool.getName() );
				
            WorkflowHelper.insertInputParameterValue( applDescription
                                                    , tool
                                                    , parameterName
                                                    , oldParameterValue
                                                    , parameterValue
                                                    , parameterIndirect ) ;
				
		  }
            catch( WorkflowInterfaceException wix ) {
                wix.printStackTrace();
            }
		  finally {
				if( TRACE_ENABLED ) trace( "DesignActionImpl.insertInputValue() exit" ) ;
		  }
                    
	  } // end of insertInputValue()       

	  private void insertInputValue(int i) throws ConsistencyException {
	      if( TRACE_ENABLED ) trace( "DesignActionImpl.insertInputValue() entry" ) ;
			
		  Step step = null;
		  Tool tool = null ;
		  ParameterValue p = null ;
	      boolean parameterIndirect = false;
	      String paramCount = "" + i ;
              
	      try {
			   // Tool should already have been inserted into step
			
			   String oldParameterValue = request.getParameter( ORIG_PARAM_VALUE_PARAMETER+"#input#"+paramCount );						
		       String parameterName = request.getParameter( PARAM_NAME_PARAMETER+"#input#"+paramCount ) ;				    					
		       String parameterValue = request.getParameter( PARAM_VALUE_PARAMETER+"#input#"+paramCount ) ;
		       String activityKey = request.getParameter( ACTIVITY_KEY_PARAMETER+"#input#"+paramCount ) ;
		       String ivornValue = request.getParameter( IVORN_VALUE_PARAMETER+"#input#"+paramCount ) ;
			
		       if (request.getParameter( PARAM_INDIRECT+"#input#"+paramCount ).equalsIgnoreCase("on") )
			     parameterIndirect = true;
		       
		       // Bug # 1047 - if param is indirect we need to manually add the ivo://
		       if (parameterIndirect) {
		       	  ivornValue = "ivo://" + ivornValue ;
		       }

		debug( "multi: ivornValue: " + i + ": " + ivornValue );
		debug( "multi: parameterName:" + i + ": " + parameterName ) ;
		debug( "multi: parameterValue: " + i + ": " + parameterValue ) ;
		debug( "multi: oldParameterValue: " + i + ": " + oldParameterValue ) ;
		debug( "multi: activityKey: " + i + ": " + activityKey ) ;
		debug( "multi: parameterIndirect: " + i + ": " + parameterIndirect  ) ;   
                            
		if ( parameterName == null) {
			debug( "parameterName is null" ) ;
		}
		else if ( parameterValue == null) {
			debug( "parameterValue is null" ) ;
		}
		else if ( activityKey == null) {
			debug( "activityKey is null" ) ;
		}
		else if (ivornValue != null && ivornValue.length() > 0) {
			debug( "setting parameterValue to equal ivornValue" ) ;
			parameterValue = ivornValue ;          
			debug( "parameterValue now: " + parameterValue ) ;
		}
		
		if ( (parameterValue != null && parameterValue.length() > 0 ) || (oldParameterValue.length() > 0) )
		{			
		    step = locateStep( workflow, activityKey );
		    tool = step.getTool() ;
		    ApplicationRegistry applRegistry = workflowManager.getToolRegistry();
		    ApplicationDescription applDescription = applRegistry.getDescriptionFor( tool.getName() );

            if (parameterValue.indexOf(PARAMATER_SEPARATOR_PARAM) == -1 ) // single parameter value
                {
		            WorkflowHelper.insertInputParameterValue( applDescription
					        							    , tool
							        					    , parameterName
									        			    , oldParameterValue
											        	    , parameterValue
												            , parameterIndirect ) ;
                }
            else // multiple parameter values
            {
            	while (parameterValue.indexOf(PARAMATER_SEPARATOR_PARAM) != -1)
            	{
                    String singleParamValue = parameterValue.substring( 0, parameterValue.indexOf(PARAMATER_SEPARATOR_PARAM ) );
                    parameterValue = parameterValue.substring(singleParamValue.length() + 2 , parameterValue.length() ) ;

					WorkflowHelper.insertInputParameterValue( applDescription
															, tool
															, parameterName
															, ""
															, singleParamValue.trim()
															, parameterIndirect ) ;
        		
            	}
            	// and the final value
				WorkflowHelper.insertInputParameterValue( applDescription
														, tool
														, parameterName
														, ""
														, parameterValue.trim()
														, parameterIndirect ) ;            	
            }
		}				
		}
		catch( WorkflowInterfaceException wix ) {
			wix.printStackTrace();
		}
		finally {
			if( TRACE_ENABLED ) trace( "DesignActionImpl.insertInputValue() exit" ) ;
		}
                    
	} // end of insertInputValue()       



        private void insertOutputValue() throws ConsistencyException {
           if( TRACE_ENABLED ) trace( "DesignActionImpl.insertOutputValue() entry" ) ;
			
		   Step step = null;
		   Tool tool = null ;
		   ParameterValue p = null ;
		   boolean parameterIndirect = false ;
              
	       try {
				 // Tool should already have been inserted into step
									
			     String
                    oldParameterValue = request.getParameter( ORIG_PARAM_VALUE_PARAMETER ),
					parameterName = request.getParameter( PARAM_NAME_PARAMETER ),			  
					parameterValue = request.getParameter( PARAM_VALUE_PARAMETER ) ;
					String ivornValue = request.getParameter( IVORN_VALUE_PARAMETER ) ;
// Bug #560: PARAM_INDIRECT is now a radio button
					if (request.getParameter( PARAM_INDIRECT ).equalsIgnoreCase("on") )
						parameterIndirect = true;					
//					boolean parameterIndirect = new Boolean( request.getParameter( PARAM_INDIRECT ) ).booleanValue() ;
					debug( "ivornValue: " + ivornValue );					
                            
			     if ( parameterName == null) {
					debug( "parameterName is null" ) ;
			     }
			     else if ( parameterValue == null) {
					debug( "parameterValue is null" ) ;
			     }
			    
				else if (ivornValue != null && ivornValue.length() > 0) {
					debug( "setting parameterValue to equal ivornValue" ) ;
					parameterValue = ivornValue ;          
					debug( "parameterValue now: " + parameterValue ) ;
				}			     
				
               step = locateStep( workflow, request.getParameter( ACTIVITY_KEY_PARAMETER ) );
               tool = step.getTool() ;
               ApplicationRegistry applRegistry = workflowManager.getToolRegistry();
               ApplicationDescription applDescription = applRegistry.getDescriptionFor( tool.getName() );
                
               WorkflowHelper.insertOutputParameterValue( applDescription
                                                        , tool
                                                        , parameterName
                                                        , oldParameterValue
                                                        , parameterValue
					                                    , parameterIndirect ) ;
				
		   }
           catch( WorkflowInterfaceException wix ) {
               wix.printStackTrace();
           }
		   finally {
			  if( TRACE_ENABLED ) trace( "DesignActionImpl.insertOutputValue() exit" ) ;
		   }
                    
	} // end of insertOutputValue()

		private void insertOutputValue(int i) throws ConsistencyException {
		    if( TRACE_ENABLED ) trace( "DesignActionImpl.insertOutputValue() entry" ) ;
	
		    Step step = null;
		    Tool tool = null ;
		    ParameterValue p = null ;
		    boolean parameterIndirect = false ;
			String paramCount = "" + i ;
			try {
			// Tool should already have been inserted into step
									
			String oldParameterValue = request.getParameter( ORIG_PARAM_VALUE_PARAMETER+"#output#"+paramCount ) ;
			String parameterName = request.getParameter( PARAM_NAME_PARAMETER+"#output#"+paramCount ) ;
			String parameterValue = request.getParameter( PARAM_VALUE_PARAMETER+"#output#"+paramCount ) ;
			String activityKey = request.getParameter( ACTIVITY_KEY_PARAMETER+"#output#"+paramCount ) ;
			String ivornValue = request.getParameter( IVORN_VALUE_PARAMETER+"#output#"+paramCount ) ;
			if (request.getParameter( PARAM_INDIRECT+"#output#"+paramCount ).equalsIgnoreCase("on") )
				parameterIndirect = true;
			
		       // Bug # 1047 - if param is indirect we need to manually add the ivo://
		       if (parameterIndirect) {
		       	  ivornValue = "ivo://" + ivornValue ;
		       }			

				debug( "multi: ivornValue: " + i + ": " + ivornValue );
				debug( "multi: parameterName:" + i + ": " + parameterName ) ;
				debug( "multi: parameterValue: " + i + ": " + parameterValue ) ;
				debug( "multi: oldParameterValue: " + i + ": " + oldParameterValue ) ;
				debug( "multi: activityKey: " + i + ": " + activityKey ) ;
				debug( "multi: parameterIndirect: " + i + ": " + parameterIndirect  ) ;									

			if ( parameterName == null) {
				debug( "parameterName is null" ) ;
			}
			else if ( parameterValue == null) {
				debug( "parameterValue is null" ) ;
			}
			    
			else if (ivornValue != null && ivornValue.length() > 0) {
				debug( "setting parameterValue to equal ivornValue" ) ;
				parameterValue = ivornValue ;          
				debug( "parameterValue now: " + parameterValue ) ;
			}			     

			step = locateStep( workflow, activityKey );
			tool = step.getTool() ;
			ApplicationRegistry applRegistry = workflowManager.getToolRegistry();
			ApplicationDescription applDescription = applRegistry.getDescriptionFor( tool.getName() );
                
			WorkflowHelper.insertOutputParameterValue( applDescription
													, tool
													, parameterName
													, oldParameterValue
													, parameterValue
													, parameterIndirect ) ;
				
		    }
			catch( WorkflowInterfaceException wix ) {
			   wix.printStackTrace();
		    }
		    finally {
			  if( TRACE_ENABLED ) trace( "DesignActionImpl.insertOutputValue() exit" ) ;
		   }
                    
    } // end of insertOutputValue()


    private void resetParameter() throws ConsistencyException {
       if( TRACE_ENABLED ) trace( "DesignActionImpl.resetParameter() entry" ) ;
			
       Step step = null;
       Tool tool = null ;
       ParameterValue p = null ;
              
        try {		
									
           String
              parameterName = request.getParameter( PARAM_NAME_PARAMETER ),
              parameterValue = request.getParameter( PARAM_VALUE_PARAMETER ),
              direction = request.getParameter( DIRECTION_PARAMETER );
           boolean bInput = direction.equalsIgnoreCase( "input" );
                            
           if ( parameterName == null) {
              debug( "parameterName is null" ) ;
           }
          else if ( parameterValue == null) {
              debug( "parameterValue is null" ) ;
          }      
          else if ( direction == null) {
             debug( "direction is null" ) ;
          }

          step = locateStep( workflow, request.getParameter( ACTIVITY_KEY_PARAMETER ) );
          tool = step.getTool() ;
				
          ApplicationRegistry applRegistry = workflowManager.getToolRegistry();
          ApplicationDescription applDescription = applRegistry.getDescriptionFor( tool.getName() );
    
          if( bInput ) {                           
              WorkflowHelper.insertInputParameterValue( applDescription, tool, parameterName, parameterValue, "", false ) ;   
          } else {
              WorkflowHelper.insertOutputParameterValue( applDescription, tool, parameterName, parameterValue, "", false ) ;   
          }
                                           
				
       }
       catch( WorkflowInterfaceException wix ) {
           wix.printStackTrace();
       }
	   finally {
          if( TRACE_ENABLED ) trace( "DesignActionImpl.resetParameter() exit" ) ;
	   }
                    
    } // end of deleteParameter() 
	
	
	private void pasteActivity() throws ConsistencyException {
		if( TRACE_ENABLED ) trace( "DesignActionImpl.pasteActivity() entry" ) ;
        
		AbstractActivity abstractActivity = null ;      
		try {
			String xpathKey = (String)session.getAttribute( COPY_ACTIVITY_KEY ) ;			   
			if ( xpathKey == null) {
				debug( "xpathKey is null" ) ;
				throw new ConsistencyException();
			}
			else
			{				
                 Step copyStep = new Step() ;
				 Step origionalStep = new Step() ;
				 origionalStep = locateStep( workflow, xpathKey ) ;
                 copyStep.setName( origionalStep.getName()+"_COPY" ) ;
                 copyStep.setDescription( origionalStep.getDescription() ) ;
                 copyStep.setTool( origionalStep.getTool() ) ;                 
                 this.insertActivity( copyStep ) ; 
			}          
		}
		finally {
			if( TRACE_ENABLED ) trace( "DesignActionImpl.pasteActivity() exit" ) ;
		}                    
	} // end of pasteActivity()	


        private void insertStep() throws ConsistencyException {
           if( TRACE_ENABLED ) trace( "DesignActionImpl.insertStep() entry" ) ;
              
           try {
           	  Step step = new Step() ;
           	  step.setDescription("...") ;
              this.insertActivity( step ) ;
            }
            finally {
                  if( TRACE_ENABLED ) trace( "DesignActionImpl.insertStep() exit" ) ;
            }
                    
        } // end of insertStep()       
        
        
        private void insertFlow() throws ConsistencyException {
           if( TRACE_ENABLED ) trace( "DesignActionImpl.insertFlow() entry" ) ;
              
           try {
              this.insertActivity( new Flow() ) ;          
            }
            finally {
                  if( TRACE_ENABLED ) trace( "DesignActionImpl.insertFlow() exit" ) ;
            }
                    
        } // end of insertFlow()

		private void insertElse() throws ConsistencyException {
			if( TRACE_ENABLED ) trace( "DesignActionImpl.insertFor() entry" ) ;
              
			try {
				If ifActivity = new If() ;
				Else elseActivity = new Else() ;			
				elseActivity.setActivity(new Sequence() );					
				
				String xpathKey = request.getParameter( ACTIVITY_KEY_PARAMETER ) ;    
				if ( xpathKey == null) {
				    debug( "xpathKey is null" ) ;
				    throw new ConsistencyException();
				}
				else
				{				
				    ifActivity = locateIf( workflow, xpathKey ) ;				  				    
				    ifActivity.setElse( elseActivity ) ;
				}          
			}
			finally {
				if( TRACE_ENABLED ) trace( "DesignActionImpl.insertElse() exit" ) ;
			}                    
		} // end of insertElse()        

		private void insertFor() throws ConsistencyException {
			if( TRACE_ENABLED ) trace( "DesignActionImpl.insertFor() entry" ) ;
              
		    try {
		    	For forActivity = new For();
		    	forActivity.setActivity(new Sequence() );		    	
			    this.insertActivity( forActivity ) ;          
		    }
		    finally {
		        if( TRACE_ENABLED ) trace( "DesignActionImpl.insertFor() exit" ) ;
		    }                    
		} // end of insertFor()
		
		
		private void insertParfor() throws ConsistencyException {
			if( TRACE_ENABLED ) trace( "DesignActionImpl.insertParfor() entry" ) ;
              
			try {
				Parfor parForActivity = new Parfor() ;
				parForActivity.setActivity( new Sequence() ) ;
				this.insertActivity( parForActivity ) ; 
			}
			finally {
				if( TRACE_ENABLED ) trace( "DesignActionImpl.insertParfor() exit" ) ;
			}                    
		} // end of insertParfor()
		
		
		private void insertParforDetails() throws ConsistencyException {
			if( TRACE_ENABLED ) trace( "DesignActionImpl.insertParforDetails() entry" ) ;
              
			Parfor parForObject = null ;
            
			try {
				String defaultVar = "(the name of the loop variable...)" ;
				String defaultItems = "(A sequence or iterator of items - the loop variable will be assigned to each in turn, and then loop body executed...)" ;					
			    String xpathKey = request.getParameter( ACTIVITY_KEY_PARAMETER ) ;
			    String items = request.getParameter( FOR_ITEMS_PARAMETER ) ;
				String var = request.getParameter( FOR_VAR_PARAMETER ) ;
                                
				if ( xpathKey == null) {
					debug( "xpathKey is null" ) ;
				}

				parForObject = locateParfor( workflow, xpathKey ); 
				if (!(items.equalsIgnoreCase(defaultItems))) {				 
				    parForObject.setItems(items);
				}
				if (!(var.equalsIgnoreCase(defaultVar))) {				
				    parForObject.setVar(var);
				}
			}
			finally {
				if( TRACE_ENABLED ) trace( "DesignActionImpl.insertParforDetails() exit" ) ;
			}                    
		} // end of insertParforDetails()						
		
		
		private void insertForDetails() throws ConsistencyException {
			if( TRACE_ENABLED ) trace( "DesignActionImpl.insertForDetails() entry" ) ;
              
			 For forObject = null ;
            
             try {
             	String defaultVar = "(the name of the loop variable...)" ;
             	String defaultItems = "(A sequence or iterator of items - the loop variable will be assigned to each in turn, and then loop body executed...)" ;	
		        String xpathKey = request.getParameter( ACTIVITY_KEY_PARAMETER ) ;
	            String items = request.getParameter( FOR_ITEMS_PARAMETER ).trim() ;
				String var = request.getParameter( FOR_VAR_PARAMETER ).trim() ;	
				// Bug #799 - temporary fix ...' is used to prevent text area collapsing in xsl
				if (var.equalsIgnoreCase("..."))
						var = "" ;
                                
	            if ( xpathKey == null) {
		           debug( "xpathKey is null" ) ;
	            }

				forObject = locateFor( workflow, xpathKey );
				if (!(items.equalsIgnoreCase(defaultItems))) {
					forObject.setItems(items);				  
				}				    

				if (!(var.equalsIgnoreCase(defaultVar))) {
				    forObject.setVar(var);
				}
				
            }
			finally {
				if( TRACE_ENABLED ) trace( "DesignActionImpl.insertForDetails() exit" ) ;
			}                    
		} // end of insertForDetails()	
		
		
		private void insertThen() throws ConsistencyException {
			if( TRACE_ENABLED ) trace( "DesignActionImpl.insertThen() entry" ) ;
              
			try {
				If ifActivity = new If() ;
				Then thenActivity = new Then() ;			
				thenActivity.setActivity( new Sequence() );					
				
				String xpathKey = request.getParameter( ACTIVITY_KEY_PARAMETER ) ;    
				if ( xpathKey == null) {
						debug( "xpathKey is null" ) ;
				}
				else
				{				
						ifActivity = locateIf( workflow, xpathKey ) ;				  				    
						ifActivity.setThen( thenActivity ) ;
				}          
			}
			finally {
				if( TRACE_ENABLED ) trace( "DesignActionImpl.insertThen() exit" ) ;
			}                    
		} // end of insertThen()
		
		
		private void insertWhile() throws ConsistencyException {
			if( TRACE_ENABLED ) trace( "DesignActionImpl.insertWhile() entry" ) ;
              
				try {
					While whileActivity = new While();
					whileActivity.setActivity(new Sequence() );
					this.insertActivity( whileActivity ) ;
				}
				finally {
						if( TRACE_ENABLED ) trace( "DesignActionImpl.insertWhile() exit" ) ;
				}                    
		} // end of insertWhile()

		
		private void insertWhileDetails() throws ConsistencyException {
			if( TRACE_ENABLED ) trace( "DesignActionImpl.insertWhileDetails() entry" ) ;
              
			 While whileObject = null ;
            
			try {	
			    String xpathKey = request.getParameter( ACTIVITY_KEY_PARAMETER ) ;
				String test = request.getParameter( WHILE_TEST_PARAMETER ) ;				
                                
				if ( xpathKey == null) {
			        debug( "xpathKey is null" ) ;
				}

				whileObject = locateWhile( workflow, xpathKey );  
				whileObject.setTest(test);				
				
			}
			finally {
				if( TRACE_ENABLED ) trace( "DesignActionImpl.insertWhileDetails() exit" ) ;
			}                    
		} // end of insertWhileDetails()		
		
				
		private void insertIf() throws ConsistencyException {
			if( TRACE_ENABLED ) trace( "DesignActionImpl.insertIf() entry" ) ;
              
			try {
				If ifActivity = new If() ;
				Then thenActivity = new Then() ;
				thenActivity.setActivity(new Sequence() ) ;
				ifActivity.setThen(thenActivity) ;
				this.insertActivity( ifActivity ) ;          
			}
			finally {
			    if( TRACE_ENABLED ) trace( "DesignActionImpl.insertIf() exit" ) ;
			}                    
		} // end of insertIf()	
		
		
		private void insertIfDetails() throws ConsistencyException {
			if( TRACE_ENABLED ) trace( "DesignActionImpl.insertIfDetails() entry" ) ;
              
			If ifObject = null ;						
            
			try {	
			    String xpathKey = request.getParameter( ACTIVITY_KEY_PARAMETER ) ;
				String ifTest = request.getParameter( IF_TEST_PARAMETER ) ;
                                
				if ( xpathKey == null) {
				    debug( "xpathKey is null" ) ;
				}

				ifObject = locateIf( workflow, xpathKey );  
				ifObject.setTest(ifTest);				
			}
			finally {
				if( TRACE_ENABLED ) trace( "DesignActionImpl.insertIfDetails() exit" ) ;
			}                    
		} // end of insertIfDetails()


		private void insertScope() throws ConsistencyException {
			if( TRACE_ENABLED ) trace( "DesignActionImpl.insertScope() entry" ) ;
              
			try {
				Scope scopeActivity = new Scope();
				scopeActivity.setActivity(new Sequence() );
				this.insertActivity( scopeActivity ) ;         
			}
			finally {
				if( TRACE_ENABLED ) trace( "DesignActionImpl.insertScope() exit" ) ;
			}                    
		} // end of insertScope()


		private void insertSet() throws ConsistencyException {
			if( TRACE_ENABLED ) trace( "DesignActionImpl.insertSet() entry" ) ;
              
			try {
					this.insertActivity( new Set() ) ;          
			}
			finally {
				if( TRACE_ENABLED ) trace( "DesignActionImpl.insertSet() exit" ) ;
			}                    
		} // end of insertSet()


		private void insertSetDetails() throws ConsistencyException {
			if( TRACE_ENABLED ) trace( "DesignActionImpl.insertSetDetails() entry" ) ;
              
			Set setObject = null ;
            
			try {	
				String xpathKey = request.getParameter( ACTIVITY_KEY_PARAMETER ) ;
				String val = request.getParameter( SET_VALUE_PARAMETER ) ;
				String var = request.getParameter( SET_VAR_PARAMETER ).trim() ;
				// Bug #799 - temporary fix ...' is used to prevent text area collapsing in xsl
				if (var.equalsIgnoreCase("..."))
				    var = "" ;
                                
				if ( xpathKey == null) {
					debug( "xpathKey is null" ) ;
				}

				setObject = locateSet( workflow, xpathKey );  
				setObject.setValue(val);
				setObject.setVar(var);
				
						}
			finally {
				if( TRACE_ENABLED ) trace( "DesignActionImpl.insertSetDetails() exit" ) ;
			}                    
		} // end of insertSetDetails()
		
		
		private void insertUnset() throws ConsistencyException {
			if( TRACE_ENABLED ) trace( "DesignActionImpl.insertUnset() entry" ) ;
              
			try {
					this.insertActivity( new Unset() ) ;          
			}
			finally {
				if( TRACE_ENABLED ) trace( "DesignActionImpl.insertUnset() exit" ) ;
			}                    
		} // end of insertUnset()
		
		
		private void insertUnsetDetails() throws ConsistencyException {
			if( TRACE_ENABLED ) trace( "DesignActionImpl.insertUnsetDetails() entry" ) ;
              
			Unset unsetObject = null ;
            
			try {	
				String xpathKey = request.getParameter( ACTIVITY_KEY_PARAMETER ) ;
				String var = request.getParameter( UNSET_VAR_PARAMETER ) ;
                                
				if ( xpathKey == null) {
					debug( "xpathKey is null" ) ;
				}

				unsetObject = locateUnset( workflow, xpathKey );  
				unsetObject.setVar(var);				
			}
			finally {
				if( TRACE_ENABLED ) trace( "DesignActionImpl.insertUnsetDetails() exit" ) ;
			}                    
		} // end of insertUnsetDetails()

        
		private void insertScript() throws ConsistencyException {
		    if( TRACE_ENABLED ) trace( "DesignActionImpl.insertScript() entry" ) ;
              
			try {
			    this.insertActivity( new Script() ) ;          
			}
			finally {
			    if( TRACE_ENABLED ) trace( "DesignActionImpl.insertScript() exit" ) ;
			}                    
		} // end of insertScript()
		
		
		private void insertScriptDetails() throws ConsistencyException {
		    if( TRACE_ENABLED ) trace( "DesignActionImpl.insertScriptBody() entry" ) ;
              
			Script script = null ;              
              
			try {
				
				String xpathKey = request.getParameter( ACTIVITY_KEY_PARAMETER ) ;
				String body = request.getParameter( SCRIPT_BODY_PARAMETER ) ;
				String desc = request.getParameter( SCRIPT_DESCRIPTION_PARAMETER ) ;
                                
				if ( xpathKey == null) {
					debug( "xpathKey is null" ) ;
				}

				script = locateScript( workflow, xpathKey );  
				script.setBody( body );
				script.setDescription( desc );        
			}
			finally {
				if( TRACE_ENABLED ) trace( "DesignActionImpl.insertScriptBody() exit" ) ;
			}                    
		} // end of insertScriptBody()
			
			
        private void insertSequence() throws ConsistencyException {
           if( TRACE_ENABLED ) trace( "DesignActionImpl.insertSequence() entry" ) ;
              
           try {
              this.insertActivity( new Sequence() ) ;          
            }
            finally {
                  if( TRACE_ENABLED ) trace( "DesignActionImpl.insertSequence() exit" ) ;
            }
                    
        } // end of insertSequence()       


		private void addWorkflowNameAndDescription() throws ConsistencyException 
		{
			if( TRACE_ENABLED ) trace( "DesignActionImpl.addWorkflowNameAndDescription() entry" ) ;
              
			try {
				
				if( workflow == null ) {
						throw new ConsistencyException() ; 
				}
								
				String
					name = request.getParameter( WORKFLOW_NAME_PARAMETER ),
					description = request.getParameter( WORKFLOW_DESCRIPTION_PARAMETER ) ;                    
                    
				if( name == null ) {
					name = "new workflow";
				}
                
				if( description == null ) {
					description = "no description entered" ;
				}
				workflow.setName(name) ;
				workflow.setDescription(description) ;		          
			}
			finally {
				if( TRACE_ENABLED ) trace( "DesignActionImpl.addWorkflowNameAndDescription() exit" ) ;
			}                    
		} // end of addWorkflowNameAndDescription()         


        private void insertActivity( AbstractActivity activity ) throws ConsistencyException {
           if( TRACE_ENABLED ) trace( "DesignActionImpl.insertActivity() entry" ) ;
            
           Step step = null;
           ActivityContainer activityContainer = null;
           int index = 0;
           int highWaterMark = 0;
              
           try {

              String activityTargetKey = request.getParameter( ACTIVITY_KEY_PARAMETER ) ;
              debug("activityTargetKey: " + activityTargetKey ) ;
			  String parentKey = request.getParameter( PARENT_ACTIVITY_KEY_PARAMETER ) ; 
			  debug("parentKey: " + parentKey ) ;                                                   
              String activityIndex = request.getParameter( ACTIVITY_INDEX_PARAMETER ) ;
              debug("activityIndex: " + activityIndex ) ;
			  String activityOrder = request.getParameter( ACTIVITY_ORDER_PARAMETER ) ;
			  debug("activityOrder: " + activityOrder ) ; 
			  String activityType = request.getParameter( ACTIVITY_TYPE_PARAMETER ) ;
			  debug("activityType: " + activityType ) ;			               
                            
              if ( activityTargetKey == null) {
                  debug( "activityTargetKey is null" ) ;
              }
              else if ( activityIndex == null) {
                  debug( "activityIndex is null" ) ;
              }
                            
              try { 
                  index = new Integer( activityIndex ).intValue() ;
              }
              catch ( NumberFormatException nfx ) {
                  index = -1;
              }
              
			  if (activityOrder.equalsIgnoreCase("HERE"))
			  {
			      activityContainer = locateActivityContainer( workflow, activityTargetKey );
			      index = 0;
			  }
			  else 
			  {
					activityContainer = locateActivityContainer( workflow, parentKey );
			  }
			  	              
			  
			  highWaterMark = activityContainer.getActivityCount() - 1;
			  
			  if( highWaterMark < 0 ) {
			      highWaterMark = 0;
			  }			  
                                
			  if( index < 0 || index > highWaterMark ){
			      index = highWaterMark;
			  }
			  
			  if (activityOrder.equalsIgnoreCase("AFTER"))
			  {                            
			      index ++;
			  }
         	                                                            
              activityContainer.addActivity( index, activity ) ;
			  request.setAttribute( INSERTED_ACTIVITY_KEY_PARAMETER, workflow.getXPathFor( activity ) ) ;
			  debug("request inserted_activity_key: " + request.getAttribute(INSERTED_ACTIVITY_KEY_PARAMETER));              
                        
            }
            finally {
                  if( TRACE_ENABLED ) trace( "DesignActionImpl.insertActivity() exit" ) ;
            }
                    
        } // end of insertActivity()                     


        private void insertActivity() throws ConsistencyException {
		    if( TRACE_ENABLED ) trace( "DesignActionImpl.insertActivity() entry" ) ;           
              
			try {
			    String insertActivityType = request.getParameter( INSERT_ACTIVITY_TYPE_PARAMETER ) ;
				debug("insertActivityType: " + insertActivityType ) ;
				if (insertActivityType == null){
					debug( "insertActivityType is null" ) ;
				}
				else if (insertActivityType.equals("ELSE")){this.insertElse();}
				else if (insertActivityType.equals("FLOW")){this.insertFlow();}
				else if (insertActivityType.equals("FORLOOP")){this.insertFor();}
				else if (insertActivityType.equals("PARLOOP")){this.insertParfor();}
				else if (insertActivityType.equals("IF")){this.insertIf();}
				else if (insertActivityType.equals("SCOPE")){this.insertScope();}											
				else if (insertActivityType.equals("SCRIPT")){this.insertScript();}
				else if (insertActivityType.equals("SEQUENCE")){this.insertSequence();}
				else if (insertActivityType.equals("SET")){this.insertSet();}
				else if (insertActivityType.equals("STEP")){this.insertStep();}
				else if (insertActivityType.equals("THEN")){this.insertThen();}
				else if (insertActivityType.equals("UNSET")){this.insertUnset();}
				else if (insertActivityType.equals("WHILELOOP")){this.insertWhile();}
				else if (insertActivityType.equals("PASTE")){this.pasteActivity();}
				else if (insertActivityType.equals("COPY"))
				    {
				    	session.setAttribute( COPY_ACTIVITY_KEY, request.getParameter( ACTIVITY_KEY_PARAMETER ) );
				    }								
				else {
					debug("Unknown activity: " + insertActivityType);
				}					
			}                                   			
					
			finally {
			    if( TRACE_ENABLED ) trace( "DesignActionImpl.insertActivity() exit" ) ;
			}                    
		} // end of insertActivity() 


        private void removeActivity() throws ConsistencyException {
           if( TRACE_ENABLED ) trace( "DesignActionImpl.removeActivity() entry" ) ;
            
           ActivityContainer activityContainer = null;
           AbstractActivity activity = null;
		   int index = 0;
              
           try {

              String activityTargetKey = request.getParameter( ACTIVITY_KEY_PARAMETER ) ;
              debug("activityTargetKey: " + activityTargetKey ) ;
			  String parentKey = request.getParameter( PARENT_ACTIVITY_KEY_PARAMETER ) ; 
			  debug("parentKey: " + parentKey ) ;
			  String activityIndex = request.getParameter( ACTIVITY_INDEX_PARAMETER ) ; 
		      debug("activityIndex: " + activityIndex ) ;
			  index = new Integer( activityIndex ).intValue() ;                                   
                            
              if ( activityTargetKey == null) {
                  debug( "activityTargetKey is null" ) ;
              }
			  else if ( parentKey == null) {
			      debug( "parentKey is null" ) ;
			  }
  			  else if( activityTargetKey.equals( "/sequence" ) && index == 0) {						
			      ; // ignore the top sequence
			  }
			                
              else {
					activity = (AbstractActivity)workflow.findXPathValue( activityTargetKey );
					activityContainer = locateActivityContainer( workflow, parentKey );
				if( activityContainer != null ) {
						activityContainer.removeActivity( activity ) ;
				}					
              	
              }

                                    
           }
           catch (Exception e) {
					e.printStackTrace() ;
					}
					
           finally {
              if( TRACE_ENABLED ) trace( "DesignActionImpl.removeActivity() exit" ) ;
           }
                    
        } // end of removeActivity()       

	    private void insertStepDetails() throws ConsistencyException {
		    if( TRACE_ENABLED ) trace( "DesignActionImpl.insertStepDetails() entry" ) ;
			
			Step step = null ;
			Tool tool = null;
            
			try {
		        String xpathKey = request.getParameter( ACTIVITY_KEY_PARAMETER ) ;
				String toolName = request.getParameter( TOOL_NAME_PARAMETER ) ;
				String stepName = request.getParameter( STEP_NAME_PARAMETER ) ;
				String stepVar = request.getParameter( STEP_VAR_PARAMETER ) ;				
				String stepDescription = request.getParameter( STEP_DESCRIPTION_PARAMETER ) ;
                                
				if ( xpathKey == null) {
					debug( "xpathKey is null" ) ;
				}

				step = locateStep( workflow, xpathKey ); 
				step.setName( stepName );				
				if (!(stepVar.length() <= 0 || stepVar == null)) // the resultVar must contain a valid string, it cannot be empty so do not set if no value present.
				  step.setResultVar( stepVar ) ;
				step.setDescription( stepDescription );
				
				tool = this.createTool( toolName ) ;
				step.setTool( tool );
				updateUserToolList( toolName );				
				
			}
			finally {
				if( TRACE_ENABLED ) trace( "DesignActionImpl.insertStepDescription() exit" ) ;
			}                   
		} // end of insertStepDetails()

        
        private void checkPermissions ( String someResource, String anAction ) 
                                 throws CommunityServiceException, 
                                        CommunityPolicyException, 
                                        CommunityIdentifierException {
            if( TRACE_ENABLED ) trace( "DesignActionImpl.checkPermission() entry" ) ;
                       
            PolicyServiceDelegate ps = null;
            PolicyCredentials pCredentials = null;
            PolicyPermission pPermission = null;
            this.credentials = new Credentials();

            try {               
                ps = getPolicyDelegate () ;
                pCredentials = new PolicyCredentials();
                pCredentials.setAccount( this.credentials.getAccount().getName() );
                pCredentials.setGroup( this.credentials.getGroup().getName() );
                pPermission = ps.checkPermissions( pCredentials, someResource, anAction ) ;             
               
            }
            catch( MalformedURLException muex ) {
                muex.printStackTrace();
            }
            finally {
                if( TRACE_ENABLED ) trace( "DesignActionImpl.checkPermission() exit" ) ;  
            }
                        
        } // end of checkPermission()
        
               
        /**
         * Get the policy delegate.  
         * Looks for the property org.astrogrid.portal.community.url
         * if this property is not found (or is set to "dummy")
         * then a mock delegate is returned.
         *   
         * @return either a genuine or mock delegate
         * @throws MalformedURLException if the url is malformed
         */
        private PolicyServiceDelegate getPolicyDelegate() throws MalformedURLException {
            final Config config = SimpleConfig.getSingleton();
            final String endpoint = config.getString(ORG_ASTROGRID_PORTAL_COMMUNITY_URL, "dummy");
            if ("dummy".equals(endpoint)) {
                debug("Using dummy delegate");
                return new PolicyServiceMockDelegate();
            } else {
                debug("Using delegate at "+endpoint);
                return new PolicyServiceSoapDelegate(endpoint);
            }
        }

		private Set locateSet( Workflow workflow, String xpathKey ) throws ConsistencyException {
        
			Object obj = workflow.findXPathValue( xpathKey );
                    
			if( obj instanceof Set ) {
				return (Set)obj ;
			}
			else {
				throw new ConsistencyException() ;
			}   
		}
        
        private Step locateStep( Workflow workflow, String xpathKey ) throws ConsistencyException {
        
            Object obj = workflow.findXPathValue( xpathKey );
                    
            if( obj instanceof Step ) {
                return (Step)obj ;
            }
            else {
                throw new ConsistencyException() ;
            }   
        }
        
		private Unset locateUnset( Workflow workflow, String xpathKey ) throws ConsistencyException {
        
			Object obj = workflow.findXPathValue( xpathKey );
                    
			if( obj instanceof Unset ) {
				return (Unset)obj ;
			}
			else {
				throw new ConsistencyException() ;
			}   
		}        
        
		private While locateWhile( Workflow workflow, String xpathKey ) throws ConsistencyException {
        
			Object obj = workflow.findXPathValue( xpathKey );
                    
			if( obj instanceof While ) {
					return (While)obj ;
			}
			else {
				throw new ConsistencyException() ;
			}   
		}        
        
		private Script locateScript( Workflow workflow, String xpathKey ) throws ConsistencyException {
        
			Object obj = workflow.findXPathValue( xpathKey );
                    
			if( obj instanceof Script ) {
			    return (Script)obj ;
			}
		    else {
			    throw new ConsistencyException() ;
			}   
		}        

		private If locateIf( Workflow workflow, String xpathKey ) throws ConsistencyException {
        
			Object obj = workflow.findXPathValue( xpathKey );
                    
			if( obj instanceof If ) {
				return (If)obj ;
			}
			else {
				throw new ConsistencyException() ;
			}   
		}

		private For locateFor( Workflow workflow, String xpathKey ) throws ConsistencyException {
        
			Object obj = workflow.findXPathValue( xpathKey );
                    
			if( obj instanceof For ) {
					return (For)obj ;
			}
			else {
				throw new ConsistencyException() ;
			}   
		}
		
		private Parfor locateParfor( Workflow workflow, String xpathKey ) throws ConsistencyException {
        
			Object obj = workflow.findXPathValue( xpathKey );
                    
			if( obj instanceof Parfor ) {
					return (Parfor)obj ;
			}
			else {
				throw new ConsistencyException() ;
			}   
		}		
        
        private ActivityContainer locateActivityContainer( Workflow workflow, String xpathKey ) throws ConsistencyException {
            
            Object obj = workflow.findXPathValue( xpathKey );
            ActivityContainer activityContainer = null;
                
            if( obj instanceof Flow || obj instanceof Sequence ) {
               activityContainer = (ActivityContainer)obj ;
            }
            else {
               throw new ConsistencyException() ;
            } 
                              
            return activityContainer;  
            
        } // end of locateActivityContainer()
        
        
		private AbstractActivity locateAbstractActivity( Workflow workflow, String xpathKey ) throws ConsistencyException {
            
			Object obj = workflow.findXPathValue( xpathKey );
		    AbstractActivity abstractActivity = null;
                
			if( obj instanceof Flow || obj instanceof Sequence ) {
				 // do something
			}
			else if( obj instanceof Step || obj instanceof Script ) {
			    abstractActivity = (AbstractActivity)obj ;
			    // success
			}
			else {
				 throw new ConsistencyException() ;
			} 
                              
			return abstractActivity;  
            
		} // end of locateActivity()        
        
        
        private ApplicationDescription locateDescription( String toolName ) throws WorkflowInterfaceException {
           if( TRACE_ENABLED ) trace( "DesignActionImpl.locateDescription(toolName) entry" ) ;
           
           ApplicationDescription description = null;
           String toolCacheKey = TOOLS_CACHE + toolName;
           String WorkflowMessage = null ;
           
           try {
               
              description = (ApplicationDescription)session.getAttribute( toolCacheKey );
              
              if( description == null ){
                  ApplicationRegistry applRegistry = workflowManager.getToolRegistry();
                  description = applRegistry.getDescriptionFor( toolName );
                  session.setAttribute( toolCacheKey, description );
              }
                                                        
           }
           catch(WorkflowInterfaceException wiex) {
				WorkflowMessage =  "Unable to locate tool named: "+toolName ;
           }
           finally {
			  this.session.setAttribute( WORKFLOW_SUBMIT_MESSAGE, WorkflowMessage ) ;
              if( TRACE_ENABLED ) trace( "DesignActionImpl.locateDescription(toolName) exit" ) ;
           }        
           
           return description;
                             
        } // end of locateDescription(toolName)
           
           
                          
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