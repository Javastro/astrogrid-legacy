package org.astrogrid.registry.server.query;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import org.xml.sax.SAXException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.util.DomHelper;

import org.astrogrid.registry.server.SOAPFaultException;
import org.astrogrid.registry.server.XSLHelper;

public class ProcessResults {
    
    /**
     * Logging variable for writing information to the logs
     */
    private static final Log log = LogFactory.getLog(DefaultQueryService.class);    
    
    public static Document processQueryResults(Node resultDoc, String wsdlNS, String contractVersion, String responseWrapper) {
        Document doc = null;
        //Okay nothing from the query
        try {
            //check if it is a Fault, if so just return the resultDoc;
            if(resultDoc.getNodeName().indexOf("Fault") != -1 || 
               (resultDoc.hasChildNodes() && resultDoc.getFirstChild().getNodeName().indexOf("Fault") != -1)  ) {
                //All Faults should have been created by server.SOAPFaultException meaning a Document object.
                return (Document)resultDoc;
            }

            XSLHelper xslHelper = new XSLHelper();
            doc = xslHelper.transformExistResult(resultDoc, contractVersion);
            if(responseWrapper != null && responseWrapper.trim().length() > 0) {
                Element currentRoot = doc.getDocumentElement();
                Element root = doc.createElementNS(wsdlNS,responseWrapper);
                root.appendChild(currentRoot);
                doc.appendChild(root);
            }//if
        }catch(ParserConfigurationException e) {
          log.error(e);
          doc = SOAPFaultException.createQuerySOAPFaultException("Server Error: " + e.getMessage(),e);
        }catch(TransformerConfigurationException e) {
            log.error(e);
            doc = SOAPFaultException.createQuerySOAPFaultException("Server Error: " + e.getMessage(),e);
        }catch(TransformerException e) {
            log.error(e);
            doc = SOAPFaultException.createQuerySOAPFaultException("Server Error: " + e.getMessage(),e);
        }catch(UnsupportedEncodingException e) {
            log.error(e);
            doc = SOAPFaultException.createQuerySOAPFaultException("Server Error: " + e.getMessage(),e);
        }catch(IOException e) {
            log.error(e);
            doc = SOAPFaultException.createQuerySOAPFaultException("Server Error: " + e.getMessage(),e);           
        }
        return doc;
    }
}