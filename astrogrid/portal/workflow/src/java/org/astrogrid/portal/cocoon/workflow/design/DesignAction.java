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
   
//import org.astrogrid.i18n.*;
//import org.astrogrid.AstroGridException;
//import org.astrogrid.portal.workflow.WKF;
//import org.astrogrid.portal.workflow.WorkflowException;
import org.astrogrid.portal.workflow.intf.*;
import org.astrogrid.portal.cocoon.workflow.WorkflowHelper;

import org.astrogrid.applications.beans.v1.parameters.ParameterValue;

import org.astrogrid.workflow.beans.v1.AbstractActivity;
import org.astrogrid.workflow.beans.v1.ActivityContainer;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.Flow;
import org.astrogrid.workflow.beans.v1.Sequence;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.types.JoinType;

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

import java.util.Map;
import java.util.HashMap;
import java.util.Enumeration;
import java.net.MalformedURLException;

import org.apache.avalon.framework.parameters.Parameters;
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
        ACTIVITY_KEY_PARAMETER = "activity_key",
        WORKFLOW_LIST_PARAMETER = "workflow-list",
        QUERY_LIST_PARAMETER = "query-list",
	    TOOL_LIST_PARAMETER = "tool-list",
	    TOOL_NAME_PARAMETER = "tool_name",
		STEP_KEY_PARAMETER = "step-key",
        ERROR_MESSAGE_PARAMETER = "ErrorMessage",
        LOCATION_PARAMETER = "location",
	    PARAM_NAME_PARAMETER = "param-name",
	    PARAM_VALUE_PARAMETER = "param-value",
        DIRECTION_PARAMETER = "direction",
	    STEP_NAME_PARAMETER = "step_name",
	    STEP_DESCRIPTION_PARAMETER = "step_description";
        
    public static final String
        TOOLS_CACHE = "tools-cache-";
        
    public static final String
        FLOW_KEY_PARAMETER = "flow-key",
        SEQUENCE_KEY_PARAMETER = "sequence-key",
        ACTIVITY_INDEX_PARAMETER = "activity_index_key";
        
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
        ACTION_EDIT_JOINCONDITION = "edit-join-condition",
        ACTION_READ_QUERY = "read-query",
        ACTION_READ_QUERY_LIST = "read-query-list",
	    ACTION_CREATE_TOOL = "create-tool-for-step",
        ACTION_INSERT_TOOL_INTO_STEP = "insert-tool-into-step",
        ACTION_INSERT_STEP = "insert-step",
        ACTION_INSERT_SEQUENCE = "insert-sequence", 
        ACTION_INSERT_FLOW = "insert-flow",
		ACTION_INSERT_PARAMETER = "insert-parameter-value",
	    ACTION_INSERT_INPUT_PARAMETER_INTO_TOOL = "insert-input-parameter-into-tool",
	    ACTION_INSERT_OUTPUT_PARAMETER_INTO_TOOL = "insert-output-parameter-into-tool",
	    ACTION_INSERT_INPUT_PARAMETER = "insert-input-value",
	    ACTION_INSERT_OUTPUT_PARAMETER = "insert-output-value",
	    ACTION_RESET_PARAMETER = "reset-parameter",
	    ACTION_REMOVE_TOOL_FROM_STEP = "remove-tool-from-step",
	    ACTION_ADD_STEP_DESCRIPTION = "add-step-description",
	    ACTION_ADD_STEP_NAME = "add-step-name", 	    
        ACTION_READ_LISTS = "read-lists",
        ACTION_REMOVE_WORKFLOW_FROM_SESSION = "remove-workflow-from-session";
        
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
//					template = request.getParameter( TEMPLATE_PARAM_TAG ); 
//					if ( template.equals( EMPTY_TEMPLATE ) ) {
//						this.createWorkflow();
//					}
//                    else {
//						this.createWorkflowFromTemplate( template ); 
//                    }
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
                else if( action.equals( ACTION_EDIT_JOINCONDITION ) ) {
                    this.editJoinCondition(); 
                } 
                else if( action.equals( ACTION_READ_TOOL_LIST ) ) {
                    this.readToolList(); 
                }
				else if( action.equals( ACTION_READ_LISTS ) ) {
					this.readLists(); 
				}			
				else if( action.equals( ACTION_INSERT_TOOL_INTO_STEP ) ) {
					this.insertToolIntoStep();       													
			    }
			    else if( action.equals( ACTION_INSERT_INPUT_PARAMETER ) ) {
					this.insertInputValue();         	
				}                
				else if( action.equals( ACTION_INSERT_OUTPUT_PARAMETER ) ) {
					this.insertOutputValue();
				}
				else if( action.equals( ACTION_INSERT_PARAMETER ) ) {
					this.insertValue();
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
				else if( action.equals( ACTION_ADD_STEP_DESCRIPTION )){
					this.insertStepDescription();  
				}
				else if( action.equals( ACTION_ADD_STEP_NAME )){
					this.insertStepName();  
				}				
				else if( action.equals( ACTION_REMOVE_WORKFLOW_FROM_SESSION )){
					this.removeWorkflow();  
				}				
                else if( action.equals( ACTION_INSERT_FLOW ) ) {
                    this.insertFlow();                                                    
                }   
                else if( action.equals( ACTION_INSERT_SEQUENCE ) ) {
                    this.insertSequence();                                                    
                } 
                else if( action.equals( ACTION_INSERT_STEP ) ) {
                    this.insertStep();                                                    
                }      			                
                else {
                    results = null;
                    debug( "unsupported action"); 
                    throw new UnsupportedOperationException( action + " no longer supported");
                }
                				
this.readToolList(); // temp PJN
                
                if (workflow != null ){
	                // Save the workflow in the session object...
    	            debug( "about to set session attribute..." ) ;
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
                
                // PJN note: alterred slightly,
                // also not sure if LoginAction intends to put security token
                // into session?
                             
                this.userid = (String)session.getAttribute( USER_TAG );
                debug( "userid: " + this.userid );
                this.community = (String)session.getAttribute( COMMUNITY_NAME_TAG );
                debug( "community: " + this.community );
                this.group = (String)session.getAttribute( CREDENTIAL_TAG );
                debug( "group: " + this.group ); 
//                SecurityToken secToken =
//                   (SecurityToken)session.getAttribute( COMMUNITY_TOKEN_TAG );
//                this.token = secToken.getToken();
                debug( "token: " + this.token ); 
                
                Account account = new Account();
                account.setName( userid );
                account.setCommunity( community );
                Group group = new Group();
                group.setName( this.group );
                group.setCommunity( community );
                        
                this.credentials = new Credentials();
                credentials.setAccount( account );
                credentials.setGroup( group );
                credentials.setSecurityToken( "dummy" );
                
                this.user = new User();
                user.setAccount( userid );
                user.setGroup( this.group );
                user.setToken( token );
                     
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
                    debug( "Create ignored - bConfirm == false: however at the minute I am ignoring bConfirm and creating workflow regardless!" ) ;
					workflow = workflowBuilder.createWorkflow( credentials, name, description ) ;
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
            
            String ivornName = request.getParameter( WORKFLOW_IVORN_PARAMETER ) ;
            Ivorn ivorn = null;
                                       
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
                    session.removeAttribute( HTTP_WORKFLOW_TAG );
					workflow = null ;
                }            
            }
            catch( CommunityIdentifierException cix ) {
                cix.printStackTrace();
            }
            catch( WorkflowInterfaceException wix ) {
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
              
            String
                 name = request.getParameter( WORKFLOW_NAME_PARAMETER ),
                 ivornName = request.getParameter( WORKFLOW_IVORN_PARAMETER ) ;
            Ivorn ivorn = null;
              
            try {
                                 
                if( (name == null) || (ivornName == null) ) {
                    ; // some logging here
                    throw new ConsistencyException() ; 
                }
                
                ivorn = (new CommunityIvornParser(ivornName)).getIvorn();
                
                if( workflow == null  || bConfirm == true ) {
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
            Ivorn ivorn = null;
                
            try {

               if( ivornName == null ) {
                  ; // some logging here
                  throw new ConsistencyException() ;
               }
               
               ivorn = (new CommunityIvornParser(ivornName)).getIvorn();

			   if( workflow == null ) {
                  WorkflowStore wfStore = this.workflowManager.getWorkflowStore();
                  workflow = wfStore.readWorkflow( user, ivorn ) ;
			   }

               if( workflow == null ) {
                  ; // some logging here
                  throw new ConsistencyException() ; 
               }
               else {
                  JobExecutionService jes = this.workflowManager.getJobExecutionService();
                  jes.submitWorkflow( workflow ) ;
               }
                          
            }
            catch( CommunityIdentifierException cix ) {
                cix.printStackTrace();
            }
            catch( WorkflowInterfaceException wix ) {
                wix.printStackTrace(); // JBL note
            }            
            finally {
                if( TRACE_ENABLED ) trace( "DesignActionImpl.submitWorkflow() exit" ) ;
            }
         
        } // end of submitQuery()
        
        
        
        private void editJoinCondition() throws ConsistencyException {
            if( TRACE_ENABLED ) trace( "DesignActionImpl.editJoinCondition() entry" ) ;
              
            Step step = null;
            JoinType joinCondition = null;
              
            try {
                
                if( workflow == null ) {
                    throw new ConsistencyException() ; 
                }
                
                String editCondition = request.getParameter( EDIT_CONDITION_PARAMETER ) ;
                    
                if( editCondition == null ) {
                    throw new ConsistencyException() ;
                }
                else if( editCondition.equalsIgnoreCase( JoinType.ANY.toString() ) ) {
                    joinCondition =  JoinType.ANY ;
                }
                else if( editCondition.equalsIgnoreCase( JoinType.TRUE.toString() ) ) {
                    joinCondition =  JoinType.TRUE ;
                }
                else if( editCondition.equalsIgnoreCase( JoinType.FALSE.toString() ) ) {
                    joinCondition =  JoinType.FALSE ;
                }
                else {
                    throw new ConsistencyException() ; 
                }
             
                step = locateStep( workflow, request.getParameter( ACTIVITY_KEY_PARAMETER ) );
                step.setJoinCondition( joinCondition ) ;                
                               
            }
            finally {
                if( TRACE_ENABLED ) trace( "DesignActionImpl.editJoinCondition() exit" ) ;
            }
         
        } // end of editJoinCondition()
        
        
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
           
		   try {
                    
			  if( toolName == null ) {
			     throw new ConsistencyException() ;		   	
			  }
              
//              ApplicationRegistry applRegistry = workflowManager.getToolRegistry();
//              ApplicationDescription applDescription = applRegistry.getDescriptionFor( toolName );
			  tool = this.locateDescription(toolName).createToolFromDefaultInterface();
				                           
			  this.request.setAttribute( HTTP_TOOL_TAG, tool ) ;
				trace("-----------------------------------------") ;
				trace("tool put into request object") ;
				trace("Name:" + tool.getName() ) ;
//				trace("Documentation: " + tool.getDocumentation() ) ;
				trace("Input parameters:") ;
                
                Enumeration iterator = tool.getInput().enumerateParameter() ;
				while(iterator.hasMoreElements()) {
					ParameterValue p = (ParameterValue)iterator.nextElement() ;
					trace("input param: Name " + p.getName()) ;
//					trace("input param: Documentation " + p.getDocumentation()) ;
					trace("input param: Type " + p.getType()) ;
//					trace("input param: Cardinality " + p.getCardinality()) ;
					trace("input param: value " + p.getValue()) ;
					
				}
				trace("-----------------------------------------") ;			                            	   	  
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
           
           String[] tools = (String[])this.session.getAttribute(TOOL_LIST_PARAMETER) ;
              
           try {
                         
              if( tools == null ) {
                  ApplicationRegistry toolRegistry = workflowManager.getToolRegistry();
                  tools = toolRegistry.listApplications();
                  debug( "tools list: " ); 
                  if(tools != null){
                      for( int i=0; i<tools.length; i++ ){
                          debug( tools[i]);
                      }
                  }
                  else {
                      debug( "tools list is null" );
                  } 
                  this.session.setAttribute( TOOL_LIST_PARAMETER, tools ) ;   
              }
              this.request.setAttribute( TOOL_LIST_PARAMETER, tools ) ; 
       
           }
           catch( WorkflowInterfaceException wix ) {
                debug( "wix exception: " + wix.toString() );
                this.request.setAttribute( ERROR_MESSAGE_PARAMETER, wix.toString() ) ;
           }           
           finally {
              if( TRACE_ENABLED ) trace( "DesignActionImpl.readToolList() exit" ) ;
           }
           
        } // end of readQueryList()           


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
                    this.readToolList();

				}
				catch( Exception ex ) {              
					this.request.setAttribute( ERROR_MESSAGE_PARAMETER, "permission denied" ) ;
				}
				finally {
					if( TRACE_ENABLED ) trace( "DesignActionImpl.readLists() exit" ) ;
				}
                    
			} // end of readLists()
           
		private void insertToolIntoStep() throws ConsistencyException {
			if( TRACE_ENABLED ) trace( "DesignActionImpl.insertToolIntoStep() entry" ) ;
			
            Tool tool = null;
            Step step = null;
              
			try {
				
//				this.tool = (Tool) request.getAttribute( HTTP_TOOL_TAG ) ;
					
                String toolname = request.getParameter( TOOL_NAME_PARAMETER ) ;					
//				if (toolname == null) {
//					debug("tool-name is null") ;
//				}
                debug("tool name: " + toolname);
                debug("activity key: " + request.getParameter( ACTIVITY_KEY_PARAMETER ) );
                
                step = locateStep( workflow, request.getParameter( ACTIVITY_KEY_PARAMETER ) );     
                
                tool = this.createTool( toolname ) ; 
                
                if ( tool == null ) {
                	debug( "tool is null" ) ;
                }
                else {                	
				    step.setTool( tool );
                }   
						
			}
			finally {
				if( TRACE_ENABLED ) trace( "DesignActionImpl.insertQueryIntoStep() exit" ) ;
			}
                    
		} // end of insertToolIntoStep() 

           
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


		private void insertValue() throws ConsistencyException {
			if( TRACE_ENABLED ) trace( "DesignActionImpl.insertValue() entry" ) ;
			try {		
				String direction = request.getParameter( DIRECTION_PARAMETER ) ;
				debug("direction: " + direction);
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

         try {
            // Tool should already have been inserted into step
									
			String parameterName = request.getParameter( PARAM_NAME_PARAMETER ) ;				    					
			String parameterValue = request.getParameter( PARAM_VALUE_PARAMETER ) ;
			String activityKey = request.getParameter( ACTIVITY_KEY_PARAMETER ) ;
			debug( "parameterName:" + parameterName ) ;
			debug( "parameterValue: " + parameterValue ) ;
			debug( "activityKey: " + activityKey ) ;    
                            
			if ( parameterName == null) {
				debug( "parameterName is null" ) ;
			}
			else if ( parameterValue == null) {
				debug( "parameterValue is null" ) ;
			}
			else if ( activityKey == null) {
				debug( "activityKey is null" ) ;
			}            

            step = locateStep( workflow, request.getParameter( ACTIVITY_KEY_PARAMETER ) );
            tool = step.getTool() ;
            ApplicationRegistry applRegistry = workflowManager.getToolRegistry();
            ApplicationDescription applDescription = applRegistry.getDescriptionFor( tool.getName() );	
            WorkflowHelper.insertInputParameterValue( applDescription
                                                    , tool
                                                    , parameterName
                                                    , null
                                                    , parameterValue ) ;
				
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
              
	       try {
				 // Tool should already have been inserted into step
									
			     String
					parameterName = request.getParameter( PARAM_NAME_PARAMETER ),			  
					parameterValue = request.getParameter( PARAM_VALUE_PARAMETER ) ;
                            
			     if ( parameterName == null) {
					debug( "parameterName is null" ) ;
			     }
			     else if ( parameterValue == null) {
					debug( "parameterValue is null" ) ;
			     }

                 step = locateStep( workflow, request.getParameter( ACTIVITY_KEY_PARAMETER ) );
			     tool = step.getTool() ; 
				
               step = locateStep( workflow, request.getParameter( ACTIVITY_KEY_PARAMETER ) );
               tool = step.getTool() ;
               ApplicationRegistry applRegistry = workflowManager.getToolRegistry();
               ApplicationDescription applDescription = applRegistry.getDescriptionFor( tool.getName() );
                
               WorkflowHelper.insertOutputParameterValue( applDescription
                                                        , tool
                                                        , parameterName
                                                        , null
                                                        , parameterValue ) ;
				
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
              WorkflowHelper.insertInputParameterValue( applDescription, tool, parameterName, parameterValue, "" ) ;   
          } else {
              WorkflowHelper.insertInputParameterValue( applDescription, tool, parameterName, parameterValue, "" ) ;   
          }
                                           
				
       }
       catch( WorkflowInterfaceException wix ) {
           wix.printStackTrace();
       }
	   finally {
          if( TRACE_ENABLED ) trace( "DesignActionImpl.resetParameter() exit" ) ;
	   }
                    
    } // end of deleteParameter() 

 
 
//		private void insertInputParameterIntoTool() throws ConsistencyException {
//			if( TRACE_ENABLED ) trace( "DesignActionImpl.insertInputParameterIntoTool() entry" ) ;
//			
//            Step step = null ;
//            Tool tool = null ;
//              
//			try {
//				// Tool should already have been inserted into step
//									
//				String contents = request.getParameter( LOCATION_PARAMETER ) ;				    					
//                
//                if ( contents == null) {
//					debug( "contents is null" ) ;
//				}
//
//				step = locateStep( workflow, request.getParameter( ACTIVITY_KEY_PARAMETER ) );				
//				Enumeration iterator = step.getTool().getInput().enumerateParameter() ;
//				ParameterValue p = null;
//             
//				while( iterator.hasMoreElements() ) {
//				
//				    p = (ParameterValue)iterator.nextElement() ;
//					{
//
//						if ( p.getContent().length() <= 0 ) //look for 1st parameter where location (?) hasn't been set,
//
//							 p.setContent( contents ) ;     //for query tool this will be 1st parameter, for tools with greater 
//							 break ;                        //cardinality than 1 this may not be the case.
//					}		 
//				}
//			
//				
//			}
//			finally {
//				if( TRACE_ENABLED ) trace( "DesignActionImpl.insertInputParameterIntoTool() exit" ) ;
//			}
//                    
//		} // end of inserInputParameterIntoTool()       
        

//		private void insertOutputParameterIntoTool() throws ConsistencyException {
//			if( TRACE_ENABLED ) trace( "DesignActionImpl.insertOutputParameterIntoTool() entry" ) ;
//			
//						Step
//								step = null ;
//						Tool
//								tool = null ;
//						Parameter
//								param = null ;
//              
//			try {
//				// Tool should already have been inserted into step
//									
//				String
//						stepActivityKey = request.getParameter( ACTIVITY_KEY_PARAMETER ) ;					
//                
//				if ( stepActivityKey == null) {
//						debug( "stepActivityKey is null" ) ;
//				}
//
//				step = locateStep( workflow, stepActivityKey );
//				
//				ListIterator ListIt = step.getTool().getOutputParameters() ;
//			
//				
//			}
//			finally {
//				if( TRACE_ENABLED ) trace( "DesignActionImpl.insertOutputParameterIntoTool() exit" ) ;
//			}
//                    
//		} // end of insertOutputParameterIntoTool()


        private void insertStep() throws ConsistencyException {
           if( TRACE_ENABLED ) trace( "DesignActionImpl.insertStep() entry" ) ;
              
           try {
              this.insertActivity( new Step() ) ;
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


        private void insertSequence() throws ConsistencyException {
           if( TRACE_ENABLED ) trace( "DesignActionImpl.insertSequence() entry" ) ;
              
           try {
              this.insertActivity( new Sequence() ) ;          
            }
            finally {
                  if( TRACE_ENABLED ) trace( "DesignActionImpl.insertSequence() exit" ) ;
            }
                    
        } // end of insertSequence()       

//      private void insertInputParameterIntoTool() throws ConsistencyException {
//          if( TRACE_ENABLED ) trace( "DesignActionImpl.insertInputParameterIntoTool() entry" ) ;
//          
//            Step step = null ;
//            Tool tool = null ;
//              
//          try {
//              // Tool should already have been inserted into step
//                                  
//              String contents = request.getParameter( LOCATION_PARAMETER ) ;                                      
//                
//                if ( contents == null) {
//                  debug( "contents is null" ) ;
//              }
//
//              step = locateStep( workflow, request.getParameter( ACTIVITY_KEY_PARAMETER ) );              
//              Enumeration iterator = step.getTool().getInput().enumerateParameter() ;
//              ParameterValue p = null;
//             
//              while( iterator.hasMoreElements() ) {
//              
//                  p = (ParameterValue)iterator.nextElement() ;
//                  {
//
//                      if ( p.getContent().length() <= 0 ) //look for 1st parameter where location (?) hasn't been set,
//
//                           p.setContent( contents ) ;     //for query tool this will be 1st parameter, for tools with greater 
//                           break ;                        //cardinality than 1 this may not be the case.
//                  }        
//              }
//          
//              
//          }
//          finally {
//              if( TRACE_ENABLED ) trace( "DesignActionImpl.insertInputParameterIntoTool() exit" ) ;
//          }
//                    
//      } // end of inserInputParameterIntoTool()       
        

//      private void insertOutputParameterIntoTool() throws ConsistencyException {
//          if( TRACE_ENABLED ) trace( "DesignActionImpl.insertOutputParameterIntoTool() entry" ) ;
//          
//                      Step
//                              step = null ;
//                      Tool
//                              tool = null ;
//                      Parameter
//                              param = null ;
//              
//          try {
//              // Tool should already have been inserted into step
//                                  
//              String
//                      stepActivityKey = request.getParameter( ACTIVITY_KEY_PARAMETER ) ;                  
//                
//              if ( stepActivityKey == null) {
//                      debug( "stepActivityKey is null" ) ;
//              }
//
//              step = locateStep( workflow, stepActivityKey );
//              
//              ListIterator ListIt = step.getTool().getOutputParameters() ;
//          
//              
//          }
//          finally {
//              if( TRACE_ENABLED ) trace( "DesignActionImpl.insertOutputParameterIntoTool() exit" ) ;
//          }
//                    
//      } // end of insertOutputParameterIntoTool()


        private void insertActivity( AbstractActivity activity ) throws ConsistencyException {
           if( TRACE_ENABLED ) trace( "DesignActionImpl.insertActivity() entry" ) ;
            
           Step step = null;
           ActivityContainer activityContainer = null;
           int index = 0;
           int highWaterMark = 0;
              
           try {

              String activityTargetKey = request.getParameter( ACTIVITY_KEY_PARAMETER ) ;
              debug("activityTargetKey: " + activityTargetKey ) ;                                     
              String activityIndex = request.getParameter( ACTIVITY_INDEX_PARAMETER ) ;
              debug("activityIndex: " + activityIndex ) ;
                            
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
              
              activityContainer = locateActivityContainer( workflow, activityTargetKey );
			 
              highWaterMark = activityContainer.getActivityCount() - 1;
              if( highWaterMark < 0 ) {
                  highWaterMark = 0;
              }
                  
              if( index < 0 || index > highWaterMark ){
                  index = highWaterMark;
              }
                  
              activityContainer.addActivity( index, activity ) ;
                        
            }
            finally {
                  if( TRACE_ENABLED ) trace( "DesignActionImpl.insertActivity() exit" ) ;
            }
                    
        } // end of insertActivity()       


		private void insertStepName() throws ConsistencyException {
			if( TRACE_ENABLED ) trace( "DesignActionImpl.insertStepName() entry" ) ;
			
			Step step = null ;
            
			try {
	
				String xpathKey = request.getParameter( ACTIVITY_KEY_PARAMETER ) ;
				String name = request.getParameter( STEP_NAME_PARAMETER ) ;
				
				debug( "xpathKey: " + xpathKey ) ;
				debug( "name: " + name ) ;
                                
				if ( xpathKey == null) {
					debug( "xpathKey is null" ) ;
				}

				step = locateStep( workflow, xpathKey );  
				step.setName( name );
				
				
			}
			finally {
				if( TRACE_ENABLED ) trace( "DesignActionImpl.insertStepName() exit" ) ;
			}
                    
		} // end of insertStepName()


		private void insertStepDescription() throws ConsistencyException {
			if( TRACE_ENABLED ) trace( "DesignActionImpl.insertStepDescription() entry" ) ;
			
             Step step = null ;
            
			 try {
	
			    String xpathKey = request.getParameter( ACTIVITY_KEY_PARAMETER ) ;
				String description = request.getParameter( STEP_DESCRIPTION_PARAMETER ) ;
                                
				if ( xpathKey == null) {
					debug( "xpathKey is null" ) ;
				}

                step = locateStep( workflow, xpathKey );  
                step.setDescription( description );
				
			}
			finally {
				if( TRACE_ENABLED ) trace( "DesignActionImpl.insertStepDescription() exit" ) ;
			}
                    
		} // end of insertStepDescription()


        
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
        
        private Step locateStep( Workflow workflow, String xpathKey ) throws ConsistencyException {
        
            Object obj = workflow.findXPathValue( xpathKey );
                    
            if( obj instanceof Step ) {
                return (Step)obj ;
            }
            else {
                throw new ConsistencyException() ;
            }   
        }
        
        
        private ActivityContainer locateActivityContainer( Workflow workflow, String xpathKey ) throws ConsistencyException {
        
            Object obj = workflow.findXPathValue( xpathKey );
                    
            if( obj instanceof Flow || obj instanceof Sequence ) {
                return (ActivityContainer)obj ;
            }
            else {
                throw new ConsistencyException() ;
            }   
            
        } // end of locateActivityContainer()
        
        
        private ApplicationDescription locateDescription( String toolName ) throws WorkflowInterfaceException {
           if( TRACE_ENABLED ) trace( "DesignActionImpl.locateDescription(toolName) entry" ) ;
           
           ApplicationDescription description = null;
           String toolCacheKey = TOOLS_CACHE + toolName;
           
           try {
               
              description = (ApplicationDescription)session.getAttribute( toolCacheKey );
              
              if( description == null ){
                  ApplicationRegistry applRegistry = workflowManager.getToolRegistry();
                  description = applRegistry.getDescriptionFor( toolName );
                  session.setAttribute( toolCacheKey, description );
              }
                                                        
           }
           finally {
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