/*$Id: BrowserControl.java,v 1.1 2005/02/21 11:25:07 nw Exp $
 * Created on 01-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.ui;

import java.net.URL;

/** interface services have into the browser 
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Feb-2005
 *
 */
public interface BrowserControl {
    /** open an absolute url */
    void openURL(String url) throws Exception;
    /** open an absolute url */
    void openURL(URL url) throws Exception;
    /** open a url within the server context */
    void openRelative(String relativeURL) throws Exception;
}


/* 
$Log: BrowserControl.java,v $
Revision 1.1  2005/02/21 11:25:07  nw
first add to cvs
 
*/