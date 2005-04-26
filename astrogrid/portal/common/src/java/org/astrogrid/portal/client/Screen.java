/*
 * Created on 02-Apr-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.astrogrid.portal.client;

/**
 * @author jl99
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Screen {
    
    private int
    	availableHeight,
    	availableLeft,
    	availableTop,
    	availableWidth,
    	colorDepth,
    	height,
    	pixelDepth,
    	width ;

    /**
     * 
     */
    public Screen( int width
                 , int height
                 , int availableWidth
                 , int availableHeight
                 , int availableLeft
                 , int availableTop
                 , int colorDepth
                 , int pixelDepth ) {
        super();
        this.width = width ;
        this.height = height ;
        this.availableWidth = availableWidth ;
        this.availableHeight = availableHeight ;
        this.availableLeft = availableLeft ;
        this.availableTop = availableTop ;
        this.colorDepth = colorDepth ;
        this.pixelDepth = pixelDepth ;
    }
    
    

    /**
     * @return Returns the availableHeight.
     */
    public int getAvailableHeight() {
        return availableHeight;
    }
    /**
     * @return Returns the availableLeft.
     */
    public int getAvailableLeft() {
        return availableLeft;
    }
    /**
     * @return Returns the availableTop.
     */
    public int getAvailableTop() {
        return availableTop;
    }
    /**
     * @return Returns the availableWidth.
     */
    public int getAvailableWidth() {
        return availableWidth;
    }
    /**
     * @return Returns the colorDepth.
     */
    public int getColorDepth() {
        return colorDepth;
    }
    /**
     * @return Returns the height.
     */
    public int getHeight() {
        return height;
    }
    /**
     * @return Returns the pixelDepth.
     */
    public int getPixelDepth() {
        return pixelDepth;
    }
    /**
     * @return Returns the width.
     */
    public int getWidth() {
        return width;
    }
    
}
