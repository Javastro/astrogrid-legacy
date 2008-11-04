/*$Id: Vizualization.java,v 1.10 2008/11/04 14:35:48 nw Exp $
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
/** abstract class for a prefuse vizualization.
 * Acts as a view onto the shared {@code VizModel}
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Nov 4, 200811:02:23 AM
 */
public abstract class Vizualization extends GraphEventAdapter {
        public Vizualization(final String name, final VizualizationController vizs) {
            this.name = name;   
            this.vizs = vizs;
        }
        public final String getName() {
            return name;
        }
        final String name;
        protected final VizualizationController vizs;
    ItemRegistry itemRegistry;
   /** create default node renderer */
    private TextImageItemRenderer nodeRenderer;
    protected final TextImageItemRenderer getTextRenderer() {
        if (nodeRenderer == null) {
            final TextImageTreeNodeRenderer renderer = new TextImageTreeNodeRenderer(vizs.getVizModel().getNodeSizingMap()) ;
            renderer.setMaxTextWidth(150);
            renderer.setRoundedCorner(8,8);
            renderer.setTextAttributeName(AbstractRetriever.LABEL_ATTRIBUTE);
            renderer.setSizeAttributeName(AbstractRetriever.RESULT_COUNT);
            renderer.setImageAttributeName(AbstractRetriever.SERVICE_LOGO_ATTRIBUTE);
            renderer.setMaxImageDimensions(20,20);
            renderer.setAbbrevType(StringAbbreviator.TRUNCATE);
            renderer.setImageFactory(vizs.getImageFactory());
            nodeRenderer = renderer;

        }
        return nodeRenderer;
    }
    
    protected final ItemRegistry getItemRegistry() {
        if (itemRegistry == null) {
            itemRegistry = new ItemRegistry(vizs.getVizModel().getTree());
            itemRegistry.getFocusManager().putFocusSet(FocusManager.SELECTION_KEY,vizs.getVizModel().getSelectionFocusSet());
            
            final DefaultEdgeRenderer edgeRenderer = new DefaultEdgeRenderer();
            edgeRenderer.setWeightAttributeName(AbstractRetriever.WEIGHT_ATTRIBUTE);
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
Revision 1.10  2008/11/04 14:35:48  nw
javadoc polishing

Revision 1.9  2008/04/25 08:59:36  nw
extracted interface from retriever, to ease unit testing.

Revision 1.8  2007/12/12 13:54:12  nw
astroscope upgrade, and minor changes for first beta release

Revision 1.7  2007/05/13 12:00:48  nw
changes for ivoa.

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