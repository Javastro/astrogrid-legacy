package org.astrogrid.portal.workflow.design ;


import junit.framework.Test; 
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.log4j.Logger;


public class CardinalityTestSuite extends TestCase {
	
	private static Logger 
		logger = Logger.getLogger( CardinalityTestSuite.class ) ;
		
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
	
    
    /**
     * Tests a maximum cardinality with value less than -1.
     * It should be set to -1 by the constructor.
     *  
     */
    public void testMaxCardinalityLessThanMinusOne() {
         logger.info( "---------------------------------------------------------" ); 
         logger.info( "enter: CardinalityTestSuite.testMaxCardinalityLessThanMinusOne()" ); 
        
         Cardinality cardinality = null ;
            
         try {
             cardinality = new Cardinality( 1, -2 ) ;
             logger.info( "new Cardinality( 1, -2 ) just executed.") ;
             logger.info( "cardinality.getMimimum(): " + cardinality.getMinimum() ) ;
             logger.info( "cardinality.getMaximum(): " + cardinality.getMaximum() ) ;
             if( cardinality.getMaximum() == Cardinality.UNLIMITED ) {
                 assertTrue( true ) ; 
             }
             else {
                 assertTrue( false ) ;
             }
    
         }
         catch( Exception ex ) {             
             assertTrue( false ) ;
             ex.printStackTrace() ;
         }
         finally {
             logger.info( "exit: CardinalityTestSuite.testMaxCardinalityLessThanMinusOne()" );  
         }
        
     } // end of testMaxCardinalityLessThanMinusOne()
    
    
    /**
     * Tests a minimum cardinality with value less than 0.
     * It should be set the minimum cardinality to the default.
     *  
     */
    public void testMinCardinalityLessThanZero() {
         logger.info( "---------------------------------------------------------" ); 
         logger.info( "enter: CardinalityTestSuite.testMinCardinalityLessThanZero()" ); 
        
         Cardinality cardinality = null ;
            
         try {
             cardinality = new Cardinality( -1, 1 ) ;
             logger.info( "new Cardinality( -1, 1 ) just executed.") ;
             logger.info( "cardinality.getMimimum(): " + cardinality.getMinimum() ) ;
             logger.info( "cardinality.getMaximum(): " + cardinality.getMaximum() ) ;
             if( cardinality.getMinimum() == Cardinality.MIN_DEFAULT ) {
                 assertTrue( true ) ; 
             }
             else {
                 assertTrue( false ) ;
             }
    
         }
         catch( Exception ex ) {             
             assertTrue( false ) ;
             ex.printStackTrace() ;
         }
         finally {
             logger.info( "exit: CardinalityTestSuite.testMinCardinalityLessThanZero()" );  
         }
        
     } // end of testMinCardinalityLessThanZero()
    
    
    /**
     * Tests a minimum cardinality with value greater than maximum.
     * It should be set the minimum and maximum to their default values.
     *  
     */
    public void testMinCardinalityGreaterThanMax() {
         logger.info( "---------------------------------------------------------" ); 
         logger.info( "enter: CardinalityTestSuite.testMinCardinalityGreaterThanMax()" ); 
        
         Cardinality cardinality = null ;
            
         try {
             cardinality = new Cardinality( 1, 0 ) ;
             logger.info( "new Cardinality( 1, 0 ) just executed.") ;
             logger.info( "cardinality.getMimimum(): " + cardinality.getMinimum() ) ;
             logger.info( "cardinality.getMaximum(): " + cardinality.getMaximum() ) ;
             if( (cardinality.getMinimum() == Cardinality.MIN_DEFAULT) 
                 &&
                 (cardinality.getMaximum() == Cardinality.MAX_DEFAULT)  ) {
                 assertTrue( true ) ; 
             }
             else {
                 assertTrue( false ) ;
             }
    
         }
         catch( Exception ex ) {             
             assertTrue( false ) ;
             ex.printStackTrace() ;
         }
         finally {
             logger.info( "exit: CardinalityTestSuite.testMinCardinalityGreaterThanMax()" );  
         }
        
     } // end of testMinCardinalityGreaterThanMax()
    
   
   
    /**
     * Tests a maximum cardinality with value 0.
     * It should be set the maximum cardinality to its default values.
     *  
     */
    public void testMaxCardinalityOfZero() {
         logger.info( "---------------------------------------------------------" ); 
         logger.info( "enter: CardinalityTestSuite.testMaxCardinalityOfZero()" ); 
        
         Cardinality cardinality = null ;
            
         try {
             cardinality = new Cardinality( 0, 0 ) ;
             logger.info( "new Cardinality( 0, 0 ) just executed.") ;
             logger.info( "cardinality.getMimimum(): " + cardinality.getMinimum() ) ;
             logger.info( "cardinality.getMaximum(): " + cardinality.getMaximum() ) ;
             if( cardinality.getMaximum() == Cardinality.MAX_DEFAULT  ) {
                 assertTrue( true ) ; 
             }
             else {
                 assertTrue( false ) ;
             }
    
         }
         catch( Exception ex ) {             
             assertTrue( false ) ;
             ex.printStackTrace() ;
         }
         finally {
             logger.info( "exit: CardinalityTestSuite.testMaxCardinalityOfZero()" );  
         }
        
     } // end of testMaxCardinalityOfZero() 
    
    
    /**
     * Tests a sensible cardinality.
     */
    public void testSensibleCardinality() {
         logger.info( "---------------------------------------------------------" ); 
         logger.info( "enter: CardinalityTestSuite.testSensibleCardinality()" ); 
        
         Cardinality cardinality = null ;
            
         try {
             cardinality = new Cardinality( 2, 3 ) ;
             logger.info( "new Cardinality( 2, 3 ) just executed.") ;
             logger.info( "cardinality.getMimimum(): " + cardinality.getMinimum() ) ;
             logger.info( "cardinality.getMaximum(): " + cardinality.getMaximum() ) ;
             if( cardinality.getMaximum() >= cardinality.getMinimum() ) {
                 assertTrue( true ) ; 
             }
             else {
                 assertTrue( false ) ;
             }
    
         }
         catch( Exception ex ) {             
             assertTrue( false ) ;
             ex.printStackTrace() ;
         }
         finally {
             logger.info( "exit: CardinalityTestSuite.testSensibleCardinality()" );  
         }
        
     } // end of testSensibleCardinality() 
     
     
    /**
     * Tests 100 random cardinalities.
     */
    public void testRandomCardinalities() {
         logger.info( "---------------------------------------------------------" ); 
         logger.info( "enter: CardinalityTestSuite.testRandomCardinalities()" ); 
        
         Cardinality cardinality = null ;
         int max, min ;
            
         try {
             
             int uniqueID =  ( new Double( Math.random() * Integer.MAX_VALUE ) ).intValue() ;
             
             for( int i=0; i<100; i++ ) {
                 
                 min = ( new Double( Math.random() * Integer.MAX_VALUE ) ).intValue() ;
                 max = ( new Double( Math.random() * Integer.MAX_VALUE ) ).intValue() ;
                 if( max%3 == 0 ) {
                     max = -max ;
                 }
                 if( min%7 == 0 ) {
                     min = -min ;
                 }
                 cardinality = new Cardinality( min, max ) ;
                 logger.info( "new Cardinality( " + min + ", " + max + " ) just executed." ) ;
                 logger.info( "cardinality.getMimimum(): " + cardinality.getMinimum() ) ;
                 logger.info( "cardinality.getMaximum(): " + cardinality.getMaximum() ) ;                           
                 if( (cardinality.getMaximum() != Cardinality.UNLIMITED) 
                      &&
                     (cardinality.getMaximum() >= cardinality.getMinimum())  ) {
                     
                     assertTrue( true ) ; 
                 }
                 else if( (cardinality.getMaximum() == Cardinality.UNLIMITED) 
                           &&
                          (cardinality.getMinimum() >= 0 )  )  {
                     assertTrue( true ) ;
                 }
                 else {
                     assertTrue( false ) ;
                 }
                 
             }
    
         }
         catch( Exception ex ) {  
             ex.printStackTrace() ;           
             assertTrue( false ) ;
         }
         finally {
             logger.info( "exit: CardinalityTestSuite.testRandomCardinalities()" );  
         }
        
     } // end of testRandomCardinalities() 
    
    
    
	/** 
	 * Assembles and returns a test suite for
	 * all the test methods of this test case.
	 *
	 * @return A non-null test suite.
	 */
	public static Test suite() {
		// Reflection is used here to add all the testXXX() methods to the suite.
		return new TestSuite( CardinalityTestSuite.class ) ;
	}


	/**
     * Runs the test case.
     */
    public static void main( String args[] )  {

//		PropertyConfigurator.configure( log4jproperties ) ;
			
	   logger.info("Entering CardinalityTestSuite application.");
       junit.textui.TestRunner.run( suite() ) ;
	   logger.info("Exit CardinalityTestSuite application.");
		
    }
    
} // end of class CardinalityTestSuite