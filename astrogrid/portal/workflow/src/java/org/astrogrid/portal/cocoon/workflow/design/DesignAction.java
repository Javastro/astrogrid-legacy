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
import org.astrogrid.portal.workflow.WorkflowException;
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

//import org.astrogrid.config.Config;
//import org.astrogrid.config.SimpleConfig;

import java.util.Map;
import java.util.HashMap;
import java.util.Enumeration;

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
	    WORKFLOW_DESCRIPTION_PARAMETER = "workflow-description";
        
    public static final String
        EDIT_CONDITION_PARAMETER = "edit-condition",
        ACTIVITY_KEY_PARAMETER = "activity-key",
        WORKFLOW_LIST_PARAMETER = "workflow-list",
        QUERY_LIST_PARAMETER = "query-list",
	    TOOL_LIST_PARAMETER = "tool-list",
	    TOOL_NAME_PARAMETER = "tool-name",
		STEP_KEY_PARAMETER = "step-key",
        ERROR_MESSAGE_PARAMETER = "ErrorMessage",
        LOCATION_PARAMETER = "location",
	    PARAM_NAME_PARAMETER = "param-name",
	    PARAM_VALUE_PARAMETER = "param-value",
        DIRECTION_PARAMETER = "direction",
	    STEP_DESCRIPTION_PARAMETER = "step-description";
        
    public static final String
        FLOW_KEY_PARAMETER = "flow-key",
        SEQUENCE_KEY_PARAMETER = "sequence-key",
        ACTIVITY_INDEX_PARAMETER = "activity-index-key";
        
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
		ACTION_COPY_WORKFLOW = "copy-workflow",
        ACTION_CHOOSE_QUERY = "choose-query",
        ACTION_EDIT_JOINCONDITION = "edit-join-condition",
        ACTION_READ_QUERY = "read-query",
        ACTION_READ_QUERY_LIST = "read-query-list",
	    ACTION_CREATE_TOOL = "create-tool-for-step",
        ACTION_INSERT_TOOL_INTO_STEP = "insert-tool-into-step",
	    ACTION_INSERT_INPUT_PARAMETER_INTO_TOOL = "insert-input-parameter-into-tool",
	    ACTION_INSERT_OUTPUT_PARAMETER_INTO_TOOL = "insert-output-parameter-into-tool",
	    ACTION_INSERT_INPUT_PARAMETER = "insert-input-value",
	    ACTION_INSERT_OUTPUT_PARAMETER = "insert-output-value",
	    ACTION_RESET_PARAMETER = "reset-parameter",
	    ACTION_REMOVE_TOOL_FROM_STEP = "remove-tool-from-step",
	    ACTION_ADD_STEP_DESCRIPTION = "add-step-description", 	    
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
//            debug( "About to check properties loaded") ;
            // Load the workflow config file and messages...
//            WKF.getInstance().checkPropertiesLoaded() ;
//            debug( "Properties loaded OK") ;
            
            myAction = new DesignActionImpl( redirector
                                           , resolver
                                           , objectModel
                                           , source
                                           , params ) ;
                                           
            retMap = myAction.act() ;    
                                          
        }
//        catch ( AstroGridException agex ) {
//            debug( agex.toString() ) ;
//        }
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
            	
				debug( "action is: " + action );
                
                this.consistencyCheck();
                    
                if( action == null ){
                    debug( "action is null");  
                }      
                else if( action.equals( ACTION_CREATE_WORKFLOW ) ) {
					template = request.getParameter( TEMPLATE_PARAM_TAG ); 
					if ( template.equals( EMPTY_TEMPLATE ) ) {
						this.createWorkflow();
					}
                    else {
						this.createWorkflowFromTemplate( template ); 
                    }
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
                else if( action.equals( ACTION_DELETE_WORKFLOW ) ) {
                    this.deleteWorkflow(); 
                }
                else if( action.equals( ACTION_SUBMIT_WORKFLOW ) ) {
                    this.submitWorkflow(); 
                }
                else if( action.equals( ACTION_CHOOSE_QUERY ) ) {
                    this.chooseQuery(); 
                }
                else if( action.equals( ACTION_EDIT_JOINCONDITION ) ) {
                    this.editJoinCondition(); 
                }
                else if( action.equals( ACTION_READ_WORKFLOW_LIST ) ) {
                    this.readWorkflowList(); 
                }
                else if( action.equals( ACTION_READ_QUERY_LIST ) ) {
                    this.readQueryList(); 
                }
				else if( action.equals( ACTION_READ_LISTS ) ) {
					this.readLists(); 
				}			
				else if( action.equals( ACTION_INSERT_TOOL_INTO_STEP ) ) {
					this.insertToolIntoStep();       					
                }
			    else if( action.equals( ACTION_INSERT_INPUT_PARAMETER_INTO_TOOL ) ) {
//					this.insertInputParameterIntoTool();         	
				}                
				else if( action.equals( ACTION_INSERT_OUTPUT_PARAMETER_INTO_TOOL ) ) {
//				    this.insertOutputParameterIntoTool();                     								
			    }
			    else if( action.equals( ACTION_INSERT_INPUT_PARAMETER ) ) {
					this.insertInputValue();         	
				}                
				else if( action.equals( ACTION_INSERT_OUTPUT_PARAMETER ) ) {
					this.insertOutputValue();
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
				else if( action.equals( ACTION_REMOVE_WORKFLOW_FROM_SESSION )){
					this.removeWorkflow();  
				}				
				else if( action.equals( ACTION_COPY_WORKFLOW ) ) {
					this.copyWorkflow();                     								
				}				                
                else {
                    debug( "unsupported action"); 
                }
                
                if (workflow != null ){
	                // Save the workflow in the session object...
    	            debug( "about to set session attribute..." ) ;
        	        session.setAttribute( HTTP_WORKFLOW_TAG, workflow ) ;
            	    debug( session.getAttribute(HTTP_WORKFLOW_TAG).toString() ); 
                }
                
				this.readLists() ; // Ensure request object contains latest Workflow/Query
            }
            catch( ConsistencyException cex ) {
                debug( "ConsistencyException occurred");
            }
            //JBL Note: these should only be here during testing...
            catch( Exception ex) {
                debug( "Exception: ex" );
                ex.printStackTrace();
            }
            //JBL Note: these should only be here during testing...
            catch( Throwable th ) {
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
                group.setCommunity( community );
                        
                this.credentials = new Credentials();
                credentials.setAccount( account );
                credentials.setGroup( group );
                credentials.setSecurityToken( "1234" );
                     
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
                    ; // some logging here
                    throw new ConsistencyException() ;
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
                    debug( "Create ignored - bConfirm == false" ) ;
                }
        
            }
            catch( WorkflowInterfaceException wix ) {
                ;
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
                    WorkflowStore wfStore = this.workflowManager.getWorkflowStore();
                    wfStore.saveWorkflow( credentials.getAccount(), workflow ) ;
					session.setAttribute( HTTP_WORKFLOW_TAG, null) ;
					workflow = null ;
                }            
            }
            catch( WorkflowInterfaceException wix ) {
                ;
            }
            finally {
                if( TRACE_ENABLED ) trace( "DesignActionImpl.saveWorkflow() exit" ) ;
            }
         
        } // end of createWorkflow()
        
   
        private void removeWorkflow() throws ConsistencyException {
            if( TRACE_ENABLED ) trace( "DesignActionImpl.removeWorkflow() entry" ) ;
              
            try {
                session.setAttribute( HTTP_WORKFLOW_TAG, null) ;
                workflow = null ;
            }
            finally {
               if( TRACE_ENABLED ) trace( "DesignActionImpl.removeWorkflow() exit" ) ;
            }
        } // end of removeWorkflow()   
        
        
        private void readWorkflow() throws ConsistencyException {
            if( TRACE_ENABLED ) trace( "DesignActionImpl.readWorkflow() entry" ) ;
              
            try {
                
                String
                    name = request.getParameter( WORKFLOW_NAME_PARAMETER ) ;
                    
                if( name == null ) {
                    ; // some logging here
                    throw new ConsistencyException() ;
                }
                
                if( workflow == null  || bConfirm == true ) {
                    WorkflowStore wfStore = this.workflowManager.getWorkflowStore();
                    workflow = wfStore.readWorkflow( credentials.getAccount(), name ) ;
                } 
                
                if( workflow != null ) {   
                    debug( "account: " + workflow.getCredentials().getAccount() ) ;
                    debug( "name: " + workflow.getName() ) ; 
                    debug( "description: " + workflow.getDescription() ) ;         
                }
                    
            }
            catch( WorkflowInterfaceException wix ) {
                ; //JBL note
            }
            finally {
                if( TRACE_ENABLED ) trace( "DesignActionImpl.readWorkflow() exit" ) ;
            }
         
        }

        private void copyWorkflow() throws ConsistencyException {
            if( TRACE_ENABLED ) trace( "DesignActionImpl.copyWorkflow() entry" ) ;
              
            try {
                
				String
                	name = request.getParameter( WORKFLOW_NAME_PARAMETER ) ;
            	String
                	newName = request.getParameter( WORKFLOW_NEW_NAME_PARAMETER ) ;
                trace("Copying workflow: ") ;
                trace("Origional name: " + name) ;
                trace("New name: " + newName ) ;    
            	if( name == null ) {
                	trace( "DesignActionImpl.copyWorkflow(): WORKFLOW_NAME_PARAMETER was null" );
                	throw new ConsistencyException() ;
            	}
            	else if( newName == null ) {
					trace( "DesignActionImpl.copyWorkflow(): WORKFLOW_NEW_NAME_PARAMETER was null" );
					throw new ConsistencyException() ;
				}
				else{
                    WorkflowStore wfStore = this.workflowManager.getWorkflowStore();
                    workflow = wfStore.readWorkflow( credentials.getAccount(), name ) ;
					workflow.setName( newName ) ;
                    wfStore.saveWorkflow( credentials.getAccount(), workflow ) ;
					workflow = null ;   //JBL Is this OK?             
				}
            }
            catch( WorkflowInterfaceException wix ) {
                ; //JBL note
            }
			finally {
				if( TRACE_ENABLED ) trace( "DesignActionImpl.copyWorkflow() exit" ) ;
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
                
                if( workflow == null || bConfirm == true ) {
                    WorkflowStore wfStore = this.workflowManager.getWorkflowStore();
                    wfStore.deleteWorkflow( credentials.getAccount(), name ) ;
                }                    
            }
            catch( WorkflowInterfaceException wix ) {
                ; // JBL note
            }
            finally {
                if( TRACE_ENABLED ) trace( "DesignActionImpl.deleteWorkflow() exit" ) ;
            }
         
        }
        
        
        private void createWorkflowFromTemplate( String template ) throws ConsistencyException {       
            throw new UnsupportedOperationException("DesignActionImpl.createWorkflowFromTemplate() no longer supported");
        } 
        
        
        private void submitWorkflow() throws ConsistencyException {
            if( TRACE_ENABLED ) trace( "DesignActionImpl.submitWorkflow() entry" ) ;

            try {
            	
               String
                  name = request.getParameter( WORKFLOW_NAME_PARAMETER ) ;

               if( name == null ) {
                  ; // some logging here
                  throw new ConsistencyException() ;
               }

			   if( workflow == null ) {
                  WorkflowStore wfStore = this.workflowManager.getWorkflowStore();
                  wfStore.readWorkflow( credentials.getAccount(), name ) ;
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
            catch( WorkflowInterfaceException wix ) {
                 ; // JBL note
            }            
            finally {
                if( TRACE_ENABLED ) trace( "DesignActionImpl.submitWorkflow() exit" ) ;
            }
         
        } // end of submitQuery()
        
        
        
        private void chooseQuery() throws ConsistencyException {
            if( TRACE_ENABLED ) trace( "DesignActionImpl.chooseQuery() entry" ) ;
              
            Step step = null;
            String
                xpathKey = null,
                queryName = null ;
              
            try {
                
                if( workflow == null ) {
                     throw new ConsistencyException() ; 
                 }
                 
                xpathKey = request.getParameter( ACTIVITY_KEY_PARAMETER ) ;
                queryName = request.getParameter( QUERY_NAME_PARAMETER ) ;
                    
                 if( xpathKey == null || queryName == null ) {
                     ; // some logging here
                     throw new ConsistencyException() ;
                 }
             
                 step = locateStep( workflow, xpathKey );
//               Workflow.insertQueryIntoStep( step, queryName ) ;
    
            }
            finally {
                if( TRACE_ENABLED ) trace( "DesignActionImpl.chooseQuery() exit" ) ;
            }
                    
        } // end of chooseQuery()
        
        
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
        
        
        //JBL Note: this seems a redundant action. The tool is not saved anywhere.
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
              
              ApplicationRegistry applRegistry = workflowManager.getToolRegistry();
              ApplicationDescription applDescription = applRegistry.getDescriptionFor( toolName );
			  tool = applDescription.createToolFromDefaultInterface();
				                           
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
					trace("input param: Content " + p.getContent()) ;
					
				}
				trace("-----------------------------------------") ;			                            	   	  
		   }
           catch( WorkflowInterfaceException wix ) {
               ;
           }
	       finally {
		      if( TRACE_ENABLED ) trace( "DesignActionImpl.createTool(toolName) exit" ) ;
		   }		
           
           return tool;
           			         
        } // end of createTool(toolName)
           
           
        private void readWorkflowList() {
            if( TRACE_ENABLED ) trace( "DesignActionImpl.readWorkflowList() entry" ) ;
            
            String[] workflows = null;
              
            try {
                    
                // For the moment this is where we have placed the door.
                // If users cannot see a list, then they cannot do anything...
                this.checkPermissions( AUTHORIZATION_RESOURCE_WORKFLOW
                                     , AUTHORIZATION_ACTION_EDIT ) ;
                               
                //NB: The filter argument is ignored at present (Sept 2003).
                WorkflowStore wfStore = workflowManager.getWorkflowStore();
                workflows = wfStore.listWorkflows( credentials.getAccount() ) ;
                this.request.setAttribute( WORKFLOW_LIST_PARAMETER, workflows ) ;               
            }
            catch( WorkflowInterfaceException wix ) {
                this.request.setAttribute( ERROR_MESSAGE_PARAMETER, wix.toString() ) ;
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
              
            String[] queries = null;
              
            try {   
                WorkflowStore wfStore = workflowManager.getWorkflowStore();
                queries = wfStore.listQueries( credentials.getAccount() ) ;
                this.request.setAttribute( QUERY_LIST_PARAMETER, queries ) ;               
            }
            catch( WorkflowInterfaceException wix ) {
                this.request.setAttribute( ERROR_MESSAGE_PARAMETER, wix.toString() ) ;                       
            }
            finally {
                if( TRACE_ENABLED ) trace( "DesignActionImpl.readQueryList() exit" ) ;
            }
                    
        } // end of readQueryList()
        
        
        private void readToolList() {
           if( TRACE_ENABLED ) trace( "DesignActionImpl.readToolList() entry" ) ;
           
           String[] tools = null;
              
           try {
              ApplicationRegistry toolRegistry = workflowManager.getToolRegistry();
              tools = toolRegistry.listApplications();
              this.request.setAttribute( TOOL_LIST_PARAMETER, tools ) ;               
           }
           catch( WorkflowInterfaceException wix ) {
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
					this.checkPermissions( AUTHORIZATION_RESOURCE_WORKFLOW
									     , AUTHORIZATION_ACTION_EDIT ) ;
                     
                    this.readWorkflowList();          
                    this.readQueryList();
                    this.readToolList();

				}
				catch( WorkflowException wfex ) {              
					this.request.setAttribute( ERROR_MESSAGE_PARAMETER, wfex.toString() ) ;
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


      private void insertInputValue() throws ConsistencyException {
         if( TRACE_ENABLED ) trace( "DesignActionImpl.insertInputValue() entry" ) ;
			
         Step step = null;
         Tool tool = null ;
         ParameterValue p = null ;
              
         try {
            // Tool should already have been inserted into step
									
		    String parameterName = request.getParameter( PARAM_NAME_PARAMETER ) ;				    					
            String parameterValue = request.getParameter( PARAM_VALUE_PARAMETER ) ;
                            
            if ( parameterName == null) {
                debug( "parameterName is null" ) ;
            }
            else if ( parameterValue == null) {
                debug( "parameterValue is null" ) ;
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
                ;
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
               ;
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
           ;
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
              String activityIndex = request.getParameter( ACTIVITY_INDEX_PARAMETER ) ;
                            
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
              
              highWaterMark = activityContainer.getActivityCount() - 1;
              if( highWaterMark < 0 ) {
                  highWaterMark = 0;
              }
                  
              if( index < 0 || index > highWaterMark ){
                  index = highWaterMark;
              }
                  
              activityContainer = locateActivityContainer( workflow, activityTargetKey );
              activityContainer.addActivity( index, activity ) ;
                        
            }
            finally {
                  if( TRACE_ENABLED ) trace( "DesignActionImpl.insertActivity() exit" ) ;
            }
                    
        } // end of insertActivity()       




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


        
        private void checkPermissions ( String someResource, String anAction ) throws WorkflowException {
            if( TRACE_ENABLED ) trace( "DesignActionImpl.checkPermission() entry" ) ;
/*                        
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
            
*/
             
        } // end of checkPermission()
  		

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