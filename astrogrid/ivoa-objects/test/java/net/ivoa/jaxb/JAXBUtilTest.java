/*
 * $Id: JAXBUtilTest.java,v 1.2 2011/09/13 13:43:32 pah Exp $
 * 
 * Created on 13 Sep 2011 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2011 Manchester University. All rights reserved.
 *
 * This software is published under the terms of the Academic 
 * Free License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package net.ivoa.jaxb;

import static org.junit.Assert.*;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import net.ivoa.resource.registry.iface.VOResources;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

/**
 *  .
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 13 Sep 2011
 * @version $Revision: 1.2 $ $date$
 */
public class JAXBUtilTest {

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void unmarshaltest() throws JAXBException, IOException, SAXException {
        VOResources resources = IvoaJAXBUtils.unmarshall(this.getClass().getResourceAsStream("/VOResource.xml"), VOResources.class);
        assertNotNull("resource bundle", resources);
    }
}


/*
 * $Log: JAXBUtilTest.java,v $
 * Revision 1.2  2011/09/13 13:43:32  pah
 * result of merge of branch ivoa_pah_2931
 *
 * Revision 1.1.2.1  2011/09/13 13:32:58  pah
 * new test
 *
 */
