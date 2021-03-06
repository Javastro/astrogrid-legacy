/*$Id: MySpaceIntegrationTest.java,v 1.10 2005/03/14 22:03:53 clq2 Exp $
 * Created on 12-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.workflow.integration;

import org.astrogrid.integration.AbstractTestForIntegration;
import org.astrogrid.portal.workflow.intf.WorkflowStore;
import org.astrogrid.store.Ivorn;
import org.astrogrid.workflow.beans.v1.Workflow;

/** Tests functionality that workflow requires of myspace.
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Mar-2004
 *
 */
public class MySpaceIntegrationTest extends AbstractTestForIntegration {
    /**
     * Constructor for MySpaceItegrationTest.
     * @param arg0
     */
    public MySpaceIntegrationTest(String arg0) {
        super(arg0);
    }
    protected void setUp() throws Exception {
        super.setUp();
        store = ag.getWorkflowManager().getWorkflowStore();
        assertNotNull(store);
    }
    protected WorkflowStore store;
    
    public void testStore() throws Exception{

        //VoSpaceClient client = new VoSpaceClient(user);
                
        Ivorn location = createIVORN("/Workflow-MySpaceIntegrationTest.workflow-xml");
        store.saveWorkflow(user,location,wf);
        
        // get it back again.
        
        Workflow wf1 = store.readWorkflow(user,location);
        assertNotNull("read workflow is null",wf1);
        assertEquals("name of read workflow does not match",wf1.getName(),wf.getName());

    }
}


/* 
$Log: MySpaceIntegrationTest.java,v $
Revision 1.10  2005/03/14 22:03:53  clq2
auto-integration-nww-994

Revision 1.9.176.1  2005/03/11 17:17:17  nw
changed bunch of tests to use FileManagerClient instead of VoSpaceClient

Revision 1.9  2004/05/07 15:32:36  pah
more registry tests to flush out the fact that new entries are not being added

Revision 1.8  2004/04/23 00:27:56  nw
reorganized end-to-end tests. added test to verify flows are executed in parallel

Revision 1.7  2004/04/21 13:43:43  nw
tidied imports

Revision 1.6  2004/04/20 14:48:02  nw
no need for client.

Revision 1.5  2004/04/19 09:35:24  nw
added constants for ivorns of services.
added test query

Revision 1.4  2004/04/15 23:11:20  nw
tweaks

Revision 1.3  2004/04/14 15:28:47  nw
updated tests to fit with new WorkspaceStore interface

Revision 1.2  2004/04/08 14:50:54  nw
polished up the workflow integratioin tests

Revision 1.1  2004/03/16 17:48:34  nw
first stab at an auto-integration project
 
*/