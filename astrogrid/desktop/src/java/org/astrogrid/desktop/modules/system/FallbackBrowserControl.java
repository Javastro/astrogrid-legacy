/*$Id: FallbackBrowserControl.java,v 1.7 2005/08/05 11:46:55 nw Exp $
 * Created on 21-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system;


import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.system.WebServer;
import org.astrogrid.desktop.modules.dialogs.ResultDialog;

import java.net.MalformedURLException;
import java.net.URL;

/** fallback implementation of the browser control - tells user which http url to go to.
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Feb-2005
 *
 */
public class FallbackBrowserControl implements BrowserControl {

    /** Construct a new FallbackBrowserControl
     * 
     */
    public FallbackBrowserControl(WebServer root) {
        super();
        this.root = root;
    }

    protected final WebServer root;
    /**
     * @throws MalformedURLException
     * @throws ACRException
     * @see org.astrogrid.acr.system.BrowserControl#openURL(java.lang.String)
     */
    public void openURL(String stringUrl) throws MalformedURLException, ACRException {
        URL url = new URL(stringUrl);
        openURL(url);
    }

    /**
     * @see org.astrogrid.acr.system.BrowserControl#openURL(java.net.URL)
     */
    public void openURL(URL url) throws ACRException {
       ResultDialog rd = new ResultDialog(null,"Cannot control browser\n Please go to \n" + url.toString());
       rd.show();
    }
    
    /**
     * @throws MalformedURLException
     * @throws ACRException
     * @see org.astrogrid.acr.system.BrowserControl#openRelative(java.lang.String)
     */
    public void openRelative(String relativeURL) throws MalformedURLException, ACRException {
        URL url = new URL(root.getUrlRoot()+ relativeURL);
        openURL(url);
    }


}


/* 
$Log: FallbackBrowserControl.java,v $
Revision 1.7  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.6  2005/06/08 14:51:59  clq2
1111

Revision 1.3.8.2  2005/06/02 14:34:32  nw
first release of application launcher

Revision 1.3.8.1  2005/05/11 11:55:30  nw
removed unused interface.

Revision 1.3  2005/04/27 13:42:41  clq2
1082

Revision 1.2.2.1  2005/04/25 11:18:51  nw
split component interfaces into separate package hierarchy
- improved documentation

Revision 1.2  2005/04/13 12:59:12  nw
checkin from branch desktop-nww-998

Revision 1.1.2.1  2005/03/18 12:09:31  nw
got framework, builtin and system levels working.

Revision 1.1  2005/02/22 01:10:31  nw
enough of a prototype here to do a show-n-tell on.
 
*/