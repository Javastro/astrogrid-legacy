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
import org.astrogrid.registry.server.XSLHelper;
import java.net.URL;

import java.net.MalformedURLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.io.*;
import org.apache.axis.AxisFault;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.server.RegistryServerHelper;

import java.util.ArrayList;


public class OAIService {
    
    /**
     * Logging variable for writing information to the logs
     */
     private static final Log log = LogFactory.getLog(OAIService.class);

     /**
      * conf - Config variable to access the configuration for the server normally
      * jndi to a config file.
      * @see org.astrogrid.config.Config
      */   
     public static Config conf = null;
     
     /**
      * Static to be used on the initiatian of this class for the config
      */   
     static {
        if(conf == null) {
           conf = org.astrogrid.config.SimpleConfig.getSingleton();
        }
     }

    /**
     * Used by all the OAI required method interfaces to get the OAI
     * conformed Resources from a URL.  This URL is a servlet to query
     * the eXist database and put the XML in a OAI form.  The XML DOM returned
     * are all the Resources managed by this Registry.
     * @param oaiServlet a url string 
     * @return OAI conformed DOM object of all the Resourced managed by this Registry.
     * @throws - AxisFault containing exceptions that might have occurred setting up
     * or querying the registry.
     */
    private Document queryOAI(String oaiServlet) throws AxisFault {
       try {
         log.info("the oaiservlet url = '" + oaiServlet + "'");
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
    
    private String getOAIServletURL(Document query) {
        String versionNumber = RegistryServerHelper.getRegistryVersionFromNode(query);       
        return conf.getString("reg.amend.oaipublish." + versionNumber);       
    }
    
    /**
     * OAI-Identify conformed Web service method.
     * 
     * @param query actually this OAI mehtod requires nothing. 
     * @return XML DOM object conforming to the OAI Identify.
     * @throws - AxisFault containing exceptions that might have occurred
     *  calling the servlet/url.
     * @link http://www.openarchives.org 
     */
    public Document Identify(Document query) throws AxisFault {
       String oaiServlet = getOAIServletURL(query) + "?verb=Identify";
       Document resultDoc = queryOAI(oaiServlet);
       Element currentRoot = resultDoc.getDocumentElement();
       Element root = resultDoc.createElementNS("http://www.astrogrid.org/registry/wsdl","IdentifyResponse");
       root.appendChild(currentRoot);
       resultDoc.appendChild(root);
       return resultDoc;
    }
    
    /**
     * OAI-ListMetadataFormats conformed Web service method.
     * 
     * @param query contains an optional identifier string. 
     * @return XML DOM object conforming to the OAI ListMetadataFormats.
     * @throws - AxisFault containing exceptions that might have occurred
     *  calling the servlet/url.
     * @link http://www.openarchives.org 
     */
    public Document ListMetadataFormats(Document query) throws AxisFault {
       String oaiServlet = getOAIServletURL(query) + "?verb=ListMetadataFormats";       
       NodeList nl = null;
       if( (nl = query.getElementsByTagName("identifier")).getLength() > 0  )
            oaiServlet += "&identifier=" + nl.item(0).getFirstChild().getNodeValue(); 
       Document resultDoc = queryOAI(oaiServlet);
       Element currentRoot = resultDoc.getDocumentElement();
       Element root = resultDoc.createElementNS("http://www.astrogrid.org/registry/wsdl","ListMetadataFormatsResponse");
       root.appendChild(currentRoot);
       resultDoc.appendChild(root);
       return resultDoc;
    }
    
    /**
     * OAI-ListSets conformed Web service method. Currently not implemented.
     * 
     * @param query 
     * @return XML DOM object conforming to the OAI OAI-ListSets.
     * @throws - AxisFault containing exceptions that might have occurred
     *  calling the servlet/url.
     * @link http://www.openarchives.org 
     */   
    public Document ListSets(Document query) throws AxisFault {
     throw new AxisFault("Sorry but this method is currently not implemented");
    }
    
    /**
     * OAI-ResumeListSets conformed Web service method. Currently not implemented.
     * 
     * @param query 
     * @return XML DOM object conforming to the OAI OAI-ResumeListSets.
     * @throws - AxisFault containing exceptions that might have occurred
     *  calling the servlet/url.
     * @link http://www.openarchives.org 
     */   
    public Document ResumeListSets(Document query) throws AxisFault {
       throw new AxisFault("Sorry but this method is currently not implemented");
    }
    
    /**
     * OAI-GetRecord conformed Web service method.
     * 
     * @param query contains an identifier string and metadataPrefix. The prefix
     * is defaulted to the standard registry ivo_vor if not given. 
     * @return XML DOM object conforming to the OAI GetRecord.
     * @throws - AxisFault containing exceptions that might have occurred
     *  calling the servlet/url.
     * @link http://www.openarchives.org 
     */   
    public Document GetRecord(Document query) throws AxisFault {
        String oaiServlet = getOAIServletURL(query) + "?verb=GetRecord";       
        NodeList nl = null;
        if( (nl = query.getElementsByTagName("identifier")).getLength() > 0  ) 
           oaiServlet += "&identifier=" + nl.item(0).getFirstChild().getNodeValue();
        else
           throw new AxisFault("No Identifier given"); 
        if( (nl = query.getElementsByTagName("metadataPrefix")).getLength() > 0  )
         oaiServlet += "&metadataPrefix=" + nl.item(0).getFirstChild().getNodeValue();
        else
         oaiServlet += "&metadataPrefix=ivo_vor";
        Document resultDoc = queryOAI(oaiServlet);
        //wrap it with a response method element.
        Element currentRoot = resultDoc.getDocumentElement();
        Element root = resultDoc.createElementNS("http://www.astrogrid.org/registry/wsdl","GetRecordResponse");
        root.appendChild(currentRoot);
        resultDoc.appendChild(root);
        return resultDoc;
    }
    
    /**
     * OAI-ListIdentifiers conformed Web service method.
     * 
     * @param query contains a metadataPrefix, and optional from and until 
     * @return XML DOM object conforming to the OAI ListIdentifiers.
     * @throws - AxisFault containing exceptions that might have occurred
     *  calling the servlet/url.
     * @link http://www.openarchives.org 
     */   
    public Document ListIdentifiers(Document query) throws AxisFault {
       String oaiServlet = getOAIServletURL(query) + "?verb=ListIdentifiers";
       NodeList nl = null;      
       if( (nl = query.getElementsByTagName("metadataPrefix")).getLength() > 0  )
        oaiServlet += "&metadataPrefix=" + nl.item(0).getFirstChild().getNodeValue();
       else 
         oaiServlet += "&metadataPrefix=ivo_vor";        
       if( (nl = query.getElementsByTagName("from")).getLength() > 0  ) 
         oaiServlet += "&from=" + nl.item(0).getFirstChild().getNodeValue();
       if( (nl = query.getElementsByTagName("until")).getLength() > 0  )
         oaiServlet += "&until=" + nl.item(0).getFirstChild().getNodeValue();
       if( (nl = query.getElementsByTagName("resumtptionToken")).getLength() > 0  )
           oaiServlet += "&resumptionToken=" + nl.item(0).getFirstChild().getNodeValue();       
       Document resultDoc = queryOAI(oaiServlet);
       Element currentRoot = resultDoc.getDocumentElement();
       Element root = resultDoc.createElementNS("http://www.astrogrid.org/registry/wsdl","ListIdentifiersResponse");
       root.appendChild(currentRoot);
       resultDoc.appendChild(root);
       return resultDoc;
    
    }
    
    
    /**
     * OAI-ListRecords conformed Web service method.
     * 
     * @param query contains a metadataPrefix, optional from&until elements. 
     * @return XML DOM object conforming to the OAI ListRecords.
     * @throws - AxisFault containing exceptions that might have occurred
     *  calling the servlet/url.
     * @link http://www.openarchives.org 
     */   
    public Document ListRecords(Document query) throws AxisFault {
         String oaiServlet = getOAIServletURL(query) + "?verb=ListRecords";
         NodeList nl = null;        
         if( (nl = query.getElementsByTagName("metadataPrefix")).getLength() > 0  )
          oaiServlet += "&metadataPrefix=" + nl.item(0).getNodeValue();
         else
          oaiServlet += "&metadataPrefix=ivo_vor";
         if( (nl = query.getElementsByTagName("from")).getLength() > 0  ) 
           oaiServlet += "&from=" + nl.item(0).getFirstChild().getNodeValue();
         if( (nl = query.getElementsByTagName("until")).getLength() > 0  )
           oaiServlet += "&until=" + nl.item(0).getFirstChild().getNodeValue();
         if( (nl = query.getElementsByTagName("resumtptionToken")).getLength() > 0  )
             oaiServlet += "&resumptionToken=" + nl.item(0).getFirstChild().getNodeValue();                
         Document resultDoc = queryOAI(oaiServlet);
         Element currentRoot = resultDoc.getDocumentElement();
         Element root = resultDoc.createElementNS("http://www.astrogrid.org/registry/wsdl","ListRecordsResponse");
         root.appendChild(currentRoot);
         resultDoc.appendChild(root);
         return resultDoc;
    }
    
}