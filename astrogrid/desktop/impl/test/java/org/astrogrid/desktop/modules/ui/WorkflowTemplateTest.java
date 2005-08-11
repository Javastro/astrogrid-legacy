/*$Id: WorkflowTemplateTest.java,v 1.1 2005/08/11 10:15:00 nw Exp $
 * Created on 22-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ui;

import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;

import org.exolab.castor.xml.Marshaller;

import java.io.InputStream;
import java.io.StringWriter;

import junit.framework.TestCase;

/** test parsing capabilities of workflow template.
 * @author Noel Winstanley nw@jb.man.ac.uk 22-Mar-2005
 *
 */
public class WorkflowTemplateTest extends TestCase {

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    public void testPlainWorkflow() throws Exception{
        InputStream is = this.getClass().getResourceAsStream("plainworkflow.xml");
        assertNotNull(is);
        try {
            new WorkflowTemplate(is);
            fail("expected to barf");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
    
    public void testParameterizedWorkflow() throws Exception {
        InputStream is = this.getClass().getResourceAsStream("parameterizedworkflow.xml");
        assertNotNull(is);
        WorkflowTemplate wt = new WorkflowTemplate(is);
        assertNotNull(wt.getDesc());
        System.out.println(wt);
    }
    
    public void testCreateTemplate() throws Exception {
        InputStream is = this.getClass().getResourceAsStream("parameterizedworkflow.xml");
        assertNotNull(is);
        WorkflowTemplate wt = new WorkflowTemplate(is);
        Tool t = wt.getDesc().createToolFromDefaultInterface();
        assertNotNull(t);
        assertTrue(t.isValid());
        StringWriter sw = new StringWriter();
        Marshaller.marshal(t,sw);
        System.out.println(sw);
    }
    
    public void testInstantiate() throws Exception {
        InputStream is = this.getClass().getResourceAsStream("parameterizedworkflow.xml");
        assertNotNull(is);
        WorkflowTemplate wt = new WorkflowTemplate(is);
        Tool t = wt.getDesc().createToolFromDefaultInterface();
        Account acc = new Account();
        acc.setCommunity("astrogrid.org");
        acc.setName("fred.bloggs");
        Workflow wf = wt.instantiate(acc,t);
        assertNotNull(wf);
        assertTrue(wf.isValid());
        // add in more checking of workflow here.
        StringWriter sw = new StringWriter();
        Marshaller.marshal(wf,sw);
        System.out.println(sw);
    }

}


/* 
$Log: WorkflowTemplateTest.java,v $
Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.3  2005/08/05 11:46:56  nw
reimplemented acr interfaces, added system tests.

Revision 1.2  2005/04/27 13:42:41  clq2
1082

Revision 1.1.2.1  2005/04/25 11:18:51  nw
split component interfaces into separate package hierarchy
- improved documentation

Revision 1.2  2005/04/13 12:59:18  nw
checkin from branch desktop-nww-998

Revision 1.1.2.1  2005/03/23 14:36:18  nw
got pw working
 
*/