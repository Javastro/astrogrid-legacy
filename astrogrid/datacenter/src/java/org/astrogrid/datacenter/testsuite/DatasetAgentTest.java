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
	
	static Logger 
		logger = Logger.getLogger( DatasetAgentTest.class );

    public void setUp() {
    	 	
    }

    public void tearDown() {
    	
    }

	public void testDataCenter() {
		DatasetAgent
			dsAgent= new DatasetAgent() ;
		dsAgent.runQuery( "This should be XML" ) ;	
		assertTrue( true ) ;
	}
	
	public static void main(String[] args) {
		
		BasicConfigurator.configure();
		logger.info("Entering application.");	
		junit.textui.TestRunner.run( DatasetAgentTest.class );
		logger.info("Exit application.");
			    
	}
		
}
