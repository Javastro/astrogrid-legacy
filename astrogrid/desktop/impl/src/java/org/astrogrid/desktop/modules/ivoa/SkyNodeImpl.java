/*$Id: SkyNodeImpl.java,v 1.5 2006/08/31 21:34:46 nw Exp $
 * Created on 22-Feb-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ivoa;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
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
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFunctionResolver;
import javax.xml.xpath.XPathVariableResolver;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xpath.jaxp.XPathFactoryImpl;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.acr.ivoa.AvailabilityBean;
import org.astrogrid.acr.ivoa.FunctionBean;
import org.astrogrid.acr.ivoa.SkyNode;
import org.astrogrid.acr.ivoa.SkyNodeColumnBean;
import org.astrogrid.acr.ivoa.SkyNodeTableBean;
import org.astrogrid.adql.v1_0.beans.AllSelectionItemType;
import org.astrogrid.adql.v1_0.beans.FromTableType;
import org.astrogrid.adql.v1_0.beans.SelectDocument;
import org.astrogrid.adql.v1_0.beans.SelectType;
import org.astrogrid.adql.v1_0.beans.SelectionItemType;
import org.astrogrid.adql.v1_0.beans.TableType;
import org.astrogrid.desktop.modules.adqlEditor.AdqlData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

/** implementation of the skynode interface
 * 
 *  error-tolerant - ini particular, tries to be forgiving of namespace differences between us and japan services
 *  @todo reimplement using xfire.
 *  */
public class SkyNodeImpl implements SkyNode {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(SkyNodeImpl.class);

    /** namespace translator. */
    private final class SkyNodeNamespaceContext implements NamespaceContext {
        private SkyNodeNamespaceContext() {
            super();
        }

        public String getNamespaceURI(String arg0) {
            if (arg0.equals("ws")) {
                return WS_NAMESPACE;
            }
            if (arg0.equals("skynode")) {
                return SKYNODE_NAMESPACE;
            }
            if (arg0.equals("votable")) {
                return VOTABLE_NAMESPACE;
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

 
    private static final String SKYNODE_NAMESPACE = "http://www.ivoa.net/xml/SkyNode/v1.0";   
    private static final String SOAPACTION = "SOAPAction";
    private static final String WS_NAMESPACE = "SkyNode.ivoa.net";
    private static final String VOTABLE_NAMESPACE = "http://www.ivoa.net/xml/VOTable/v1.1";
   
    
    private final SOAPConnectionFactory fac;
    private final MessageFactory msgFac;

    private final XPathFactory xpathFactory;

    public SkyNodeImpl() throws UnsupportedOperationException, SOAPException {
        super();
        fac = SOAPConnectionFactory.newInstance();
        msgFac = MessageFactory.newInstance();       
        xpathFactory = new XPathFactoryImpl(); //@todo work out why I need to call implementation directlly
    }


    public AvailabilityBean getAvailability(URI arg0) throws InvalidArgumentException,
            NotFoundException, ServiceException {
        try {
            SOAPMessage request = newSoapMessage("GetAvailability");
            SOAPPart part = request.getSOAPPart();
            SOAPEnvelope envelope = part.getEnvelope();
            SOAPBody body = envelope.getBody();        
            // build message body.
            Name requestName = envelope.createName("GetAvailability","ws",WS_NAMESPACE);
            body.addBodyElement(requestName);
            // execute
            SOAPConnection conn = fac.createConnection();
            SOAPMessage response = conn.call(request,resolveService(arg0));
            
            // process results
            SOAPBody responseBody = response.getSOAPBody();
            checkFault(responseBody);
            XPath xp = getXPath();
            
            Node availability =(Node) xp.evaluate("//skynode:GetAvailabilityResult|//ws:GetAvailabilityResult"
                    ,response.getSOAPPart().getEnvelope()
                    ,XPathConstants.NODE);
            if (availability  == null) {
                throw new ServiceException("Unexpected Response: " + response.getSOAPPart().getEnvelope());
            }  
            return null;
/* @implement this.
            return new AvailabilityBean(
                    xp.evaluate("skynode:Servername|ws:Servername",availability)
                    ,xp.evaluate("skynode:location|ws:location",availability)
                    ,xp.evaluate("skynode:message|ws:message",availability)
                    ,xp.evaluate("skynode:validTo|ws:validTo",availability)
                    ,xp.evaluate("skynode:upTime|ws:upTime",availability)
                    ,xp.evaluate("skynode:timeOnServer|ws:timeOnServer",availability)                    
                    );
*/
            } catch (SOAPException e) {
                throw new ServiceException("Failed to call service",e);
            } catch (XPathExpressionException e) {
                throw new ServiceException("Failed to parse response",e);
            }  
    }
//@todo untested
    public Document getFootprint(URI arg0, Document arg1) throws InvalidArgumentException,
            NotFoundException, ServiceException {
        try {
            SOAPMessage request = newSoapMessage("Footprint");
            SOAPPart part = request.getSOAPPart();
            SOAPEnvelope envelope = part.getEnvelope();
            SOAPBody body = envelope.getBody();        
            // build message body.
            Name requestName = envelope.createName("Footprint","ws",WS_NAMESPACE);
            body.addBodyElement(requestName)
                .appendChild(arg1.getDocumentElement());
            // execute
            SOAPConnection conn = fac.createConnection();
            SOAPMessage response = conn.call(request,resolveService(arg0));
            
            // process results
            SOAPBody responseBody = response.getSOAPBody();
            checkFault(responseBody);
            XPath xp = getXPath();
            

            NodeList l =(NodeList) xp.evaluate("//skynode:FootprintResult|//ws:FootprintResult"
                    ,response.getSOAPPart().getEnvelope()
                    ,XPathConstants.NODESET);
            if (l.getLength() == 0) {
                throw new ServiceException("Unexpected Response: " + response.getSOAPPart().getEnvelope());
            }
            Document doc = XMLUtils.newDocument();
            doc.importNode(l.item(0),true);
            return doc;
            
            } catch (SOAPException e) {
                throw new ServiceException("Failed to call service",e);
            } catch (XPathExpressionException e) {
                throw new ServiceException("Failed to parse response",e);
            } catch (ParserConfigurationException e) {
                throw new ServiceException("Failed to parse response",e);
            }      
    }
    public String[] getFormats(URI arg0) throws InvalidArgumentException, NotFoundException,
            ServiceException {
        try {
            SOAPMessage request = newSoapMessage("Formats");
            SOAPPart part = request.getSOAPPart();
            SOAPEnvelope envelope = part.getEnvelope();
            SOAPBody body = envelope.getBody();        
            // build message body.
            Name requestName = envelope.createName("Formats","ws",WS_NAMESPACE);
            body.addBodyElement(requestName);
            // execute
            SOAPConnection conn = fac.createConnection();
            SOAPMessage response = conn.call(request,resolveService(arg0));
            
            // process results
            SOAPBody responseBody = response.getSOAPBody();
            checkFault(responseBody);
            XPath xp = getXPath();
            

            NodeList l =(NodeList) xp.evaluate("//skynode:string|//ws:string"
                    ,response.getSOAPPart().getEnvelope()
                    ,XPathConstants.NODESET);
            if (l.getLength() == 0) {
                throw new ServiceException("Unexpected Response: " + response.getSOAPPart().getEnvelope());
            }
            String[] result = new String[l.getLength()];
            for (int i = 0; i < l.getLength(); i++) {
                Element el = (Element)l.item(i);
                result[i] = el.getFirstChild().getNodeValue();
            }
            return result;
            } catch (SOAPException e) {
                throw new ServiceException("Failed to call service",e);
            } catch (XPathExpressionException e) {
                throw new ServiceException("Failed to parse response",e);
            }
    }

    public FunctionBean[] getFunctions(URI arg0) throws InvalidArgumentException,
            NotFoundException, ServiceException {
        try {
            SOAPMessage request = newSoapMessage("Functions");
            SOAPPart part = request.getSOAPPart();
            SOAPEnvelope envelope = part.getEnvelope();
            SOAPBody body = envelope.getBody();        
            // build message body.
            Name requestName = envelope.createName("Functions","ws",WS_NAMESPACE);
            body.addBodyElement(requestName);
            // execute
            SOAPConnection conn = fac.createConnection();
            SOAPMessage response = conn.call(request,resolveService(arg0));
            
            // process results
            SOAPBody responseBody = response.getSOAPBody();
            checkFault(responseBody);
            XPath xp = getXPath();
            
            NodeList functions =(NodeList) xp.evaluate("//skynode:MetaFunction|//ws:MetaFunction"
                    ,response.getSOAPPart().getEnvelope()
                    ,XPathConstants.NODESET);
            /* seems like returning none is permitted
            if (functions.getLength() == 0) {
               // throw new ServiceException("Unexpected Response: " + response.getSOAPPart().getEnvelope());
                return new 
            } */
            FunctionBean[] functionBeans = new FunctionBean[functions.getLength()];
            for (int i = 0; i < functions.getLength(); i++) {
                Element function = (Element)functions.item(i);
                NodeList params = (NodeList)xp.evaluate("skynode:parameters/skynode:Parameter|ws:parameters/ws:Parameter",function,XPathConstants.NODESET);
                ParameterBean[] paramBeans = new ParameterBean[params.getLength()];
                for (int j = 0; j < params.getLength(); j++) {
                    Element param = (Element)params.item(i);
                    String paramName = xp.evaluate("skynode:name|ws:name",param);
                    paramBeans[j] = new ParameterBean(
                            paramName
                            ,paramName // repeated
                            ,xp.evaluate("skynode:description|ws:description",param)
                            ,null,null,null
                            ,xp.evaluate("skynode:type|ws:type",param)
                            ,null,null
                            );
                }
                functionBeans[i] = new FunctionBean(
                        xp.evaluate("skynode:name|ws:name",function)
                        ,xp.evaluate("skynode:description|ws:description",function)
                        ,paramBeans
                        );
            }
            return functionBeans;
            } catch (SOAPException e) {
                throw new ServiceException("Failed to call service",e);
            } catch (XPathExpressionException e) {
                throw new ServiceException("Failed to parse response",e);
            }
    }

    // working,, but much too slow on the parsing. need to optimize.
    public SkyNodeTableBean[] getMetadata(URI arg0) throws InvalidArgumentException, NotFoundException,
            ServiceException {
        try {
            SOAPMessage request = newSoapMessage("Tables");
            SOAPPart part = request.getSOAPPart();
            SOAPEnvelope envelope = part.getEnvelope();
            SOAPBody body = envelope.getBody();        
            // build message body.
            Name requestName = envelope.createName("Tables","ws",WS_NAMESPACE);
            body.addBodyElement(requestName);
            // execute
            final SOAPConnection conn = fac.createConnection();
            final URL endpoint = resolveService(arg0);
            SOAPMessage response = conn.call(request,endpoint);
            
            // process results
            SOAPBody responseBody = response.getSOAPBody();
            checkFault(responseBody);
            XPath xp = getXPath();

            NodeList tables =(NodeList) xp.evaluate("//skynode:MetaTable|//ws:MetaTable"
                    ,response.getSOAPPart().getEnvelope()
                    ,XPathConstants.NODESET);
            if (tables.getLength() == 0) {
                throw new ServiceException("Unexpected Response:" + responseBody);
            }
            System.err.println("tables: " + tables.getLength());
            SkyNodeTableBean[] tableBeans = new SkyNodeTableBean[tables.getLength()];
            for (int i = 0; i < tables.getLength(); i++) {
                Element table = (Element)tables.item(i);
                String tablename = xp.evaluate("skynode:Name|ws:Name",table);
                SkyNodeColumnBean[] colBeans = getColumnMetadata(xp,conn,endpoint,tablename);
                Map relationMap = new HashMap();
                NodeList relations = (NodeList)xp.evaluate("//skynode:RelationShip|//ws:RelationShip",table,XPathConstants.NODESET);
                for (int j = 0; j < relations.getLength(); j++) {
                    Element relation = (Element)relations.item(i);
                    relationMap.put(xp.evaluate("skynode:ForeignKey|ws:ForeignKey",relation)
                                        , xp.evaluate("skynode:Table|ws:Table",relation));
                }
                tableBeans[i] = new SkyNodeTableBean(
                        tablename
                        ,xp.evaluate("skynode:Description|ws:Description",table)
                        ,colBeans
                        ,xp.evaluate("skynode:PrimaryKey|ws:PrimaryKey",table)
                        ,((Double)xp.evaluate("skynode:Rows|ws:Rows",table, XPathConstants.NUMBER)).intValue()
                        ,((Double)xp.evaluate("skynode:Rank|ws:Rank",table,XPathConstants.NUMBER)).intValue()
                        ,relationMap
                        );
            }
            return null; //@todo tableBeans;
            } catch (SOAPException e) {
                throw new ServiceException("Failed to call service",e);
            } catch (XPathExpressionException e) {
                throw new ServiceException("Failed to parse response",e);
            }
    }

	public String getRegistryAdqlQuery() {
		return "select * from Registry where @xsi:type like '%OpenSkyNode' ";
		 
	}


	public String getRegistryXQuery() {
		return "//vor:Resource[@xsi:type &= '*OpenSkyNode' and ( not ( @status = 'inactive' or @status='deleted'))]";

	}

    public Document getResults(URI arg0, Document arg1) throws InvalidArgumentException,
            NotFoundException, ServiceException {
        try {
            SOAPMessage request = newSoapMessage("PerformQuery");
            SOAPPart part = request.getSOAPPart();
            SOAPEnvelope envelope = part.getEnvelope();
            SOAPBody body = envelope.getBody();        
            // build message body.
            Name requestName = envelope.createName("PerformQuery","ws",WS_NAMESPACE);
            Document requestDoc = XMLUtils.newDocument();
            Node adql = requestDoc.importNode(arg1.getDocumentElement(),true);
            //@todo - unsure whether this works with JVO - need to have different namespace there?
            Element root = requestDoc.createElementNS(WS_NAMESPACE,"ws:PerformQuery");
            requestDoc.appendChild(root);
            root.appendChild(adql);
            Element format = requestDoc.createElementNS(WS_NAMESPACE,"ws:format");
            Text txt = requestDoc.createTextNode("VOTABLE");
            format.appendChild(txt);
            root.appendChild(format);
            
            body.addDocument(requestDoc);
            System.err.println(body);
       
            // execute
            SOAPConnection conn = fac.createConnection();
            SOAPMessage response = conn.call(request,resolveService(arg0));
            
            // process results
            SOAPBody responseBody = response.getSOAPBody();
            checkFault(responseBody);
            XPath xp = getXPath();
            

            NodeList l =(NodeList) xp.evaluate("//skynode:PerformQueryResult|//ws:PerformQueryResult"
                    ,response.getSOAPPart().getEnvelope()
                    ,XPathConstants.NODESET);
            if (l.getLength() == 0) {
                throw new ServiceException("Unexpected Response: " + response.getSOAPPart().getEnvelope());
            }
            Element vodata = (Element)l.item(0);
            Node error = (Node)xp.evaluate("skynode:Error|ws:Error",vodata,XPathConstants.NODE);
            if (error != null) {
                throw new ServiceException("Query Failed: " + error);
            }
            Node votable = (Node)xp.evaluate("//skynode:VOTABLE|//ws:VOTABLE|//votable:VOTABLE",vodata,XPathConstants.NODE);
            if (votable == null) {
                throw new ServiceException("Query failed to produce a result: " + response.getSOAPPart().getEnvelope());
            }
            Document result = XMLUtils.newDocument();
            Node n = result.importNode(votable,true);
            //n.setPrefix(VOTABLE_NAMESPACE); // suspect it may be in a dodge namespace at this point
            result.appendChild(n);
            return result;
            
            } catch (SOAPException e) {
                throw new ServiceException("Failed to call service",e);
            } catch (XPathExpressionException e) {
                throw new ServiceException("Failed to parse response",e);
            } catch (ParserConfigurationException e) {
                throw new ServiceException("Failed to parse response",e);
            }      
    }        
   

    public String getResultsF(URI arg0, Document arg1, String arg2)
            throws InvalidArgumentException, NotFoundException, ServiceException {
        return null;
    }

    public void saveResults(URI arg0, Document arg1, URI arg2) throws InvalidArgumentException,
            NotFoundException, ServiceException, SecurityException {
        
    }

    public void saveResultsF(URI arg0, Document arg1, URI arg2, String arg3)
            throws InvalidArgumentException, NotFoundException, ServiceException, SecurityException {
    }

    /** helper method - checks whether a fault occured, and throws if that's the case.
     * @param responseBody
     * @throws ServiceException
     */
    private void checkFault(SOAPBody responseBody) throws ServiceException {
        if (responseBody.hasFault()) {
            SOAPFault f = responseBody.getFault();
            throw new ServiceException(f.getFaultActor() + " : " + f.getFaultString());            
        }
    }

    /** build an array of column descriptors for a table 
     * @throws ServiceException */
    private SkyNodeColumnBean[] getColumnMetadata(XPath xp,SOAPConnection conn, URL endpoint, String tablename) throws ServiceException {
        try {
            SOAPMessage request = newSoapMessage("Columns");
            SOAPPart part = request.getSOAPPart();
            SOAPEnvelope envelope = part.getEnvelope();
            SOAPBody body = envelope.getBody();        
            // build message body.
            Name requestName = envelope.createName("Columns","ws",WS_NAMESPACE);
            body.addBodyElement(requestName)
                .addChildElement("table","skynode",SKYNODE_NAMESPACE)
                    .addTextNode(tablename);

            
            SOAPMessage response = conn.call(request,endpoint);
            // process results
            SOAPBody responseBody = response.getSOAPBody();
            checkFault(responseBody);

            NodeList columns =(NodeList) xp.evaluate("//skynode:MetaColumn|//ws:MetaColumn"
                    ,response.getSOAPPart().getEnvelope()
                    ,XPathConstants.NODESET);
            if (columns.getLength() == 0) {
                throw new ServiceException("Unexpected Response: " + responseBody);
            }
            System.err.println("columns: " + columns.getLength());
            SkyNodeColumnBean[] columnBeans = new SkyNodeColumnBean[columns.getLength()];
            for (int i = 0; i < columns.getLength(); i++) {
                Element column = (Element)columns.item(i);
               
                columnBeans[i] = new SkyNodeColumnBean(
                        xp.evaluate("skynode:Name|ws:Name",column)
                        ,xp.evaluate("skynode:Description|ws:Description",column)
                        ,xp.evaluate("skynode:UCD|ws:UCD",column)
                        ,xp.evaluate("skynode:DataType|ws:DataType",column)     
                        ,xp.evaluate("skynode:Unit|ws:Unit",column)               
                        ,((Double)xp.evaluate("skynode:Precision|ws:Precision",column, XPathConstants.NUMBER)).intValue()
                        ,((Double)xp.evaluate("skynode:ByteSize|ws:ByteSize",column,XPathConstants.NUMBER)).intValue()
                        ,((Double)xp.evaluate("skynode:Rank|ws:Rank",column,XPathConstants.NUMBER)).intValue()
                        );
            }
            return columnBeans;
            } catch (SOAPException e) {
                throw new ServiceException("Failed to call service",e);
            } catch (XPathExpressionException e) {
                throw new ServiceException("Failed to parse response",e);
            }              
    }

    /**
     *  xpath objects are not thread safe - neither are  xpath compile expressions.
     *  
     *  still, would like to find some place to hang compiled xpath expressions - for efficiencies sake.
     *  - so going to make an xpath proxy, that hangs onto previously used expressions, and reuses them next time they're encountered.
     *  even with 
     */
    private XPath getXPath() {
        final XPath xp = xpathFactory.newXPath();
        xp.setNamespaceContext(new SkyNodeNamespaceContext());
        return new XPath() {
            Map compiledExpressions = new HashMap(); //@todo make this weak later.
            public void reset() {
                xp.reset();
            }

            public void setXPathVariableResolver(XPathVariableResolver arg0) {
                xp.setXPathVariableResolver(arg0);
            }

            public XPathVariableResolver getXPathVariableResolver() {
                return xp.getXPathVariableResolver();
            }

            public void setXPathFunctionResolver(XPathFunctionResolver arg0) {
                xp.setXPathFunctionResolver(arg0);
            }

            public XPathFunctionResolver getXPathFunctionResolver() {
                return xp.getXPathFunctionResolver();
            }

            public void setNamespaceContext(NamespaceContext arg0) {
                xp.setNamespaceContext(arg0);
            }

            public NamespaceContext getNamespaceContext() {
                return xp.getNamespaceContext();
            }

            public XPathExpression compile(String arg0) throws XPathExpressionException {
                return xp.compile(arg0);
            }

            public Object evaluate(String arg0, Object arg1, QName arg2) throws XPathExpressionException {
                XPathExpression exp;
                if (compiledExpressions.containsKey(arg0)) {
                    exp = (XPathExpression)compiledExpressions.get(arg0);                    
                } else {
                    exp = xp.compile(arg0);
                    compiledExpressions.put(arg0,exp);
                }
                return exp.evaluate(arg1,arg2);
            }

            public String evaluate(String arg0, Object arg1) throws XPathExpressionException {
                XPathExpression exp;
                if (compiledExpressions.containsKey(arg0)) {
                    exp = (XPathExpression)compiledExpressions.get(arg0);                    
                } else {
                    exp = xp.compile(arg0);
                    compiledExpressions.put(arg0,exp);
                }
                return exp.evaluate(arg1);
            }

            public Object evaluate(String arg0, InputSource arg1, QName arg2) throws XPathExpressionException {
                XPathExpression exp;
                if (compiledExpressions.containsKey(arg0)) {
                    exp = (XPathExpression)compiledExpressions.get(arg0);                    
                } else {
                    exp = xp.compile(arg0);
                    compiledExpressions.put(arg0,exp);
                }
                return exp.evaluate(arg1,arg2);
            }

            public String evaluate(String arg0, InputSource arg1) throws XPathExpressionException {
                XPathExpression exp;
                if (compiledExpressions.containsKey(arg0)) {
                    exp = (XPathExpression)compiledExpressions.get(arg0);                    
                } else {
                    exp = xp.compile(arg0);
                    compiledExpressions.put(arg0,exp);
                }
                return exp.evaluate(arg1);
            }
            
        };
    }
    
    /** create a new soap message.
     * @param soapAction
     * @return
     * @throws ServiceException
     */
    private SOAPMessage newSoapMessage(String soapAction) throws ServiceException {
        try {
            SOAPMessage request = msgFac.createMessage();
            request.getMimeHeaders().setHeader(SOAPACTION,WS_NAMESPACE + "/" + soapAction);               
            return request;
        } catch (SOAPException e) {
            throw new ServiceException("Failed to create message",e);
        }
    }
    
    /** resolve a registry id to a service endpoint (while letting already-endpoints pass through unchanged) */
private URL resolveService(URI id) throws InvalidArgumentException {
    if (id.getScheme().equals("ivo")) {            
    //@todo later place registry resolver in here
        throw  new InvalidArgumentException("Unimplemented for ivo");
    } else if (id.getScheme().equals("http")) {
        try {
        return id.toURL();
        } catch (MalformedURLException e) {
            throw new InvalidArgumentException(e);
        }
    } else {
        throw new InvalidArgumentException("Unknown protocol " + id.getScheme());
    }
                
}

//@implement
    public float estimateQueryCost(long arg0, Document arg1) throws InvalidArgumentException, NotFoundException, ServiceException {
        return 0;
    }



}


/* 
$Log: SkyNodeImpl.java,v $
Revision 1.5  2006/08/31 21:34:46  nw
minor tweaks and doc fixes.

Revision 1.4  2006/08/15 10:13:50  nw
migrated from old to new registry models.

Revision 1.3  2006/06/15 09:48:56  nw
mived testing out into unit test.

Revision 1.2  2006/04/18 23:25:45  nw
merged asr development.

Revision 1.1.2.3  2006/04/14 02:45:03  nw
finished code.extruded plastic hub.

Revision 1.1.2.2  2006/03/28 13:47:35  nw
first webstartable version.

Revision 1.1.2.1  2006/03/22 18:01:31  nw
merges from head, and snapshot of development

Revision 1.1  2006/02/24 16:28:11  nw
startings of an immplementation of skynode. not operational yet.
 
*/