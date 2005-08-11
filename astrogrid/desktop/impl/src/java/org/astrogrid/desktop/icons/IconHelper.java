/*$Id: IconHelper.java,v 1.1 2005/08/11 10:15:00 nw Exp $
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
    
    /** load an icon, from classpath
     * @todo extend to find icons from elsewhere as needed.
     * @param img
     * @return
     */
    public static ImageIcon loadIcon(String img) {
        URL u = IconHelper.class.getResource(img);
        if (u != null) {
            return new ImageIcon(u);
        } else {
            logger.warn("Failed to find " + img);
            return null;
        }
    }

}


/* 
$Log: IconHelper.java,v $
Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.2  2005/04/13 12:59:11  nw
checkin from branch desktop-nww-998

Revision 1.1.2.1  2005/04/06 15:03:56  nw
added new front end - more modern, with lots if icons.
 
*/