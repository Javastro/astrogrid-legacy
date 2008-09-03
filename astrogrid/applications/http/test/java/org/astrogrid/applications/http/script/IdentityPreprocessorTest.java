/* $Id: IdentityPreprocessorTest.java,v 1.7 2008/09/03 14:19:08 pah Exp $
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 * Created on Aug 10, 2004
 *
 */
package org.astrogrid.applications.http.script;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import junit.framework.TestCase;

import org.astrogrid.applications.description.MetadataException;
import org.astrogrid.applications.description.execution.Tool;
import org.astrogrid.applications.description.impl.CeaHttpApplicationDefinition;
import org.astrogrid.applications.description.impl.HttpMethodType;
import org.astrogrid.applications.description.impl.HttpURLType;
import org.astrogrid.applications.description.impl.WebHttpCall;
import org.astrogrid.applications.description.impl.WebHttpCall.SimpleParameter;
import org.astrogrid.applications.http.exceptions.HttpParameterProcessingException;
import org.astrogrid.applications.http.test.FileUnmarshaller;
import org.astrogrid.applications.http.test.TestRegistryQuerier;

/**
 * JUnit test for IdentityPreprocessor
 * @author jdt
 */
public class IdentityPreprocessorTest extends TestCase {

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }


    public final void testProcess() throws HttpParameterProcessingException, javax.xml.bind.MarshalException, javax.xml.bind.ValidationException, MetadataException, XMLStreamException, FactoryConfigurationError {
        TestRegistryQuerier querier = new TestRegistryQuerier(null);
        CeaHttpApplicationDefinition app 
            = querier.getHttpApplication("ivo://org.astrogrid.test/Adder");
        Tool tool = (Tool) new FileUnmarshaller(Tool.class).unmarshallFromFile("tool-eg.xml");
        assertNotNull(app);
        assertNotNull(tool);
        Preprocessor processor = new IdentityPreprocessor();
        WebHttpCall call = processor.process(tool, app);
        HttpURLType url = call.getURL();
        
        assertEquals(url.getMethod(), HttpMethodType.GET);
        assertEquals("http://127.0.0.1:8078/add",url.getValue());
        SimpleParameter[] simpleParameters = call.getSimpleParameter().toArray(new SimpleParameter[0]);
        assertEquals("Number of parameters incorrect",2,simpleParameters.length);
        SimpleParameter xParam;
        SimpleParameter yParam;
        if (simpleParameters[0].getName().equals("x")) {
            xParam = simpleParameters[0];
            yParam = simpleParameters[1];
        } else {
            xParam = simpleParameters[1];
            yParam = simpleParameters[0];            
        }
        assertEquals("x",xParam.getName());
        assertEquals("3",xParam.getValue());
        assertEquals("y",yParam.getName());
        assertEquals("5",yParam.getValue());
    }

}
