/*
 * Created on 19-May-2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.astrogrid.datacenter.testsuite;

import org.astrogrid.datacenter.* ;
import junit.framework.* ;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import org.apache.log4j.PropertyConfigurator ;
import org.apache.log4j.BasicConfigurator ;

import java.io.FileReader;
import java.io.BufferedReader ;
import java.io.FileNotFoundException ;
import java.io.IOException ;

/**
 * @author jl99
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class DatasetAgentTest extends TestCase {
	
	static Logger 
		logger = Logger.getLogger( DatasetAgentTest.class );
		
	public static BufferedReader
		reader = null ;
		
	public static StringBuffer
		buffer = new StringBuffer( 4096 ) ;

    public void setUp() {	 	
    }

    public void tearDown() {
    }

	public void testDataCenter() {
		logger.info("Entering testDataCenter.");	
		logger.info( buffer.toString() ) ;

		DatasetAgent
			dsAgent= new DatasetAgent() ;
		dsAgent.runQuery( buffer.toString() ) ;	
		assertTrue( true ) ;
		logger.info("Exiting testDataCenter.");	
	} // end of testDataCenter()
	
	
	public static void main( String[] args ) {
		
//		BasicConfigurator.configure();
        PropertyConfigurator.configure( "/home/jl99/downloads/log4j.properties" ) ;
				
        if( args.length == 0 ) {
             System.out.println( "File name required" ) ;
        }
		else { 
		
		    try {   
			
		        reader = new BufferedReader( new FileReader( args[0] ) ) ;
		    
		        while( reader.ready() ) {
		    	    buffer.append( reader.readLine() ) ;
		        }
		
			    logger.info("Entering application.");	
			    junit.textui.TestRunner.run( DatasetAgentTest.class );
			    logger.info("Exit application.");
		    }
		    catch( FileNotFoundException fnfex ) {
			    logger.error( "File not found: " + args[0] ) ;	
		    }
		    catch( IOException ioex ) {
		        logger.error( ioex.getMessage() ) ; 
		    }
		
		} // end else
		  
	} // end of main()
		
} // end of class DatasetAgentTest
