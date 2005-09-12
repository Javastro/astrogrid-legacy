/*$Id: ParameterizedWorkflowLauncherTest.java,v 1.3 2005/09/12 15:21:16 nw Exp $
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

import org.astrogrid.workflow.beans.v1.Tool;

import org.exolab.castor.xml.Marshaller;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;

import junit.framework.TestCase;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 22-Mar-2005
 *
 */
public class ParameterizedWorkflowLauncherTest extends TestCase {

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        // produce an index file - need to generate dynamcially, so that references are correct 
        File tmpIndex = File.createTempFile("index",".xml");
        tmpIndex.deleteOnExit();
        PrintWriter pw = new PrintWriter( new FileWriter(tmpIndex));
        pw.println("<?xml version='1.0' ?>");
        pw.println("<index>");
        pw.println("<workflow-template>");
        pw.println(this.getClass().getResource("parameterizedworkflow.xml"));
        pw.println("</workflow-template>");
        pw.println("<workflow-template>");
        pw.println(this.getClass().getResource("plainworkflow.xml"));
        pw.println("</workflow-template>");        
        pw.println("</index>");
        pw.close();
     
        URL indexUrl = tmpIndex.toURL();
        pwl = new ParameterizedWorkflowLauncherImpl(null,null,null,null,null,null,null,indexUrl);
    
    }
    
    protected ParameterizedWorkflowLauncherImpl pwl;
    
    /** check iindex and ttemplates are getting in corrctly */ 
    public void testParser() throws Exception {
        assertEquals(1,pwl.templates.length); // only 1, as we expectthe other to fail to parse.
        assertNotNull(pwl.templates[0]);           
        }
    
    /** run the chooser ourselves */
    public void dontTestChooser() {
        ParameterizedWorkflowTemplate chosen =pwl.chooseTemplate();
        System.out.println(chosen);        
    }

    


}


/* 
$Log: ParameterizedWorkflowLauncherTest.java,v $
Revision 1.3  2005/09/12 15:21:16  nw
reworked application launcher. starting on workflow builder

Revision 1.2  2005/08/25 16:59:58  nw
1.1-beta-3

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.3  2005/08/05 11:46:56  nw
reimplemented acr interfaces, added system tests.

Revision 1.2  2005/04/27 13:42:41  clq2
1082

Revision 1.1.2.1  2005/04/25 11:18:51  nw
split component interfaces into separate package hierarchy
- improved documentation

Revision 1.2.2.1  2005/04/22 10:55:48  nw
adjusted to fit

Revision 1.2  2005/04/13 12:59:18  nw
checkin from branch desktop-nww-998

Revision 1.1.2.3  2005/04/04 16:43:48  nw
made frames remember their previous positions.
synchronized guiLogin, so only one login box ever comes up.
made refresh action in jobmonitor more robust

Revision 1.1.2.2  2005/03/29 16:10:59  nw
integration with the portal

Revision 1.1.2.1  2005/03/23 14:36:18  nw
got pw working
 
*/