/*
 * $Id: RegistryNamespaceTest.java,v 1.1 2004/09/01 13:53:40 pah Exp $
 * 
 * Created on 31-Aug-2004 by Paul Harrison (pah@jb.man.ac.uk)
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.registry.integration;

import java.net.URISyntaxException;
import java.net.URL;

import org.apache.axis.utils.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.astrogrid.registry.RegistryException;
import org.astrogrid.store.Ivorn;

import junit.framework.TestCase;

/**
 * Test the registry namespace compliance. The registry should not be relying on the namespace prefixes in the instance documents as these are arbitrary.
 * @author Paul Harrison (pah@jb.man.ac.uk) 31-Aug-2004
 * @version $Name:  $
 * @since iteration6
 */
public class RegistryNamespaceTest extends RegistryBaseTest {

    private URL url;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(RegistryNamespaceTest.class);
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        url = this.getClass().getResource("namespace1.xml");
    }

    /**
     * Constructor for RegistryNamespaceTest.
     * @param arg0
     */
    public RegistryNamespaceTest(String arg0) {
        super(arg0);
    }
    
    public void testLoadWithDifferentPrefix() throws RegistryException, URISyntaxException
    {
        ras.updateFromURL(url);
        Ivorn ivorn = new Ivorn("ivo://"+AUTHORITY_ID+"/"+RESOURCE_KEY+".namespace");
        Document dom = rs.getResourceByIdentifier(ivorn);
        assertNotNull("did not return the resource as DOM", dom);
        Element doc = dom.getDocumentElement();
        XMLUtils.PrettyElementToStream(doc, System.out);
        assertTrue("did not find the resource  - returned a null document - should have an error message before now",doc.hasChildNodes());
       
   } 

}
