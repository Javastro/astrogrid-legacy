/*
 * 
 */
package org.astrogrid.desktop.modules.plastic;

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    private static final String NAME = "ACR-Plastic-Hub";

    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(MessengerImpl.class);

    private MessageHandler handler;

    private URI id;

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
    public Object perform(URI sender, URI message, List args) {
        return handler.handle(sender, message, args);
    }

    /**
     * @return
     */
    public List getSupportedMessages() {
        return handler.getHandledMessages();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.astrogrid.acr.plastic.HubApplication#getHubPlasticVersion()
     */
    public String getHubPlasticVersion() {
        return (String) handler.handle(this.id,
                CommonMessageConstants.GET_VERSION,
                CommonMessageConstants.EMPTY);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.astrogrid.acr.plastic.HubApplication#getHubName()
     */
    public String getHubName() {
        return (String) handler.handle(this.id,
                CommonMessageConstants.GET_NAME,
                CommonMessageConstants.EMPTY);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.astrogrid.acr.plastic.HubApplication#getHubIvorn()
     */
    public String getHubIvorn() {
        return (String) handler.handle(this.id,
                CommonMessageConstants.GET_IVORN,
                CommonMessageConstants.EMPTY);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.astrogrid.acr.plastic.HubApplication#getPlasticVersions()
     */
    public List getPlasticVersions() {
        return prettify(hub.request(this.id,
                CommonMessageConstants.GET_VERSION,
                CommonMessageConstants.EMPTY));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.astrogrid.acr.plastic.HubApplication#getNames()
     */
    public List getNames() {
        Map results = hub.request(this.id, CommonMessageConstants.GET_NAME,
                CommonMessageConstants.EMPTY);
        return prettify(results);
    }

    /**
     * Takes a Vector of Hashtables and returns an array of Strings of key: value
     * 
     * @param results see above
     * @return see above
     */
    private List prettify(Map results) {
        List prettified = new ArrayList();
        Set keySet = results.keySet();
        Iterator it = keySet.iterator();
        while (it.hasNext()) {
            Object key = it.next();
            Object value = results.get(key);
            prettified.add(key + ": " + value);
        }
        return prettified;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.astrogrid.acr.plastic.HubApplication#getIvorns()
     */
    public List getIvorns() {
        return prettify(hub.request(this.id, CommonMessageConstants.GET_IVORN,
                CommonMessageConstants.EMPTY));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.astrogrid.acr.plastic.HubApplication#echo(java.lang.String)
     */
    public List echo(String message) {
        List args = new ArrayList();
        args.add(message);
        return prettify(hub.request(this.id, CommonMessageConstants.ECHO, args));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.astrogrid.acr.plastic.HubApplication#sendMessage(java.lang.String)
     */
    public List sendNoArgMessage(URI message) {
        return sendMessage(message, CommonMessageConstants.EMPTY);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.astrogrid.desktop.modules.plastic.HubApplication#registerWith(org.votech.plastic.PlasticHub)
     */
    public URI registerWith(PlasticHubListener hub) {
        this.hub = hub;
        this.id = hub.registerRMI(NAME, this.getSupportedMessages(), this);
        return id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.astrogrid.desktop.modules.plastic.MessengerInternal#sendMessage(java.lang.String, java.util.Vector)
     */
    public List sendMessage(URI message, List args) {
        return prettify(hub.request(this.id, message, args));
    }

}
