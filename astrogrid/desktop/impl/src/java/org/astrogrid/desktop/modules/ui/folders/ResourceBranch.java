package org.astrogrid.desktop.modules.ui.folders;

import org.astrogrid.desktop.modules.ui.voexplorer.RegistryGooglePanel;
import org.astrogrid.desktop.modules.ui.voexplorer.VOExplorerImpl;

/**
 * ResourceFolder implementation whose function is just to contain other
 * ResourceFolder objects.
 * Note that this object only describes the folder characteristics, 
 * it does not contain its children - that function
 * is taken care of by the associated TreeNode when it's within a tree.
 *
 * @author    Mark Taylor
 * @since     6 Sep 2007
 */
public class ResourceBranch extends ResourceFolder {

   // public static final String defaultIcon = "file-manager.png";
    private static final String defaultIcon ="folder16.png";
    /**
     * Constructs a branch with a given name.
     *
     * @param  name   branch name
     */
    public ResourceBranch(String name) {
        super(name, defaultIcon);
    }

    /**
     * No-arg constructor.
     */
    public ResourceBranch() {
        this("New Container");
    }

    @Override
    public void display(RegistryGooglePanel p) {
        p.clear();
    }

    @Override
    public void edit(VOExplorerImpl voe) {
        // no action - best disabled
    }

    @Override
    public void editAsNew(VOExplorerImpl voe) {
        voe.editNewResourceBranch(this);
    }
}
