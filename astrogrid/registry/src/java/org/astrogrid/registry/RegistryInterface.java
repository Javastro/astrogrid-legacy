/*
 * Created on 25-Apr-2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package registry;

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
    queryResponse = QueryParser.parseQuery(query);
	return queryResponse;
  }
}
