/*
 * $Id: CEAJAXBUtils.java,v 1.2 2009/02/26 12:25:48 pah Exp $
 * 
 * Created on 13 May 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.applications.description.jaxb;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.util.ValidationEventCollector;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import net.ivoa.resource.Resource;
import net.ivoa.resource.registry.iface.VOResources;

import org.astrogrid.applications.description.MetadataException;
import org.astrogrid.applications.description.execution.ExecutionSummaryType;
import org.astrogrid.applications.description.execution.Tool;
import org.astrogrid.contracts.Namespaces;
import org.astrogrid.contracts.SchemaMap;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Some utilities to make working with JAXB marshalling easier. Sets up all of the validation and namespaces etc.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 10 Jun 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
public class CEAJAXBUtils {

    public static Transformer regStylesheet;
    public static Transformer identityTransformer;
    /** logger for this class */
    private static final org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory
	    .getLog(CEAJAXBUtils.class);
    private static JAXBContext contextFactory; 

    static {
	InputStream xslFileStream;
	TransformerFactory xformFactory = TransformerFactory.newInstance();
	xslFileStream = CEAJAXBUtils.class
		.getResourceAsStream("/RegistryFixup.xsl");
	assert xslFileStream != null: "could not find the RegistryFixup.xsl";
	Source xsl = new StreamSource(new BufferedReader(new InputStreamReader(
		xslFileStream)));
	try {
	    regStylesheet = xformFactory.newTransformer(xsl);
	    identityTransformer = xformFactory.newTransformer();
	} catch (TransformerConfigurationException e) {
	    logger.fatal("problem setting up default registry stylesheet", e);
	}
	try {
        contextFactory = CEAJAXBContextFactory.newInstance();
    } catch (JAXBException e) {
        logger.fatal("problem setting up the JAXB context", e);
    }

    }

    private CEAJAXBUtils() {
	// stop instantiation
    }

    @SuppressWarnings("unchecked")
    public static Document marshall(Resource desc)
	    throws ParserConfigurationException, JAXBException,
	    MetadataException {
	Schema schema = null;// do not attempt to validate at the moment, as there is often not a single schema that suffices for Resources...(e.g. multiple capabilities) findSchema(desc.getClass());
	return CEAJAXBUtils.marshall(new JAXBElement(new QName(Namespaces.RI
		.getNamespace(), "Resource"), Resource.class, desc),
		CEAJAXBUtils.regStylesheet, schema);
    }

    @SuppressWarnings("unchecked")
    public static Document marshall(VOResources desc)
	    throws ParserConfigurationException, JAXBException,
	    MetadataException {
	Schema schema = null;//findSchema(desc.getClass());
	return CEAJAXBUtils.marshall(new JAXBElement(new QName(Namespaces.RI
		.getNamespace(), "VOResources"), VOResources.class, desc),
		CEAJAXBUtils.regStylesheet, schema);
    }

    public static Document marshall(Tool tool) throws MetadataException {
	Schema schema = findSchema(tool.getClass());
	return CEAJAXBUtils.marshall(new JAXBElement(new QName(Namespaces.CEAT
		.getNamespace(), "tool"), Tool.class, tool),
		identityTransformer,schema);
    }

    /**
     * @param element
     * @param stylesheet
     * @param schema if non-null then this schema is used to validate
     * @return
     * @throws MetadataException
     * @TODO - create a family of functions that marshall to other outputs...
     */
    static public Document marshall(JAXBElement<?> element,
	    Transformer stylesheet, Schema schema) throws MetadataException {

	try {
	    // IMPL this is not particularly efficient, but does not matter as
	    // not
	    // used very often...
	    javax.xml.parsers.DocumentBuilderFactory dbf = DocumentBuilderFactory
		    .newInstance();
	    dbf.setNamespaceAware(true);

	    // IMPL is DOM best to use here?
	    // IMPL needs to be overridden to remove some things
	    Document doc = dbf.newDocumentBuilder().newDocument();
	    Marshaller m = CEAJAXBContextFactory.newInstance()
		    .createMarshaller();
	    m.setProperty("com.sun.xml.bind.namespacePrefixMapper",
		    new NamespacePrefixMapperImpl());
	    if(schema != null){
	    m.setSchema(schema);
	    }
	     ValidationEventCollector handler  = new ValidationEventCollector();
             m.setEventHandler(handler );

	    m.marshal(element, doc);

	          if(handler.hasEvents())
	          {
	            logger.error("invalid - "+element);
	              for (int i = 0; i < handler.getEvents().length; i++) {
			ValidationEvent array_element = handler.getEvents()[i];
		    logger.error("validation error - "
			    + array_element.toString(), null);
		    }
	          }
// xsl transfrom

	    Source request = new DOMSource(doc);
	    DOMResult response = new DOMResult();
	    stylesheet.transform(request, response);

	    return (Document) response.getNode();
	} catch (Exception e) {
	    throw new MetadataException("problem marshalling metadata", e);
	} catch (TransformerFactoryConfigurationError e) {
	    throw new MetadataException("problem marshalling metadata", e);
	}

    }

    public static <T> T unmarshall(InputStream is, Class<T> clazz) throws JAXBException, IOException, SAXException, MetadataException {
      StreamSource s = new StreamSource(is);
      T umObj = unmarshall(s, clazz, true);
      return umObj;
    }

    public static <T> T unmarshall(InputStream is, Class<T> clazz, boolean validate) throws JAXBException, IOException, SAXException, MetadataException {
        StreamSource s = new StreamSource(is);
        T umObj = unmarshall(s, clazz, validate);
        return umObj;
      }

    
    public static <T> T unmarshall(Document doc, Class<T> clazz) throws JAXBException, IOException, SAXException, MetadataException {
	       return unmarshall(new DOMSource(doc), clazz, true);
	    }
   
    public static <T> T unmarshall(Reader rd, Class<T> clazz) throws JAXBException, IOException, SAXException, MetadataException {
        return unmarshall(new StreamSource(rd), clazz, true);
     }
    public static <T> T unmarshall(Reader rd, Class<T> clazz, boolean validate) throws JAXBException, IOException, SAXException, MetadataException {
        return unmarshall(new StreamSource(rd), clazz, validate);
     }

    
    private static <T> T unmarshall(Source s,Class<T> clazz, boolean validate) throws JAXBException, IOException, SAXException, MetadataException
    {
	T retval;
	logger.debug("unmarshalling to "+clazz.getSimpleName());
	Unmarshaller um = contextFactory
		.createUnmarshaller();
	if(validate){
	    logger.debug("finding schema to validate");
	Schema schema = findSchema(clazz);
	um.setSchema(schema);
	}
	else
	{
	    um.setSchema(null);
	}
	javax.xml.bind.util.ValidationEventCollector validationEventCollector = new javax.xml.bind.util.ValidationEventCollector();
	um.setEventHandler(validationEventCollector);

	JAXBElement<T> el = um.unmarshal(s, clazz);
	retval = el.getValue();
	    if (validationEventCollector.hasEvents()) {
	        StringBuffer errmsg = new StringBuffer();
	        for (ValidationEvent err : validationEventCollector.getEvents()) {
                    errmsg.append(err.toString());
                    errmsg.append("\n");
                }
		logger.error(errmsg.toString());
		throw new MetadataException("xml invalid for " + errmsg.toString());
	    }

	
	return retval;
	
    }
    private static <T> Schema findSchema(Class<T> clazz) {
	javax.xml.validation.SchemaFactory sf = SchemaFactory
		.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
	XmlSchema ann = clazz.getPackage().getAnnotation(
		javax.xml.bind.annotation.XmlSchema.class);
	try {
	    String namespace = ann.namespace();
	    URL url = SchemaMap.getSchemaURL(namespace);
	    Source schemas = new StreamSource(url.openStream(), url
	    	.toExternalForm());
	    Schema schema = sf.newSchema(schemas);
	    return schema;
	} catch (Exception e) {
	   logger.info("unable to find schema - validation will not occur", e);
	   return null;
	}
    }
    
    
    public String printXML(Document doc){
	    try
	          {
	            // Set up the output transformer
	            TransformerFactory transfac = TransformerFactory.newInstance();
	            Transformer trans = transfac.newTransformer();
	            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	            trans.setOutputProperty(OutputKeys.INDENT, "yes");
	      
	           // Print the DOM node
	     
	           StringWriter sw = new StringWriter();
	           StreamResult result = new StreamResult(sw);
	           DOMSource source = new DOMSource(doc);
	           trans.transform(source, result);
	           String xmlString = sw.toString();
	     
	           return xmlString;
	         }
	         catch (TransformerException e)
	         {
	           e.printStackTrace();
	           return null;
	         }
    }

 
}

/*
 * $Log: CEAJAXBUtils.java,v $
 * Revision 1.2  2009/02/26 12:25:48  pah
 * separate more out into cea-common for both client and server
 *
 * Revision 1.1  2008/10/06 12:12:37  pah
 * factor out classes common to server and client
 *
 * Revision 1.3  2008/09/24 13:39:17  pah
 * slightly better error reporting
 *
 * Revision 1.2  2008/09/03 14:18:56  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.6  2008/09/03 12:22:55  pah
 * improve unit tests so that they can run in single eclipse gulp
 *
 * Revision 1.1.2.5  2008/08/29 07:28:31  pah
 * moved most of the commandline CEC into the main server - also new schema for CEAImplementation in preparation for DAL compatible service registration
 *
 * Revision 1.1.2.4  2008/06/16 21:58:58  pah
 * altered how the description libraries fit together  - introduced the SimpleApplicationDescriptionLibrary to just plonk app descriptions into.
 *
 * Revision 1.1.2.3  2008/06/10 20:16:43  pah
 * annot do such strict schema validation as resources often have capabilities with types from several schema - it seems that jaxb can only cope with validating against a single schema.
 *
 * Revision 1.1.2.2  2008/06/10 20:01:39  pah
 * moved ParameterValue and friends to CEATypes.xsd
 * Revision 1.1.2.1 2008/05/13 15:57:32 pah uws with
 * full app running UI is working
 * 
 */
