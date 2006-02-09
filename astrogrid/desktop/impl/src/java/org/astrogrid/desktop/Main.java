/*$Id: Main.java,v 1.4 2006/02/09 15:39:15 nw Exp $
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

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.system.UI;
import org.astrogrid.desktop.modules.system.UIImpl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.picocontainer.Startable;

import com.jgoodies.looks.Options;
import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;

/** Main class when running workbench / ACR in standalone mode.
 * @author Noel Winstanley nw@jb.man.ac.uk 15-Mar-2005
 *
 */
public class Main extends BuildInprocessACR implements Startable  {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(Main.class);

    /** Construct a new workbench.
     * 
     */
    public Main() {
        super();
        try {
            UIManager.setLookAndFeel(new PlasticXPLookAndFeel());
            UIManager.put(Options.USE_SYSTEM_FONTS_APP_KEY,    Boolean.TRUE);          
         } catch (Exception e) {
             logger.warn("Failed to install plastic look and feel - oh well");
             }   
         
         // configure tooltip behaviour.
         UIManager.put("ToolTip.background",Color.white);
         ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
         ToolTipManager.sharedInstance().setInitialDelay(500);
    }

    /**
     * @see org.picocontainer.Startable#start()
     * starts the pico container - on the swing event dispatch thread - but waits for it. 
     */
    public void start() {
       try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    Main.super.start();
                    // maybe show the ui.
                    ACR acr = getACR();
                    try {
                    Configuration c = (Configuration)acr.getService(Configuration.class);
                    String key = c.getKey(UIImpl.START_HIDDEN_KEY);
                    if (key == null || !  Boolean.valueOf(key).equals(Boolean.TRUE)){
                        UI ui = (UI)acr.getService(UI.class);
                        ui.show();                        
                    }
                    } catch (ACRException e) {
                        logger.warn("Problems displaying the ui - maybe running in headless mode");
                    }
                }
            });
            
        } catch (InterruptedException e) {
            logger.fatal("InterruptedException on startup",e);
        } catch (InvocationTargetException e) {
            logger.fatal("InvocationTargetException on startup",e);
        }
    }

 
    public static final void main(String[] args) {
        Main d = new Main();
        d.start();
    }


}


/* 
$Log: Main.java,v $
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