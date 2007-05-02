/**
 * 
 */
package org.astrogrid.desktop.modules.ui.folders;

import java.io.File;
import java.net.URI;

import org.apache.commons.lang.SystemUtils;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.system.ui.UIContext;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 26, 200711:35:21 PM
 */
public class StorageFoldersProvider extends AbstractFoldersProvider {

	/**
	 * @param parent
	 * @param storage
	 */
	public StorageFoldersProvider(final UIContext parent, Preference workdirPreference) {
		super(parent,new File(new File(workdirPreference.getValue()),"storageFolders.xml"));
		logger.info("Reading/Writing storage folders to " + storage);
	}

	protected void initializeFolderList() {
		
		URI homeUri = SystemUtils.getUserHome().toURI();
		folderList.add(new StorageFolder("Home","home16.png",homeUri));
		
		folderList.add(new StorageFolder("Workspace","networkdisk16.png",URI.create("workspace:///")));
		
		File[] fileRoots = File.listRoots();
        for ( int i = 0; i < fileRoots.length; i++ ) {
            File fileRoot = fileRoots[ i ];
            if ( fileRoot.isDirectory() && fileRoot.canRead() ) {
            	String name = fileRoot.getAbsolutePath();
            	if (name == null || name.trim().length() ==0) { // unlkely, but a last resort
            		name = "/";
            	}
               folderList.add(new StorageFolder(name,"disk16.png",fileRoot.toURI()));
            }
        }		

	}

}
