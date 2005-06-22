/*$Id: DefaultModuleRegistry.java,v 1.6 2005/06/22 08:48:52 nw Exp $
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
import org.astrogrid.acr.builtin.ModuleRegistry;
import org.astrogrid.acr.builtin.NewModuleEvent;
import org.astrogrid.acr.builtin.NewModuleListener;
import org.astrogrid.acr.builtin.Shutdown;
import org.astrogrid.desktop.framework.descriptors.ComponentDescriptor;
import org.astrogrid.desktop.framework.descriptors.MethodDescriptor;
import org.astrogrid.desktop.framework.descriptors.ModuleDescriptor;
import org.astrogrid.desktop.modules.system.Check;

import org.apache.commons.collections.OrderedMap;
import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nanocontainer.aop.ClassPointcut;
import org.nanocontainer.aop.MethodPointcut;
import org.nanocontainer.aop.PointcutsFactory;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/** Default implementation fo a {@link ModuleRegistry}
 * <p>
 * Basically a big map.
 * @author Noel Winstanley nw@jb.man.ac.uk 10-Mar-2005
 *
 */
public class DefaultModuleRegistry implements MutableModuleRegistry {
    private static final String IMPLEMENTATIONS_KEY = "implementations";
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
    
    /** constructs the builtin module - contains the registry itself, and the shutdown component. may add more later.
     * @return
     */
    private final Module buildBuiltinModule() {
        DefaultModule m = new DefaultModule();
        m.registerComponentInstance("modules",this);
        m.registerComponentInstance("shutdown",new ShutdownImpl(modules));
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
    
    /** register a new module
     * 
     * creates a module object from the descriptor, populates the module by registering components, 
     * and then registers aspects - at the moment, just a progress interceptor that turn the GUI throbber on at the start of a method call, 
     * and off again at the end.
     * 
     *  */
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
            if (c.getImplementationClass() != null) {
                m1.registerComponentImplementation(c.getName(),c.getImplementationClass());
            } else { // got a conditional component of some sort
                logger.info("Checking conditional component " + c.getName());
                Class impl  = selectImplementation(c);
                if (impl != null) { // as there may be no suitable implementation
                    m1.registerComponentImplementation(c.getName(),impl);
                }
            }
        }
        
        // register interceptors.
        PointcutsFactory cuts = m1.getPointcutsFactory();
    //    m1.registerInterceptor(cuts.allClasses(),publicMethods(cuts),new LoggingInterceptor());
        m1.registerInterceptor(notBuiltinOrSystem(cuts),publicMethods(cuts),"throbber");
        m1.start(); // order of these is important - as there may be reg listeners in m1  - so need to start it before notifying listners.      
        modules.put(m1.getDescriptor().getName(),m1);
        notifyListeners(m1);
        
    }
    /** method to handle case where there's more than one possible implementation of a component
     *  - and the right one needs to be chosen depending on some condition */
    private Class selectImplementation(ComponentDescriptor c) {
        String configDoc = c.getPropertyDocument(IMPLEMENTATIONS_KEY);
        if (configDoc == null) {
            logger.warn("Component " + c.getName() + " seems to have no implementation at all");
            return null;
        }
        implementationDigester.clear();
        implementationDigester.push(new ArrayList());
        try {            
            List alternatives = (List)implementationDigester.parse(new StringReader(configDoc));
            for (Iterator i = alternatives.iterator();i.hasNext(); ) {
                Implementation im = (Implementation)i.next();
                try {
                    if ( ((Check)im.getCheckClass().newInstance()).check()) {
                        return Class.forName(im.getImplementationClass());
                    }
                } catch (InstantiationException e1) {
                    logger.warn("InstantiationException",e1);
                } catch (IllegalAccessException e1) {
                    logger.warn("IllegalAccessException",e1);
                } catch (ClassNotFoundException e1) {
                    logger.warn("ClassNotFoundException",e1);
                }
            }
        } catch (IOException e) {
            logger.error("Could not parse implementations for " + c.getName() ,e);
        } catch (SAXException e) {
            logger.error("Could not parse implementations for " + c.getName(),e);
        }
        logger.info("Failed to find suitable implementation for " + c.getName());
        return null;
      
    }
    /** digester to parse block of implementation alternatives */
    private final Digester implementationDigester = new Digester() {{
        addObjectCreate("*/implementation",Implementation.class);
        addSetProperties("*/implementation");
        addSetNext("*/implementation","add");
    }};
    
    /** class representing an implementation alternative */
    public static class Implementation {
        private Class checkClass;
        private String implementationClass;
        public Class getCheckClass() {
            return this.checkClass;
        }
        public void setCheckClass(Class checkClass) {
            this.checkClass = checkClass;
        }
        public String getImplementationClass() {
            return this.implementationClass;
        }
        public void setImplementationClass(String implementationClass) {
            this.implementationClass = implementationClass;
        }
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
     * @see org.astrogrid.acr.builtin.ModuleRegistry#addNewModuleListener(org.astrogrid.desktop.framework.NewModuleListener)
     */
    public void addNewModuleListener(NewModuleListener l) {
        listeners.add(l);
        // fire set of current modules at the new listener 
        for (Iterator i = moduleIterator(); i.hasNext(); ) {
            l.newModuleRegistered(new NewModuleEvent(this,(Module)i.next()));
        }
    }

    /**
     * @see org.astrogrid.acr.builtin.ModuleRegistry#removeNewModuleListener(org.astrogrid.desktop.framework.NewModuleListener)
     */
    public void removeNewModuleListener(NewModuleListener l) {
        listeners.remove(l);
    }
}


/* 
$Log: DefaultModuleRegistry.java,v $
Revision 1.6  2005/06/22 08:48:52  nw
latest changes - for 1.0.3-beta-1

Revision 1.5  2005/05/12 15:59:11  clq2
nww 1111 again

Revision 1.3.8.1  2005/05/11 14:25:24  nw
javadoc, improved result transformers for xml

Revision 1.3  2005/04/27 13:42:41  clq2
1082

Revision 1.2.2.2  2005/04/25 11:18:51  nw
split component interfaces into separate package hierarchy
- improved documentation

Revision 1.2.2.1  2005/04/22 15:59:26  nw
made a star documenting desktop.

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