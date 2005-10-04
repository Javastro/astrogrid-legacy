/*$Id: DataScopeLauncher.java,v 1.1 2005/10/04 20:36:30 KevinBenson Exp $
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

/** Interface to the Application Launcher GUI
 * <p>
 * <img src="doc-files/applauncher.png">
 * @service userInterface.applicationLauncher
 * @author Noel Winstanley nw@jb.man.ac.uk 12-May-2005
 *@see org.astrogrid.acr.astrogrid.Applications
 */
public interface DataScopeLauncher {
    /** display the application launcher gui */
    public void show() ;
    /** hide the application launcher window */
    public void hide();
}


/* 
$Log: DataScopeLauncher.java,v $
Revision 1.1  2005/10/04 20:36:30  KevinBenson
new small app for datascope.

Revision 1.2  2005/08/12 08:45:15  nw
souped up the javadocs

Revision 1.1  2005/08/11 10:15:01  nw
finished split

Revision 1.2  2005/06/08 14:51:59  clq2
1111

Revision 1.1.2.1  2005/06/02 14:34:32  nw
first release of application launcher
 
*/