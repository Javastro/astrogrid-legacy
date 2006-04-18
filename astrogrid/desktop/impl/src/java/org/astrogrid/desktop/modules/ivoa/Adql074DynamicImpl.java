/*$Id: Adql074DynamicImpl.java,v 1.2 2006/04/18 23:25:45 nw Exp $
 * Created on 28-Feb-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ivoa;

import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.axis.utils.XMLUtils;
import org.apache.xpath.jaxp.XPathFactoryImpl;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.ivoa.Adql074;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
/**
 * implementation of the adql translator, that calls the web service using
 * the javax.xml.soap api - so no stub or proxy objects.
 * @author Noel Winstanley nw@jb.man.ac.uk 28-Feb-2006
 *
 */
public class Adql074DynamicImpl implements Adql074 {

    /** namespace translator. */
    private final class AdqlTranslatorNamespaceContext implements NamespaceContext {
        private AdqlTranslatorNamespaceContext() {
            super();
        }

        public String getNamespaceURI(String arg0) {
            if (arg0.equals("parser")) {
                return WS_NAMESPACE;
            }
            System.err.println("unrecognized prefix " + arg0);
            return null;
        }

        public String getPrefix(String arg0) {
            return null;
        }

        public Iterator getPrefixes(String arg0) {
            return null;
        }
    }

    private static final String SOAPACTION = "SOAPAction";    
    /** web service method to convert to ADQL */
    private static final String SQL_TO_ADQL_STRING = "SQLtoADQLString";
    /** web service method to convert from ADQL */
    private static final String ADQL_STRING_TO_SQL = "ADQLStringtoSQL";
    /** namespace for webservice - happens to be used as prefix to soap action too */
    private static final String WS_NAMESPACE = "http://ws.parser.adql.ivoa.net/";
    /** service endpoint to call */
    public static final String DEFAULT_ENDPOINT = "http://openskyquery.net/AdqlTranslator/ADQLTrans.asmx";
    
    private String endpoint = DEFAULT_ENDPOINT;
    
    /** use only during construction, to configure service */
    public void setEndpoint(String ep) {
        this.endpoint = ep;
    }
   
    public Adql074DynamicImpl() throws UnsupportedOperationException, SOAPException {
        super();
        fac = SOAPConnectionFactory.newInstance();
        msgFac = MessageFactory.newInstance();
        xpathFactory = new XPathFactoryImpl(); //@todo work out why I need to call implementation directlly
    }
    private final SOAPConnectionFactory fac;
    private final MessageFactory msgFac;
    private final XPathFactory xpathFactory;
     
  // private final String endpoint = "http://127.0.0.1:8080/AdqlTranslator/ADQLTrans.asmx";
    
    public Document s2x(String arg0) throws ServiceException {       
        try {
            SOAPMessage request = msgFac.createMessage();
            request.getMimeHeaders().setHeader(SOAPACTION,WS_NAMESPACE + SQL_TO_ADQL_STRING);
            SOAPPart part = request.getSOAPPart();
            SOAPEnvelope envelope = part.getEnvelope();
            SOAPBody body = envelope.getBody();
            // buuild the message body
            Name requestName = envelope.createName(SQL_TO_ADQL_STRING,"parser",WS_NAMESPACE);
            Name sqlName = envelope.createName("sql","parser",WS_NAMESPACE);
            body.addBodyElement(requestName)
                .addChildElement(sqlName)
                    .addTextNode(arg0);
            
            // submit the message
            SOAPConnection conn = fac.createConnection();
            SOAPMessage response = conn.call(request,endpoint); 

            // parse the response
            SOAPBody responseBody = response.getSOAPBody();
            if (responseBody.hasFault()) {
                SOAPFault f = responseBody.getFault();
                throw new ServiceException(f.getFaultActor() + " : " + f.getFaultString());
            } 
            XPath xp = xpathFactory.newXPath();
            xp.setNamespaceContext(new AdqlTranslatorNamespaceContext());
            String content = xp.evaluate("//parser:SQLtoADQLStringResult"
                    , response.getSOAPPart().getEnvelope());                                
            return XMLUtils.getDocumentBuilder().parse(new InputSource(new StringReader(content)));             
        } catch (SOAPException e) {
            throw new ServiceException("Failure to call service",e);
        } catch (ParserConfigurationException e) {
            throw new ServiceException("Failed to parse result",e);
        } catch (SAXException e) {
            throw new ServiceException("Failed to parse result",e);
        } catch (IOException e) {
            throw new ServiceException("Failed to parse result",e);
        } catch (XPathExpressionException e) {
            throw new ServiceException("Failed to parse result",e);
        }
        
    }

    public String x2s(Document arg0) throws ServiceException {
        try {
            SOAPMessage request = msgFac.createMessage();
            request.getMimeHeaders().setHeader(SOAPACTION,WS_NAMESPACE + ADQL_STRING_TO_SQL);
            SOAPPart part = request.getSOAPPart();
            SOAPEnvelope envelope = part.getEnvelope();
            SOAPBody body = envelope.getBody();
            // buuild the message body
            Name requestName = envelope.createName(ADQL_STRING_TO_SQL,"parser",WS_NAMESPACE);
            Name sqlName = envelope.createName("adql","parser",WS_NAMESPACE);
            String str = XMLUtils.DocumentToString(arg0);
            body.addBodyElement(requestName)
                .addChildElement(sqlName)
                    .addTextNode(str);
            
            // submit the message
            SOAPConnection conn = fac.createConnection();
            SOAPMessage response = conn.call(request,endpoint); 

            // parse the response
            SOAPBody responseBody = response.getSOAPBody();
            if (responseBody.hasFault()) {
                SOAPFault f = responseBody.getFault();
                throw new ServiceException(f.getFaultActor() + " : " + f.getFaultString());
            } 
            XPath xp = xpathFactory.newXPath();
            xp.setNamespaceContext(new AdqlTranslatorNamespaceContext());
            return xp.evaluate("//parser:ADQLStringtoSQLResponse"
                    , response.getSOAPPart().getEnvelope());                                            

        } catch (SOAPException e) {
            throw new ServiceException("Failure to call service",e);
        } catch (XPathExpressionException e) {
            throw new ServiceException("Failed to parse response",e);
        } 
                
    }
    
public static void main(String[] args) {
    try {
        /*
        InputStream is= Adql074DynamicImpl.class.getResourceAsStream("module.xml");       
        XPathFactory fac = new XPathFactoryImpl(); //@todo work out why this isn't being auto-located
        XPath xp = fac.newXPath();
        System.out.println(xp.evaluate("//description",new InputSource(is)));
        */
    Adql074DynamicImpl i = new Adql074DynamicImpl();
    Document doc = i.s2x("Select * From Tab t"); // returns no result.
    XMLUtils.PrettyDocumentToStream(doc,System.out);
    String s= i.x2s(doc);
    System.out.println(s);
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}


/* 
$Log: Adql074DynamicImpl.java,v $
Revision 1.2  2006/04/18 23:25:45  nw
merged asr development.

Revision 1.1.2.2  2006/04/14 02:45:03  nw
finished code.extruded plastic hub.

Revision 1.1.2.1  2006/03/22 18:01:31  nw
merges from head, and snapshot of development
 
*/