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
package org.astrogrid.portal.cocoon.workflow.jes ;

import org.apache.log4j.Logger ;

import org.astrogrid.i18n.*;

// import org.astrogrid.workflow.*;
// import org.astrogrid.workflow.design.activity.*;

import org.astrogrid.AstroGridException ;

import org.astrogrid.portal.workflow.*;
import org.astrogrid.portal.workflow.jes.*;

import org.astrogrid.community.delegate.policy.PolicyServiceDelegate;
import org.astrogrid.community.policy.data.PolicyPermission ;

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
public class JesAction extends AbstractAction {
    
    /** Compile-time switch used to turn tracing on/off. 
      * Set this to false to eliminate all trace statements within the byte code.*/         
    private static final boolean 
        TRACE_ENABLED = true ;
        
    /** Compile-time switch used to turn certain debugging statements on/off. 
      * Set this to false to eliminate these debugging statements within the byte code.*/         
    private static final boolean 
        DEBUG_ENABLED = true ;
        
    private static Logger 
        logger = Logger.getLogger( JesAction.class ) ; 
        
    private static final String
        ASTROGRIDERROR_JOB_PERMISSION_DENIED = "AGWKFE00060",
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
	public static final String CONFIRM_PARAM_TAG = "confirm" ;
    
    public static final String
        HTTP_JOBLIST_TAG = "job-list-tag" ,
        COMMUNITY_ACCOUNT_TAG = "community_account" ,
        CREDENTIAL_TAG = "credential" ,
        COMMUNITY_TOKEN_TAG = "community-token" ;
    
    public static final String
        ERROR_MESSAGE_PARAMETER = "ErrorMessage";
    
    public static final String 
        ACTION_READ_JOB_LIST = "read-job-list" ;
        
    public static final String
        AUTHORIZATION_RESOURCE_JOB = "job" ,
        AUTHORIZATION_ACTION_EDIT = "edit" ;

    public static final String 
        USERID_COMMUNITY_SEPARATOR = "@" ;     
        

    /**
    * Our action method.
    *
    */
    public Map act ( Redirector redirector
                   , SourceResolver resolver
                   , Map objectModel
                   , String source
                   , Parameters params ) {                  
        if( TRACE_ENABLED ) trace( "JesAction.act() entry" ) ;  
        
        JesActionImpl
            myAction = null ;
        Map
            retMap = null ;
        
        try { 
            debug( "About to check properties loaded") ;
            // Load the workflow config file and messages...
            WKF.getInstance().checkPropertiesLoaded() ;
            debug( "Properties loaded OK") ;
            
            myAction = new JesActionImpl( redirector
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
            if( TRACE_ENABLED ) trace( "JesAction.act() exit" ) ;  
        }
                                        
        return retMap ; 
 
    } // end of act() 


    private class JesActionImpl {
        
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
		private String
			template ;            
        
        public JesActionImpl( Redirector redirector
                            , SourceResolver resolver
                            , Map objectModel
                            , String source
                            , Parameters params ) {                                   
            if( TRACE_ENABLED ) trace( "JesActionImpl() entry" ) ; 
            
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
                          
            }
            finally {
                if( TRACE_ENABLED ) trace( "JesActionImpl() exit" ) ; 
            }
              
        } // end of JesActionImpl()
        
        
        public Map act() {
            if( TRACE_ENABLED ) trace( "JesActionImpl.act() entry" ) ;      
        
            try {
            	
				debug( "action is: " + action ) ;
                
                this.consistencyCheck() ;
                    
                if( action == null ){
                    debug( "action is null") ;  
                }  
                    
                if( action.equals( ACTION_READ_JOB_LIST ) ) {
                    this.readJobList() ; 
                }
                else {
                    debug( "unsupported action") ; 
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
                if( TRACE_ENABLED ) trace( "JesActionImpl.act() exit" ) ;  
            }
    
            return results ;
            
        } // end of JesActionImpl()
        
        
        private void retrieveUserDetails() {
            if( TRACE_ENABLED ) trace( "JesActionImpl.retrieveUserDetails() entry" ) ;   
                     
            String 
                tag  = null ,
                useridCommunity = null ;
         int
             ampersandIndex ;

            try {
                
                // JL Note: Iteration 3 way of doing things...
                useridCommunity = (String)session.getAttribute( COMMUNITY_ACCOUNT_TAG ) ;
                ampersandIndex = useridCommunity.indexOf( USERID_COMMUNITY_SEPARATOR ) ;
                this.userid = useridCommunity.substring(  0, ampersandIndex ) ;
                this.community = useridCommunity.substring( ampersandIndex + 1 );   
                
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
                if( TRACE_ENABLED ) trace( "JesActionImpl.retrieveUserDetails() exit" ) ;  
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
        
        
        private void readJobList() {
            if( TRACE_ENABLED ) trace( "JesActionImpl.readJobList() entry" ) ;
              
            try {
                // For the moment this is where we have placed the door.
                // If users cannot see a list, then they cannot do anything...
                this.checkPermissions( AUTHORIZATION_RESOURCE_JOB
                                     , AUTHORIZATION_ACTION_EDIT ) ;
                
                
                //NB: The filter argument is ignored at present (Sept 2003).
                Iterator
                    iterator =  Job.readJobList( userid, community, "*" ) ;
                this.request.setAttribute( HTTP_JOBLIST_TAG, iterator ) ;               
            }
            catch( WorkflowException wfex ) {
                
                this.request.setAttribute( ERROR_MESSAGE_PARAMETER, wfex.toString() ) ;
                
            }
            finally {
                if( TRACE_ENABLED ) trace( "JesActionImpl.readJobList() exit" ) ;
            }
                    
        } // end of readJobList()   
           
  
        private void checkPermissions ( String someResource, String anAction ) throws WorkflowException {
            if( TRACE_ENABLED ) trace( "JesActionImpl.checkPermission() entry" ) ;
                        
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
                    message = new AstroGridMessage( ASTROGRIDERROR_JOB_PERMISSION_DENIED
                                                  , WKF.getClassName( this.getClass() )
                                                  , wfex.getAstroGridMessage().toString() ) ;
                     
                throw new WorkflowException( message, (Exception)wfex ) ;
                
            }
            catch( Exception ex ) {
                
                if( DEBUG_ENABLED) ex.printStackTrace();  
               
                String
                    localizedMessage = ex.getLocalizedMessage() ;    
               
               AstroGridMessage
                   message = new AstroGridMessage( ASTROGRIDERROR_JOB_PERMISSION_DENIED
                                                 , WKF.getClassName( this.getClass() )
                                                 , (localizedMessage == null) ? "" : localizedMessage ) ;
                     
               throw new WorkflowException( message, ex ) ;
               
            }
            finally {
                if( TRACE_ENABLED ) trace( "JesActionImpl.checkPermission() exit" ) ;  
            }
             
        } // end of checkPermission() 
   
   
    } // end of inner class JesActionImpl
        
    
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
             
            
} // end of class JesAction 