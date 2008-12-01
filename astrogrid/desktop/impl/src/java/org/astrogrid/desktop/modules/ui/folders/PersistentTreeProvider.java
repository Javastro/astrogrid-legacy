package org.astrogrid.desktop.modules.ui.folders;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.desktop.modules.system.XmlPersist;
import org.astrogrid.desktop.modules.system.ui.UIContext;

/**
 * TreeProvider which will write the tree contents to a persistent file 
 * every time they change.
 *
 * @author   Mark Taylor
 * @since    5 Sep 2007
 * @see      {@link AbstractListProvider}
 */
public abstract class PersistentTreeProvider implements TreeProvider {

    protected static final Log logger =
        LogFactory.getLog(PersistentTreeProvider.class);

    protected final UIContext parent;
    protected final File storage;
    protected final XmlPersist persister;
    private DefaultTreeModel treeModel;

    /**
     * Thread which is currently engaged in saving the state of this object
     * to a file.  When it finishes it should remove itself.
     */
    private volatile Saver saver;

    /**
     * Constructor.
     *
     * @param  parent  ui context
     * @param  storage   file to which state will be persisted
     * @param  persister  xml persistence implementation
     */
    public PersistentTreeProvider(final UIContext parent, final File storage,
                                  final XmlPersist persister) {
        this.parent = parent;
        this.storage = storage;
        this.persister = persister;
    }

    /**
     * Performs initialisation.  Must be called in the constructor of
     * concrete subclasses.  Contains invocations of methods which can 
     * be overridden by subclasses, and therefore should not be invoked
     * in the constructor of this superclass.
     *
     * @param  treeModel  an empty instance of the model which will 
     *                    be provided by this object
     */
    protected void init(final DefaultTreeModel treeModel) {
        this.treeModel = treeModel;
        treeModel.addTreeModelListener(new TreeModelListener() {
            public void treeNodesChanged(final TreeModelEvent evt) {
                treeChanged();
            }
            public void treeNodesInserted(final TreeModelEvent evt) {
                treeChanged();
            }
            public void treeNodesRemoved(final TreeModelEvent evt) {
                treeChanged();
            }
            public void treeStructureChanged(final TreeModelEvent evt) {
                treeChanged();
            }
            private void treeChanged() {
                save((TreeNode) PersistentTreeProvider.this.treeModel.getRoot());
            }
        });

        // Attempt to load from persistent storage
        TreeNode root = null;
        try {
            root = load();
            if (root != null) {
                logger.info("Loaded " + root.getChildCount()
                           + " top-level nodes " + "from " + this.storage );
            }
        }
        catch (final IOException e) {
            logger.error("Error loading from " + this.storage, e);
        }
        catch (final ServiceException e) {
            logger.error("Error deserializing " + this.storage, e);
        }

        // If no persistent state was found, initialise by calling
        // subclass-specific code.
        if (root == null || root.getChildCount() == 0) {
            logger.info("Initialising tree with default contents");
            root = getDefaultRoot();
            save(root);
        }

        // Ensure root is appropriately titled.
        if (root instanceof DefaultMutableTreeNode) {
            final Object userObj = ((DefaultMutableTreeNode) root).getUserObject();
            if (userObj instanceof ResourceBranch) {
                ((Folder) userObj).setName("Resource Lists");
            }
        }
        treeModel.setRoot(root);
    }

    /**
     * Provides a root node which can populate this object's tree
     * if no persistent state can be loaded.
     *
     * @return   root node
     */
    public abstract DefaultMutableTreeNode getDefaultRoot();

    /**
     * Returns the root node, which in this case is a 
     * {@link javax.swing.tree.DefaultTreeModel} (and hence mutable).
     *
     * @return   tree model
     * @see   {@link #getDefaultTreeModel}
     */
    public TreeModel getTreeModel() {
        return getDefaultTreeModel();
    }

    /**
     * Returns the root node cast to a
     * {@link javax.swing.tree.DefaultTreeModel}.
     *
     * @return  tree model
     * @see   {@link #getTreeModel}
     */
    public DefaultTreeModel getDefaultTreeModel() {
        return treeModel;
    }

    /**
     * Returns the file used for persistence.
     *
     * @return  file
     */
    public File getStorageLocation() {
        return storage;
    }

    /**
     * Queues the given root element to save as this object's state 
     * to the persistence file.  May be called from any thread.
     *
     * @param   root  tree state to save
     */
    public synchronized void save(final TreeNode root) {

        // If we're not already saving, save now in a background thread.
        if (this.saver == null) {
            this.saver = new Saver(root);
            try {
                parent.getExecutor().execute(saver);
            } catch (final InterruptedException x) {
                logger.error("InterruptedException",x);
            }
        }

        // If a save operation is already in progress, store the new saver
        // but leave it to be started by the existing saver when it finishes.
        // In this way if a load of save requests arrive in quick succession
        // some may get skipped but more recent ones are favoured.
        else {
            this.saver = new Saver(root);
        }
    }

    /**
     * Blocks until no save operation is pending.
     * only used from unit test
     * join() not possible now that we're using an executor. - biut only used from testing.
     */
    synchronized void waitForUpdate() throws InterruptedException {
        while (saver != null) {
            // busy-wait.
            //saver.join();
        }
    }

    /**
     * Loads the state of this object from the persistence file.
     *
     * <p><strong>Note:</strong> no measures are taken to prevent a 
     * read clashing with a write, so failure because the load sees
     * a file partially written by a save is possible here.
     * My (mbt's) understanding is that this is not a very likely event.
     *
     * @return  persisted tree root, or null on failure
     */
    public DefaultMutableTreeNode load() throws IOException, ServiceException {
        if (storage.exists() && storage.length() > 0) {
            logger.info("Loading tree from " + storage);
            InputStream fis = null;
            try {
                fis = FileUtils.openInputStream(storage);
                final Object obj = persister.fromXml(fis);
                return BranchBean.toTreeRoot(obj, "root");
            }
            finally {
               IOUtils.closeQuietly(fis);
            }
        }
        else {
            return null;
        }
    }

    /**
     * Worker thread class which persists the state of this provider to the
     * storage file and then terminates.  When it terminates it will
     * remove itself from the provider's saver member.
     * If when it finishes another saver is waiting, it will start that one
     * running.
     * 
     * NWW - modified from a Thread to a Runnable, which is then passed to background executor to run - pooling of threads.
     */
    private class Saver implements Runnable {
        private final Object bean;

        /**
         * Constructor.
         *
         * @param   root  state to save
         */
        Saver(final TreeNode root) {
          //  super("Tree saver");
            bean = BranchBean.fromTreeNode(root);
        }

        public void run() {
            try {
                save();
            }
            catch (final IOException e) {
                logger.error("Error writing to " + storage, e);
            }
            catch (final ServiceException e) {
                logger.error("Error serializing to " + storage, e);
            }
            finally {
                final PersistentTreeProvider owner = PersistentTreeProvider.this;
                final Saver workingSaver = owner.saver;

                // If we are the most recently submitted save request, 
                // remove ourself from the owner to indicate that no save
                // is in progress.
                if (workingSaver == this) {
                    owner.saver = null;
                }

                // If another save request has been submitted since we 
                // started, then start that one running.
                else if (workingSaver != null) {
                    try {
                        parent.getExecutor().execute(workingSaver);
                    } catch (final InterruptedException x) {
                        logger.error("InterruptedException",x);
                    }
                }
            }
        }

        /**
         * Perform the actual save.
         */
        private void save() throws IOException, ServiceException {
            final OutputStream out =FileUtils.openOutputStream(storage);
            try {
                persister.toXml(bean, out);
            }
            finally {
                IOUtils.closeQuietly(out);
            }
        }
    }
}
