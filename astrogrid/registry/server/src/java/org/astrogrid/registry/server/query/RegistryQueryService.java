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

   static {
      if(conf == null) {
         conf = org.astrogrid.config.SimpleConfig.getSingleton();
      }
   }
   
   public Document Search(Document query) throws AxisFault {
      log.debug("start Search");
      long beginQ = System.currentTimeMillis();
      XSLHelper xslHelper = new XSLHelper();
      
      String xqlQuery = getQuery(query);
      System.out.println("THE XQLQWUERY = " + xqlQuery);
      String versionNumber = getRegistryVersion(query).replace('.','_');
      System.out.println("the versionnumber = " + versionNumber);
      
      String collectionName = "astrogridv" + versionNumber;
      System.out.println("the collectionName = " + collectionName);

      Document resultDoc = queryExist(xqlQuery,collectionName);
      log.info("Time taken to complete search on server = " +
              (System.currentTimeMillis() - beginQ));
      log.debug("end Search");
      System.out.println("BEFORE THE TRANSFORMATIONS = " + DomHelper.DocumentToString(resultDoc));      
      return xslHelper.transformExistResult((Node)resultDoc,versionNumber,"SearchResponse");
   }

   public Document Select(Document query) throws AxisFault {
      log.debug("start Select");
      long beginQ = System.currentTimeMillis();
      XSLHelper xslHelper = new XSLHelper();
      String versionNumber = getRegistryVersion(query).replace('.','_');      
      String collectionName = "astrogridv" + versionNumber;
      String xqlQuery = getQuery(query);
      Document resultDoc = queryExist(xqlQuery,collectionName);
      log.info("Time taken to complete select on server = " +
              (System.currentTimeMillis() - beginQ));
      log.debug("end select");      
      return xslHelper.transformExistResult((Node)resultDoc,versionNumber,null);
   }
   
   public Document Query(Document query) throws AxisFault {
      log.debug("start Query");
      Document result = null;
      try {
         String xql = DomHelper.getNodeTextValue(query,"XQLString");
         log.debug("end Query");
         result = queryExist(xql,"astrogridv" + conf.getString("org.astrogrid.registry.version"));         
      }catch(IOException ioe) {
         throw new AxisFault("IO problem", ioe);
      }
      return result;
   }

   public Document XQLString(Document query) throws AxisFault {
      log.debug("start XQLString");
      Document result = null;
      try {
         String versionNumber = getRegistryVersion(query).replace('.','_');
         String collectionName = "astrogridv" + versionNumber;          
         String xql = DomHelper.getNodeTextValue(query,"XQLString");
         log.debug("end XQLString");
         result = queryExist(xql,collectionName);
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
      XSLHelper xslHelper = new XSLHelper();
      String versionNumber = getRegistryVersion(query).replace('.','_');
      String collectionName = "astrogridv" + versionNumber;

      //log.info("received = " + DomHelper.DocumentToString(query));
      //parse query right now actually does the query.
      Document resultDoc = queryExist(XQueryExecution.createXQL(query),collectionName);
      log.info("Time taken to complete submitQuery on server = " +
              (System.currentTimeMillis() - beginQ));
      log.debug("end submitQuery");
      
      return xslHelper.transformExistResult((Node)resultDoc,versionNumber,null);
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
      String versionNumber = getRegistryVersion(query).replace('.','_');      
      String collectionName = "astrogridv" + versionNumber;
      System.out.println("xqlstring = " + versionNumber);      
      String xqlString = "//vr:Resource[vr:Identifier/vr:AuthorityID='" + authorityID +
                         "' and @xsi:type='RegistryType']";
      System.out.println("xqlstring = " + xqlString);      
      Document resultDoc = queryExist(xqlString,collectionName);
      XSLHelper xslHelper = new XSLHelper();
      log.info("Time taken to complete loadRegistry on server = " +
              (System.currentTimeMillis() - beginQ));
      log.debug("end loadRegistry");         
      return xslHelper.transformExistResult((Node)resultDoc,versionNumber,null);
   }
   
   private Document queryExist(String xqlString, String collectionName) throws AxisFault {
      log.debug("start queryExist");
      QueryDBService qdb = new QueryDBService();
      return qdb.query(collectionName,xqlString);
   }

   public Document KeywordSearch(Document query) throws AxisFault {
      //DomHelper.DocumentToStream(query,System.out);
      try {
         String keywords = DomHelper.getNodeTextValue(query,"keywords");
         String orValue = DomHelper.getNodeTextValue(query,"orValue");
         String versionNumber = getRegistryVersion(query);
         String collectionName = "astrogridv" + versionNumber;
         boolean orIt = new Boolean(orValue).booleanValue();
         
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
      //DomHelper.DocumentToStream(query,System.out);
      long beginQ = System.currentTimeMillis();
      String versionNumber = getRegistryVersion(query).replace('.','_');      
      String collectionName = "astrogridv" + versionNumber;
      
      //Should declare namespaces, but it is not required so will leave out for now.
      String xqlString = "for $x in //vr:Resource where @xsi:type='RegistryType' return $x";
      Document resultDoc = queryExist(xqlString,collectionName);
      XSLHelper xslHelper = new XSLHelper();
        log.info("Time taken to complete loadRegistry on server = " +
        (System.currentTimeMillis() - beginQ));
        log.debug("end loadRegistry");         
        Document getRegDoc =  xslHelper.transformExistResult((Node)resultDoc,versionNumber,null);
        Element currentRoot = getRegDoc.getDocumentElement();
        Element root = getRegDoc.createElement("GetREgistriesResponse");
        root.appendChild(currentRoot);
        getRegDoc.appendChild(root);
        return getRegDoc;
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
      Document resultDoc = queryOAI(oaiServlet);
      Element currentRoot = resultDoc.getDocumentElement();
      Element root = resultDoc.createElement("IdentifyResponse");
      root.appendChild(currentRoot);
      resultDoc.appendChild(root);
      return resultDoc;
   }
    
   public Document ListMetadataFormats(Document query) throws AxisFault {
      String oaiServlet = conf.getString("oai.servlet.url") + "?verb=ListMetadataFormats";       
      NodeList nl = null;
      if( (nl = query.getElementsByTagName("identifier")).getLength() > 0  )
           oaiServlet += "&identifier=" + nl.item(0).getFirstChild().getNodeValue(); 
      Document resultDoc = queryOAI(oaiServlet);
      Element currentRoot = resultDoc.getDocumentElement();
      Element root = resultDoc.createElement("ListMetadataFormatsResponse");
      root.appendChild(currentRoot);
      resultDoc.appendChild(root);
      return resultDoc;
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
       Document resultDoc = queryOAI(oaiServlet);
       Element currentRoot = resultDoc.getDocumentElement();
       Element root = resultDoc.createElement("GetRecordResponse");
       root.appendChild(currentRoot);
       resultDoc.appendChild(root);
       return resultDoc;
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
      Document resultDoc = queryOAI(oaiServlet);
      Element currentRoot = resultDoc.getDocumentElement();
      Element root = resultDoc.createElement("ListIdentifiersResponse");
      root.appendChild(currentRoot);
      resultDoc.appendChild(root);
      return resultDoc;

   }

    public Document ResumeListIdentifiers(Document query) throws AxisFault {
        String oaiServlet = conf.getString("oai.servlet.url") + "?verb=ListIdentifiers";
        NodeList nl = null;        
        if( (nl = query.getElementsByTagName("resumptionToken")).getLength() > 0  ) 
          oaiServlet += "&resumptionToken=" + nl.item(0).getFirstChild().getNodeValue();
        Document resultDoc = queryOAI(oaiServlet);
        Element currentRoot = resultDoc.getDocumentElement();
        Element root = resultDoc.createElement("ResumeListIdentifiersResponse");
        root.appendChild(currentRoot);
        resultDoc.appendChild(root);
        return resultDoc;
          
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
        Document resultDoc = queryOAI(oaiServlet);
        Element currentRoot = resultDoc.getDocumentElement();
        Element root = resultDoc.createElement("ListRecordsResponse");
        root.appendChild(currentRoot);
        resultDoc.appendChild(root);
        return resultDoc;
    }

   public Document ResumeListRecords(Document query) throws AxisFault {
       String oaiServlet = conf.getString("oai.servlet.url") + "?verb=ListRecords";
       NodeList nl = null;       
       if( (nl = query.getElementsByTagName("resumptionToken")).getLength() > 0  ) 
         oaiServlet += "&resumptionToken=" + nl.item(0).getFirstChild().getNodeValue();
       Document resultDoc = queryOAI(oaiServlet);
       Element currentRoot = resultDoc.getDocumentElement();
       Element root = resultDoc.createElement("ResumeListRecordsResponse");
       root.appendChild(currentRoot);
       resultDoc.appendChild(root);
       return resultDoc;          
   }
   
   private String getQuery(Document query) throws AxisFault {
       XSLHelper xslHelper = new XSLHelper();       
       NodeList nl = null;
       try {
           nl = DomHelper.getNodeListTags(query,"Select");
       }catch(IOException ioe) {
           throw new AxisFault("IOE problem finding Select element", ioe);
       }
       
       String adqlVersion = null;
       if(nl != null && nl.getLength() > 0) {
           adqlVersion = DomHelper.getNodeAttrValue((Element)nl.item(0),"ad","xmlns");
           if(adqlVersion == null || adqlVersion.trim().length() == 0) {
               adqlVersion = DomHelper.getNodeAttrValue((Element)nl.item(0),"xmlns");
           }//if
       }//if
       
       if(adqlVersion == null || adqlVersion.trim().length() == 0) {
           throw new AxisFault("Could not find a version of the ADQL");
       }
       adqlVersion = adqlVersion.substring(adqlVersion.lastIndexOf("v")+1);
       return xslHelper.transformADQLToXQL(query, adqlVersion.replace('.','_'));
   }
   
   private String getRegistryVersion(Document query) {
       String regVersion = null;
       regVersion = DomHelper.getNodeAttrValue((Element)query.getDocumentElement(),"vr","xmlns");       
       if((regVersion == null || regVersion.trim().length() == 0) &&
           query.getDocumentElement().hasChildNodes() &&
           Node.ELEMENT_NODE == query.getDocumentElement().getFirstChild().getNodeType()) {           
          regVersion = DomHelper.getNodeAttrValue(
                 (Element)query.getDocumentElement().getFirstChild(),"vr","xmlns");
          if((regVersion == null || regVersion.trim().length() == 0) &&
             query.getDocumentElement().getFirstChild().hasChildNodes() &&
             Node.ELEMENT_NODE == query.getDocumentElement().getFirstChild().getFirstChild().getNodeType()) {             
             regVersion = DomHelper.getNodeAttrValue(
                     (Element)query.getDocumentElement().getFirstChild().getFirstChild(),"vr","xmlns");             
          }
       }
       
       if(regVersion == null || regVersion.trim().length() == 0) {
           regVersion = conf.getString("org.astrogrid.registry.version");
       }       
       regVersion = regVersion.substring((regVersion.lastIndexOf("v")+1));
       return regVersion;
   }
   
}