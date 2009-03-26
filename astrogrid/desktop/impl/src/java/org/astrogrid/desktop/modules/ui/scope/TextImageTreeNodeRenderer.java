/*$Id: TextImageTreeNodeRenderer.java,v 1.2 2009/03/26 18:04:10 nw Exp $
 * Created on 27-Jan-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ui.scope;


import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import edu.berkeley.guir.prefuse.VisualItem;
/** Duplication of TextImageItemSizeRenderer that also 
 * draws glpyhs for nodes with associated result files.
 * 
 * renderer for node that produces a text and image, where the text varies in size.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 27-Jan-2006
 *
 */
public class TextImageTreeNodeRenderer extends TextImageItemSizeRenderer {
    

    public TextImageTreeNodeRenderer(NodeSizingMap m) {
        super(m);
    }
    @Override
    protected Image getImage(VisualItem item) {
        Image ico =  super.getImage(item);
        Image file = null;
        if (item.getEntity() instanceof ImageProducingTreeNode) {
            file = ((ImageProducingTreeNode)item.getEntity()).getImage();
        }
        if (ico == null) {
            if (file == null) {
                return null;
            } else {
                return file;
            }
        } else {
            if (file == null) {
                return ico;
            } else {
                Image joined = combine(ico,file);
                // now we dont want to combine these icons every time.
                // instead, clear the ico image, and set the file image to the one we've just built.
                item.getEntity().setAttribute(getImageAttributeName(),null);
                ((ImageProducingTreeNode)item.getEntity()).setImage(joined);
                return joined;
            }
        }
    }
    private static final Component component = new Component() {};
    private static final MediaTracker tracker = new MediaTracker(component);
    /** combine two images.
     * @param ico
     * @param file
     * @return
     */
    private Image combine(Image ico, Image file) {
        // make sure both images are loaded first.
        synchronized(tracker) {
            int id = getNextID();
            tracker.addImage(ico,id);
            tracker.addImage(file,id);
            
            try {
                tracker.waitForID(id); //waits until both images are loaded.
            } catch(InterruptedException e) {

            }
        }
        int icoWidth = ico.getWidth(null);
        int fileWidth = file.getWidth(null); 
        GraphicsConfiguration configuration = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        BufferedImage img = configuration.createCompatibleImage(icoWidth + fileWidth + MARGIN                
                ,Math.max(ico.getHeight(null),file.getHeight(null))
                ,Transparency.BITMASK
                );
       Graphics2D graphics = img.createGraphics();
       graphics.drawImage(ico,0,0,null);
       graphics.drawImage(file,icoWidth + MARGIN,0,null);
       graphics.dispose();
       return img;
    }
    private final static int MARGIN = 2;
    
    /**
     * Returns an ID to use with the MediaTracker in loading an image.
     */
    private int getNextID() {
        synchronized(tracker) {
            return ++mediaTrackerID;
        }
    }

    /**
     * Id used in loading images from MediaTracker.
     */
    private static int mediaTrackerID;
}

/* 
$Log: TextImageTreeNodeRenderer.java,v $
Revision 1.2  2009/03/26 18:04:10  nw
source code improvements - cleaned imports, @override, etc.

Revision 1.1  2007/12/12 13:54:14  nw
astroscope upgrade, and minor changes for first beta release

Revision 1.7  2007/05/03 19:20:42  nw
removed helioscope.merged into uberscope.

Revision 1.6  2007/01/29 10:43:49  nw
documentation fixes.

Revision 1.5  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.4.2.1  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.4  2006/02/23 12:27:26  KevinBenson
small change to make Search Results bigger

Revision 1.3  2006/02/23 09:30:33  KevinBenson
Added a small tablesortder for the JTable of servies and got rid of Color
for the nodesizing distant nodes.

Revision 1.2  2006/02/09 15:40:01  nw
finished refactoring of astroscope.
added vospec viewer

Revision 1.1  2006/02/02 14:51:11  nw
components of astroscope, plus new ssap component.
 
*/