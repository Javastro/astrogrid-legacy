/*$Id: AnnoyPaulWorkflowTest.java,v 1.1 2004/09/03 14:07:12 nw Exp $
 * Created on 03-Sep-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.workflow.externaldep.itn6.mail;

import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.workflow.beans.v1.For;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.integration.AbstractTestForWorkflow;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 03-Sep-2004
 *
 */
public class AnnoyPaulWorkflowTest extends AbstractTestForWorkflow {

    /** Construct a new AnnoyPaulWorkflowTest
     * @param applications
     * @param arg0
     */
    public AnnoyPaulWorkflowTest( String arg0) {
        super(new String[]{EMAIL}, arg0);
    }

    public static final String EMAIL = "org.astrogrid.localhost/sendmail";    

    /**
     * @see org.astrogrid.workflow.integration.AbstractTestForWorkflow#buildWorkflow()
     */
    protected void buildWorkflow() throws Exception {
        wf.setName(this.getClass().getName());
        ApplicationDescription desc = reg.getDescriptionFor(EMAIL);
        Tool mailTool = desc.createToolFromDefaultInterface();
        // populate tool.
        ParameterValue to = (ParameterValue)mailTool.findXPathValue("input/parameter[name='to']");
        to.setIndirect(false);
        to.setValue("pah@jb.man.ac.uk");
 
        ParameterValue message = (ParameterValue)mailTool.findXPathValue("input/parameter[name='message']");
        message.setIndirect(false);
        message.setValue("Helloooooo. ");

        ParameterValue subject = (ParameterValue)mailTool.findXPathValue("input/parameter[name='subject']");
        subject.setIndirect(false);
        subject.setValue("Astrogrid SPAM");        
        
        For f = new For();
        f.setVar("i");
        f.setItems("${1..100}");
        wf.getSequence().addActivity(f);
        
        // add to the workflow
        Step s = new Step();
        s.setDescription("email tool");
        s.setName("mail");
        s.setTool(mailTool);
        f.setActivity(s);                
    }

}


/* 
$Log: AnnoyPaulWorkflowTest.java,v $
Revision 1.1  2004/09/03 14:07:12  nw
because we can...
 
*/