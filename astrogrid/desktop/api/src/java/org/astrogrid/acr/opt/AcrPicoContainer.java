/*$Id: AcrPicoContainer.java,v 1.1 2005/08/11 10:15:01 nw Exp $
 * Created on 10-Aug-2005
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
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.builtin.ACR;

import org.picocontainer.ComponentAdapter;
import org.picocontainer.PicoContainer;
import org.picocontainer.PicoException;
import org.picocontainer.PicoInitializationException;
import org.picocontainer.PicoIntrospectionException;
import org.picocontainer.PicoVerificationException;
import org.picocontainer.PicoVisitor;
import org.picocontainer.defaults.AbstractComponentAdapter;
import org.picocontainer.defaults.AssignabilityRegistrationException;
import org.picocontainer.defaults.CachingComponentAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 10-Aug-2005
 *
 */
public class AcrPicoContainer implements PicoContainer {

    public static class AcrComponentAdapter extends AbstractComponentAdapter {
        private final ACR acr;

        public AcrComponentAdapter( Class arg1, ACR acr) throws AssignabilityRegistrationException {
            super(arg1, arg1);
            this.acr = acr;
        }

        public Object getComponentInstance(PicoContainer arg0) throws PicoInitializationException, PicoIntrospectionException {
            try {
                return acr.getService((Class)getComponentKey());
            } catch (InvalidArgumentException e) {
                throw new PicoInitializationException(e);
            } catch (NotFoundException e) {
                throw new PicoInitializationException(e);
            } catch (ACRException e) {
                throw new PicoInitializationException(e);
            }
        }

        public void verify(PicoContainer arg0) throws PicoIntrospectionException {
        }
    }

    /** Construct a new AcrPicoContainer
     * 
     */
    public AcrPicoContainer(ACR acr) {
        super();
        this.acr = acr;
    }
    
    protected final ACR acr;

    public Object getComponentInstance(Object arg0) {
        if (arg0.getClass() != Class.class) {
            throw new PicoInitializationException("Cannot get instances from keys other than classes");
        }
        return getComponentInstanceOfType((Class)arg0);
    }

    public Object getComponentInstanceOfType(Class arg0) {
        try {
            return acr.getService(arg0);
        } catch (InvalidArgumentException e) {
            return null;
        } catch (NotFoundException e) {
            return null;
        } catch (ACRException e) {
            throw new PicoInitializationException(e);
        }
    }

    public List getComponentInstances() {
        throw new PicoIntrospectionException("Can't list component instances");
    }
    public List getComponentInstancesOfType(Class arg0) throws PicoException {
        List l = new ArrayList();
        try {
            Object o = acr.getService(arg0);
            l.add(o);
        } catch (InvalidArgumentException e) {
            // do nothing.
        } catch (NotFoundException e) {
            // fair enough, do nothing.
        } catch (ACRException e) {
            throw new PicoInitializationException(e);
        }
        return l;
    }    

    public PicoContainer getParent() {
        // no parent.
        return null;
    }

    public ComponentAdapter getComponentAdapter(Object arg0) {
        if (arg0.getClass() != Class.class) {
            throw new PicoInitializationException("Cannot get adapters from keys other than classes");
        }
        return getComponentAdapterOfType((Class)arg0);        
    }

    public ComponentAdapter getComponentAdapterOfType(Class arg0) {
        return new CachingComponentAdapter(new AcrComponentAdapter( arg0, acr));
    }

    public Collection getComponentAdapters() {
        throw new PicoIntrospectionException("Can't list component instances");
    }

    public List getComponentAdaptersOfType(Class arg0) {
        List l = new ArrayList();
        l.add(getComponentAdapterOfType(arg0));
        return l;
    }

    public void verify() throws PicoVerificationException {
        // do nothing.
    }


    public void accept(PicoVisitor arg0) {
        // do nothing
    }

    public void start() {
        // do nothing
    }

    public void stop() {
        // do nothing
    }

    public void dispose() {
        // do nothing
    }

}


/* 
$Log: AcrPicoContainer.java,v $
Revision 1.1  2005/08/11 10:15:01  nw
finished split
 
*/