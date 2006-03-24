/*
 * 
 */
package org.astrogrid.desktop.modules.plastic;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
	public static final String DESCRIPTION = "This is the ACR Plastic Hub.\nThe ACR (Astro Client Runtime) provides the reference implementation of a Plastic Hub, together with easy access to numerous AstroGrid and VO Services.";

	public static final String IVORN = "ivo://org.astrogrid/acr/fixme"; //TODO this should be determined properly

	public static final String NAME = "ACR-Plastic-Hub";

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
        handler.setNextHandler(new StandardHandler(NAME, DESCRIPTION,
        		IVORN, null, 
                PlasticListener.CURRENT_VERSION)); //TODO ACR logo?

        
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.votech.plastic.PlasticListener#receive(java.lang.String, java.lang.String, java.util.Vector)
     */
    public Object perform(URI sender, URI message, List args) {
        return handler.perform(sender, message, args);
    }

    /**
     * @return
     */
    public Collection getSupportedMessages() {
        return handler.getHandledMessages();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.astrogrid.acr.plastic.HubApplication#getHubPlasticVersion()
     */
    public String getHubPlasticVersion() {
        return  handler.perform(this.id,
                CommonMessageConstants.GET_VERSION,
                CommonMessageConstants.EMPTY).toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.astrogrid.acr.plastic.HubApplication#getHubName()
     */
    public String getHubName() {
        return handler.perform(this.id,
                CommonMessageConstants.GET_NAME,
                CommonMessageConstants.EMPTY).toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.astrogrid.acr.plastic.HubApplication#getHubIvorn()
     */
    public String getHubIvorn() {
        return handler.perform(this.id,
                CommonMessageConstants.GET_IVORN,
                CommonMessageConstants.EMPTY).toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.astrogrid.acr.plastic.HubApplication#getPlasticVersions()
     */
    public Map getPlasticVersions() {
        return hub.request(this.id,
        CommonMessageConstants.GET_VERSION,
        CommonMessageConstants.EMPTY);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.astrogrid.acr.plastic.HubApplication#getNames()
     */
    public Map getNames() {
        Map results = hub.request(this.id, CommonMessageConstants.GET_NAME,
                CommonMessageConstants.EMPTY);
        return results;
    }
    
    public Map getIconURLs() {
        Map results = hub.request(this.id, CommonMessageConstants.GET_ICON,
                CommonMessageConstants.EMPTY);
        return results;
    }
    

    /*
     * (non-Javadoc)
     * 
     * @see org.astrogrid.acr.plastic.HubApplication#getIvorns()
     */
    public Map getIvorns() {
        return hub.request(this.id, CommonMessageConstants.GET_IVORN,
        CommonMessageConstants.EMPTY);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.astrogrid.acr.plastic.HubApplication#echo(java.lang.String)
     */
    public Map echo(String message) {
        List args = new ArrayList();
        args.add(message);
        return hub.request(this.id, CommonMessageConstants.ECHO, args);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.astrogrid.acr.plastic.HubApplication#sendMessage(java.lang.String)
     */
    public Map sendNoArgMessage(URI message) {
        return sendMessage(message, CommonMessageConstants.EMPTY);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.astrogrid.desktop.modules.plastic.HubApplication#registerWith(org.votech.plastic.PlasticHub)
     */
    public URI registerWith(PlasticHubListener hub) {
        this.hub = hub;
        this.id = hub.registerRMI(NAME, new ArrayList(this.getSupportedMessages()), this);
        logger.info("Hub self-registered with ID " + id);
        return id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.astrogrid.desktop.modules.plastic.MessengerInternal#sendMessage(java.lang.String, java.util.Vector)
     */
    public Map sendMessage(URI message, List args) {
        return hub.request(this.id, message, args);
    }

}
