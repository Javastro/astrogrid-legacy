/**
 * 
 */
package org.astrogrid.desktop.modules.plastic;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.ladypleaser.rmilite.Client;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;
import org.astrogrid.acr.builtin.ShutdownListener;
import org.astrogrid.acr.system.SystemTray;
import org.astrogrid.desktop.SplashWindow;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.votech.plastic.PlasticHubListener;
import org.votech.plastic.PlasticListener;

import uk.ac.starlink.plastic.Agent;
import uk.ac.starlink.plastic.MinimalHub;
import uk.ac.starlink.plastic.XmlRpcAgent;

/** Alternate implementaiton of the plastic hub in AR, implemented 
 * by extending the plaskit hub
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 30, 20084:34:53 PM
 */
public class PlaskitPlasticHub extends MinimalHub implements PlasticHubListenerInternal,
        ShutdownListener {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(PlaskitPlasticHub.class);
    
    private final Preference notificationsEnabled;
    private final HttpClient httpClient;
    private final SystemTray tray;
    
    private File plasticPropertyFile;

    // info used to write out the config file.
    private final int rmiPort;
    private final String xmlrpcServiceEndpoint;
    private final String arVersion;    
    
    
    /**
     * 
     * @param notificationsEnabled preference indicating that hub events should be notified to user
     * @param httpClient improved client to use when performing xmlrpc callbacks
     * @param tray component to post user notifications to
     * @throws RemoteException 
     */ 
    public PlaskitPlasticHub(
            final String arVersion
            ,final int rmiPort
            , final URL serverRoot
            ,final Preference notificationsEnabled
            ,final SystemTray tray
            , final HttpClient httpClient
            )
            throws RemoteException {
        super(null); // instantiate without writing out a connections file.
        this.arVersion = arVersion;
        this.rmiPort = rmiPort;
        this.xmlrpcServiceEndpoint =serverRoot.toString() + "xmlrpc";
        this.notificationsEnabled = notificationsEnabled;
        this.tray = tray;
        this.httpClient = httpClient;
        

    }

    /**
     * Displays an info message on the system tray, if there is one.
     * @param caption
     * @param message 
     */
    private void displayInfoMessage(final String caption, final String message) {
        if (logger.isDebugEnabled()) {
            logger.debug("displayInfoMessage(caption=" + caption + ", message="
                    + message + ") - start");
        }

        //If the config is not set, then assume we will show popups
        if (tray != null && notificationsEnabled.asBoolean()) {
            tray.displayInfoMessage(caption, message);
        } else {
            logger.info(caption + " : " + message);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("displayInfoMessage() - end");
        }
    }
    
    // overridden to use a httpclient.
    @Override
    protected Agent createXmlRpcAgent(final int id, final String name,
            final URI[] supportedMessages, final URL callbackURL) {
        if (logger.isDebugEnabled()) {
            logger.debug("createXmlRpcAgent(id=" + id + ", name=" + name
                    + ", supportedMessages=" + Arrays.toString(supportedMessages)
                    + ", callbackURL=" + callbackURL + ") - start");
        }

        final XmlRpcClient client = new XmlRpcClient();
        final XmlRpcClientConfigImpl config = (XmlRpcClientConfigImpl) client.getClientConfig();
       // final XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();        
        config.setServerURL(callbackURL);
        
//        client.setConfig(config);
        
        final XmlRpcCommonsTransportFactory factory = new XmlRpcCommonsTransportFactory(client);
        factory.setHttpClient(httpClient);
        client.setTransportFactory(factory);         

        final Agent returnAgent = new XmlRpcAgent(id, name, supportedMessages,
                callbackURL, client);
        if (logger.isDebugEnabled()) {
            logger.debug("createXmlRpcAgent() - end - return value="
                    + returnAgent);
        }
        return returnAgent ; 

    }
    /** additional key written out to connectio properties file */
    private static final String ACR_VERSION = "acr.version";
    private final static String SERVER_ID_KEY = "org.astrogrid.plastic.servid";
    private final String serverId = this.toString();
   
    // write out the connection file
    /** write out a connection file, if there's not already one present.
     * does nothing if a valid file already exists.
     * @throws IOException if unable to write a file. 
     */
    public void start() throws IOException {
        if (logger.isDebugEnabled()) {
            logger.debug("start() - start");
        }

        plasticPropertyFile = new File(System.getProperty("user.home"), PlasticHubListener.PLASTIC_CONFIG_FILENAME);
        /* Check if the config file already exists. */
        if ( plasticPropertyFile != null && plasticPropertyFile.exists() ) {
            /* Load config file. */
            final Properties props = new Properties();
            InputStream propStream = null;
            
            try {
               propStream =   new BufferedInputStream( new FileInputStream( plasticPropertyFile ) );
                props.load( propStream );
            }   catch ( final IOException e ) {
                logger.error("start()", e);

               logger.warn( "File " + plasticPropertyFile + "exists "   + "but is unreadable",e );
                  return; // bail out here.
            } finally {
                IOUtils.closeQuietly(propStream);             
            }
            final String otherRmiPort = props.getProperty( PlasticHubListener
                                               .PLASTIC_RMI_PORT_KEY );

            /* Examine it to see if it describes a living or dead hub. */
            boolean badFile = false;
            if ( otherRmiPort != null ) {
                try {
                    final Client client =  new Client( "localhost", Integer.parseInt( otherRmiPort ) );
                    client.exportInterface( PlasticListener.class );
                    client.lookup( PlasticHubListener.class );
                } catch ( final NumberFormatException e ) {
                    logger.error("start()", e);

                    badFile = true;
                } catch ( final Exception e ) {
                    if ( plasticPropertyFile.delete() ) {
                        logger.warn( "Apparently moribund " + plasticPropertyFile +
                                         " - deleting" );
                    }  else {
                        logger.error( "Tried and failed to delete apparently moribund "
                                             + plasticPropertyFile ); 
                        return ; // bail out.
                    }
                }
            }
            else {
                badFile = true;
            }

            if ( plasticPropertyFile.exists() ) {
                final String msg = badFile
                    ? "File " + plasticPropertyFile + " exists but looks wrong" +
                      " - delete it?"
                    : "Hub described at " + plasticPropertyFile + " is already running";
                logger.info(msg);
                return; // bail out
            }
        }
        SplashWindow.reportProgress("Constructing a PLASTIC Hub...");
        logger.info("Constructing a PLASTIC Hub");
        // ok, seems like there's nothing else running - so we'll write out our config file.
        storeConfig(plasticPropertyFile);

        if (logger.isDebugEnabled()) {
            logger.debug("start() - end");
        }
    }

    /**
     * Writes the relevant information about this object to a PLASTIC-format
     * properties file.
     *
     * @param  configFile  output file
     */
    private void storeConfig( final File configFile ) throws IOException {
        logger.info("Writing out plastic connection properties file");
        final Properties props = new Properties();
        props.setProperty( PlasticHubListener.PLASTIC_VERSION_KEY,
                           PlasticListener.CURRENT_VERSION );
        props.setProperty( PlasticHubListener.PLASTIC_RMI_PORT_KEY,
                           Integer.toString( rmiPort ) );
        props.setProperty( PlasticHubListener.PLASTIC_XMLRPC_URL_KEY, 
                           xmlrpcServiceEndpoint );
        props.setProperty( SERVER_ID_KEY, serverId);
        props.put(ACR_VERSION, arVersion);        
        OutputStream out =null;
        try {
            out = new FileOutputStream( configFile );
            props.store( out, "PLASTIC server: " + this.getClass().getSimpleName() +  " See http://plastic.sourceforge.net");
        }
        finally {
           IOUtils.closeQuietly(out);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("storeConfig() - end");
        }
    }
         
    public void halting() {
        if (logger.isDebugEnabled()) {
            logger.debug("halting() - start");
        }

        super.stop();
        
        if ( plasticPropertyFile != null && plasticPropertyFile.exists() ) {
            final Properties props = new Properties();
            InputStream configStream = null;
            try {
                configStream = new FileInputStream( plasticPropertyFile);
                props.load( configStream );
            } catch ( final IOException e ) {
                logger.error("halting()", e);
            } finally {
                IOUtils.closeQuietly(configStream);
            }
            if ( serverId.equals( props.getProperty( SERVER_ID_KEY ) ) ) {
                plasticPropertyFile.delete();
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("halting() - end");
        }
    }

    public String lastChance() {
        if (logger.isDebugEnabled()) {
            logger.debug("lastChance() - start");
        }

        if (logger.isDebugEnabled()) {
            logger.debug("lastChance() - end - return value=" + null);
        }
        return null; // we don't care.
    }
// loads of delegates, so I can watch what's going on.
    @Override
    public Object execute(final XmlRpcRequest arg0) throws XmlRpcException {
        if (logger.isDebugEnabled()) {
            logger.debug("execute(arg0=" + arg0 + ") - start");
        }

        final Object returnObject = super.execute(arg0);
        if (logger.isDebugEnabled()) {
            logger.debug("execute() - end - return value=" + returnObject);
        }
        return returnObject;
    }

    @Override
    public void finalize() throws Throwable {
        if (logger.isDebugEnabled()) {
            logger.debug("finalize() - start");
        }

        super.finalize();

        if (logger.isDebugEnabled()) {
            logger.debug("finalize() - end");
        }
    }

    @Override
    public URI getHubId() {
        if (logger.isDebugEnabled()) {
            logger.debug("getHubId() - start");
        }

        final URI returnURI = super.getHubId();
        if (logger.isDebugEnabled()) {
            logger.debug("getHubId() - end - return value=" + returnURI);
        }
        return returnURI;
    }

    @Override
    public List getMessageRegisteredIds(final URI message) {
        if (logger.isDebugEnabled()) {
            logger.debug("getMessageRegisteredIds(message=" + message
                    + ") - start");
        }

        final List returnList = super.getMessageRegisteredIds(message);
        if (logger.isDebugEnabled()) {
            logger.debug("getMessageRegisteredIds() - end - return value="
                    + returnList);
        }
        return returnList;
    }

    @Override
    public String getName(final URI id) {
        if (logger.isDebugEnabled()) {
            logger.debug("getName(id=" + id + ") - start");
        }

        String returnString = super.getName(id);
        // avoid nulls here - occasionally a naughty script might register with null, and this is ok,
        // apart from when the response is trasferred to the caller over xmlrpc - no way to represent null
        // and so it's getting type convertted into an object[]/
        if (returnString == null) {
            returnString = "";
        }
        if (logger.isDebugEnabled()) {
            logger.debug("getName() - end - return value=" + returnString);
        }
        return returnString;
    }

    @Override
    public List getRegisteredIds() {
        if (logger.isDebugEnabled()) {
            logger.debug("getRegisteredIds() - start");
        }

        final List returnList = super.getRegisteredIds();
        if (logger.isDebugEnabled()) {
            logger.debug("getRegisteredIds() - end - return value="
                    + returnList);
        }
        return returnList;
    }

    @Override
    public List getUnderstoodMessages(final URI id) {
        if (logger.isDebugEnabled()) {
            logger.debug("getUnderstoodMessages(id=" + id + ") - start");
        }

        final List returnList = super.getUnderstoodMessages(id);
        if (logger.isDebugEnabled()) {
            logger.debug("getUnderstoodMessages() - end - return value="
                    + returnList);
        }
        return returnList;
    }

    @Override
    public List pollForMessages(final URI id) {
        if (logger.isDebugEnabled()) {
            logger.debug("pollForMessages(id=" + id + ") - start");
        }

        final List returnList = super.pollForMessages(id);
        if (logger.isDebugEnabled()) {
            logger
                    .debug("pollForMessages() - end - return value="
                            + returnList);
        }
        return returnList;
    }

    @Override
    public URI registerNoCallBack(final String name) {
        if (logger.isDebugEnabled()) {
            logger.debug("registerNoCallBack(name=" + name + ") - start");
        }

        final URI returnURI = super.registerNoCallBack(name);
        if (logger.isDebugEnabled()) {
            logger.debug("registerNoCallBack() - end - return value="
                    + returnURI);
        }
        return returnURI;
    }

    @Override
    public URI registerPolling(final String name, final List supportedMessages) {
        if (logger.isDebugEnabled()) {
            logger.debug("registerPolling(name=" + name
                    + ", supportedMessages=" + supportedMessages + ") - start");
        }

        final URI returnURI = super.registerPolling(name, supportedMessages);
        if (logger.isDebugEnabled()) {
            logger.debug("registerPolling() - end - return value=" + returnURI);
        }
        return returnURI;
    }

    @Override
    public URI registerRMI(final String name, final List supportedMessages,
            final PlasticListener caller) {
        if (logger.isDebugEnabled()) {
            logger.debug("registerRMI(name=" + name + ", supportedMessages="
                    + supportedMessages + ", caller=" + caller + ") - start");
        }

        final URI returnURI = super.registerRMI(name, supportedMessages, caller);
        if (logger.isDebugEnabled()) {
            logger.debug("registerRMI() - end - return value=" + returnURI);
        }
        return returnURI;
    }

    @Override
    public URI registerXMLRPC(final String name, final List supportedMessages,
            final URL callbackURL) {
        if (logger.isDebugEnabled()) {
            logger.debug("registerXMLRPC(name=" + name + ", supportedMessages="
                    + supportedMessages + ", callbackURL=" + callbackURL
                    + ") - start");
        }

        final URI returnURI = super.registerXMLRPC(name, supportedMessages,
                callbackURL);
        if (logger.isDebugEnabled()) {
            logger.debug("registerXMLRPC() - end - return value=" + returnURI);
        }
        return returnURI;
    }

    @Override
    public Map request(final URI sender, final URI message, final List args) {
        if (logger.isDebugEnabled()) {
            logger.debug("request(sender=" + sender + ", message=" + message
                    + ", args=" + args + ") - start");
        }

        final Map returnMap = super.request(sender, message, args);
        if (logger.isDebugEnabled()) {
            logger.debug("request() - end - return value=" + returnMap);
        }
        return returnMap;
    }

    @Override
    public void requestAsynch(final URI sender, final URI message, final List args) {
        if (logger.isDebugEnabled()) {
            logger.debug("requestAsynch(sender=" + sender + ", message="
                    + message + ", args=" + args + ") - start");
        }

        super.requestAsynch(sender, message, args);

        if (logger.isDebugEnabled()) {
            logger.debug("requestAsynch() - end");
        }
    }

    @Override
    public Map requestToSubset(final URI sender, final URI message, final List args,
            final List recipientIds) {
        if (logger.isDebugEnabled()) {
            logger.debug("requestToSubset(sender=" + sender + ", message="
                    + message + ", args=" + args + ", recipientIds="
                    + recipientIds + ") - start");
        }

        final Map returnMap = super.requestToSubset(sender, message, args,
                recipientIds);
        if (logger.isDebugEnabled()) {
            logger.debug("requestToSubset() - end - return value=" + returnMap);
        }
        return returnMap;
    }

    @Override
    public void requestToSubsetAsynch(final URI sender, final URI message, final List args,
            final List recipientIds) {
        if (logger.isDebugEnabled()) {
            logger.debug("requestToSubsetAsynch(sender=" + sender
                    + ", message=" + message + ", args=" + args
                    + ", recipientIds=" + recipientIds + ") - start");
        }

        super.requestToSubsetAsynch(sender, message, args, recipientIds);

        if (logger.isDebugEnabled()) {
            logger.debug("requestToSubsetAsynch() - end");
        }
    }

    @Override
    public void stop() {
        if (logger.isDebugEnabled()) {
            logger.debug("stop() - start");
        }

        super.stop();

        if (logger.isDebugEnabled()) {
            logger.debug("stop() - end");
        }
    }

    @Override
    public void unregister(final URI id) {
        if (logger.isDebugEnabled()) {
            logger.debug("unregister(id=" + id + ") - start");
        }

        super.unregister(id);

        if (logger.isDebugEnabled()) {
            logger.debug("unregister() - end");
        }
    }
    


}
