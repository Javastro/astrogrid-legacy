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
import java.net.URL;

import java.net.MalformedURLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.io.*;
import org.apache.axis.AxisFault;
import org.astrogrid.xmldb.eXist.server.QueryDBService;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.server.harvest.RegistryHarvestService;

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
      //instead of xsl maybe we should try to replace the exist root element then add the searchResponse?
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
   
   private Document queryOAI(String oaiServlet) throws AxisFault {
      try {
          System.out.println("the oaiservlet url = '" + oaiServlet + "'");
        return DomHelper.newDocument(new URL(oaiServlet));
       }catch(MalformedURLException me) {
        throw new AxisFault("Incorrect url for calling oai servlet", me);
       }catch(ParserConfigurationException pce) {
         throw new AxisFault("Parser Config error", pce);
       }catch(SAXException sax) {
         throw new AxisFault("SAX problem parsing xml" , sax);
       }catch(IOException ioe) {
         throw new AxisFault("IO Problem", ioe);
       }    
   }

   public Document Identify(Document query) throws AxisFault {
      String oaiServlet = conf.getString("oai.servlet.url") + "?verb=Identify";
      return queryOAI(oaiServlet);
   }
    
   public Document ListMetadataFormats(Document query) throws AxisFault {
      String oaiServlet = conf.getString("oai.servlet.url") + "?verb=ListMetadataFormats";       
      NodeList nl = null;
      if( (nl = query.getElementsByTagName("identifier")).getLength() > 0  )
           oaiServlet += "&identifier=" + nl.item(0).getFirstChild().getNodeValue(); 
      return queryOAI(oaiServlet);
   }

   public Document ListSets(Document query) throws AxisFault {
    throw new AxisFault("Sorry but this method is currently not implemented");
   }

   public Document ResumeListSets(Document query) throws AxisFault {
      throw new AxisFault("Sorry but this method is currently not implemented");
   }

   public Document GetRecord(Document query) throws AxisFault {
       String oaiServlet = conf.getString("oai.servlet.url") + "?verb=GetRecord";       
       NodeList nl = null;
       if( (nl = query.getElementsByTagName("identifier")).getLength() > 0  ) 
          oaiServlet += "&identifier=" + nl.item(0).getFirstChild().getNodeValue();
       if( (nl = query.getElementsByTagName("metadataPrefix")).getLength() > 0  )
        oaiServlet += "&metadataPrefix=" + nl.item(0).getFirstChild().getNodeValue();
       else
        oaiServlet += "&metadataPrefix=ivo_vor";
       return queryOAI(oaiServlet);
   }

   public Document ListIdentifiers(Document query) throws AxisFault {
      String oaiServlet = conf.getString("oai.servlet.url") + "?verb=ListIdentifiers";
      NodeList nl = null;      
      if( (nl = query.getElementsByTagName("metadataPrefix")).getLength() > 0  )
       oaiServlet += "&metadataPrefix=" + nl.item(0).getFirstChild().getNodeValue();
      else 
        oaiServlet += "&metadataPrefix=ivo_vor";        
      if( (nl = query.getElementsByTagName("from")).getLength() > 0  ) 
        oaiServlet += "&from=" + nl.item(0).getFirstChild().getNodeValue();
      if( (nl = query.getElementsByTagName("until")).getLength() > 0  )
        oaiServlet += "&until=" + nl.item(0).getFirstChild().getNodeValue();
      return queryOAI(oaiServlet);
   }

    public Document ResumeListIdentifiers(Document query) throws AxisFault {
        String oaiServlet = conf.getString("oai.servlet.url") + "?verb=ListIdentifiers";
        NodeList nl = null;        
        if( (nl = query.getElementsByTagName("resumptionToken")).getLength() > 0  ) 
          oaiServlet += "&resumptionToken=" + nl.item(0).getFirstChild().getNodeValue();
        return queryOAI(oaiServlet);          
   }

    public Document ListRecords(Document query) throws AxisFault {
        String oaiServlet = conf.getString("oai.servlet.url") + "?verb=ListRecords";
        NodeList nl = null;        
        if( (nl = query.getElementsByTagName("metadataPrefix")).getLength() > 0  )
         oaiServlet += "&metadataPrefix=" + nl.item(0).getNodeValue();
        else
         oaiServlet += "&metadataPrefix=ivo_vor";
        if( (nl = query.getElementsByTagName("from")).getLength() > 0  ) 
          oaiServlet += "&from=" + nl.item(0).getFirstChild().getNodeValue();
        if( (nl = query.getElementsByTagName("until")).getLength() > 0  )
          oaiServlet += "&until=" + nl.item(0).getFirstChild().getNodeValue();
        return queryOAI(oaiServlet);
    }

   public Document ResumeListRecords(Document query) throws AxisFault {
       String oaiServlet = conf.getString("oai.servlet.url") + "?verb=ListRecords";
       NodeList nl = null;       
       if( (nl = query.getElementsByTagName("resumptionToken")).getLength() > 0  ) 
         oaiServlet += "&resumptionToken=" + nl.item(0).getFirstChild().getNodeValue();
       return queryOAI(oaiServlet);          
   }   
}