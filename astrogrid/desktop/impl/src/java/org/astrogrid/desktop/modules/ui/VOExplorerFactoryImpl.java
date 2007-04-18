/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.SwingUtilities;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.desktop.hivemind.IterableObjectBuilder;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.system.ui.UIContributionBuilder;
import org.votech.plastic.incoming.handlers.AbstractMessageHandler;

/** Factory for voexplorers - also handles some messages.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 6, 20076:44:32 PM
 */
public class VOExplorerFactoryImpl  extends AbstractMessageHandler implements VOExplorerFactoryInternal {
	
	public final UIContext context;
	public final UIComponent ui;
	public final IterableObjectBuilder views;
	public final UIContributionBuilder builder;
	public final IterableObjectBuilder activities;
	
	
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(VOExplorerFactoryImpl.class);


	// create a new voexplorer
	public Object create() {
		VOExplorerImpl vo = new VOExplorerImpl(context,views,activities, builder);
		vo.setVisible(true);
		return vo;
	}


	
	public static final List REGISTRY_MESSAGES = new ArrayList() {{
			add(URI.create("ivo://votech.org/voresource/load"));
			add(URI.create("ivo://votech.org/voresource/loadList"));
	}};
	
	public static final URI VORESOURCE_LOAD = (URI)REGISTRY_MESSAGES.get(0);
	public static final URI VORESOURCE_LOADLIST= (URI)REGISTRY_MESSAGES.get(1);
	
	protected List getLocalMessages() {
		return REGISTRY_MESSAGES;
	}
	// handles both kinds of message - quite tolerant of different object types.
	// however, at the moment will choke and fail on the first malformed uri.
	public Object perform(URI sender, URI message, List args) {
		if (REGISTRY_MESSAGES.contains(message) && args.size() > 0) {
			try { //handle a string, collection, or array...
				final List resList = new ArrayList();
				Object o = args.get(0);
				if (o == null) {
					logger.warn("Null argument");
					return Boolean.FALSE;
				}
				if (o instanceof Collection) {
					Collection c= (Collection)o;
					for (Iterator i = c.iterator(); i.hasNext();) {
						Object e =  i.next();
						if (e != null) {
							resList.add(new URI(e.toString()));
						}
					}
				} else if (o.getClass().isArray()) {
					Object[] arr = (Object[])o;
					for (int i = 0; i < arr.length; i++) {
						if (arr[i] != null) {
							resList.add(new URI(arr[i].toString()));
						}
					}
				} else { // treat it as a single string
					URI resourceId = new URI(o.toString());
					resList.add(resourceId);
				}
				// got all the info we need. display the ui on the EDT.
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						VOExplorerImpl ve = (VOExplorerImpl) create();
						ve.displayResources(resList);
					}
				});				
				return Boolean.TRUE;
			} catch (URISyntaxException x) {
				logger.error("URISyntaxException",x);
				return Boolean.FALSE;
			}
		} else {
			// let other messages pass-thru to the next handler in the list.
			if (nextHandler != null) {
				return nextHandler.perform(sender,message,args);
			} else {
				return null;
			}
		}
	}
	public VOExplorerFactoryImpl(UIContext context,final IterableObjectBuilder views, final IterableObjectBuilder activitiesBuilder,UIContributionBuilder builder) {
		super();
		this.context = context;
		this.views = views;
		this.builder = builder;
		this.activities = activitiesBuilder;
		this.ui = null; //@todo work out what to do here.. - sometimes will want to provide a parent (maybe never throught eh factory though??)
		create();
	}

}
