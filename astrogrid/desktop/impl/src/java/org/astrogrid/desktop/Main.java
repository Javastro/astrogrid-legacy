/*$Id: Main.java,v 1.11 2008/11/04 14:35:52 nw Exp $
 * Created on 15-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop;

import org.astrogrid.VODesktop;

/** @deprecated use the main entry point {@link VODesktop} instead.
 * 
 * Delegtes to {@link Workbench1}
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 15-Mar-2005
 */
@Deprecated
public class Main  {
  

   
    public static final void main(final String[] args) {
    	/*
    	try {
        UIManager.setLookAndFeel(new PlasticXPLookAndFeel());
        UIManager.put(Options.USE_SYSTEM_FONTS_APP_KEY,    Boolean.TRUE);
    	} catch (Exception e) {
    		System.err.println("Failed to install plastic look and feel");
    		e.printStackTrace();
    	}
    	*/
    	VODesktop.main(args);
    }


}


/* 
$Log: Main.java,v $
Revision 1.11  2008/11/04 14:35:52  nw
javadoc polishing

Revision 1.10  2008/04/23 10:49:55  nw
removed obsolete entrypoints.

Revision 1.9  2008/03/05 10:55:43  nw
added progress reporting to splashscreen

Revision 1.8  2007/01/29 11:11:35  nw
updated contact details.

Revision 1.7  2006/05/26 15:08:09  nw
minor changes.

Revision 1.6  2006/04/21 13:48:11  nw
mroe code changes. organized impoerts to reduce x-package linkage.

Revision 1.5  2006/04/18 23:25:47  nw
merged asr development.

Revision 1.4.6.4  2006/04/14 02:45:03  nw
finished code.extruded plastic hub.

Revision 1.4.6.3  2006/04/04 10:31:26  nw
preparing to move to mac.

Revision 1.4.6.2  2006/03/28 13:47:35  nw
first webstartable version.

Revision 1.4.6.1  2006/03/22 18:01:31  nw
merges from head, and snapshot of development

Revision 1.4  2006/02/09 15:39:15  nw
tooltip improvements

Revision 1.3  2005/08/25 16:59:58  nw
1.1-beta-3

Revision 1.2  2005/08/16 13:19:32  nw
fixes for 1.1-beta-2

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.6  2005/08/05 11:46:56  nw
reimplemented acr interfaces, added system tests.

Revision 1.5  2005/07/08 14:06:30  nw
final fixes for the workshop.

Revision 1.4  2005/06/17 12:06:14  nw
added changelog, made start on docs.
fixed race condition.

Revision 1.3  2005/04/27 13:42:41  clq2
1082

Revision 1.2.2.1  2005/04/22 15:59:26  nw
made a star documenting desktop.

Revision 1.2  2005/04/13 12:59:10  nw
checkin from branch desktop-nww-998

Revision 1.1.4.2  2005/04/05 15:13:56  nw
changed default look and feel.

Revision 1.1.4.1  2005/03/18 12:09:31  nw
got framework, builtin and system levels working.
 
*/