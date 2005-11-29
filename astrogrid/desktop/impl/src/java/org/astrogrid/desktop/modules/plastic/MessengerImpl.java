/*
 * 
 */
package org.astrogrid.desktop.modules.plastic;

import java.util.Hashtable;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.votech.plastic.CommonMessageConstants;
import org.votech.plastic.PlasticHubListener;
import org.votech.plastic.PlasticListener;
import org.votech.plastic.incoming.handlers.LoggingHandler;
import org.votech.plastic.incoming.handlers.MessageHandler;
import org.votech.plastic.incoming.handlers.StandardHandler;

/**
 * @author jdt@roe.ac.uk
 * @date 02-Nov-2005
 */
public class MessengerImpl implements MessengerInternal, PlasticListener {
    private static final String IVORN = "ivo://org.astrogrid/acr";

    private static final String NAME = "ACR Plastic Hub";

    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(MessengerImpl.class);

    private MessageHandler handler;

    private String id = "undefined";

    private PlasticHubListener hub;

    public MessengerImpl() {
        logger.trace("Constructing HubApplicationImpl");
        handler = new LoggingHandler(logger);
        handler.setNextHandler(new StandardHandler(NAME, IVORN,
                PlasticListener.CURRENT_VERSION));

        logger.info("Hub self-registered with ID " + id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.votech.plastic.PlasticListener#receive(java.lang.String, java.lang.String, java.util.Vector)
     */
    public Object perform(String sender, String message, Vector args) {
        return handler.perform(sender, message, args);
    }

    /**
     * @return
     */
    public Vector getSupportedMessages() {
        return handler.getHandledMessages();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.astrogrid.acr.plastic.HubApplication#getHubPlasticVersion()
     */
    public String getHubPlasticVersion() {
        return (String) handler.perform(this.id,
                CommonMessageConstants.GET_VERSION,
                CommonMessageConstants.EMPTY_VECTOR);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.astrogrid.acr.plastic.HubApplication#getHubName()
     */
    public String getHubName() {
        return (String) handler.perform(this.id,
                CommonMessageConstants.GET_NAME,
                CommonMessageConstants.EMPTY_VECTOR);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.astrogrid.acr.plastic.HubApplication#getHubIvorn()
     */
    public String getHubIvorn() {
        return (String) handler.perform(this.id,
                CommonMessageConstants.GET_IVORN,
                CommonMessageConstants.EMPTY_VECTOR);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.astrogrid.acr.plastic.HubApplication#getPlasticVersions()
     */
    public String[] getPlasticVersions() {
        return prettify(hub.request(this.id,
                CommonMessageConstants.GET_VERSION,
                CommonMessageConstants.EMPTY_VECTOR));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.astrogrid.acr.plastic.HubApplication#getNames()
     */
    public String[] getNames() {
        Vector results = hub.request(this.id, CommonMessageConstants.GET_NAME,
                CommonMessageConstants.EMPTY_VECTOR);
        return prettify(results);
    }

    /**
     * Takes a Vector of Hashtables and returns an array of Strings of key: value
     * 
     * @param results see above
     * @return see above
     */
    private String[] prettify(Vector results) {
        String[] prettified = new String[results.size()];
        for (int i = 0; i < prettified.length; ++i) {
            //JOHN - this assumes that each hash map only ever has one key - is this always true?? ifso, why the hash?
            Hashtable result = (Hashtable) results.get(i);
            Object key = result.keys().nextElement();
            Object value = result.get(key);
            prettified[i] = key + ": " + value;
        }
        return prettified;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.astrogrid.acr.plastic.HubApplication#getIvorns()
     */
    public String[] getIvorns() {
        return prettify(hub.request(this.id, CommonMessageConstants.GET_IVORN,
                CommonMessageConstants.EMPTY_VECTOR));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.astrogrid.acr.plastic.HubApplication#echo(java.lang.String)
     */
    public String[] echo(String message) {
        Vector args = new Vector();
        args.add(message);
        return prettify(hub.request(this.id, CommonMessageConstants.ECHO, args));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.astrogrid.acr.plastic.HubApplication#sendMessage(java.lang.String)
     */
    public Object[] sendNoArgMessage(String message) {
        return sendMessage(message, CommonMessageConstants.EMPTY_VECTOR);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.astrogrid.desktop.modules.plastic.HubApplication#registerWith(org.votech.plastic.PlasticHub)
     */
    public String registerWith(PlasticHubListener hub) {
        this.hub = hub;
        this.id = hub.registerRMI(NAME, this.getSupportedMessages(), this);
        return id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.astrogrid.desktop.modules.plastic.MessengerInternal#sendMessage(java.lang.String, java.util.Vector)
     */
    public Object[] sendMessage(String message, Vector args) {
        return prettify(hub.request(this.id, message, args));
    }

}
