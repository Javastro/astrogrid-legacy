/**
 * 
 */
package org.astrogrid.desktop.modules.system;

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

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

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
import org.astrogrid.desktop.SplashWindow;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.plastic.PlasticApplicationDescription;
import org.astrogrid.desktop.modules.plastic.PlasticHubListenerInternal;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.votech.plastic.CommonMessageConstants;
import org.votech.plastic.HubMessageConstants;
import org.votech.plastic.PlasticHubListener;
import org.votech.plastic.PlasticListener;
import org.votech.plastic.incoming.handlers.AbstractMessageHandler;
import org.votech.plastic.incoming.handlers.MessageHandler;
import org.votech.plastic.incoming.handlers.StandardHandler;
import org.votech.plastic.incoming.messages.hub.HubListener;

import uk.ac.starlink.plastic.PlasticUtils;
import uk.ac.starlink.plastic.XmlRpcHub;
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
	public TupperwareImpl(final UIContext parent
	        ,  final String applicationName, final String description, final String icon,final String ivorn
	        , final URL clientEndpoint
	        , final List<AbstractMessageHandler> handlers, final EventList<PlasticApplicationDescription> model
	        ,final PlasticHubListenerInternal internalHub
	        ,final HttpClient httpClient
	        ) throws IOException {
		super();
		
        this.parent = parent;
        this.applicationName = applicationName;
        this.vodesktop_ivorn = ivorn;
        this.httpClient = httpClient;
        this.xmlrpcClientEndpoint = new URL(clientEndpoint.toString() + "xmlrpc");
		this.model = model;
        this.internalHub = internalHub;
        
		this.supportedMessages = new HashSet();
		
		// add default handlers to the contributed list.. - contribution list is immutable, so need to take a copy
		final List<AbstractMessageHandler> allHandlers = new ArrayList<AbstractMessageHandler>(handlers);
		allHandlers.add(new StandardHandler(applicationName,description,ivorn,icon,PlasticListener.CURRENT_VERSION,this,null));
		applicationRegisteredMessageHandler = new ApplicationRegisteredMessageHandler();
		allHandlers.add(applicationRegisteredMessageHandler);
		
		// process handler contribution - extract list of handled messages, and chain all the handlers together.
		MessageHandler firstHandler = null;
		MessageHandler lastHandler = null;
		for (final Iterator<AbstractMessageHandler> i = allHandlers.iterator(); i.hasNext(); ) {
			final MessageHandler mh = i.next();
			final List messages = mh.getHandledMessages();
			logger.debug(messages);
			if (messages == null || messages.size() ==0) {
				logger.warn("Skipping - No messages registered for handler" + mh);
				continue;
			} 
			supportedMessages.addAll(mh.getHandledMessages());
			if (firstHandler == null) {
				firstHandler = mh;
			} else {
				lastHandler.setNextHandler(mh);
			}
			lastHandler = mh; 
		}
		this.plasticHandler = firstHandler;
		logger.info("Will handle messages:" + supportedMessages);
		
	    if (PlasticUtils.isHubRunning()) {
	        connect.actionPerformed(null);
	    } else {
	        startInternal.actionPerformed(null);
	    }
	
	}

	private final ConnectAction connect = new ConnectAction();
	private final DisconnectAction disconnect = new DisconnectAction();
	/** reference to the hub we've connected to */
	private  PlasticHubListener hub;
	private final EventList<PlasticApplicationDescription>  model;
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
            final List params = Arrays.asList(arr);
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

    

public class ApplicationRegisteredMessageHandler extends AbstractMessageHandler {

	private final List dynamicButtonMessages = new ArrayList() {
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
        }
        if (message.equals(HubMessageConstants.APPLICATION_UNREGISTERED_EVENT)) {            
            removePlasticApp(new URI(args.get(0).toString()));
        }
        } catch (final URISyntaxException e) { // don't care overmuch.
            logger.warn(e);       
        }
    	}
        return null;
    }

	/** remove a plastic application from the container.
     * 
     * @param id plastic id of the application to remove 
     */
    public void removePlasticApp(final URI id) {      
        try {
            model.getReadWriteLock().writeLock().lock();
            	for (int i = 0; i < model.size(); i++) {
            		final PlasticApplicationDescription pad = model.get(i);
            		if (pad.getId().equals(id)) {
            			model.remove(i);
            			return;
            		}
            	}
        } finally {
            model.getReadWriteLock().writeLock().unlock();
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
	private final class PlasticInterrogatorWorker extends BackgroundWorker {
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
        protected Object construct() throws Exception {
		    final int opCount = 7;
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
		    final String version = safeStringCast(singleTargetRequestResponseMessage(CommonMessageConstants.GET_VERSION,noArgs,this.id));
            setProgress(++progress,opCount);		    
		    final String iconURLString = safeStringCast(singleTargetRequestResponseMessage(CommonMessageConstants.GET_ICON,noArgs,this.id));
		    URL iconUrl;
            ImageIcon icon;
            try {
                iconUrl = new URL(iconURLString);
                icon = IconHelper.loadIcon(iconUrl);
            } catch (final MalformedURLException e) {
                //Cannot rely on the client returning a well-formed URL, or indeed any URL.
                iconUrl = null;
                icon = null; 
            }
            setProgress(++progress,opCount);
		    final List appMsgList = hub.getUnderstoodMessages(this.id);
            setProgress(++progress,opCount);		    
		     final PlasticApplicationDescription result = new PlasticApplicationDescription(this.id,name,description,appMsgList,version,icon,iconUrl,ivorn);
		     try {
		    	 model.getReadWriteLock().writeLock().lock();
			if (! model.contains(result)) {  // would be odd if it did already contain it.
				model.add(result); // this should fire notifications, etc.
			}          
		     } finally {
		    	 model.getReadWriteLock().writeLock().unlock();
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
        SplashWindow.reportProgress("Registering with PLASTIC..."); // only shown when called as part of the startup routines.
        new BackgroundWorker(parent,"Registering with PLASTIC") {
            {
                setTransient(true);
            }
            @Override
            protected Object construct() throws Exception {
                // create an xmlrpc client.
                final URL hubEndpoint = PlasticUtils.getXmlRpcUrl();
                final XmlRpcClient client = new XmlRpcClient();
                final XmlRpcClientConfigImpl config = (XmlRpcClientConfigImpl) client.getClientConfig();
                config.setServerURL(hubEndpoint);
                 final XmlRpcCommonsTransportFactory factory = new XmlRpcCommonsTransportFactory(client);
                 factory.setHttpClient(httpClient);
                 client.setTransportFactory(factory);         
                
                 // create a connection to the hub.
                hub= new XmlRpcHub(client,null);
                
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
            protected void doFinished(final Object result) {
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
        new BackgroundWorker(parent,"Unregistering with PLASTIC") {
            {
                setTransient(true);
            }
            @Override
            protected Object construct() throws Exception {               
                hub.unregister(myPlasticId);
                myPlasticId = null;
                hub = null;
                return null;
            }
            @Override
            protected void doFinished(final Object result) {
                connect.setEnabled(true);
                disconnect.setEnabled(false);
                startInternal.setEnabled(! PlasticUtils.isHubRunning());
            }
            
        }.start();
        try {
            model.getReadWriteLock().writeLock().lock();
            model.clear();
        } finally {
            model.getReadWriteLock().writeLock().unlock();
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
        new BackgroundWorker(parent,"Starting internal PLASTIC Hub") {

            @Override
            protected Object construct() throws Exception {
                internalHub.start();
                return null;
            }
            @Override
            protected void doFinished(final Object result) {
                connect.actionPerformed(null);
            }
        }.start();
    }
}

// shutdown listener.

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
                model.getReadWriteLock().writeLock().lock();
                model.clear();
            } finally {
                model.getReadWriteLock().writeLock().unlock();
            }            
        }
    });
}


}

