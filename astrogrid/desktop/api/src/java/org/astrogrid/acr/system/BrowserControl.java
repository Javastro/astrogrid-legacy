/*$Id: BrowserControl.java,v 1.7 2008/09/25 16:02:03 nw Exp $
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

import java.net.URL;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.InvalidArgumentException;

/** AR Service: Control the desktop webbrowser.
 * 
 * Trivial interface to display a URL in the desktop's webbrowser.
 * @service system.browser


 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 01-Feb-2005
 * @see #openURL Example of use
 */
public interface BrowserControl {
    /**Display a page in the system webbrowser.
     * 
     * {@example "Python Example"
     *  # connect to the AR
     * from xmlrpclib import Server
     * from os.path import expanduser
     * ar = Server(file(expanduser('~/.astrogrid-desktop')).next().strip() +'xmlrpc')
     *  #call this function
     * ar.system.browser.openURL('http://www.ivoa.net')
     * } 
     * 
     * {@example "Java Example"   
     * // connect to the AR
     * ACR ar = new Finder().find();
     * // find this component   
     * BrowserControl bc = (BrowserControl) ar.getService(BrowserControl.class);
     *  // call this function
     * bc.openURL(new URL("http://www.ivoa.net"));
     * }
 * @param url URL of the resource to display
 * @throws ACRException if the system webbrowser cannot be launched. Will not throw if after launch the  webbrowser then fails to 
 * display the page
 */

    
    void openURL(URL url) throws ACRException;

    /** Display a page from the internal AR webserver in the system webbrowser
     * @param relativeURL relative url from root of AR webserver context
     * @throws ACRException if the system webbrowser cannot be launched. Will not throw if the webbrowser then fails to 
 * display the page
     * @throws InvalidArgumentException if valid URL can't be build from parameter <tt>relativeURL</tt>
     * @see WebServer#getUrlRoot()
     */
    void openRelative(String relativeURL) throws ACRException, InvalidArgumentException;
}


/* 
$Log: BrowserControl.java,v $
Revision 1.7  2008/09/25 16:02:03  nw
documentation overhaul

Revision 1.6  2007/11/12 13:36:27  pah
change parameter name to structure

Revision 1.5  2007/01/24 14:04:44  nw
updated my email address

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