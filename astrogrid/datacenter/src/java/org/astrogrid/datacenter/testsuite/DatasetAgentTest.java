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
import org.apache.log4j.PropertyConfigurator ;
import org.apache.log4j.BasicConfigurator ;

/**
 * @author jl99
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class DatasetAgentTest extends TestCase {
	
	private static final String
	    query7 =  
	"<?xml version = '1.0' encoding = 'UTF8'?>" +
	"<job name = 'jl012'>" +
	   "<userid>jlusted</userid>" +
	   "<community>leicester</community>" +
	   "<jobstep name='aconesearch'>" +
		  "<query>" + 
		     "<from></from>"+      
			  "<return></return>" +			  "<criteria>" +                 "<operation name='GREATER_THAN'>"
                 			  "SELECT COUNT(*) FROM USNOB..USNOB" + 
		  "</query>" +
	   "</jobstep>" +
	"</job>";
	
	static Logger 
		logger = Logger.getLogger( DatasetAgentTest.class );

    public void setUp() {
    	 	
    }

    public void tearDown() {
    	
    }

	public void testDataCenter() {
		DatasetAgent
			dsAgent= new DatasetAgent() ;
		dsAgent.runQuery( query7 ) ;	
		assertTrue( true ) ;
	}
	
	public static void main(String[] args) {
		
		BasicConfigurator.configure();
		logger.info("Entering application.");	
		junit.textui.TestRunner.run( DatasetAgentTest.class );
		logger.info("Exit application.");
			    
	}
		
}
