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
 * @author Elizabeth Auden
 * 
 * The RegistryService class is a web service that submits an XML formatted
 * registry query to the QueryParser3_0 class.  This web service allows
 * a user to browse the registry.  Queries should be formatted according to
 * the schema at http://wiki.astrogrid.org/bin/view/Astrogrid/RegistryQuerySchema.
 * 
 * Elizabeth  Auden, 24 October 2003
 */

public class RegistryService implements org.astrogrid.registry.RegistryInterface {

  public Document submitQuery(Document query) throws Exception {
    String queryResponse = new String();
    Document registryDoc = null;

    try {
		queryResponse = QueryParser3_0.parseQuery(query);
      DocumentBuilder registryBuilder = null;
      try {
         
         Reader reader2 = new StringReader(queryResponse);
         InputSource inputSource = new InputSource(reader2);
         
         registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         registryDoc = registryBuilder.parse(inputSource);
         System.out.println("the registryDoc = " + XMLUtils.DocumentToString(registryDoc));
         
      } catch (Exception e){
         registryDoc = null;
      }
      
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   return registryDoc;
	//return query;
  }
  
  public Document fullNodeQuery(Document query) throws Exception {
	String queryResponse = new String();
   Document registryDoc = null;

	try {
		queryResponse = QueryParser3_0.parseFullNodeQuery(query);
      DocumentBuilderFactory registryFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder registryBuilder = null;
      try {
         Reader reader2 = new StringReader(queryResponse);
         InputSource inputSource = new InputSource(reader2);
         
         registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         registryDoc = registryBuilder.parse(inputSource);
         System.out.println("the registryDoc = " + XMLUtils.DocumentToString(registryDoc));
      } catch (Exception e){
         registryDoc = null;
      }
      
   } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
   }
   return registryDoc;

  }
  
}