/*
 * Created on 18-Jul-2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.astrogrid.registry;
import junit.framework.TestCase;
/**
 * @author Elizabeth Auden
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class RegistryTest3_0 extends TestCase {
	public void testXMLQuery(){
		/** Test multiple nested selectionSequences:*/
		Registry3_0 r3 = new Registry3_0();
		
		/** Test for malformed XQL throws proper error. */
		String query= "//service[(((.//resolutionSpatial < '0.13'))]/identity";		String queryResponse = "<queryResponse><error>Malformed XQL statement.</error></queryResponse>";
		assertEquals(queryResponse, r3.xmlQuery(query));	
	}
}
