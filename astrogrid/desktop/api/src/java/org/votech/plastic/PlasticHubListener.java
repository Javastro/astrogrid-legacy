package org.votech.plastic;

import java.util.Vector;


/**
 * The interface that a Plastic Hub should support. 
 * For information on what a Plastic Hub is, and why you'd want one, please see the URL below.
 * @see <a href="http://plastic.sourceforge.net/">http://plastic.sourceforge.net</a>
 * @author jdt@roe.ac.uk
 * @version 0.1
 * @service plastic.hub
 * @date 11-Oct-2005
 */
public interface PlasticHubListener  {
    /**
     * The key used to get the plastic.version out of the Plastic Hub config file.
     * @see #PLASTIC_CONFIG_FILENAME
     */
    public static final String PLASTIC_VERSION_KEY = "plastic.version";
    /**
     * The key used to get the URL of the xml-rpc server out of the Plastic Hub config file.
     * @see #PLASTIC_CONFIG_FILENAME
     */
    public static final String PLASTIC_XMLRPC_URL_KEY = "plastic.xmlrpc.url";
    /**
     * The key used to get the RMI port out of the Plastic Hub config file.
     * @see #PLASTIC_CONFIG_FILENAME
     */
    public static final String PLASTIC_RMI_PORT_KEY = "plastic.rmi.port";
    /**
     * The name of the file containing Plastic Hub config information (placed in ${user.home}).
     * This file currently duplicates the information in the standard ACR config files,
     * but is present to allow for future expansion and to be "organisation-neutral".
     * 
     */
    public static final String PLASTIC_CONFIG_FILENAME = ".plastic";
    /**
     * Get all the IDs of the currently registered applications.
     * @return see above
     * @xmlrpc returns an array
     */
    public Vector getRegisteredIds();
    /**
     * Get this hub's ID.
     * The hub "registers with itself", and this method will give you its own Id.      
     * @return see above
     */
    public String getHubId();
    /**
     * Register an application with the hub.
     * Each application that wishes to use the hub should register with it - the hub
     * may not forward messages from applications whose ID it doesn't recognise.  There
     * are different register methods dependening on how (and whether) the application
     * wishes to receive messages back from the hub.
     * 
     * @param name An optional string with a short name describing the application.  This may be pre-pended to the hub assigned ID, making it more human friendly.
     * @param supportedMessages an array of operations the application is interested in.  An empty Vector signifies "all".
     * @param callBackURL the application's internal xmlrpc server URL.  Used by the hub to send messages to the application.
     * @return a hub-assigned ID
     * @see <a href="http://plastic.sourceforge.net/">http://plastic.sourceforge.net</a>
     * @see #registerRMI(String, Vector, PlasticListener)
     * @see #registerNoCallBack(String)
     * @xmlrpc The supportedMessages parameter is an Array
     */
    public String registerXMLRPC(String name, Vector supportedMessages, String callBackURL);
    /**
     * A java-rmi version of {@link #registerXMLRPC(String, Vector, String) registerXMLRPC}
     * @param name see {@link #registerRMI(String, Vector, PlasticListener) registerRMI}
     * @param supportedMessages @see #registerXMLRPC(String, Vector, String)
     * @param caller the PlasticListener that wishes to register
     * @return @see #registerXMLRPC(String, Vector, String)
     * @xmlrpc Not available.
     * @example Suppose your application implements {@link PlasticListener PlasticListener}.  Then you register with the hub to receive ALL messages using <code>String id = hub.registerRMI("MyApp", new Vector(),this);</code>
     */
    public String registerRMI(String name, Vector supportedMessages, PlasticListener caller);
    
    /**
     * Register this application with the hub, but don't send it any messages in return.
     * This is to allow uncallable applications like scripting environments to register.
     * Note: this method is currently not part of the Plastic spec.
     * @param name @see #registerXMLRPC(String, Vector, String)
     * @return @see #registerXMLRPC(String, Vector, String)
     * @see #registerXMLRPC(String, Vector, String)
     */
    public String registerNoCallBack(String name);
    
    /**
     * Unregister the application from the hub.
     * @param id the application to unregister
     */
    public void unregister(String id);
    
    /**
     * Send a message to all registered Plastic applications.
     * @param sender the id of the originating tool.  Note that the hub is at liberty to refused to forward requests that don't come from an ID that it has registered.
     * @param message the message to send.  
     * @param args any arguments to pass with the message
     * @return a Vector of Hashtables mapping application ids to responses
     * @xmlrpc the return object is an array of structs mapping application ids to responses
     */
    public Vector request (String sender, String message, Vector args);
    
    /**
     * Send a request to listed registered Plastic apps.
     * See {@link #request(String, String, Vector) request} for details of the other parameters.
     * @param recipientIds a list of target application ids
     * @xmlrpc the list of target application ids is an Array
     */
    public Vector requestToSubset (String sender, String message, Vector args, Vector recipientIds);
    /**
     * Send a request to listed registered Plastic apps, but don't wait for a response.
     * @param recipientIds a list of target application ids
     * See {@link #request(String, String, Vector) request} for details of the other parameters.
     */
    
    public void requestToSubsetAsynch (String sender, String message, Vector args, Vector recipientIds);
    /**
     * Send a request to all registered Plastic apps, but don't wait for a response.
     * See {@link #request(String, String, Vector) request} for details of the other parameters.
     */
    public void requestAsynch (String sender, String message, Vector args);    
    
}
