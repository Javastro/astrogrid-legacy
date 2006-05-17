package org.astrogrid.desktop.modules.ui.comp;

import java.awt.Component;
import java.awt.Container;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.PlasticWrapper;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.votech.plastic.CommonMessageConstants;
import org.votech.plastic.HubMessageConstants;
import org.votech.plastic.incoming.handlers.AbstractMessageHandler;

/**
 * class that listens for new PLASTIC application registrations, and adds buttons / components to a component
 * for each suitable application
 * @author Noel Winstanley nw@jb.man.ac.uk 24-Feb-2006
 *
 */
public abstract class ApplicationRegisteredPlasticMessageHandler extends AbstractMessageHandler {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(ApplicationRegisteredPlasticMessageHandler.class);

/**
 *  Construct a new ApplicationRegisteredPlasticMessageHandler
 * @param parent parent ui window
 * @param wrapper interface to the plastic app this class is part of
 * @param container ui container to add new widgets to / remove from when applications
 * register / unregister.
 */
    public ApplicationRegisteredPlasticMessageHandler(UIComponent parent, PlasticWrapper wrapper, Container container) {
        this.wrapper = wrapper;
        this.container = container;
        this.parent = parent;
    }
    protected final PlasticWrapper wrapper;
    private final Container container;
    private final UIComponent parent;
    private final Map componentMap =MapUtils.synchronizedMap(new HashMap());

    protected List getLocalMessages() {
        return dynamicButtonMessages;
    }
   /** called to handle messages */
    public Object perform(URI sender, URI message, List args) {
        try {
        if (message.equals(HubMessageConstants.APPLICATION_REGISTERED_EVENT)) {
            interrogatePlasticApp(new URI(args.get(0).toString())); //redundant, but reliable.
        }
        if (message.equals(HubMessageConstants.APPLICATION_UNREGISTERED_EVENT)) {            
            removePlasticApp(new URI(args.get(0).toString()));
        }
        } catch (URISyntaxException e) { // don't care overmuch.
            logger.warn(e);       
        }
        return null;
        
    }
    /** users should implement this method - build the required components
     * when a new application registers
     * @param applicationId id of the plastic app
     * @param name name as returned by the plastic app
     * @param iconURL url of an icon image (may be null)
     * @param messages plastic messages this app claims to support.
     * @return array of new components 
     */
    protected abstract Component[] buildComponents(URI applicationId,String name,String description, URL iconURL, URI[] messages) ; 
    
	private final static List dynamicButtonMessages = new ArrayList() {
	    {// init
	        this.add(HubMessageConstants.APPLICATION_REGISTERED_EVENT);
	        this.add(HubMessageConstants.APPLICATION_UNREGISTERED_EVENT);
	    }
	};

/**
 * inspect a plastic application, and create a button for it if it's suitable
 * @param id plastic id for the application in inspect.
 */
public void interrogatePlasticApp(final URI id) {
    (new BackgroundWorker(parent,"Inspecting Plastic Application " + id) {                     
        protected Object construct() throws Exception {
            List noArgs = new ArrayList();
            String name = (String)singleTargetPlasticMessage(CommonMessageConstants.GET_NAME,noArgs,id);
            String description = (String)singleTargetPlasticMessage(CommonMessageConstants.GET_DESCRIPTION,noArgs,id);
            URL iconURL;
            try {
                Object o = singleTargetPlasticMessage(CommonMessageConstants.GET_ICON,noArgs,id);
                if (o == null) {
                    iconURL = null;
                } else {
                    iconURL = new URL(o.toString());
                }
            } catch (MalformedURLException e) {
                iconURL = null;
            } 
            List appMsgList = wrapper.getHub().getUnderstoodMessages(id);
            URI[] appMessages = (URI[])appMsgList.toArray(new URI[appMsgList.size()]);
            return buildComponents(id,name,description, iconURL,appMessages);
      
        }
                
        protected void doFinished(Object result) {
            // add the freshly created button to the UI
            Component[] components = (Component[])result;
            if (components == null || components.length == 0) {
                return;
            }
            for (int i = 0; i < components.length; i++) {
                container.add(components[i]);
            }
            // record what components pertain to this application.
            componentMap.put(id,components);            
        }
    }).start();
}


/** remove components associated with a plastic application from the container.
 * 
 * @param id plastic id of the application to remove 
 */
public void removePlasticApp(URI id) {
    if (! componentMap.containsKey(id)) {
        return;
    }
    final Component[] components = (Component[]) componentMap.get(id);
    if (components == null || components.length ==0) {
        return;
    }
     SwingUtilities.invokeLater(new Runnable() {
        public void run() {           
            for (int i = 0; i < components.length; i++) {
                container.remove(components[i]);
            }
            container.repaint();
        }
     });
}


/** convenience method - calls a single application */
protected Object singleTargetPlasticMessage(URI message, List args, URI target) {
    List targetSet = new ArrayList();
    targetSet.add(target);
    Map m = wrapper.getHub().requestToSubset(wrapper.getPlasticId(), message, args, targetSet);
    if (m == null || ! m.containsKey(target)) {
        return null;
    } else {
        return m.get(target);
    }
}


}