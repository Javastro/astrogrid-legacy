package org.astrogrid.registry.server.query;



import org.w3c.dom.*;
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
import org.astrogrid.registry.common.XSLHelper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.io.*;
import org.apache.axis.AxisFault;
import org.astrogrid.xmldb.eXist.server.QueryDBService;

/**
 *
 *
 *
 * @see org.astrogrid.registry.common.RegistryInterface
 * @link http://www.ivoa.net/twiki/bin/view/IVOA/IVOARegWp03
 * @author Kevin Benson
 */
public class RegistryQueryService {

   private static final Log log = LogFactory.getLog(RegistryService.class);

   public static Config conf = null;

   private static final String AUTHORITYID_PROPERTY =
                                          "org.astrogrid.registry.authorityid";

   private static String collectionName = null;
   private static String versionNumber = null;
   static {
      if(conf == null) {
         conf = org.astrogrid.config.SimpleConfig.getSingleton();
         versionNumber = conf.getString("org.astrogrid.registry.version");
         collectionName = "astrogridv" + conf.getString("org.astrogrid.registry.version");         
      }
   }

   public Document Search(Document query) throws AxisFault {
      log.debug("start Search");
      log.debug("start Search");
      XSLHelper xslHelper = new XSLHelper();
      //Needs a searchResponse element wrapped around the results.
      //DomHelper.DocumentToStream(query,System.out);
//      File fi = new File("c:\\testreturn.xml");
      //Do the adql xsl stylesheet here.
      Document doc = queryRegistry("0_73",query);
      Document resultDoc = xslHelper.transformExistResult(versionNumber,true,doc.getDocumentElement());
      System.out.println("the final result of the query is = " + DomHelper.DocumentToString(resultDoc));
      //throw to xsl stylesheet to get rid of the root element 
      //and probably put the SearchResponse element around as well and the VoResources
      log.debug("end Search");
      return doc;
   }

   public Document Select(Document query) throws AxisFault {
      log.debug("start Select");
      XSLHelper xslHelper = new XSLHelper();
      Document doc = queryRegistry("0_73",query);
      Document resultDoc = xslHelper.transformExistResult(versionNumber,false,doc.getDocumentElement());
      log.debug("end Select");
      return resultDoc;
   }
   
   public Document Query(Document query) throws AxisFault {
      log.debug("start Query");
      Document result = null;
      try {
         String xql = DomHelper.getNodeTextValue(query,"XQLString");
         log.debug("end Query");
         result = queryExist(xql);         
      }catch(IOException ioe) {
         throw new AxisFault("IO problem", ioe);
      }finally {
         return result;
      }
   }

   public Document XQLString(Document query) throws AxisFault {
      log.debug("start XQLString");
      Document result = null;
      try {
         String xql = DomHelper.getNodeTextValue(query,"XQLString");
         log.debug("end XQLString");
         result = queryExist(xql);
      }catch(IOException ioe) {
         throw new AxisFault("IO problem", ioe);         
      }finally {
         return result;
      }
   }
   
   /**
   * submitQuery queries the registry for Resources.  Currently uses
   * an older xml query language that Astrogrid came up with, but will
   * soon be rarely used for the ADQL version to be the standard for the
   * IVOA.
   * 
   * @param query XML document object representing the query language used on the registry.
   * @return XML docuemnt object representing the result of the query.
   * @author Kevin Benson 
   */
   public Document submitQuery(Document query) throws AxisFault {
      log.debug("start submitQuery");
      long beginQ = System.currentTimeMillis();
      Document registryDoc = null;
      //log.info("received = " + DomHelper.DocumentToString(query));
      //parse query right now actually does the query.
      registryDoc = queryExist(XQueryExecution.createXQL(query));
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
   public Document loadRegistry(Document query) throws AxisFault {
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
         doc = DomHelper.newDocument(selectQuery);
         return submitQuery(doc);
      }catch(ParserConfigurationException pce) {
         log.error(pce);         
         throw new AxisFault("Parser Configuration problem", pce);
      }catch(IOException ioe) {
         log.error(ioe);
         throw new AxisFault("IO problem", ioe);
      }catch(SAXException sax) {
         log.error(sax);
         throw new AxisFault("Could not parse XML results", sax);         
      }finally {
         log.info("Time taken to complete loadRegistry on server = " +
                  (System.currentTimeMillis() - beginQ));
         log.debug("end loadRegistry");         
      }
   }
   
   private Document queryRegistry(String adqlVersion, Document query) throws AxisFault {
      log.debug("start queryRegistry (converts the adql)");
      XSLHelper xslHelper = new XSLHelper();
      String xqlString = xslHelper.transformADQLToXQL(adqlVersion,query);
      log.info("Transformed ADQL to XQL = " + xqlString);
      
      return queryExist(xqlString);
   }
   
   private Document queryExist(String xqlString) throws AxisFault {
      log.debug("start queryExist");
      QueryDBService qdb = new QueryDBService();
      return qdb.query(collectionName,xqlString);
   }

   public Document KeywordSearch(Document query) throws AxisFault {
      //DomHelper.DocumentToStream(query,System.out);
      try {
         String keywords = DomHelper.getNodeTextValue(query,"keywords");
         String orValue = DomHelper.getNodeTextValue(query,"orValue");
      }catch(IOException ioe) {
         throw new AxisFault("IO problem", ioe);
      }
      
      return query;
   }
   
   public Document keywords(Document query) throws AxisFault {
      //return KeyWor
      return null;
   }

   public Document GetRegistries(Document query) throws AxisFault {
      DomHelper.DocumentToStream(query,System.out);
      //Should declare namespaces, but it is not required so will leave out for now.
      String xqlString = "for $x in //vr:Resource where @xsi:type='RegistryType' return $x";
      return queryExist(xqlString);
   }

   public Document Identify(Document query) {
      DomHelper.DocumentToStream(query,System.out);
      return query;
   }

   public Document ListMetadataFormats(Document query) {
      DomHelper.DocumentToStream(query,System.out);
      return query;
   }

   public Document ListSets(Document query) {
      DomHelper.DocumentToStream(query,System.out);
      return query;
   }

   public Document ResumeListSets(Document query) {
      DomHelper.DocumentToStream(query,System.out);
      return query;
   }

   public Document GetRecord(Document query) {
      DomHelper.DocumentToStream(query,System.out);
      return query;
   }

   public Document ListIdentifiers(Document query) {
      DomHelper.DocumentToStream(query,System.out);
      return query;
   }

   public Document ResumeListIdentifiers(Document query) {
      DomHelper.DocumentToStream(query,System.out);
      return query;
   }

   public Document ListRecords(Document query) {
      DomHelper.DocumentToStream(query,System.out);
      return query;
   }
   
   public Document ResumeListRecords(Document query) {
      DomHelper.DocumentToStream(query,System.out);
      return query;
   }
   

}