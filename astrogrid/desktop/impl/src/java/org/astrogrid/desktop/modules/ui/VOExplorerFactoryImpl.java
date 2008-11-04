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

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.desktop.modules.plastic.PlasticApplicationDescription;
import org.astrogrid.desktop.modules.ui.voexplorer.VOExplorerImpl;
import org.votech.plastic.incoming.handlers.AbstractMessageHandler;

/** Factory for voexplorers.
 *  - also handles some plastic messages. 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 6, 20076:44:32 PM
 */
public class VOExplorerFactoryImpl  extends AbstractMessageHandler implements VOExplorerFactoryInternal {
	
	public final TypesafeObjectBuilder builder;

	private static final Log logger = LogFactory.getLog(VOExplorerFactoryImpl.class);

    private final List plasticApps; // dynamic list model of currently registered applications.
	
	public VOExplorerFactoryImpl(final List plasticApps,final TypesafeObjectBuilder builder) {
		this.plasticApps = plasticApps;
        this.builder = builder;
	}	
	private VOExplorerImpl newWindow() {
		final VOExplorerImpl vo = builder.createVoExplorer();
		vo.setVisible(true);
		return vo;
	}
// runnable interface.
	public void run() {
		create();
	}
//Factory Interface
	// create a new voexplorer
	public Object create() {
		return newWindow();
	}
	
//RegistryBrowser interface.
	public void show() {
		newWindow();
	}
	public void hide() {
		// not implemented.
	}
	public void open(final URI arg0) {
		final VOExplorerImpl impl = newWindow();
		impl.doOpen(arg0);
	}
	public void search(final String arg0) {
		final VOExplorerImpl impl = newWindow();
		impl.doQuery("Search",arg0);
	}

	// Message Handling Interface
	public static final URI BIBCODE_MESSAGE = URI.create("ivo://votech.org/bibcode");

	public static final List REGISTRY_MESSAGES = new ArrayList() {{
			add(URI.create("ivo://votech.org/voresource/load"));
			add(URI.create("ivo://votech.org/voresource/loadList"));
	}};
	
	public static final List ALL_MESSAGES = new ArrayList() {{
	    addAll(REGISTRY_MESSAGES);
	    add(BIBCODE_MESSAGE);
	}};
	
	public static final URI VORESOURCE_LOAD = (URI)REGISTRY_MESSAGES.get(0);
	public static final URI VORESOURCE_LOADLIST= (URI)REGISTRY_MESSAGES.get(1);
	
	protected List getLocalMessages() {
		return ALL_MESSAGES;
	}
	// handles both kinds of message - quite tolerant of different object types.
	// however, at the moment will choke and fail on the first malformed uri.
	public Object perform(final URI sender, final URI message, final List args) {
		if (REGISTRY_MESSAGES.contains(message) && args.size() > 0) {
			try { //handle a string, collection, or array...
				final List resList = new ArrayList();
				final Object o = args.get(0);
				if (o == null) {
					logger.warn("Null argument");
					return Boolean.FALSE;
				}
				if (o instanceof Collection) {
					final Collection c= (Collection)o;
					for (final Iterator i = c.iterator(); i.hasNext();) {
						final Object e =  i.next();
						if (e != null) {
							resList.add(new URI(e.toString()));
						}
					}
				} else if (o.getClass().isArray()) {
					final Object[] arr = (Object[])o;
					for (int i = 0; i < arr.length; i++) {
						if (arr[i] != null) {
							resList.add(new URI(arr[i].toString()));
						}
					}
				} else { // treat it as a single string
					final URI resourceId = new URI(o.toString());
					resList.add(resourceId);
				}
				final String finalAppName = findSenderName(sender);
				// got all the info we need. display the ui on the EDT.
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						final VOExplorerImpl ve = newWindow();
						ve.displayResources("Resources from " + finalAppName,resList);
					}
				});				
				return Boolean.TRUE;
			} catch (final URISyntaxException x) {
				logger.error("URISyntaxException",x);
				return Boolean.FALSE;
			}
		} else if (BIBCODE_MESSAGE.equals(message) && args.size() == 1) {
		    final Object o = args.get(0);
		    if (o == null) {
                logger.warn("Null argument");
                return Boolean.FALSE;
		    } else {
		        final String bibcode = o.toString();
		        if (StringUtils.isEmpty(bibcode)) {
                    logger.warn("Null argument");
                    return Boolean.FALSE;		            
		        }
                //final String finalAppName = findSenderName(sender);
                // got all the info we need. display the ui on the EDT.
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        final VOExplorerImpl ve = newWindow();
                        ve.doQuery("Resources for " + bibcode,
                                "for $r in //vor:Resource[not (@status='inactive' or @status='deleted')] \n "+
                                " where $r/vr:content/vr:source =  '" + StringEscapeUtils.escapeSql(bibcode) + "' \n" +
                                " return $r" );
                    }
                }); 	
                return Boolean.TRUE;
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
    /**
     * @param sender
     * @return
     */
    private String findSenderName(final URI sender) {
        String appName = "unknown application";
        for (final Iterator i = plasticApps.iterator(); i.hasNext();) {
            final PlasticApplicationDescription desc = (PlasticApplicationDescription) i.next();
            if (desc.getId().equals(sender)) {
                appName = desc.getName();
                break;
            }
        }
        return appName;
    }

	
}
