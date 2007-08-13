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

import net.sourceforge.hiveutils.service.ObjectBuilder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileObject;
import org.astrogrid.desktop.hivemind.IterableObjectBuilder;
import org.astrogrid.desktop.modules.system.ui.ActivitiesManager;
import org.astrogrid.desktop.modules.system.ui.ActivityFactory;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.system.ui.UIContributionBuilder;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileExplorerImpl;
import org.astrogrid.desktop.modules.ui.voexplorer.VOExplorerImpl;
import org.votech.plastic.incoming.handlers.AbstractMessageHandler;

import ca.odell.glazedlists.EventList;

/** Factory for fileexplorers- also handles some messages.
 * 
 * @fixme implement the message handling part of this.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 6, 20076:44:32 PM
 */
public class FileExplorerFactoryImpl  extends AbstractMessageHandler implements FileManagerFactory {
	
	public final TypesafeObjectBuilder builder;

	private static final Log logger = LogFactory.getLog(FileExplorerFactoryImpl.class);
	
	public FileExplorerFactoryImpl(TypesafeObjectBuilder builder) {
		this.builder = builder;
	}	
	private FileManagerInternal newWindow() {
		FileExplorerImpl vo = builder.createFileExplorer();
		vo.setVisible(true);
		return vo;
	}

//Factory Interface
	// create a new voexplorer
	public Object create() {
		return newWindow();
	}
	
//Myspace  interface.
	public void show() {
		newWindow();
	}
	public void hide() {
		// not implemented.
	}
	

	// Message Handling Interface
	public static final List FILE_MESSAGES = new ArrayList() {{
	//		add(URI.create("ivo://votech.org/voresource/load"));
	//		add(URI.create("ivo://votech.org/voresource/loadList"));
	}};
	
	//public static final URI VORESOURCE_LOAD = (URI)FILE_MESSAGES.get(0);
	//public static final URI VORESOURCE_LOADLIST= (URI)FILE_MESSAGES.get(1);
	
	protected List getLocalMessages() {
		return FILE_MESSAGES;
	}
	// handles both kinds of message - quite tolerant of different object types.
	// however, at the moment will choke and fail on the first malformed uri.
	public Object perform(URI sender, URI message, List args) {
		if (FILE_MESSAGES.contains(message) && args.size() > 0) {
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
						 FileManagerInternal newWindow = newWindow();
						//@todo implement
						//ve.displayResources(resList);
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
    public void show(FileObject fo) {
        newWindow().show(fo);
    }
	
}
