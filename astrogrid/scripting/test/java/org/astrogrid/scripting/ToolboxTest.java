/*$Id: ToolboxTest.java,v 1.3 2004/11/30 15:39:56 clq2 Exp $
 * Created on 22-Nov-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.scripting;

import org.astrogrid.applications.delegate.CommonExecutionConnectorClient;
import org.astrogrid.applications.delegate.DelegateFactory;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;
import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;

import junit.framework.TestCase;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 22-Nov-2004
 *
 */
public class ToolboxTest extends TestCase {

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        t = new Toolbox();
    }
    protected Toolbox t;

    public void testGetStarTableBuilder() {
        assertNotNull(t.getStarTableBuilder());
    }

    public void testGetLogger() {
        assertNotNull(t.getLogger());
    }

    public void testGetProtocolLibrary() {
        ProtocolLibrary l = t.getProtocolLibrary();
        assertNotNull(l);
        assertTrue(l.listSupportedProtocols().length > 0);
    }

    public void testGetObjectBuilder() {
        assertNotNull(t.getObjectBuilder());
    }

    public void testGetXMLHelper() {
        assertNotNull(t.getXmlHelper());
    }

    public void testGetIOHelper() {
        assertNotNull(t.getIoHelper());
    }

    public void testGetSystemInfo() {
        assertNotNull(t.getSystemInfo());
    }

    public void testGetVersion() {
        assertNotNull(t.getVersion());
    }

    public void testGetSystemConfig() {
      assertNotNull(t.getSystemConfig());
    }

    public void testGetWorkflowManager() throws WorkflowInterfaceException {
        // can't test this - requires config.
        //assertNotNull(t.getWorkflowManager());
    }

    public void testCreateCeaClient() {
        assertNotNull(t.createCeaClient(CommonExecutionConnectorClient.TEST_URI));
    }

    public void testCreateRegistryClient() {
        assertNotNull(t.createRegistryClient());
    }

    public void testCreateRegistryAdminClient() {
        assertNotNull(t.createRegistryAdminClient());
    }

    public void testCreateVoSpaceClient() {
        assertNotNull(t.createVoSpaceClient(t.getObjectBuilder().user()));
    }

    public void testCreateTreeClient() {
        // can't do much here - will connect, which can't be unit tests.
    }

    /*
     * Class under test for String toString()
     */
    public void testToString() {
        assertNotNull(t.toString());
    }

}


/* 
$Log: ToolboxTest.java,v $
Revision 1.3  2004/11/30 15:39:56  clq2
scripting-nww-777

Revision 1.2.2.1  2004/11/26 15:38:16  nw
improved some names, added some missing methods.

Revision 1.2  2004/11/22 18:26:54  clq2
scripting-nww-715

Revision 1.1.2.1  2004/11/22 15:54:51  nw
deprecated existing scripting interface (which includes service lists).
produced new scripting interface, with more helpler objects.
 
*/