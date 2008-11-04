/*$Id: FallbackBrowserControl.java,v 1.6 2008/11/04 14:35:54 nw Exp $
 * Created on 21-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.alternatives;


import java.net.MalformedURLException;
import java.net.URL;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.system.WebServer;
import org.astrogrid.desktop.modules.dialogs.ResultDialog;

/** Fallback implementation of the browser control. 
 * 
 * used when not running under webstart (i.e. during development)- tells user which http url to go to.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 21-Feb-2005
 *
 */
public class FallbackBrowserControl implements BrowserControl {

    /** Construct a new FallbackBrowserControl
     * 
     */
    public FallbackBrowserControl(final WebServer root) {
        super();
        this.root = root;
    }

    protected final WebServer root;
    /**
     * @throws MalformedURLException
     * @throws ACRException
     */
    public void openURL(final String stringUrl) throws MalformedURLException, ACRException {
        final URL url = new URL(stringUrl);
        openURL(url);
    }

    /**
     * @see org.astrogrid.acr.system.BrowserControl#openURL(java.net.URL)
     */
    public void openURL(final URL url) throws ACRException {
       final ResultDialog rd = new ResultDialog("Cannot control browser\n Please go to \n" + url.toString());
       rd.show();
    }
    
    /**
     * @throws MalformedURLException
     * @throws ACRException
     * @see org.astrogrid.acr.system.BrowserControl#openRelative(java.lang.String)
     */
    public void openRelative(final String relativeURL) throws ACRException {
        try {
        final URL url = new URL(root.getUrlRoot()+ relativeURL);
        openURL(url);
        } catch (final MalformedURLException e) {
            throw new InvalidArgumentException(e);
        }
    }


}


/* 
$Log: FallbackBrowserControl.java,v $
Revision 1.6  2008/11/04 14:35:54  nw
javadoc polishing

Revision 1.5  2007/11/21 07:55:39  nw
Complete - task 65: Replace modal dialogues

Revision 1.4  2007/06/18 16:19:39  nw
javadoc fixes.

Revision 1.3  2007/01/29 11:11:37  nw
updated contact details.

Revision 1.2  2006/04/18 23:25:45  nw
merged asr development.

Revision 1.1.2.1  2006/04/14 02:45:03  nw
finished code.extruded plastic hub.

Revision 1.2  2005/09/02 14:03:34  nw
javadocs for impl

Revision 1.1  2005/08/11 10:15:00  nw
finished split

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