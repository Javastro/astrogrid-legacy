/*$Id: SsapDispatcher.java,v 1.2 2005/03/13 07:13:39 clq2 Exp $
 * Created on 07-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.dispatcher;

import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.component.CEAComponentManager;
import org.astrogrid.component.descriptor.ComponentDescriptor;
import org.astrogrid.jes.JesException;
import org.astrogrid.jes.jobscheduler.Dispatcher;
import org.astrogrid.jes.jobscheduler.dispatcher.inprocess.InProcessQueryService;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;

import junit.framework.Test;

/** Dispatcher that will execute in-process SSAP searches.
 * @author Noel Winstanley nw@jb.man.ac.uk 07-Feb-2005
 *
 */
public class SsapDispatcher  extends AbstractInProcessDispatcher implements    ComponentDescriptor {

    /** Construct a new InProcessConeSearchDispatcher
     * 
     */
    public SsapDispatcher(CEAComponentManager cea) {
        super(cea);
    }
    /**@todo implement
     * @see org.astrogrid.jes.jobscheduler.dispatcher.AbstractInProcessDispatcher#transformTool(org.astrogrid.workflow.beans.v1.Tool)
     */
    protected Tool transformTool(Tool tool) {
        return tool;
    }
    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getName()
     */
    public String getName() {
        return "SSAPDispatcher";
    }

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        return "Dispatcher for SSAP queries";
    }

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getInstallationTest()
     */
    public Test getInstallationTest() {
        return null;
    }



}


/* 
$Log: SsapDispatcher.java,v $
Revision 1.2  2005/03/13 07:13:39  clq2
merging jes-nww-686 common-nww-686 workflow-nww-996 scripting-nww-995 cea-nww-994

Revision 1.1.2.1  2005/03/11 14:04:03  nw
added new kinds of dispatcher.
 
*/