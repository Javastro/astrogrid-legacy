/*$Id: DefaultModule.java,v 1.2 2005/04/13 12:59:11 nw Exp $
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

import org.astrogrid.desktop.framework.descriptors.ComponentDescriptor;
import org.astrogrid.desktop.framework.descriptors.ModuleDescriptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.nanocontainer.aop.AspectablePicoContainer;
import org.nanocontainer.aop.AspectablePicoContainerFactory;
import org.nanocontainer.aop.ClassPointcut;
import org.nanocontainer.aop.ComponentPointcut;
import org.nanocontainer.aop.MethodPointcut;
import org.nanocontainer.aop.PointcutsFactory;
import org.nanocontainer.aop.dynaop.DynaopAspectablePicoContainerFactory;
import org.picocontainer.ComponentAdapter;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.Parameter;
import org.picocontainer.PicoContainer;
import org.picocontainer.PicoException;
import org.picocontainer.PicoRegistrationException;
import org.picocontainer.PicoVerificationException;
import org.picocontainer.PicoVisitor;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 10-Mar-2005
 *
 */
class DefaultModule  implements AspectablePicoContainer, Module{


    /** Construct a new DefaultModule
     * @param arg0
     */
    public DefaultModule(PicoContainer arg0) {
       pico = factory.createContainer(arg0);
    }

    protected static final AspectablePicoContainerFactory factory = new DynaopAspectablePicoContainerFactory();
    
    protected final AspectablePicoContainer pico;


    /* Construct a new DefaultModule
     * 
     */
    public DefaultModule() {
        pico = factory.createContainer();
    }

    /**
     * @see org.astrogrid.desktop.framework.Module#getDescriptor()
     */
    public ModuleDescriptor getDescriptor() {
        return descriptor;
    }
    
    protected ModuleDescriptor descriptor;
    
    public void setDescriptor(ModuleDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    /**
     * @see org.astrogrid.desktop.framework.Module#getComponent(java.lang.String)
     */
    public Object getComponent(String componentName) {
        return this.getComponentInstance(componentName);
    }
    


    /**
     * @see org.astrogrid.desktop.framework.Module#componentIterator()
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
                return getComponentInstance(cDesc.getInterfaceClass());
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
    public PointcutsFactory getPointcutsFactory() {
        return this.pico.getPointcutsFactory();
    }
    public void registerInterceptor(ClassPointcut arg0, MethodPointcut arg1, Object arg2) {
        this.pico.registerInterceptor(arg0, arg1, arg2);
    }
    public void registerInterceptor(ClassPointcut arg0, MethodPointcut arg1, MethodInterceptor arg2) {
        this.pico.registerInterceptor(arg0, arg1, arg2);
    }
    public void registerInterceptor(ComponentPointcut arg0, MethodPointcut arg1, Object arg2) {
        this.pico.registerInterceptor(arg0, arg1, arg2);
    }
    public void registerInterceptor(ComponentPointcut arg0, MethodPointcut arg1, MethodInterceptor arg2) {
        this.pico.registerInterceptor(arg0, arg1, arg2);
    }
    public void registerInterfaces(ClassPointcut arg0, Class[] arg1) {
        this.pico.registerInterfaces(arg0, arg1);
    }
    public void registerInterfaces(ComponentPointcut arg0, Class[] arg1) {
        this.pico.registerInterfaces(arg0, arg1);
    }
    public void registerMixin(ClassPointcut arg0, Class arg1) {
        this.pico.registerMixin(arg0, arg1);
    }
    public void registerMixin(ClassPointcut arg0, Class[] arg1, Class arg2) {
        this.pico.registerMixin(arg0, arg1, arg2);
    }
    public void registerMixin(ComponentPointcut arg0, Class arg1) {
        this.pico.registerMixin(arg0, arg1);
    }
    public void registerMixin(ComponentPointcut arg0, Class[] arg1, Class arg2) {
        this.pico.registerMixin(arg0, arg1, arg2);
    }

    /**
     * @see org.astrogrid.desktop.framework.Module#getChain()
     */
    public String getChain() {
        
        return this.descriptor.getName() + (getParent() != null ? ", " + ((Module)getParent()).getChain(): "");
    }
}


/* 
$Log: DefaultModule.java,v $
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