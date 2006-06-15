/*$Id: IconHelper.java,v 1.5 2006/06/15 09:44:37 nw Exp $
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

import java.net.URL;
import java.util.Map;

import javax.swing.ImageIcon;

import org.apache.commons.collections.map.ReferenceMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
     * @param imgName filename of the icon, which must be located in this package (<tt>org.astrogrid.desktop.icons</tt>)
     * @return the icon object, or null if not found
     */
    public static ImageIcon loadIcon(String imgName) {
    	if (imgName == null) {
    		return null;
    	}
        if (cache.containsKey(imgName)) {
            return (ImageIcon)cache.get(imgName);
        }
        URL u = IconHelper.class.getResource(imgName);
        if (u != null) {
            ImageIcon i = new ImageIcon(u);
            cache.put(imgName,i);
            return i;
        } else {
            logger.warn("Failed to find " + imgName);
            return null;
        }
    }
    
    /** cache for icons - hard references for keys, weak references for values. 
     * @modified nww - corrected from WEAK to SOFT - otherwise it doesn't cache.*/
    protected static final Map cache = new ReferenceMap(ReferenceMap.HARD,ReferenceMap.SOFT);
  

}


/* 
$Log: IconHelper.java,v $
Revision 1.5  2006/06/15 09:44:37  nw
improvements coming from unit testing

Revision 1.4  2006/04/18 23:25:45  nw
merged asr development.

Revision 1.3.56.2  2006/04/14 02:45:03  nw
finished code.extruded plastic hub.

Revision 1.3.56.1  2006/04/04 10:31:26  nw
preparing to move to mac.

Revision 1.3  2005/09/12 15:21:16  nw
reworked application launcher. starting on workflow builder

Revision 1.2  2005/09/02 14:03:34  nw
javadocs for impl

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.2  2005/04/13 12:59:11  nw
checkin from branch desktop-nww-998

Revision 1.1.2.1  2005/04/06 15:03:56  nw
added new front end - more modern, with lots if icons.
 
*/