/*
 * Created on 25-Apr-2003
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
public class RegistryInterfaceServiceTest extends TestCase {
  public void testSubmitQuery(){
  	String query = "<query><queryType><portalQuery><id>This query</id></portalQuery></queryType></query>";
  	RegistryInterfaceService ris = new RegistryInterfaceService();
    assertEquals("portalQuery", ris.submitQuery(query));
	String query2 = "<query><queryType><workflowQuery><id>This query</id></workflowQuery></queryType></query>";
	assertEquals("workflowQuery", ris.submitQuery(query2));
	String query3 = "<query><queryType><jobSchedulerQuery><id>This query</id></jobSchedulerQuery></queryType></query>";
	assertEquals("jobSchedulerQuery", ris.submitQuery(query3));
	String query4 = "<query><queryType><dataMoverQuery><id>This query</id></dataMoverQuery></queryType></query>";
	assertEquals("dataMoverQuery", ris.submitQuery(query4));
	String query5 = "<query><workflowQuery><id>This query</id></workflowQuery></query>";
	assertEquals("", ris.submitQuery(query5));
	String query6 = "<query><workflowQuery><id>This query</id></workflowQuery>";
	assertEquals("", ris.submitQuery(query6));
	String query7 = "";
	assertEquals("", ris.submitQuery(query7));
	String query8 = "<query><queryType><id>This query</id></queryType></query>";
	assertEquals("", ris.submitQuery(query8));
	
  }
}
