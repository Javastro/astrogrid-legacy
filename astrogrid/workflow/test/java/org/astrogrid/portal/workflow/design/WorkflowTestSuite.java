package org.astrogrid.portal.workflow.design ;

import org.astrogrid.i18n.*;
import org.astrogrid.AstroGridException ;

import java.io.IOException;
import java.util.ListIterator;
import java.util.Iterator;
import javax.xml.parsers.ParserConfigurationException;

import junit.framework.Test; 
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.axis.utils.XMLUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
// import org.astrogrid.portal.workflow.design.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import org.astrogrid.portal.workflow.*;


 
public class WorkflowTestSuite extends TestCase {
	
	private static Logger 
		logger = Logger.getLogger( WorkflowTestSuite.class ) ;
		
	private static final String
        log4jproperties = "/home/jl99/log4j.properties" ;		
        
	/**
	 * Sets up the test fixture.
	 *
	 * Called before every test case method.
	 */
	protected void setUp() {
        try {
             logger.info( "About to check properties loaded") ;
             WKF.getInstance().checkPropertiesLoaded() ;
             logger.info( "Properties loaded OK") ;
         }
         catch( AstroGridException agex ) {
             logger.info( agex.getAstroGridMessage().toString() ) ;
         }
	}


	/**
	 * Tears down the test fixture.
	 *
	 * Called after every test case method.
	 */
	protected void tearDown() {	
	}
	
    
    
    public void testWorkflowActivityNavigation() {
         logger.info( "enter: WorkflowTestSuite.testWorkflowActivityNavigation()" ); 
        
         final String 
             userid = "jl99",
             community = "leicester",
             name = "OneStepJob",
             description = "This is a one step job",
             templateName = "OneStepJob" ;
         Workflow
            workflow = null ,
            workflowActivity = null ;
         Sequence
            sequenceActivity = null ;
         Step
            stepActivity = null ;
            
         try{
             workflow = Workflow.createWorkflowFromTemplate( userid, community, name, description, templateName ) ;
             
             if( (workflowActivity = (Workflow)workflow.getActivity( "0" )) == null ) {
                 logger.info( "workflowActivity is null" ) ;
             }
             if( (sequenceActivity = (Sequence)workflow.getActivity( "1" )) == null ) {
                 logger.info( "sequenceActivity is null" ) ;
             } 
             if( (stepActivity = (Step)workflow.getActivity( "2" )) == null ) {
                 logger.info( "stepActivity is null" ) ;
             }  
 
             logger.info( "Workflow: " + workflow.toXMLString() ) ;
             assertTrue( true ) ;    
         }
         catch( Exception ex ) {
             
             assertTrue( false ) ;
             ex.printStackTrace() ;
         }
         finally {
             logger.info( "exit: WorkflowTestSuite.testWorkflowActivityNavigation()" );  
         }
        
     } // end of testWorkflowActivityNavigation()
    
    

    public void testCreateWorkflowFromTemplate_MissingTemplate() {
        logger.info( "enter: WorkflowTestSuite.testCreateWorkflowFromTemplate_MissingTemplate()" ); 
        
        final String 
            userid = "jl99",
            community = "leicester",
            name = "OneStepJob",
            description = "This is a one step job",
            templateName = "IncorrectName" ;
            
        try{
            Workflow.createWorkflowFromTemplate( userid, community, name, description, templateName ) ;
//          logger.info( "testQueryToString_CONE: " + resultString ) ;
//            assertTrue( resultString.equals( sqlString ) ) ;    
        }
        catch( Exception ex ) {
            assertTrue( false ) ;
        }
        finally {
            logger.info( "exit: WorkflowTestSuite.testCreateWorkflowFromTemplate_MissingTemplate()" );  
        }
        
    } // end of testCreateWorkflowFromTemplate_MissingTemplate()


    public void testCreateWorkflowFromTemplate_OneStepTemplate() {
        logger.info( "enter: WorkflowTestSuite.testCreateWorkflowFromTemplate_OneStepTemplate()" ); 
        
        final String 
            userid = "jl99",
            community = "leicester",
            name = "OneStepJob",
            description = "This is a one step job",
            templateName = "OneStepJob" ;
        Workflow
            workflow = null ;
            
        try{
            workflow = Workflow.createWorkflowFromTemplate( userid, community, name, description, templateName ) ;
            logger.info( "Workflow: " + workflow.toXMLString() ) ;
            assertTrue( true ) ;    
        }
        catch( Exception ex ) {
            assertTrue( false ) ;
        }
        finally {
            logger.info( "exit: WorkflowTestSuite.testCreateWorkflowFromTemplate_OneStepTemplate()" );  
        }
        
    } // end of testCreateWorkflowFromTemplate_OneStepTemplate()


    public void testWorkflowReadList() {
         logger.info( "enter: WorkflowTestSuite.testWorkflowReadList()" ); 
        
         final String 
             userid = "pjn3",
             community = "leicester",
             name = "SomeOneStepJob", 
             communitySnippet = "dummySnippet";
         Iterator
            iterator ;
            
         try{
             iterator = Workflow.readWorkflowList( userid, community, communitySnippet, name) ;
             
             while ( iterator.hasNext() ) {
                 logger.info( "Workflow Name: " + (String)iterator.next() ) ;
             }
 
             assertTrue( true ) ;    
         }
         catch( Exception ex ) {
             
             assertTrue( false ) ;
             ex.printStackTrace() ;
         }
         finally {
             logger.info( "exit: WorkflowTestSuite.testWorkflowReadList()" );  
         }
        
     } // end of testWorkflowReadList()
    
    
    public void testReadQuery() {
         logger.info( "enter: WorkflowTestSuite.testReadQuery()" ); 
        
         final String 
             userid = "dfh",
             community = "moscow",
             name = "SomeDevilishQuery", 
             communitySnippet = "dummySnippet";
         Query
            query ;
            
         try{
             query = Query.readQuery( userid, community, communitySnippet, name) ;
            
             logger.info( "Query: " + query.toXMLString() ) ;
 
             assertTrue( true ) ;    
         }
         catch( Exception ex ) {
             
             assertTrue( false ) ;
             ex.printStackTrace() ;
         }
         finally {
             logger.info( "exit: WorkflowTestSuite.testReadQuery()" );  
         }
        
     } // end of testReadQuery()
    
    
    public void testSaveWorkflow() {
         logger.info( "enter: WorkflowTestSuite.testSaveWorkflow()" ); 
        
        final String 
            userid = "shd",
            community = "edinburgh",
            name = "JobToSave", 
            communitySnippet = "dummySnippet";
        Workflow
           workflow ;
            
        try{
            workflow = Workflow.readWorkflow( userid, community, communitySnippet, name) ;
            
            logger.info( "Workflow: " + workflow.toXMLString() ) ;
            logger.info( "About to save" ) ;
            
            Workflow.saveWorkflow( workflow ) ;
 
            assertTrue( true ) ;    
         }
         catch( Exception ex ) {
             
             assertTrue( false ) ;
             ex.printStackTrace() ;
         }
         finally {
             logger.info( "exit: WorkflowTestSuite.testSaveWorkflow()" );  
         }
        
     } // end of testSaveWorkflow()
    
    

    public void testDeleteWorkflow() {
         logger.info( "enter: WorkflowTestSuite.testDeleteWorkflow()" ); 
        
        final String 
            userid = "kjh",
            community = "berlin",
            name = "JobToDelete", 
            communitySnippet = "dummySnippet";
        boolean
            ret = false ;
            
        try{
            ret = Workflow.deleteWorkflow( userid, community, communitySnippet, name) ;
            
            logger.info( "deleted: " + ret ) ;
 
            assertTrue( true ) ;    
         }
         catch( Exception ex ) {
             
             assertTrue( false ) ;
             ex.printStackTrace() ;
         }
         finally {
             logger.info( "exit: WorkflowTestSuite.testDeleteWorkflow()" );  
         }
        
     } // end of testDeleteWorkflow()
    
    



    public void testReadQueryList() {
         logger.info( "enter: WorkflowTestSuite.testReadQueryList()" ); 
        
         final String 
             userid = "xvz8",
             community = "leicester",
             filter = "*", 
             communitySnippet = "dummySnippet";
         Iterator
            iterator ;
            
         try{
             iterator = Query.readQueryList( userid, community, communitySnippet, filter) ;
             
             while ( iterator.hasNext() ) {
                 logger.info( "Query Name: " + (String)iterator.next() ) ;
             }
 
             assertTrue( true ) ;    
         }
         catch( Exception ex ) {
             
             assertTrue( false ) ;
             ex.printStackTrace() ;
         }
         finally {
             logger.info( "exit: WorkflowTestSuite.testReadQueryList()" );  
         }
        
     } // end of testReadQueryList()
    
    
    public void testReadWorkflow() {
         logger.info( "enter: WorkflowTestSuite.testReadWorkflow()" ); 
        
         final String 
             userid = "dfh",
             community = "nottingham",
             name = "JobFromDFH", 
             communitySnippet = "dummySnippet";
         Workflow
            workflow ;
            
         try{
             workflow = Workflow.readWorkflow( userid, community, communitySnippet, name) ;
            
             logger.info( "Workflow: " + workflow.toXMLString() ) ;
 
             assertTrue( true ) ;    
         }
         catch( Exception ex ) {
             
             assertTrue( false ) ;
             ex.printStackTrace() ;
         }
         finally {
             logger.info( "exit: WorkflowTestSuite.testReadWorkflow()" );  
         }
        
     } // end of testReadWorkflow()   
    



    
    
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