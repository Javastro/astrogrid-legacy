/*$Id: TextImageItemSizeRenderer.java,v 1.7 2007/05/03 19:20:42 nw Exp $
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


import java.awt.Font;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.render.TextImageItemRenderer;
/**
 * renderer for node that produces a text and image, where the text varies in size.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 27-Jan-2006
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
        String offset = item.getAttribute(Retriever.OFFSET_ATTRIBUTE);
        String font = item.getAttribute(Retriever.FONT_ATTRIBUTE);
        if(font != null) {
            item.setFont(Font.decode(font));
        }
        NodeSizing nodeSizing = nodeSizingMap.getNodeSizing(offset);
            if(nodeSizing != null) {
                //Font currentFont = item.getFont();
                //Font currentColor = item.getColor();                
                item.setFont(nodeSizing.getFont());
                //item.setColor(nodeSizing.getColor());
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
    
    private String sizeAttributeName;

	public void setSizeAttributeName(String sizeAttributeName) {
		this.sizeAttributeName = sizeAttributeName;
	}
	

	public String getSizeAttributeName() {
		return this.sizeAttributeName;
	}
	
	protected String getText(VisualItem arg0) {
		String orig = super.getText(arg0);
		if (sizeAttributeName == null) {
			return orig;
		}
		String sz = arg0.getAttribute(sizeAttributeName);
		if (sz == null) {
			return orig;
		} else {
			return orig + " - " + sz + " results";
		}
	}
}

/* 
$Log: TextImageItemSizeRenderer.java,v $
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