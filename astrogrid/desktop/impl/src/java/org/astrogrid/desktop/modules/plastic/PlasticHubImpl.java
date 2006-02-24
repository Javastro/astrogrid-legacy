package org.astrogrid.desktop.modules.plastic;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
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
import org.astrogrid.acr.system.BrowserControl;
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

public class PlasticHubImpl implements PlasticHubListener, PlasticHubListenerInternal, Startable {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(PlasticHubImpl.class);

    private final ApplicationStore clients = new ApplicationStore();

    final NameGen idGenerator;
    private final SystemTray tray;
    private final RmiServer rmiServer;
    private final WebServer webServer;
    private final Executor executor;

    private final URI hubId;

    private File plasticPropertyFile;

	private BrowserControl browser;

    /** constructor selected by pico when systemtray is not available */
    public PlasticHubImpl(Executor executor, NameGen idGenerator,
            MessengerInternal app, RmiServer rmi, BrowserControl browser, WebServer web) {
        this(executor,idGenerator,app,rmi,browser, web,null);
    }
    
    /** constructor selected by pico when systemtray is available */
    public PlasticHubImpl(Executor executor, NameGen idGenerator,
            MessengerInternal app,RmiServer rmi, BrowserControl browser, WebServer web,SystemTray tray) {
        this.tray = tray;
        this.rmiServer= rmi;
        this.browser = browser;
        this.webServer= web;
        this.executor = executor;
        this.idGenerator = idGenerator;
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
            displayInfoMessage("Plastic", id + " has registered.");
        }

        return id;
    }



    /**
     * Displays an info message on the system tray, if there is one.
     * @param caption
     * @param message 
     */
    private void displayInfoMessage(String caption, String message) {
        if (tray != null) {
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
        clients.remove(id);
        
        //Tell the world
        Vector args = new Vector(); //TODO jdt why am I using vector?
        args.add(id.toString());
        requestAsynch(hubId, HubMessageConstants.APPLICATION_UNREGISTERED_EVENT, args);
        logger.info(id + " has unregistered");

        if (tray != null) {
            tray.displayInfoMessage("Plastic", "The application with id " + id + " has unregistered.");
        }
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
                    Object rv = client.perform(sender, message, args);

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
        // todo To allow concurrent use of the ACR and alterative
        // platic hubs (if there should ever be one),
        // we'll need a mechanism to disable PLASTIC
        // and prevent this file being written.
        try {
            int rmiPort = rmiServer.getPort();
            String xmlServer = webServer.getUrlRoot() + "xmlrpc";
            Properties props = new Properties();
            props.put(PlasticHubListener.PLASTIC_RMI_PORT_KEY, new Integer(rmiPort).toString());
            props.put(PlasticHubListener.PLASTIC_XMLRPC_URL_KEY, xmlServer);
            props.put(PlasticHubListener.PLASTIC_VERSION_KEY, PlasticListener.CURRENT_VERSION);
            File homeDir = new File(System.getProperty("user.home"));
            plasticPropertyFile = new File(homeDir, PlasticHubListener.PLASTIC_CONFIG_FILENAME);
            if (plasticPropertyFile.exists()) {
                // If there are competing versions of Plastic we'll have to
                // change this behaviour.
                logger.warn("Plastic config file was already present - deleting");
                plasticPropertyFile.delete();
            }
            plasticPropertyFile.deleteOnExit();
            OutputStream os = new BufferedOutputStream(new FileOutputStream(plasticPropertyFile));
            props.store(os, "Plastic Hub Properties.  See http://plastic.sourceforge.net");
            os.close();
        } catch (IOException e) {
            logger.error("There was a problem creating the Plastic config file .plastic");
        }
    }

    public void stop() {
        requestAsynch(hubId, HubMessageConstants.HUB_STOPPING_EVENT, CommonMessageConstants.EMPTY);
        if (plasticPropertyFile != null) {
            logger.debug("Deleting Plastic Property File");
            plasticPropertyFile.delete();
        }
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
	
	//TODO remove NULLS and broken images and consider linking to the ACR reg browser
	public void prettyPrintRegisteredApps() throws IOException, ACRException {
		File file = File.createTempFile("registeredapps",".html");
		PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file)));
		List apps = getRegisteredIds();
		writer.println("<html><head><title>Plastic Registered Apps</title></head>");
		writer.println("<body><h2>Plastic Registered Applications</h2>");
		writer.println("This ACR\'s hub supports <a href='http://plastic.sourceforge.net'>plastic</a> version "+PlasticListener.CURRENT_VERSION);
		writer.println("<table border='1'>");
		writer.println("<tr><th>Icon</th><th>Name</th><th>Plastic Id</th><th>IVORN</th><th>Supported Messages</th><th>Plastic Version</th><th>Alive?</th></tr>");
		Map ivorns = request(getHubId(), CommonMessageConstants.GET_IVORN, CommonMessageConstants.EMPTY);
		Map icons = request(getHubId(), CommonMessageConstants.GET_ICON, CommonMessageConstants.EMPTY);
		Map versions = request(getHubId(), CommonMessageConstants.GET_VERSION, CommonMessageConstants.EMPTY);
		
		Iterator it = apps.iterator();
		while (it.hasNext()) {
			URI plid = (URI) it.next();
			String name = getName(plid);
			List messages = getUnderstoodMessages(plid);
			PlasticClientProxy client = clients.get(plid);
			boolean alive = client.isResponding();
			String version = (String) versions.get(plid);
			String icon = (String) icons.get(plid);
			String ivorn = (String) ivorns.get(plid);
			
			writer.println("<tr>");
				writer.println("<td><img src='"+icon+"'/></td>");
				writer.println("<td>"+name+"</td>");
				writer.println("<td>"+plid+"</td>");
				writer.println("<td>"+ivorn+"</td>");
				writer.println("<td>");
				if (CommonMessageConstants.EMPTY.equals(messages)) {
					writer.write("All");
				} else {
					writer.write("<ul>");
					Iterator mit = messages.iterator();
					while (mit.hasNext()) {
						URI msg = (URI) mit.next();
						writer.write("<li>"+msg+"</li>");
					}
					writer.write("</ul>");
				}
				writer.println("</td>");
				writer.println("<td>"+version+"</td>");
				writer.println("<td>"+(alive ? "yes":"no")+"</td>");

			writer.println("</tr>");
			
		}
		writer.println("</table></body></html>");
		writer.close();
		browser.openURL(file.toURL());
	}

}
