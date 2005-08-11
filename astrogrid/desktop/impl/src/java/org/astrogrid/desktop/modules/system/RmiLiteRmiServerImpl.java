/*$Id: RmiLiteRmiServerImpl.java,v 1.1 2005/08/11 10:15:00 nw Exp $
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

import net.ladypleaser.rmilite.Server;
import net.ladypleaser.rmilite.impl.RemoteInvocationHandlerImpl;

import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.builtin.Module;
import org.astrogrid.acr.system.RmiServer;
import org.astrogrid.desktop.framework.DefaultModule;
import org.astrogrid.desktop.framework.MutableACR;
import org.astrogrid.desktop.framework.NewModuleEvent;
import org.astrogrid.desktop.framework.NewModuleListener;
import org.astrogrid.desktop.framework.descriptors.ComponentDescriptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.picocontainer.Startable;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.activation.Activatable;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 27-Jul-2005
 *
 */
public class RmiLiteRmiServerImpl extends AbstractRmiServerImpl implements RmiServer, Startable, NewModuleListener {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(RmiLiteRmiServerImpl.class);

    /** Construct a new RmiLiteRmiServerImpl
     * 
     */
    public RmiLiteRmiServerImpl(MutableACR acr) throws Exception {
        super();
        this.acr = acr;
    }
    private Server server;
    private final MutableACR acr;

    /**
     * @see org.picocontainer.Startable#start()
     */
    public void start() {
        try {
        server = new Server(getPort());       
        logger.info("Started RMI Server");
        // only register for events once we've started the server.
        acr.addNewModuleListener(this);        
        } catch (RemoteException e) {
            logger.error("Unable to start rmi service",e);
        }
    }

    /**
     * @see org.picocontainer.Startable#stop()
     */
    public void stop() {

    }

    public static final String LISTENER_INTERFACES_KEY = "listener-interfaces";
    /**
     * @see org.astrogrid.desktop.framework.NewModuleListener#newModuleRegistered(org.astrogrid.desktop.framework.NewModuleEvent)
     */
    public void newModuleRegistered(NewModuleEvent e) {
        DefaultModule module = (DefaultModule)e.getModule();
        // special case - don't bother if there's no components..
        if (! module.getDescriptor().componentIterator().hasNext()) {
            return;
        }
        String name = module.getDescriptor().getName();
        logger.info("Registering components in " + name);
        for (Iterator i = module.getDescriptor().componentIterator(); i.hasNext(); ) {
            ComponentDescriptor cd = (ComponentDescriptor)i.next();
            Class iface = cd.getInterfaceClass();
            List listenerClasses= new ArrayList();
            if (cd.getProperty(LISTENER_INTERFACES_KEY) != null) {
                logger.info("Component " + cd.getName() +" includes listeners");
                StringTokenizer tok = new StringTokenizer(cd.getProperty(LISTENER_INTERFACES_KEY));
                while (tok.hasMoreTokens()) {
                    try {
                        Class listenerClass = Class.forName(tok.nextToken());
                        listenerClasses.add(listenerClass);
                    } catch (ClassNotFoundException e2) {
                        logger.warn("Failed to find listener class - skipping",e2);
                    }                    
                }
            }
            try {
                Object impl = module.getComponent(cd.getName());                
                server.publish(iface,impl,(Class[])listenerClasses.toArray(new Class[]{}));
                logger.info("Published " + cd.getName());
         
            } catch (Exception e1) {               
                logger.error("Failed to publish " + cd.getName(),e1);
            }
        }
    }
    /** lifted from rmilite sources - couldn't extend, as registry field is private */
    static class CustomServer {
            public static final int DEFAULT_PORT = Registry.REGISTRY_PORT;

            protected Registry registry;
 
            public CustomServer() throws RemoteException {
                this(DEFAULT_PORT);
            }

            public CustomServer(int port) throws RemoteException{
                try {
                    registry = LocateRegistry.getRegistry(port);
                    
                } catch (RemoteException e) {                
                    registry = LocateRegistry.createRegistry(port);
                }
            }
            
            public CustomServer(Registry reg) {
                this.registry = reg;
            }

            public void publish(Class iface, Object impl, Class[] exportedInterfaces) throws RemoteException {
                RemoteInvocationHandlerImpl handler = new RemoteInvocationHandlerImpl(impl, new HashSet(Arrays.asList(exportedInterfaces)));
                registry.rebind(iface.getName(), handler);
            }
        }       

}


/* 
$Log: RmiLiteRmiServerImpl.java,v $
Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/