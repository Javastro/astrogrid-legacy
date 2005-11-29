package org.astrogrid.desktop.modules.plastic;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import net.ladypleaser.rmilite.RemoteInvocationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlrpc.XmlRpcClient;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.Finder;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.builtin.ACR;
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
import org.votech.plastic.util.ImmutableVector;

import EDU.oswego.cs.dl.util.concurrent.CountDown;
import EDU.oswego.cs.dl.util.concurrent.Executor;

public class PlasticHubImpl implements PlasticHubListener, PlasticHubListenerInternal, Startable {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(PlasticHubImpl.class);

    /**
     * Delegate to the actual remote Plastic app.
     * 
     * @author jdt@roe.ac.uk
     * @date 01-Nov-2005
     */
    private abstract class PlasticClientProxy {
        /**
         * Logger for this class
         */
        private final Log logger = LogFactory.getLog(PlasticClientProxy.class);

        private boolean responding = true;

        protected String id;

        protected String name;

        protected Vector supportedMessages;

        public boolean isResponding() {
            return responding;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public Vector getMessages() {
            return supportedMessages;
        }

        public PlasticClientProxy(String name, Vector supportedMessages) {
            try {
                this.id = name + "(" + idGenerator.next() + ")";
            } catch (Exception e) {
                logger.error("Exception in unique name generator ", e);
                // we're going to use the InMemoryNameGen
                // class, so this can't happen.
            }
            this.supportedMessages = supportedMessages;
        }

        public PlasticClientProxy(String name) {
            this(name, CommonMessageConstants.EMPTY_VECTOR);
        }

        /**
         * Does this client understand this message?
         * 
         * @param message
         * @return true if yes
         */
        public boolean understands(String message) {
            if (supportedMessages.size() == 0 || supportedMessages.contains(message))
                return true;
            return false;
        }

        /**
         * See PlasticListerner.perform()
         * 
         * @param message
         * @param args
         * @return
         * @throws PlasticException
         */
        public abstract Object perform(String sender, String message, Vector args) throws PlasticException;

        protected void setResponding(boolean responding) {
            this.responding = responding;
        }
    }

    private class RMIPlasticClient extends PlasticClientProxy {
        /**
         * Logger for this class
         */
        private final Log logger = LogFactory.getLog(RMIPlasticClient.class);

        private PlasticListener remoteClient;

        public RMIPlasticClient(String name, Vector supportedMessages, PlasticListener plastic) {
            super(name, supportedMessages);
            logger.debug("Ctor: RMIPlasticClient supports messages: " + supportedMessages);
            this.remoteClient = plastic;
        }

        public Object perform(String sender, String message, Vector args) throws PlasticException {
            try {
                Object response = remoteClient.perform(sender, message, args);
                setResponding(true);
                return response;
            } catch (RemoteInvocationException e) {
                setResponding(false);
                throw new PlasticException(e);
            }
        }

    }

    private class XMLRPCPlasticClient extends PlasticClientProxy {
        /**
         * 
         */
        private static final String PLASTIC_CLIENT_PERFORM = "plastic.client.perform";

        /**
         * Logger for this class
         */
        private final Log logger = LogFactory.getLog(XMLRPCPlasticClient.class);

        private XmlRpcClient xmlrpc;

        private boolean validURL;

        public XMLRPCPlasticClient(String name, Vector supportedMessages, String callbackURL) {
            super(name, supportedMessages);
            logger.info("Ctor: XMLRPCPlasticClient with callBackURL " + callbackURL + " and supports messages: "
                    + supportedMessages);
            try {
                xmlrpc = new XmlRpcClient(callbackURL);
                validURL = true;
            } catch (MalformedURLException e) {
                logger.warn("Attempt to register with invalid callback URL");
                setResponding(false);
            }
        }

        public Object perform(String sender, String message, Vector args) throws PlasticException {
            if (!validURL)
                throw new PlasticException("Cannot send message to " + getId() + " due to invalid callback URL");
            logger.info("Performing " + message + " from " + sender);
            try {
                Vector xmlrpcArgs = new Vector();
                xmlrpcArgs.add(sender);
                xmlrpcArgs.add(message);
                xmlrpcArgs.add(args);
                setResponding(true);
                return xmlrpc.execute(PLASTIC_CLIENT_PERFORM, xmlrpcArgs);
            } catch (Exception e) {
                setResponding(false);
                logger.warn("Got " + e + " trying to send message to " + getId());
                throw new PlasticException(e);
            }
        }

    }

    /**
     * Represents a client such as a scripting environment that can't be called back.
     * 
     * @author jdt@roe.ac.uk
     * @date 18-Nov-2005
     */
    private class DeafPlasticClient extends PlasticClientProxy {

        /**
         * A proxy for a client that can't receive callbacks.
         * 
         * @param name
         */
        public DeafPlasticClient(String name) {
            super(name);
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.astrogrid.desktop.modules.plastic.PlasticHubImpl.PlasticClientProxy#perform(java.lang.String,
         *      java.lang.String, java.util.Vector)
         */
        public Object perform(String sender, String message, Vector args) {
            // do nothing
            return CommonMessageConstants.RPCNULL;
        }

    }

    private final Map clients = new HashMap();

    private final  NameGen idGenerator;

    private final SystemTray tray;
    
    private final RmiServer rmiServer;
    private final WebServer webServer;
    
    private final Vector EMPTYVECTOR = new ImmutableVector(); 

    private final Executor executor;

    private final String hubId;

    private File plasticPropertyFile;

    /** constrcutor selected by pico when systemtray is not available */
    public PlasticHubImpl(Executor executor, NameGen idGenerator,
            MessengerInternal app, RmiServer rmi, WebServer web) {
        this(executor,idGenerator,app,rmi,web,null);
    }
    
    /** constructor selected by pico when systemtray is available */
    public PlasticHubImpl(Executor executor, NameGen idGenerator,
            MessengerInternal app,RmiServer rmi,WebServer web,SystemTray tray) {
        this.tray = tray;
        this.rmiServer= rmi;
        this.webServer= web;
        this.executor = executor;
        this.idGenerator = idGenerator;
        logger.info("Constructing a PlasticHubImpl");
        hubId = app.registerWith(this); // todo Not sure about this. Need
        // PlasticHub and HubApplication to hold
        // refs to each other...is this a code
        // smell?
        // NWW - not too much - makes them kind of like co-routhines. not ideal - but maybe better than having one monster class.
    }

    public Vector getRegisteredIds() {
        Set ids = clients.keySet();
        return new Vector(ids);
    }

    public String registerXMLRPC(String name, Vector supportedOperations, String callBackURL) {
        PlasticClientProxy client = new XMLRPCPlasticClient(name, supportedOperations, callBackURL);
        return register(client);
    }

    public String registerRMI(String name, Vector supportedOperations, PlasticListener caller) {
        PlasticClientProxy client = new RMIPlasticClient(name, supportedOperations, caller);
        return register(client);
    }

    public String registerNoCallBack(String name) {
        PlasticClientProxy client = new DeafPlasticClient(name);
        return register(client);
    }

    // todo - think about synch issues
    private synchronized String register(PlasticClientProxy client) {
        String id = client.getId();

        clients.put(id, client);
        Vector args = new Vector();
        args.add(id);
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
     * JOHN - systray won't be available when developing, or when running on Mac, or some other more esoteric arch
     * @param caption
     * @param message todo refactor this
     */
    private void displayInfoMessage(String caption, String message) {
        if (tray != null) {
            tray.displayInfoMessage(caption, message);
        } else {
            logger.info(caption + " : " + message);
        }

    }

    public void unregister(String id) {
        if (id.equals(hubId))
            // JOHN - sure you want to throw a runtime here? bettr to just log and ignore is some client is playing sillybuggers?
            throw new RuntimeException("Cannot unregister the hub itself");
        clients.remove(id);
        Vector args = new Vector();
        args.add(id);
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
     * @param recipients if non-null, then only multiplex to these recipients, otherwise send to all
     * @param shouldWaitForResults
     */
    private Vector send(final String sender, final String message, final Vector args, Vector recipients,
            boolean shouldWaitForResults) {
        final List returns = Collections.synchronizedList(new ArrayList());
        Iterator it = clients.keySet().iterator();
        List clientsToMessage = new ArrayList();
        // We need to get the number of clients to wait for first
        while (it.hasNext()) {
            PlasticClientProxy client = (PlasticClientProxy) clients.get(it.next());
            if (client.getId().equals(sender))
                continue;
            if (!client.understands(message))
                continue;
            if (recipients == null || recipients.contains(client.getId())) {
                clientsToMessage.add(client);
            }
        }

        final CountDown gate = new CountDown(clientsToMessage.size());

        //
        // Local worker class to send the message, and add the result to our
        // list.
        // We use a gate to wait for all the threads to finish before exiting
        // the method.
        //
        class Messager implements Runnable {
            /**
             * Logger for this class
             */
            private final Log logger = LogFactory.getLog(Messager.class);

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
                    Hashtable result = new Hashtable();
                    // A return value really shouldn't be null, as xml-rpc
                    // doesn't
                    // support it...but insulate ourselves in case java-rmi
                    // clients
                    // return null by mistake.
                    if (rv == null) {
                        rv = CommonMessageConstants.RPCNULL;
                    }
                    result.put(client.getId(), rv);
                    returns.add(result);
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

        it = clientsToMessage.iterator();
        while (it.hasNext()) {
            PlasticClientProxy currentClient = (PlasticClientProxy) it.next();
            try {
                executor.execute(new Messager(currentClient));
            } catch (InterruptedException e) {
                // LOW what happens now?
                //NWW - ignore - shit happens. will only get interrupted if some other thread has called interrupt on this thread in the meantime - which isn't going to happen.
                logger.warn("Interrupted executing client "
                        + currentClient.getId(), e);
            }
        }

        if (shouldWaitForResults) {
            try {
                gate.acquire();
            } catch (InterruptedException e) {
                // LOW what happens now?
                logger.warn("Interrupted aquiring gate ", e);
            }
        }
        return new Vector(returns);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.votech.plastic.PlasticHub#send(java.lang.String, java.lang.String, java.util.Vector)
     */
    public Vector request(String sender, String message, Vector args) {
        return send(sender, message, args, null, true);
    }

    public Vector requestToSubset(String sender, String message, Vector args, Vector recipientIds) {
        return send(sender, message, args, recipientIds, true);
    }

    public void requestToSubsetAsynch(String sender, String message, Vector args, Vector recipientIds) {
        send(sender, message, args, recipientIds, false);
    }

    public void requestAsynch(String sender, String message, Vector args) {
        send(sender, message, args, null, false);
    }

    public String getHubId() {
        return hubId;
    }

    public void start() {
        // Write out connection properties in a non-astrogriddy way
        // see the Plastic spec http://plastic.sourceforge.net and
        // discussions on the DS6 forum about debranding.
        // todo To allow concurrent use of the ACR and alterative
        // platic hubs, we'll need a mechanism to disable PLASTIC
        // and prevent this file being written.
        try {
            // JOHN - not to keen on creating finders internally. if you need to dynamically lookup services, 
            // get pico to pass you in an ACRImpl. in this case, the dependencies are known - so just ask for them in the constrctor.
            /*
            ACR acr = new Finder().find();
            RmiServer rmiServer = (RmiServer) acr.getService(RmiServer.class);
            WebServer webServer = (WebServer) acr.getService(WebServer.class);
            */
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
            /*
        } catch (ACRException e) {
            logger
                    .error(
                            "Unable to obtain RmiServer and WebServer to read connection properties for .plastic",
                            e);
                            */
        } catch (IOException e) {
            logger.error("There was a problem creating the Plastic config file .plastic");
        }
    }

    public void stop() {
        requestAsynch(hubId, HubMessageConstants.HUB_STOPPING_EVENT, CommonMessageConstants.EMPTY_VECTOR);
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
    public Vector purgeUnresponsiveApps() {
        Vector nonResponders = getNonResponders();
        Set deadIds = new HashSet(nonResponders);
        Iterator it = deadIds.iterator();
        while (it.hasNext()) {
            String id = (String) it.next();
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
    public Vector markUnresponsiveApps() {
        // ping them. Applications will automcatically mark themselves as duff.
        Vector args = new Vector();
        args.add("ping");
        request(hubId, CommonMessageConstants.ECHO, args);
        return getNonResponders();
    }

    private Vector getNonResponders() {
        Vector dead = new Vector();
        Set appsIds = clients.keySet();
        Iterator it = appsIds.iterator();
        while (it.hasNext()) {
            String proxyId = (String) it.next();
            PlasticClientProxy proxy = (PlasticClientProxy) clients.get(proxyId);
            if (!proxy.isResponding()) {
                String id = proxy.getId();
                logger.debug("Application " + id + " is marked as dead.");
                dead.add(id);
            }
        }
        return dead;
    }
}
