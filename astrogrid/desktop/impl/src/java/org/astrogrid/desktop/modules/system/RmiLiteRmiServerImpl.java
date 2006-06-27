/*$Id: RmiLiteRmiServerImpl.java,v 1.8 2006/06/27 10:41:51 nw Exp $
 * Created on 27-Jul-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system;

import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.ladypleaser.rmilite.Server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.builtin.ComponentDescriptor;
import org.astrogrid.acr.builtin.ShutdownListener;
import org.astrogrid.acr.system.RmiServer;
import org.astrogrid.desktop.framework.ACRInternal;
import org.astrogrid.desktop.framework.Module;

/** Implementation of the RmiServer using rmi lite.
 * @author Noel Winstanley nw@jb.man.ac.uk 27-Jul-2005
 
 */
public class RmiLiteRmiServerImpl extends AbstractRmiServerImpl implements  ShutdownListener {


    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(RmiLiteRmiServerImpl.class);
    private final ACRInternal acr;
    private final Map listenerInterfaces;
    private Server server;


    /** Construct a new RmiLiteRmiServerImpl
     * @throws UnknownHostException 
     * 
     */
    public RmiLiteRmiServerImpl(ACRInternal acr, Map listenerInterfaces) throws UnknownHostException  {
        super();
        this.acr = acr;
        this.listenerInterfaces = listenerInterfaces;
    }

 

    public void init() throws Exception {
        super.init();// selects correct port, etc.
        server = new Server(getPort());       
        logger.debug("Started RMI Server");       
        for (Iterator i = acr.moduleIterator(); i.hasNext(); ) {
            registerServicesInModule((Module)i.next());
        }
    }

    private void registerServicesInModule(Module module) {
        // special case - don't bother if there's no components..
        if (! module.getDescriptor().componentIterator().hasNext()) {
            return;
        }
        final List theEmptyList = Collections.EMPTY_LIST;
        String moduleName = module.getDescriptor().getName();
        logger.debug("Registering components in " + moduleName);
        for (Iterator i = module.getDescriptor().componentIterator(); i.hasNext(); ) {
            ComponentDescriptor cd = (ComponentDescriptor)i.next();
            String componentName = cd.getName();
            Class iface = cd.getInterfaceClass();            
            List listeners= (List)listenerInterfaces.get(moduleName + "." + componentName);
            if (listeners == null) { // most likely.
                listeners = theEmptyList;
            }
            try {
                Object impl = module.getComponent(componentName);                
                server.publish(iface,impl,(Class[])listeners.toArray(new Class[listeners.size()]));
         
            } catch (Exception e1) {
                logger.error("Failed to publish " + componentName,e1);
            }
        }
    }



    public void halting() {
      //  need to shut down the rmi registry.
        try {
            Registry reg= LocateRegistry.getRegistry(getPort());
            for (Iterator i = acr.moduleIterator(); i.hasNext(); ) {
                Module m = (Module)i.next();
                for (Iterator j = m.getDescriptor().componentIterator(); j.hasNext(); ) {
                    ComponentDescriptor cd = (ComponentDescriptor)j.next();
                    String regKey = cd.getInterfaceClass().getName();
                    try {
                        reg.unbind(regKey);
                    } catch (Exception e) {
                        logger.warn("Failed to deregister " + regKey + " from rmi server",e);
                    }
                }                
            }
        } catch (RemoteException e) {
            logger.warn("Failed to deregister all from rmi server",e);
        }
       
    }



    public String lastChance() {
        return null;
    }
 
}


/* 
$Log: RmiLiteRmiServerImpl.java,v $
Revision 1.8  2006/06/27 10:41:51  nw
findbugs tweaks

Revision 1.7  2006/06/02 00:16:15  nw
Moved Module, Component and Method-Descriptors from implementation code into interface. Then added methods to ApiHelp that provide access to these beans.

Revision 1.6  2006/04/26 15:56:18  nw
made servers more configurable.added standalone browser launcher

Revision 1.5  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.4.34.3  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.4.34.2  2006/04/04 10:31:26  nw
preparing to move to mac.

Revision 1.4.34.1  2006/03/22 18:01:30  nw
merges from head, and snapshot of development

Revision 1.4  2005/11/10 12:05:53  nw
big change around for vo lookout

Revision 1.3  2005/09/02 14:03:34  nw
javadocs for impl

Revision 1.2  2005/08/25 16:59:58  nw
1.1-beta-3

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/