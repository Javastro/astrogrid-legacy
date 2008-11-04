/*$Id: VizualizationController.java,v 1.2 2008/11/04 14:35:48 nw Exp $
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


import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.collections.iterators.UnmodifiableIterator;

import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.focus.FocusSet;
import edu.berkeley.guir.prefuse.graph.Entity;
import edu.berkeley.guir.prefuse.graph.TreeNode;
import edu.berkeley.guir.prefuse.render.ImageFactory;
/**
 * Aggregates a set of prefuse vizualizations together. - enables them to be worked
 * with as a whole, attached to a single vizualization model.
 * 
 *<p>
 *also contains resources, helper objects, etc that are shared between vizualizations.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 27-Jan-2006
 *
 */
public class VizualizationController {
    public VizualizationController(final VizModel model) {            
        this.model = model;
        imageFactory = new ImageFactory(24,24); // small thumbnails.        
    
    }
    private final ImageFactory imageFactory;    
    private final ArrayList l = new ArrayList();
    private final VizModel model;
    
    public void add(final Vizualization v) {
        l.add(v);
        model.getTree().addGraphEventListener(v);
    }
    
    /** access the common image factory.*/
    public ImageFactory getImageFactory() {
        return imageFactory;
    }
    
    public VizModel getVizModel() {
        return model;
    }
    
    public Iterator iterator() {
        return UnmodifiableIterator.decorate(l.iterator());
    }
    
    /**
     * method: refocusMainNodes()
     * description: Refocuses the graph on the main root node.  Typically called right before a query or user
     * hits a particular button.  Seems to focus better if it focuses on cone and sia tree nodes first before root.
     *
     */
    public void refocusMainNodes() {
        for (final Iterator i = this.iterator(); i.hasNext(); ) {
            final Vizualization vis = (Vizualization)i.next();
            final ItemRegistry itemReg = vis.getItemRegistry();
            final FocusSet defaultFocusSet = itemReg.getFocusManager().getDefaultFocusSet();
            for (final Iterator j = model.getProtocols().iterator(); j.hasNext(); ) {
                final DalProtocol p = (DalProtocol)j.next();
                defaultFocusSet.set(p.getPrimaryNode());
            }  
            defaultFocusSet.set(model.getTree().getRoot());
            gc(itemReg);
        }    
    }

    /**
    force a garbage collection too - will remove any old visual nodes lurking after the real nodes have been removed.
     * @param itemReg
     */
    private void gc(final ItemRegistry itemReg) {
        itemReg.garbageCollectAggregates();
        itemReg.garbageCollectEdges();
        itemReg.garbageCollectNodes();
    }
    
    public void moveUp() {
        for (final Iterator i = this.iterator(); i.hasNext(); ) {
            final Vizualization v = (Vizualization)i.next();
            final ItemRegistry itemReg = v.getItemRegistry();
            final FocusSet focusSet = itemReg.getFocusManager().getDefaultFocusSet();
            final Iterator j = focusSet.iterator();
            if (j.hasNext()) {
                final Entity e = (Entity)j.next();
                if (e instanceof TreeNode) {
                    final TreeNode parent = ((TreeNode)e).getParent();
                    if (parent != null) {
                        focusSet.set(parent);
                    }
                }
            }
            gc(itemReg);
        }
    }
    
    /**
     * method: reDrawGraphs
     * description: Calls Redraw on all the displays/visualizations typically called after a refocus and needs to
     * draw the new focus on the graph.  
     * Note: clearTree does not call it because nodes are typically added from a query causing the graph to be 
     * redrawn anyways.
     *
     */
    public void reDrawGraphs() {
        for (final Iterator i = this.iterator(); i.hasNext(); ) {
            final Vizualization vis = (Vizualization)i.next();
            vis.reDraw();
        }
    }        
}

/* 
$Log: VizualizationController.java,v $
Revision 1.2  2008/11/04 14:35:48  nw
javadoc polishing

Revision 1.1  2007/12/12 13:54:12  nw
astroscope upgrade, and minor changes for first beta release

Revision 1.5  2007/01/29 10:43:49  nw
documentation fixes.

Revision 1.4  2006/09/15 14:37:20  nw
reduced size of thumbnails.

Revision 1.3  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.2.6.1  2006/04/14 02:45:00  nw
finished code.extruded plastic hub.

Revision 1.2  2006/02/09 15:40:01  nw
finished refactoring of astroscope.
added vospec viewer

Revision 1.1  2006/02/02 14:51:11  nw
components of astroscope, plus new ssap component.
 
*/