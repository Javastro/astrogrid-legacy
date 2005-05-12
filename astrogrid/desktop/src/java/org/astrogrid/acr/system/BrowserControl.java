/*$Id: BrowserControl.java,v 1.3 2005/05/12 15:37:44 clq2 Exp $
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

/** Display a resource in the system web browser
 * 
 * @todo implement using JDIC, or for platforms that don't support thisimplement a browser control that fires off http://jrex.mozdev.org/ - java implementaiton of mozilla.
 *  - launch this as an optional plugin in jnlp.

 * @author Noel Winstanley nw@jb.man.ac.uk 01-Feb-2005
 *
 */
public interface BrowserControl {
    /** open an absolute url */
    void openURL(String url) throws Exception;
    /** open an absolute url */
    void openURL(URL url) throws Exception;
    /** open a url within the ACR webserver context */
    void openRelative(String relativeURL) throws Exception;
}


/* 
$Log: BrowserControl.java,v $
Revision 1.3  2005/05/12 15:37:44  clq2
nww 1111

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