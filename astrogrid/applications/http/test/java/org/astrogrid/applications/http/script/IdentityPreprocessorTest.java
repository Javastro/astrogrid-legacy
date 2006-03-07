/* $Id: IdentityPreprocessorTest.java,v 1.4 2006/03/07 21:45:27 clq2 Exp $
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

import junit.framework.TestCase;

import org.apache.commons.httpclient.HttpURL;
import org.astrogrid.applications.beans.v1.HttpURLType;
import org.astrogrid.applications.beans.v1.SimpleParameter;
import org.astrogrid.applications.beans.v1.WebHttpCall;
import org.astrogrid.applications.beans.v1.types.HttpMethodType;
import org.astrogrid.applications.http.test.FileUnmarshaller;
import org.astrogrid.applications.http.test.TestRegistryQuerier;
import org.astrogrid.registry.beans.v10.cea.CeaHttpApplicationType;
import org.astrogrid.workflow.beans.v1.Tool;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

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


    public final void testProcess() throws MarshalException, ValidationException {
        TestRegistryQuerier querier = new TestRegistryQuerier(null);
        CeaHttpApplicationType app 
            = querier.getHttpApplication("org.astrogrid.test/Adder");
        Tool tool = (Tool) new FileUnmarshaller(Tool.class).unmarshallFromFile("tool-eg.xml");
        assertNotNull(app);
        assertNotNull(tool);
        Preprocessor processor = new IdentityPreprocessor();
        WebHttpCall call = processor.process(tool, app);
        HttpURLType url = call.getURL();
        
        assertEquals(url.getMethod(), HttpMethodType.GET);
        assertEquals("http://127.0.0.1:8078/add",url.getContent());
        SimpleParameter[] simpleParameters = call.getSimpleParameter();
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
