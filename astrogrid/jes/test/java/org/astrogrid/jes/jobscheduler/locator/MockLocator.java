/*$Id: MockLocator.java,v 1.4 2004/03/24 11:51:29 pah Exp $
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

import org.astrogrid.applications.delegate.Delegate;
import org.astrogrid.jes.JesException;
import org.astrogrid.jes.jobscheduler.Locator;
import org.astrogrid.workflow.beans.v1.Step;

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
    public String locateTool(Step js) throws JesException {
        return willSucceed ? Delegate.TEST_URI : null;
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