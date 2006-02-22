package org.astrogrid.desktop.modules.plastic;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.common.namegen.NameGen;
import org.votech.plastic.CommonMessageConstants;
import org.votech.plastic.outgoing.PlasticException;

/**
 * Delegate to the actual remote Plastic app.
 * 
 * @author jdt@roe.ac.uk
 * @date 01-Nov-2005
 */
abstract class PlasticClientProxy {
    /**
     * Logger for this class
     */
    private final Log logger = LogFactory.getLog(PlasticClientProxy.class);

    boolean responding = true;

    protected URI id;

    protected String name;

    protected List supportedMessages;

    public boolean isResponding() {
        return responding;
    }

    public URI getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List getMessages() {
        return supportedMessages;
    }

    public PlasticClientProxy(NameGen gen, String name, List supportedMessages) {
        try {
            this.id = new URI("plastic://acr/"+gen.next()); 
        } catch(URISyntaxException se) {
        	logger.error("Unable to contruct URI for Id",se);
        	throw new RuntimeException("Unable to create URI",se);
        }  catch (Exception e) {
            logger.error("Exception in unique name generator ", e);
            // we're going to use the InMemoryNameGen
            // class, so this shouldn't happen.  
        }
        this.supportedMessages = supportedMessages;
    }

    public PlasticClientProxy(NameGen gen, String name) {
        this(gen, name, CommonMessageConstants.EMPTY);
    }

    /**
     * Does this client understand this message?
     * 
     * @param message
     * @return true if yes
     */
    public boolean understands(URI message) {
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
    public abstract Object perform(URI sender, URI message, List args) throws PlasticException;

    protected void setResponding(boolean responding) {
        this.responding = responding;
    }
}