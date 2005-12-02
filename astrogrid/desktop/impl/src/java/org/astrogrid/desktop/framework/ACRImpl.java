/*$Id: ACRImpl.java,v 1.8 2005/12/02 13:40:13 nw Exp $
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
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.builtin.Module;
import org.astrogrid.acr.builtin.Shutdown;
import org.astrogrid.common.namegen.InMemoryNameGen;
import org.astrogrid.common.namegen.NameGen;
import org.astrogrid.desktop.framework.descriptors.ComponentDescriptor;
import org.astrogrid.desktop.framework.descriptors.MethodDescriptor;
import org.astrogrid.desktop.framework.descriptors.ModuleDescriptor;
import org.astrogrid.desktop.modules.ag.MessageRecorderInternal;
import org.astrogrid.desktop.modules.ag.MessagingInternal;
import org.astrogrid.desktop.modules.system.Check;
import org.astrogrid.desktop.modules.system.ThrobbingInterceptor;

import org.aopalliance.intercept.MethodInterceptor;
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
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Default implementation fo a {@link ACR}
 * <p>
 * Basically a big map.
 * @author Noel Winstanley nw@jb.man.ac.uk 10-Mar-2005
 *
 */
public class ACRImpl implements MutableACR {
    private static final String IMPLEMENTATIONS_KEY = "implementations";
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(ACRImpl.class);

    /** Construct a new ModuleContainer
     * 
     */
    public ACRImpl() {
        super();        
        modules = new ListOrderedMap();
        DefaultModule m = buildBuiltinModule();
        modules.put(m.getDescriptor().getName(),m);
        m.start();
        notifyListeners(m); // probably haven't got any listeners yet.
    }
    
    /** constructs the builtin module - contains the registry itself, and the shutdown component. may add more later.
     * @return
     */
    private final DefaultModule buildBuiltinModule() {
        DefaultModule m = new DefaultModule();
        m.registerComponentInstance(MutableACR.class,this);
        m.registerComponentInstance(Shutdown.class,new ShutdownImpl(modules));
    
        ModuleDescriptor md = new ModuleDescriptor();
        md.setName("builtin");
        md.setDescription("Builtin components");
        ComponentDescriptor cd = new ComponentDescriptor();
        
        cd.setDescription("shutdown the system");
         cd.setName("shutdown");
         cd.setImplementationClass(ShutdownImpl.class);
         cd.setInterfaceClass(Shutdown.class);
         MethodDescriptor meth = new MethodDescriptor();
         cd.addMethod(meth);
                meth.setName("halt");
                meth.setDescription("bring the system to a halt");
         md.addComponent(cd);
         
         cd = new ComponentDescriptor();       
        cd.setDescription("module registry");
         cd.setName("modules");
         cd.setImplementationClass(this.getClass());
          cd.setInterfaceClass(MutableACR.class);  
          md.addComponent(cd);
        // no need to register any methods of registry - as all have complex parameter types 
        // and so can't be called exxternally anyhow.
        
   
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

        DefaultModule parent =(DefaultModule) modules.get(modules.lastKey());
        AspectableModule m1 = new AspectableModule(parent.pico);
        
        m1.setDescriptor(desc);
        // register components
        for (Iterator i = desc.componentIterator(); i.hasNext(); ) {
            ComponentDescriptor c = (ComponentDescriptor)i.next();
            logger.info("Processing " + c.getName());              
            // special case for aspects - register without any strangeness.
            if ( c.getImplementationClass() != null && MethodInterceptor.class.isAssignableFrom(c.getImplementationClass())) {
                logger.info("Registering " + c.getName() + " as aspect");
                m1.registerComponentImplementation(c.getImplementationClass());
                continue;
            } 
                
            Class impl= null;
            if (c.getImplementationClass() != null) {
                impl = c.getImplementationClass();
             } else {
                logger.info("Checking conditional component " + c.getName());
                impl  = selectImplementation(c);
             }
                if (impl != null) { // as there may be no suitable implementation
                    if (c.isSingleton()) {
                        m1.registerSingletonComponentImplementation(impl.getInterfaces(),impl);
                    } else{                       
                        m1.registerComponentImplementation(impl.getInterfaces(),impl);
                    }
                } else {
                    // no suitable implementation found - not going to instantiate this component,
                    // so remove it from the module descriptor.
                    i.remove();
                }            
        }

        // register interceptors.
        PointcutsFactory cuts = m1.getPointcutsFactory();
       
    ///   m1.registerInterceptor(cuts.allClasses(),publicMethods(cuts),new LoggingInterceptor());
        // @todo move registration of interceptors out of reg code into module.xml with all else.
        // @todo the throbber is currently registered as a method interceptor. will need to change this if start using other interceptors as well.
       m1.registerInterceptor(notBuiltinOrSystemOrInternal(cuts),publicMethods(cuts),ThrobbingInterceptor.class);
        
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
                    if ( im.getCheckClass() == null || ((Check)im.getCheckClass().newInstance()).check()) {
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
    
    
    private MethodPointcut publicMethods(PointcutsFactory cuts) {
        return new MethodPointcut() {
            public boolean picks(Method arg0) {
                return Modifier.isPublic(arg0.getModifiers());
            }
        };
    }
    
    private ClassPointcut notBuiltinOrSystemOrInternal(PointcutsFactory cuts) {
        return     cuts.not(
                cuts.union(
                        cuts.union(
                                cuts.packageName("org.astrogrid.desktop.framework")
                                , cuts.packageName("org.astrogrid.desktop.modules.system")
                        )
                        ,
                        cuts.union(
                        //cuts.className("\\.*Internal")// don't work
                        //cuts.packageName("org.astrogrid.desktop.modules.background")
                                cuts.instancesOf(MessageRecorderInternal.class)
                                , cuts.instancesOf(MessagingInternal.class)
                                )
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
     * @see org.astrogrid.acr.builtin.ACR#addNewModuleListener(org.astrogrid.desktop.framework.NewModuleListener)
     */
    public void addNewModuleListener(NewModuleListener l) {
        listeners.add(l);
        // fire set of current modules at the new listener 
        for (Iterator i = moduleIterator(); i.hasNext(); ) {
            l.newModuleRegistered(new NewModuleEvent(this,(Module)i.next()));
        }
    }

    /**
     * @see org.astrogrid.acr.builtin.ACR#removeNewModuleListener(org.astrogrid.desktop.framework.NewModuleListener)
     */
    public void removeNewModuleListener(NewModuleListener l) {
        listeners.remove(l);
    }

    /**
     * @throws ACRException
     * @see org.astrogrid.acr.builtin.ACR#getService(java.lang.Class)
     */
    public Object getService(Class interfaceClass) throws ACRException {
        DefaultModule latest = (DefaultModule)modules.get(modules.lastKey());
        try { 
        Object result = latest.getComponentInstanceOfType(interfaceClass);
        if (result == null) {
            throw new NotFoundException(interfaceClass.getName());
        }
        return result;
        } catch (ACRException e) {
            throw e;
        } catch (Throwable t) {
            throw new ACRException(t);
        }
        
    }
    
    /**
     * @see org.astrogrid.acr.builtin.ACR#getService(java.lang.String, java.lang.String)
     */
    public Object getService(String component) throws ACRException {
        try {
            Class clazz = Class.forName(component);
            return getService(clazz);
        } catch (ClassNotFoundException e) {
            //try it as a composite component name instead
            String[] names = component.split("\\.");                
            DefaultModule m = (DefaultModule)modules.get(names[0]);
            if ( m == null) {
                throw new NotFoundException(names[0]);
            }        
            Object service = m.getComponent(names[1]);
            if (service == null) {
                throw new NotFoundException(names[1]);
            }
            return service;
        }
    }

    /**
     * @see org.astrogrid.desktop.framework.MutableACR#getDescriptors()
     */
    public Map getDescriptors() {
        Map result = new HashMap(modules.size());    
        
        for (Iterator i = modules.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry e = (Map.Entry)i.next();
            ModuleDescriptor d = ((DefaultModule)e.getValue()).getDescriptor();
            result.put(e.getKey(), d);
        }
        return result;
    }



}


/* 
$Log: ACRImpl.java,v $
Revision 1.8  2005/12/02 13:40:13  nw
removed temporary executor

Revision 1.7  2005/11/29 12:10:49  nw
put in the executor for now - will take out again after.

Revision 1.6  2005/11/23 19:15:30  jdt
Extruded plastic.

Revision 1.5  2005/11/15 19:39:07  nw
merged in improvements from release branch.

Revision 1.4.2.1  2005/11/15 19:33:38  nw
allowed singlton and non-singleton components.

Revision 1.4  2005/11/10 12:05:05  nw
big change around for vo lookout

Revision 1.3  2005/11/01 09:19:46  nw
messsaging for applicaitons.

Revision 1.2  2005/08/25 16:59:58  nw
1.1-beta-3

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

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