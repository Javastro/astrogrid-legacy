/*
 * $Id: ToolReadTest.java,v 1.2 2008/09/03 14:19:07 pah Exp $
 * 
 * Created on 1 May 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.description.execution;


import java.net.URL;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.util.ValidationEventCollector;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import static junit.framework.Assert.*;

import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.description.jaxb.CEAJAXBContextFactory;
import org.astrogrid.contracts.Namespaces;
import org.astrogrid.contracts.SchemaMap;
import org.junit.Before;
import org.junit.Test;

public class ToolReadTest {

    @Before
    public void setUp() throws Exception {
    }
    
    @Test
    public void testRead(){
	ValidationEventCollector handler = new ValidationEventCollector();
	try {
		Tool tool;
		    JAXBContext jc = CEAJAXBContextFactory.newInstance();
		    Unmarshaller um = jc.createUnmarshaller();
		    javax.xml.validation.SchemaFactory sf = SchemaFactory
			    .newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);

		    URL url = SchemaMap.getSchemaURL(Namespaces.CEAT.getNamespace());
		    Source schemas = new StreamSource(url.openStream(), url
			    .toExternalForm());
		    Schema schema = sf.newSchema(schemas);
		    um.setSchema(schema);
		    um.setEventHandler(handler);
		    // Unmarshall the file into a content object
		    tool = (Tool) um
			    .unmarshal(getClass()
					.getResourceAsStream("/testTool.xml"));
		    
		} catch (Exception e) {
		    System.err.println("error reading tool");
		    if (handler.hasEvents()) {
			for (int i = 0; i < handler.getEvents().length; i++) {
			    ValidationEvent event = handler.getEvents()[i];
			    System.err.println(event.toString());
			}

		    }
		    e.printStackTrace();
		    fail(e.getMessage());

    }
    }

}


/*
 * $Log: ToolReadTest.java,v $
 * Revision 1.2  2008/09/03 14:19:07  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.1  2008/05/08 22:40:53  pah
 * basic UWS working
 *
 */
