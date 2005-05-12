/*
 * @(#)ParameterizedWfAction.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.3, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.portal.cocoon.workflow.parameterizedWf;

import org.apache.log4j.Logger;

import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.community.beans.v1.Credentials;
import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.community.beans.v1.Group;
import org.astrogrid.portal.cocoon.workflow.WorkflowTemplate;
import org.astrogrid.portal.workflow.intf.*;
import org.astrogrid.workflow.beans.v1.execution.JobURN;
import org.astrogrid.workflow.beans.v1.execution.WorkflowSummaryType;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;

import org.astrogrid.config.Config;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.community.User;
import org.astrogrid.store.Ivorn;

import org.astrogrid.filemanager.client.FileManagerClient;
import org.astrogrid.filemanager.client.FileManagerNode;

import org.astrogrid.portal.myspace.filesystem.Tree;
import org.astrogrid.portal.myspace.filesystem.Directory ;
import org.astrogrid.portal.common.session.AstrogridSessionFactory ;
import org.astrogrid.portal.common.session.AstrogridSession ;
import org.astrogrid.portal.common.session.AttributeKey ;

import org.astrogrid.community.common.exception.CommunityIdentifierException;
import org.astrogrid.community.common.ivorn.CommunityIvornParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

/**
 * A Cocoon action to handle parameterized workflows.
 *
 */
public class ParameterizedWfAction extends AbstractAction {
    
    /** Compile-time switch used to turn tracing on/off. 
     * Set this to false to eliminate all trace statements within the byte code.
     */         
    private static final boolean TRACE_ENABLED = false;
        
    /** Compile-time switch used to turn certain debugging statements on/off. 
     * Set this to false to eliminate these debugging statements
     * within the byte code.
     */         
    private static final boolean DEBUG_ENABLED = false;
        
    private static Logger logger = Logger.getLogger( ParameterizedWfAction.class ); 
        
	/**
	 * Http request param for the action.
	 *
	 */
	public static final String ACTION_PARAM_TAG = "action";
	/**
	 * Location of xml file containing list of parameterized workflows
	 *
	 */	
	public static final String DEFAULT_INDEX_URL = "http://wiki.astrogrid.org/pub/Astrogrid/ParameterizedWorkflows/index.xml";
	
	public static final String HTTP_PARAMWF_LIST_TAG = "paramwf-list-tag";
	public static final String HTTP_WORKFLOW_TEMPLATE_TAG = "paramwf-template-tag";
	public static final String HTTP_PARAMWF_TEMPLATE_NAME = "paramWf_name";
	public static final String HTTP_PARAMWF_SUBMIT_MESSAGE = "paramWf_submit_message";
	public static final String HTTP_PARAMWF_ERROR_MESSAGE = "paramWf_error_message";
	public static final String HTTP_PARAMWF_EXECUTE_AND_SAVE = "executeAndSave";
	public static final String SAVE_PARAMWF_IVORN_PARAMETER = "paramWf_save_ivorn_tag";	
	
	public static final String ACTION_READ_PARAMETERIZED_WF_LIST = "read-paramWf-list";
	public static final String ACTION_CREATE_PARAMETERIZED_WF = "create_paramWf";
	public static final String ACTION_EXECUTE_PARAMETERIZED_WF = "execute_paramWf";		
		    
    public static final String COMMUNITY_ACCOUNT_TAG = "user" ,
                               COMMUNITY_NAME_TAG = "community_name" ,
                               CREDENTIAL_TAG = "credential" ,
                               COMMUNITY_TOKEN_TAG = "community-token",
	                           USER_TAG = "user";
      

    /**
    * Our action method.
    *
    */
    public Map act ( Redirector redirector,
                     SourceResolver resolver,
                     Map objectModel,
                     String source,
                     Parameters params ) {                  
        if( TRACE_ENABLED ) trace( "ParameterizedWfAction.act() entry" );  
        
        ParameterizedWfActionImpl myAction = null;
        Map retMap = null;
        
        try { 
            
            myAction = new ParameterizedWfActionImpl( redirector,
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
            if( TRACE_ENABLED ) trace( "ParameterizedWfAction.act() exit" );  
        }
                                        
        return retMap; 
 
    } // end of act() 


    private class ParameterizedWfActionImpl {
        
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
		private String template;  
        private Credentials credentials;   
        private User user;  
        private WorkflowTemplate[] templates;
        
        public ParameterizedWfActionImpl( Redirector redirector,
                                          SourceResolver resolver,
                                          Map objectModel,
                                          String source,
                                          Parameters params ) {
            if( TRACE_ENABLED ) trace( "ParameterizedWfActionImpl() entry" ); 
            
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
                          
            }
            catch( WorkflowInterfaceException wix ) {
                wix.printStackTrace();
            }
            finally {
                if( TRACE_ENABLED ) trace( "ParameterizedWfActionImpl() exit" ); 
            }
              
        } // end of ParameterizedWfActionImpl()
        
        
        public Map act() {
            if( TRACE_ENABLED ) trace( "ParameterizedWfActionImpl.act() entry" );      
        
            try {
            	
            	if( DEBUG_ENABLED ) debug( "action is: " + action );
                    
                if( action == null ){
                    debug( "action is null");  
                }  
				else if( action.equals( ACTION_READ_PARAMETERIZED_WF_LIST ) ) {
					URL[] list = getWorkflowList(new URL(DEFAULT_INDEX_URL));
				    templates = loadWorkflows(list);
				    session.setAttribute( HTTP_PARAMWF_LIST_TAG, templates ) ;
				}
				else if( action.equals( ACTION_CREATE_PARAMETERIZED_WF ) ) {
					createWorkflowTemplate();
				}
				else if( action.equals( ACTION_EXECUTE_PARAMETERIZED_WF ) ) {
					executeWorkflowTemplate();
				}								
                else {
                    debug( "unsupported action"); 
                    throw new UnsupportedOperationException( action + " no longer supported");
                }
                
            }
            //PJN Note: these should only be here during testing...
            catch( Exception ex) {
                debug( "Exception: ex" );
                ex.printStackTrace();
            }
            //PJN Note: these should only be here during testing...
            catch( Throwable th ) {
                debug( "Throwable th" );
                th.printStackTrace();
            }
            finally {
                if( TRACE_ENABLED ) trace( "ParameterizedWfActionImpl.act() exit" );  
            }
    
            return results;
            
        } // end of ParameterizedWfActionImplImpl()
        

        /**
         * retrieve user details - set credentials
         * 
         * @return void
         */
        private void retrieveUserDetails() {
        	if( TRACE_ENABLED ) trace( "ParameterizedWfActionImpl.retrieveUserDetails() entry" );   
		                     
		    try {		                
		         String fullUserid = (String)session.getAttribute( USER_TAG );
		         this.userid = fullUserid.substring( fullUserid.lastIndexOf('/')+1 );
		         this.community = fullUserid.substring( fullUserid.indexOf('/')+2, fullUserid.lastIndexOf('/') );
		         this.group = this.community;
		             
		         if( DEBUG_ENABLED ) debug( "userid: " + this.userid );
		         if( DEBUG_ENABLED ) debug( "community: " + this.community );
		         if( DEBUG_ENABLED ) debug( "group: " + this.group ); 
		         if( DEBUG_ENABLED ) debug( "token: " + this.token ); 
		                            
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
		                
                 this.user = new User( this.userid, this.community, this.group, this.token );
              }
		      finally {
		          if( TRACE_ENABLED ) trace( "ParameterizedWfActionImpl.retrieveUserDetails() exit" );  
		      }
		  } // end of retrieveUserDetails()        
       
		
	    /** access a list of URLs from somewhere, each of which points to a workflow document 
	     * @throws IOException
	     * @throws SAXException
	     * @throws MalformedURLException
	     * */
	    public URL[] getWorkflowList(URL indexURL) throws IOException, SAXException {
	    	if( TRACE_ENABLED ) trace( "ParameterizedWfActionImpl.getWorkflowList() entry" );
	        InputStream is = indexURL.openStream();
	        indexDigester.clear();
	        List l = new ArrayList() {
	            public boolean add(Object o) {
	                try {
	                    return super.add(new URL(o.toString().trim()));
	                } catch (MalformedURLException e) {
	                    logger.error("Didn't recognize that one " + e);
	                    return false;
	                }
	            }
	        };
	        
	        indexDigester.push(l);
	        indexDigester.parse(is);
	        logger.debug(l);	        
	        return (URL[])l.toArray(new URL[]{});
	    }
	    
	    protected final Digester indexDigester = new Digester() {{
	        addCallMethod("*/workflow-template","add",1,new Class[]{String.class});
	        addCallParam("*/workflow-template",0);
	    }};		
 
	    /** construct a list of workflow documents from an array of urls , silently handle any errors.*/
	    protected WorkflowTemplate[] loadWorkflows(URL[] arr) {
	    	if( TRACE_ENABLED ) trace( "ParameterizedWfActionImpl.loadWorkflows() entry" );
	        List wfts= new ArrayList(arr.length); // using a list, rather than array, incase we end up with less than we expected..
	        if( TRACE_ENABLED ) trace("Number of parameterized workflow templates: " + arr.length);
	        for (int i = 0; i < arr.length; i++) {        	
	            try {
	                wfts.add(new WorkflowTemplate(arr[i].openStream()));	                
	            } catch (Exception e) {
	                logger.warn(arr[i] + " couldn't be parsed",e);
	            }
	        }
	        return (WorkflowTemplate[])wfts.toArray(new WorkflowTemplate[]{});
	    }

	    /**  
	     * createWorkflowTemplate: create WorkflowflowTemplate and put in session object
	     * @throws ConsistencyException
	     * */	    
	    private void createWorkflowTemplate() throws ConsistencyException {
	    	if( TRACE_ENABLED ) trace( "ParameterizedWfActionImpl.createWorkflowTemplate() entry" ) ;
	        try {
	        	String templateName = request.getParameter( HTTP_PARAMWF_TEMPLATE_NAME );
	        	WorkflowTemplate templates[] = (WorkflowTemplate[])session.getAttribute( HTTP_PARAMWF_LIST_TAG ) ;
	        	WorkflowTemplate wft = null;
	        	if( DEBUG_ENABLED ) debug( "templateName: " + templateName );
	        	
	        	for (int i =0 ; i <= templates.length; i++){
	        		if (templates[i].getDesc().getName().equalsIgnoreCase(templateName)){
	        			wft = templates[i];
	        			break;
	        		}	        			        	
	        	}
	        	if( TRACE_ENABLED ) trace("Created workflow template: " + wft.getDesc());
	        session.setAttribute( HTTP_WORKFLOW_TEMPLATE_TAG, wft ) ;
	        	
	        }
	        finally {
	           if( TRACE_ENABLED ) trace( "ParameterizedWfActionImpl.createWorkflowTemplate() exit" ) ;
	        }	    	
	    } // end of createWorkflowTemplate

	    /**  
	     * executeWorkflowTemplate: create WorkflowflowTemplate, set parameter values from request object,
	     * use this to create a workflow object which can then be submitted to JES
	     * @throws ConsistencyException
	     * */	    
	    private void executeWorkflowTemplate() throws ConsistencyException {
	    	if( TRACE_ENABLED ) trace( "ParameterizedWfActionImpl.executeWorkflowTemplate() entry" ) ;
	        try {
                WorkflowTemplate templates[] = (WorkflowTemplate[])session.getAttribute( HTTP_PARAMWF_LIST_TAG ) ;
                WorkflowTemplate wft = null;
                String templateName = request.getParameter( HTTP_PARAMWF_TEMPLATE_NAME );
                if( DEBUG_ENABLED ) debug( "templateName: " + templateName );

                for (int i =0 ; i <= templates.length; i++){
	                if (templates[i].getDesc().getName().equalsIgnoreCase(templateName)){
		                wft = templates[i];
		                break;
	                }	        			        	
                 }
	        	
	        	Tool tool = wft.getDesc().createToolFromDefaultInterface();
	        	
	            for (int i = 0; i < tool.getInput().getParameterCount(); i++) {
	            	if( DEBUG_ENABLED ) debug("Expecting input name: " + tool.getInput().getParameter(i).getName());
	            	String v = request.getParameter( "inputParam_" + tool.getInput().getParameter(i).getName() ).trim();
	            	if( DEBUG_ENABLED ) debug("value to assign to this parameter: " + v);	          	
	                tool.getInput().getParameter(i).setValue(v);
	                if( DEBUG_ENABLED ) debug("param name: "+tool.getInput().getParameter(i).getName()+" assigned value: "+tool.getInput().getParameter(i).getValue());
	            }
	            
	            for (int i = 0; i < tool.getOutput().getParameterCount(); i++) {
	            	if( DEBUG_ENABLED ) debug("Expecting output name: " + tool.getInput().getParameter(i).getName());
	                String v = request.getParameter( "outputParam_" + tool.getOutput().getParameter(i).getName() ).trim();
	                if( DEBUG_ENABLED ) debug("value to assign to this parameter: " + v);
	                tool.getOutput().getParameter(i).setValue(v);
	                if( DEBUG_ENABLED ) debug("param name: "+tool.getOutput().getParameter(i).getName()+" assigned value: "+tool.getOutput().getParameter(i).getValue());	                
	            }	            
	        	
	            Workflow wf = null;
	            
	            if ( tool.isValid() ) {
	            	if( DEBUG_ENABLED ) debug("Tool valid - Creating workflow");
	            	wf = wft.instantiate(credentials.getAccount(),tool);
	            }
				else {
					if( DEBUG_ENABLED ) debug("Tool invalid - not able to create workflow");
					session.setAttribute( HTTP_PARAMWF_ERROR_MESSAGE, "Unable to create workflow" ) ;
				}	            
	            
	            if (wf.isValid() ) {
	            	if( DEBUG_ENABLED ) debug("Workflow valid - Submitting workflow");
	            	String jobID = submit(wf).getContent();
	            	session.setAttribute( HTTP_PARAMWF_SUBMIT_MESSAGE, jobID ) ;
		        	//See if we need to save the workflow to myspace
	            	String executeAndSave = request.getParameter( HTTP_PARAMWF_EXECUTE_AND_SAVE );	            	
					if ((executeAndSave != null) && ( executeAndSave.equalsIgnoreCase("on") ) ) {
						if( DEBUG_ENABLED ) debug("Saving workflow to myspace");
		        		saveWorkflow(wf);
		        	}	            	
	            }
				else {
					if( DEBUG_ENABLED ) debug("Workflow invalid");
					session.setAttribute( HTTP_PARAMWF_ERROR_MESSAGE, "Unable to submit workflow" ) ;
				}	            	            	        	
	        }
            catch( WorkflowInterfaceException wix ) {
                wix.printStackTrace();
                session.setAttribute( HTTP_PARAMWF_ERROR_MESSAGE, "Unable to submit workflow: " + wix.getMessage() ) ;
            }	        
	        finally {
	           if( TRACE_ENABLED ) trace( "ParameterizedWfActionImpl.executeWorkflowTemplate() exit" ) ;
	        }	    	
	    } // end of executeWorkflowTemplate	
	    
	    /** submit a job to jes.
	     * @param wf
	     * @return JobURN
	     * @throws WorkflowInterfaceException
	     */
	    private JobURN submit(Workflow wf) throws WorkflowInterfaceException {
	    	if( TRACE_ENABLED ) trace( "ParameterizedWfActionImpl.submit() entry" );
            JobExecutionService jes = this.workflowManager.getJobExecutionService();
            JobURN job = jes.submitWorkflow( wf ) ;
            return job;
	    }

	    /** saveWorkflow: save workflow to myspace
	     * @param wf
	     * @throws ConsistencyException
	     */
        private void saveWorkflow(Workflow wf) throws ConsistencyException {
            if( TRACE_ENABLED ) trace( "ParameterizedWfActionImpl.saveWorkflow() entry" ) ;
            
            String ivornName = "ivo://" + request.getParameter( SAVE_PARAMWF_IVORN_PARAMETER ) ;
			debug( "ivornName: " + ivornName );
            Ivorn ivorn = null;
            AstrogridSession agSession = AstrogridSessionFactory.getSession(request.getSession(true));
            FileManagerClient fmc = null ;
                                       
            try {

                if( (wf == null) || (ivornName == null) ) {
                    ; // some logging here
                    throw new ConsistencyException() ; 
                }
                else {
                    ivorn = (new CommunityIvornParser(ivornName)).getIvorn();
                    debug( "user: " + user );
                    debug( "ivorn: " + ivorn );
                    debug( "workflow: " + wf );
                    WorkflowStore wfStore = this.workflowManager.getWorkflowStore();
                    debug( "wfStore: " + wfStore );
                    fmc = (FileManagerClient)agSession.getAttribute( AttributeKey.FILE_MANAGER_CLIENT ) ;
                    FileManagerNode fileNode = wfStore.saveWorkflow( fmc, ivorn, wf ) ;
                    
                    // We need to wrap the file node if it doesn't already exist...
                    // The following string manipulation code constructs a path
                    // relative to the account space. For instance, taking...
                    // org.astrogrid.localhost/frog#workflows/wf1
                    // and changes it to this...
                    // home/workflows/wf1
                    String path = request.getParameter( SAVE_PARAMWF_IVORN_PARAMETER ) ;
                    path = "home/" + path.substring( path.indexOf( '#' ) + 1 ) ;
                    Tree tree = (Tree)agSession.getAttribute( AttributeKey.MYSPACE_TREE ) ;
                    debug( "path: " + path ) ;
                    
                    Directory directory = null ;
                    // If it already existed in the tree, we simply ignore it...
                    if( tree.getFile( path ) == null ) {
                        // otherwise we wrap it...
                        path = path.substring( 0, path.lastIndexOf( '/' ) + 1) ;
                        directory = tree.getDirectory( path ) ;
                        if( directory != null ) {
                            tree.constructFile( fileNode, directory ) ;
                        }
                        else {
                            debug( "directory is null") ;
                        }
                    }
                    
                } 
                               
            }
            catch( CommunityIdentifierException cix ) {
                cix.printStackTrace();
            }         
            catch( WorkflowInterfaceException wix ) {            	
				session.setAttribute( HTTP_PARAMWF_SUBMIT_MESSAGE, "An error has occured whilst trying to save your workflow (possibly because your workflow failed to validate - e.g. are all values entered)" ) ;
                wix.printStackTrace();
               }
            finally {
                if( TRACE_ENABLED ) trace( "ParameterizedWfActionImpl.saveWorkflow() exit" ) ;
            }
         
        } // end of saveWorkflow()
        
		
    } // end of inner class Impl
        
    
    private class ConsistencyException extends Exception {
    }
    
    
    private void trace( String traceString ) {
        System.out.println( traceString );
    }
    
    private void debug( String logString ){
        System.out.println( logString );
    }
             
            
} // end of class
