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
public class QueryParserTest extends TestCase {
  public void testParseQuery(){
  	String query = "<query><queryType><portalQuery><id>This query</id></portalQuery></queryType></query>";
  	QueryParser qp = new QueryParser();
    assertEquals("portalQuery", QueryParser.parseQuery(query));
	String query2 = "<query><queryType><workflowQuery><id>This query</id></workflowQuery></queryType></query>";
	assertEquals("workflowQuery", QueryParser.parseQuery(query2));
	String query3 = "<query><queryType><jobSchedulerQuery><id>This query</id></jobSchedulerQuery></queryType></query>";
	assertEquals("jobSchedulerQuery", QueryParser.parseQuery(query3));
	String query4 = "<query><queryType><dataMoverQuery><id>This query</id></dataMoverQuery></queryType></query>";
	assertEquals("dataMoverQuery", QueryParser.parseQuery(query4));
	String query5 = "<query><workflowQuery><id>This query</id></workflowQuery></query>";
	assertEquals("", QueryParser.parseQuery(query5));
	String query6 = "<query><workflowQuery><id>This query</id></workflowQuery>";
	assertEquals("", QueryParser.parseQuery(query6));
	String query7 = "";
	assertEquals("", QueryParser.parseQuery(query7));
	String query8 = "<query><queryType><id>This query</id></queryType></query>";
	assertEquals("", QueryParser.parseQuery(query8));
	
  }
}
