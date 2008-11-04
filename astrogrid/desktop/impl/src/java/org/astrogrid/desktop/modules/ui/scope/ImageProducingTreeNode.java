/**
 * 
 */
package org.astrogrid.desktop.modules.ui.scope;

import java.awt.Image;

/** Common interface to the two kinds of node that produce an image.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Dec 10, 200711:31:06 AM
 */
public interface ImageProducingTreeNode {

    public Image getImage();
    
    public void setImage(Image img);

}