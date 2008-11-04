/*$Id: NodeSizing.java,v 1.3 2008/11/04 14:35:48 nw Exp $
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
import java.awt.Font;
/**
 * Class that adjusts size of nodes in display.
 * @author Kevin Benson
 *
 *@modified nww - refactoered as an enumeration type - each kind of node sizing
 *is a constant, and so no more than 3 node sizing objects are ever created
 *
 *@todo at the moment, node sizing is computed over all services.
 *Not only is this quite expensive, it's also not clear whether it gives good results
 * - maybe node sizing by service would give a better presentation (would be more efficient too)
 */
public class NodeSizing {
    Color color;
    private double extraSize;
    Font font;
    private NodeSizing(final int constraint) {
        switch(constraint) {
           case 1:
               this.color = Color.BLACK;
               font = new Font(null,Font.BOLD,16);
               extraSize = 0;
           break;
           case 2:
               this.color = Color.BLACK;
               this.font = new Font(null,Font.PLAIN, 12);
               this.extraSize = 0;
           break;
           case 3:
               this.color = Color.BLACK;
               this.font = new Font(null,Font.PLAIN, 10);
               this.extraSize = 0;
           break;
           default:
               //hmmm not sure should be throw an illegalargumentexception?
               this.color = Color.BLACK;
               this.font = new Font(null,Font.PLAIN,10);
               this.extraSize = 0;
           break;
        }//switch            
    }        
    public Color getColor() { return this.color;}
    public Font getFont() { return this.font;}
    public double getSize() { return this.extraSize;}        
    
    public static  final NodeSizing LARGE_NODE = new NodeSizing(3);
    
    
    public static final NodeSizing MEDIUM_NODE = new NodeSizing(2);

    public static final NodeSizing SMALL_NODE = new NodeSizing(1);
    public static final NodeSizing []NODE_SIZING_ARRAY = {SMALL_NODE, MEDIUM_NODE, LARGE_NODE};        
}

/* 
$Log: NodeSizing.java,v $
Revision 1.3  2008/11/04 14:35:48  nw
javadoc polishing

Revision 1.2  2006/02/23 09:30:33  KevinBenson
Added a small tablesortder for the JTable of servies and got rid of Color
for the nodesizing distant nodes.

Revision 1.1  2006/02/02 14:51:11  nw
components of astroscope, plus new ssap component.
 
*/