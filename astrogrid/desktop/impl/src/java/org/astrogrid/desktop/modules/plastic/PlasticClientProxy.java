package org.astrogrid.desktop.modules.plastic;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.common.namegen.NameGen;
import org.votech.plastic.outgoing.PlasticException;

/**
 * Delegate to the actual remote Plastic app.
 * 
 * @author jdt@roe.ac.uk
 * @date 01-Nov-2005
 */
abstract class PlasticClientProxy {
    public String toString() {
        return getClass().getName()+":"+name+"("+id+")";   
     }
    /**
     * Logger for this class
     */
    private final Log logger = LogFactory.getLog(PlasticClientProxy.class);

    boolean responding = true;

    protected URI id;

    protected String name;

    protected List supportedMessages;

    private boolean registerSilently;

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
        this(gen, name, supportedMessages, false);
    }
    
    /**
     * Ctor
     * @param gen
     * @param name
     * @param understoodMessages
     * @param registerSilently If true, then no notification is sent to the user, or other apps, that an app has registered.
     */
    public PlasticClientProxy(NameGen gen, String name, List understoodMessages, boolean registerSilently) {
        try {
            this.id = URI.create("plastic://acr/"+gen.next()); //TODO something more meaningful?
        }  catch (Exception e) {
            logger.error("Exception in unique name generator ", e);
            // we're going to use the InMemoryNameGen
            // class, so this shouldn't happen.  
        }
        if (understoodMessages==null) {
            logger.warn("Attempt to register with a null list of understood messages - assuming you meant zero understood.  If that's the case, consider registering with registerNoCallback instead");
            understoodMessages = new Vector();
        }
    	//Gotcha.  The supportedMessages are in a List of URIs from Java, but a List of Strings from xml-rpc
        //Hopefully will go away in java1.5 when we have generics and can type the lists
    	if (understoodMessages.size()!=0 && understoodMessages.get(0).getClass()==String.class) {
    		this.supportedMessages = new Vector();
    		for (Iterator it = understoodMessages.iterator();it.hasNext();) {
    			String m = (String) it.next();
    			try {
					this.supportedMessages.add(new URI(m));
				} catch (URISyntaxException e) {
					logger.warn("Message "+m+" was not a valid URI, so is not being added to the list of supported messages for "+name);
				}
    		}
    		//Now we can carry on with our List of URIs....
    	} else {       
    		this.supportedMessages = new Vector(understoodMessages); //take a copy in case someone pulls the list from under our feet.
    	}
        this.name = name;
        this.registerSilently = registerSilently;
    }

/*    public PlasticClientProxy(NameGen gen, String name) {
        this(gen, name, CommonMessageConstants.EMPTY);
    }
*/
    /**
     * Does this client understand this message?
     * 
     * @param message
     * @return true if yes
     */
    public boolean understands(URI message) {
        return (supportedMessages.contains(message));
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

    /**
     * Return true if you're the sort of client that answers messages, e.g. rmi, and false
     * if you're not, e.g. Polling.
     * @return
     */
	public abstract boolean canRespond();

    public boolean isSilent() {
        return registerSilently;
    }
}