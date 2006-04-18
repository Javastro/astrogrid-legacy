/*$Id: ApiHelpTest.java,v 1.2 2006/04/18 23:25:47 nw Exp $
 * Created on 25-Jul-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system;

import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.system.ApiHelp;
import org.astrogrid.desktop.framework.ACRTestSetup;

import org.apache.xmlrpc.XmlRpcException;

import java.util.Arrays;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/** test the apihelp interface
 * @author Noel Winstanley nw@jb.man.ac.uk 25-Jul-2005
 *
 */
public class ApiHelpTest extends TestCase {

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        ACR reg =getACR();
        assertNotNull(reg);
        help = (ApiHelp) reg.getService(ApiHelp.class);
        assertNotNull(help);
    }
    protected ApiHelp help;
    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    /**
     * @return
     */
    protected ACR getACR() throws Exception{
        return (ACR)ACRTestSetup.pico.getACR();
    }    

    public void testListMethods() {
        List l = Arrays.asList(help.listMethods());
        assertNotNull(l);
        assertTrue(l.contains("system.configuration.getKey"));
    }

    public void testListModules() {
        List l = Arrays.asList(help.listModules());
        assertNotNull(l);
        assertTrue(l.contains("system"));
    }

    public void testListComponentsOfModule() throws Exception {
        List l = Arrays.asList(help.listComponentsOfModule("builtin"));
        assertTrue(l.contains("builtin.shutdown"));
        l = Arrays.asList(help.listComponentsOfModule("system"));
        assertTrue(l.contains("system.configuration"));
    }

    public void testListMethodsOfComponent() throws Exception {
        List l = Arrays.asList(help.listMethodsOfComponent("builtin.shutdown"));
        assertTrue(l.contains("builtin.shutdown.halt"));
        l = Arrays.asList(help.listMethodsOfComponent("system.configuration"));
        assertTrue(l.contains("system.configuration.list"));
    }

    public void testMethodSignature() throws Exception {
        List result = Arrays.asList(help.methodSignature("system.configuration.list"));
        assertEquals(1,result.size());
        String[] sig1 = (String[])result.get(0);
        assertEquals(1,sig1.length);
        assertEquals("key-value map",sig1[0]); 
    }

    public void testModuleHelp() throws Exception {
       assertNotNull(help.moduleHelp("builtin"));
       assertNotNull(help.moduleHelp("system"));
    }

    public void testComponentHelp() throws Exception {
        assertNotNull(help.componentHelp("builtin.shutdown"));
        assertNotNull(help.componentHelp("system.configuration"));
    }

    public void testMethodHelp() throws Exception {
        assertNotNull(help.methodHelp("system.configuration.getKey"));
    }
    
    public void testMethodHelpForIntrospection() throws Exception {
        assertNotNull(help.methodHelp("system.listMethods"));
    }
    public static Test suite() {
        return new ACRTestSetup(new TestSuite(ApiHelpTest.class));
    }
}


/* 
$Log: ApiHelpTest.java,v $
Revision 1.2  2006/04/18 23:25:47  nw
merged asr development.

Revision 1.1.66.1  2006/03/22 18:01:31  nw
merges from head, and snapshot of development

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/