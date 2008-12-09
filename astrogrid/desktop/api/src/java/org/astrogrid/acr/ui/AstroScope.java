/*$Id: AstroScope.java,v 1.5 2008/12/09 21:20:20 nw Exp $
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

/**AR Servuce: Launch a new AstroScope GUI
 * 
 * @service ui.astroscope
 * @author Kevin Benson
 */
public interface AstroScope {
//    * @todo add show(XQuery) and show(ResourceList)
//    * @todo also add hook into helioscope too?
    /** Display a new instance of All-VO Astroscope */
    public void show() ;
}


/* 
$Log: AstroScope.java,v $
Revision 1.5  2008/12/09 21:20:20  nw
Complete - taskutil.tables is missing.

Revision 1.4  2008/09/25 16:02:09  nw
documentation overhaul

Revision 1.3  2006/02/02 14:19:48  nw
fixed up documentation.

Revision 1.2  2005/11/24 01:18:42  nw
merged in final changes from release branch.

Revision 1.1.2.2  2005/11/23 18:07:22  nw
improved docs.

Revision 1.1.2.1  2005/11/23 04:32:54  nw
tidied up

Revision 1.1  2005/10/26 15:52:28  KevinBenson
new app for astroscope

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