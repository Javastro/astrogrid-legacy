package org.astrogrid.registry.server.query;



import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import java.io.Reader;
import java.io.StringReader;
import org.xml.sax.InputSource;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import java.io.IOException;
import org.astrogrid.util.DomHelper;
import org.astrogrid.config.Config;
import org.astrogrid.registry.server.XQueryExecution;

/**
 * 
 * The RegistryService class is a web service that submits an XML formatted
 * registry query to the QueryParser class.
 * This delegate helps the user browse the registry.  Queries should be formatted according to
 * the schema at IVOA schema version 0.9.  This class also uses the common RegistryInterface for
 * knowing the web service methods to call on the server side.
 * 
 * @see org.astrogrid.registry.common.RegistryInterface
 * @link http://www.ivoa.net/twiki/bin/view/IVOA/IVOARegWp03
 * @author Kevin Benson
 */
public class RegistryService implements
                             org.astrogrid.registry.common.RegistryInterface {


   public static Config conf = null;
   
   private static final String AUTHORITYID_PROPERTY = "org.astrogrid.registry.authorityid";
   
   static {
      if(conf == null) {
         conf = org.astrogrid.config.SimpleConfig.getSingleton();
      }      
   }


   /**
   * submitQuery queries the registry with the same XML document used as fullNodeQuery, but
   * the response comes back in a different record key pair XML formatted document object.
   * Current implementation uses the fullNodeQuery.  fullNodeQuery may be deprecated at a
   * later date and this method reestablished as the main method to use.
   * 
   * @param query XML document object representing the query language used on the registry.
   * @return XML docuemnt object representing the result of the query.
   * @author Kevin Benson 
   */
   public Document submitQuery(Document query) {
      Document registryDoc = null;
      System.out.println("received = " + DomHelper.DocumentToString(query));    
      //try {
   		registryDoc = XQueryExecution.parseQuery(query);
         if(registryDoc != null)
            System.out.println("the registryDoc = " + DomHelper.DocumentToString(registryDoc));
   	//} catch (ClassNotFoundException e) {
   		// TODO Auto-generated catch block
   	//	e.printStackTrace();
   	//}
      return registryDoc;
   }
   
   public Document loadRegistry(Document query) {
      //System.out.println("received = " + XMLUtils.DocumentToString(query));
      String authorityID = conf.getString(AUTHORITYID_PROPERTY);
      authorityID = authorityID.trim();
      Document doc = null;
      Document responseDoc = null;
      
      String selectQuery = "<query><selectionSequence>" +
      "<selection item='searchElements' itemOp='EQ' value='Resource'/>" +
      "<selectionOp op='$and$'/>" +
      "<selection item='AuthorityID' itemOp='EQ' value='" + authorityID + "'/>" +
      "<selectionOp op='AND'/>" +
      "<selection item='@*:type' itemOp='EQ' value='RegistryType'/>"  +
      "</selectionSequence></query>";
      
      try {
         Reader reader2 = new StringReader(selectQuery);
         InputSource inputSource = new InputSource(reader2);
         DocumentBuilder registryBuilder = null;
         registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         doc = registryBuilder.parse(inputSource);
      }catch(ParserConfigurationException pce) {
         pce.printStackTrace();
      }catch(IOException ioe) {
         ioe.printStackTrace();
      }catch(SAXException sax) {
         sax.printStackTrace();
      }
      
      if(doc != null) {
         responseDoc = XQueryExecution.parseQuery(doc);
      }
      return responseDoc;
   }
}