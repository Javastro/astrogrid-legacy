/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcHandler;
import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;
import org.apache.xmlrpc.server.XmlRpcNoSuchHandlerException;
import org.astrogrid.acr.builtin.ShutdownListener;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.plastic.PlasticHubListenerInternal;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.votech.plastic.CommonMessageConstants;
import org.votech.plastic.HubMessageConstants;
import org.votech.plastic.PlasticHubListener;
import org.votech.plastic.PlasticListener;
import org.votech.plastic.incoming.handlers.AbstractMessageHandler;
import org.votech.plastic.incoming.handlers.LoggingHandler;
import org.votech.plastic.incoming.handlers.MessageHandler;
import org.votech.plastic.incoming.handlers.StandardHandler;
import org.votech.plastic.incoming.messages.hub.HubListener;

import uk.ac.starlink.plastic.PlasticUtils;
import uk.ac.starlink.plastic.XmlRpcAgent;
import uk.ac.starlink.plastic.XmlRpcHub;
import ca.odell.glazedlists.CompositeList;
import ca.odell.glazedlists.EventList;

/** Implementation of the tupperware container.
 * Takes care of the client-side of Plastic for VODesktop. The hub-side of Plastic is deliberately kept
 * separate (as we might be connectiing to another hub altogether) in the plastic.hub component.
 * @author Noel Winstanley
 * @since Jun 16, 20061:52:12 PM
 */
public class TupperwareImpl implements TupperwareInternal, PlasticListener, XmlRpcHandler, ShutdownListener, HubListener {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(TupperwareImpl.class);
    private final String applicationName;
    private final PlasticHubListenerInternal internalHub;
    private final URL xmlrpcClientEndpoint;
    private final String vodesktop_ivorn;
    private final HttpClient httpClient;
    private final List<MessageType<?>> allKnownMessages;
    private final URL webserverRoot;
    
    public URL getWebserverRoot() {
        return webserverRoot;
    }
    
	/**
	 * 
	 * @param parent the ui context
	 * @param internalHub intrnal hub component, to start as needed;.
	 * @param applicationName current name of workbench - may change, depending on variant.
	 * @param description variable description of this workbench.
	 * @param icon icon url to report for this application
	 * @param ivorn ivo id to report for this application
	 * @param clientEndpoint xmlrpc endpoint to provide to hub to callback.
	 * @param handlers contribution list of handlers to register. 
	 * @param model 
	 * @param httpClient client to use to connect to hubs.
	 * @throws IOException 
	 */
	public TupperwareImpl(final UIContext parent, final URL webserverRoot
	        ,  final String applicationName, final String description, final String icon,final String ivorn
	        , final URL clientEndpoint
	        , final List<MessageTarget> internalTargets
	        , final List<MessageType<?>> knownTypes
	        , final CompositeList<ExternalMessageTarget> allTargets
	        ,final PlasticHubListenerInternal internalHub
	        ,final HttpClient httpClient
	        ) throws IOException {
		super();
		
        this.parent = parent;
        this.webserverRoot = webserverRoot;
        this.applicationName = applicationName;
        this.vodesktop_ivorn = ivorn;
        this.allKnownMessages = knownTypes;
        this.httpClient = httpClient;
        this.xmlrpcClientEndpoint = new URL(clientEndpoint.toString() + "xmlrpc");
		this.plasticTargets = allTargets.createMemberList();
		allTargets.addMemberList(this.plasticTargets);
        this.internalHub = internalHub;
        
        // build the plastic handler chain.
		this.plasticHandler = new LoggingHandler(logger);			
		// handle the application registered message
		applicationRegisteredMessageHandler = new ApplicationRegisteredMessageHandler();
		plasticHandler.appendHandler(applicationRegisteredMessageHandler);
		
		plasticHandler.appendHandler(new InternalMessageTargetMessageHandler(internalTargets));		
		
		plasticHandler.appendHandler(new StandardHandler(
		        applicationName
		        ,description
		        ,ivorn
		        ,icon
		        ,PlasticListener.CURRENT_VERSION
		        ,this
		        ,null));
		
		this.supportedMessages = new HashSet(plasticHandler.getHandledMessages());		
		logger.info("Will handle messages:" + supportedMessages);
	}

	private final ConnectAction connect = new ConnectAction();
	private final DisconnectAction disconnect = new DisconnectAction();
	/** reference to the hub we've connected to */
	private ArXmlRpcHub hub;
	private final EventList<ExternalMessageTarget>  plasticTargets;
	private URI myPlasticId;
	private final UIContext parent;
	private final MessageHandler plasticHandler;
	private final StartInternalHubAction startInternal = new StartInternalHubAction();
    private final Set supportedMessages;
    private final ApplicationRegisteredMessageHandler applicationRegisteredMessageHandler;


    /** convenience method - calls a single application */
    public void singleTargetFireAndForgetMessage(final URI message, final List args, final URI target) {
        if (logger.isDebugEnabled()) {
            logger.debug("Single target fire and forgeet:" + target + " message:" + message + " args:" + args);
        }
        final List<URI> targetSet = new ArrayList<URI>();
        targetSet.add(target);
        hub.requestToSubsetAsynch(myPlasticId, message, args, targetSet);
  
    }
    
    /** convenience method - calls a single application */
    public Object singleTargetRequestResponseMessage(final URI message, final List args, final URI target) {
        if (logger.isDebugEnabled()) {
            logger.debug("Single target request response :" + target + " message:" + message + " args:" + args);
        }
        final List<URI> targetSet = new ArrayList<URI>();
        targetSet.add(target);
        final Map m = hub.requestToSubset(myPlasticId, message, args, targetSet);
        if (m == null || ! m.containsKey(target)) {
            return null;
        } else {
            return m.get(target);
        }
    }
    
	public Action connectAction() {
        return connect;
    }

	public Action disconnectAction() {
        return disconnect;
    }
	
    public Action startInternalHubAction() {
        return startInternal;
    }


// XMLRPC handler interface. 
    public Object execute(final XmlRpcRequest request) throws XmlRpcException {
        if ("perform".equals(request.getMethodName()) && request.getParameterCount() == 3) {
            try {
            final URI src = new URI(request.getParameter(0).toString());
            final URI msg = new URI(request.getParameter(1).toString());
            final Object[] arr = (Object[])request.getParameter(2);
            final List<Object> params = Arrays.asList(arr);
            final Object o =  plasticHandler.perform(src,msg,params);
            return o == null ? Collections.EMPTY_LIST : o; // can't ever return null (not a valid xmlrpc type) - use empty list to represent null instead.
            } catch (final URISyntaxException e) { 
                throw new XmlRpcException("Failed to parse parameter",e);
            }
        } else {
            logger.warn("Unknown method call: " + request.getMethodName());
            throw new XmlRpcNoSuchHandlerException(request.getMethodName());
        }
    }
	
	
// plastic listener interface.
public Object perform(final URI arg0, final URI arg1, final List arg2) {
	return plasticHandler.perform(arg0,arg1,arg2);
}
/** plastic message handler for the application-level mesages that vodesktop 
 * can consume
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 16, 20091:54:33 PM
 */
private class InternalMessageTargetMessageHandler extends AbstractMessageHandler {

    private final List<URI> localMessages = new ArrayList<URI>();
    private final List<MessageTarget> internalTargets;
    /**
     * work out what the local mesages are 
     * @param internalTargets 
     */
    public InternalMessageTargetMessageHandler(final List<MessageTarget> internalTargets) {
        this.internalTargets = internalTargets;
        final Set<MessageType<?>> msgs = new HashSet<MessageType<?>>();
       for (final MessageTarget messageTarget : internalTargets) {
        msgs.addAll(messageTarget.acceptedMessageTypes());
       }
       // ok. msgs now contains union of all supported messages.
       for(final MessageType<?> mt : msgs) {
           if (mt.getPlasticMessageType() != null) {
               localMessages.add(mt.getPlasticMessageType());
           }
       }
    }
    
    @Override
    protected List getLocalMessages() {
        return localMessages;
    }

    public Object perform(final URI sender, final URI msgID, final List args) {
        for (final MessageTarget m : internalTargets) {
            for(final MessageType t : m.acceptedMessageTypes()) {
                if (msgID.equals(t.getPlasticMessageType())) {
                    // found the correct target, and correct type
                    // this is what the message is destined for.
                    // intentionally omitting the generic typing here, otherwise it won't work.
                    // we know the definition is strongly typed anyhow, so this works.
                     final MessageSender dispatch = m.createMessageSender(t);
                     // create a handler (unmarshaller might be a better name)
                     final MessageUnmarshaller<MessageSender> unmarshal = t.createPlasticUnmarshaller();
                     // see if we can find the message sender in the list of currently known applications
                     // not vital if we can't.
                     ExternalMessageTarget source = null;
                     if (sender != null) {
                         final String senderID = sender.toString();
                         for(final ExternalMessageTarget mt : plasticTargets) {
                             if (mt.getId().equals(senderID)) {
                                 source = mt;
                                 break;
                             }
                         }
                     }
                     try {
                         return unmarshal.handle(source,args,dispatch);
                     } catch (final Exception e) {
                         //
                         //@todo  dunno if it's possible to pass exception back to caller
                         // need to read up on spec.
                         throw new RuntimeException(e);
                     }
                }
            }
        } 
        // no handler found
        if (nextHandler != null) {
            return nextHandler.perform(sender,msgID,args);
        } else {
            return CommonMessageConstants.RPCNULL;
        }
    }
}

    

/** plastic message handler that processes application registered / unregistered 
 * messages
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 16, 20091:53:42 PM
 */
private class ApplicationRegisteredMessageHandler extends AbstractMessageHandler {

	private final List<URI> dynamicButtonMessages = new ArrayList<URI>() {
	    {// init
	        this.add(HubMessageConstants.APPLICATION_REGISTERED_EVENT);
	        this.add(HubMessageConstants.APPLICATION_UNREGISTERED_EVENT);
	    }
	};
	private boolean enabled = false;
	
    public void enable() {
		enabled  = true;
	}
    /**
     * 
     */
    public void disable() {
        enabled = false;
    }
    
    /**
     * inspect a plastic application, and create a button for it if it's suitable
     * @param id plastic id for the application in inspect.
     */
    public void interrogatePlasticApp(final URI id) {
    	if (!enabled || id.equals(myPlasticId) || id.equals(hub.getHubId())) {
    		return;
    	}
    	// we're on a background thread at the moment, but still, 
    	// do this on a background thread, as the inspection of app (e.g. fetching it's icon) may take some time.
    	// meanwhile this thread can return.
        (new PlasticInterrogatorWorker(parent, "Inspecting Plastic Application " + id, id)).start();
    }

	/** called to handle messages */
    public Object perform(final URI sender, final URI message, final List args) {
        if (enabled) {
            try {
                if (message.equals(HubMessageConstants.APPLICATION_REGISTERED_EVENT)) {
                    interrogatePlasticApp(new URI(args.get(0).toString())); //redundant, but reliable.
                    return CommonMessageConstants.RPCNULL;
                }
                if (message.equals(HubMessageConstants.APPLICATION_UNREGISTERED_EVENT)) {            
                    removePlasticApp(new URI(args.get(0).toString()));
                    return CommonMessageConstants.RPCNULL;
                }
            } catch (final URISyntaxException e) { // don't care overmuch.
                logger.warn(e);       
            }
        }
        if (nextHandler != null) {
            return nextHandler.perform(sender,message,args);
        } else {
            return CommonMessageConstants.RPCNULL;
        }
    }

	/** remove a plastic application from the container.
     * 
     * @param id plastic id of the application to remove 
     */
    public void removePlasticApp(final URI id) {      
        try {
            plasticTargets.getReadWriteLock().writeLock().lock();
            	for (int i = 0; i < plasticTargets.size(); i++) {
            		final ExternalMessageTarget pad = plasticTargets.get(i);
            		if (pad.getId().equals(id.toString())) {
            			plasticTargets.remove(i);
            			return;
            		}
            	}
        } finally {
            plasticTargets.getReadWriteLock().writeLock().unlock();
        }
    }

	  private String safeStringCast(final Object o) {
        if (o==null) {
            return null;
        }
        return o.toString();
    }
    
    @Override
    protected List getLocalMessages() {
        return dynamicButtonMessages;
    }

    /**
	 * @author Noel Winstanley
	 * @since Jun 27, 200612:43:43 AM
	 */
	private final class PlasticInterrogatorWorker extends BackgroundWorker<Void> {
		/**
		 * @param parent
		 * @param msg
		 * @param id
		 */
		private PlasticInterrogatorWorker(final UIContext parent, final String msg, final URI id) {
			super(parent, msg);
			this.id = id;
			setTransient(true);
		}

		/**
		 * 
		 */
		private final URI id;

		@Override
        protected Void construct() throws Exception {
		    final int opCount = 6;
		    int progress = 0;
		    setProgress(progress,opCount);
		    final List noArgs = new ArrayList();
		    Object resp = singleTargetRequestResponseMessage(CommonMessageConstants.GET_IVORN,noArgs,this.id);
		    if (resp instanceof Object[]) { // not the type we expect, but what's come be used to symbolize 'null'
		        resp = null;
		    }
            final String ivorn = safeStringCast(resp);
            setProgress(++progress,opCount);
		    if (TupperwareImpl.this.vodesktop_ivorn.equals(ivorn)) {
		    	return null; // it's only ourselves
		    }
		    final String name =hub.getName(this.id) ;
            setProgress(++progress,opCount);		    
		    final String description = safeStringCast(singleTargetRequestResponseMessage(CommonMessageConstants.GET_DESCRIPTION,noArgs,this.id));
            setProgress(++progress,opCount);
//		    final String version = safeStringCast(singleTargetRequestResponseMessage(CommonMessageConstants.GET_VERSION,noArgs,this.id));
//            setProgress(++progress,opCount);		    
		    final String iconURLString = safeStringCast(singleTargetRequestResponseMessage(CommonMessageConstants.GET_ICON,noArgs,this.id));
            ImageIcon icon;
            try {
                final URL iconUrl = new URL(iconURLString);
                icon = IconHelper.loadIcon(iconUrl);
            } catch (final MalformedURLException e) {
                //Cannot rely on the client returning a well-formed URL, or indeed any URL.             
                icon = null; 
            }
            setProgress(++progress,opCount);
		    final List<URI> appMsgList = hub.getUnderstoodMessages(this.id);
		    // build up set of message types that this supports.
		    final Set<MessageType<?>> supportedTypes = new HashSet<MessageType<?>>();
		    CollectionUtils.select(allKnownMessages,new Predicate() {

                public boolean evaluate(final Object arg0) {
                    final MessageType mt = (MessageType)arg0;                    
                    return mt.getPlasticMessageType() != null
                        && appMsgList.contains(mt.getPlasticMessageType());
                }
		    }, supportedTypes);
		    
            setProgress(++progress,opCount);		    
		     final PlasticApplicationDescription result = new PlasticApplicationDescription(
		             this.id
		             ,name
		             ,description
		             ,supportedTypes
		             ,icon
		             ,TupperwareImpl.this
		             );
		     try {
		    	 plasticTargets.getReadWriteLock().writeLock().lock();
			if (! plasticTargets.contains(result)) {  // would be odd if it did already contain it.
				plasticTargets.add(result); // this should fire notifications, etc.
			}          
		     } finally {
		    	 plasticTargets.getReadWriteLock().writeLock().unlock();
		     }
	            setProgress(++progress,opCount);
			return null;
		}
	}


} // end inner class

// actions.
/** connect to a server via xmlrpc
 */
private class ConnectAction extends AbstractAction {

    public ConnectAction() {
        super("Register with PLASTIC");
        putValue(Action.SHORT_DESCRIPTION,"Accept interop requests from other tools");
    }
    public void actionPerformed(final ActionEvent e) {
        new BackgroundWorker<Void>(parent,"Registering with PLASTIC") {
            {
                setTransient(true);
            }
            @Override
            protected Void construct() throws Exception {
                // create an xmlrpc client.
                final URL hubEndpoint = PlasticUtils.getXmlRpcUrl();
                final XmlRpcClient client = new XmlRpcClient();
                final XmlRpcClientConfigImpl config = (XmlRpcClientConfigImpl) client.getClientConfig();
                config.setServerURL(hubEndpoint);
                 final XmlRpcCommonsTransportFactory factory = new XmlRpcCommonsTransportFactory(client);
                 factory.setHttpClient(httpClient);
                 client.setTransportFactory(factory);         
                
                 // create a connection to the hub.
                hub= new ArXmlRpcHub(client, null);
                
                // myPlasticId = hub.registerRMI(applicationName,new ArrayList(supportedMessages),TupperwareImpl.this);
                myPlasticId = hub.registerXMLRPC(applicationName,new ArrayList(supportedMessages),xmlrpcClientEndpoint);
                // only now enable appRegistered message handler - otherwise we just get notified about ourselves.
                applicationRegisteredMessageHandler.enable();
                // now check to see if we've missed any applications that have already registered
                final List ids = hub.getRegisteredIds();
                for (final Iterator i = ids.iterator(); i.hasNext(); ) {
                    final URI uri = (URI)i.next();
                    applicationRegisteredMessageHandler.interrogatePlasticApp(uri);
                }
                return null;
            }
            @Override
            protected void doFinished(final Void ignored) {
                connect.setEnabled(false);
                disconnect.setEnabled(true);
                startInternal.setEnabled(false);
            }            
        }.start();        
    }
}

/** disconnect from the server, and clean up the state */
private class DisconnectAction extends AbstractAction {
    public DisconnectAction() {
        super("Unregister with PLASTIC");
        setEnabled(false);
        putValue(Action.SHORT_DESCRIPTION,"Ignore interop requests from other tools");
    }
    public void actionPerformed(final ActionEvent e) {
        applicationRegisteredMessageHandler.disable();
        new BackgroundWorker<Void>(parent,"Unregistering with PLASTIC") {
            {
                setTransient(true);
            }
            @Override
            protected Void construct() throws Exception {               
                //bz 2814 - if the hub has already been shut down, this is noisy
                // so disable error reporting at this point.
                hub.setSilent(true);
                hub.unregister(myPlasticId);
                myPlasticId = null;
                hub = null;
                return null;
            }
            @Override
            protected void doFinished(final Void ignored) {
                connect.setEnabled(true);
                disconnect.setEnabled(false);
                startInternal.setEnabled(! PlasticUtils.isHubRunning());
            }
            
        }.start();
        try {
            plasticTargets.getReadWriteLock().writeLock().lock();
            plasticTargets.clear();
        } finally {
            plasticTargets.getReadWriteLock().writeLock().unlock();
        }
    }
}


private class StartInternalHubAction extends AbstractAction {
    public StartInternalHubAction() {
        super("Start internal PLASTIC Hub");
        setEnabled(! PlasticUtils.isHubRunning());
        putValue(Action.SHORT_DESCRIPTION,"Start a PLASTIC Hub running in this JVM");
    }
    public void actionPerformed(final ActionEvent e) {
        new BackgroundWorker<Void>(parent,"Starting internal PLASTIC Hub") {

            @Override
            protected Void construct() throws Exception {
                internalHub.start();
                return null;
            }
            @Override
            protected void doFinished(final Void ignored) {
                connect.actionPerformed(null);
            }
        }.start();
    }
}

// shutdown listener.

/** Custom subclass to not log errors when shutting down. 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Aug 19, 20081:34:07 PM
 */
private final class ArXmlRpcHub extends XmlRpcHub {
    
    private boolean silent = false;
    /**
     * @return the silent
     */
    public boolean isSilent() {
        return this.silent;
    }
    /** 
     * @param silent the silent to set
     */
    public void setSilent(final boolean silent) {
        this.silent = silent;
    }
    /**
     * @param client
     * @param properHub
     */
    public ArXmlRpcHub(final XmlRpcClient client, final PlasticHubListener properHub) {
        super(client, properHub);
    }
    // overridden to cure bz2814 (and related problems)
    // by logging using clogging and check whether we're in 'silent' mode
    // before reporting an error message.
    @Override
    protected Object xmlrpcCall(final String method, final Object[] args) {
        final Vector argv = new Vector();
        if ( args != null ) {
            for ( int i = 0; i < args.length; i++ ) {
                argv.add( XmlRpcAgent.doctorObject( args[ i ] ) );
            }
        }
        try {
            final Object result = client_.execute( "plastic.hub." + method, argv );
            if ( result instanceof XmlRpcException ) {
                throw (XmlRpcException) result;
            }
            return result;
        }
        catch ( final Exception e ) {
            if (! silent) {
                logger.warn(  "XML-RPC Execution error: " + e, e );                
            }
                return null;                
        }
    }
}

public void halting() { // disconnect from the hub.
    if (disconnect.isEnabled()) { //i.e. we're connected to the hub
        disconnect.actionPerformed(null);
    }
}

public String lastChance() {
    return null;
}

// hublistener interface - called when we detect the hub is stopping.
// remove all traces of the previous hub in the UI.
public void hubStopping() {
    SwingUtilities.invokeLater(new Runnable() {
        public void run() {
            connect.setEnabled(true);
            disconnect.setEnabled(false);
            startInternal.setEnabled(true);
            myPlasticId = null;
            hub = null;
            try {
                plasticTargets.getReadWriteLock().writeLock().lock();
                plasticTargets.clear();
            } finally {
                plasticTargets.getReadWriteLock().writeLock().unlock();
            }            
        }
    });
}


}

