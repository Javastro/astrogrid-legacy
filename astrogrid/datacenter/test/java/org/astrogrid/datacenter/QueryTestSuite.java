package org.astrogrid.datacenter;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.astrogrid.datacenter.datasetagent.*;
import org.astrogrid.datacenter.query.*;
import org.astrogrid.datacenter.impl.*;
import org.w3c.dom.* ;
// import junit.framework.* ;
import org.apache.log4j.Logger;
// import org.apache.log4j.Level;
import org.apache.log4j.PropertyConfigurator ;

import org.apache.axis.utils.XMLUtils ;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException ;


// import java.io.FileReader;
// import java.io.BufferedReader ;
// import java.io.FileNotFoundException ;
import java.io.IOException ;

 
public class QueryTestSuite extends TestCase {
	
	private static Logger 
		logger = Logger.getLogger( QueryTestSuite.class ) ;
		
	public static String
	    path ; // Path to directory where query XML docs are stored
		
	public QueryFactoryImpl
		factory = new QueryFactoryImpl() ;
	
	
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
	
	
	public Element setUpQueryElement( String filePath ) {	
		logger.info( "enter: QueryTestSuite.setUpQueryElement()" );		
			
		Element
		    element = null ;

		try {
  			
  			Document
			    document = XMLUtils.newDocument( filePath ) ;
			
			NodeList
			   nodeList = document.getDocumentElement().getElementsByTagName( RunJobRequestDD.QUERY_ELEMENT ) ;
			   
			for( int i=0 ; i < nodeList.getLength() ; i++ ) {
				
				if( nodeList.item(i).getNodeType() != Node.ELEMENT_NODE )
					continue ;				
				element = (Element) nodeList.item(i) ;
				if( element.getTagName().equals( RunJobRequestDD.QUERY_ELEMENT ) )
					break ;
				
			}
			
			if( element == null ) {
				logger.error( "Failed to pick up a Query element" ) ; 
				assertTrue( false ) ;
			}
			
		}
		catch( IOException ioex ) {
			logger.error( ioex.getMessage() ) ; 
			assertTrue( false ) ;
		}
		catch( SAXException sax ) {
			logger.error( sax.getMessage() ) ; 
			assertTrue( false ) ;		    	
		}
		catch( ParserConfigurationException pcex ) {
			logger.error( pcex.getMessage() ) ; 
			assertTrue( false ) ;
		}
		finally {
			logger.info( "exit: QueryTestSuite.setUpQueryElement()" );					
		}
		
		return element ;

	} // end setUpQueryElement()
    


	public void testQueryToString_CONE() {
		logger.info( "enter: QueryTestSuite.testQueryToString()" );	
		
		// This is the pseudo-SQL... "SELECT URA, UDEC, PMPROB FROM USNOB WHERE CONE(234.56, -12.34, 0.01)"
        final String
            sqlString = "SELECT  URA, UDEC, PMPROB FROM USNOB..USNOB  WHERE " +
                        "( ((2 * ASIN(SQRT(POW(SIN(-12.34-UDEC)/2),2) + COS(UDEC) + POW(SIN(234.56-URA)/2),2)) < 0.01 )" ,
            fileName = "query_CONE01.xml" ;
        Element
            queryElement = this.setUpQueryElement( path + System.getProperty( "file.separator" ) + fileName ) ;
		Query
			query = null ;
		String
		    resultString = null ;
			
		try{
			query = new Query( queryElement, factory ) ;
			resultString = query.toSQLString() ;
			logger.info( "testQueryToString_CONE: " + resultString ) ;
			assertTrue( resultString.equals( sqlString ) ) ;	
		}
		catch( Exception ex ) {
			assertTrue( false ) ;
		}
		finally {
			logger.info( "exit: QueryTestSuite.testQueryToString()" );	
		}
        
	} // end of testQueryToString_CONE()


	/**
	 * Assembles and returns a test suite for
	 * all the test methods of this test case.
	 *
	 * @return A non-null test suite.
	 */
	public static Test suite() {
		// Reflection is used here to add all the testXXX() methods to the suite.
		return new TestSuite( QueryTestSuite.class ) ;
	}


	/**
     * Runs the test case.
     */
    public static void main( String args[] )  {

		PropertyConfigurator.configure( "/home/jl99/downloads/log4j.properties" ) ;
				
		if( args.length == 0 ) {
			 System.out.println( "Usage: Path to XML query directory required" ) ;
		}
		else { 
		
			 path = args[0] ;			

			 logger.info("Entering QueryTest application.");	
			 junit.textui.TestRunner.run( suite() ) ;
			 logger.info("Exit QueryTest application.");
		
		} // end else
    }
    
} // end of class QueryTestSuite