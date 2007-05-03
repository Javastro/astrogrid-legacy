/*$Id: Vizualization.java,v 1.6 2007/05/03 19:20:42 nw Exp $
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



import edu.berkeley.guir.prefuse.Display;
import edu.berkeley.guir.prefuse.FocusManager;
import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.graph.event.GraphEventAdapter;
import edu.berkeley.guir.prefuse.render.DefaultEdgeRenderer;
import edu.berkeley.guir.prefuse.render.DefaultRendererFactory;
import edu.berkeley.guir.prefuse.render.TextImageItemRenderer;
import edu.berkeley.guir.prefuse.util.StringAbbreviator;

public abstract class Vizualization extends GraphEventAdapter {
        public Vizualization(String name, VizualizationManager vizs) {
            this.name = name;   
            this.vizs = vizs;
        }
        public final String getName() {
            return name;
        }
        final String name;
        protected final VizualizationManager vizs;
    ItemRegistry itemRegistry;
   /** create default node renderer */
    private TextImageItemSizeRenderer nodeRenderer;
    protected final TextImageItemRenderer getTextRenderer() {
        if (nodeRenderer == null) {
            nodeRenderer = new TextImageItemSizeRenderer(vizs.getVizModel().getNodeSizingMap()) ;
            nodeRenderer.setMaxTextWidth(150);
            nodeRenderer.setRoundedCorner(8,8);
            nodeRenderer.setTextAttributeName(Retriever.LABEL_ATTRIBUTE);
            nodeRenderer.setSizeAttributeName(Retriever.RESULT_COUNT);
            nodeRenderer.setImageAttributeName(Retriever.SERVICE_LOGO_ATTRIBUTE);
            nodeRenderer.setMaxImageDimensions(20,20);//@fixme doesn;'t seem to make any difference.
            nodeRenderer.setAbbrevType(StringAbbreviator.TRUNCATE);
            nodeRenderer.setImageFactory(vizs.getImageFactory());

        }
        return nodeRenderer;
    }
    
    protected final ItemRegistry getItemRegistry() {
        if (itemRegistry == null) {
            itemRegistry = new ItemRegistry(vizs.getVizModel().getTree());
            itemRegistry.getFocusManager().putFocusSet(FocusManager.SELECTION_KEY,vizs.getVizModel().getSelectionFocusSet());
            
            DefaultEdgeRenderer edgeRenderer = new DefaultEdgeRenderer();
            edgeRenderer.setWeightAttributeName(Retriever.WEIGHT_ATTRIBUTE);
            edgeRenderer.setWeightType(DefaultEdgeRenderer.WEIGHT_TYPE_LINEAR);
            itemRegistry.setRendererFactory(new DefaultRendererFactory(getTextRenderer(), edgeRenderer));            
        }
        return itemRegistry;
    }
    /** access the display for this vizualization */
    public abstract Display getDisplay();
    
    /** access the display for this vizualization */
    public abstract void reDraw();
    
    }

/* 
$Log: Vizualization.java,v $
Revision 1.6  2007/05/03 19:20:42  nw
removed helioscope.merged into uberscope.

Revision 1.5  2006/08/31 21:31:37  nw
minor tweaks and doc fixes.

Revision 1.4  2006/08/15 09:58:49  nw
tried to reduce size of service icons. doesn't seem to work.

Revision 1.3  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.2.6.1  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.2  2006/02/09 15:40:01  nw
finished refactoring of astroscope.
added vospec viewer

Revision 1.1  2006/02/02 14:51:11  nw
components of astroscope, plus new ssap component.
 
*/