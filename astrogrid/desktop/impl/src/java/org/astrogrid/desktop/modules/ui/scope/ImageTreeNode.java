/**
 * 
 */
package org.astrogrid.desktop.modules.ui.scope;

import java.awt.Image;

import edu.berkeley.guir.prefuse.graph.DefaultTreeNode;

/**A Tree node that contains an image.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Dec 10, 200711:31:50 AM
 */
public class ImageTreeNode extends DefaultTreeNode implements
        ImageProducingTreeNode {
    private Image img;
    public Image getImage() {
        return img;
    }
    public void setImage(final Image img) {
        this.img = img;
    }
    
    /**
     * 
     */
    public ImageTreeNode() {
    }
  
    public ImageTreeNode(final Image img) {
        this.img = img;
    }    
}
