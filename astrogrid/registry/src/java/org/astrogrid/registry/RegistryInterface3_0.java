/*
 * Created on 25-Apr-2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package astrogrid.registry.src.java.org.astrogrid.registry;

/**
 * @author Elizabeth Auden
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
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
}

