/*
 * $Id: JaxbTest.java,v 1.2 2011/09/13 13:43:32 pah Exp $
 * 
 * Created on 8 Jun 2011 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2011 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package net.ivoa.jaxb;

import static org.junit.Assert.*;

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

import net.ivoa.jpa.ResourceJpaController;
import net.ivoa.jpa.exceptions.PreexistingEntityException;
import net.ivoa.resource.Resource;
import net.ivoa.resource.registry.iface.VOResources;

import org.astrogrid.contracts.Namespaces;
import org.astrogrid.contracts.SchemaMap;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.SAXException;

public class JaxbTest {

    private static JAXBContext jc;
    private static Unmarshaller um;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        jc = IvoaJAXBContextFactory.newInstance();
        System.out.println(jc.toString());
        um = jc.createUnmarshaller();
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
        URL url = SchemaMap.getSchemaURL(Namespaces.RI.getNamespace());
        Source schemas = new StreamSource(url.openStream(), url
                .toExternalForm());
        Schema schema = sf.newSchema(schemas);
        um.setSchema(schema);
        um.setEventHandler(new DefaultValidationEventHandler());
    }

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testReadXML() throws JAXBException, IOException, SAXException{
        // Unmarshall the file into a content object
        VOResources vr =  (VOResources) um.unmarshal(getClass()
                .getResourceAsStream("/VOResource.xml"));
        System.out.println(vr.getResource().get(0).getIdentifier());

        Marshaller m = jc.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        m.setProperty("com.sun.xml.bind.namespacePrefixMapper",
                new NamespacePrefixMapperImpl());

        m.marshal(new JAXBElement(new QName(
                "http://www.ivoa.net/xml/RegistryInterface/v1.0", "VOResources"),
                VOResources.class, vr), System.out);
        
    }
    @Test
    public void testWriteToDB() throws PreexistingEntityException, Exception{
        // Unmarshall the file into a content object
        VOResources vr =  (VOResources) um.unmarshal(getClass()
                .getResourceAsStream("/VOResource.xml"));
        System.out.println(vr.getResource().get(0).getIdentifier());
        ResourceJpaController jpacont = new ResourceJpaController();
        for (Resource res : vr.getResource()) {
            jpacont.create(res);
        }

       
    }
}


/*
 * $Log: JaxbTest.java,v $
 * Revision 1.2  2011/09/13 13:43:32  pah
 * result of merge of branch ivoa_pah_2931
 *
 * Revision 1.1.2.1  2011/06/09 22:18:52  pah
 * basic VOResource schema nearly done - but not got save/recall working
 *
 */
