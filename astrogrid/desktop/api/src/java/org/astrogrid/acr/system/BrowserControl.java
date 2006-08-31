/*$Id: BrowserControl.java,v 1.4 2006/08/31 20:23:09 nw Exp $
 * Created on 01-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr.system;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.InvalidArgumentException;

import java.net.MalformedURLException;
import java.net.URL;

/** Control the desktop webbrowser.
 * 
 * Trivial interface to display a URL in the desktop's webbrowser (relies on this being correclty configured in java web start 
 * console - mostly fine for windows, sometimes needs to be configured by hand on unixes.)
 * @service system.browser


 * @author Noel Winstanley nw@jb.man.ac.uk 01-Feb-2005
 *
 */
public interface BrowserControl {
    /**Display a page in the system webbrowser
 * @param url URL of the resource to display
 * @throws ACRException if the system webbrowser cannot be launched. Will not throw if the webbrowser then fails to 
 * display the page
 */

    
    void openURL(URL url) throws ACRException;

    /** Display a page from the internal ACR webserver in the system webbrowser
     * @param relativeURL relative url from root of ACR webserver context
     * @throws ACRException if the system webbrowser cannot be launched. Will not throw if the webbrowser then fails to 
 * display the page
     * @throws InvalidArgumentException if valid URL can't be build from parameter <tt>relativeURL</tt>
     * @see WebServer#getUrlRoot()
     */
    void openRelative(String relativeURL) throws ACRException, InvalidArgumentException;
}


/* 
$Log: BrowserControl.java,v $
Revision 1.4  2006/08/31 20:23:09  nw
doc fix.

Revision 1.3  2006/02/02 14:19:47  nw
fixed up documentation.

Revision 1.2  2005/08/12 08:45:15  nw
souped up the javadocs

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.5  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.4  2005/05/12 15:59:12  clq2
nww 1111 again

Revision 1.2.8.1  2005/05/11 11:55:19  nw
javadoc

Revision 1.2  2005/04/27 13:42:41  clq2
1082

Revision 1.1.2.1  2005/04/25 11:18:51  nw
split component interfaces into separate package hierarchy
- improved documentation

Revision 1.2  2005/04/13 12:59:12  nw
checkin from branch desktop-nww-998

Revision 1.1.2.1  2005/03/18 12:09:31  nw
got framework, builtin and system levels working.

Revision 1.2  2005/02/22 01:10:31  nw
enough of a prototype here to do a show-n-tell on.

Revision 1.1  2005/02/21 11:25:07  nw
first add to cvs
 
*/