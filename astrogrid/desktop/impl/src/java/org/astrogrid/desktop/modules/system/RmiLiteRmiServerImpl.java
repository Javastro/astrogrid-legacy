/*$Id: RmiLiteRmiServerImpl.java,v 1.16 2008/08/04 16:37:23 nw Exp $
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.ladypleaser.rmilite.RemoteInvocationException;
import net.ladypleaser.rmilite.impl.RemoteInvocationHandler;
import net.ladypleaser.rmilite.impl.RemoteInvocationHandlerImpl;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.Finder;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.builtin.ComponentDescriptor;
import org.astrogrid.acr.builtin.ShutdownListener;
import org.astrogrid.acr.system.RmiServer;
import org.astrogrid.desktop.SplashWindow;
import org.astrogrid.desktop.framework.ACRInternal;
import org.astrogrid.desktop.framework.Module;
import org.astrogrid.desktop.framework.SessionManagerInternal;

/** Implementation of the RmiServer using rmi lite.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 27-Jul-2005
 * @future configure the java.rmi.server.codebase property to point to the internal webserver - so that clients can grab missing classes if needed.
 * @todo find way to reliably shut down the rmi server / registry - sometimes it seems to hang around.
 * @todo restrict connections to specified ports.
   @modified inlined the source from RmiLite's server - so we can adapt it (hard to extend).
 */
public class RmiLiteRmiServerImpl extends AbstractRmiServerImpl implements  ShutdownListener {


    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(RmiLiteRmiServerImpl.class);
    private final ACRInternal acr;
    private final Map listenerInterfaces;
    final SessionManagerInternal session;
    private Registry registry;


    /** Construct a new RmiLiteRmiServerImpl
     * @throws UnknownHostException 
     * 
     */
    public RmiLiteRmiServerImpl(final ACRInternal acr, final Map listenerInterfaces, final SessionManagerInternal sess) throws UnknownHostException  {
        super();
        SplashWindow.reportProgress("Starting AstroRuntime RMI Interface...");
        this.acr = acr;
        this.listenerInterfaces = listenerInterfaces;
        this.session = sess;
    }


    public void init() throws IOException  {
        if (port < 1) {
            if (scanEndPort < scanStartPort) {
                throw new IOException("scanEndPort (" + scanEndPort + ") is smaller than scanStartPort (" + scanStartPort + ")");
            }
            logger.info("Will scan for spare port, from " + scanStartPort + " to " + scanEndPort);
            for (int i = scanStartPort; i < scanEndPort; i++) {
                try {
                    registry = LocateRegistry.createRegistry(i);
                    // if we've passed this point, it's ok, we've found an available port.
                    port = i;
                    break; // break out of the loop
                } catch (final IOException e) {
                    // no matter, keep scanning.
                    registry = null;
                }
            } 
            if (registry == null) {
                // JL Note. Added address to exception.
                // It is possible to be looking at the wrong address!
                throw new IOException( "Could not find a free port between " 
                        + scanStartPort + " and " + scanEndPort ) ;
            }
        } else {
            registry = LocateRegistry.createRegistry(getPort());
        }
        logger.info("Rmi Server will listen on port " + port );  
        if (! disableConnectionFile) {
            recordDetails();
        }             
        for (final Iterator i = acr.moduleIterator(); i.hasNext(); ) {
            registerServicesInModule((Module)i.next());
        }
    }
 
    private void registerServicesInModule(final Module module) {
        // special case - don't bother if there's no components..
        if (! module.getDescriptor().componentIterator().hasNext()) {
            return;
        }
        final String moduleName = module.getDescriptor().getName();
        logger.debug("Registering components in " + moduleName);
        for (final Iterator i = module.getDescriptor().componentIterator(); i.hasNext(); ) {
            final ComponentDescriptor cd = (ComponentDescriptor)i.next();
            final String componentName = cd.getName();
            final Class iface = cd.getInterfaceClass();            
            List listeners= (List)listenerInterfaces.get(moduleName + "." + componentName);
            if (listeners == null) { // most likely.
                listeners = Collections.EMPTY_LIST;
            }
            try {
                final Object impl = module.getComponent(componentName);                
                publish(iface,impl,(Class[])listeners.toArray(new Class[listeners.size()]));
         
            } catch (final Exception e1) {
                logger.error("Failed to publish " + componentName,e1);
            }
        }
    }

    // copied from rmiLite impl of Server
    private void publish(final Class iface, final Object impl, final Class[] exportedInterfaces) throws RemoteException {
    	final RemoteInvocationHandler handler = new SessionAwareRemoteInvocationHandlerImpl(impl, new HashSet(Arrays.asList(exportedInterfaces)));
    	registry.rebind(iface.getName(), handler);
    }


    public void halting() {
      //  need to shut down the rmi registry.
        try {
            final Registry reg= LocateRegistry.getRegistry(getPort());
            for (final Iterator i = acr.moduleIterator(); i.hasNext(); ) {
                final Module m = (Module)i.next();
                for (final Iterator j = m.getDescriptor().componentIterator(); j.hasNext(); ) {
                    final ComponentDescriptor cd = (ComponentDescriptor)j.next();
                    final String regKey = cd.getInterfaceClass().getName();
                    try {
                        reg.unbind(regKey);
                    } catch (final Exception e) {
                        logger.warn("Failed to deregister " + regKey + " from rmi server",e);
                    }
                }                
            }
        } catch (final RemoteException e) {
            logger.warn("Failed to deregister all from rmi server",e);
        }
       
    }



    public String lastChance() {
        return null;
    }
    
    /** an invocation handler that takes care of setting the 
     * correct session id. Need to squeeze the session parameter in as an 
     * additional item in the args, rather than using a separate parameter -
     * as defining a new method with an additinal parameter would require defining
     * a new interface that extended RemoteInvocationHandler, and doing this
     * causes existing clients to break - as although they only need to call the default
     * RemoteInvocationHandler interface, all other interfaces that an object supports seem
     * to be transported to the client too - and an unknown interface causes the client
     * to fail with a 'notsuchclass' exception.
     * @author Noel.Winstanley@manchester.ac.uk
     * @since Mar 21, 200712:07:13 PM
     */
    public class SessionAwareRemoteInvocationHandlerImpl extends RemoteInvocationHandlerImpl {

		/**
		 * @param impl
		 * @param exportedInterfaces
		 * @throws RemoteException
		 */
		public SessionAwareRemoteInvocationHandlerImpl(final Object impl, final Set exportedInterfaces) throws RemoteException {
			super(impl, exportedInterfaces);
		}

		public Object invoke(final String sessionId, final String methodName, final Class[] paramTypes, final Object[] args) throws RemoteException {
			final Principal p = session.findSessionForKey(sessionId);
			if (p == null) {
				throw new RemoteException("Invalid session " + sessionId);
			}
			session.adoptSession(p);
			try {
				return super.invoke(methodName,paramTypes,args);
			} catch (final RemoteInvocationException ex) { // re-map runtime exceptions which are not known client-side.
			    final Throwable cause = ex.getCause();
			    if (cause != null 
			            && cause instanceof RuntimeException
			            && ! cause.getClass().getName().startsWith("java")) {
			        final RemoteInvocationException replacement = new RemoteInvocationException(methodName
			                , new RuntimeException(cause.getClass().getName() + " : " + cause.getMessage()));
			        throw replacement;
			    } else {
			        throw ex; // re-throw the original exception.
			    }
			} finally {
				session.clearSession();
			}
		}
		
		@Override
        public Object invoke(final String arg0, final Class[] paramTypes, final Object[] args) throws RemoteException {
		
			if (paramTypes.length > 0 && paramTypes[0].equals(Void.class)) { // a meta-parameter indicating the session to invoke this method in
				final String sessionId = (String)args[0];
				return this.invoke(sessionId,arg0,(Class[])ArrayUtils.remove(paramTypes,0),ArrayUtils.remove(args,0));
			} else { //no session id provided.
				return this.invoke(session.getDefaultSessionId(),arg0, paramTypes, args);
			}
		}
		
    }

    public Test getSelftest() {
        final TestSuite ts = new TestSuite("RMI");
        ts.addTest(new TestCase("RMI connection file"){
            @Override
            protected void runTest()  {
                final File f = new File(SystemUtils.getUserHome(),".acr-rmi-port");
                assertTrue("~/.acr-rmi-port not present",f.exists());
                FileReader fr = null;
                try {
                    fr = new FileReader(f);
                    final String str = new BufferedReader(fr).readLine();
                    assertNotNull("~/.acr-rmi-port is empty",str);
                    final int port = Integer.parseInt(str);
                    assertEquals("incorrect port in ~/.acr-rmi-port",getPort(),port);
                } catch (final NumberFormatException ex) {
                    fail("unable to parse contents of ~/.acr-rmi-port");
                } catch (final FileNotFoundException x) {
                    // just tested for this.
                    fail("~/.acr-rmi-port not present");                    
                } catch (final IOException x) {
                    fail("unable to read ~/.acr-rmi-port");
                } finally {
                    if (fr != null) {
                        try {
                            fr.close();
                        } catch (final IOException e) {
                            // ho hum
                        }
                    }
                }
            }
        });
        ts.addTest(new TestCase("RMI access to AR") {
            @Override
            protected void runTest()  {
                final Finder f = new Finder() {
                    // overridden to only ever try to connect external, and not to cache.
                    @Override
                    public ACR find(final boolean ignored, final boolean alsoIgnored) {
                        try {
                            return connectExternal();
                        } catch (final Exception ex) {
                            logger.info("Failed to connect to AR",ex);
                            fail("failure when connecting to AR");
                            return null; // never reached
                        }
                    }
                };
                // ok. got a connection. now try doing some stuff with it.
                try {
                    final ACR ar = f.find(false,false);
                    assertNotNull("failed to connect to AR",ar);
                    final RmiServer arRmi = (RmiServer) ar.getService(RmiServer.class);
                    assertEquals("rmi service didn't respond with expected result",getPort(),arRmi.getPort());
                } catch (final InvalidArgumentException x) {
                    fail("error while retreiving AR component");
                } catch (final NotFoundException x) {
                    fail("unable to locate required AR component");
                } catch (final ACRException x) {
                    fail("error when connecting to AR");
                }
            }
        });
        return ts;
    }
 
}


/* 
$Log: RmiLiteRmiServerImpl.java,v $
Revision 1.16  2008/08/04 16:37:23  nw
Complete - task 441: Get plastic upgraded to latest XMLRPC

Complete - task 430: upgrade to latest xmlrpc lib

Revision 1.15  2008/03/05 10:59:22  nw
added progress reporting to splashscreen

Revision 1.14  2007/10/22 10:28:31  nw
moved runtime exceptioni lifting to the RmiServer, where it belongs.

Revision 1.13  2007/10/12 11:02:49  nw
added code for selftesting

Revision 1.12  2007/04/18 15:47:07  nw
tidied up voexplorer, removed front pane.

Revision 1.11  2007/03/22 19:03:48  nw
added support for sessions and multi-user ar.

Revision 1.10  2007/01/29 11:11:36  nw
updated contact details.

Revision 1.9  2006/09/02 00:49:42  nw
inlined rmilite server, as precursor to customization.

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