/**
 * 
 */
package org.astrogrid.desktop.modules.ui.comp;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jun 18, 20071:20:05 AM
 * 
 * cribbed from http://forum.java.sun.com/thread.jspa?threadID=740142&messageID=4245988
 */
public class CombinedIcon implements Icon
{
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    
    public CombinedIcon(Icon[] is)
    {
        this(is, HORIZONTAL);
    }
    
    public CombinedIcon(Icon[] is, int orientation)
    {
        assert (orientation == HORIZONTAL) || (orientation == VERTICAL) : "Invalid orientation";
        this.is = is;
        orientation_ = orientation;
    }
    
    public int getIconHeight()
    {
        if (orientation_ == VERTICAL)
        {
        	int sum = 0;
        	for (int i = 0; i < is.length; i++) {
        		sum += is[i].getIconHeight();
        	}
            return sum;
        }
        else
        {
        	int max = is[0].getIconHeight();
        	for (int i =1; i < is.length; i++) {
        		max = Math.max(max,is[i].getIconHeight());
        	}
        	return max;
        }
    }
    
    public int getIconWidth()
    {
        if (orientation_ == VERTICAL)
        {
        	int sum = 0;
        	for (int i = 0; i < is.length; i++) {
        		sum += is[i].getIconWidth();
        	}
            return sum;
        }
        else
        {
        	int max = is[0].getIconWidth();
        	for (int i =1; i < is.length; i++) {
        		max = Math.max(max,is[i].getIconWidth());
        	}
        	return max;
        }
    }
    
    public void paintIcon(Component c, Graphics g, int x, int y)
    {
        if (orientation_ == VERTICAL)
        {
            int maxWidth = getIconWidth();
            int heightSoFar = 0;
            for (int i = 0; i < is.length; i++) {
				is[i].paintIcon(c,g, x + (maxWidth - is[i].getIconWidth())/2,y + heightSoFar );
				heightSoFar += is[i].getIconHeight();
			}
       }
        else
        {
            int maxHeight = getIconHeight();
            int widthSoFar = 0;
            for (int i = 0; i < is.length; i++) {
				is[i].paintIcon(c,g, x + widthSoFar, y + (maxHeight - is[i].getIconHeight())/2);
				widthSoFar += is[i].getIconWidth();
			}            
         }
    }
    
    private Icon[] is;
    private int orientation_;
}
