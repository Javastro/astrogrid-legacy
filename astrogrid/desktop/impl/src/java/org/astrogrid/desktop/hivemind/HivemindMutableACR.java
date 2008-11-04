/*$Id: HivemindMutableACR.java,v 1.10 2008/11/04 14:35:49 nw Exp $
 * Created on 15-Mar-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.hivemind;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hivemind.ApplicationRuntimeException;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.builtin.ComponentDescriptor;
import org.astrogrid.acr.builtin.ModuleDescriptor;
import org.astrogrid.desktop.SplashWindow;
import org.astrogrid.desktop.framework.ACRInternal;
import org.astrogrid.desktop.framework.Module;

/** Implementation of {@link ACRInternal} using Hivemind.
 * <p/>
 *  provides access to AR modules by interfacing to hivemind.
 *  
 *  */
public class HivemindMutableACR implements ACRInternal {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(HivemindMutableACR.class);

    
    public HivemindMutableACR(final Map<String,ServiceBean> services,final Map<String,ModuleDescriptor> descriptors) {
        super();
        SplashWindow.reportProgress("Starting Astro Runtime background software...");
        this.servicesByName = services;
        this.servicesByClass = createInterfaceClassIndex(servicesByName);
        this.modules = createModules(servicesByName); 
        removeUnavailableComponents(descriptors); //necessary to copy dscriptors to a map implementation that is serializable.
        this.descriptors = new HashMap<String,ModuleDescriptor>(descriptors);
        
    }
    /** map between String and ServiceBean */
    protected final Map<String,ServiceBean> servicesByName;
    /** map between Class and service bean */
    protected final Map<Class, ServiceBean> servicesByClass;
    /** map between string and module descriptor */
    protected final Map<String,ModuleDescriptor> descriptors;
    /** map between string and module object */
    protected final Map<String, HivemindModuleAdapter> modules;

    private final  Map<Class, ServiceBean> createInterfaceClassIndex(final Map<String,ServiceBean> services) {
        final Map<Class, ServiceBean> result = new HashMap<Class, ServiceBean>(services.size());
        for (final ServiceBean sb : services.values()) {            
            result.put(sb.getInterface(), sb);
        }
        return result;
    }
    
    private final Map<String, HivemindModuleAdapter> createModules(final Map<String,ServiceBean> services) {
        final Map<String, HivemindModuleAdapter> result = new HashMap<String, HivemindModuleAdapter>();
        for (final ServiceBean sb : services.values()) {
            final String moduleName = sb.getModule().getModuleId();
            if (! result.containsKey(moduleName)) {
                result.put(moduleName,new HivemindModuleAdapter(moduleName));
            }
        }
        return result;
    }

    
    /** filters descriptors, to only those with components avalable */
    private final void removeUnavailableComponents(final Map<String, ModuleDescriptor> m) {
        for (final ModuleDescriptor d : m.values()) {
            final Module module = this.getModule(d.getName());
           
            inner:
            for (final Iterator j = d.componentIterator(); j.hasNext(); ) {
                final ComponentDescriptor c = (ComponentDescriptor)j.next();
                try {
                    final Object instance =module.getComponent(c.getInterfaceClass());
                    // if an implementation is present, and isn't a 'mock', then all is ok.
                    if (instance != null && ! Boolean.getBoolean(d.getName() + "." + c.getName() + ".disabled")) {
                        continue inner;
                    }
                } catch (final ACRException e) {
                    // not worried.
                	logger.debug("Failed when accessing component: " + c.getName(),e);
                }
                // fall through - if not continued by now, something is wrong - remove this one.
                logger.info("Removing component " + c.getName() + " - not available");
                j.remove();
            }
        }
    }
    
    /** adapter that makes a hivemind module look like our idea of a module
     * 
     *  - it's a little bit lazy to delegate to the mutable acr - but at least it
     *  keeps the code consistent.
     *  
     *  @todo - test it's safe to serialize an inner class without serializing the outer.s
     *  */
    private class HivemindModuleAdapter implements Module, Serializable {

        /**
		 * 
		 */
		private static final long serialVersionUID = -8775877307037598162L;

		public HivemindModuleAdapter(final String moduleName) {
            this.moduleName= moduleName;
        }
        private final String moduleName;
        
        public Object getComponent(final String name) throws ACRException {
            return HivemindMutableACR.this.getService(moduleName + "." + name);
        }

        public Object getComponent(final Class iface) throws ACRException {
            return HivemindMutableACR.this.getService(iface);           
        }

        public ModuleDescriptor getDescriptor() {
            return HivemindMutableACR.this.descriptors.get(moduleName);
        }
    }

    public Map<String, ModuleDescriptor> getDescriptors() {
        return descriptors;
    }

    public Module getModule(final String name) {
        return modules.get(name);
    }

    public Iterator<? extends Module> moduleIterator() {
        return modules.values().iterator();
    }
    

    
    public Object getService(final Class arg0) throws ACRException,NotFoundException {
        if (!arg0.isInterface()) {
            throw new NotFoundException("Not a service interface: " + arg0.getName());
        }
        if (! servicesByClass.containsKey(arg0)) {
            throw new NotFoundException(arg0.getName());
        }
        final ServiceBean sb = servicesByClass.get(arg0);
        try {
        return sb.getModule().getService(sb.getId(),arg0);
        } catch (final ApplicationRuntimeException e) {
            throw new ACRException(e);
        }
    }

    public Object getService(final String arg0) throws ACRException, InvalidArgumentException,
            NotFoundException {
       if (! servicesByName.containsKey(arg0)) {
           throw new NotFoundException(arg0);
       }
       final ServiceBean sb = servicesByName.get(arg0);
       try {
       return sb.getModule().getService(arg0,sb.getInterface());
       } catch (final ApplicationRuntimeException e) {
           throw new ACRException(e);
       }       
    }


}


/* 
$Log: HivemindMutableACR.java,v $
Revision 1.10  2008/11/04 14:35:49  nw
javadoc polishing

Revision 1.9  2008/09/25 16:04:04  nw
code improvements.

Revision 1.8  2008/03/05 10:55:50  nw
added progress reporting to splashscreen

Revision 1.7  2007/01/23 11:45:40  nw
logging.

Revision 1.6  2007/01/09 16:26:52  nw
minor

Revision 1.5  2006/06/15 09:41:01  nw
fixed minor behaviour bug

Revision 1.4  2006/06/02 00:16:15  nw
Moved Module, Component and Method-Descriptors from implementation code into interface. Then added methods to ApiHelp that provide access to these beans.

Revision 1.3  2006/05/17 23:57:46  nw
documentation improvements.

Revision 1.2  2006/04/18 23:25:43  nw
merged asr development.

Revision 1.1.2.3  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.1.2.2  2006/04/04 10:31:25  nw
preparing to move to mac.

Revision 1.1.2.1  2006/03/22 18:01:30  nw
merges from head, and snapshot of development
 
*/