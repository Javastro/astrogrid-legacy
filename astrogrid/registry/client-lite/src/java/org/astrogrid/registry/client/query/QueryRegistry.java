package org.astrogrid.registry.client.query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.URL;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.message.SOAPBodyElement;
import org.apache.axis.utils.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.Reader;
import java.io.StringReader;
import org.xml.sax.InputSource;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Calendar;
import java.util.List;
import java.util.Iterator;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.net.MalformedURLException;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.common.XSLHelper;
import org.astrogrid.registry.common.InterfaceType;

import org.astrogrid.util.DomHelper;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import javax.wsdl.xml.WSDLReader;
import javax.wsdl.*;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.soap.SOAPAddress;

import org.xml.sax.SAXException;
import java.rmi.RemoteException;

import org.astrogrid.registry.common.WSDLBasicInformation;

import javax.wsdl.factory.WSDLFactory;

import org.astrogrid.config.Config;
import org.astrogrid.store.Ivorn;

/**
 * 
 * The QueryRegistry class is a delegate to a web service that submits an XML formatted
 * registry query to the to the server side web service also named the same RegistryService.
 * This delegate helps the user browse the registry and also the OAI. 
 * 
 * @see org.astrogrid.registry.common.RegistryInterface
 * @link http://www.ivoa.net/twiki/bin/view/IVOA/IVOARegWp03
 * @author Kevin Benson
 */
public class QueryRegistry implements RegistryService {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(QueryRegistry.class);

   /**
    * target end point is the location of the webservice. 
    */
   private URL endPoint = null;

   private boolean useCache = false;

   private static final String NAMESPACE_URI =
      "http://www.ivoa.net/schemas/services/QueryRegistry/wsdl";

   private static final String QUERY_URL_PROPERTY =
      "org.astrogrid.registry.query.endpoint";

   public static Config conf = null;

   //@todo don't think it's necessary to hang onto this object
   static {
      if (conf == null) {
         conf = org.astrogrid.config.SimpleConfig.getSingleton();
      }
   }

   /**
    * @todo - I think this is almost cyclic. 
    * Empty constructor that defaults the end point to local host.
    * @author Kevin Benson
    */
   public QueryRegistry() {
      this(conf.getUrl(org.astrogrid.registry.client.RegistryDelegateFactory.QUERY_URL_PROPERTY,null));
   }
   

   /**
    * Main constructor to allocate the endPoint variable.
    * @param endPoint location to the web service.
    * @author Kevin Benson
    */
   public QueryRegistry(URL endPoint) {
       
        logger
                .info("QueryRegistry(URL) - entered const(url) of RegistryService");
      this.endPoint = endPoint;
      if (this.endPoint == null) {
          logger.warn("endpoint is null, using cache");
         useCache = true;
      }
       
        logger
                .info("QueryRegistry(URL) - exiting const(url) of RegistryService");
   }

   /**
    * Method to establish a Service and a Call to the server side web service.
    * @return Call object which has the necessary properties set for an Axis message style.
    * @throws Exception
    * @todo there's code similar to this in eac of the delegate classes. could it be moved into a common baseclass / helper class.
    * @author Kevin Benson
    */
   private Call getCall() throws ServiceException {
       
      logger.info("getCall() - entered getCall()");
      Call _call = null;
      Service service = new Service();
      _call = (Call)service.createCall();
      _call.setTargetEndpointAddress(this.endPoint);
      _call.setSOAPActionURI("");
      _call.setOperationStyle(org.apache.axis.enum.Style.MESSAGE);
      _call.setOperationUse(org.apache.axis.enum.Use.LITERAL);
      _call.setEncodingStyle(null);
      return _call;
   }

   /**
    * To perform a query with ADQL, using adqls.
    * @param adql string form of adqls
    * @
    * @return XML DOM of Resources queried from the registry.
    * @todo throw registry exception until this method is implemented.
    * @throws RegistryException problem during the query servor or client side.
    */
   public Document searchFromSADQL(String adql) throws RegistryException {
      //send to sadql->adql parser.
      //call return search(adql);
      throw new RegistryException("No implementation for adqls.");
   }

   /**
    * To perform a query with ADQL, using adql string.
    * @param adql string form of adql (xml)
    * @return XML DOM of Resources queried from the registry.
    * @throws RegistryException problem during the query servor or client side.
    */   
   public Document search(String xadql) throws RegistryException {
      //search using adqlx. Catch any exceptions and throw them as RegistryExceptions
      try {
         return search(DomHelper.newDocument(xadql));
      } catch (ParserConfigurationException pce) {
         throw new RegistryException(pce);
      } catch (IOException ioe) {
         throw new RegistryException(ioe);
      } catch (SAXException sax) {
         throw new RegistryException(sax);
      }
   }

   /**
    * To perform a query on the Registry using a DOM conforming of ADQL.
    * Uses a Axis-Message type style so wrap the DOM in the method name conforming
    * to the WSDL.
    * @param adql string form of adqls
    * @return XML DOM of Resources queried from the registry.
    * @throws RegistryException problem during the query servor or client side.
    */   
   public Document search(Document adql) throws RegistryException {
      //wrap a Search element around the dom.
      Element currentRoot = adql.getDocumentElement();
      Element newRoot = adql.createElementNS(NAMESPACE_URI, "Search");
      newRoot.appendChild(currentRoot);
      adql.appendChild(newRoot);
      try {
         //get a call object
         Call call = getCall();
         //create the soap body to be placed in the
         //outgoing soap message.
         SOAPBodyElement sbeRequest =
            new SOAPBodyElement(adql.getDocumentElement());
         //go ahead and set the name and namespace on the soap body
         //not sure if this is that important.
         sbeRequest.setName("Search");
         sbeRequest.setNamespaceURI(NAMESPACE_URI);
         //call the web service, on axis-message style it
         //comes back as a vector of soabodyelements.
         Vector result = (Vector)call.invoke(new Object[] { sbeRequest });
         SOAPBodyElement sbe = null;
         if (result.size() > 0) {
            sbe = (SOAPBodyElement)result.get(0);
            return sbe.getAsDocument();
         }
      } catch (RemoteException re) {
         throw new RegistryException(re);
      } catch (ServiceException se) {
         throw new RegistryException(se);
      } catch (Exception e) {
         throw new RegistryException(e);
      }
      throw new RegistryException("Server error must have occurred.");
   }
   
   /**
    * Performas a query to return all Resources of a type of Registry.
    * @return XML DOM of Resources queried from the registry.
    * @throws RegistryException problem during the query servor or client side.
    */
   public Document getRegistries() throws RegistryException {
       Document doc = null;
       Document resultDoc = null;

       try {
          logger.info("getRegistries() - creating full soap element.");
          doc = DomHelper.newDocument();
          //@todo GetRegistries should be a constant.
          Element root = doc.createElementNS(NAMESPACE_URI, "GetRegistries");
          doc.appendChild(root);
       } catch (ParserConfigurationException pce) {
          throw new RegistryException(pce);
       }
       
       try {
          Call call = getCall();
          SOAPBodyElement sbeRequest =
             new SOAPBodyElement(doc.getDocumentElement());
          sbeRequest.setName("GetRegistries");
          sbeRequest.setNamespaceURI(NAMESPACE_URI);
          //call the web service, on axis-message style it
          //comes back as a vector of soabodyelements.          
          Vector result = (Vector)call.invoke(new Object[] { sbeRequest });
          SOAPBodyElement sbe = null;
          if (result.size() > 0) {
             sbe = (SOAPBodyElement)result.get(0);
             return sbe.getAsDocument();
          }//if
       } catch (RemoteException re) {
          throw new RegistryException(re);
       } catch (ServiceException se) {
          throw new RegistryException(se);
       } catch (Exception e) {
          throw new RegistryException(e);
       }
       //should not reach here.
       throw new RegistryException("Error from server it returned nothing");
   }
   
   /**
    * Identify - Queryies based on OAI-Identify verb, identifying the repository.
    * @return XML DOM of an OAI-PMH for the Identify. 
    */   
   public Document identify() throws RegistryException {
      Document doc = null;
      Document resultDoc = null;

      try {
         logger.info("identify() - creating full soap element.");
         doc = DomHelper.newDocument();
         Element root = doc.createElementNS(NAMESPACE_URI, "Identify");
         doc.appendChild(root);
      } catch (ParserConfigurationException pce) {
         throw new RegistryException(pce);
      }
      
      try {
         Call call = getCall();
         SOAPBodyElement sbeRequest =
            new SOAPBodyElement(doc.getDocumentElement());
         sbeRequest.setName("Identify");
         sbeRequest.setNamespaceURI(NAMESPACE_URI);
         Vector result = (Vector)call.invoke(new Object[] { sbeRequest });
         SOAPBodyElement sbe = null;
         if (result.size() > 0) {
            sbe = (SOAPBodyElement)result.get(0);
            return sbe.getAsDocument();
         }
      } catch (RemoteException re) {
         throw new RegistryException(re);
      } catch (ServiceException se) {
         throw new RegistryException(se);
      } catch (Exception e) {
         throw new RegistryException(e);
      }
      //should not reach here.
      throw new RegistryException("Error from server it returned nothing");
   }
   

   /**
    * ListRecords - OAI ListRecords query, the Registry server will default the
    * metadataPrefix to ivo_vor. 
    * @return XML DOM of an OAI-PMH for the ListRecords. 
    */
   public Document listRecords() throws RegistryException {
   	return listRecords(null,null,null);
   }
   
   /**
    * ListRecords - OAI ListRecords query based on a fromDate, the recods
    * changed from that date The Registry server will default the
    * metadataPrefix to ivo_vor
    * @param fromDate - A from date for returning Resources from a date till now. 
    * @return XML DOM of an OAI-PMH for the ListRecords. 
    */
   public Document listRecords(Date fromDate) throws RegistryException {
   	return listRecords(null,fromDate,null);    
   }
   
   /**
    * ListRecords - OAI ListRecords query. This will be the most used OAI verb for harvesting.
    * @param metadataPrefix - oai metadataPrefix string normally ivo_vor or oai_dc. 
    * A null will let the Registry server default it to ivo_vor. 
    * @param fromDate - A from date for returning Resources from a date till now. 
    * @param untilDate - Returning resources from the beginning till a date. 
    * @return XML DOM of an OAI-PMH for the ListRecords. 
    */
   public Document listRecords(String metadataPrefix, Date fromDate, Date untilDate) throws RegistryException {
      Document doc = null;
      Document resultDoc = null;

      try {
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
          
         logger
               .info("listRecords(String, Date, Date) - creating full soap element.");
         doc = DomHelper.newDocument();
         Element root = doc.createElementNS(NAMESPACE_URI, "ListRecords");
         doc.appendChild(root);
         Element temp = null;
         //Create the other xml elements in the soap body if they are present
         if(metadataPrefix != null && metadataPrefix.trim().length() > 0) {
         	temp = doc.createElement("metadataPrefix");
            temp.appendChild(doc.createTextNode(metadataPrefix));
            root.appendChild(temp);    
         }         
         if(fromDate != null) {
            temp = doc.createElement("from");
            temp.appendChild(doc.createTextNode(sdf.format(fromDate)));
            root.appendChild(temp);
         }
         if(untilDate != null) {
            temp = doc.createElement("until");
            temp.appendChild(doc.createTextNode(sdf.format(untilDate)));
            root.appendChild(temp);            
         }
      } catch (ParserConfigurationException pce) {
         throw new RegistryException(pce);
      }
      
      try {
         Call call = getCall();
         SOAPBodyElement sbeRequest =
            new SOAPBodyElement(doc.getDocumentElement());
         sbeRequest.setName("ListRecords");
         sbeRequest.setNamespaceURI(NAMESPACE_URI);
         logger
                .info("listRecords(String, Date, Date) - List Records Client-side = "
                        + DomHelper.DocumentToString(doc));
         Vector result = (Vector)call.invoke(new Object[] { sbeRequest });
         SOAPBodyElement sbe = null;
         if (result.size() > 0) {
            sbe = (SOAPBodyElement)result.get(0);
            return sbe.getAsDocument();
         }
      } catch (RemoteException re) {
         throw new RegistryException(re);
      } catch (ServiceException se) {
         throw new RegistryException(se);
      } catch (Exception e) {
         throw new RegistryException(e);
      }
      //should not reach here.
      throw new RegistryException("Error from server it returned nothing");
   }
   
   /**
    * ListMetadataFormats - OAI ListMetadtaFormats verb call.  With an optional
    * identifier string to list the metadata formats for a particular id.
    * @return XML DOM of an OAI-PMH for the ListMetadataFormats. 
    */
   public Document listMetadataFormats(String identifier) throws RegistryException {
      Document doc = null;
      Document resultDoc = null;

      try {
          
         logger
              .info("listMetadataFormats(String) - creating full soap element.");
         doc = DomHelper.newDocument();
         Element root = doc.createElementNS(NAMESPACE_URI, "ListMetadataFormats");
         doc.appendChild(root);
         if(identifier != null || identifier.trim().length() > 0) {          
             Element temp = doc.createElement("identifier");
             temp.appendChild(doc.createTextNode(identifier));
             root.appendChild(temp);
         }
      } catch (ParserConfigurationException pce) {
         throw new RegistryException(pce);
      }
      
      try {
         Call call = getCall();
         SOAPBodyElement sbeRequest =
            new SOAPBodyElement(doc.getDocumentElement());
         sbeRequest.setName("ListMetadataFormats");
         sbeRequest.setNamespaceURI(NAMESPACE_URI);
         Vector result = (Vector)call.invoke(new Object[] { sbeRequest });
         SOAPBodyElement sbe = null;
         if (result.size() > 0) {
            sbe = (SOAPBodyElement)result.get(0);
            return sbe.getAsDocument();
         }
      } catch (RemoteException re) {
         throw new RegistryException(re);
      } catch (ServiceException se) {
         throw new RegistryException(se);
      } catch (Exception e) {
         throw new RegistryException(e);
      }
      //should not reach here.
      throw new RegistryException("Error from server it returned nothing");    
   }
   
   /**
    * OAI - Get a specific record from OAI given an identifier. 
    * Defaults the metadataPrefix to ivo_vor.
    * @param identifier for a particular record ex: ivo_vor://astrogrid.org/Registry
    * 
    * @return XML DOM of an OAI-PMH for the GetRecord. 
    */
   public Document getRecord(String identifier) throws RegistryException {
   	return getRecord(identifier,null);
   }
   
   /**
    * OAI - Get a specefic record for an identifier and metadataprefix
    * @param identifier for a particular record ex: ivo_vor://astrogrid.org/Registry
    * @param metadataPrefix is the oai prefix/id to be used, currently only ivo_vor and oai_dc. 
    * @return XML DOM of an OAI-PMH for the GetRecord. 
    */
   public Document getRecord(String identifier, String metadataPrefix) throws RegistryException {
      Document doc = null;
      Document resultDoc = null;

      try {
         logger
              .info("getRecord(String, String) - creating full soap element.");
         doc = DomHelper.newDocument();
         Element root = doc.createElementNS(NAMESPACE_URI, "GetRecord");
         doc.appendChild(root);
         Element temp = null;
         if(identifier == null || identifier.trim().length() <= 0) 
            throw new RegistryException("Error From Client: No identifier found for calling GetRecord");
         
         temp = doc.createElement("identifier");
         temp.appendChild(doc.createTextNode(identifier));
         root.appendChild(temp);    

         if(metadataPrefix != null && metadataPrefix.trim().length() > 0) {
            temp = doc.createElement("metadataPrefix");
            temp.appendChild(doc.createTextNode(metadataPrefix));
            root.appendChild(temp);    
         }         
 
      } catch (ParserConfigurationException pce) {
         throw new RegistryException(pce);
      }
      
      try {
         Call call = getCall();
         SOAPBodyElement sbeRequest =
            new SOAPBodyElement(doc.getDocumentElement());
         sbeRequest.setName("GetRecord");
         sbeRequest.setNamespaceURI(NAMESPACE_URI);
         Vector result = (Vector)call.invoke(new Object[] { sbeRequest });
         SOAPBodyElement sbe = null;
         if (result.size() > 0) {
            sbe = (SOAPBodyElement)result.get(0);
            return sbe.getAsDocument();
         }
      } catch (RemoteException re) {
         throw new RegistryException(re);
      } catch (ServiceException se) {
         throw new RegistryException(se);
      } catch (Exception e) {
         throw new RegistryException(e);
      }
      //should not reach here.
      throw new RegistryException("Error from server it returned nothing");
   }
   
   /**
    * OAI - ListIdentifiers call, similiar to ListRecords but only returns
    * the identifiers (unique ids) for the records. Defaults the metadataPrefix to
    * ivo_vor.
    * 
    * @return XML DOM of an OAI-PMH for the ListIdentifiers. 
    */
   public Document listIdentifiers() throws RegistryException {
   	return listIdentifiers(null,null,null);
   }
   
   /**
    * OAI - ListIdentifiers call, similiar to ListRecords but only returns
    * the identifiers (unique ids) for the records. Defaults the metadataPrefix to
    * ivo_vor.
    * @param metadataPrefix the oai prefix; normally ivo_vor.  Also available is oai_dc.
    * @param fromDate - A from date for returning Resources from a date till now. 
    * @param untilDate - Returning resources from the beginning till a date.
    * @return XML DOM of an OAI-PMH for the ListIdentifiers. 
    */
   public Document listIdentifiers(String metadataPrefix, Date fromDate, Date untilDate) throws RegistryException {
      Document doc = null;
      Document resultDoc = null;

      try {
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
          
         logger
              .info("listIdentifiers(String, Date, Date) - creating full soap element.");
         doc = DomHelper.newDocument();
         Element root = doc.createElementNS(NAMESPACE_URI, "ListIdentifiers");
         doc.appendChild(root);
         Element temp = null;
         if(metadataPrefix != null && metadataPrefix.trim().length() > 0) {
            temp = doc.createElement("metadataPrefix");
            temp.appendChild(doc.createTextNode(metadataPrefix));
            root.appendChild(temp);    
         }         
         if(fromDate != null) {
            temp = doc.createElement("from");
            temp.appendChild(doc.createTextNode(sdf.format(fromDate)));
            root.appendChild(temp);
         }
         if(untilDate != null) {
            temp = doc.createElement("until");
            temp.appendChild(doc.createTextNode(sdf.format(untilDate)));
            root.appendChild(temp);            
         }
      } catch (ParserConfigurationException pce) {
         throw new RegistryException(pce);
      }
      
      try {
         Call call = getCall();
         SOAPBodyElement sbeRequest =
            new SOAPBodyElement(doc.getDocumentElement());
         sbeRequest.setName("ListIdentifiers");
         sbeRequest.setNamespaceURI(NAMESPACE_URI);
         Vector result = (Vector)call.invoke(new Object[] { sbeRequest });
         SOAPBodyElement sbe = null;
         if (result.size() > 0) {
            sbe = (SOAPBodyElement)result.get(0);
            return sbe.getAsDocument();
         }
      } catch (RemoteException re) {
         throw new RegistryException(re);
      } catch (ServiceException se) {
         throw new RegistryException(se);
      } catch (Exception e) {
         throw new RegistryException(e);
      }
      //should not reach here.
      throw new RegistryException("Error from server it returned nothing");
   }   

   /**
    * Old style xml in string form to perform a query. To be deprecated soon, but currently
    * other astrogrid components use this method.  Created before the standard of ADQL.
    * @param the xml string version of the old style astrogrid query language
    * for the registry..
    * @return XML DOM of Resources queried from the registry. 
    */
   public Document submitQuery(String query) throws RegistryException {
       
      logger.info("submitQuery(String) - entered submitQueryStringDOM()");
      try {
         return submitQuery(DomHelper.newDocument(query));
      } catch (ParserConfigurationException pce) {
         throw new RegistryException(pce);
      } catch (IOException ioe) {
         throw new RegistryException(ioe);
      } catch (SAXException sax) {
         throw new RegistryException(sax);
      }
   }

   /**
    * Old style form to perform a query.
    * @param xml version of the old style astrogrid query language.
    * @return XML DOM of Resources queried from the registry. 
    */
   public Document submitQuery(Document query) throws RegistryException {
       
      logger.info("submitQuery(Document) - entered submitQueryDOM()");
      Document doc = null;
      Document resultDoc = null;

      try {
         logger.info("submitQuery(Document) - creating full soap element.");
         doc = DomHelper.newDocument();
         Element root = doc.createElementNS(NAMESPACE_URI, "submitQuery");
         doc.appendChild(root);
         Node nd = doc.importNode(query.getDocumentElement(), true);
         root.appendChild(nd);
      } catch (ParserConfigurationException pce) {
         throw new RegistryException(pce);
      }

      try {
         Call call = getCall();
         SOAPBodyElement sbeRequest =
            new SOAPBodyElement(doc.getDocumentElement());
         sbeRequest.setName("submitQuery");
         sbeRequest.setNamespaceURI(NAMESPACE_URI);
         Vector result = (Vector)call.invoke(new Object[] { sbeRequest });
         SOAPBodyElement sbe = null;
         if (result.size() > 0) {
            sbe = (SOAPBodyElement)result.get(0);
            return sbe.getAsDocument();
         }else {
             return DomHelper.newDocument();
         }
      } catch (RemoteException re) {
         throw new RegistryException(re);
      } catch (ServiceException se) {
         throw new RegistryException(se);
      } catch (Exception e) {
         throw new RegistryException(e);
      }
   }

   /**
    * Loads this registry type resource for the registry. Essentially querying
    * for one resource that defines the Registry.
    * 
    * @return XML DOM of Resources queried from the registry. 
    */
   public Document loadRegistry() throws RegistryException {
       
        logger.info("loadRegistry() - loadRegistry");
      Document doc = null;
      Document resultDoc = null;
      try {

         DocumentBuilder registryBuilder = null;
         registryBuilder =
            DocumentBuilderFactory.newInstance().newDocumentBuilder();
         doc = registryBuilder.newDocument();
         Element root = doc.createElementNS(NAMESPACE_URI, "loadRegistry");
         doc.appendChild(root);
      } catch (ParserConfigurationException pce) {
         throw new RegistryException(pce);
      }
      try {
         Call call = getCall();
         SOAPBodyElement sbeRequest =
            new SOAPBodyElement(doc.getDocumentElement());
         sbeRequest.setName("loadRegistry");
         sbeRequest.setNamespaceURI(NAMESPACE_URI);

         Vector result = (Vector)call.invoke(new Object[] { sbeRequest });
         SOAPBodyElement sbe = null;
         if (result.size() > 0) {
            sbe = (SOAPBodyElement)result.get(0);
            return sbe.getAsDocument();
         }
      } catch (RemoteException re) {
         throw new RegistryException(re);
      } catch (ServiceException se) {
         throw new RegistryException(se);
      } catch (Exception e) {
         throw new RegistryException(e);
      }
      throw new RegistryException("Error server returned nothing");
   }

   /**
    * Queries for all the authorities managed by this registry. By loading this
    * registries main registry resource type and looking for the ManagedAuthority
    * elements, currently does not work with version 0.10.  But is slowly being
    * factored out of use.
    * 
    * @return a hashmap of all the managed authority id's.
    */
   public HashMap managedAuthorities() throws RegistryException {
       
      logger.info("managedAuthorities() - entered managedAuthorities");
      HashMap hm = null;
      Document doc = loadRegistry();
      if (doc != null) {
         //try {
            //System.out.println("the doc in managedAuthorities = " + DomHelper.DocumentToString(doc));
            //NodeList nl = DomHelper.getNodeListTags(doc, "ManagedAuthority", "vg");
            NodeList nl = doc.getElementsByTagNameNS("*","ManagedAuthority");            
            hm = new HashMap();
            for (int i = 0; i < nl.getLength(); i++) {
               hm.put(nl.item(i).getFirstChild().getNodeValue(), null);
            } //for
         //}catch(IOException ioe) {
         //   throw new RegistryException(ioe);   
         //}
      }       
      logger.info("managedAuthorities() - exiting managedAuthorities");
      return hm;
   }

   /**
    * Query for a specific resource in the Registry based on its identifier element(s).
    * Essentially creates a search query "old style astrogrid" for now.  Based on the identifier.
    * 
    * @param identifier IVORN object.
    * @return XML DOM of Resource queried from the registry. 
    * @see org.astrogrid.store.Ivorn
    */
   public Document getResourceByIdentifier(Ivorn ident)
      throws RegistryException {
      if (ident == null) {
         throw new RegistryException("Cannot call this method with a null ivorn identifier");
      }
      return getResourceByIdentifier(ident.getPath());
   }

   /**
    * Query for a specific resource in the Registry based on its identifier element(s).
    * Essentially creates a search query "old style astrogrid" for now.  Based on the identifier.
    * 
    * @param identifier string.
    * @return XML DOM of Resource queried from the registry. 
    */
   public Document getResourceByIdentifier(String ident)
      throws RegistryException {
      Document doc = null;
       
      logger
           .info("getResourceByIdentifier(String) - entered getResourceByIdentifierDOM");
      if (ident == null) {
         throw new RegistryException("Cannot call this method with a null identifier");
      }
       
        logger.info("getResourceByIdentifier(String) - using ident = " + ident);
      if (!useCache) {
         int iTemp = 0;
         iTemp = ident.indexOf("/");
         if (iTemp == -1)
            iTemp = ident.length();
         String selectQuery =
            "<query><selectionSequence>"
               + "<selection item='searchElements' itemOp='EQ' value='all'/>"
               + "<selectionOp op='$and$'/>"
               + "<selection item='vr:Identifier/vr:AuthorityID' itemOp='EQ' value='"
               + ident.substring(0, iTemp)
               + "'/>";
         if (iTemp < ident.length()) {
            selectQuery += "<selectionOp op='AND'/>"
               + "<selection item='vr:Identifier/vr:ResourceKey' itemOp='EQ' value='"
               + ident.substring((iTemp + 1))
               + "'/>";
         }
         selectQuery += "</selectionSequence></query>";
         doc = submitQuery(selectQuery);
         //try {
            //NodeList resultList = DomHelper.getNodeListTags(doc,"Resource","vr");
            NodeList resultList = doc.getElementsByTagNameNS("*","Resource");            
            if(resultList.getLength() == 0) {
               throw new RegistryException("No Resource Found for ident = " + ident);   
            }          
            if(resultList.getLength() > 1) {
               throw new RegistryException("Found more than one Resource for Ident = " + ident);   
            }
         //}catch(IOException ioe) {
         //   throw new RegistryException(ioe);   
         //}         
          
            logger
                 .info("getResourceByIdentifier(String) - exiting getResourceByIdentifierDOM (did not use config cache)");
               
         return doc;
      } else {
          
            logger
                 .info("getResourceByIdentifier(String) - exiting getResourceByIdentifierDOM (used config cache)");
         return conf.getDom(ident);
      }
   }
   
   public URL[] getEndPointByInterfaceType(InterfaceType interfaceType) throws RegistryException {
       ServiceData []sd = getResourcesByInterfaceType(interfaceType);
       URL []serviceURL = new URL[sd.length];
       for(int i = 0;i < sd.length;i++) {
           serviceURL[i] = sd[i].getAccessURL();
       }//for
       return serviceURL;
   }
   
   public ServiceData[] getResourcesByInterfaceType(InterfaceType interfaceType) throws RegistryException  {
      Document doc = null;
      if (interfaceType == null) {
         throw new RegistryException("No interfaceType defined");
      }
      String type = interfaceType.getInterfaceType();
      logger
           .info("getResourcesByInterfaceType(InterfaceType) type - " + type);
       
      String selectQuery =
            "<query><selectionSequence>"
               + "<selection item='searchElements' itemOp='EQ' value='all'/>"
               + "<selectionOp op='$and$'/>"               
               + "<selection item='vr:RelatedResource/vr:Relationship' itemOp='EQ' value='derived-from'/>"
               + "<selectionOp op='AND'/>"
               + "<selection item='vr:RelatedResource/vr:RelatedTo/vr:Identifier/vr:ResourceKey' itemOp='EQ' value='"
               + type
               + "'/>";
         selectQuery += "</selectionSequence></query>";
         doc = submitQuery(selectQuery);          
         logger
             .info("getResourcesByInterfaceType(InterfaceType) - exiting getResourcesByInterfaceType");
         
         return createServiceData(doc);
   }
   
   private ServiceData[] createServiceData(Document doc) {
       NodeList nl = doc.getElementsByTagNameNS("*","Resource");
       ServiceData[] sd = new ServiceData[nl.getLength()];
       NodeList serviceNodes = null;
       String authority = null;
       String resKey = null;
       for(int i = 0;i < nl.getLength(); i++) {
           sd[i] = new ServiceData();
           serviceNodes = ((Element)nl.item(i)).getElementsByTagNameNS("*","Title");
           if(serviceNodes.getLength() > 0)
               sd[i].setTitle(DomHelper.getValue((Element)serviceNodes.item(0)));
           serviceNodes = ((Element)nl.item(i)).getElementsByTagNameNS("*","Description");
           if(serviceNodes.getLength() > 0)
               sd[i].setDescription(DomHelper.getValue((Element)serviceNodes.item(0)));
           serviceNodes = ((Element)nl.item(i)).getElementsByTagNameNS("*","AccessURL");
           if(serviceNodes.getLength() > 0) {
               try {
                   sd[i].setAccessURL(new URL(DomHelper.getValue((Element)serviceNodes.item(0))));
               }catch(MalformedURLException mfe) {
                   logger.error(mfe);
               }
           }
           serviceNodes = ((Element)nl.item(i)).getElementsByTagNameNS("*","AuthorityID");
           if(serviceNodes.getLength() > 0) {
               authority = DomHelper.getValue((Element)serviceNodes.item(0));
               serviceNodes = ((Element)nl.item(i)).getElementsByTagNameNS("*","ResourceKey");
               resKey = DomHelper.getValue((Element)serviceNodes.item(0));
               if(resKey != null)
                   sd[i].setIvorn(new Ivorn(authority, resKey,null));
               else
                   sd[i].setIvorn(new Ivorn(authority, null, null));
           }//if
           //lets skip getting all the interface types for now.
       }//fors
       return sd;
   }

   /**
    * Query for a specific resource in the Registry based on its identifier element(s), Then extracts out
    * the AccessURL element to find the endpoint. If the endpoint is a web service and has a "?wsdl" ending
    * then attempts to parse the wsdl for the end point of the service.
    * 
    * @param ivorn object.
    * @see org.astrogrid.store.Ivorn 
    * @return String of a url. 
    */
   public String getEndPointByIdentifier(Ivorn ident)
      throws RegistryException {
      return getEndPointByIdentifier(ident.getPath());
   }

   /**
    * Query for a specific resource in the Registry based on its identifier element(s), Then extracts out
    * the AccessURL element to find the endpoint. If the endpoint is a web service and has a "?wsdl" ending
    * then attempts to parse the wsdl for the end point of the service.
    * 
    * @param string identifer of the resource. 
    * @return String of a url. 
    */
   public String getEndPointByIdentifier(String ident)
      throws RegistryException {
       
        logger
             .info("getEndPointByIdentifier(String) - entered getEndPointByIdentifier with ident = "
                    + ident);
      //check for an AccessURL
      //if AccessURL is their and it is a web service then get the wsdl
      //into a DOM object and run an XSL on it to get the endpoint.
      String returnVal, invocation = null;
      Document doc = getResourceByIdentifier(ident);
      try {
         returnVal = DomHelper.getNodeTextValue(doc, "AccessURL", "vr");
         invocation = DomHelper.getNodeTextValue(doc, "Invocation", "vr");
      } catch (IOException ioe) {
         throw new RegistryException("Could not parse xml to get AcessURL or Invocation");
      }
      if (returnVal == null) {
         throw new RegistryException("Found Resource Document, but had no AccessURL");
      }
    logger.info("getEndPointByIdentifier(String) - The AccessURL = "
            + returnVal);
    logger.info("getEndPointByIdentifier(String) - The Invocation = "
            + invocation);
      if (returnVal != null
         && returnVal.indexOf("wsdl") > 0
         && "WebService".equals(invocation)) {
          logger.info("getEndPointByIdentifier(String) - has ?wsdl stripping off");
         returnVal = returnVal.substring(0,returnVal.indexOf("?wsdl"));
      }
      return returnVal;
   }

   /**
    * Query for a specific resource in the Registry based on its identifier element(s), Then extracts out
    * the AccessURL element to find the endpoint. If the endpoint is a web service and has a "?wsdl" ending
    * then attempts to parse the wsdl to obtain certain information such as endpoints, and port names.
    * 
    * @param string identifer of the resource.
    * @see org.astrogrid.store.Ivorn
    * @see org.astrogrid.registry.common.WSDLBasicInformation
    * @return String of a url. 
    * @deprecated no longer in use.
    */
   public WSDLBasicInformation getBasicWSDLInformation(Ivorn ident)
      throws RegistryException {
      return getBasicWSDLInformation(getResourceByIdentifier(ident));
   }

   /**
    * Query for a specific resource in the Registry based on its identifier element(s), Then extracts out
    * the AccessURL element to find the endpoint. If the endpoint is a web service and has a "?wsdl" ending
    * then attempts to parse the wsdl to obtain certain information such as endpoints, and port names.
    * 
    * @param string identifer of the resource.
    * @see org.astrogrid.store.Ivorn
    * @see org.astrogrid.registry.common.WSDLBasicInformation
    * @return String of a url. 
    * @deprecated there is no need for this anymore it has been said no ?wsdl, so if we fine
    *   one in the accessurl just strip it off don't go to the wsdl.
    */
   public WSDLBasicInformation getBasicWSDLInformation(Document voDoc)
      throws RegistryException {
      //if(DEBUG_FLAG) System.out.println("entered getBasicWSDLInformation with ident = " + ident);

      //Document voDoc = getResourceByIdentifier(ident);
      WSDLBasicInformation wsdlBasic = null;
      String invocType = null;
      String accessURL = null;
      try {
         invocType = DomHelper.getNodeTextValue(voDoc, "Invocation", "vr");
         accessURL = DomHelper.getNodeTextValue(voDoc, "AccessURL", "vr");
      } catch (IOException ioe) {
         throw new RegistryException("Could not parse xml to get AcessURL or Invocation");
      }
      if ("WebService".equals(invocType)) {
         if (accessURL == null) {
            throw new RegistryException("Cound not find an AccessURL with a web service invocation type");
         }
         try {
             
            logger
                    .info("getBasicWSDLInformation(Document) - status msg for getBasicWSDLInformation, the invocation is a Web service being processing wsdl");
            WSDLFactory wf = WSDLFactory.newInstance();
            WSDLReader wr = wf.newWSDLReader();
            Definition def = wr.readWSDL(accessURL);
            wsdlBasic = new WSDLBasicInformation();
            wsdlBasic.setTargetNameSpace(def.getTargetNamespace());
            Map mp = def.getServices();
            Set serviceSet = mp.keySet();
            Iterator iter = serviceSet.iterator();
            while (iter.hasNext()) {
               //I think this is actually a QName may need to change.
               //String serviceName = (String)iter.next();
               //             javax.wsdl.Service service = (javax.wsdl.Service)mp.get(serviceName);
               QName serviceQName = (QName)iter.next();
               javax.wsdl.Service service =
                  (javax.wsdl.Service)mp.get(serviceQName);
               Set portSet = service.getPorts().keySet();
               Iterator portIter = portSet.iterator();
               while (portIter.hasNext()) {
                  //Probably also a QName
                  String portName = (String)portIter.next();
                  Port port = (Port)service.getPorts().get(portName);
                  List lst = port.getExtensibilityElements();
                  for (int i = 0; i < lst.size(); i++) {
                     ExtensibilityElement extElement =
                        (ExtensibilityElement)lst.get(i);
                     if (extElement instanceof SOAPAddress) {
                        SOAPAddress soapAddress = (SOAPAddress)extElement;
                         
                        logger
                                .info("getBasicWSDLInformation(Document) - status msg for getBasicWSDLInformation, found a LocationURI in the wsdl = "
                                        + soapAddress.getLocationURI());
                        wsdlBasic.addEndPoint(
                           port.getName(),
                           soapAddress.getLocationURI());
                     } //if   
                  } //for                        
               } //while                     
            } //while
         } catch (WSDLException wsdle) {
            logger.error("getBasicWSDLInformation(Document)", wsdle);
            throw new RegistryException(wsdle);
         }
      } else {
         throw new RegistryException("Invalid Entry in Method: This method only accepts WebService InvocationTypes");
      }
       
        logger
                .info("getBasicWSDLInformation(Document) - exiting getBasicWSDLInformation with ident");
      return wsdlBasic;
   }
}