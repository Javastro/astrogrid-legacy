package org.astrogrid.portal.workflow.jes ;

import org.astrogrid.i18n.*;
import org.astrogrid.AstroGridException ;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import junit.framework.Test; 
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.astrogrid.community.common.util.CommunityMessage;
import java.util.ListIterator;

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

 
public class JesTestSuite extends TestCase {
	
	private static Logger 
		logger = Logger.getLogger( JesTestSuite.class ) ;
		
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
	
    
    
    public void testReadJobList() {
         logger.info( "enter: JesTestSuite.testReadJobList()" ); 
        
         final String 
             userid = "jl99",
             community = "leicester",
             communitySnippet 
                = CommunityMessage.getMessage( "1234"
                                             , userid + "@" + community
                                             , "guest@" + community ) ;
         
         ListIterator
            iterator = null ;
         Job
            job = null ;
            
         try{
             iterator = Job.readJobList( userid
                                       , community
                                       , communitySnippet
                                       , "*" ) ;
                                       
             while( iterator.hasNext() ) {
                 job = (Job)iterator.next() ;
                 logger.info( "====*====" ) ;
                 logger.info( "job.getName(): " + job.getName() ) ;
                 logger.info( "job.getDescription(): " + job.getDescription() ) ;
                 logger.info( "job.getStatus(): " + job.getStatus() ) ;
                 logger.info( "job.getTimestamp(): " + job.getTimestamp() ) ;
                 logger.info( "job.getJobid(): " + job.getJobid() ) ;
                 logger.info( "job.getUserid(): " + job.getUserid() ) ;
                 assertTrue( userid.equals( job.getUserid() ) ) ;
                 logger.info( "job.getCommunity(): " + job.getCommunity() ) ;
                 assertTrue( community.equals( job.getCommunity() ) ) ;
             }
             logger.info( "====*====" ) ;
             assertTrue( true ) ;    
         }
         catch( Exception ex ) {
             
             assertTrue( false ) ;
             ex.printStackTrace() ;
         }
         finally {
             logger.info( "exit: JesTestSuite.testReadJobList()" );  
         }
        
     } // end of testReadJobList()
    
    

	/** 
	 * Assembles and returns a test suite for
	 * all the test methods of this test case.
	 *
	 * @return A non-null test suite.
	 */
	public static Test suite() {
		// Reflection is used here to add all the testXXX() methods to the suite.
		return new TestSuite( JesTestSuite.class ) ;
	}


	/**
     * Runs the test case.
     */
    public static void main( String args[] )  {

//		PropertyConfigurator.configure( log4jproperties ) ;
			
	   logger.info("Entering JesTestSuite application.");	
	   junit.textui.TestRunner.run( suite() ) ;
	   logger.info("Exit JesTestSuite application.");
		
    }
    
} // end of class JesTestSuite