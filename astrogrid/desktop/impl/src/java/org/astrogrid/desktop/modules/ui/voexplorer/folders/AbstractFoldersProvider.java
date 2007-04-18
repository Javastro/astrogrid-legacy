/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.folders;

import java.beans.ExceptionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.NoSuchElementException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;

/** abstract baseclass for a persistent list.
 * provides a persistent model to which multiple views can connect.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 26, 200711:25:20 PM
 */
public abstract class AbstractFoldersProvider 
	implements ListEventListener,  FoldersProvider, ExceptionListener{

	/**
	 * Logger for this class
	 */
	protected static final Log logger = LogFactory
				.getLog(ResourceFoldersProvider.class);

	/**
	 *  create a new persisten list.
	 *  
	 */
	public AbstractFoldersProvider(final UIContext parent, final File storage) {
			this.parent = parent;
			this.storage = storage;
			this.folderList = new BasicEventList();			
			loadFolderList(storage);
			if (folderList.size() == 0) {
				initializeFolderList(); 
				saveFolderList(storage);
			}
			folderList.addListEventListener(this);			
}

	/** subclasses should extend this to pre-populate the list */
	protected abstract void initializeFolderList() ;
	
	protected final UIContext parent;
	protected final EventList folderList;
	protected final File storage;

	public EventList getList() {
		return folderList;
	}

	protected void loadFolderList(File f) {
		if (! f.exists()) {
			return;
		}
		logger.info("Loading folder list from " + f);
		InputStream fis = null;
		XMLDecoder x = null;
		try { 
			fis = new FileInputStream(f);
			x = new XMLDecoder(fis,this,this);
			Folder[] rs = (Folder[])x.readObject();
			if (rs != null) {
				folderList.addAll(Arrays.asList(rs));
				logger.info("Loaded " + rs.length + " items");
			} else {
				logger.info("File is empty");
			}
		} catch (FileNotFoundException ex) {
			logger.error(storage.toString(),ex);
		} catch (ArrayIndexOutOfBoundsException ex) {
			// thrown when the file contains no data - a bit crap - no way to check this.
			// fail gracefully.
			logger.info("File is empty");
		} catch (NoSuchElementException ex) {
			// thrown when the file contains no data - a bit crap - no way to check this.
			// fail gracefully.
			logger.info("File is empty");			
		}finally {
			if (x != null) {
				x.close();
			} 
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException x1) {
					logger.error("IOException",x1);
				}
			}
		}
	}

	protected void saveFolderList(final File file) {
		(new BackgroundWorker(parent,"Saving resource lists") {
			protected Object construct() throws Exception {
				logger.info("Saving list to " + file);
				OutputStream fos = null;
				XMLEncoder a = null;
				try {
					fos = new FileOutputStream(file);
					a = new XMLEncoder(fos);
					Folder[] rf = (Folder[])folderList.toArray(new Folder[folderList.size()]);
					a.writeObject(rf);
				} catch (FileNotFoundException x) {
					logger.error(file.toString(),x);
				} finally {
					if (a != null) {
						a.close();
					} 
					if (fos != null) {
						try {
							fos.close();
						} catch (IOException x) {
							logger.error("Failed to save folder list.",x);
						}
					}
				}
				return null;
			}
		}).start();
	}

	/** folder list has changed - write it back to disk */
	public void listChanged(ListEvent arg0) {
		saveFolderList(storage);
	}
	// called when a recoverable exception is thrown by the decoder.
	public void exceptionThrown(Exception e) {
		logger.warn("Exception whilst reading file: '" + e.getMessage() + "' - continuing");
		logger.debug(e);
	}

}