/*$Id: SwingSetup.java,v 1.10 2008/11/04 14:35:49 nw Exp $
 * Created on 21-Mar-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system;

import java.awt.Color;

import javax.swing.ToolTipManager;
import javax.swing.UIManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Global configuration of swing UI.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 21-Mar-2006
 *
 */
public class SwingSetup implements Runnable {
    /**
 * Logger for this class
 */
private static final Log logger = LogFactory.getLog(SwingSetup.class);

    public SwingSetup() {
        super();
    }

    public void run()   {
     // configure tooltip behaviour.
     UIManager.put("ToolTip.background",Color.white);
     ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
     ToolTipManager.sharedInstance().setInitialDelay(500);
     

    }

}


/* 
$Log: SwingSetup.java,v $
Revision 1.10  2008/11/04 14:35:49  nw
javadoc polishing

Revision 1.9  2007/01/29 16:45:07  nw
cleaned up imports.

Revision 1.8  2007/01/29 11:11:36  nw
updated contact details.

Revision 1.7  2006/08/31 21:32:49  nw
doc fixes.

Revision 1.6  2006/06/27 19:18:32  nw
adjusted todo tags.

Revision 1.5  2006/06/27 10:40:55  nw
no change

Revision 1.4  2006/05/17 23:57:46  nw
documentation improvements.

Revision 1.3  2006/04/21 13:48:11  nw
mroe code changes. organized impoerts to reduce x-package linkage.

Revision 1.2  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.1.2.3  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.1.2.2  2006/04/04 10:31:26  nw
preparing to move to mac.

Revision 1.1.2.1  2006/03/22 18:01:30  nw
merges from head, and snapshot of development
 
*/