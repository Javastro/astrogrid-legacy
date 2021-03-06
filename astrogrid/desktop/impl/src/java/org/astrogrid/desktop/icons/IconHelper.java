/*$Id: IconHelper.java,v 1.14 2009/04/06 17:04:03 nw Exp $
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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import javax.swing.ImageIcon;

import org.apache.commons.collections.map.ReferenceMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** Locates and loads icons.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 06-Apr-2005
 *@todo integrate with new cache system - means making this a service, rather than an object. but then could cache remote icons to disk.
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
    
    /** load an icon from a url.
       * @param iconUrl url of the object to load
     * @return the icon object, or null if not found.
     */
    public static ImageIcon loadIcon(final URL iconUrl) {
        return new ImageIcon(iconUrl);
    	
    	
    }
    
    /** load an icon,
     * @param imgName filename of the icon, which must be located in this package (<tt>org.astrogrid.desktop.icons</tt>)
     * @return the icon object, or null if not found
     */
    public static ImageIcon loadIcon(final String imgName) {
    	if (imgName == null) {
    		return null;
    	}
        if (cache.containsKey(imgName)) {
            return cache.get(imgName);
        }
        URL u = IconHelper.class.getResource(imgName);
        if (u != null) {
            final ImageIcon i = new ImageIcon(u);
            cache.put(imgName,i);
            return i;            
        } else {
            // maybe it's a url itself then... try downloading from this.
            try {
                u = new URL(imgName);
                return loadIcon(u);
            } catch (final MalformedURLException x) {
                // fall through
            }
            logger.warn("Failed to find " + imgName );
            return null;
        }
    }
    
    /** cache for icons - hard references for keys, weak references for values. 
     * @modified nww - corrected from WEAK to SOFT - otherwise it doesn't cache.*/
    protected static final Map<String, ImageIcon> cache = new ReferenceMap(ReferenceMap.HARD,ReferenceMap.SOFT);
  

}


/* 
$Log: IconHelper.java,v $
Revision 1.14  2009/04/06 17:04:03  nw
fixed hangover from webstart that was inefficient

Revision 1.13  2009/04/06 11:29:34  nw
added generic types

Revision 1.12  2008/12/01 23:29:55  nw
used commons.io utilities

Revision 1.11  2008/11/04 14:35:54  nw
javadoc polishing

Revision 1.10  2007/08/02 10:56:00  nw
Complete - task 47: Use XStream for persistence.

Revision 1.9  2007/06/18 16:23:52  nw
javadoc fix

Revision 1.8  2007/01/29 10:56:50  nw
added new icon for filechooser

Revision 1.7  2006/08/31 21:12:06  nw
doc fix.

Revision 1.6  2006/07/20 12:29:38  nw
added method to load remote icons.

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