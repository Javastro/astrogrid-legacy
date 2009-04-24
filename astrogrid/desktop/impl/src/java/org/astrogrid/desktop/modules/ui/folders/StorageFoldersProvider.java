/**
 * 
 */
package org.astrogrid.desktop.modules.ui.folders;

import java.io.File;
import java.net.URI;

import org.apache.commons.lang.SystemUtils;
import org.apache.commons.vfs.provider.FileProvider;
import org.astrogrid.acr.astrogrid.UserLoginEvent;
import org.astrogrid.acr.astrogrid.UserLoginListener;
import org.astrogrid.desktop.modules.system.XmlPersist;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.system.ui.UIContext;

import ca.odell.glazedlists.EventList;
import org.astrogrid.desktop.modules.ag.vfs.ActivatableVfsFileProvider;
import org.astrogrid.desktop.modules.system.HivemindFileSystemManager;

/** Provider for Storage Folders.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 26, 200711:35:21 PM
 */
public class StorageFoldersProvider extends AbstractListProvider<StorageFolder> implements UserLoginListener {


	public StorageFoldersProvider(final UIContext parent, final Preference workdirPreference, final XmlPersist xml, HivemindFileSystemManager hvfs) {
		super(parent,new File(new File(workdirPreference.getValue()),"storageFolders.xml"), xml);
		logger.info("Reading/Writing storage folders to " + getStorageLocation());
		
		// now check that required folders are present.
		// if some are missing, add them in at the end.
		final EventList<StorageFolder> list = getList();
		final StorageFolder home = createHome();
		if (! list.contains(home)) {
		    list.add(home);
		}
        final StorageFolder workspace = createWorkspace();
        if (! list.contains(workspace)) {
            list.add(workspace);
        }
        FileProvider prov  = hvfs.getProvidermap().get("examples");
        examplesActive = prov != null
                && prov instanceof ActivatableVfsFileProvider
                && ((ActivatableVfsFileProvider)prov).isActive()
                ;
        final StorageFolder examples = createExamples();
        if (examplesActive) {
            if (!list.contains(examples)) {
                getList().add(examples);
            }
        } else {
            if (list.contains(examples)) {
                getList().remove(examples);
            }
        }
	}

    private final boolean examplesActive;

	@Override
    protected void initializeFolderList() {
		
		final StorageFolder home = createHome();
        getList().add(home);
		
		final StorageFolder workspace = createWorkspace();
        getList().add(workspace);
        if (examplesActive) {
            final StorageFolder examples = createExamples();
            getList().add(examples);
        }
		final File[] fileRoots = File.listRoots();
        for ( int i = 0; i < fileRoots.length; i++ ) {
            final File fileRoot = fileRoots[ i ];
            if ( fileRoot.isDirectory() && fileRoot.canRead() ) {
            	String name = fileRoot.getAbsolutePath();
            	if (name == null || name.trim().length() ==0) { // unlkely, but a last resort
            		name = "/";
            	}
               getList().add(new StorageFolder(name,"disk16.png",fileRoot.toURI()));
            }
        }		

	}

    /**
     * @return
     */
    private StorageFolder createExamples() {
        final StorageFolder examples = new StorageFolder("Examples","folder_examples16.png",URI.create("examples:/"));
        examples.setDescription("Example files and scripts for VODesktop");
        return examples;
    }

    /**
     * @return
     */
    private StorageFolder createWorkspace() {
        final StorageFolder workspace = new StorageFolder("VO Workspace","anystorage16.png",URI.create("workspace:///"));
		workspace.setDescription("Access to your VOSpace provided by AstroGrid:<br>requires login to a VO Community");
        return workspace;
    }

    /**
     * @return
     */
    private StorageFolder createHome() {
        final URI homeUri = SystemUtils.getUserHome().toURI();
		final StorageFolder home = new StorageFolder("Home","home16.png",homeUri);
        return home;
    }
// clear any cached file objects on logout.
    public void userLogin(final UserLoginEvent arg0) {
        //nothing here for now. in future, could possibly pre-load bookmarks on login.
    }

    public void userLogout(final UserLoginEvent arg0) {
        // some of the sotrage folders may hold a pointer to a myspace file - be safe by removing them all.
        for (final StorageFolder f : getList()) {
            f.setFile(null);
        }        
    }

}
