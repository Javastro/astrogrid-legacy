package org.astrogrid.common.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import org.astrogrid.AstroGridException;
import org.astrogrid.i18n.*;
 
public class MessageTestSuite extends TestCase {
	
	private static Logger 
		logger = Logger.getLogger( MessageTestSuite.class ) ;
		
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
        logger.info( "enter: testMissingFile()" ); 
        
        try{
            SubConfigurator_AAA_messagetest.getInstance().checkPropertiesLoaded() ;
            AstroGridMessage
                message = new AstroGridMessage( "AGAAAI00200"
                                              , "MessageTestSuite"
                                              , "Message file missing!!!" ) ;
                                              
            logger.info( message.toString() ) ;
            
            assertTrue( true ) ; // All the above should be failsafe!           
        }
        catch( AstroGridException ex ) {
            assertTrue( false ) ;  // We should never fail completely with a message
        }
        finally {
            logger.info( "exit: testMissingFile()" );  
        }
        
    } // end of testMissingFile()


    public void testMissingKey() {
        logger.info( "enter: testMissingKey()" ); 
        
        try{
            SubConfigurator_BBB_messagetest.getInstance().checkPropertiesLoaded() ;
            AstroGridMessage
                message = new AstroGridMessage( "AGBBBI00300"
                                              , "MessageTestSuite"
                                              , "Message key missing!!!" ) ;
                                              
            logger.info( message.toString() ) ;
            
            assertTrue( true ) ; // All the above should be failsafe!           
        }
        catch( AstroGridException ex ) {
            assertTrue( false ) ;  // We should never fail completely with a message
        }
        finally {
            logger.info( "exit: testMissingKey()" );  
        }
        
    } // end of testMissingKey()


    public void testMalformedKey_SubsystemMissing() {
        logger.info( "enter: testMalformedKey_SubsystemMissing()" ); 
        
        try{
            SubConfigurator_BBB_messagetest.getInstance().checkPropertiesLoaded() ;
            AstroGridMessage
                message = new AstroGridMessage( "AG...E00030"
                                              , "MessageTestSuite"
                                              , "Message key malformed - subsystem incorrect!" ) ;
                                              
            logger.info( message.toString() ) ;
            
            assertTrue( true ) ; // All the above should be failsafe!           
        }
        catch( AstroGridException ex ) {
            assertTrue( false ) ;  // We should never fail completely with a message
        }
        finally {
            logger.info( "exit: testMalformedKey_SubsystemMissing()" );  
        }
        
    } // end of testMalformedKey_SubsystemMissing()
 

    public void testMalformedKey_null() {
        logger.info( "enter: testMalformedKey_null()" ); 
        
        try{
            SubConfigurator_BBB_messagetest.getInstance().checkPropertiesLoaded() ;
            AstroGridMessage
                message = new AstroGridMessage( null
                                              , "MessageTestSuite"
                                              , "Message key is null" ) ;
                                              
            logger.info( message.toString() ) ;
            
            assertTrue( true ) ; // All the above should be failsafe!           
        }
        catch( AstroGridException ex ) {
            assertTrue( false ) ;  // We should never fail completely with a message
        }
        finally {
            logger.info( "exit: testMalformedKey_null()" );  
        }
        
    } // end of testMalformedKey_null() 
 
 
    public void testInserts_tooFew() {
        logger.info( "enter: testInserts_tooFew()" ); 
        
        try{
            SubConfigurator_BBB_messagetest.getInstance().checkPropertiesLoaded() ;
            AstroGridMessage
                message = new AstroGridMessage( "AGBBBE00040"
                                              , "MessageTestSuite"
                                              , "this should be insert-1" ) ;
                                              
            logger.info( message.toString() ) ;
            
            assertTrue( true ) ; // All the above should be failsafe!           
        }
        catch( AstroGridException ex ) {
            assertTrue( false ) ;  // We should never fail completely with a message
        }
        finally {
            logger.info( "exit: testInserts_tooFew()" );  
        }
        
    } // end of testInserts_tooFew() 
    
    
    public void testInserts_tooMany() {
        logger.info( "enter: testInserts_tooMany()" ); 
        
        try{
            SubConfigurator_BBB_messagetest.getInstance().checkPropertiesLoaded() ;
            Object[]
               oa = new Object[5] ;
            oa[0] = "MessageTestSuite" ;
            oa[1] = "this is insert-1";
            oa[2] = "this is insert-2";
            oa[3] = "this is insert-3";
            oa[4] = "this is insert-4";
            
            AstroGridMessage
                message = new AstroGridMessage( "AGBBBE00040"
                                              , oa ) ;
                                              
            logger.info( message.toString() ) ;
            
            assertTrue( true ) ; // All the above should be failsafe!           
        }
        catch( AstroGridException ex ) {
            assertTrue( false ) ;  // We should never fail completely with a message
        }
        finally {
            logger.info( "exit: testInserts_tooMany()" );  
        }
        
    } // end of testInserts_tooMany()  
    
    
    public void testInserts_Null() {
        logger.info( "enter: testInserts_Null()" ); 
        
        try{
            SubConfigurator_BBB_messagetest.getInstance().checkPropertiesLoaded() ;
            Object[]
               oa = new Object[5] ;
            oa[0] = "MessageTestSuite" ;
            oa[1] = "this is insert-1";
            oa[2] = null ;
            oa[3] = "this is insert-3";
            
            AstroGridMessage
                message = new AstroGridMessage( "AGBBBE00040"
                                              , oa ) ;
                                              
            logger.info( message.toString() ) ;
            
            assertTrue( true ) ; // All the above should be failsafe!           
        }
        catch( AstroGridException ex ) {
            assertTrue( false ) ;  // We should never fail completely with a message
        }
        finally {
            logger.info( "exit: testInserts_Null()" );  
        }
        
    } // end of testInserts_Null()        
    
    
    public void testInserts_NonString() {
        logger.info( "enter: testInserts_NonString()" ); 
        
        try{
            SubConfigurator_BBB_messagetest.getInstance().checkPropertiesLoaded() ;
            Object[]
               oa = new Object[5] ;
            oa[0] = "MessageTestSuite" ;
            oa[1] = "this is insert-1";
            oa[2] = new Integer( 123 ) ;
            oa[3] = "this is insert-3";
            
            AstroGridMessage
                message = new AstroGridMessage( "AGBBBE00040"
                                              , oa ) ;
                                              
            logger.info( message.toString() ) ;
            
            assertTrue( true ) ; // All the above should be failsafe!           
        }
        catch( AstroGridException ex ) {
            assertTrue( false ) ;  // We should never fail completely with a message
        }
        finally {
            logger.info( "exit: testInserts_NonString()" );  
        }
        
    } // end of testInserts_NonString()        
    
 
	/**
	 * Assembles and returns a test suite for
	 * all the test methods of this test case.
	 *
	 * @return A non-null test suite.
	 */
	public static Test suite() {
		// Reflection is used here to add all the testXXX() methods to the suite.
		return new TestSuite( MessageTestSuite.class ) ;
	}


	/**
     * Runs the test case.
     */
    public static void main( String args[] )  {

	   PropertyConfigurator.configure( log4jproperties ) ;
	   logger.info("Entering Message test application.");	
	   junit.textui.TestRunner.run( suite() ) ;
	   logger.info("Exit Message test application.");
    }
		
} // end of class MessageTestSuite
