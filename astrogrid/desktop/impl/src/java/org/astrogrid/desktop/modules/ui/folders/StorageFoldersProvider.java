/**
 * 
 */
package org.astrogrid.desktop.modules.ui.folders;

import java.io.File;
import java.net.URI;
import java.util.Iterator;

import org.apache.commons.lang.SystemUtils;
import org.astrogrid.acr.astrogrid.UserLoginEvent;
import org.astrogrid.acr.astrogrid.UserLoginListener;
import org.astrogrid.desktop.modules.system.XmlPersist;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.system.ui.UIContext;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 26, 200711:35:21 PM
 */
public class StorageFoldersProvider extends AbstractListProvider implements UserLoginListener {


	public StorageFoldersProvider(final UIContext parent, Preference workdirPreference, XmlPersist xml) {
		super(parent,new File(new File(workdirPreference.getValue()),"storageFolders.xml"), xml);
		logger.info("Reading/Writing storage folders to " + getStorageLocation());
	}

	protected void initializeFolderList() {
		
		URI homeUri = SystemUtils.getUserHome().toURI();
		getList().add(new StorageFolder("Home","home16.png",homeUri));
		
		final StorageFolder workspace = new StorageFolder("VO Workspace","networkdisk16.png",URI.create("workspace:///"));
		workspace.setDescription("Access to your VOSpace provided by AstroGrid:<br>requires login to a VO Community");
        getList().add(workspace);
		
		File[] fileRoots = File.listRoots();
        for ( int i = 0; i < fileRoots.length; i++ ) {
            File fileRoot = fileRoots[ i ];
            if ( fileRoot.isDirectory() && fileRoot.canRead() ) {
            	String name = fileRoot.getAbsolutePath();
            	if (name == null || name.trim().length() ==0) { // unlkely, but a last resort
            		name = "/";
            	}
               getList().add(new StorageFolder(name,"disk16.png",fileRoot.toURI()));
            }
        }		

	}
// clear any cached file objects on logout.
    public void userLogin(UserLoginEvent arg0) {
        //nothing here for now. in future, could possibly pre-load bookmarks on login.
    }

    public void userLogout(UserLoginEvent arg0) {
        // some of the sotrage folders may hold a pointer to a myspace file - be safe by removing them all.
        for (Iterator i = getList().iterator(); i.hasNext();) {
            StorageFolder f = (StorageFolder) i.next();
            f.setFile(null);
        }        
    }

}
