/*$Id: ConstantToolLocator.java,v 1.3 2004/03/07 21:04:38 nw Exp $
 * Created on 27-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.locator;

import org.astrogrid.jes.JesException;
import org.astrogrid.jes.component.ComponentDescriptor;
import org.astrogrid.jes.jobscheduler.Locator;
import org.astrogrid.workflow.beans.v1.Step;

import java.net.URL;

import junit.framework.Test;

/** Constant tool locator - always returns same endpoint.
 * @author Noel Winstanley nw@jb.man.ac.uk 27-Feb-2004
 *
 */
public class ConstantToolLocator implements Locator , ComponentDescriptor{
    /** Construct a new ConstantToolLocator
     * 
     */
    public ConstantToolLocator(URL endpoint,String interfaceName) {
        this.endpoint = endpoint;
        this.interfaceName = interfaceName;
    }
    
    protected final URL endpoint;
    protected final String interfaceName;
    /**
     * @see org.astrogrid.jes.jobscheduler.Locator#locateTool(org.astrogrid.jes.job.JobStep)
     */
    public String locateTool(Step js) throws JesException {
        return endpoint.toString();
        
    }
    /**
     * @see org.astrogrid.jes.jobscheduler.Locator#getToolInterface(org.astrogrid.jes.job.JobStep)
     */
    public String getToolInterface(Step js) throws JesException {
        return interfaceName;
    }
    /**
     * @see org.astrogrid.jes.component.ComponentDescriptor#getName()
     */
    public String getName() {
        return "ConstantToolLocator";
    }
    /**
     * @see org.astrogrid.jes.component.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        return "Always returns the same tool location\n" + 
            "set to\n endpoint: " + endpoint.toString() + "\n" + 
            "interface: " + interfaceName ;
    }
    /**
     * @see org.astrogrid.jes.component.ComponentDescriptor#getInstallationTest()
     */
    public Test getInstallationTest() {
        return null;
    }
}


/* 
$Log: ConstantToolLocator.java,v $
Revision 1.3  2004/03/07 21:04:38  nw
merged in nww-itn05-pico - adds picocontainer

Revision 1.2.4.1  2004/03/07 20:41:06  nw
added component descriptor interface impl,
refactored any primitive types passed into constructor

Revision 1.2  2004/03/04 01:57:35  nw
major refactor.
upgraded to latest workflow object model.
removed internal facade
replaced community snippet with objects

Revision 1.1  2004/03/03 01:13:42  nw
updated jes to work with regenerated workflow object model
 
*/