package org.astrogrid.desktop.modules.ui.folders;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.desktop.modules.system.XmlPersist;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.comp.ExceptionFormatter;

/**
 * TreeModel which holds the set of resources for the VOExplorer.
 * Each node in the model is a {@link javax.swing.tree.DefaultMutableTreeNode}
 * with a user object of type 
 * {@link org.astrogrid.desktop.modules.ui.folders.ResourceFolder}.
 *
 * @author   Mark Taylor
 */
public class ResourceTreeModel extends DefaultTreeModel {

    private static final Log logger =
        LogFactory.getLog(ResourceTreeModel.class);

    private final UIContext parent;
    private final XmlPersist persister;
    private final Set subscriptionSet = new HashSet();

    /**
     * Constructor.
     *
     * @param    parent  context
     * @param    persister   XML persistence implementation
     */
    public ResourceTreeModel(final UIContext parent, final XmlPersist persister) {
        super(null);
        this.parent = parent;
        this.persister = persister;

        // Add a listener which handles subscription-type nodes.
        // Any time a node with the subscription attribute is entered into
        // the tree, an attempt will be triggered to load the subscribed 
        // content and substitute it for the original node content.
        // The fact that the original node content is used to start with means
        // that if the load fails the worst the user sees is failure to
        // update the out-of-date content.
        addTreeModelListener(new TreeModelListener() {
            public void treeNodesChanged(final TreeModelEvent evt) {
                final Object[] children = evt.getChildren();
                for (int i = 0; i < children.length; i++) {
                    updateSubscriptions(children[i]);
                }
            }
            public void treeNodesInserted(final TreeModelEvent evt) {
                final Object[] children = evt.getChildren();
                for (int i = 0; i < children.length; i++) {
                    updateSubscriptions(children[i]);
                }
            }
            public void treeStructureChanged(final TreeModelEvent evt) {
                updateSubscriptions(evt.getTreePath().getLastPathComponent());
            }
            public void treeNodesRemoved(final TreeModelEvent evt) {
            }
        });
    }

    /**
     * Performs a deep copy of a node in the tree.
     *
     * @param   node  node to clone
     * @param   identical  true for an identical copy, false for one which is
     *          obviously a copy of the original
     */
    public DefaultMutableTreeNode duplicateNode(final DefaultMutableTreeNode node,
                                                final boolean identical) {

        // The implementation of this method serializes to XML and then
        // deserializes back again.  This is not maximally efficient, but
        // it's not performance-critical and it ensures a deep copy.
        DefaultMutableTreeNode dupNode;
        try {
            final ByteArrayOutputStream os = new ByteArrayOutputStream();
            persister.toXml(BranchBean.fromTreeNode(node), os);
            os.close();
            final InputStream is = new ByteArrayInputStream(os.toByteArray());
            dupNode = BranchBean.toTreeNode(persister.fromXml(is));
            is.close();
        }
        catch (final IOException e) {
            throw (Error) new AssertionError("XStream persistence trouble?")
                         .initCause(e);
        }
        catch (final ServiceException e) {
            throw (Error) new AssertionError("XStream persistence trouble?")
                         .initCause(e);
        }

        // If the node is the duplicate of an existing one we do not want to
        // attempt to read subscription information again (since it either
        // did or didn't succeed when the original node was inserted, and
        // probably the same thing will happen again).  So note the existence
        // of the new node so that any subsequent insertion of it does 
        // not trigger a read.
        subscriptionSet.add(dupNode);        

        if (! identical) {
            final ResourceFolder folder = (ResourceFolder) dupNode.getUserObject();
            folder.setFixed(false);
            folder.setSubscription(null);
            final String fname = folder.getName();
            final String prefix = "Copy of ";
            if (! fname.startsWith(prefix)) {
                folder.setName(prefix + fname);
            }
        }
        return dupNode;
    }

    /**
     * Ensure that the given node and any of its descendents have had their
     * <code>subscription</code> attributes attended to.
     * If they have subscriptions the content of the nodes should be replaced
     * by that loaded from external storage at the indicated locations,
     * unless this has been done before.
     *
     * @param   nodeObj  node to update
     */
    private void updateSubscriptions(final Object nodeObj) {
        final DefaultMutableTreeNode node = (DefaultMutableTreeNode) nodeObj;
        final ResourceFolder folder = (ResourceFolder) node.getUserObject();
        final String subscrip = folder.getSubscription();

        // No subscription - check the children recursively.
        if (subscrip == null) {
            for (final Enumeration en = node.children(); en.hasMoreElements();) {
                updateSubscriptions(en.nextElement());
            }
        }

        // We have a subscription - load content unless we've done it before
        else {
            if (!subscriptionSet.contains(node)) {
                subscriptionSet.add(node);
                updateNode(node, subscrip);
            }
        }
    }

    /**
     * Take an existing node and replace it with one generated by loading
     * an XStream XML file from a given location.
     *
     * @param  oldNode  node to replace
     * @param  subscription  URL of XML file describing new content
     */
    private void updateNode(final DefaultMutableTreeNode oldNode,
                            final String subscription) {
        new BackgroundWorker(parent, 
                             "Loading subscription at " + subscription,BackgroundWorker.LONG_TIMEOUT,Thread.MAX_PRIORITY) {
            {
                assert isAttached();
            }

            protected Object construct() throws IOException, ServiceException {

                // Load content from remote location.
                final URL url = new URL(subscription);
                final InputStream is = url.openStream();
                try {
                    final Object bean = persister.fromXml(is);
                    final DefaultMutableTreeNode newNode =
                        BranchBean.toTreeNode(bean);
                    return newNode;
                }
                finally {
                   IOUtils.closeQuietly(is);
                }
            }

            protected void doFinished(final Object obj) {

                // Node may have been removed from tree while we were working.
                if (!isAttached()) {
                    return;
                }

                // Replace original node in the tree with the generated one
                final ResourceFolder oldFolder = 
                    (ResourceFolder) oldNode.getUserObject();
                final DefaultMutableTreeNode newNode = (DefaultMutableTreeNode) obj;
                final ResourceFolder newFolder =
                    (ResourceFolder) newNode.getUserObject();
                newFolder.setSubscription(subscription);
                newFolder.setName(oldFolder.getName());
                newFolder.setIconName(oldFolder.getIconName());
                final DefaultMutableTreeNode parentNode =
                    (DefaultMutableTreeNode) oldNode.getParent();
                final int childIndex = parentNode.getIndex(oldNode);

                // Remember that this one has been read from XML
                subscriptionSet.add(newNode);
                subscriptionSet.remove(oldNode);

                // Prepare and insert node.
                removeNodeFromParent(oldNode);
                insertNodeInto(newNode, parentNode, childIndex);
                
                // Recursively check new node's children for any subscription
                // elements
                for (final Enumeration en = newNode.children();
                     en.hasMoreElements();) {
                    updateSubscriptions(en.nextElement());
                }
            }

            protected void doError(final Throwable e) {

                // Node may have been removed from tree while we were working.
                if (!isAttached()) {
                    return;
                }
                parent.showTransientError("Failed to load subscription from " + subscription,ExceptionFormatter.formatException(e));
                logger.warn("Failed to load subscribed resource list from " +
                            subscription, e);

                // Recursively check old node's children for any subscription
                // elements.  This wasn't done previously in expectation that
                // they would be replaced.
                for (final Enumeration en = oldNode.children();
                     en.hasMoreElements();) {
                    updateSubscriptions(en.nextElement());
                }
            }

            /**
             * Determine whether the node being replaced is still in the
             * tree or not.
             *
             * @return  true iff oldNode is in this tree
             */
            private boolean isAttached() {
                return oldNode.isNodeAncestor((TreeNode) getRoot());
            }
        }.start();
    }
}
