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

import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.common.XSLHelper;

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
import org.astrogrid.util.DomHelper;


/**
 * 
 * The RegistryService class is a delegate to a web service that submits an XML formatted
 * registry query to the to the server side web service also named the same RegistryService.
 * This delegate helps the user browse the registry.  Queries should be formatted according to
 * the schema at IVOA schema version 0.9.  This class also uses the common RegistryInterface for
 * knowing the web service methods to call on the server side.
 * 
 * @see org.astrogrid.registry.common.RegistryInterface
 * @link http://www.ivoa.net/twiki/bin/view/IVOA/IVOARegWp03
 * @author Kevin Benson
 */
public interface RegistryService  {


   public Document getRegistries() throws RegistryException;
    
	public Document identify() throws RegistryException;
    
   public Document listRecords() throws RegistryException;
   
   public Document listRecords(Date fromDate) throws RegistryException;
   
   public Document listRecords(String metadataPrefix, Date fromDate, Date untilDate) throws RegistryException;
   
   public Document listMetadataFormats(String identifier) throws RegistryException;
   
   public Document getRecord(String identifier) throws RegistryException;
   
   public Document getRecord(String identifier, String metadataPrefix) throws RegistryException;
   
   public Document listIdentifiers() throws RegistryException;
   
   public Document listIdentifiers(String metadataPrefix, Date fromDate, Date untilDate) throws RegistryException;   
    
   public Document search(String xadql) throws RegistryException;
   
   public Document search(Document adql) throws RegistryException;   
      
   public Document submitQuery(String query) throws RegistryException;
   
   public Document submitQuery(Document query) throws RegistryException;
   
   public Document loadRegistry()  throws RegistryException;
      
   public HashMap managedAuthorities() throws RegistryException;
   
   public Document getResourceByIdentifier(Ivorn ident) throws RegistryException;
   
   public Document getResourceByIdentifier(String ident) throws RegistryException;
         
   public String getEndPointByIdentifier(Ivorn ident) throws RegistryException;
   
   public String getEndPointByIdentifier(String ident) throws RegistryException;

   public WSDLBasicInformation getBasicWSDLInformation(Ivorn ident) throws RegistryException;
   
   public WSDLBasicInformation getBasicWSDLInformation(Document voDoc) throws RegistryException;
   
}