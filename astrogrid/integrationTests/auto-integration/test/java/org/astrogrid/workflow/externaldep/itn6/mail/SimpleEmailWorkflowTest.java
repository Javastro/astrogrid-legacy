/*$Id: SimpleEmailWorkflowTest.java,v 1.4 2004/11/19 14:17:56 clq2 Exp $
 * Created on 17-Aug-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.workflow.externaldep.itn6.mail;

import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.integration.AbstractTestForWorkflow;

/** tests out a simple workflow that sends an email message.
 * .. going to need some trickery to find out whether the email was received at the other end.
 * @author Noel Winstanley nw@jb.man.ac.uk 17-Aug-2004
 *
 */
public class SimpleEmailWorkflowTest extends AbstractTestForWorkflow {

    /** Construct a new SimpleEmailWorkflowTest
     * @param applications
     * @param arg0
     */
    public SimpleEmailWorkflowTest(String arg0) {
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
        to.setValue("integration@twmbarlwm.star.le.ac.uk");
 
        ParameterValue message = (ParameterValue)mailTool.findXPathValue("input/parameter[name='message']");
        message.setIndirect(false);
        message.setValue("test message");

        ParameterValue subject = (ParameterValue)mailTool.findXPathValue("input/parameter[name='subject']");
        subject.setIndirect(false);
        subject.setValue("test message from " + this.getClass().getName());        
        
        
        // add to the workflow
        Step s = new Step();
        s.setDescription("email tool");
        s.setName("mail");
        s.setTool(mailTool);
        wf.getSequence().addActivity(s);        
    }
    
    public void checkExecutionResults(Workflow result) throws Exception {
        super.checkExecutionResults(result);
        //@todo  step itself doesn't return any value - need to find a way of reading the mailbox itself. hmm.
        
    }    

}


/* 
$Log: SimpleEmailWorkflowTest.java,v $
Revision 1.4  2004/11/19 14:17:56  clq2
roll back beforeMergenww-itn07-659

Revision 1.2  2004/08/27 13:18:27  nw
added todo

Revision 1.1  2004/08/17 16:16:17  nw
email test - need to find a way to check the mail has been received.
 
*/