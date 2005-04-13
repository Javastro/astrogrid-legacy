/*$Id: FallbackBrowserControl.java,v 1.2 2005/04/13 12:59:12 nw Exp $
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


import java.net.URL;

import javax.swing.JOptionPane;

/** fallback implementation of the browser control - tells user which http url to go to.
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Feb-2005
 *
 */
public class FallbackBrowserControl implements BrowserControl {

    /** Construct a new FallbackBrowserControl
     * 
     */
    public FallbackBrowserControl(UrlRoot root) {
        super();
        this.root = root;
    }

    protected final UrlRoot root;
    /**
     * @see org.astrogrid.desktop.modules.system.BrowserControl#openURL(java.lang.String)
     */
    public void openURL(String stringUrl) throws Exception{
        URL url = new URL(stringUrl);
        openURL(url);
    }

    /**@todo implement
     * @see org.astrogrid.desktop.modules.system.BrowserControl#openURL(java.net.URL)
     */
    public void openURL(URL url) throws Exception {
       JOptionPane.showMessageDialog(null,"Please go to \n" + url.toString(),"Could not control browser",JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * @see org.astrogrid.desktop.modules.system.BrowserControl#openRelative(java.lang.String)
     */
    public void openRelative(String relativeURL) throws Exception {
        URL url = new URL(root.getUrlRoot()+ relativeURL);
        openURL(url);
    }


}


/* 
$Log: FallbackBrowserControl.java,v $
Revision 1.2  2005/04/13 12:59:12  nw
checkin from branch desktop-nww-998

Revision 1.1.2.1  2005/03/18 12:09:31  nw
got framework, builtin and system levels working.

Revision 1.1  2005/02/22 01:10:31  nw
enough of a prototype here to do a show-n-tell on.
 
*/