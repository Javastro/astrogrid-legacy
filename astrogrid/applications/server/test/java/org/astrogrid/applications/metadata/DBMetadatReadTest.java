/*
 * $Id: DBMetadatReadTest.java,v 1.4 2011/09/02 21:55:53 pah Exp $
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

import javax.xml.bind.JAXBException;
import org.astrogrid.applications.description.MetadataException;
import org.astrogrid.applications.description.base.ApplicationBase;
import org.astrogrid.applications.description.impl.CEADALService;
import org.astrogrid.applications.description.impl.CeaDBApplicationDefinition;
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
	InputStream stream = DBMetadatReadTest.class.getResourceAsStream("/TAPConfig.xml");
//	Unmarshaller um = CEAJAXBContextFactory.newInstance()
//	.createUnmarshaller();
//	 Object el = um.unmarshal(stream);
//	 System.out.println(el.toString());
	
	CEADALService dalService = CEAJAXBUtils.unmarshall(stream, CEADALService.class);
	ApplicationBase applicationDefinition = dalService.getApplicationDefinition();
	if (applicationDefinition instanceof CeaDBApplicationDefinition) {
            CeaDBApplicationDefinition dbApp = (CeaDBApplicationDefinition) applicationDefinition;
            assertEquals("the schema name should be", "stap", dbApp.getTableset().getSchema().get(0).getName());
        }
	else
	{
	    assertEquals("different object for applicationdefinition", CeaDBApplicationDefinition.class, applicationDefinition.getClass());
	}
    
    }
}


/*
 * $Log: DBMetadatReadTest.java,v $
 * Revision 1.4  2011/09/02 21:55:53  pah
 * result of merging the 2931 branch
 *
 * Revision 1.3.6.1  2009/07/15 10:01:00  pah
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2907
 * NEW - bug 2851: generalized DAL applications
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2851
 * NEW - bug 2931: upgrades for 2009.2
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2931
 * NEW - bug 2920: upgrade to uws 1.0
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2920
 *
 * Revision 1.3  2008/09/13 09:51:06  pah
 * code cleanup
 *
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
