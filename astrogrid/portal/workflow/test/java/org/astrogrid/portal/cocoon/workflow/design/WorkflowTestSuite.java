package org.astrogrid.portal.cocoon.workflow.design ;

import org.apache.log4j.Logger; 

import org.astrogrid.portal.cocoon.common.DummyRequest ;
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

import org.astrogrid.community.common.exception.CommunityIdentifierException;
import org.astrogrid.community.common.exception.CommunityServiceException;
import org.astrogrid.community.common.exception.CommunityPolicyException;

import java.util.Map;
import java.util.HashMap;
import java.util.Enumeration;
import java.net.MalformedURLException;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.environment.commandline.CommandLineSession;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.environment.ObjectModelHelper;


import org.astrogrid.i18n.*;
import org.astrogrid.AstroGridException ;
import java.util.Iterator;
import junit.framework.Test; 
import junit.framework.TestCase;
import junit.framework.TestSuite;
//import org.apache.axis.utils.XMLUtils;
//import org.astrogrid.portal.workflow.*;
//import org.astrogrid.community.common.util.CommunityMessage;
import java.util.Date ;
//import org.astrogrid.portal.workflow.design.activity.*;
//import org.astrogrid.portal.workflow.design.Step ;




import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Vector;

import junit.framework.TestCase;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.InputSource;





 


public class WorkflowTestSuite extends TestCase {
	
	private static Logger 
		logger = Logger.getLogger( WorkflowTestSuite.class ) ;
		
	private static final String
        log4jproperties = "/home/jl99/log4j.properties" ;	
        
    private static final String
         ACCOUNT = "pjn3@test.astrogrid.org" ,    // account
         GROUP = "pjn3@test.astrogrid.org" ;      // group
     
    private static DummyRequest request = new DummyRequest();
    private static Session session = request.getSession() ;   
    
    private Workflow workflow = null; 

    private final Date
        runDate = new Date() ;
        
	/**
	 * Sets up the test fixture.
	 *
	 * Called before every test case method.
	 */
	protected void setUp() {
	}


	/**
	 * Tears down the test fixture.
	 *
	 * Called after every test case method.
	 */
	protected void tearDown() {	
	}


    public void test_AA_CreateWorkflow() {
        logger.info( "---------------------------------------------------------" ); 
        logger.info( "enter: WorkflowTestSuite.testCreateWorkflow()" ); 
        final DesignAction action = new DesignAction();
        final Map objectModel = new HashMap(); 
//      final DummyRequest request = new DummyRequest();
//      final Session session = request.getSession() ;
        
        try {
            request.addParameter( DesignAction.WORKFLOW_NAME_PARAMETER,"JL_Workflow" );
            request.addParameter( DesignAction.WORKFLOW_DESCRIPTION_PARAMETER,"JL_WorkflowDescription" ) ;
            request.addParameter( DesignAction.ACTION_PARAM_TAG, DesignAction.ACTION_CREATE_WORKFLOW );     
        
            session.setAttribute( DesignAction.USER_TAG, "jl99" );
            session.setAttribute( DesignAction.COMMUNITY_NAME_TAG,"leicester" );
            session.setAttribute( DesignAction.CREDENTIAL_TAG, "star" );
    
            objectModel.put(ObjectModelHelper.REQUEST_OBJECT, request);
            final Map results = action.act(null, null, objectModel, null, null);
            workflow = (Workflow)session.getAttribute( DesignAction.HTTP_WORKFLOW_TAG ) ;
            logger.info( workflow.getName() ); 
            
            File f1 = new File("/tmp/" + workflow.getName() + ".xml");
            FileWriter writer = new FileWriter(f1);
            Marshaller.marshal(workflow, writer);
                       
            assertNotNull(results);
        }
        catch( Exception ex ) {
            ex.printStackTrace();
        }
        finally {
            logger.info( "exit: WorkflowTestSuite.testCreateWorkflow()" ); 
        }
             
    }


    
    public void test_AB_SaveWorkflow() {
        logger.info( "---------------------------------------------------------" ); 
        logger.info( "enter: WorkflowTestSuite.testSaveWorkflow()" ); 
        final DesignAction action = new DesignAction();
        final Map objectModel = new HashMap(); 
//        final DummyRequest request = new DummyRequest();
//        final Session session = request.getSession() ;
        
        try {
//            request.addParameter( DesignAction.WORKFLOW_NAME_PARAMETER,"JL_Workflow" );
//            request.addParameter( DesignAction.WORKFLOW_DESCRIPTION_PARAMETER,"JL_WorkflowDescription" ) ;
            request.addParameter( DesignAction.ACTION_PARAM_TAG, DesignAction.ACTION_SAVE_WORKFLOW );     
        
            session.setAttribute( DesignAction.USER_TAG, "jl99" );
            session.setAttribute( DesignAction.COMMUNITY_NAME_TAG,"leicester" );
            session.setAttribute( DesignAction.CREDENTIAL_TAG, "star" );
 
            objectModel.put(ObjectModelHelper.REQUEST_OBJECT, request);
            final Map results = action.act(null, null, objectModel, null, null);
            assertNotNull(results);
        }
        catch( Exception ex ) {
            ex.printStackTrace();
        }
        finally {
            logger.info( "exit: WorkflowTestSuite.testSaveWorkflow()" ); 
        }
             
    }

	    public void test_AM_ReadToolsList() {
	        logger.info( "---------------------------------------------------------" ); 
	        logger.info( "enter: WorkflowTestSuite.test_AM_ReadToolsList()" ); 
	        final DesignAction action = new DesignAction();
	        final Map objectModel = new HashMap(); 
	//        final DummyRequest request = new DummyRequest();
	//        final Session session = request.getSession() ;
	        
	        try {
	//            request.addParameter( DesignAction.WORKFLOW_NAME_PARAMETER,"JL_Workflow" );
	//            request.addParameter( DesignAction.WORKFLOW_DESCRIPTION_PARAMETER,"JL_WorkflowDescription" ) ;
	            request.addParameter( DesignAction.ACTION_PARAM_TAG, DesignAction.ACTION_READ_LISTS );     
	        
	            session.setAttribute( DesignAction.USER_TAG, "jl99" );
	            session.setAttribute( DesignAction.COMMUNITY_NAME_TAG,"leicester" );
	            session.setAttribute( DesignAction.CREDENTIAL_TAG, "star" );
	 
	            objectModel.put(ObjectModelHelper.REQUEST_OBJECT, request);
	            final Map results = action.act(null, null, objectModel, null, null);
	            assertNotNull(results);
                logger.info( "tools list: " );
                String[] tools = (String[])request.getAttribute( DesignAction.TOOL_LIST_PARAMETER );
                if(tools != null){
                    for( int i=0; i<tools.length; i++ ){
                        logger.info( tools[i]);
                    }
                }
                else {
                    logger.info( "tools list is null" );
                }
	        }
	        catch( Exception ex ) {
	            ex.printStackTrace();
	        }
	        finally {
	            logger.info( "exit: WorkflowTestSuite.test_AM_ReadToolsList()" ); 
	        }
	             
	    }
    
    



    /**
     * Helper method which sets up HyperZ specific job step details.
     */ 
//    private Step setUpHyperZStep( Step targetStep
//                                , String config_file
//                                , String input_catalog
//                                , String output_catalog ) {
//        logger.info( "enter: WorkflowTestSuite.setUpHyperZStep()" );
//       
//        ListIterator iterator ;
//        
//        try {
//            
//            Tool tool = Workflow.createTool( communitySnippet(), "HyperZ" ) ;
//            Parameter p ;
//            String pName ;
//            
//            iterator = tool.getInputParameters() ;            
//            while( iterator.hasNext() ) {
//                
//                p = (Parameter)iterator.next() ;
//                pName = p.getName() ;
//                
//                if( pName.equals("config_file") ) {
//                    p.setValue( config_file ) ;
//                }
//                else if( pName.equals( "input_catalog") ) {
//                    p.setValue( input_catalog ) ;
//                }
//
//            }
//            
//            iterator = tool.getOutputParameters() ;            
//            while( iterator.hasNext() ) {
//                p = (Parameter)iterator.next() ;
//                if( p.getName().equals("output_catalog") )
//                   p.setValue( output_catalog ) ;
//                   break ;
//            }  
//            
//            targetStep.setTool( tool ) ;                                        
//                                                                
//        }
//        finally {
//            logger.info( "exit: WorkflowTestSuite.setUpHyperZStep()" );  
//        }
//        
//        return targetStep ;
//        
//    } // end of setUpHyperZStep()
    
    
    /**
     * Helper method which sets up SExtractor specific job step details.
     */ 
//    private Step setUpSExtractorStep( Step targetStep
//                                    , String detectionImage
//                                    , String config_file
//                                    , String parameters_name
//                                    , String catalog_name ) {
//        logger.info( "enter: WorkflowTestSuite.setUpSExtractorStep()" );
//       
//        ListIterator iterator ;
//        
//        try {
//            
//            Tool tool = Workflow.createTool( communitySnippet(), "SExtractor" ) ;
//            Parameter p ;
//            String pName ;
//            
//            iterator = tool.getInputParameters() ;            
//            while( iterator.hasNext() ) {
//                
//                p = (Parameter)iterator.next() ;
//                pName = p.getName() ;
//                
//                if( pName.equals("DetectionImage") ) {
//                    p.setValue( detectionImage ) ;
//                }
//                else if( pName.equals( "config_file") ) {
//                    p.setValue( config_file ) ;
//                }
//                else if( pName.equals( "PARAMETERS_NAME" ) ) {
//                    p.setValue( parameters_name ) ;
//                }
//
//            }
//            
//            iterator = tool.getOutputParameters() ;            
//            while( iterator.hasNext() ) {
//                p = (Parameter)iterator.next() ;
//                if( p.getName().equals("CATALOG_NAME") )
//                   p.setValue( catalog_name ) ;
//                   break ;
//            }  
//            
//            targetStep.setTool( tool ) ;                                        
//                                                                
//        }
//        finally {
//            logger.info( "exit: WorkflowTestSuite.setUpSExtractorStep()" );  
//        }
//        
//        return targetStep ;
//        
//    } // end of setUpSExtractorStep()
    
    
    /**
     * Helper method which sets up DataFederation specific job step details.
     * Note: this shows how to setup added optional input parameters.
     * 
     */ 
//    private Step setUpDataFederationStep( Step targetStep
//                                        , String [] votables
//                                        , boolean mergeCols
//                                        , String show
//                                        , String target
//                                        , String what2show
//                                        , String require
//                                        , String output
//                                        , int maxent
//                                        , String merged_output ) {
//        logger.info( "enter: WorkflowTestSuite.setUpDataFederationStep()" );
//       
//        ListIterator iterator ;
//        
//        try {
//            
//            Tool tool = Workflow.createTool( communitySnippet(), "DataFederation" ) ;
//            Parameter p ;
//            int votablesIndex = 0 ;
//            String pName ;
//            
//            iterator = tool.getInputParameters() ;            
//            while( iterator.hasNext() ) {
//                
//                p = (Parameter)iterator.next() ;
//                pName = p.getName() ;
//                
//                // There can be a variable number of VOTfiles.
//                // The initial setup assumes there are only 2
//                // ie: the minimum number...
//                if( pName.equals("VOTfiles") ) {
//                    p.setValue( votables[votablesIndex] ) ;
//                    votablesIndex++ ;
//                }
//                else if( pName.equals( "mergeCols") ) {
//                    p.setValue( new Boolean( mergeCols ).toString() ) ;
//                }
//                else if( pName.equals( "show" ) ) {
//                    p.setValue( show ) ;
//                }
//                else if( pName.equals( "target" ) ) {
//                    p.setValue( "target" ) ;
//                }
//                else if( pName.equals( "what2show") ) {
//                    p.setValue( what2show ) ;
//                }
//                else if( pName.equals( "require" ) ) {
//                    p.setValue( require ) ;
//                }
//                else if( pName.equals( "output" ) ) {
//                    p.setValue( "output" ) ;
//                }
//                else if( pName.equals( "maxent" ) ) {
//                    p.setValue( new Integer( maxent ).toString() ) ;
//                }
//
//            }
//            
//            iterator = null ; 
//            
//            // Here are the additional optional input parameters.
//            // This sets up the VOTfiles beyond the minimum 2...
//            for( votablesIndex=2; votablesIndex < votables.length; votablesIndex++ ) {
//                p = tool.newInputParameter( "VOTfiles" ) ;
//                p.setValue( votables[votablesIndex] ) ;
//            }
//            
//            iterator = tool.getOutputParameters() ;            
//            while( iterator.hasNext() ) {
//                p = (Parameter)iterator.next() ;
//                if( p.getName().equals("merged_output") )
//                   p.setValue( merged_output ) ;
//                   break ;
//            }  
//            
//            targetStep.setTool( tool ) ;                                        
//                                                                
//        }
//        finally {
//            logger.info( "exit: WorkflowTestSuite.setUpDataFederationStep()" );  
//        }
//        
//        return targetStep ;
//        
//    } // end of setUpDataFederationStep()
    
    
	/** 
	 * Assembles and returns a test suite for
	 * all the test methods of this test case.
	 *
	 * @return A non-null test suite.
	 */
	public static Test suite() {
		// Reflection is used here to add all the testXXX() methods to the suite.
		return new TestSuite( WorkflowTestSuite.class ) ;
	}


	/**
     * Runs the test case.
     */
    public static void main( String args[] )  {

//		PropertyConfigurator.configure( log4jproperties ) ;
			
	   logger.info("Entering WorkflowTestSuite application.");
       junit.textui.TestRunner.run( suite() ) ;
	   logger.info("Exit WorkflowTestSuite application.");
		
    }
    
} // end of class WorkflowTestSuite