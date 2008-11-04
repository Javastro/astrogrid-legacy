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
import org.apache.commons.vfs.FileObject;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileExplorerImpl;
import org.votech.plastic.incoming.handlers.AbstractMessageHandler;

/** Factory for fileexplorers.
 * - also handles some messages.
 * 
 * @todo implement the plastic message handling part of this, then uncomment section in ui.xml
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 6, 20076:44:32 PM
 */
public class FileExplorerFactoryImpl  extends AbstractMessageHandler implements FileManagerFactory {
	
	public final TypesafeObjectBuilder builder;

	private static final Log logger = LogFactory.getLog(FileExplorerFactoryImpl.class);
	
	public FileExplorerFactoryImpl(final TypesafeObjectBuilder builder) {
		this.builder = builder;
	}	
	private FileManagerInternal newWindow() {
		final FileExplorerImpl vo = builder.createFileExplorer();
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
	
	@Override
    protected List getLocalMessages() {
		return FILE_MESSAGES;
	}
	// handles both kinds of message - quite tolerant of different object types.
	// however, at the moment will choke and fail on the first malformed uri.
	public Object perform(final URI sender, final URI message, final List args) {
		if (FILE_MESSAGES.contains(message) && args.size() > 0) {
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
				// got all the info we need. display the ui on the EDT.
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						  newWindow();
						//@todo implement
						//newWindow.show()displayResources(resList);
					}
				});				
				return Boolean.TRUE;
			} catch (final URISyntaxException x) {
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
    public void show(final FileObject fo) {
        newWindow().show(fo);
    }
	
}
