/*$Id: Workbench.java,v 1.4 2006/10/11 10:34:34 nw Exp $
 * Created on 04-Jul-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid;

import org.astrogrid.desktop.SplashWindow;

/** Entry point for launching the workbench.
 * @author Noel Winstanley nw@jb.man.ac.uk 04-Jul-2005
 */
public class Workbench {

    /** Construct a new Workbench
     * 
     */
    public Workbench() {
        super();
    }
    
    public static void main(String[] args) {

        SplashWindow.splash(Workbench.class.getResource("workbench-splash.gif"));
        SplashWindow.invokeMain("org.astrogrid.Workbench1", args);
        SplashWindow.disposeSplash();
    }

}


/* 
$Log: Workbench.java,v $
Revision 1.4  2006/10/11 10:34:34  nw
revamped all the splash screens.

Revision 1.3  2006/06/15 09:38:16  nw
made exeuciton classes more versatile

Revision 1.2  2006/04/18 23:25:43  nw
merged asr development.

Revision 1.1.2.1  2006/04/14 02:45:02  nw
finished code.extruded plastic hub.

Revision 1.1.68.3  2006/04/04 10:31:26  nw
preparing to move to mac.

Revision 1.1.68.2  2006/03/28 13:47:35  nw
first webstartable version.

Revision 1.1.68.1  2006/03/22 18:01:31  nw
merges from head, and snapshot of development

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/07/08 11:08:02  nw
bug fixes and polishing for the workshop
 
*/