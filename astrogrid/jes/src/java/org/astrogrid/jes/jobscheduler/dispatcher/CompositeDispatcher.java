/*$Id: CompositeDispatcher.java,v 1.2 2005/03/13 07:13:39 clq2 Exp $
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

import org.astrogrid.component.descriptor.ComponentDescriptor;
import org.astrogrid.jes.JesException;
import org.astrogrid.jes.jobscheduler.Dispatcher;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;

import junit.framework.Test;

/** A dispatcher that is a composite of a set og other dispatchers - and dispatches to the correct dispatcher accordingly!
 * could either hard-code the composite,  - but then would need to construct and initialize each item (not the container-based way).
 * or make it so that it's populated by the picocontainer from whatever dispathers are in the container - would require new interfaces to let the composite determine when each dispatcher applies 
 * so will come with a compromise for now - composite has hard-coded rules, but lets pico create the (fixed set) of composite items.
 * @todo later make it all assembled by picocontainer -  i.e. not a fixed number of dispatchers, but arbitrary list, and selection of correct dispatcher done using
 * some kind of strategy / chain pattern (is this the correct name?) 
 * @author Noel Winstanley nw@jb.man.ac.uk 07-Feb-2005
 *
 */
public class CompositeDispatcher implements Dispatcher, ComponentDescriptor {

    /** Construct a new CompositeDispatcher
     * 
     */
    public CompositeDispatcher(CeaApplicationDispatcher cea,ConeSearchDispatcher cone,SiapDispatcher siap,SsapDispatcher ssap) {
        super();
        this.cea = cea;
        this.cone = cone;
        this.siap = siap;
        this.ssap = ssap;
    }
    
    protected final Dispatcher cea;
    protected final Dispatcher cone;
    protected final Dispatcher siap;
    protected final Dispatcher ssap;

    /**
     * @see org.astrogrid.jes.jobscheduler.Dispatcher#dispatchStep(org.astrogrid.workflow.beans.v1.Workflow, org.astrogrid.workflow.beans.v1.Tool, java.lang.String)
     * @todo at moment, all steps are dispatched by (external)cea dispatcher - implement more fully, so that other dispatchers are fired when needed - 
     * based on tool name, etc. - probalby needs some work on the locators, so that more info on the type of service to call is passed into here.
     */
    public void dispatchStep(Workflow wf, Tool tool, String id)
            throws JesException {
        cea.dispatchStep(wf,tool,id);
    }

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getName()
     */
    public String getName() {
        return "Composite  Dispatcher";
    }

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        return "Composite over a set of dispatcher implementations. determines which to use based on hard-coded rules";
    }

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getInstallationTest()
     */
    public Test getInstallationTest() {
        return null;
    }

}


/* 
$Log: CompositeDispatcher.java,v $
Revision 1.2  2005/03/13 07:13:39  clq2
merging jes-nww-686 common-nww-686 workflow-nww-996 scripting-nww-995 cea-nww-994

Revision 1.1.2.1  2005/03/11 14:04:03  nw
added new kinds of dispatcher.
 
*/