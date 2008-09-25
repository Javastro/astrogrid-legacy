/*$Id: MyspaceBrowser.java,v 1.7 2008/09/25 16:02:09 nw Exp $
 * Created on 07-Apr-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr.ui;


/**Control the  Myspace Browser UI.
 * @exclude 
 * @deprecated prefer filemanager
 * @service userInterface.myspaceBrowser
 * @todo add methods to open a particular location in myspace, force a refreshm and to dispose the exploer
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 07-Apr-2005
 *@see org.astrogrid.acr.astrogrid.Myspace
 */
@Deprecated
public interface MyspaceBrowser {
    /** show the explorer gui */
    public void show(); 
    /** hide the explorer gui */
    public void hide();
}


/* 
$Log: MyspaceBrowser.java,v $
Revision 1.7  2008/09/25 16:02:09  nw
documentation overhaul

Revision 1.6  2007/06/27 11:08:36  nw
public apis for new ui components.

Revision 1.5  2007/01/24 14:04:45  nw
updated my email address

Revision 1.4  2006/10/12 02:22:33  nw
fixed up documentaiton

Revision 1.3  2006/02/02 14:19:48  nw
fixed up documentation.

Revision 1.2  2005/08/12 08:45:15  nw
souped up the javadocs

Revision 1.1  2005/08/11 10:15:01  nw
finished split

Revision 1.4  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.3  2005/05/12 15:59:10  clq2
nww 1111 again

Revision 1.1.2.2  2005/05/11 11:55:19  nw
javadoc

Revision 1.1.2.1  2005/05/09 14:51:02  nw
renamed to 'myspace' and 'workbench'
added confirmation on app exit.

Revision 1.2  2005/04/27 13:42:41  clq2
1082

Revision 1.1.2.1  2005/04/25 11:18:51  nw
split component interfaces into separate package hierarchy
- improved documentation

Revision 1.2.2.1  2005/04/22 10:55:32  nw
implemented vospace file chooser dialogue.

Revision 1.2  2005/04/13 12:59:18  nw
checkin from branch desktop-nww-998

Revision 1.1.2.4  2005/04/13 12:23:29  nw
refactored a common base class for ui components
 
*/