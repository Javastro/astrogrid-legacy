/*
 * Created on 25-Apr-2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.astrogrid.registry.server.query;

import org.astrogrid.registry.server.QueryParser3_0;
import java.rmi.RemoteException; 

import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.apache.axis.utils.XMLUtils;
import org.w3c.dom.Element;
import java.io.Reader;
import java.io.StringReader;
import org.xml.sax.InputSource;


/**
 * 
 * The RegistryService class is a web service that submits an XML formatted
 * registry query to the QueryParser class.
 * This delegate helps the user browse the registry.  Queries should be formatted according to
 * the schema at IVOA schema version 0.9.  This class also uses the common RegistryInterface for
 * knowing the web service methods to call on the server side.
 * 
 * @see org.astrogrid.registry.RegistryInterface
 * @link http://www.ivoa.net/twiki/bin/view/IVOA/IVOARegWp03
 * @author Kevin Benson
 */
public class RegistryService implements org.astrogrid.registry.RegistryInterface {

   /**
   * submitQuery queries the registry with the same XML document used as fullNodeQuery, but
   * the response comes back in a different record key pair XML formatted document object.
   * Current implementation uses the fullNodeQuery.  fullNodeQuery may be deprecated at a
   * later date and this method reestablished as the main method to use.
   * 
   * @param query XML document object representing the query language used on the registry.
   * @return XML docuemnt object representing the result of the query.
   * @deprecated Being deprecated this method now only returns the full XML document.
   * @author Kevin Benson 
   */
   public Document submitQuery(Document query) throws Exception {
    String queryResponse = new String();
    Document registryDoc = null;
    try {
		registryDoc = QueryParser3_0.parseQuery(query);
      System.out.println("the registryDoc = " + XMLUtils.DocumentToString(registryDoc));
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   return registryDoc;
	//return query;
  }
  
  /**
    * fullNodeQuery queries the registry with the a XML docuemnt object, and returns the results
    * in a XML Document object query.
    * 
    * @param query XML document object representing the query language used on the registry.
    * @return XML docuemnt object representing the result of the query.
    * @deprecated Being deprecated this method now only returns the full XML document.
    * @author Kevin Benson 
    */  
  public Document fullNodeQuery(Document query) throws Exception {
	String queryResponse = new String();
   Document registryDoc = null;

	try {
      //Go query the registry.
		registryDoc = QueryParser3_0.parseFullNodeQuery(query);
      System.out.println("the registryDoc = " + XMLUtils.DocumentToString(registryDoc));
   } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
   }
   //return the response
   return registryDoc;

  }
  
}