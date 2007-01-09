/*$Id: HivemindMutableACR.java,v 1.6 2007/01/09 16:26:52 nw Exp $
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
import org.astrogrid.desktop.framework.ACRInternal;
import org.astrogrid.desktop.framework.Module;

/** MutableACR implementation for hivemind system
 * 
 *  provides access to AR modules by interfacing to hivemind.
 *  
 *  */
public class HivemindMutableACR implements ACRInternal {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(HivemindMutableACR.class);

    
    public HivemindMutableACR(Map services,Map descriptors) {
        super();
        this.servicesByName = services;
        this.servicesByClass = createInterfaceClassIndex(services);
        this.modules = createModules(services); 
        removeUnavailableComponents(descriptors); //necessary to copy dscriptors to a map implementation that is serializable.
        this.descriptors = new HashMap(descriptors);

    }
    /** map between String and ServiceBean */
    protected final Map servicesByName;
    /** map between Class and service bean */
    protected final Map servicesByClass;
    /** map between string and module descriptor */
    protected final Map descriptors;
    /** map between string and module object */
    protected final Map modules;

    private final  Map createInterfaceClassIndex(final Map services) {
        Map result = new HashMap(services.size());
        for (Iterator i = services.values().iterator(); i.hasNext(); ) {
            ServiceBean sb = (ServiceBean)i.next();
            result.put(sb.getInterface(), sb);
        }
        return result;
    }
    
    private final Map createModules(final Map services) {
        Map result = new HashMap();
        for (Iterator i = services.values().iterator(); i.hasNext(); ) {
            ServiceBean sb = (ServiceBean)i.next();
            String moduleName = sb.getModule().getModuleId();
            if (! result.containsKey(moduleName)) {
                result.put(moduleName,new HivemindModuleAdapter(moduleName));
            }
        }
        return result;
    }

    
    /** filters descriptors, to only those with components avalable */
    private final void removeUnavailableComponents(Map m) {
        for (Iterator i = m.values().iterator(); i.hasNext();) {
            ModuleDescriptor d = (ModuleDescriptor)i.next();
            Module module = this.getModule(d.getName());
           
            inner:
            for (Iterator j = d.componentIterator(); j.hasNext(); ) {
                ComponentDescriptor c = (ComponentDescriptor)j.next();
                try {
                    Object instance =module.getComponent(c.getInterfaceClass());
                    // if an implementation is present, and isn't a 'mock', then all is ok.
                    if (instance != null && ! Boolean.getBoolean(d.getName() + "." + c.getName() + ".disabled")) {
                        continue inner;
                    }
                } catch (ACRException e) {
                    // not worried.
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

		public HivemindModuleAdapter(String moduleName) {
            this.moduleName= moduleName;
        }
        private final String moduleName;
        
        public Object getComponent(String name) throws ACRException {
            return HivemindMutableACR.this.getService(moduleName + "." + name);
        }

        public Object getComponent(Class iface) throws ACRException {
            return HivemindMutableACR.this.getService(iface);           
        }

        public ModuleDescriptor getDescriptor() {
            return (ModuleDescriptor)HivemindMutableACR.this.descriptors.get(moduleName);
        }
    }

    public Map getDescriptors() {
        return descriptors;
    }

    public Module getModule(String name) {
        return (Module)modules.get(name);
    }

    public Iterator moduleIterator() {
        return modules.values().iterator();
    }
    
    public Object getService(Class arg0) throws ACRException,NotFoundException {
        if (!arg0.isInterface()) {
            throw new NotFoundException("Not a service interface: " + arg0.getName());
        }
        if (! servicesByClass.containsKey(arg0)) {
            throw new NotFoundException(arg0.getName());
        }
        ServiceBean sb = (ServiceBean)servicesByClass.get(arg0);
        try {
        return sb.getModule().getService(sb.getId(),arg0);
        } catch (ApplicationRuntimeException e) {
            throw new ACRException(e);
        }
    }

    public Object getService(String arg0) throws ACRException, InvalidArgumentException,
            NotFoundException {
       if (! servicesByName.containsKey(arg0)) {
           throw new NotFoundException(arg0);
       }
       ServiceBean sb = (ServiceBean)servicesByName.get(arg0);
       try {
       return sb.getModule().getService(arg0,sb.getInterface());
       } catch (ApplicationRuntimeException e) {
           throw new ACRException(e);
       }       
    }


}


/* 
$Log: HivemindMutableACR.java,v $
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