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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.builtin.ShutdownListener;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.system.RmiServer;
import org.astrogrid.acr.system.SystemTray;
import org.astrogrid.acr.system.WebServer;
import org.astrogrid.common.namegen.NameGen;
import org.astrogrid.desktop.modules.system.SnitchInternal;
import org.votech.plastic.CommonMessageConstants;
import org.votech.plastic.HubMessageConstants;
import org.votech.plastic.PlasticHubListener;
import org.votech.plastic.PlasticListener;
import org.votech.plastic.outgoing.PlasticException;

import EDU.oswego.cs.dl.util.concurrent.CountDown;
import EDU.oswego.cs.dl.util.concurrent.DirectExecutor;
import EDU.oswego.cs.dl.util.concurrent.Executor;

public class PlasticHubImpl implements PlasticHubListener, PlasticHubListenerInternal, ShutdownListener {
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
    private final Executor systemExecutor;
    private final URI hubId;

    private File plasticPropertyFile;

	private PrettyPrinterInternal prettyPrinter;

	private Configuration config;

	private boolean weWroteTheConfigFile;

	private Executor sequentialExecutor = new DirectExecutor();



	/**
	 * The keys used to identify this hub
	 * @see PlasticHubListener#PLASTIC_CONFIG_FILENAME
	 */
	public static final String ACR_VERSION = "acr.version";
	//public static final String HUB_IMPL = "acr.hub.impl";	



    /** constructor selected by acrFactory when systemtray is not available */
    public PlasticHubImpl(Executor executor, NameGen idGenerator,
            RmiServer rmi,  WebServer web, PrettyPrinterInternal prettyPrinter, Configuration config) {
        this(executor,idGenerator,rmi, web,null, prettyPrinter, config);
    }
    
    /** constructor selected by acrFactory when systemtray is available 
     * @param prettyPrinter */
    public PlasticHubImpl(Executor executor, NameGen idGenerator,
            RmiServer rmi, WebServer web,SystemTray tray, PrettyPrinterInternal prettyPrinter, Configuration config) {
        this.tray = tray;
        this.rmiServer= rmi;
        this.webServer= web;
        this.systemExecutor = executor;
        this.idGenerator = idGenerator;
        this.prettyPrinter = prettyPrinter;
        this.config = config;
        logger.info("Constructing a PlasticHubImpl");
        hubId = URI.create("plastic://ar-hub");
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
        List args = new Vector();
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
     * @param singleThreaded if true, will send all the messages on a single thread.
     * 
     * 
     * @todo this methid is getting rather unwieldy
     */
    private Map send(final URI sender, final URI message, final List args, List recipients,
            boolean shouldWaitForResults, boolean singleThreaded) {
    	//xmlrpc<->Java gotchas
    	//Gotcha 1.  The recipients are in a List of URIs from Java, but a List of Strings from xml-rpc
    	// TODO JDK1.5 this should go away with Java 5
    	if (recipients.size()!=0 && recipients.get(0).getClass()==String.class) {
    		List recipientURIs = new Vector();
    		for (Iterator it = recipients.iterator();it.hasNext();recipientURIs.add(URI.create((String) it.next())));
    		recipients = recipientURIs;
    		//Now we can carry on with our List of URIs....
    	}
    	//Gotcha 2.   The spec says that xml-rpc arrays can be converted to Lists.  In fact, with the current
    	//version of the xml-rpc library, they must be Vectors.  This will go away with version 3.
    	final Vector safeArgs = sanitizeXmlRpcTypes(args);
    	
        final Map returns = Collections.synchronizedMap(new HashMap());
        
        List clientsSupportingMessage = clients.getClientIdsSupportingMessage(message);
        Collection clientsToMessage;
        if (recipients==EVERYONE) {
        	clientsToMessage = clientsSupportingMessage; 
        } else {
        	clientsToMessage = CollectionUtils.intersection(clientsSupportingMessage, recipients);
        }
        clientsToMessage.remove(sender); //don't message the sender

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
                    Object rv = client.perform(sender, message, safeArgs);
                    logger.debug("Client "+client.getId()+" returned "+rv);
                    // A return value really shouldn't be null, as xml-rpc doesn't support it
                    // ...but insulate ourselves in case java-rmi clients return null by mistake.
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

                //              Automatic purge of dead applications
                if (!client.isResponding()) {
                    logger.debug("Client "+client.getName()+ "("+client.getId()+")"+" is not responding.  Attempting to unregister");
                    unregister(client.getId());
            }
            }

        }// end of message class.

        Executor localExecutor = singleThreaded ? sequentialExecutor : systemExecutor;
        
        Iterator it = clientsToMessage.iterator();
        while (it.hasNext()) {
            PlasticClientProxy currentClient = clients.get((URI) it.next());
            try {
            	localExecutor.execute(new Messager(currentClient));
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

    /**
     * With the current version of xmlrpc libraries, only Vectors are compatible with xml-rpc arrays.
     * Furthermore, only only Integers become ints.
     * The need for this method will disappear with version 3 of the xml-rpc lib when any List will be allowed.
     * Thank God.
     * @param args
     * @return
     */
    private Vector sanitizeXmlRpcTypes(List args) {
		Vector toReturn;
		if (!(args instanceof Vector)) {
			toReturn = new Vector(args);
		} else {
			toReturn = (Vector) args;
		}
		for (int i=0;i<toReturn.size();++i) {
			Object item = toReturn.get(i);
			if (item instanceof List) {
				toReturn.set(i, sanitizeXmlRpcTypes((List)item));
			}
		}
		return toReturn;
	}

    private final List EVERYONE = Collections.unmodifiableList(new ArrayList());
    
	/*
     * (non-Javadoc)
     * 
     * @see org.votech.plastic.PlasticHub#send(java.lang.String, java.lang.String, java.util.Vector)
     */
    public Map request(URI sender, URI message, List args) {
        return send(sender, message, args, EVERYONE, true, false);
    }

    public Map requestToSubset(URI sender, URI message, List args, List recipientIds) {
        return send(sender, message, args, recipientIds, true, false);
    }

    public void requestToSubsetAsynch(URI sender, URI message, List args, List recipientIds) {
        send(sender, message, args, recipientIds, false, false);
    }

    public void requestAsynch(URI sender, URI message, List args) {
        send(sender, message, args, EVERYONE, false, false);
    }

    private void requestSingleThreaded(URI sender, URI message, List args) {
    	send(sender, message, args, EVERYONE, false, true);
    }
    
    public URI getHubId() {
        return hubId;
    }

    public void start() {
        // Write out connection properties in a non-astrogriddy way
        // see the Plastic spec http://plastic.sourceforge.net and
        // discussions on the DS6 forum about debranding.
    	weWroteTheConfigFile = false;
    	OutputStream os = null;
        try {
            int rmiPort = rmiServer.getPort();
            String xmlServer = webServer.getUrlRoot() + "xmlrpc";
            Properties props = new Properties();
            props.put(PlasticHubListener.PLASTIC_RMI_PORT_KEY, Integer.toString(rmiPort));
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
            weWroteTheConfigFile = true;
            os = new BufferedOutputStream(new FileOutputStream(plasticPropertyFile));
            props.store(os, "Plastic Hub Properties.  See http://plastic.sourceforge.net");
        } catch (IOException e) {
            logger.error("There was a problem creating the Plastic config file .plastic",e);
        } finally {
        	if (os != null) {
        		try {
        			os.close();
        		} catch (IOException ignored) {
        		}
        	}
        }
    }

	private Properties loadExistingDotPlastic() throws FileNotFoundException, IOException {
		Properties alreadyPresent = new Properties();
		InputStream is = null; 
		try {
			is = new BufferedInputStream(new FileInputStream(plasticPropertyFile));
			alreadyPresent.load(is);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
		}
		return alreadyPresent;
	}




	public String getName(URI plid) {
		PlasticClientProxy client = (PlasticClientProxy) clients.get(plid);
		if (client==null) {
			return ""; //TODO CommonMessageConstants.RPCNULL; 
		}
		return client.getName(); 
	}

	/**
	 * Return all applications that have registered for a particular message.
	 */
	public List getMessageRegisteredIds(URI message) {
		return clients.getClientIdsSupportingMessage(message);
	}

	public List getUnderstoodMessages(URI plid) {
		PlasticClientProxy client = (PlasticClientProxy) clients.get(plid);
		if (client==null) {
			return CommonMessageConstants.EMPTY; //TODO really must think about returning nulls and xml-rpc.  Returning the empty list is the same as saying "I understand every message"
		}
		return client.getMessages();
	}
	
	public void prettyPrintRegisteredApps() throws IOException, ACRException {
		List applicationDescriptions = new Vector();
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
			
			String version = safeStringCast(versions, plid); //avoid casting in case the client's been naughty and returned a null
			String icon = safeStringCast(icons, plid);
			String ivorn = safeStringCast(ivorns, plid);
			String description = safeStringCast(descriptions, plid);
			
			PlasticApplicationDescription desc = new PlasticApplicationDescription(plid,name,description,messages,version,icon,ivorn);
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
		requestSingleThreaded(hubId, HubMessageConstants.HUB_STOPPING_EVENT, EVERYONE);
		
    	if (plasticPropertyFile != null && weWroteTheConfigFile && plasticPropertyFile.exists()) {
    		plasticPropertyFile.delete();
    	}
    }

	public String lastChance() {
		return null; //returning null indicates we don't care.
	}


}
