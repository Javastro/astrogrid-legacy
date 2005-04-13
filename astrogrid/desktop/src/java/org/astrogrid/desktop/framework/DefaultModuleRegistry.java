/*$Id: DefaultModuleRegistry.java,v 1.2 2005/04/13 12:59:11 nw Exp $
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
import org.astrogrid.desktop.framework.descriptors.MethodDescriptor;
import org.astrogrid.desktop.framework.descriptors.ModuleDescriptor;

import org.apache.commons.collections.OrderedMap;
import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nanocontainer.aop.ClassPointcut;
import org.nanocontainer.aop.MethodPointcut;
import org.nanocontainer.aop.PointcutsFactory;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/** Container for a set of modules.
 * @author Noel Winstanley nw@jb.man.ac.uk 10-Mar-2005
 *
 */
public class DefaultModuleRegistry implements MutableModuleRegistry {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(DefaultModuleRegistry.class);

    /** Construct a new ModuleContainer
     * 
     */
    public DefaultModuleRegistry() {
        super();        
        modules = new ListOrderedMap();
        Module m = buildBuiltinModule();
        modules.put(m.getDescriptor().getName(),m);
        m.start();
        notifyListeners(m); // probably haven't got any listeners yet.
    }
    
    /**
     * @return
     */
    private final Module buildBuiltinModule() {
        DefaultModule m = new DefaultModule();
        m.registerComponentInstance("modules",this);
        m.registerComponentInstance("shutdown",new ShutdownImpl(this, modules));
        ModuleDescriptor md = new ModuleDescriptor();
        md.setName("builtin");
        md.setDescription("Builtin components");
        
        md.addComponent(new ComponentDescriptor(){{
            setDescription("module registry");
            setName("modules");
            setImplementationClass(this.getClass());
            setInterfaceClass(MutableModuleRegistry.class);
        }});
        
        // no need to register any methods of registry - as all have complex parameter types 
        // and so can't be called exxternally anyhow.
        
        md.addComponent(new ComponentDescriptor(){{
            setDescription("shutdown the system");
            setName("shutdown");
            setImplementationClass(ShutdownImpl.class);
            setInterfaceClass(Shutdown.class);            
            addMethod(new MethodDescriptor() {{
                setName("halt");
                setDescription("bring the system to a halt");
            }});                       
        }});

        m.setDescriptor(md);
        return m;
    }

    protected final OrderedMap modules ;
    
    public void register(ModuleDescriptor desc) {
        logger.info("Building module for " + desc.getName());
       // logger.debug(desc);
        // make the parent container an aggregate to all for now. - will restrict later, when introducing security, sandboxes, etc.
        // @todo bt of an issue here - strictly speaking, don't need aggregate - as all moduels are added in sequence, 
        // they form a chain. however, this will change when we implement sandboxing, etc. I think aggregates will be needed then. dunno.
        // could maybe so same thing by just getting correct component adapters, and stuffing them in a new container. issues with dependency chains too..
        //PicoContainer agg = new AggregatePicoContainer(modules.values());
        // just get the last one added for now..
        DefaultModule parent =(DefaultModule) modules.get(modules.lastKey());
        //logger.info("Ancestor:" + parent.getChain());
        DefaultModule m1 = new DefaultModule(parent.pico);
        
        m1.setDescriptor(desc);
        // register components
        for (Iterator i = desc.componentIterator(); i.hasNext(); ) {
            ComponentDescriptor c = (ComponentDescriptor)i.next();
            logger.info("Processing " + c.getName());                        
            m1.registerComponentImplementation(c.getName(),c.getImplementationClass());     
        }
        
        // register interceptors.
        PointcutsFactory cuts = m1.getPointcutsFactory();
    //    m1.registerInterceptor(cuts.allClasses(),publicMethods(cuts),new LoggingInterceptor());
        m1.registerInterceptor(notBuiltinOrSystem(cuts),publicMethods(cuts),"throbber");
        m1.start(); // order of these is important - as there may be reg listeners in m1        
        modules.put(m1.getDescriptor().getName(),m1);
        notifyListeners(m1);
        
    }
    
    /** @todo refine to public methods */
    private MethodPointcut publicMethods(PointcutsFactory cuts) {
        return cuts.allMethods();
    }
    
    private ClassPointcut notBuiltinOrSystem(PointcutsFactory cuts) {
        return cuts.not(
          cuts.union(
                  cuts.packageName("org.astrogrid.desktop.framework")
                  , cuts.packageName("org.astrogrid.desktop.modules.system")
                  )      
        );
    }

    public Module getModule(String name) {
        return (Module)modules.get(name);
    }
    
    public Iterator moduleIterator() {
        return modules.values().iterator();
    }

    protected Set listeners = new HashSet();
    
    private void notifyListeners(Module m) {
        NewModuleEvent e = new NewModuleEvent(this,m);
        for (Iterator i = listeners.iterator(); i.hasNext();) {
            ((NewModuleListener)i.next()).newModuleRegistered(e);            
        }
    }
    
    /**
     * @see org.astrogrid.desktop.framework.ModuleRegistry#addNewModuleListener(org.astrogrid.desktop.framework.NewModuleListener)
     */
    public void addNewModuleListener(NewModuleListener l) {
        listeners.add(l);
        // fire set of current modules at the new listener 
        for (Iterator i = moduleIterator(); i.hasNext(); ) {
            l.newModuleRegistered(new NewModuleEvent(this,(Module)i.next()));
        }
    }

    /**
     * @see org.astrogrid.desktop.framework.ModuleRegistry#removeNewModuleListener(org.astrogrid.desktop.framework.NewModuleListener)
     */
    public void removeNewModuleListener(NewModuleListener l) {
        listeners.remove(l);
    }
}


/* 
$Log: DefaultModuleRegistry.java,v $
Revision 1.2  2005/04/13 12:59:11  nw
checkin from branch desktop-nww-998

Revision 1.1.2.5  2005/04/04 08:49:27  nw
working job monitor, tied into pw launcher.

Revision 1.1.2.4  2005/04/01 19:03:10  nw
beta of job monitor

Revision 1.1.2.3  2005/03/22 12:04:03  nw
working draft of system and ag components.

Revision 1.1.2.2  2005/03/18 15:47:37  nw
worked in swingworker.
got community login working.

Revision 1.1.2.1  2005/03/18 12:09:31  nw
got framework, builtin and system levels working.
 
*/