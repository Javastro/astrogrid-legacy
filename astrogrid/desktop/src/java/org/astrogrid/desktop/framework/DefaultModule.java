/*$Id: DefaultModule.java,v 1.4 2005/08/05 11:46:55 nw Exp $
 * Created on 10-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.framework;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.builtin.Module;
import org.astrogrid.desktop.framework.descriptors.ComponentDescriptor;
import org.astrogrid.desktop.framework.descriptors.ModuleDescriptor;

import org.picocontainer.ComponentAdapter;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.Parameter;
import org.picocontainer.PicoContainer;
import org.picocontainer.PicoException;
import org.picocontainer.PicoRegistrationException;
import org.picocontainer.PicoVerificationException;
import org.picocontainer.PicoVisitor;
import org.picocontainer.defaults.DefaultPicoContainer;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/** Default implementation of a Module.
 * <p>
 * Internally uses a standard pico container
 * 
 * @author Noel Winstanley nw@jb.man.ac.uk 10-Mar-2005
 *
 */
public class DefaultModule  implements PicoContainer, Module, Serializable{


    /** Construct a new DefaultModule
     * @param arg0
     */
    public DefaultModule(PicoContainer arg0) {
       pico =createContainer(arg0);
    }
   
    protected MutablePicoContainer createContainer() {
        return new DefaultPicoContainer();
    }
    
    protected MutablePicoContainer createContainer(PicoContainer parent) {
        return new DefaultPicoContainer(parent);
    }
     
    protected final MutablePicoContainer pico;


    /* Construct a new DefaultModule
     * 
     */
    public DefaultModule() {
        pico = createContainer();
    }

    /**
     * @see org.astrogrid.acr.builtin.Module#getDescriptor()
     */
    public ModuleDescriptor getDescriptor() {
        return descriptor;
    }
    
    protected ModuleDescriptor descriptor;
    
    public void setDescriptor(ModuleDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    /**
     * @see org.astrogrid.acr.builtin.Module#getComponent(java.lang.String)
     */
    public Object getComponent(String componentName) throws ACRException {
        ComponentDescriptor cd = descriptor.getComponent(componentName);
        if (cd == null) {
            throw new NotFoundException(componentName);
        }
        try {
            Object result =  this.getComponentInstanceOfType(cd.getInterfaceClass());
            if (result == null) {
                throw new NotFoundException(cd.getInterfaceClass().getName());
            }
            return result;
        } catch (Throwable t) {
            throw new ACRException(t);
        }
    }
    


    /**
     * @see org.astrogrid.acr.builtin.Module#componentIterator()
     */
    public Iterator componentIterator() {
        final Iterator descriptions = descriptor.componentIterator();
        return new Iterator() {

            public void remove() {
                throw new UnsupportedOperationException();
            }

            public boolean hasNext() {
                return descriptions.hasNext();
            }

            public Object next() {
                ComponentDescriptor cDesc = (ComponentDescriptor) descriptions.next();                
                return getComponentInstanceOfType(cDesc.getInterfaceClass());
            }
        };
    }



    public void accept(PicoVisitor arg0) {
        this.pico.accept(arg0);
    }
    public boolean addChildContainer(PicoContainer arg0) {
        return this.pico.addChildContainer(arg0);
    }
    public void dispose() {
        this.pico.dispose();
    }
    public boolean equals(Object obj) {
        return this.pico.equals(obj);
    }
    public ComponentAdapter getComponentAdapter(Object arg0) {
        return this.pico.getComponentAdapter(arg0);
    }
    public ComponentAdapter getComponentAdapterOfType(Class arg0) {
        return this.pico.getComponentAdapterOfType(arg0);
    }
    public Collection getComponentAdapters() {
        return this.pico.getComponentAdapters();
    }
    public List getComponentAdaptersOfType(Class arg0) {
        return this.pico.getComponentAdaptersOfType(arg0);
    }
    public Object getComponentInstance(Object arg0) {
        return this.pico.getComponentInstance(arg0);
    }
    public Object getComponentInstanceOfType(Class arg0) {
        return this.pico.getComponentInstanceOfType(arg0);
    }
    public List getComponentInstances() {
        return this.pico.getComponentInstances();
    }
    public List getComponentInstancesOfType(Class arg0) throws PicoException {
        return this.pico.getComponentInstancesOfType(arg0);
    }
    public PicoContainer getParent() {
        return this.pico.getParent();
    }
    public int hashCode() {
        return this.pico.hashCode();
    }
    public MutablePicoContainer makeChildContainer() {
        return this.pico.makeChildContainer();
    }
    public ComponentAdapter registerComponent(ComponentAdapter arg0)
            throws PicoRegistrationException {
        return this.pico.registerComponent(arg0);
    }
    public ComponentAdapter registerComponentImplementation(Class arg0)
            throws PicoRegistrationException {
        return this.pico.registerComponentImplementation(arg0);
    }
    public ComponentAdapter registerComponentImplementation(Object arg0, Class arg1)
            throws PicoRegistrationException {
        return this.pico.registerComponentImplementation(arg0, arg1);
    }
    public ComponentAdapter registerComponentImplementation(Object arg0, Class arg1, Parameter[] arg2)
            throws PicoRegistrationException {
        return this.pico.registerComponentImplementation(arg0, arg1, arg2);
    }
    public ComponentAdapter registerComponentInstance(Object arg0) throws PicoRegistrationException {
        return this.pico.registerComponentInstance(arg0);
    }
    public ComponentAdapter registerComponentInstance(Object arg0, Object arg1)
            throws PicoRegistrationException {
        return this.pico.registerComponentInstance(arg0, arg1);
    }
    public boolean removeChildContainer(PicoContainer arg0) {
        return this.pico.removeChildContainer(arg0);
    }
    public void start() {
        this.pico.start();
    }
    public void stop() {
        this.pico.stop();
    }
    public String toString() {
        return this.pico.toString();
    }
    public ComponentAdapter unregisterComponent(Object arg0) {
        return this.pico.unregisterComponent(arg0);
    }
    public ComponentAdapter unregisterComponentByInstance(Object arg0) {
        return this.pico.unregisterComponentByInstance(arg0);
    }
    public void verify() throws PicoVerificationException {
        this.pico.verify();
    }
  
}


/* 
$Log: DefaultModule.java,v $
Revision 1.4  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.3  2005/04/27 13:42:41  clq2
1082

Revision 1.2.2.2  2005/04/25 11:18:51  nw
split component interfaces into separate package hierarchy
- improved documentation

Revision 1.2.2.1  2005/04/22 15:59:26  nw
made a star documenting desktop.

Revision 1.2  2005/04/13 12:59:11  nw
checkin from branch desktop-nww-998

Revision 1.1.2.4  2005/04/04 08:49:27  nw
working job monitor, tied into pw launcher.

Revision 1.1.2.3  2005/04/01 19:03:10  nw
beta of job monitor

Revision 1.1.2.2  2005/03/22 12:04:03  nw
working draft of system and ag components.

Revision 1.1.2.1  2005/03/18 12:09:31  nw
got framework, builtin and system levels working.
 
*/