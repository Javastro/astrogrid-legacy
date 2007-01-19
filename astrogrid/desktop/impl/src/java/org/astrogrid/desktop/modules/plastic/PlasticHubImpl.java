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
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.builtin.ShutdownListener;
import org.astrogrid.acr.system.RmiServer;
import org.astrogrid.acr.system.SystemTray;
import org.astrogrid.acr.system.WebServer;
import org.astrogrid.common.namegen.NameGen;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.Preference;
import org.astrogrid.desktop.modules.system.UIInternal;
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

    private final ApplicationStore clients = new ApplicationStore();

    private final NameGen idGenerator;
    private final SystemTray tray;
    private final RmiServer rmiServer;
    private final WebServer webServer;
    private final Executor systemExecutor;
    private  URI hubId = URI.create("file://hub_not_set_up");  //A PlasticListener should be registered through registerSelf() to "be" the hub for the outside world.

    private File plasticPropertyFile;

	private PrettyPrinterInternal prettyPrinter;

	private final Preference notificationsEnabled;

	private boolean weWroteTheConfigFile;

	private Executor sequentialExecutor = new DirectExecutor();

    private final UIInternal ui;

    private final String arVersion;

	/**
	 * The keys used to identify this hub
	 * @see PlasticHubListener#PLASTIC_CONFIG_FILENAME
	 */
	public static final String ACR_VERSION = "acr.version";
	//public static final String HUB_IMPL = "acr.hub.impl";	



    /** constructor selected by acrFactory when systemtray is not available */
    public PlasticHubImpl(String arVersion, UIInternal ui,Executor executor, NameGen idGenerator,
            RmiServer rmi,  WebServer web, PrettyPrinterInternal prettyPrinter, Preference p) {
        this(arVersion, ui,executor,idGenerator,rmi, web,null, prettyPrinter, p);
    }
    
    /** constructor selected by acrFactory when systemtray is available 
     * @param prettyPrinter */
    public PlasticHubImpl(String arVersion, UIInternal ui,Executor executor, NameGen idGenerator,
            RmiServer rmi, WebServer web,SystemTray tray, PrettyPrinterInternal prettyPrinter, Preference p) {
        this.tray = tray;
        this.rmiServer= rmi;
        this.webServer= web;
        this.systemExecutor = executor;
        this.idGenerator = idGenerator;
        this.prettyPrinter = prettyPrinter;
        this.notificationsEnabled = p;
        this.ui = ui;
        this.arVersion = arVersion;
        logger.info("Constructing a PlasticHubImpl");
    }

    public List getRegisteredIds() {
        List ids = clients.getAppIds();
        return ids;  //No need to copy as the ApplicationStore will return a copy.
    }

    public URI registerXMLRPC(String name, List supportedOperations, URL callBackURL) {
        PlasticClientProxy client = new XMLRPCPlasticClient(idGenerator, name, supportedOperations, callBackURL);
        return register(client);
    }

    public URI registerRMI(String name, List supportedOperations, PlasticListener caller) {
    	return registerJava(name, supportedOperations, caller,false);
    }
    
    /**
     * Internal method used by the AstroRuntime to register itself.
     * @param name
     * @param supportedOperations
     * @param caller
     * @return
     */
    public URI registerSelf(String name, List supportedOperations, PlasticListener caller) {
        hubId = registerJava(name, supportedOperations, caller,true);
        return hubId;
    }

    /**
     * Register an application running in the same JVM.  This is not part of the public API - such applications
     * might have special privs, such as not notifying the user when they register, or not appearing in the list
     * of registered applications.
     * @param name human readable name
     * @param supportedOperations a List<URI> of arguments
     * @param caller the registering app
     * @param silent true if the user shouldn't be notified on registration
     * @return
     */
    private URI registerJava(String name, List supportedOperations, PlasticListener caller, boolean silent) {
        PlasticClientProxy client = new RMIPlasticClient(idGenerator, name, supportedOperations, caller, silent);
        return register(client);
    }

    public URI registerNoCallBack(String name) {
        PlasticClientProxy client = new DeafPlasticClient(idGenerator, name);
        return register(client);
    }

    /**
     * @deprecated this is not part of the plastic spec
     */
	public URI registerPolling(String name, List supportedMessages) {
		PlasticClientProxy client = new PollingPlasticClient(idGenerator, name, supportedMessages);
		return register(client);
	}


	/**
	 * Returns empty list if the id isn't for a polling client.
     * @deprecated this method is not in the PlasticSpec
	 */
	public List pollForMessages(URI id) {
		PlasticClientProxy client = (PlasticClientProxy) clients.get(id);
		if (client==null) return CommonMessageConstants.EMPTY;
		if (!(client instanceof PollingPlasticClient)) return CommonMessageConstants.EMPTY; //always disliked instanceof, but it's that or maintain a separate list
		List messages = ((PollingPlasticClient)client).getStoredMessages();
		((PollingPlasticClient)client).flush();
		return new ArrayList(messages); //Copy to ensure clients don't change
	}
	
	
    /**
     * Register an plastic-aware application.
     * @param client the application (proxy) to register
     * @param silent if true, don't pop up a notification or broadcast a message
     */
    private URI register(PlasticClientProxy client) {
        URI id = client.getId();

        clients.add(client);
        
        // Tell the world
        List args = new Vector();
        args.add(id.toString());
        logger.info(id + " has registered");
        

        // Only display this if it's not the hub itself that's registering
        if (!client.isSilent()) {
            requestAsynch(hubId, HubMessageConstants.APPLICATION_REGISTERED_EVENT, args);
            displayInfoMessage("Plastic", client.getName()+" has connected.");
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
        if (tray != null && notificationsEnabled.asBoolean()) {
            tray.displayInfoMessage(caption, message);
        } else {
            logger.info(caption + " : " + message);
        }

    }

    public void unregister(URI id) {
        if (id==null) return;
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
        Vector args = new Vector(); //TODO jdt why am I using vector?  A: restrictions in the current xml-rpc library
        args.add(id.toString());
        requestAsynch(hubId, HubMessageConstants.APPLICATION_UNREGISTERED_EVENT, args);
        logger.info(id + " has unregistered");

        if (!client.isSilent()) displayInfoMessage("Plastic", client.getName()+" has disconnected.");
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
        
        if (recipients==null) {
            logger.warn("Null recipients list.  By rights you should pass in an empty List, not a null if that's what you meant.");
            return new Hashtable();
        }
        
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
    	
        List clientsSupportingMessage = clients.getClientIdsSupportingMessage(message);
        Collection clientsToMessage;
        if (recipients==EVERYONE) {
        	clientsToMessage = clientsSupportingMessage; 
        } else {
        	clientsToMessage = CollectionUtils.intersection(clientsSupportingMessage, recipients);
        }
        if (hubId!=null && !sender.equals(hubId)) {
            clientsToMessage.remove(sender); //don't message the sender _unless_ we are the hub. (The AstroRuntime Tupperware class appears to the outside world to be the hub, but relies on getting messages from this class.
        }

        final CountDown gate = new CountDown(clientsToMessage.size());
        
        //
        // Local worker class to send the message, and add the result to our
        // list.
        // We use a gate to wait for all the threads to finish before exiting
        // the method.
        //
        final Map returns = new HashMap(); //Doesn't need synchronization as it's not read until all writes are done.
        	// NWW - consider using LinkedHashMap - if maintaining order of insertion is important.
            // JDT Don't think it is.  I'm tempted by http://gee.cs.oswego.edu/dl/jsr166/dist/docs/java/util/concurrent/ConcurrentHashMap.html though
            // even though I don't _think_ I need this to be synchronized.
            // TODO Rethink use of Collections.
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

                // Automatic purge of dead applications
                if (!client.isResponding()) {
                    logger.info("Client "+client.getName()+ "("+client.getId()+")"+" is not responding.  Attempting to unregister");
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

    /**
     * Just like request, but done without spawning a new thread.  This is necessary during shutdown to make sure
     * that the message gets sent before the framework shuts down.
     * @param sender
     * @param message
     * @param args
     */
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
            props.put(PlasticHubImpl.ACR_VERSION, arVersion);
            //props.put(PlasticHubImpl.HUB_IMPL, this.toString());
            File homeDir = new File(System.getProperty("user.home"));
            plasticPropertyFile = new File(homeDir, PlasticHubListener.PLASTIC_CONFIG_FILENAME);
            if (plasticPropertyFile.exists()) {
                logger.info("Plastic config file was already present");
                // See if we can guess who it is.
                // TODO decide on an official plastic.name key
                String runningHubName="Unknown application";
                Properties alreadyPresent = loadExistingDotPlastic();
                if (alreadyPresent.containsKey("uk.ac.starlink.plastic.servid")) runningHubName="Topcat";
                if (alreadyPresent.containsKey(ACR_VERSION)) runningHubName="the Astro Runtime";
                
                
                JOptionPane.showMessageDialog(ui.getComponent(),
                        "<html>It appears that a Plastic Hub is already running on your system inside "+runningHubName+
                        ".<br>We <em>will not</em> start our Plastic Hub.  If you are sure that there is no other Hub" +
                        " <br>actually running on your machine, then delete your .plastic file and restart this application.</html>" 
                        ,"Plastic Hub Already Running",JOptionPane.OK_OPTION);
                
                return;
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
			return CommonMessageConstants.RPCNULL; 
		}
		return client.getName(); 
	}

	/**
	 * Return all applications that have registered for a particular message.
	 */
	public List getMessageRegisteredIds(URI message) {
		return new ArrayList(clients.getClientIdsSupportingMessage(message)); //Copy to ensure clients don't change
	}

	public List getUnderstoodMessages(URI plid) {
		PlasticClientProxy client = (PlasticClientProxy) clients.get(plid);
		if (client==null) {
			return CommonMessageConstants.EMPTY;   
		}
		return new ArrayList(client.getMessages()); //Copy to ensure clients don't change
	}
	
    /**
     * Display registered application metadata to the user.
     */
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
			String iconUrlString = safeStringCast(icons, plid);
			String ivorn = safeStringCast(ivorns, plid);
			String description = safeStringCast(descriptions, plid);
			
            URL iconUrl;
            ImageIcon icon;
            try {
                iconUrl = new URL(iconUrlString);
                icon = IconHelper.loadIcon(iconUrl);
            } catch (MalformedURLException e) {
                //Cannot rely on the client returning a well-formed URL, or indeed any URL.
                iconUrl = null;
                icon = null; //TODO default?
            }
			PlasticApplicationDescription desc = new PlasticApplicationDescription(plid,name,description,messages,version,icon,iconUrl,ivorn);
			applicationDescriptions.add(desc);
		}
		
		prettyPrinter.show(applicationDescriptions);

	}

	private String safeStringCast(Map map, URI key) {
		Object mapo = map.get(key);
		return mapo!=null ? mapo.toString() : "";
	}
	
	public void setNotificationsEnabled(boolean enable) {
		notificationsEnabled.setValue(Boolean.toString(enable)); 
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
