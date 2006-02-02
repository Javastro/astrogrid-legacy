/*$Id: TextImageItemSizeRenderer.java,v 1.1 2006/02/02 14:51:11 nw Exp $
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


import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.render.TextImageItemRenderer;

import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.util.Map;
/**
 * renderer for node that produces a text and image, where the text varies in size.
 * @author Noel Winstanley nw@jb.man.ac.uk 27-Jan-2006
 *
 */
public class TextImageItemSizeRenderer extends TextImageItemRenderer {
    
    public TextImageItemSizeRenderer(NodeSizingMap m) {
        super();
        this.nodeSizingMap = m;
    }
    private final NodeSizingMap nodeSizingMap;
    
    protected Shape getRawShape(VisualItem item) {
        //System.out.println("getRawShape called and nodsizing size = " + nodeSizingMap.size());
        String offset = item.getAttribute("offset");            
        NodeSizing nodeSizing = nodeSizingMap.getNodeSizing(offset);
            if(nodeSizing != null) {
                //Font currentFont = item.getFont();
                //Font currentColor = item.getColor();                
                item.setFont(nodeSizing.getFont());
                item.setColor(nodeSizing.getColor());
                double extraSize = nodeSizing.getSize();
                super.getRawShape(item);                    
                if ( m_imageBox instanceof RoundRectangle2D ) {
                    ((RoundRectangle2D)m_imageBox)
                        .setRoundRect(((RoundRectangle2D)m_imageBox).getX(),((RoundRectangle2D)m_imageBox).getY(),
                        ((RoundRectangle2D)m_imageBox).getWidth() + extraSize ,((RoundRectangle2D)m_imageBox).getHeight()  + extraSize,
                        ((RoundRectangle2D)m_imageBox).getArcWidth(),((RoundRectangle2D)m_imageBox).getArcHeight());
                } else {
                    m_imageBox.setFrame(m_imageBox.getX(),m_imageBox.getY(),m_imageBox.getWidth() + extraSize ,m_imageBox.getHeight()  + extraSize);
                }                    
            }else {
                super.getRawShape(item);
            }
        return m_imageBox;            
    }
}

/* 
$Log: TextImageItemSizeRenderer.java,v $
Revision 1.1  2006/02/02 14:51:11  nw
components of astroscope, plus new ssap component.
 
*/