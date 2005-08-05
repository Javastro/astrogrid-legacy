/*$Id: RmiLiteACR.java,v 1.1 2005/08/05 11:46:55 nw Exp $
 * Created on 27-Jul-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr;

import net.ladypleaser.rmilite.Client;

import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.builtin.Module;
import org.astrogrid.desktop.framework.MutableACR;
import org.astrogrid.desktop.framework.descriptors.ComponentDescriptor;
import org.astrogrid.desktop.framework.descriptors.ModuleDescriptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

/** used internally as a delegate to acr over rmi.
 * @author Noel Winstanley nw@jb.man.ac.uk 27-Jul-2005
 *
 */
public class RmiLiteACR implements ACR {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(RmiLiteACR.class);

    /** Construct a new RmiLiteACR
     * @throws NotBoundException
     * @throws RemoteException
     * 
     */
     RmiLiteACR(int port) throws RemoteException, NotBoundException {
        super();
        client = new Client("localhost",port);       
        remoteRegistry = (MutableACR)client.lookup(MutableACR.class);
        descriptors = remoteRegistry.getDescriptors();
        for (Iterator i = descriptors.values().iterator(); i.hasNext(); ) {
            registerListeners((ModuleDescriptor)i.next());
        }
    }
    final Client client;
    final MutableACR remoteRegistry;
    final Map descriptors;
    public static final String LISTENER_INTERFACES_KEY = "listener-interfaces";

    
    /** listener classes involved in callbacks need extra treatment - rmilite needs to be told that they're callback classes. */
    private void registerListeners(ModuleDescriptor md) {
        for (Iterator i = md.componentIterator(); i.hasNext(); ) {
            ComponentDescriptor cd = (ComponentDescriptor)i.next();
            if (cd.getProperty(RmiLiteACR.LISTENER_INTERFACES_KEY) != null) {                
                logger.info("Component " + cd.getName() +" includes listeners");
                StringTokenizer tok = new StringTokenizer(cd.getProperty(LISTENER_INTERFACES_KEY));
                while (tok.hasMoreTokens()) {
                    try {
                        Class listenerClass = Class.forName(tok.nextToken());
                        client.exportInterface(listenerClass);
                    } catch (ClassNotFoundException e2) {
                        logger.warn("Failed to find listener class - skipping",e2);
                    }
                }
            }
        }
    }
    
    /**
     * @see org.astrogrid.acr.builtin.ACR#getModule(java.lang.String)
     */
    public Module getModule(String name) {
        return new RmiModule(name);
    }

    /**
     * @see org.astrogrid.acr.builtin.ACR#moduleIterator()
     */
    public Iterator moduleIterator() {
        final Iterator i = descriptors.keySet().iterator();
        return new Iterator() {

            public void remove() {
                throw new UnsupportedOperationException();
            }

            public boolean hasNext() {
                return i.hasNext();
            }

            public Object next() {
                return new RmiModule((String)i.next());
            }
        };
        
    }

    /**
     * @throws 
     * @see org.astrogrid.acr.builtin.ACR#getService(java.lang.Class)
     */
    public Object getService(Class interfaceClass) throws ACRException, NotFoundException {
        if (interfaceClass.equals(ACR.class)) {
            // special case - always want to return this when someone is requesting the acr - not the real remote registry.
            return this;
        }
        try {
            return client.lookup(interfaceClass);
        } catch (NotBoundException e) {
            throw new NotFoundException(interfaceClass.getName());
        } catch (Throwable e) {
            throw new ACRException(e);
        }
    }

    /** class that acts as a proxy for a remote module. */
    private class RmiModule implements Module {
        /**
         * Commons Logger for this class
         */
        private final Log logger = LogFactory.getLog(RmiModule.class);

        public RmiModule(String name) {
            this.name = name;
        }
        private  final String name;
        

        public ModuleDescriptor getDescriptor() {
            return (ModuleDescriptor)descriptors.get(name);
        }

        public Object getComponent(String componentName) throws ACRException{
            // find the interface class tis name refers to..
            ComponentDescriptor cd= getDescriptor().getComponent(componentName);
            if (cd == null) {
                throw new NotFoundException(componentName);
            }
            return getService(cd.getInterfaceClass());
        }


        public Iterator componentIterator() {
            final Iterator i = getDescriptor().componentIterator();
            return new Iterator() {

                public void remove() {
                    throw new UnsupportedOperationException();
                }

                public boolean hasNext() {
                    return i.hasNext();
                }

                public Object next() {
                    try {                        
                        String name=((ComponentDescriptor)i.next()).getName();
                        return getComponent(name);
                    } catch (Exception e) {
                          throw new RuntimeException("Failed to call next()",e);
                    }
                }
            };
        }

    }
    
}


/* 
$Log: RmiLiteACR.java,v $
Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/