/*$Id: IconHelper.java,v 1.2 2005/09/02 14:03:34 nw Exp $
 * Created on 06-Apr-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.icons;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.URL;

import javax.swing.ImageIcon;

/** class of helper methods for locating and loading icons.
 * @author Noel Winstanley nw@jb.man.ac.uk 06-Apr-2005
 *
 */
public class IconHelper {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(IconHelper.class);

    /** Construct a new IconHelper
     * 
     */
    private IconHelper() {
        super();
    }
    
    /** load an icon,
     * @todo extend to find icons from elsewhere as needed.
     * @param imgName filename of the icon, which must be located in this package (<tt>org.astrogrid.desktop.icons</tt>)
     * @return the icon object, or null if not found
     */
    public static ImageIcon loadIcon(String imgName) {
        URL u = IconHelper.class.getResource(imgName);
        if (u != null) {
            return new ImageIcon(u);
        } else {
            logger.warn("Failed to find " + imgName);
            return null;
        }
    }

}


/* 
$Log: IconHelper.java,v $
Revision 1.2  2005/09/02 14:03:34  nw
javadocs for impl

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.2  2005/04/13 12:59:11  nw
checkin from branch desktop-nww-998

Revision 1.1.2.1  2005/04/06 15:03:56  nw
added new front end - more modern, with lots if icons.
 
*/