package org.astrogrid.common.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.astrogrid.TemplateManager ;
 
public class TemplateTestSuite extends TestCase {
	
	private static Logger 
		logger = Logger.getLogger( TemplateTestSuite.class ) ;
		
	private static final String
	    log4jproperties = "/home/jl99/development/workspace/datacenter/test/files/log4j.properties" ;
		
		
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
            "<job name=\"{0}\" userid=\"{1}\" community=\"{2}\" time=\"{3}\" jobURN=\"{4}\" />" ,
			fileName = "JobSchedulerRequestTemplate.xml" ;
		String
			loadedTemplateString = "" ;
			
		try{
            loadedTemplateString = TemplateManager.getInstance().getTemplate( "DTC", fileName ) ;
			logger.info( "loadedTemplateString: " + loadedTemplateString ) ;
			assertTrue( loadedTemplateString.equals( correctTemplateString ) ) ;	
		}
		catch( Exception ex ) {
			assertTrue( false ) ;
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

	   PropertyConfigurator.configure( log4jproperties ) ;
	   logger.info("Entering TemplateTest application.");	
	   junit.textui.TestRunner.run( suite() ) ;
	   logger.info("Exit TemplateTest application.");
    }
		
} // end of class TemplateTestSuite