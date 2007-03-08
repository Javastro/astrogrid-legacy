/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.resource.folders;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

import org.astrogrid.desktop.modules.system.Preference;
import org.astrogrid.desktop.modules.system.UIImpl;
import org.astrogrid.desktop.modules.system.UIInternal;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIComponent;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;

/** takes care of persisting resource folders.
 * Provides a single model to which multople {@link RsourceFoldersView} can attach
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 5, 200712:24:41 PM
 */
public class ResourceFoldersStorageImpl implements ListEventListener, ResourceFoldersStorage {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(ResourceFoldersStorageImpl.class);

	private final UIComponent parent;
	private final EventList folderList;
	private final File storage;
	
	
	public ResourceFoldersStorageImpl(final UIInternal parent, Preference workdirPreference) {
		super();
		this.storage = new File(new File(workdirPreference.getValue()),"resourceFolders.xml");
		logger.info("Reading/Writing resource folders to " + storage);
		this.parent = parent;
		this.folderList = new BasicEventList();
		loadFolderList(storage);
		if (folderList.size() == 0) {
			initializeFolderList(); 
			saveFolderList(storage);
		}
		folderList.addListEventListener(this);
	}



	public EventList getList() {
		return folderList;
	}
	
	

//	 create some basic entries in the folder list.
	private void initializeFolderList() {
		ResourceFolder[] folders = new ResourceFolder[] {
				new FilterResourceFolder("All Resources","world16.png",null)
				, new FilterResourceFolder("Catalogues","search16.png","@xsi:type &= '*ConeSearch' or @xsi:type &= '*TabularSkyService'")  //@todo find a better icon.
				, new FilterResourceFolder("Images","search16.png","@xsi:type &= '*SimpleImageAccess'", 300)
				, new FilterResourceFolder("Spectra","search16.png","@xsi:type &= '*SimpleSpectrumAccess' ", 10)
				, new FilterResourceFolder("Long-running tasks","exec16.png","@xsi:type &= '*CeaApplicationType' or @xsi:type &= '*CeaHttpApplicationType'", 100)
				, new FilterResourceFolder("Databases","db16.png","@xsi:type &= '*TabularDb' or @xsi:type &= '*DataCollection'",100)
				, new FilterResourceFolder("Time Series","latest16.png","@xsi:type &= '*SimpleTimeAccess'",100)
				,new QueryResourceFolder("Recent Changes","foldernew16.png",
						"let $thresh := current-dateTime() - xs:dayTimeDuration('P21D')\n"
						+ "let $dthresh := current-date() - xs:dayTimeDuration('P21D')\n"
						+ "for $r in //vor:Resource[not (@status='inactive' or @status='deleted')]\n"
						+ "where  ($r/@updated castable as xs:dateTime and xs:dateTime($r/@updated) > $thresh)\n"
						+ "or ($r/@updated castable as xs:date and xs:date($r/@updated) > $dthresh)\n"
						+ "or ($r/@created castable as xs:dateTime and xs:dateTime($r/@created) > $thresh)\n"
						+ "or ($r/@created castable as xs:date and xs:date($r/@created) > $dthresh)\n"
						+ "return $r"
					) //@todo need to find a way to avoid caching - or to control the caching period of this entry.
					// I suppose default cache is 3 days - that's not too bad.
				//@todo add in stap
				//@future add recently used, most used, tagged.
		};
		for (int i = 0; i < folders.length; i++) {
			ResourceFolder f = folders[i];
			f.setFixed(true);
			folderList.add( f);
		}
	}
//	@todo report load errors more visibly.
	private void loadFolderList(File f) {
		if (! f.exists()) {
			return;
		}
		InputStream fis = null;
		XMLDecoder x = null;
		try { 
			fis = new FileInputStream(f);
			x = new XMLDecoder(fis,this);
				ResourceFolder[] rs = (ResourceFolder[])x.readObject();
				folderList.addAll(Arrays.asList(rs));
			} catch (FileNotFoundException ex) {
				logger.error("FileNotFoundException",ex);
			} finally {
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
//	@todo report save errors more visibly.
	private void saveFolderList(final File file) {
		(new BackgroundWorker(parent,"Saving resource lists") {
			protected Object construct() throws Exception {
				OutputStream fos = null;
				XMLEncoder a = null;
				try {
					fos = new FileOutputStream(file);
					a = new XMLEncoder(fos);
					ResourceFolder[] rf = (ResourceFolder[])folderList.toArray(new ResourceFolder[folderList.size()]);
					a.writeObject(rf);
				} catch (FileNotFoundException x) {
					logger.error("FileNotFoundException",x);
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

}
