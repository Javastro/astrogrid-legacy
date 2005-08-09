/*$Id: ApplicationsSystemTest.java,v 1.1 2005/08/09 17:33:07 nw Exp $
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
import org.astrogrid.acr.astrogrid.Jobs;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.desktop.framework.ACRTestSetup;

import org.w3c.dom.Document;

import java.net.URI;
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
        for (int i = 0; i < a.length; i++) {
            ApplicationInformation other = apps.getApplicationInformation(a[i].getId());
            assertEquals(a[i],other);
        }
    }

    public void testGetDocumentation()  throws Exception {
        URI[] u = apps.list();
        for (int i = 0; i < u.length; i++) {
            String s = apps.getDocumentation(u[i]);
            assertNotNull(s);
        }
            
    }

    public void testCreateTemplateDocumentAndStruct()  throws Exception {
        URI[] u = apps.list();
        for (int i = 0; i < u.length; i++) {
            Document d = apps.createTemplateDocument(u[i],"default");
            assertNotNull(d);
            Map m = apps.createTemplateStruct(u[i],"default");
            assertNotNull(m);
            assertEquals(m,apps.convertDocumentToStruct(d));
            assertEquals(m,apps.convertDocumentToStruct(apps.convertStructToDocument(m)));
            apps.validate(d); // the template should be valid.
        }
    }




    public void testListProvidersOf()  throws Exception {
        URI[] u = apps.list();
        for (int i = 0; i < u.length; i++) {
            ResourceInformation[] a = apps.listProvidersOf(u[i]);
            assertNotNull(a);
            for (int j = 0; j < a.length; j++) {
                assertNotNull(a[j].getAccessURL());
            }
        }
    }
        
        public void testValidateStored()  throws Exception {
            fail("todo");
        }

    public void testSubmit()  throws Exception {
        fail("todo");
    }

    public void testSubmitTo()  throws Exception {
        fail("todo");
    }

    public void testSubmitStored()  throws Exception {
        fail("todo");
    }
    public void testSubmitStoredTo()  throws Exception {
        fail("todo");        
    }

    public void testCancel()  throws Exception {
        fail("todo");        
    }

    public void testGetExecutionInformation() throws Exception  {
        fail("todo");        
    }

    public void testGetResults()  throws Exception {
        fail("todo");        
    }

}


/* 
$Log: ApplicationsSystemTest.java,v $
Revision 1.1  2005/08/09 17:33:07  nw
finished system tests for ag components.
 
*/