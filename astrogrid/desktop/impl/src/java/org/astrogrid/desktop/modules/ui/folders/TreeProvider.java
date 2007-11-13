package org.astrogrid.desktop.modules.ui.folders;

import javax.swing.tree.TreeModel;

/**
 * Interface to something which can supply a {@link javax.swing.tree.TreeModel}.
 *
 * @author   Mark Taylor
 * @since    5 Sep 2007
 */
public interface TreeProvider {

    /**
     * Returns a TreeModel.
     *
     * @return  tree model
     */
    TreeModel getTreeModel();
}
