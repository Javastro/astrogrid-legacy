package org.astrogrid.portal.workflow.design ;

import java.io.IOException;

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