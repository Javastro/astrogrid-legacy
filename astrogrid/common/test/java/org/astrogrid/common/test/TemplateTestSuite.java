package org.astrogrid.common.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.TemplateManager;
 
public class TemplateTestSuite extends TestCase {
	
	private static Log 
		logger = LogFactory.getLog( TemplateTestSuite.class ) ;
		
		
		
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
	
	
	public void testCorrectLoading() {
		logger.info( "enter: TemplateTestSuite.testCorrectLoading()" );	
		
		// This is what should be returned... 
		final String
			correctTemplateString = "<?xml version=\"1.0\" encoding=\"UTF8\"?> <!-- JobScheduler Request Template -->" +
            "<job name=\"{0}\" time=\"{1}\" jobURN=\"{2}\" >{3}</job>" ,
			fileName = "JobSchedulerRequest.xmltemplate" ;
		String
			loadedTemplateString = "" ;
			
		try{
            loadedTemplateString = TemplateManager.getInstance().getTemplate( "DTC", fileName ) ;
			logger.info( "loadedTemplateString: " + loadedTemplateString ) ;
			assertEquals( correctTemplateString,loadedTemplateString ) ;	
		}
		finally {
			logger.info( "exit: TemplateTestSuite.testCorrectLoading()" );	
		}
        
	} // end of testCorrectLoading()
    
    
    public void testMissingFile() {
        logger.info( "enter: TemplateTestSuite.testMissingFile()" ); 
        
        final String
            fileName = "NonExistentTemplate.xml" ;
        String
            loadedTemplateString = null ;
            
        try{
            loadedTemplateString = TemplateManager.getInstance().getTemplate( "DTC", fileName ) ;
            assertTrue( loadedTemplateString == null ) ;
        }
        catch( Exception ex ) {
            assertTrue( false ) ;
        }
        finally {
            logger.info( "exit: TemplateTestSuite.testMissingFile()" );  
        }
        
    } // end of testMissingFile()



	/**
	 * Assembles and returns a test suite for
	 * all the test methods of this test case.
	 *
	 * @return A non-null test suite.
	 */
	public static Test suite() {
		// Reflection is used here to add all the testXXX() methods to the suite.
		return new TestSuite( TemplateTestSuite.class ) ;
	}


	/**
     * Runs the test case.
     */
    public static void main( String args[] )  {


	   logger.info("Entering TemplateTest application.");	
	   junit.textui.TestRunner.run( suite() ) ;
	   logger.info("Exit TemplateTest application.");
    }
		
} // end of class TemplateTestSuite
