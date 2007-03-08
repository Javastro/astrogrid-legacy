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
import java.util.Vector;

import javax.swing.SwingUtilities;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.desktop.hivemind.IterableObjectBuilder;
import org.astrogrid.desktop.modules.system.HelpServerInternal;
import org.astrogrid.desktop.modules.system.UIInternal;
import org.votech.plastic.incoming.handlers.AbstractMessageHandler;

/** Factory for voexplorers - also handles some messages.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 6, 20076:44:32 PM
 */
public class VOExplorerFactoryImpl  extends AbstractMessageHandler implements VOExplorerFactoryInternal {
	
	public final Configuration conf;
	public final HelpServerInternal help;
	public final UIInternal ui; // @todo change this later.
	public final IterableObjectBuilder views;
	
	
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(VOExplorerFactoryImpl.class);


	// create a new voexplorer
	public Object create() {
		
		VOExplorerImpl vo = new VOExplorerImpl(help,ui,conf,views);
		vo.setVisible(true);
		return vo;
	}


	
	public static final List REGISTRY_MESSAGES = new ArrayList() {{
		try {
			add(new URI("ivo://votech.org/voresource/load"));
			add(new URI("ivo://votech.org/voresource/loadList"));
		} catch (Exception e) {
			logger.error("Failed to define plastic messages for voexplorer",e);
		}
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
	public VOExplorerFactoryImpl(final Configuration conf, final HelpServerInternal help, final UIInternal ui, final IterableObjectBuilder views) {
		super();
		this.conf = conf;
		this.help = help;
		this.ui = ui;
		this.views = views;
		create();
	}

}
