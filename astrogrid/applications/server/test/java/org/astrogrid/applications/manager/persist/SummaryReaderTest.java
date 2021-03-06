/*
 * $Id: SummaryReaderTest.java,v 1.4 2008/10/06 12:16:16 pah Exp $
 * 
 * Created on 10 Mar 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.applications.manager.persist;

import java.io.IOException;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.helpers.DefaultValidationEventHandler;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import junit.framework.TestCase;

import org.astrogrid.applications.description.execution.ExecutionSummaryType;
import org.astrogrid.applications.description.jaxb.CEAJAXBContextFactory;
import org.astrogrid.applications.description.jaxb.NamespacePrefixMapperImpl;
import org.astrogrid.contracts.Namespaces;
import org.astrogrid.contracts.SchemaMap;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.SAXException;

public class SummaryReaderTest extends TestCase {

    public SummaryReaderTest(String name) {
	super(name);
    }

    @Override
    protected void setUp() throws Exception {
	super.setUp();
    }

    public void testRead() throws JAXBException, SAXException, IOException {
	JAXBContext jc = CEAJAXBContextFactory.newInstance();
	System.out.println(jc.toString());
	Unmarshaller um = jc.createUnmarshaller();
	System.out.println(jc.toString());
	javax.xml.validation.SchemaFactory sf = SchemaFactory
		.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);

	LSResourceResolver resourceResolver = new LSResourceResolver() {

	    public LSInput resolveResource(String type, String namespaceURI,
		    String publicId, String systemId, String baseURI) {
		System.out.println("ns=" + namespaceURI + " base=" + baseURI);
		LSInput retval = null;
		return retval;
	    }

	};
	sf.setResourceResolver(resourceResolver);
	URL url = SchemaMap.getSchemaURL(Namespaces.CEAT.getNamespace());
	Source schemas = new StreamSource(url.openStream(), url
		.toExternalForm());
	Schema schema = sf.newSchema(schemas);
	um.setSchema(schema);
	um.setEventHandler(new DefaultValidationEventHandler());
	// Unmarshall the file into a content object
	JAXBElement<?> o = (JAXBElement<?>) um.unmarshal(getClass()
		.getResourceAsStream("testsummary.xml"));
	ExecutionSummaryType c = (ExecutionSummaryType) o.getValue();
	System.out.println(c.getStartTime());

	Marshaller m = jc.createMarshaller();
	m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	m.setProperty("com.sun.xml.bind.namespacePrefixMapper",
		new NamespacePrefixMapperImpl());

	m.marshal(new JAXBElement(new QName(
		Namespaces.CEAT.getNamespace(), "executionSummary"),
		ExecutionSummaryType.class, c), System.out);
    }
}

/*
 * $Log: SummaryReaderTest.java,v $
 * Revision 1.4  2008/10/06 12:16:16  pah
 * factor out classes common to server and client
 *
 * Revision 1.3  2008/09/13 09:51:02  pah
 * code cleanup
 *
 * Revision 1.2  2008/09/03 14:18:59  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.1  2008/08/29 07:28:29  pah
 * moved most of the commandline CEC into the main server - also new schema for CEAImplementation in preparation for DAL compatible service registration
 *
 * Revision 1.1.2.2  2008/04/17 16:08:33  pah
 * removed all castor marshalling - even in the web service layer - unit tests passing
 *
 * ASSIGNED - bug 1611: enhancements for stdization holding bug
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
 * ASSIGNED - bug 2708: Use Spring as the container
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2708
 * ASSIGNED - bug 2739: remove dependence on castor/workflow objects
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2739
 *
 * Revision 1.1.2.1  2008/03/19 23:10:55  pah
 * First stage of refactoring done - code compiles again - not all unit tests passed
 *
 * ASSIGNED - bug 1611: enhancements for stdization holding bug
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
 *
 */
