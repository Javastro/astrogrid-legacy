/*
 * $Id: ToolWriteTest.java,v 1.3 2008/10/06 12:16:16 pah Exp $
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

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.util.ValidationEventCollector;

import org.astrogrid.applications.description.jaxb.CEAJAXBContextFactory;
import org.astrogrid.applications.description.jaxb.NamespacePrefixMapperImpl;
import org.astrogrid.contracts.SchemaMap;
import org.astrogrid.test.AstrogridAssert;
import org.junit.Before;
import org.junit.Test;

public class ToolWriteTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testWrite() throws JAXBException {
	ValidationEventCollector handler = new ValidationEventCollector();

	Tool tool = new Tool();
	tool.setId("ivo://silly.auth/mytool");
	tool.setInterface("silly");
	JAXBContext jc = CEAJAXBContextFactory.newInstance();
	Marshaller m = jc.createMarshaller();
	m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	m.setProperty("com.sun.xml.bind.namespacePrefixMapper",
		new NamespacePrefixMapperImpl());
	StringWriter sw = new StringWriter();
	m.marshal(tool, sw);
	System.out.println(sw.toString());
	AstrogridAssert.assertSchemaValid(sw.toString(), "tool", SchemaMap.ALL);

    }

}

/*
 * $Log: ToolWriteTest.java,v $
 * Revision 1.3  2008/10/06 12:16:16  pah
 * factor out classes common to server and client
 *
 * Revision 1.2  2008/09/03 14:19:07  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.2  2008/05/17 16:45:01  pah
 * tidy tests to make sure more are passing
 * Revision 1.1.2.1 2008/05/08 22:40:53 pah basic
 * UWS working
 * 
 */
