/*
 * Created on 25-Apr-2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.astrogrid.registry;

/**
 * @author Elizabeth Auden
 * 
 * The RegistryInterface3_0 class is a web service that submits an XML formatted
 * registry query to the QueryParser3_0 class.  This web service allows
 * a user to browse the registry.  Queries should be formatted according to
 * the schema at http://wiki.astrogrid.org/bin/view/Astrogrid/RegistryQuerySchema.
 * 
 * Elizabeth  Auden, 24 October 2003
 */

public class RegistryInterface3_0 {

  public String submitQuery(String query) {
    String queryResponse = new String();
    //QueryParser3_0 qp = new QueryParser3_0();
    try {
		queryResponse = QueryParser3_0.parseQuery(query);
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return queryResponse;
  }
  
  public String fullNodeQuery(String query) {
	String queryResponse = new String();
	//QueryParser3_0 qp = new QueryParser3_0();
	try {
		queryResponse = QueryParser3_0.parseFullNodeQuery(query);
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return queryResponse;
  }
}

