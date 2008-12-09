/*$Id: ApplicationLauncher.java,v 1.7 2008/12/09 21:20:20 nw Exp $
 * Created on 12-May-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr.ui;

/**AR Service: Launch a new Task Runner / Query Builder GUI
 * 
 * @service ui.applicationLauncher
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 12-May-2005
 *@see org.astrogrid.acr.astrogrid.Applications
 */
public interface ApplicationLauncher {
    /** display a new TaskRunner */
    public void show() ;
    /** Not implemented
     * @exclude 
     * @deprecated not implemented */
    @Deprecated
    public void hide();
}


/* 
$Log: ApplicationLauncher.java,v $
Revision 1.7  2008/12/09 21:20:20  nw
Complete - taskutil.tables is missing.

Revision 1.6  2008/09/25 16:02:09  nw
documentation overhaul

Revision 1.5  2007/07/17 17:05:28  nw
documentation fix.

Revision 1.4  2007/01/24 14:04:45  nw
updated my email address

Revision 1.3  2006/02/02 14:19:48  nw
fixed up documentation.

Revision 1.2  2005/08/12 08:45:15  nw
souped up the javadocs

Revision 1.1  2005/08/11 10:15:01  nw
finished split

Revision 1.2  2005/06/08 14:51:59  clq2
1111

Revision 1.1.2.1  2005/06/02 14:34:32  nw
first release of application launcher
 
*/