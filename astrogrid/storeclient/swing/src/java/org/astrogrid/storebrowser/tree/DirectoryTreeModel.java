/**
 * DirectoryTreeModel.java
 *
 * @author Created by Omnicore CodeGuide
 */

package org.astrogrid.storebrowser.tree;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

public class DirectoryTreeModel extends DefaultTreeModel
{
   public DirectoryTreeModel(StoresList stores) {
      super(stores);
   }
   
}

