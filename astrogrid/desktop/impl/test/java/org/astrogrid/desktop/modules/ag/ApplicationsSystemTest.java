/*$Id: ApplicationsSystemTest.java,v 1.1 2005/08/11 10:15:00 nw Exp $
 * Created on 09-Aug-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ag;

import org.astrogrid.acr.astrogrid.ApplicationInformation;
import org.astrogrid.acr.astrogrid.Applications;
import org.astrogrid.acr.astrogrid.ExecutionInformation;
import org.astrogrid.acr.astrogrid.Jobs;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.desktop.framework.ACRTestSetup;

import org.apache.axis.utils.XMLUtils;
import org.w3c.dom.Document;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 09-Aug-2005
 *
 */
public class ApplicationsSystemTest extends TestCase {

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        reg = getACR();
        apps = (Applications)reg.getService(Applications.class);
        assertNotNull(apps);
    }
    protected Applications apps;
    protected ACR reg;
    protected ACR getACR() throws Exception {
        return ACRTestSetup.pico.getACR();
    }
    
    public static Test suite() {
        return new ACRTestSetup(new TestSuite(ApplicationsSystemTest.class),true); // login.
    }    
    public void testList() throws Exception {
        URI[] a = apps.list();
        assertNotNull(a);
        assertTrue(a.length > 0);
        for (int i =0; i < a.length; i++) {
            assertNotNull(a[i]);
        }
    }

    public void testListFully()  throws Exception {
        ApplicationInformation[] a = apps.listFully();
        URI[] u = apps.list();
        assertNotNull(a);
        assertNotNull(u);
        assertEquals(a.length,u.length);
        for (int i = 0; i < a.length; i++) {
            assertEquals(u[i],a[i].getId());
            System.out.println(a[i]);
        }
    }

    public void testGetApplicationInformation()  throws Exception {
        ApplicationInformation[] a = apps.listFully();        
            ApplicationInformation other = apps.getApplicationInformation(a[0].getId());
            assertEquals(a[0],other);        
    }

    public void testGetDocumentation()  throws Exception {
        URI[] u = apps.list();
            String s = apps.getDocumentation(u[0]);
            assertNotNull(s);          
    }

    public void testCreateTemplateDocumentAndStruct()  throws Exception {
        URI[] u = apps.list();
        Document d = apps.createTemplateDocument(u[0],"default");
            assertNotNull(d);
            Map m = apps.createTemplateStruct(u[0],"default");
            assertNotNull(m);
            assertEquals(m,apps.convertDocumentToStruct(d));
            assertEquals(m,apps.convertDocumentToStruct(apps.convertStructToDocument(m)));
            apps.validate(d); // the template should be valid.
    }




    public void testListProvidersOf()  throws Exception {
        URI[] u = apps.list();
            ResourceInformation[] a = apps.listProvidersOf(u[0]);
            assertNotNull(a);
            for (int j = 0; j < a.length; j++) {
                assertNotNull(a[j].getAccessURL());
            }
    }
        
        public static final String TOOL_DOC = "tool.xml";
        public static final String SERVER_ID = "ivo://uk.ac.cam.ast/INT-WFS/images/CEC";
        public void testValidateStored()  throws Exception {
            URL u = this.getClass().getResource(TOOL_DOC);
            assertNotNull(u);
            apps.validateStored(new URI(u.toString()));
        }

    public void testSubmit()  throws Exception {
        InputStream is = this.getClass().getResourceAsStream(TOOL_DOC);
        assertNotNull(is);
        Document d = XMLUtils.newDocument(is);
        id = apps.submit(d);
        assertNotNull(id);
        ExecutionInformation e = apps.getExecutionInformation(id);
        assertNotNull(e);
        assertEquals(e.getId(),id);
        // pity times aren't recoreded.
        //assertNotNull(e.getStartTime());
        assertNotNull(e.getStatus());
    }

    public void testSubmitTo()  throws Exception {
        InputStream is = this.getClass().getResourceAsStream(TOOL_DOC);
        assertNotNull(is);
        Document d = XMLUtils.newDocument(is);
        id = apps.submitTo(d,new URI(SERVER_ID));
        assertNotNull(id);
        ExecutionInformation e = apps.getExecutionInformation(id);
        assertNotNull(e);
        assertEquals(e.getId(),id);
        //assertNotNull(e.getStartTime());
        assertNotNull(e.getStatus());
    }

    public void testSubmitStored()  throws Exception {
        URI u = new URI(this.getClass().getResource(TOOL_DOC).toString());
        assertNotNull(u);
        id = apps.submitStored(u);
        assertNotNull(id);
        ExecutionInformation e = apps.getExecutionInformation(id);
        assertNotNull(e);
        assertEquals(e.getId(),id);
        //assertNotNull(e.getStartTime());
        assertNotNull(e.getStatus());
    }
    private static URI id;
    public void testSubmitStoredTo()  throws Exception {
        URI u = new URI(this.getClass().getResource(TOOL_DOC).toString());
        assertNotNull(u);
        id = apps.submitStoredTo(u,new URI(SERVER_ID));
        assertNotNull(id);
        ExecutionInformation e = apps.getExecutionInformation(id);
        assertNotNull(e);
        assertEquals(e.getId(),id);
       // assertNotNull(e.getStartTime());
        assertNotNull(e.getStatus());
    }

    public void testCancel()  throws Exception {
        apps.cancel(id); // not much else we can test..        
    }


    public void testGetResults()  throws Exception {
        ExecutionInformation e= null;
        do {
            e= apps.getExecutionInformation(id);
            Thread.sleep(5000);
        } while(!(e.getStatus().equals(ExecutionInformation.COMPLETED) || e.getStatus().equals(ExecutionInformation.ERROR)));
        assertEquals(ExecutionInformation.COMPLETED,e.getStatus());
       // assertNotNull(e.getFinishTime());
        Map results = apps.getResults(id);
        assertNotNull(results);
        assertEquals(results.size(),1);
        assertTrue(results.containsKey("IMAGES"));
        InputStream is =  new ByteArrayInputStream(results.get("IMAGES").toString().getBytes());
        Document doc = XMLUtils.newDocument(is); // check we can parse the result;
        
    }

}


/* 
$Log: ApplicationsSystemTest.java,v $
Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/09 17:33:07  nw
finished system tests for ag components.
 
*/