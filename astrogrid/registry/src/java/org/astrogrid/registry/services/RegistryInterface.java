/*
 * Created on 25-Apr-2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.astrogrid.registry;

import java.sql.SQLException;

/**
 * @author Elizabeth Auden
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class RegistryInterface {

  public String submitQuery(String query) {
    String queryResponse = new String();
    QueryParser qp = new QueryParser();
    try {
		queryResponse = QueryParser.parseQuery(query);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return queryResponse;
  }
}
