/*
 * $Id: DBMetadataQuerierTest.java,v 1.2 2011/09/02 21:55:53 pah Exp $
 * 
 * Created on 7 Jul 2009 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2009 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.db.description;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import javax.sql.DataSource;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import javax.xml.validation.Schema;

import net.ivoa.resource.dataservice.Table;
import net.ivoa.resource.dataservice.TableSchema;
import net.ivoa.resource.dataservice.TableSet;

import org.astrogrid.applications.description.jaxb.CEAJAXBUtils;
import org.astrogrid.contracts.Namespaces;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Document;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/DBspringTest.xml")
public class DBMetadataQuerierTest {

    @Autowired
    private DataSource ds;
    private DBMetadataQuerier q;
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        
        q = new DBMetadataQuerier(ds);
    }
    
    @Test
    public void testReadSchemaNames() throws Exception {
        List<String> schemaNames = q.listSchema();
        assertEquals("number of schema", 12, schemaNames.size());
        assertTrue("testing schema should be present", schemaNames.contains("TESTING"));
       
    }

    @Test
    public void testReadTableSet() throws Exception {
        TableSet ts = q.readTableSet("TESTING");
        assertNotNull(ts);
        TableSchema schema = ts.getSchema().get(0);
        assertEquals("number of tables", 3, schema.getTable().size());
     }

    @Test
    public void testMarshall() throws Exception {
        TableSet ts = q.readTableSet("TESTING");
        assertNotNull(ts);
        
        Schema schema = CEAJAXBUtils.findSchema(TableSet.class);
        
        Document doc = CEAJAXBUtils.marshall(new JAXBElement(new QName("", "DB"), Object.class, ts), CEAJAXBUtils.identityTransformer, schema);
        CEAJAXBUtils.printXML(doc, new OutputStreamWriter(System.out));
    }
}


/*
 * $Log: DBMetadataQuerierTest.java,v $
 * Revision 1.2  2011/09/02 21:55:53  pah
 * result of merging the 2931 branch
 *
 * Revision 1.1.2.1  2009/07/15 10:01:00  pah
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2907
 * NEW - bug 2851: generalized DAL applications
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2851
 * NEW - bug 2931: upgrades for 2009.2
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2931
 * NEW - bug 2920: upgrade to uws 1.0
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2920
 *
 */
