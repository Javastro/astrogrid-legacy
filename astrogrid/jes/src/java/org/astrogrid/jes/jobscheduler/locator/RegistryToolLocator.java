/*$Id: RegistryToolLocator.java,v 1.1 2004/03/08 00:37:07 nw Exp $
 * Created on 08-Mar-2004
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
import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.registry.client.query.RegistryService;
import org.astrogrid.workflow.beans.v1.Step;

import org.exolab.castor.xml.ValidationException;

import java.net.URL;

import junit.framework.Test;

/** Tool locator that resolves tools using the registry.
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Mar-2004
 *
 */
public class RegistryToolLocator implements Locator, ComponentDescriptor {
    
    /** interface to configure registry with */
    public interface RegistryEndpoint {
        URL getURL();
    }
    
    public RegistryToolLocator(RegistryEndpoint endpoint) {
        url = endpoint.getURL();
        delegate = RegistryDelegateFactory.createQuery(url);
    }
    
    protected final URL url;
    protected final RegistryService delegate;
    
    /**
     * @see org.astrogrid.jes.jobscheduler.Locator#locateTool(org.astrogrid.workflow.beans.v1.Step)
     */
    public String locateTool(Step js) throws JesException {
        try {
            return delegate.getEndPointByIdentifier(js.getTool().getName());
        } catch (ValidationException e) {
            throw new JesException("Failed to locate tool due to error",e);
        }
    }
    /**
     * @see org.astrogrid.jes.jobscheduler.Locator#getToolInterface(org.astrogrid.workflow.beans.v1.Step)
     */
    public String getToolInterface(Step js) throws JesException {
        return null;
    }
    /**
     * @see org.astrogrid.jes.component.ComponentDescriptor#getName()
     */
    public String getName() {
        return "Registry Tool Locator";
    }
    /**
     * @see org.astrogrid.jes.component.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        return "Resolve Tool locations using an astrogrid registry" +            "\n Currently looking in registry at " + url.toString();
    }
    /** @todo check we can resolve endpoint 
     * @see org.astrogrid.jes.component.ComponentDescriptor#getInstallationTest()
     */
    public Test getInstallationTest() {
        return null;
    }
}


/* 
$Log: RegistryToolLocator.java,v $
Revision 1.1  2004/03/08 00:37:07  nw
preliminary implementation of registry tool locatr
 
*/