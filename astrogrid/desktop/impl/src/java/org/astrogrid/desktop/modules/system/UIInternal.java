/*$Id: UIInternal.java,v 1.6 2007/01/29 10:49:19 nw Exp $
 * Created on 26-Jul-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system;

import java.awt.Component;

import org.astrogrid.acr.system.UI;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIComponent;

/**
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 26-Jul-2005
 *
 */
public interface UIInternal extends UI, UIComponent{

    /** get the AWT component that implements the user interface .
     * - used for centering new windows, dialogues, etc. */
    public Component getComponent();

    
    /** access the background executor
     * used in backgroundWorker. */
    public BackgroundExecutor getExecutor();
    
    /**wrap an arbitrary runnable as an  BackgroundWorker, which when run, will report progress in the ui.
     *used in background exector*/
    public BackgroundWorker wrap(Runnable r);
   

  public void showAboutDialog();
  
}


/* 
$Log: UIInternal.java,v $
Revision 1.6  2007/01/29 10:49:19  nw
removed unused method.

Revision 1.5  2006/06/27 10:39:33  nw
added new menus

Revision 1.4  2006/04/21 13:48:11  nw
mroe code changes. organized impoerts to reduce x-package linkage.

Revision 1.3  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.2.26.3  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.2.26.2  2006/04/04 10:31:26  nw
preparing to move to mac.

Revision 1.2.26.1  2006/03/22 18:01:30  nw
merges from head, and snapshot of development

Revision 1.2  2005/12/02 13:43:18  nw
new compoent that manages a pool of threads to execute background processes on

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/