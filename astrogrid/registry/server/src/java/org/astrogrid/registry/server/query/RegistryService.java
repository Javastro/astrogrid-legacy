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
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import java.io.IOException;

import org.astrogrid.config.Config;


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
   * @deprecated Being deprecated this method now only returns the full XML document.
   * @author Kevin Benson 
   */
   public Document submitQuery(Document query) {
      Document registryDoc = null;
      System.out.println("received = " + XMLUtils.DocumentToString(query));    
      try {
   		registryDoc = QueryParser3_0.parseQuery(query);
         System.out.println("the registryDoc = " + XMLUtils.DocumentToString(registryDoc));
   	} catch (ClassNotFoundException e) {
   		// TODO Auto-generated catch block
   		e.printStackTrace();
   	}
      return registryDoc;
   }

/*  
   public Document harvestQuery(Document query) throws Exception {
      System.out.println("received = " + XMLUtils.DocumentToString(query));
      NodeList nl = query.getElementsByTagName("date_since");
      if(nl.getLength() > 1) {
         //throw an error trying to do more dates than expected.
      }
      Document registryDoc = loadRegistry(null);
      NodeList regNL = registryDoc.getElementsByTagName("ManagedAuthority");
      
      
      Node nd = nl.item(0);
      String updateVal = nd.getFirstChild().getNodeValue();
      String selectQuery = "<query><selectionSequence>" +
      "<selection item='searchElements' itemOp='EQ' value='all'/>" +
      "<selectionOp op='$and$'/>" +
      "<selection item='@updated' itemOp='AFTER' value='" + updateVal + "'/>";
      if(regNL.getLength() > 0) {
         selectQuery += "<selectionOp op='AND'/>" + 
         "<selection item='AuthorityID' itemOp='EQ' value='" + regNL.item(0).getFirstChild().getNodeValue() + "'/>";
      }
      for(int i = 1;i < regNL.getLength();i++) {
         selectQuery += "<selectionOp op='OR'/>" +
         "<selection item='AuthorityID' itemOp='EQ' value='" + regNL.item(i).getFirstChild().getNodeValue() + "'/>";
      }
      selectQuery += "</selectionSequence></query>";      
      
      Reader reader2 = new StringReader(selectQuery);
      InputSource inputSource = new InputSource(reader2);
      DocumentBuilder registryBuilder = null;
      registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document doc = registryBuilder.parse(inputSource);
      //System.out.println("the select/harvest query = " + XMLUtils.DocumentToString(doc));
      
      Document responseDoc = QueryParser3_0.parseFullNodeQuery(doc);
      //System.out.println("the responsedoc in harvestQuery = " + XMLUtils.DocumentToString(responseDoc));
      return responseDoc;
   }
  */
   
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
         try {
            responseDoc = QueryParser3_0.parseFullNodeQuery(doc);
         }catch(ClassNotFoundException cfe) {
            responseDoc = null;
            cfe.printStackTrace();
         }
      }
      return responseDoc;
   }
}