package org.astrogrid.common.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import org.astrogrid.AstroGridException;
 
public class ConfiguratorTestSuite extends TestCase {
	
	private static Logger 
		logger = Logger.getLogger( ConfiguratorTestSuite.class ) ;
		
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
    
    
    public void testMissingFile() {
        logger.info( "enter: ConfiguratorTestSuite.testMissingFile()" ); 
        
        try{
            SubConfigurator_AAA.getInstance().checkPropertiesLoaded() ;
            assertTrue( false ) ;
        }
        catch( AstroGridException ex ) {
            assertTrue( true ) ;
        }
        finally {
            logger.info( "exit: ConfiguratorTestSuite.testMissingFile()" );  
        }
        
    } // end of testMissingFile()


    public void testMalformedXMLFile() {
        logger.info( "enter: ConfiguratorTestSuite.testMalformedXMLFile()" ); 
        
        try{
            SubConfigurator_BBB.getInstance().checkPropertiesLoaded() ;
            assertTrue( false ) ;
        }
        catch( AstroGridException ex ) {
            assertTrue( true ) ;
        }
        finally {
            logger.info( "exit: ConfiguratorTestSuite.testMalformedXMLFile()" );  
        }
        
    } // end of testMalformedXMLFile()


    public void testMisnamedCategory() {
        logger.info( "enter: ConfiguratorTestSuite.testMisnamedCategory()" ); 
        
        try{
            SubConfigurator_CCC.getInstance().checkPropertiesLoaded() ;
            assertTrue( true ) ;
            SubConfigurator_CCC.getProperty( "DATASETAGENT", "VALIDATION" ) ;
        }
        catch( AstroGridException ex ) {
            assertTrue( false ) ;
        }
        finally {
            logger.info( "exit: ConfiguratorTestSuite.testMisnamedCategory()" );  
        }
        
    } // end of testMisnamedCategory()


    public void testCoexisting() {
        logger.info( "enter: ConfiguratorTestSuite.testCoexisting()" ); 
        
        try{
            SubConfigurator_DDD.getInstance().checkPropertiesLoaded() ;
            assertTrue( true ) ;
            SubConfigurator_EEE.getInstance().checkPropertiesLoaded() ;
            assertTrue( true ) ;
            String
               dddValue = SubConfigurator_DDD.getProperty( "SOME.PROPERTY", "DUPLICATE" ),
               eeeValue = SubConfigurator_EEE.getProperty( "SOME.PROPERTY", "DUPLICATE" ) ;
            assertTrue( dddValue.equals("DDD_value") ) ;
            assertTrue( eeeValue.equals("EEE_value") ) ;
        }
        catch( AstroGridException ex ) {
            assertTrue( false ) ;
        }
        finally {
            logger.info( "exit: ConfiguratorTestSuite.testCoexisting()" );  
        }
        
    } // end of testCoexisting()
	/**
	 * Assembles and returns a test suite for
	 * all the test methods of this test case.
	 *
	 * @return A non-null test suite.
	 */
	public static Test suite() {
		// Reflection is used here to add all the testXXX() methods to the suite.
		return new TestSuite( ConfiguratorTestSuite.class ) ;
	}


	/**
     * Runs the test case.
     */
    public static void main( String args[] )  {

	   PropertyConfigurator.configure( log4jproperties ) ;
	   logger.info("Entering Configurator test application.");	
	   junit.textui.TestRunner.run( suite() ) ;
	   logger.info("Exit Configurator test application.");
    }
		
} // end of class ConfiguratorTestSuite