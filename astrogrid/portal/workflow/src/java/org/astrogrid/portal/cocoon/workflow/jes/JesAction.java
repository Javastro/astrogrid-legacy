/*
 * @(#)JesAction.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.3, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.portal.cocoon.workflow.jes;

import org.apache.log4j.Logger;

//import org.astrogrid.portal.workflow.*; 
import org.astrogrid.portal.workflow.intf.*;
import org.astrogrid.community.beans.v1.Credentials;
import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.community.beans.v1.Group;
import org.astrogrid.jes.delegate.JobSummary;
import org.astrogrid.workflow.beans.v1.Workflow;

import org.astrogrid.config.Config;
import org.astrogrid.config.SimpleConfig;

import org.astrogrid.community.client.policy.service.PolicyServiceDelegate;
import org.astrogrid.community.client.policy.service.PolicyServiceMockDelegate;
import org.astrogrid.community.client.policy.service.PolicyServiceSoapDelegate;

import org.astrogrid.community.common.policy.data.PolicyPermission  ;
import org.astrogrid.community.common.policy.data.PolicyCredentials ;

import org.astrogrid.community.common.exception.CommunityIdentifierException;
import org.astrogrid.community.common.exception.CommunityServiceException;
import org.astrogrid.community.common.exception.CommunityPolicyException;

import java.util.Map;
import java.util.HashMap;
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
public class JesAction extends AbstractAction {
    
    /** Compile-time switch used to turn tracing on/off. 
     * Set this to false to eliminate all trace statements within the byte code.
     */         
    private static final boolean TRACE_ENABLED = true;
        
    /** Compile-time switch used to turn certain debugging statements on/off. 
     * Set this to false to eliminate these debugging statements
     * within the byte code.
     */         
    private static final boolean DEBUG_ENABLED = true;
        
    private static Logger logger = Logger.getLogger( JesAction.class ); 
        
    /**
     * Name of JNDI property holding security delegate endpoint URL
     */
    public static final String ORG_ASTROGRID_PORTAL_COMMUNITY_URL = "org.astrogrid.portal.community.url";        
        
        
    private static final String
        ASTROGRIDERROR_JOB_PERMISSION_DENIED = "AGWKFE00060",
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
	public static final String COMMUNITY_PARAM_TAG = "community";

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
    
    public static final String HTTP_JOBLIST_TAG = "job-list-tag" ,
                               COMMUNITY_ACCOUNT_TAG = "user" ,
                               COMMUNITY_NAME_TAG = "community_name" ,
                               CREDENTIAL_TAG = "credential" ,
                               COMMUNITY_TOKEN_TAG = "community-token",
	                           USER_TAG = "user";
    
    public static final String ERROR_MESSAGE_PARAMETER = "ErrorMessage";
    
    public static final String ACTION_READ_JOB_LIST = "read-job-list";
        
    public static final String AUTHORIZATION_RESOURCE_JOB = "job" ,
                               AUTHORIZATION_ACTION_EDIT = "edit";

    public static final String USERID_COMMUNITY_SEPARATOR = "@";     
        

    /**
    * Our action method.
    *
    */
    public Map act ( Redirector redirector,
                     SourceResolver resolver,
                     Map objectModel,
                     String source,
                     Parameters params ) {                  
        if( TRACE_ENABLED ) trace( "JesAction.act() entry" );  
        
        JesActionImpl myAction = null;
        Map retMap = null;
        
        try { 
            
            myAction = new JesActionImpl( redirector,
                                          resolver,
                                          objectModel,
                                          source,
                                          params );
                                           
            retMap = myAction.act();    
                                          
        }
        catch ( Exception ex ) {
            ex.printStackTrace();
        }
        finally {
            if( TRACE_ENABLED ) trace( "JesAction.act() exit" );  
        }
                                        
        return retMap; 
 
    } // end of act() 


    private class JesActionImpl {
        
        private Redirector redirector;
        private SourceResolver resolver;
        private Map objectModel,
                    results;
        private String source;
        private Parameters params;
        private Request request;
        private Session session;
        private WorkflowManager workflowManager;
        private String userid,
                       community,
                       group,
                       token;
        private String action;
        private boolean bConfirm;
		private String template;  
        private Credentials credentials;            
        
        public JesActionImpl( Redirector redirector,
                              SourceResolver resolver,
                              Map objectModel,
                              String source,
                              Parameters params ) {
            if( TRACE_ENABLED ) trace( "JesActionImpl() entry" ); 
            
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
                
                WorkflowManagerFactory wmFactory = new WorkflowManagerFactory();
                this.workflowManager = wmFactory.getManager() ;
            
                // Get user and community 
                this.retrieveUserDetails();
            
                this.action = request.getParameter( ACTION_PARAM_TAG );
                this.bConfirm = new Boolean (
                   request.getParameter(CONFIRM_PARAM_TAG) ).booleanValue();
                          
            }
            catch( WorkflowInterfaceException wix ) {
                wix.printStackTrace();
            }
            finally {
                if( TRACE_ENABLED ) trace( "JesActionImpl() exit" ); 
            }
              
        } // end of JesActionImpl()
        
        
        public Map act() {
            if( TRACE_ENABLED ) trace( "JesActionImpl.act() entry" );      
        
            try {
            	
				debug( "action is: " + action );
                
                this.consistencyCheck();
                    
                if( action == null ){
                    debug( "action is null");  
                }  
                    
                if( action.equals( ACTION_READ_JOB_LIST ) ) {
                    this.readJobList(); 
                }
                else {
                    debug( "unsupported action"); 
                }
                
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
                if( TRACE_ENABLED ) trace( "JesActionImpl.act() exit" );  
            }
    
            return results;
            
        } // end of JesActionImpl()
        
        
        private void retrieveUserDetails() {
            if( TRACE_ENABLED )
               trace( "JesActionImpl.retrieveUserDetails() entry" );   

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

            }
            finally {
                if( TRACE_ENABLED )
                   trace( "JesActionImpl.retrieveUserDetails() exit" );  
            }
                
        } // end of retrieveUserDetails()
        
       
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
       
        
        private void consistencyCheck() throws ConsistencyException {
			if( TRACE_ENABLED ) trace( "consistencyCheck() entry" );
			debug( "userid: " + userid );
			debug( "community: " + community );
			debug( "name: "  ); 
			debug( "description: "  ); 
                       
            if( userid == null ) {
               ; // redirection required 
                throw new ConsistencyException();
            }
            else if( action == null ) {
                
                debug( "action is null" );
                // throw new ConsistencyException();
     
            }
			if( TRACE_ENABLED ) trace( "consistencyCheck()) exit" );
        }
        
        
        private void readJobList() {
            if( TRACE_ENABLED ) trace( "JesActionImpl.readJobList() entry" );
              
            try {
                // For the moment this is where we have placed the door.
                // If users cannot see a list, then they cannot do anything...
//                this.checkPermissions( AUTHORIZATION_RESOURCE_JOB
//                                     , AUTHORIZATION_ACTION_EDIT );
                
                JobExecutionService jes = workflowManager.getJobExecutionService();
                JobSummary[] jobSummaries = jes.readJobList( credentials.getAccount() ) ;
                Workflow[] workflows = new Workflow[ jobSummaries.length ]; 
                WorkflowStore wfStore = this.workflowManager.getWorkflowStore();
                
                for( int i=0; i<workflows.length; i++ ) {
                     workflows[i] = wfStore.readWorkflow( credentials.getAccount(), jobSummaries[i].getName() ) ;
                }
    
                this.request.setAttribute( HTTP_JOBLIST_TAG, workflows );
            }
            catch( WorkflowInterfaceException wix ) {
                 this.request.setAttribute( ERROR_MESSAGE_PARAMETER, wix.toString() ) ;
            }
            catch( Exception ex ) {
                
                this.request.setAttribute(  ERROR_MESSAGE_PARAMETER, "permission denied" );
                
            }
            finally {
                if( TRACE_ENABLED )
                   trace( "JesActionImpl.readJobList() exit" );
            }
                    
        } // end of readJobList()   
           
  
        private void checkPermissions ( String someResource, String anAction ) 
                                 throws CommunityServiceException, 
                                        CommunityPolicyException, 
                                        CommunityIdentifierException {
            if( TRACE_ENABLED ) trace( "JesActionImpl.checkPermission() entry" ) ;
                       
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
                if( TRACE_ENABLED ) trace( "JesActionImpl.checkPermission() exit" ) ;  
            }
                        
        } // end of checkPermission()
        
   
    } // end of inner class JesActionImpl
        
    
    private class ConsistencyException extends Exception {
    }
    
    
    private void trace( String traceString ) {
        // logger.debug( traceString );
        System.out.println( traceString );
    }
    
    private void debug( String logString ){
        // logger.debug( logString );
        System.out.println( logString );
    }
             
            
} // end of class JesAction 
