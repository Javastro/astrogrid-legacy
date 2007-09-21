/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.ImageIcon;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

import ca.odell.glazedlists.EventList;

/** Implementation of the tupperware container.
 * @author Noel Winstanley
 * @since Jun 16, 20061:52:12 PM
 */
public class TupperwareImpl implements TupperwareInternal, PlasticListener {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(TupperwareImpl.class);

	
	
	private static final String ICON_URL = "http://www.astrogrid.org/image/AGlogo"; 
	private static final String AR_IVORN = "ivo://org.astrogrid/ar";
	/**
	 * 
	 * @param hub plastic hub to register to.
	 * @param applicationName current name of workbench - may change, depending on variant.
	 * @param description variable description of this workbench.
	 * @param handlers contribution list of handlers to register. 
	 */
	public TupperwareImpl(UIContext parent, PlasticHubListenerInternal hub, String applicationName, String description, List handlers, EventList appList) {
		super();
		this.parent = parent;
		this.model = appList;
		this.hub = hub;
		Set supportedMessages = new HashSet();
		// add default handlers to the list.. - contribution list is immutable, so need to take a copy
		handlers = new ArrayList(handlers);
		handlers.add(new StandardHandler(applicationName,description,AR_IVORN,ICON_URL,PlasticListener.CURRENT_VERSION));
		ApplicationRegisteredMessageHandler applicationRegisteredMessageHandler = new ApplicationRegisteredMessageHandler();
		handlers.add(applicationRegisteredMessageHandler);
		// process contributions..
		MessageHandler firstHandler = null;
		MessageHandler lastHandler = null;
		for (Iterator i = handlers.iterator(); i.hasNext(); ) {
			MessageHandler mh = (MessageHandler)i.next();
			List messages = mh.getHandledMessages();
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
		plasticHandler = firstHandler;
		logger.info("Will handle messages:" + supportedMessages);
		this.myPlasticId = hub.registerSelf(applicationName,new ArrayList(supportedMessages),this);
		// only now enable appRegistered message handler - otherwise we just get notified about ourselves.
		applicationRegisteredMessageHandler.enable();
		// now check to see if we've missed any applications already registered (unlikely).
		List ids = hub.getRegisteredIds();
		for (Iterator i = ids.iterator(); i.hasNext(); ) {
			URI uri = (URI)i.next();
			applicationRegisteredMessageHandler.interrogatePlasticApp(uri);
		}

	}

	private final UIContext parent;
	private final URI myPlasticId;
	private final EventList  model;
	private final PlasticHubListener hub;
	private final MessageHandler plasticHandler;

	public EventList getRegisteredApplications() {
		return model;
	}


	// plastic listener interface.
	public Object perform(URI arg0, URI arg1, List arg2) {
		return plasticHandler.perform(arg0,arg1,arg2);
	}

	/** convenience method - calls a single application */
	public Object singleTargetPlasticMessage(URI message, List args, URI target) {
	    if (logger.isDebugEnabled()) {
	        logger.debug("Single target:" + target + " message:" + message + " args:" + args);
	    }
	    List targetSet = new ArrayList();
	    targetSet.add(target);
	    Map m = hub.requestToSubset(myPlasticId, message, args, targetSet);
	    if (m == null || ! m.containsKey(target)) {
	        return null;
	    } else {
	        return m.get(target);
	    }
	}
	
	/** broadcast a message */
	public Map broadcastPlasticMessage(URI message, List args) {
	    if (logger.isDebugEnabled()) {
	        logger.debug("Broadcast message: " + message + " args:" + args);
	    }
		return hub.request(myPlasticId,message,args);
	}
	
	public void  broadcastPlasticMessageAsynch(URI message, List args) {
        if (logger.isDebugEnabled()) {
            logger.debug("Broadcast message asynch: " + message + " args:" + args);
        }
	    hub.requestAsynch(myPlasticId,message,args);
	}

	public boolean somethingAccepts(URI message) {
		for (int i = 0; i < model.size(); i++) {
			PlasticApplicationDescription desc = (PlasticApplicationDescription)model.get(i);
			if (desc.understandsMessage(message)) {
				return true;
			}
		}
		return false;
	}
	
public class ApplicationRegisteredMessageHandler extends AbstractMessageHandler {

	private boolean enabled = false;
	public void enable() {
		enabled  = true;
	}
		
	
	
    /**
	 * @author Noel Winstanley
	 * @since Jun 27, 200612:43:43 AM
	 */
	private final class PlasticInterrogatorWorker extends BackgroundWorker {
		/**
		 * 
		 */
		private final URI id;

		/**
		 * @param parent
		 * @param msg
		 * @param id
		 */
		private PlasticInterrogatorWorker(UIContext parent, String msg, URI id) {
			super(parent, msg);
			this.id = id;
			setTransient(true);
		}

		protected Object construct() throws Exception {
		    List noArgs = new ArrayList();
		    String ivorn = safeStringCast(singleTargetPlasticMessage(CommonMessageConstants.GET_IVORN,noArgs,this.id));
		    if (AR_IVORN.equals(ivorn)) {
		    	return null; // it's only ourselves
		    }
		    String name =hub.getName(this.id) ;
		    String description = safeStringCast(singleTargetPlasticMessage(CommonMessageConstants.GET_DESCRIPTION,noArgs,this.id));
		    String version = safeStringCast(singleTargetPlasticMessage(CommonMessageConstants.GET_VERSION,noArgs,this.id));
		    String iconURLString = safeStringCast(singleTargetPlasticMessage(CommonMessageConstants.GET_ICON,noArgs,this.id));
		    URL iconUrl;
            ImageIcon icon;
            try {
                iconUrl = new URL(iconURLString);
                icon = IconHelper.loadIcon(iconUrl);
            } catch (MalformedURLException e) {
                //Cannot rely on the client returning a well-formed URL, or indeed any URL.
                iconUrl = null;
                icon = null; //TODO default?
            }
			
		    List appMsgList = hub.getUnderstoodMessages(this.id);
		     PlasticApplicationDescription result = new PlasticApplicationDescription(this.id,name,description,appMsgList,version,icon,iconUrl,ivorn);
		     try {
		    	 model.getReadWriteLock().writeLock().lock();
			if (result != null && ! model.contains(result)) {  // would be odd if it did already contain it.
				model.add(result); // this should fire notifications, etc.
			}          
		     } finally {
		    	 model.getReadWriteLock().writeLock().unlock();
		     }
			return null;
		}
	}
    
    private String safeStringCast(Object o) {
        if (o==null) return null;
        return o.toString();
    }

	protected List getLocalMessages() {
        return dynamicButtonMessages;
    }

	private final List dynamicButtonMessages = new ArrayList() {
	    {// init
	        this.add(HubMessageConstants.APPLICATION_REGISTERED_EVENT);
	        this.add(HubMessageConstants.APPLICATION_UNREGISTERED_EVENT);
	    }
	};

	  /** called to handle messages */
    public Object perform(URI sender, URI message, List args) {
    	if (enabled) {
        try {
        if (message.equals(HubMessageConstants.APPLICATION_REGISTERED_EVENT)) {
            interrogatePlasticApp(new URI(args.get(0).toString())); //redundant, but reliable.
        }
        if (message.equals(HubMessageConstants.APPLICATION_UNREGISTERED_EVENT)) {            
            removePlasticApp(new URI(args.get(0).toString()));
        }
        } catch (URISyntaxException e) { // don't care overmuch.
            logger.warn(e);       
        }
    	}
        return null;
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

    /** remove a plastic application from the container.
     * 
     * @param id plastic id of the application to remove 
     */
    public void removePlasticApp(final URI id) {      
            	for (int i = 0; i < model.size(); i++) {
            		PlasticApplicationDescription pad = (PlasticApplicationDescription)model.get(i);
            		if (pad.getId().equals(id)) {
            			model.remove(i);
            		}
            	}
    }
} // end inner class



}

