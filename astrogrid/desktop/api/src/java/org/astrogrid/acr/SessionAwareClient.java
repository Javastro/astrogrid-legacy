package org.astrogrid.acr;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteObject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import net.ladypleaser.rmilite.RemoteInvocationException;
import net.ladypleaser.rmilite.impl.LocalInvocationHandlerImpl;
import net.ladypleaser.rmilite.impl.RemoteInvocationHandler;
import net.ladypleaser.rmilite.impl.RemoteInvocationHandlerImpl;


/** Part of the internal implementation.
 * @exclude
 *
 * rewriting of rmilite.Client that adds support for session information.
 * protocol compliant with existing client - and would have liked to 
 * just extend that class - but it was not really suitable for extension.
 * @author Noel.Winstanley@manchester.ac.uk

 */
class SessionAwareClient {

	/** create an unsessioned client - can connect to old version ACR, 
	 * and new version ACR
	 * @param arg0
	 * @param arg1
	 */
	public SessionAwareClient(final String arg0, final int arg1) {
		this.serverHost = arg0;
		this.serverPort = arg1;
	}    	
	
	public SessionAwareClient(final String arg0, final int arg1, final String session) {
		this(arg0,arg1);
		this.session = session;
	}
	private  String session;
	private final String serverHost;
	private final int serverPort;
	private final Set exportedInterfaces = new HashSet();		


	public void exportInterface(final Class iface) {
		exportedInterfaces.add(iface);
	}

	public Object lookup(final Class iface) throws RemoteException, NotBoundException {
		final Registry registry = LocateRegistry.getRegistry(serverHost, serverPort);
		final RemoteInvocationHandler remote = (RemoteInvocationHandler) registry.lookup(iface.getName());
		if (session == null) {
			return LocalInvocationHandlerImpl.create(iface,remote,exportedInterfaces);
		} else { 
		return SessionAwareLocalInvocationHandlerImpl.create(iface, remote, exportedInterfaces, session);
	}
}
	/** invocation handler that will splice in additional arg to represent sessionId, if provided */
static class SessionAwareLocalInvocationHandlerImpl implements InvocationHandler {
    private final RemoteInvocationHandler handler;
    private final Set exportedInterfaces;
    private final String session;
    public SessionAwareLocalInvocationHandlerImpl(final RemoteInvocationHandler handler, final Set exportedInterfaces, final String session) {
        this.handler = handler;
        this.exportedInterfaces = exportedInterfaces;
        this.session = session;
    }

    public Object invoke(final Object proxy, final Method method, Object[] args) throws Throwable {
        //keep a reference to remote invocation handlers that are created as a result of this method invocation
        //so that they will not be garbage collected
        final ArrayList keeparound = new ArrayList();

        try {
            final Class[] parameterTypes = method.getParameterTypes();
            if (args == null) {
                args = new Object[0];
            }
            for (int i = 0; i < parameterTypes.length; i++) {
                final Class type = parameterTypes[i];
                if (exportedInterfaces.contains(type)) {
                    final RemoteInvocationHandlerImpl obj = new RemoteInvocationHandlerImpl(args[i], exportedInterfaces);
                    keeparound.add(obj);
                    args[i] = RemoteObject.toStub(obj);
                }
            }
            Object returnValue = invokeRemote(method, parameterTypes, args);
            if (returnValue instanceof RemoteInvocationHandler) {
                returnValue = LocalInvocationHandlerImpl.create(method.getReturnType(), (RemoteInvocationHandler) returnValue, exportedInterfaces);
            }
            return returnValue;
        } finally {
            keeparound.clear();
        }
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
     */
    private Object invokeRemote(final Method method, final Class[] parameterTypes, final Object[] args) throws Throwable {
        try {
        	final Class[] sessionedParameterTypes = new Class[parameterTypes.length +1];
        	sessionedParameterTypes[0] = Void.class;
        	final Object[] sessionedArgs = new Object[args.length + 1];
        	System.arraycopy(parameterTypes,0,sessionedParameterTypes,1,parameterTypes.length);
        	sessionedArgs[0] = session;
        	System.arraycopy(args,0,sessionedArgs,1,args.length);
            return handler.invoke(method.getName(), sessionedParameterTypes, sessionedArgs);
        } catch (final RemoteInvocationException e) {
            RemoteInvocationException.rethrow(method, e);
            throw new Error("should have thrown an exception in the previous statement");
        } catch (final RemoteException e) {
            RemoteInvocationException.rethrow(method, e);
            throw new Error("should have thrown an exception in the previous statement");
        }
    }

    public static Object create(final Class iface, final RemoteInvocationHandler remote, final Set exportedInterfaces, final String session) {
        return java.lang.reflect.Proxy.newProxyInstance(SessionAwareClient.class.getClassLoader(), new Class[]{iface}
        , new SessionAwareLocalInvocationHandlerImpl(remote, exportedInterfaces, session));
    }
}
}