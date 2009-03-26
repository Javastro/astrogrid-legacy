package org.astrogrid.desktop.modules.ui.folders;

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

/**
 * Serializable object which represents a branch node of a tree, that is
 * one which has zero or more children.
 * Getter and setter methods obeying java bean conventions are provided.
 * Loss-less conversions between this and branch-type 
 * {@link javax.swing.tree.DefaultMutableTreeNode}s can be made, and
 * static methods are provided to do this.
 *
 * @author   Mark Taylor
 * @since    6 Sep 2007
 */
public class BranchBean {

    private Object node;
    private Object[] children;

    /**
     * No-arg constructor.
     */
    public BranchBean() {
    }

    /**
     * Constructor defining name and children.
     *
     * @param   node   object representing branch node characteristics
     * @param   children  node children
     */
    public BranchBean(Object node, Object[] children) {
        setNode(node);
        setChildren(children);
    }

    public void setNode(Object node) {
        this.node = node;
    }

    public Object getNode() {
        return this.node;
    }

    public Object[] getChildren() {
        return this.children;
    }

    public Object getChildren(int index) {
        return this.children[index];
    }

    public void setChildren(Object[] children) {
        this.children = children;
    }

    public void setChildren(int index, Object child) {
        this.children[index] = child;
    }

    /**
     * Returns a serializable object corresponding to a given TreeNode.
     * Branch-type nodes in the tree will be represented by BranchBeans,
     * and leaf-type nodes will be represented by themselves, or by
     * the node's user object in the case of 
     * {@link javax.swing.tree.DefaultMutableTreeNode}s.
     *
     * @param   node  tree node for serialization
     * @return  bean representation of <code>node</code>
     */
    public static Object fromTreeNode(TreeNode node) {
        Object nodeObj = node instanceof DefaultMutableTreeNode
                       ? ((DefaultMutableTreeNode) node).getUserObject()
                       : node;
        if (node.getAllowsChildren()) {
            int nc = node.getChildCount();
            Object[] children = new Object[nc];
            for (int ic = 0; ic < nc; ic++) {
                children[ic] = fromTreeNode(node.getChildAt(ic));
            }
            return new BranchBean(nodeObj, children);
        }
        else {
            return nodeObj;
        }
    }

    /**
     * Turns a bean representing a tree node into a TreeNode object
     * suitable for use within a {@link javax.swing.tree.DefaultTreeModel}.
     * BranchBean objects in the root or its children will be turned into
     * branch-like nodes with corresponding children.
     *
     * @param  nodeBean   BranchBean or branch child
     * @return  tree node representing <code>nodeBean</code> and its descendents
     */
    public static DefaultMutableTreeNode toTreeNode(Object nodeBean) {
        if (nodeBean instanceof BranchBean) {
            BranchBean branch = (BranchBean) nodeBean;
            DefaultMutableTreeNode treeNode =
                new DefaultMutableTreeNode(branch.getNode(), true);
            Object[] children = branch.getChildren();
            for (int ic = 0; ic < children.length; ic++) {
                treeNode.add(toTreeNode(children[ic]));
            }
            return treeNode;
        }
        else {
            return new DefaultMutableTreeNode(nodeBean, false);
        }
    }

    /**
     * Turns a bean representing a tree node into a TreeNode object
     * suitable for use at the root of a
     * {@link javax.swing.tree.DefaultTreeModel}.
     * BranchBean objects in the root or its children will be turned into
     * branch-like nodes with corresponding children.
     *
     * <p>If the supplied <code>nodeBean</code> is a BranchBean (and hence has
     * a name already) this name is not altered.  If a new BranchBean is
     * constructed to hold the results however it will be named using the
     * given <code>fallbackName</code>.
     *
     * @param   nodeBean  BranchBean representing tree root or
     *          or array or list or singleton representing root children
     * @param   fallbackName  name used for root node if <code>nodeBean</code>
     *          is not a branch
     * @return  tree node representing <code>nodeBean</code> and its descendents
     */
    public static DefaultMutableTreeNode toTreeRoot(Object nodeBean,
                                                    String fallbackName) {
        BranchBean root;
        if (nodeBean instanceof BranchBean) {
            root = (BranchBean) nodeBean;
        }
        else if (nodeBean instanceof Object[]) {
            root = new BranchBean(new ResourceBranch(fallbackName),
                                  (Object[]) nodeBean);
        }
        else if (nodeBean instanceof List) {
            root = new BranchBean(new ResourceBranch(fallbackName),
                                  ((List) nodeBean).toArray());
        }
        else if (nodeBean == null) {
            throw new NullPointerException();
        }
        else {
            root = new BranchBean(new ResourceBranch(fallbackName),
                                  new Object[] {nodeBean});
        }
        return toTreeNode(root);
    }
}
