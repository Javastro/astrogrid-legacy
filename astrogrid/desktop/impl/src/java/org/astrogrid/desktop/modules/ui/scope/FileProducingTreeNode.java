package org.astrogrid.desktop.modules.ui.scope;

import java.awt.Image;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.provider.DelegateFileObject;
import org.astrogrid.desktop.modules.ui.fileexplorer.IconFinder;

import edu.berkeley.guir.prefuse.graph.DefaultTreeNode;

/** subclass of a treenode that knows how to produce a file node for it's selection. 
 * 
 * this is the class of node used when a node in the tree contains some data (which is represented by the fileNode).
 * Because of this, we can also test on this class to determine whether to show a popup or not.
 * */
public final class FileProducingTreeNode extends DefaultTreeNode implements ImageProducingTreeNode {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(FileProducingTreeNode.class);
    private FileObject afo;
    // cached image object - displayed in prefuse tree.
    private Image image;

	public FileProducingTreeNode(){
	}

	/**
     * @param afo2
     */
    public FileProducingTreeNode(FileObject afo2,Image img) {      
        this.afo = afo2;
        this.image = img;
    }

    /** return the associated file object - may be null */
	public FileObject getFileObject() {
	    return afo;
	}
	/** set the file object for this node */
	public void setFileObject(FileObject afo,Image img) {
	    this.afo = afo;
	    this.image = img;
	}

    public final Image getImage() {
        return this.image;
    }

    public void setImage(Image img) {
        this.image = img;
    }
	

}