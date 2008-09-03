/* $Id: FileUnmarshaller.java,v 1.3 2008/09/03 14:18:50 pah Exp $
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 * Created on Aug 11, 2004
 */
package org.astrogrid.applications.http.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.description.MetadataException;
import org.astrogrid.applications.description.jaxb.CEAJAXBContextFactory;
import org.astrogrid.contracts.SchemaMap;
import org.xml.sax.SAXException;

/**
 * Utility class to unmarshal a castor object from a filename relative to this
 * class
 * 
 * @author jdt
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 1 Apr 2008 converted to the JAXB stuff
 * 
  */
public class FileUnmarshaller {
    /**
     * Logger for this class
     */
    private static final Log log = LogFactory.getLog(FileUnmarshaller.class);

    /**
     * The class to be unmarshalled
     */
    private Class clazz;

    private String namespace;

    /**
     * Ctor
     * 
     * @param clazz
     *                the class to be unmarshalled
     */
    public FileUnmarshaller(final Class clazz) {
	if (log.isTraceEnabled()) {
	    log.trace("FileUnmarshaller(Class clazz = " + clazz + ") - start");
	}

	this.clazz = clazz;

	if (log.isTraceEnabled()) {
	    log.trace("FileUnmarshaller(Class) - end");
	}
	XmlSchema ann = clazz.getPackage().getAnnotation(javax.xml.bind.annotation.XmlSchema.class);
	namespace = ann.namespace();
    }

    /**
     * Unmarshalls a given class from an xml file
     * 
     * @param file
     *                filename relative to the location of this class
     * @return an object that you can cast to your Class clazz
     * @throws MetadataException
     * @throws FactoryConfigurationError
     * @throws XMLStreamException
     */
    public Object unmarshallFromFile(final String file)
	    throws MetadataException, XMLStreamException,
	    FactoryConfigurationError {
	if (log.isTraceEnabled()) {
	    log.trace("unmarshallFromFile(String file = " + file + ") - start");
	}

	InputStream is = this.getClass().getResourceAsStream(file);
	JAXBElement testApplication = null;
	try {
	    Unmarshaller um = CEAJAXBContextFactory.newInstance()
		    .createUnmarshaller();
	    javax.xml.validation.SchemaFactory sf = SchemaFactory
		    .newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
	    URL url = SchemaMap.getSchemaURL(namespace);
	    Source schemas = new StreamSource(url.openStream(), url
		    .toExternalForm()
		   );
	    Schema schema = sf.newSchema(schemas);
	    um.setSchema(schema);
	    javax.xml.bind.util.ValidationEventCollector validationEventCollector = new javax.xml.bind.util.ValidationEventCollector();
	    um.setEventHandler(validationEventCollector);

	    testApplication = um.unmarshal(new StreamSource(is), clazz);
	    if (validationEventCollector.hasEvents()) {
		log.error(validationEventCollector.toString());
		throw new MetadataException("xml invalid for " + file);
	    }
	} catch (JAXBException e1) {
	    log.error("problem ummarshalling", e1);
	    throw new MetadataException("could not unmarshal from " + file, e1);
	} catch (SAXException e) {
	    log.error("problem ummarshalling", e);
	    throw new MetadataException("could not unmarshal from " + file, e);
	} catch (IOException e) {
	    log.error("problem ummarshalling", e);
	    throw new MetadataException("could not unmarshal from " + file, e);
	} finally {
	    try {
		is.close();
	    } catch (IOException e) {
		log
			.debug(
				"unmarshallFromFile(String): failed to close source file, but it doesn't really matter",
				e);
	    }
	}

	if (log.isTraceEnabled()) {
	    log.trace("unmarshallFromFile(String) - end - return value = "
		    + testApplication);
	}
	return testApplication.getValue();

    }
    
    public Object unmarshallCastorFromFile(final String file) throws JAXBException, MetadataException, XMLStreamException, FactoryConfigurationError  {
       return unmarshallFromFile(file);
    }
}
