/*
 * $Id: DBMetadatReadTest.java,v 1.2 2008/09/03 14:19:06 pah Exp $
 * 
 * Created on 17 Jun 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.metadata;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import net.ivoa.resource.dataservice.RelationalSchema;

import org.astrogrid.applications.description.MetadataException;
import org.astrogrid.applications.description.jaxb.CEAJAXBContextFactory;
import org.astrogrid.applications.description.jaxb.CEAJAXBUtils;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

public class DBMetadatReadTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testread() throws JAXBException, MetadataException, IOException, SAXException{
	InputStream stream = DBMetadatReadTest.class.getResourceAsStream("/DBdef.xml");
//	Unmarshaller um = CEAJAXBContextFactory.newInstance()
//	.createUnmarshaller();
//	 Object el = um.unmarshal(stream);
//	 System.out.println(el.toString());
	
	RelationalSchema schema = CEAJAXBUtils.unmarshall(stream, RelationalSchema.class);
	assertEquals("the name should be", "stap", schema.getName());
    }
}


/*
 * $Log: DBMetadatReadTest.java,v $
 * Revision 1.2  2008/09/03 14:19:06  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.1  2008/08/29 07:28:31  pah
 * moved most of the commandline CEC into the main server - also new schema for CEAImplementation in preparation for DAL compatible service registration
 *
 * Revision 1.1.2.1  2008/08/02 13:32:23  pah
 * safety checkin - on vacation
 *
 */
