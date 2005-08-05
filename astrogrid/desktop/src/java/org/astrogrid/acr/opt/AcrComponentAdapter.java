/*$Id: AcrComponentAdapter.java,v 1.1 2005/08/05 11:46:55 nw Exp $
 * Created on 28-Jul-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr.opt;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.desktop.framework.descriptors.ComponentDescriptor;

import org.picocontainer.ComponentAdapter;
import org.picocontainer.PicoContainer;
import org.picocontainer.PicoInitializationException;
import org.picocontainer.PicoIntrospectionException;
import org.picocontainer.defaults.AbstractComponentAdapter;
import org.picocontainer.defaults.AssignabilityRegistrationException;

/**
 * component adapter that exposes an acr service provided by an acr instance (local or remote)
 * @author Noel Winstanley nw@jb.man.ac.uk 28-Jul-2005
 *
 */
public class AcrComponentAdapter extends AbstractComponentAdapter implements  ComponentAdapter {

    public AcrComponentAdapter(ACR acr,ComponentDescriptor cd) throws AssignabilityRegistrationException {
        super(cd.getInterfaceClass(), cd.getInterfaceClass());
        this.acr = acr;
    }
    protected final ACR acr;

    /**
     * @see org.picocontainer.ComponentAdapter#getComponentInstance(org.picocontainer.PicoContainer)
     */
    public Object getComponentInstance(PicoContainer arg0) throws PicoInitializationException, PicoIntrospectionException {
        try {
            return acr.getService((Class)this.getComponentKey());
        } catch (ACRException e) {
            throw new PicoInitializationException("Could not get service from acr",e);
        }
    }

    /**
     * @see org.picocontainer.ComponentAdapter#verify(org.picocontainer.PicoContainer)
     */
    public void verify(PicoContainer arg0) throws PicoIntrospectionException {
    }
}




/* 
$Log: AcrComponentAdapter.java,v $
Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/