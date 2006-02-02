/*$Id: WindowedRadialColorFunction.java,v 1.1 2006/02/02 14:51:11 nw Exp $
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

import edu.berkeley.guir.prefuse.AggregateItem;
import edu.berkeley.guir.prefuse.EdgeItem;
import edu.berkeley.guir.prefuse.NodeItem;
import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.action.assignment.ColorFunction;
import edu.berkeley.guir.prefuse.util.ColorMap;

import java.awt.Color;
import java.awt.Paint;
/**
 * colorer that determines color of elemnts in astroscope vizualizations
 * @author Noel Winstanley nw@jb.man.ac.uk 27-Jan-2006
 *
 */
public class WindowedRadialColorFunction extends ColorFunction {
    private Color graphEdgeColor = Color.LIGHT_GRAY;
     private Color highlightColor = new Color(50,50,255);
     private Color focusColor = new Color(255,50,50);
     private Color selectedColor = Color.YELLOW;
     private ColorMap colorMap;

    public WindowedRadialColorFunction(int thresh) {
        colorMap = new ColorMap(
            ColorMap.getInterpolatedMap(thresh+1, Color.RED, Color.BLACK),
            0, thresh);
    } //
           
    public Paint getFillColor(VisualItem item) {
        if ( item instanceof NodeItem ) {
            //NodeItem n = (NodeItem)item;
            String attr=  item.getAttribute("selected");                
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

    public Paint getColor(VisualItem item) {
        if ( item.isFocus() ) {
            return focusColor;
        } else if ( item.isHighlighted() ) {
             return highlightColor;
         } else if (item instanceof NodeItem) {
             int d = ((NodeItem)item).getDepth();
             return colorMap.getColor(d);
        } else if (item instanceof EdgeItem) {
            EdgeItem e = (EdgeItem) item;
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
Revision 1.1  2006/02/02 14:51:11  nw
components of astroscope, plus new ssap component.
 
*/