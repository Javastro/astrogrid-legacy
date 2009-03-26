/*$Id: WindowedRadialColorFunction.java,v 1.5 2009/03/26 18:04:10 nw Exp $
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

import java.awt.Color;
import java.awt.Paint;

import edu.berkeley.guir.prefuse.AggregateItem;
import edu.berkeley.guir.prefuse.EdgeItem;
import edu.berkeley.guir.prefuse.NodeItem;
import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.action.assignment.ColorFunction;
import edu.berkeley.guir.prefuse.util.ColorMap;
/**
 *  Prefuse colorer that determines color of elemnts in astroscope vizualizations.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 27-Jan-2006
 *
 */
public class WindowedRadialColorFunction extends ColorFunction {
    private final Color graphEdgeColor = Color.LIGHT_GRAY;
     private final Color highlightColor = new Color(50,50,255);
     private final Color focusColor = new Color(255,50,50);
     private final Color selectedColor = Color.YELLOW;
     private final ColorMap colorMap;

    public WindowedRadialColorFunction(final int thresh) {
        colorMap = new ColorMap(
            ColorMap.getInterpolatedMap(thresh+1, Color.RED, Color.BLACK),
            0, thresh);
    } //
           
    @Override
    public Paint getFillColor(final VisualItem item) {
        if ( item instanceof NodeItem ) {
            //NodeItem n = (NodeItem)item;
            final String attr=  item.getAttribute("selected");                
            if (attr != null && attr.equals("true")) {
                return selectedColor;
            } else {               
                return Color.WHITE;
            }
        } else if ( item instanceof AggregateItem ) {
            return Color.LIGHT_GRAY;
        } else if ( item instanceof EdgeItem ) {
            return getColor(item);
        } else {
            return Color.BLACK;
        }
    } //

    @Override
    public Paint getColor(final VisualItem item) {
        if ( item.isFocus() ) {
            return focusColor;
        } else if ( item.isHighlighted() ) {
             return highlightColor;
         } else if (item instanceof NodeItem) {
             final int d = ((NodeItem)item).getDepth();
             return colorMap.getColor(d);
        } else if (item instanceof EdgeItem) {
            final EdgeItem e = (EdgeItem) item;
            if ( e.isTreeEdge() ) {
                int d, d1, d2;
                 d1 = ((NodeItem)e.getFirstNode()).getDepth();
                 d2 = ((NodeItem)e.getSecondNode()).getDepth();
                 d = Math.max(d1, d2);
                 return colorMap.getColor(d);
            } else {
                return graphEdgeColor;
            }
        } else {
            return Color.BLACK;
        }
    } //
}

/* 
$Log: WindowedRadialColorFunction.java,v $
Revision 1.5  2009/03/26 18:04:10  nw
source code improvements - cleaned imports, @override, etc.

Revision 1.4  2008/11/04 14:35:48  nw
javadoc polishing

Revision 1.3  2007/01/29 10:43:49  nw
documentation fixes.

Revision 1.2  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.1.6.1  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.1  2006/02/02 14:51:11  nw
components of astroscope, plus new ssap component.
 
*/