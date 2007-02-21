package org.astrogrid.registry.server.query.v0_9;

import java.io.IOException;

import org.astrogrid.registry.server.SOAPFaultException;
import org.astrogrid.registry.server.query.DefaultQueryService;
import org.astrogrid.registry.server.query.ISearch;
import org.astrogrid.util.DomHelper;

import org.codehaus.xfire.util.STAXUtils;
import java.io.StringReader;

import org.astrogrid.registry.common.RegistryValidator;
import junit.framework.AssertionFailedError;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import javax.xml.stream.*;


/**
 * Class: RegistryQueryService
 * Description: The main class for all queries to the Registry to go to via Web Service or via internal
 * calls such as jsp pages or other classes.  The main focus is Web Service Interface methods are here
 * such as Search, KeywordSearch, and GetResourceByIdentifier.
 *
 * @author Kevin Benson
 */
public class RegistryQueryService extends DefaultQueryService implements ISearch {
    

    /**
     * Logging variable for writing information to the logs
     */
     private static final Log log = LogFactory.getLog(RegistryQueryService.class);
     
    public static final String QUERY_WSDL_NS = "http://www.ivoa.net/wsdl/RegistrySearch/v0.9";
    
    private static final String CONTRACT_VERSION = "0.9";    
    
    private static final String VORESOURCE_VERSION = "0.10";
    
    private static final String QUERYINTERFACE_ROOT = "vor:VOResources";    

    public RegistryQueryService() {
        super(QUERY_WSDL_NS, CONTRACT_VERSION, VORESOURCE_VERSION);     
    }
   
    public String getWSDLNameSpace() {return this.QUERY_WSDL_NS;}
    public String getContractVersion() { return this.CONTRACT_VERSION;}
    public String getResourceVersion() { return this.VORESOURCE_VERSION;}
    public String getQueryInterfaceRoot(String wsInterfaceMethod) {return QUERYINTERFACE_ROOT;}
    
    public org.astrogrid.registry.server.query.QueryHelper getQueryHelper() {
        if(queryHelper == null) {
            queryHelper = new org.astrogrid.registry.server.query.QueryHelper(this);
        }
        return queryHelper;
    }

   
    
    private boolean validateResources(Document resourcesDoc) throws AssertionFailedError {
    	return RegistryValidator.isValid(resourcesDoc,"VoResources");
    }
    
    private boolean validateSingleResource(Document resourceDoc) throws AssertionFailedError {
    	return RegistryValidator.isValid(resourceDoc,"Resource");
    }
    
    public XMLStreamReader processSingleResult(Node resultDBNode,String responseWrapper) {
    	try {
    		return streamResults(resultDBNode, responseWrapper, null, null, null,true);
    	}catch(Exception e) {
    		SOAPFaultException sfe = new SOAPFaultException(e.getMessage(),e,QUERY_WSDL_NS,SOAPFaultException.QUERYSOAP_TYPE);
    		return processSingleResult(sfe.getFaultDocument(),null);    		
    	}
    }
   
    public XMLStreamReader processSingleResult(ResourceSet resultSet,String responseWrapper) {
    	try {
    		return streamResults(resultSet, responseWrapper, null, null, null, true);
    	}catch(Exception e) {
    		SOAPFaultException sfe = new SOAPFaultException(e.getMessage(),e,QUERY_WSDL_NS,SOAPFaultException.QUERYSOAP_TYPE);
    		return processSingleResult(sfe.getFaultDocument(),null);    		
    	}
    }    
    
    public XMLStreamReader processResults(ResourceSet resultSet,String responseWrapper) {
        return processResults(resultSet, responseWrapper, null, null, null);
    }
    
    public XMLStreamReader processResults(ResourceSet resultSet,String responseWrapper, String start, String max, String identOnly) {
    	try {
        return streamResults(resultSet, responseWrapper, start, max, identOnly, false);
    	}catch(Exception e) {
    		SOAPFaultException sfe = new SOAPFaultException(e.getMessage(),e,QUERY_WSDL_NS,SOAPFaultException.QUERYSOAP_TYPE);
    		return processSingleResult(sfe.getFaultDocument(),null);    		
    	}
    }
    
    private XMLStreamReader streamResults(Node resultDBNode,String responseWrapper, String start, String max, String identOnly, boolean singleResource) throws IOException, XMLDBException {
        
            //check if it is a Fault, if so just return the resultDoc;
            if(resultDBNode.getNodeName().indexOf("Fault") != -1 || 
               (resultDBNode.hasChildNodes() && resultDBNode.getFirstChild().getNodeName().indexOf("Fault") != -1)  ) {
                //All Faults should have been created by server.SOAPFaultException meaning a Document object.
                //return (Document)resultDBNode;
            	return STAXUtils.createXMLStreamReader(new StringReader(DomHelper.DocumentToString((Document)resultDBNode)));            	
            }

            NodeList childNodes = null;
            Document responseDoc = null;
            if(resultDBNode instanceof Document) {
                responseDoc = ((Document)resultDBNode);
            }else if(resultDBNode instanceof Element) {
                responseDoc = ((Element)resultDBNode).getOwnerDocument();
            }

            childNodes = responseDoc.getElementsByTagNameNS("*","Resource");
            String schemaLocations = null;
            schemaLocations =  "http://www.ivoa.net/xml/VOResource/v0.10 " + 
        		schemaLocationBase + "vo-resource-types/VOResource/v0.10/VOResource.xsd ";
            StringBuffer resultBuffer = new StringBuffer();
            if(responseWrapper != null && responseWrapper.trim().length() > 0) {
            	resultBuffer.append(("<ri:" + responseWrapper + " xmlns:ri=\"" + QUERY_WSDL_NS + "\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"));
            }                        
            StringBuffer resContent = new StringBuffer(DomHelper.ElementToString((Element)childNodes.item(0)));
            String temp = resContent.substring(0,resContent.indexOf(">"));
            if(temp.indexOf("schemaLocation") == -1) {
          	  
          	  int tempIndex;
          	  if((tempIndex = temp.indexOf("type")) != -1) {
                	tempIndex = temp.indexOf("\"",tempIndex);
                	temp = temp.substring((tempIndex+1),temp.indexOf("\"",tempIndex+1));
	              if(temp.endsWith("Registry") || temp.endsWith("Authority")) {
	                    schemaLocations = "http://www.ivoa.net/xml/VORegistry/v0.3 " + schemaLocationBase + "vo-resource-types/VORegistry/v0.3/VORegistry.xsd";
	                } else {
	                    schemaLocations = "http://www.ivoa.net/xml/VODataService/v0.5 " + schemaLocationBase + "vo-resource-types/VODataService/v0.5/VODataService.xsd " +
	                    "http://www.ivoa.net/xml/VOTable/v1.0 " + schemaLocationBase + "vo-formats/VOTable/v1.0/VOTable.xsd";
	                    if(temp.endsWith("ConeSearch")) {
	                        schemaLocations += " http://www.ivoa.net/xml/ConeSearch/v0.3 " + schemaLocationBase + "vo-resource-types/ConeSearch/v0.3/ConeSearch.xsd";    
	                    }else if(temp.endsWith("SimpleImageAccess")) {
	                        schemaLocations += " http://www.ivoa.net/xml/SIA/v0.7 " + schemaLocationBase + "vo-resource-types/SIA/v0.7/SIA.xsd";
	                    }else if(temp.endsWith("TabularDB")) {
	                        schemaLocations += " urn:astrogrid:schema:vo-resource-types:TabularDB:v0.3 " + schemaLocationBase + "vo-resource-types/TabularDB/v0.3/TabularDB.xsd";
	                    }else if(temp.endsWith("OpenSkyNode")) {
	                        schemaLocations += " http://www.ivoa.net/xml/OpenSkyNode/v0.1 " + schemaLocations + "vo-resource-types/OpenSkyNode/v0.1/OpenSkyNode.xsd";
	                    }else if(temp.endsWith("CeaService") || temp.endsWith("CeaHttpApplicationType") || temp.endsWith("CeaApplicationType")) {
	                        schemaLocations += "http://www.ivoa.net/xml/CEAService/v0.2 " + schemaLocations + "vo-resource-types/CEAService/v0.2/CEAService.xsd";
	                    }
	                }
//	            add schemaLocation.
	                resContent.insert(resContent.indexOf(">")," xsi:schemaLocation=\"" + schemaLocations + "\"");
          }//if type
            }//if schemalocations
            resultBuffer.append(resContent.toString());
            if(responseWrapper != null && responseWrapper.trim().length() > 0) {
            	resultBuffer.append(("</ri:" + responseWrapper + ">"));
            }
            return STAXUtils.createXMLStreamReader(new StringReader(resultBuffer.toString()));
    }
    
    
   private XMLStreamReader streamResults(ResourceSet resultSet,String responseWrapper, String start, String max, String identOnly, boolean singleResource) throws IOException, XMLDBException {
        
        //Document doc = null;
        //Okay nothing from the query
            //check if it is a Fault, if so just return the resultDoc;
            int limit = conf.getInt("reg.amend.returncount",100);
            if(max != null && Integer.parseInt(max) < limit) {
            	limit = Integer.parseInt(max);
            }
            
            String more = "false";
            if(resultSet.getSize() > limit) {
            	more = "true";
            	resultSet.removeResource((resultSet.getSize() - 1));
            }
            
            if(start == null || start.trim().length() == 0)
            	start = "1";

            
            String schemaLocations = null;
            schemaLocations =  "http://www.ivoa.net/xml/VOResource/v0.10 " + 
            	schemaLocationBase + "vo-resource-types/VOResource/v0.10/VOResource.xsd ";            
            
            StringBuffer resultBuffer = new StringBuffer();
            if(responseWrapper != null && responseWrapper.trim().length() > 0) {
            	resultBuffer.append(("<ri:" + responseWrapper + " xmlns:ri=\"" + QUERY_WSDL_NS + "\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"));
            }
            
            if(!singleResource) {
            	resultBuffer.append(("<ri:VOResources xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ri=\"" + QUERY_WSDL_NS + "\" xsi:schemaLocation=\"" + schemaLocations + "\" from=\"" + start + "\" more=\"" + more + "\" numberReturned=\"" + resultSet.getSize() +"\">"));
            	resultBuffer.append("</ri:VOResources>");            	
            }
            
            if(responseWrapper != null && responseWrapper.trim().length() > 0) {
            	resultBuffer.append(("</ri:" + responseWrapper + ">"));
            }            
            return new ResultStreamer(resultSet,resultBuffer.toString(), Boolean.valueOf(identOnly).booleanValue());          
    }        
    
    public boolean validateSOAPSearch(Document searchDoc) throws AssertionFailedError {
    	validateResources(searchDoc);
    	String errorMessage = "";
      	if(!searchDoc.getDocumentElement().getNamespaceURI().equals(QUERY_WSDL_NS)) {
    		errorMessage = "Error the NamespaceURI in the SOAP is " + searchDoc.getDocumentElement().getNamespaceURI() + " but should be " + QUERY_WSDL_NS;
    	}
    	if(!searchDoc.getDocumentElement().getLocalName().equals("SearchResponse")){
    		errorMessage += "The Local Name in the SOAP was " + searchDoc.getDocumentElement().getLocalName() + " but it should be SearchResponse ";
    	}
    	if(errorMessage.trim().length() > 0)
    		throw new AssertionFailedError(errorMessage);
    	return true;
    }

    public boolean validateSOAPResolve(Document resolveDoc) throws AssertionFailedError {
    	validateSingleResource(resolveDoc);
    	String errorMessage = "";
      	if(!resolveDoc.getDocumentElement().getNamespaceURI().equals(QUERY_WSDL_NS)) {
    		errorMessage = "Error the NamespaceURI in the SOAP is " + resolveDoc.getDocumentElement().getNamespaceURI() + " but should be " + QUERY_WSDL_NS;
    	}
    	if(!resolveDoc.getDocumentElement().getLocalName().equals("ResolveResource")){
    		errorMessage += "The Local Name in the SOAP was " + resolveDoc.getDocumentElement().getLocalName() + " but it should be ResolveResource ";
    	}
    	if(errorMessage.trim().length() > 0)
    		throw new AssertionFailedError(errorMessage);    	
    	return true;
    }    
}