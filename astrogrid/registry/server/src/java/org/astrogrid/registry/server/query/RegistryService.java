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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 *  
 * 
 * @see org.astrogrid.registry.common.RegistryInterface
 * @link http://www.ivoa.net/twiki/bin/view/IVOA/IVOARegWp03
 * @author Kevin Benson
 */
public class RegistryService implements
                             org.astrogrid.registry.common.RegistryInterface {

   private static final Log log = LogFactory.getLog(RegistryService.class);

   public static Config conf = null;
   
   private static final String AUTHORITYID_PROPERTY = 
                                          "org.astrogrid.registry.authorityid";
   
   static {
      if(conf == null) {
         conf = org.astrogrid.config.SimpleConfig.getSingleton();
      }      
   }


   /**
   * submitQuery queries the registry for Resources.  Currently uses
   * an older xml query language that Astrogrid came up with, but will
   * soon be rarely used for the ADQL version to be the standard for the
   * IVOA.
   * 
   * @param query XML document object representing the query language used on
   *  the registry.
   * @return XML docuemnt object representing the result of the query.
   * @author Kevin Benson 
   */
   public Document submitQuery(Document query) {
      log.debug("start submitQuery");
      long beginQ = System.currentTimeMillis();
      Document registryDoc = null;
      //log.info("received = " + DomHelper.DocumentToString(query));
      //parse query right now actually does the query.    
      registryDoc = XQueryExecution.parseQuery(query);
      if(registryDoc != null)
         log.info("the registryDoc = " + DomHelper.
                                         DocumentToString(registryDoc));
      log.info("Time taken to complete submitQuery on server = " +
               (System.currentTimeMillis() - beginQ));
      log.debug("end submitQuery");
      return registryDoc;
   }
   
   /**
    * Queries for the Registry Resource element that is tied to this Registry.
    * All Astrogrid Registries have one Registry Resource tied to the Registry.
    * Which defines the AuthorityID's it manages and how to access the Registry.
    * 
    * @param query actually normally empty/null
    * @return XML docuemnt object representing the result of the query.
    */
   public Document loadRegistry(Document query) {
      log.debug("start loadRegistry");
      long beginQ = System.currentTimeMillis();
      String authorityID = conf.getString(AUTHORITYID_PROPERTY);
      authorityID = authorityID.trim();
      Document doc = null;
      Document responseDoc = null;
      
      String selectQuery = "<query><selectionSequence>" +
            "<selection item='searchElements' itemOp='EQ' value='Resource'/>" +
            "<selectionOp op='$and$'/>" +
            "<selection item='AuthorityID' itemOp='EQ' value='" +
                authorityID + "'/>" +
            "<selectionOp op='AND'/>" +
            "<selection item='@*:type' itemOp='EQ' value='RegistryType'/>"  +
         "</selectionSequence></query>";
      
      try {
         Reader reader2 = new StringReader(selectQuery);
         InputSource inputSource = new InputSource(reader2);
         DocumentBuilder registryBuilder = null;
         registryBuilder = DocumentBuilderFactory.
                           newInstance().newDocumentBuilder();
         doc = registryBuilder.parse(inputSource);
      }catch(ParserConfigurationException pce) {
         pce.printStackTrace();
         log.error(pce);
      }catch(IOException ioe) {
         ioe.printStackTrace();
         log.error(ioe);
      }catch(SAXException sax) {
         sax.printStackTrace();
         log.error(sax);
      }
      
      if(doc != null) {
         responseDoc = XQueryExecution.parseQuery(doc);
      }
      log.info("Time taken to complete loadRegistry on server = " +
               (System.currentTimeMillis() - beginQ));
      log.debug("end loadRegistry");
      return responseDoc;
   }
}