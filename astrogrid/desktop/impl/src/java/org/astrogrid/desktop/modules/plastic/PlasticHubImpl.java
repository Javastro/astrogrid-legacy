package org.astrogrid.desktop.modules.plastic;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.builtin.Shutdown;
import org.astrogrid.acr.builtin.ShutdownListener;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.system.RmiServer;
import org.astrogrid.acr.system.SystemTray;
import org.astrogrid.acr.system.WebServer;
import org.astrogrid.common.namegen.NameGen;
import org.picocontainer.Startable;
import org.votech.plastic.CommonMessageConstants;
import org.votech.plastic.HubMessageConstants;
import org.votech.plastic.PlasticHubListener;
import org.votech.plastic.PlasticListener;
import org.votech.plastic.outgoing.PlasticException;

import EDU.oswego.cs.dl.util.concurrent.CountDown;
import EDU.oswego.cs.dl.util.concurrent.Executor;

public class PlasticHubImpl implements PlasticHubListener, PlasticHubListenerInternal, Startable, ShutdownListener {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(PlasticHubImpl.class);

	static final String PLASTIC_NOTIFICATIONS_ENABLED = "org.votech.plastic.notificationsenabled";

    private final ApplicationStore clients = new ApplicationStore();

    private final NameGen idGenerator;
    private final SystemTray tray;
    private final RmiServer rmiServer;
    private final WebServer webServer;
    private final Executor executor;

    private final URI hubId;

    private File plasticPropertyFile;

	private PrettyPrinterInternal prettyPrinter;

	private Configuration config;



	/**
	 * The keys used to identify this hub
	 * @see PlasticHubListener#PLASTIC_CONFIG_FILENAME
	 */
	public static final String ACR_VERSION = "acr.version";
	//public static final String HUB_IMPL = "acr.hub.impl";	



    /** constructor selected by pico when systemtray is not available */
    public PlasticHubImpl(Executor executor, NameGen idGenerator,
            MessengerInternal app, RmiServer rmi,  WebServer web, PrettyPrinterInternal prettyPrinter, Configuration config, Shutdown shutdown) {
        this(executor,idGenerator,app,rmi, web,null, prettyPrinter, config, shutdown);
    }
    
    /** constructor selected by pico when systemtray is available 
     * @param prettyPrinter */
    public PlasticHubImpl(Executor executor, NameGen idGenerator,
            MessengerInternal app,RmiServer rmi, WebServer web,SystemTray tray, PrettyPrinterInternal prettyPrinter, Configuration config, Shutdown shutdown) {
        this.tray = tray;
        this.rmiServer= rmi;
        this.webServer= web;
        this.executor = executor;
        this.idGenerator = idGenerator;
        this.prettyPrinter = prettyPrinter;
        this.config = config;
        shutdown.addShutdownListener(this);
        logger.info("Constructing a PlasticHubImpl");
        hubId = app.registerWith(this); 
    }

    public List getRegisteredIds() {
        List ids = clients.getIds();
        return ids;
    }

    public URI registerXMLRPC(String name, List supportedOperations, URL callBackURL) {
        PlasticClientProxy client = new XMLRPCPlasticClient(idGenerator, name, supportedOperations, callBackURL);
        return register(client);
    }

    public URI registerRMI(String name, List supportedOperations, PlasticListener caller) {
        PlasticClientProxy client = new RMIPlasticClient(idGenerator, name, supportedOperations, caller);
        return register(client);
    }

    public URI registerNoCallBack(String name) {
        PlasticClientProxy client = new DeafPlasticClient(idGenerator, name);
        return register(client);
    }

	public URI registerPolling(String name, List supportedMessages) {
		PlasticClientProxy client = new PollingPlasticClient(idGenerator, name, supportedMessages);
		return register(client);
	}

	/**
	 * Returns empty list if the id isn't for a polling client.
	 */
	public List pollForMessages(URI id) {
		PlasticClientProxy client = (PlasticClientProxy) clients.get(id);
		if (client==null) return CommonMessageConstants.EMPTY;
		if (!(client instanceof PollingPlasticClient)) return CommonMessageConstants.EMPTY; //always disliked instanceof, but it's that or maintain a separate list
		List messages = ((PollingPlasticClient)client).getStoredMessages();
		((PollingPlasticClient)client).flush();
		return messages;
	}
	
	
    // TODO - think about synch issues
    private synchronized URI register(PlasticClientProxy client) {
        URI id = client.getId();

        clients.add(client);
        
        // Tell the world
        List args = new ArrayList();
        args.add(id.toString());
        logger.info(id + " has registered");
        requestAsynch(hubId, HubMessageConstants.APPLICATION_REGISTERED_EVENT, args);

        // Only display this if it's not the hub itself that's registering
        // todo this is a bit of a hack...relies on the hubId not having been
        // set yet.
        if (hubId != null) {
            displayInfoMessage("Plastic", client.getName()+" has registered with Id " +id);
        }

        return id;
    }



    /**
     * Displays an info message on the system tray, if there is one.
     * @param caption
     * @param message 
     */
    private void displayInfoMessage(String caption, String message) {
    	//If the config is not set, then assume we will show popups
        if (tray != null && !("false".equalsIgnoreCase(config.getKey(PLASTIC_NOTIFICATIONS_ENABLED)))) {
            tray.displayInfoMessage(caption, message);
        } else {
            logger.info(caption + " : " + message);
        }

    }

    public void unregister(URI id) {
        if (id.equals(hubId)) {
            logger.warn("A client is attempting to unregister the hub itself - not allowed");
            return;
        }
        PlasticClientProxy client = (PlasticClientProxy) clients.get(id); 
        if (client==null) {
        	//No such client, so return and do nothing.
        	return;
        }
        clients.remove(id);
        
        //Tell the world
        Vector args = new Vector(); //TODO jdt why am I using vector?
        args.add(id.toString());
        requestAsynch(hubId, HubMessageConstants.APPLICATION_UNREGISTERED_EVENT, args);
        logger.info(id + " has unregistered");

        displayInfoMessage("Plastic", client.getName()+" has unregistered.");
    }

    /**
     * Send the message to all clients except the sender, subject to the recipients list.
     * 
     * @param sender hub id of sending app
     * @param message the message string being sent
     * @param args any arguments
     * @param recipients if nonzero length, then only multiplex to these recipients, otherwise send to all.  
     * @param shouldWaitForResults
     */
    private Map send(final URI sender, final URI message, final List args, List recipients,
            boolean shouldWaitForResults) {
    	//Gotcha.  The recipients are in a List of URIs from Java, but a List of Strings from xml-rpc
    	if (recipients.size()!=0 && recipients.get(0).getClass()==String.class) {
    		List recipientURIs = new ArrayList();
    		for (Iterator it = recipients.iterator();it.hasNext();recipientURIs.add(URI.create((String) it.next())));
    		recipients = recipientURIs;
    		//Now we can carry on with our List of URIs....
    	}
    	
        final Map returns = Collections.synchronizedMap(new HashMap());
        
        List clientsSupportingMessage = clients.getClientIdsSupportingMessage(message, true, true);
        Collection clientsToMessage;
        if (recipients.size()==0) {
        	clientsToMessage = clientsSupportingMessage; //send to everyone.  Ugly.
        } else {
        	clientsToMessage = CollectionUtils.intersection(clientsSupportingMessage, recipients);
        }
        clientsToMessage.remove(sender); //don't return to sender

        final CountDown gate = new CountDown(clientsToMessage.size());

        //
        // Local worker class to send the message, and add the result to our
        // list.
        // We use a gate to wait for all the threads to finish before exiting
        // the method.
        //
        class Messager implements Runnable {

            private PlasticClientProxy client;

            public Messager(PlasticClientProxy client) {
                this.client = client;
            }

            /*
             * (non-Javadoc)
             * 
             * @see java.lang.Runnable#run()
             */
            public void run() {
                try {
                	logger.debug(sender+" sending message "+message+" to "+client.getId());
                    Object rv = client.perform(sender, message, args);
                    logger.debug("Client "+client.getId()+" returned "+rv);
                    // A return value really shouldn't be null, as xml-rpc
                    // doesn't
                    // support it...but insulate ourselves in case java-rmi
                    // clients
                    // return null by mistake.
                    if (rv == null) {
                        rv = CommonMessageConstants.RPCNULL;
                    }
                    if (client.canRespond()) returns.put(client.getId(), rv); //no point in storing nulls from apps that can't respond
                } catch (PlasticException e) {
                    // Not much to do except log it...
                    // LOW consider sending a message, but beware we don't get
                    // an endlessly
                    // increasing cascade of messages.
                    logger.warn("There was an exception while messaging " + client.getId() + e);
                } finally {
                    gate.release();
                }
            }

        }// end of message class.

        Iterator it = clientsToMessage.iterator();
        while (it.hasNext()) {
            PlasticClientProxy currentClient = clients.get((URI) it.next());
            try {
                executor.execute(new Messager(currentClient));
            } catch (InterruptedException e) {
               logger.warn("Interrupted executing client "
                        + currentClient.getId(), e);
            }
        }

        if (shouldWaitForResults) {
            try {
                gate.acquire();
            } catch (InterruptedException e) {
                logger.warn("Interrupted aquiring gate ", e);
            }
        }
        return returns;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.votech.plastic.PlasticHub#send(java.lang.String, java.lang.String, java.util.Vector)
     */
    public Map request(URI sender, URI message, List args) {
        return send(sender, message, args, CommonMessageConstants.EMPTY, true);
    }

    public Map requestToSubset(URI sender, URI message, List args, List recipientIds) {
        return send(sender, message, args, recipientIds, true);
    }

    public void requestToSubsetAsynch(URI sender, URI message, List args, List recipientIds) {
        send(sender, message, args, recipientIds, false);
    }

    public void requestAsynch(URI sender, URI message, List args) {
        send(sender, message, args, CommonMessageConstants.EMPTY, false);
    }

    public URI getHubId() {
        return hubId;
    }

    public void start() {
        // Write out connection properties in a non-astrogriddy way
        // see the Plastic spec http://plastic.sourceforge.net and
        // discussions on the DS6 forum about debranding.
        try {
            int rmiPort = rmiServer.getPort();
            String xmlServer = webServer.getUrlRoot() + "xmlrpc";
            Properties props = new Properties();
            props.put(PlasticHubListener.PLASTIC_RMI_PORT_KEY, new Integer(rmiPort).toString());
            props.put(PlasticHubListener.PLASTIC_XMLRPC_URL_KEY, xmlServer);
            props.put(PlasticHubListener.PLASTIC_VERSION_KEY, PlasticListener.CURRENT_VERSION);
            String version = System.getProperty("workbench.version","Unknown");
            props.put(PlasticHubImpl.ACR_VERSION, version);
            //props.put(PlasticHubImpl.HUB_IMPL, this.toString());
            File homeDir = new File(System.getProperty("user.home"));
            plasticPropertyFile = new File(homeDir, PlasticHubListener.PLASTIC_CONFIG_FILENAME);
            if (plasticPropertyFile.exists()) {
                logger.info("Plastic config file was already present");
                Properties alreadyPresent = loadExistingDotPlastic();
                String whoisit = alreadyPresent.getProperty(PlasticHubImpl.ACR_VERSION);
                
                if (version.equals(whoisit)) {
                	logger.warn("An ACR hub of version "+version+" left this file - probably orphaned...deleting");
                	plasticPropertyFile.delete();
                } else {
                	logger.warn("A Plastic hub " + whoisit==null ? " of unknown origin " :" (ACR version +"+whoisit+") " + "is already running.  Plastic features will NOT be available through this ACR");
                	return; //TODO disable hub
                }
                
            }
            plasticPropertyFile.deleteOnExit();
            OutputStream os = new BufferedOutputStream(new FileOutputStream(plasticPropertyFile));
            props.store(os, "Plastic Hub Properties.  See http://plastic.sourceforge.net");
            os.close();
        } catch (IOException e) {
            logger.error("There was a problem creating the Plastic config file .plastic",e);
        }
    }

	private Properties loadExistingDotPlastic() throws FileNotFoundException, IOException {
		Properties alreadyPresent = new Properties();
		InputStream is = new BufferedInputStream(new FileInputStream(plasticPropertyFile));
		alreadyPresent.load(is);
		is.close();
		return alreadyPresent;
	}

    public void stop() {
        // Plastic properties file should already be deleted automatically on exit
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.votech.plastic.PlasticHub#purgeNonresponsiveApps()
     */
    public List purgeUnresponsiveApps() {
        List nonResponders = getNonResponders();
        Set deadIds = new HashSet(nonResponders);
        Iterator it = deadIds.iterator();
        while (it.hasNext()) {
            URI id = (URI) it.next();
            unregister(id);
        }
        logger.info("Removed " + nonResponders.size() + " dead applications");
        return nonResponders;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.votech.plastic.PlasticHubListener#markUnresponsiveApps()
     */
    public List markUnresponsiveApps() {
        // ping them. Applications will automcatically mark themselves as duff.
        List args = new ArrayList();
        args.add("ping");
        request(hubId, CommonMessageConstants.ECHO, args);
        return getNonResponders();
    }

    private List getNonResponders() {
        List dead = new ArrayList();
        List appsIds = clients.getIds();
        Iterator it = appsIds.iterator();
        while (it.hasNext()) {
            URI proxyId = (URI) it.next();
            PlasticClientProxy proxy = (PlasticClientProxy) clients.get(proxyId);
            if (!proxy.isResponding()) {
                URI id = proxy.getId();
                logger.debug("Application " + id + " is marked as dead.");
                dead.add(id);
            }
        }
        return dead;
    }

	public String getName(URI plid) {
		PlasticClientProxy client = (PlasticClientProxy) clients.get(plid);
		if (client==null) {
			return ""; //TODO CommonMessageConstants.RPCNULL; 
		}
		return client.getName(); 
	}

	/**
	 * At the moment this only returns apps that have explicitly registered
	 * for that message.  ie. apps that register without saying what they're
	 * interested in (in the hope of receiving all messages) won't be returned.
	 */
	public List getMessageRegisteredIds(URI message) {
		return clients.getClientIdsSupportingMessage(message, false, true);
	}

	public List getUnderstoodMessages(URI plid) {
		PlasticClientProxy client = (PlasticClientProxy) clients.get(plid);
		if (client==null) {
			return CommonMessageConstants.EMPTY; //TODO really must think about returning nulls and xml-rpc.  Returning the empty list is the same as saying "I understand every message"
		}
		return client.getMessages();
	}
	
	public void prettyPrintRegisteredApps() throws IOException, ACRException {
		List applicationDescriptions = new ArrayList();
		List apps = getRegisteredIds();
		URI hubId2 = getHubId();
		apps.remove(hubId2);

		Map ivorns = request(hubId2, CommonMessageConstants.GET_IVORN, CommonMessageConstants.EMPTY);
		Map icons = request(hubId2, CommonMessageConstants.GET_ICON, CommonMessageConstants.EMPTY);
		Map versions = request(hubId2, CommonMessageConstants.GET_VERSION, CommonMessageConstants.EMPTY);
		Map descriptions = request(hubId2, CommonMessageConstants.GET_DESCRIPTION, CommonMessageConstants.EMPTY);
		
		Iterator it = apps.iterator();
		while (it.hasNext()) {
			URI plid = (URI) it.next();
			String name = getName(plid);
			List messages = getUnderstoodMessages(plid);
			PlasticClientProxy client = clients.get(plid);
			boolean alive = client.isResponding();
			String version = safeStringCast(versions, plid); //avoid casting in case the client's been naughty
			String icon = safeStringCast(icons, plid);
			String ivorn = safeStringCast(ivorns, plid);
			String description = safeStringCast(descriptions, plid);
			
			ApplicationDescription desc = new ApplicationDescription(plid.toString(),name,description,messages,version,icon,ivorn,alive);
			applicationDescriptions.add(desc);
		}
		
		prettyPrinter.show(applicationDescriptions);

	}

	private String safeStringCast(Map map, URI key) {
		Object mapo = map.get(key);
		return mapo!=null ? mapo.toString() : "";
	}
	
	public void setNotificationsEnabled(boolean enable) {
		config.setKey(PLASTIC_NOTIFICATIONS_ENABLED, Boolean.toString(enable));
	}

	public void halting() {
		//Message should be sent _after_ apps have been given a chance to stop it.
		requestAsynch(hubId, HubMessageConstants.HUB_STOPPING_EVENT, CommonMessageConstants.EMPTY);
	}

	public String lastChance() {
		return null; //returning null indicates we don't care.
	}

}
