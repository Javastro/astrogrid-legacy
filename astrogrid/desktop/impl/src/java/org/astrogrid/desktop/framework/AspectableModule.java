/*$Id: AspectableModule.java,v 1.2 2005/11/15 19:39:07 nw Exp $
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

import org.astrogrid.acr.builtin.Module;

import org.aopalliance.intercept.MethodInterceptor;
import org.nanocontainer.aop.AspectablePicoContainer;
import org.nanocontainer.aop.AspectablePicoContainerFactory;
import org.nanocontainer.aop.AspectsContainer;
import org.nanocontainer.aop.AspectsManager;
import org.nanocontainer.aop.ClassPointcut;
import org.nanocontainer.aop.ComponentPointcut;
import org.nanocontainer.aop.MethodPointcut;
import org.nanocontainer.aop.PointcutsFactory;
import org.nanocontainer.aop.defaults.AspectsComponentAdapter;
import org.nanocontainer.aop.defaults.AspectsComponentAdapterFactory;
import org.nanocontainer.aop.dynaop.DynaopAspectablePicoContainerFactory;
import org.nanocontainer.aop.dynaop.InstanceMixinFactory;
import org.picocontainer.ComponentAdapter;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.Parameter;
import org.picocontainer.PicoContainer;
import org.picocontainer.PicoRegistrationException;
import org.picocontainer.alternatives.ImplementationHidingComponentAdapter;
import org.picocontainer.alternatives.ImplementationHidingComponentAdapterFactory;
import org.picocontainer.defaults.ComponentAdapterFactory;
import org.picocontainer.defaults.ConstructorInjectionComponentAdapter;
import org.picocontainer.defaults.ConstructorInjectionComponentAdapterFactory;
import org.picocontainer.defaults.DefaultPicoContainer;
import org.picocontainer.defaults.InstantiatingComponentAdapter;

import dynaop.Aspects;
import dynaop.Pointcuts;
import dynaop.ProxyFactory;

import java.io.Serializable;

/** Aspectable implementation of a Module.
 * <p>
 * internally ues an aspectable pico container- 
 * which allows aspects to be applied to invocations of component methods.
 * @author Noel Winstanley nw@jb.man.ac.uk 10-Mar-2005
 *
 */
public class AspectableModule  extends DefaultModule implements AspectablePicoContainer, Module, Serializable{


    /** Construct a new DefaultModule
     * @param arg0
     */
    public AspectableModule(PicoContainer arg0) {
        super(arg0);
    }
    
    public AspectableModule() {
        super();
    }    

    protected static final ImplementationHidingDynaopAspectablePicoContainerFactory factory =new ImplementationHidingDynaopAspectablePicoContainerFactory();
    /** adapttion of the standard aspectable pico container - puts an impl-hiding adapter in front of each component
     * rest of the code is cut-n-pasted from the baseclass.
     * @author Noel Winstanley nw@jb.man.ac.uk 28-Jul-2005
     *
     */
    private static class ImplementationHidingDynaopAspectablePicoContainerFactory extends DynaopAspectablePicoContainerFactory {
        public AspectablePicoContainer createContainer(Class containerClass, AspectsManager aspectsManager,
                ComponentAdapterFactory componentAdapterFactory, PicoContainer parent) {
            this.aspectsManager = aspectsManager;
                    ComponentAdapterFactory aspectsCaFactory = new ImplementationHidingComponentAdapterFactory (
                        new AspectsComponentAdapterFactory(aspectsManager,componentAdapterFactory), false);
                    MutablePicoContainer pico = createBasicContainer(containerClass, aspectsCaFactory, parent);

                    Aspects aspects = new Aspects();
                    aspects.mixin(Pointcuts.ALL_CLASSES, new Class[]{AspectsContainer.class}, new InstanceMixinFactory(aspectsManager));
                    aspects.interfaces(Pointcuts.ALL_CLASSES, new Class[]{AspectablePicoContainer.class});
                    
                    return (AspectablePicoContainer) ProxyFactory.getInstance(aspects).wrap(pico);
        }
        private AspectsManager aspectsManager;
        public ComponentAdapter createNonCachingComponentAdapter(Object key, Class impl) {
            return new ImplementationHidingComponentAdapter(
                    new AspectsComponentAdapter(aspectsManager,
                            new ConstructorInjectionComponentAdapter(key,impl))
                    ,false);
        }
        private MutablePicoContainer createBasicContainer(Class containerClass, ComponentAdapterFactory caFactory, PicoContainer parent) {
                    MutablePicoContainer temp = new DefaultPicoContainer();
                    temp.registerComponentImplementation(containerClass);
                    temp.registerComponentInstance(ComponentAdapterFactory.class, caFactory);
                    if (parent != null) {
                        temp.registerComponentInstance(PicoContainer.class, parent);
                    }
                    return (MutablePicoContainer) temp.getComponentInstance(containerClass);
        }
        
    };

    
    
    
    // uses the default component configuraiton - a singleton.
    public ComponentAdapter registerSingletonComponentImplementation(Object interfaces, Class impl) throws PicoRegistrationException{
        return this.pico.registerComponentImplementation(interfaces,impl);
    }

    // uses a component adapter without a cache - so a new item created at each call.
    public ComponentAdapter registerComponentImplementation(Object arg0, Class arg1)
            throws PicoRegistrationException {       
        return registerComponent(factory.createNonCachingComponentAdapter(arg0,arg1));
    }        
    
//  
    protected MutablePicoContainer createContainer() {
        return factory.createContainer();
    }
    
    
    protected MutablePicoContainer createContainer(PicoContainer parent) {
        return factory.createContainer(parent);
    }

    public PointcutsFactory getPointcutsFactory() {
        return ((AspectablePicoContainer)this.pico).getPointcutsFactory();
    }
    public void registerInterceptor(ClassPointcut arg0, MethodPointcut arg1, Object arg2) {
        ((AspectablePicoContainer)this.pico).registerInterceptor(arg0, arg1, arg2);
    }
    public void registerInterceptor(ClassPointcut arg0, MethodPointcut arg1, MethodInterceptor arg2) {
        ((AspectablePicoContainer)this.pico).registerInterceptor(arg0, arg1, arg2);
    }
    public void registerInterceptor(ComponentPointcut arg0, MethodPointcut arg1, Object arg2) {
        ((AspectablePicoContainer)this.pico).registerInterceptor(arg0, arg1, arg2);
    }
    public void registerInterceptor(ComponentPointcut arg0, MethodPointcut arg1, MethodInterceptor arg2) {
        ((AspectablePicoContainer)this.pico).registerInterceptor(arg0, arg1, arg2);
    }
    public void registerInterfaces(ClassPointcut arg0, Class[] arg1) {
        ((AspectablePicoContainer)this.pico).registerInterfaces(arg0, arg1);
    }
    public void registerInterfaces(ComponentPointcut arg0, Class[] arg1) {
        ((AspectablePicoContainer)this.pico).registerInterfaces(arg0, arg1);
    }
    public void registerMixin(ClassPointcut arg0, Class arg1) {
        ((AspectablePicoContainer)this.pico).registerMixin(arg0, arg1);
    }
    public void registerMixin(ClassPointcut arg0, Class[] arg1, Class arg2) {
        ((AspectablePicoContainer)this.pico).registerMixin(arg0, arg1, arg2);
    }
    public void registerMixin(ComponentPointcut arg0, Class arg1) {
        ((AspectablePicoContainer)this.pico).registerMixin(arg0, arg1);
    }
    public void registerMixin(ComponentPointcut arg0, Class[] arg1, Class arg2) {
        ((AspectablePicoContainer)this.pico).registerMixin(arg0, arg1, arg2);
    }




}


/* 
$Log: AspectableModule.java,v $
Revision 1.2  2005/11/15 19:39:07  nw
merged in improvements from release branch.

Revision 1.1.36.1  2005/11/15 19:33:38  nw
allowed singlton and non-singleton components.

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/05 11:46:55  nw
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