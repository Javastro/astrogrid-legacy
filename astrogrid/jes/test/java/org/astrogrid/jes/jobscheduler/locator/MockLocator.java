/*$Id: MockLocator.java,v 1.7 2005/03/13 07:13:39 clq2 Exp $
 * Created on 13-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.locator;

import org.astrogrid.common.delegate.Delegate;
import org.astrogrid.jes.JesException;
import org.astrogrid.jes.jobscheduler.Locator;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;

/** mock implementation of a tool locator.
 * <p />
 * can be configured to fail. otherwise will always return address that corresponds to the dummy application controller delegate.
 * @author Noel Winstanley nw@jb.man.ac.uk 13-Feb-2004
 *
 */
public class MockLocator implements Locator {
    /** Construct a new MockToolLocator
     * 
     */
    public MockLocator() {
        this(true);
    }
    
    public MockLocator(boolean willSucceed) {
        this.willSucceed = willSucceed;
    }
    protected final boolean willSucceed;
    /**
     * @see org.astrogrid.jes.jobscheduler.ToolLocator#locateTool(org.astrogrid.jes.job.JobStep)
     */
    public String[] locateTool(Tool t) throws JesException {
        return willSucceed ? new String[]{Delegate.TEST_URI} : null;
    }
    /**
     * @see org.astrogrid.jes.jobscheduler.ToolLocator#getToolInterface(org.astrogrid.jes.job.JobStep)
     */
    public String getToolInterface(Step js) throws JesException {
        return willSucceed ? "toolInterface" : null;
    }
}


/* 
$Log: MockLocator.java,v $
Revision 1.7  2005/03/13 07:13:39  clq2
merging jes-nww-686 common-nww-686 workflow-nww-996 scripting-nww-995 cea-nww-994

Revision 1.6.96.1  2005/03/11 15:21:35  nw
adjusted locator so that it returns a list of endpoints to connect to.
we can get round-robin by shuffling the list.
dispatcher tries each endpoint in the list until can connect to one wihout throwing an exception.

Revision 1.6  2004/08/03 16:31:25  nw
simplified interface to dispatcher and locator components.
removed redundant implementations.

Revision 1.5  2004/07/01 11:20:07  nw
updated interface with cea - part of cea componentization

Revision 1.4  2004/03/24 11:51:29  pah
update the test for the new dummydelegate URI

Revision 1.3  2004/03/04 01:57:35  nw
major refactor.
upgraded to latest workflow object model.
removed internal facade
replaced community snippet with objects

Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.1  2004/02/27 00:29:00  nw
rearranging code

Revision 1.1.2.1  2004/02/17 12:57:11  nw
improved documentation
 
*/