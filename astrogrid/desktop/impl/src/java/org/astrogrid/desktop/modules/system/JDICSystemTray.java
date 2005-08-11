/*$Id: JDICSystemTray.java,v 1.1 2005/08/11 10:15:00 nw Exp $
 * Created on 21-Jun-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system;

import org.astrogrid.desktop.icons.IconHelper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.jdic.tray.SystemTray;
import org.jdesktop.jdic.tray.TrayIcon;
import org.picocontainer.Startable;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;

/** Implementation of System Tray, using JDIC extensions.
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Jun-2005
 * @todo popup menu, (can reuse main menu?)
 *
 */
public class JDICSystemTray implements Startable, org.astrogrid.acr.system.SystemTray {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(JDICSystemTray.class);

    /** Construct a new JDICSystemTray
     * 
     */
    public JDICSystemTray(final UIInternal ui) {
        super();
        this.ui = ui;
        SystemTray st1 = null;
        try {
            st1 = SystemTray.getDefaultSystemTray();
        } catch (UnsatisfiedLinkError e) { // being really cautious - as have already 'checked' for this - but best to be safe.
            logger.error("Could not create system tray component");
        }
        st = st1;
            idleIcon = IconHelper.loadIcon("AGlogo16x16.png");
            activeIcon = IconHelper.loadIcon("flashpoint.gif");
  

    }
    protected final SystemTray st;
    protected final UIInternal ui;
    protected TrayIcon ti;
    protected final Icon idleIcon;
    protected final Icon activeIcon;
    protected int throbberCallCount = 0;

    /**
     * @see org.picocontainer.Startable#start()
     */
    public void start() {
        if (st != null) {
            ti = new TrayIcon(idleIcon,"AstroGrid Workbench");
            ti.setIconAutoSize(true);
            ti.setToolTip("Left-click to show / hide Workbench");   
            ti.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (ui.getComponent().isVisible()) {
                    ui.hide();
                } else {
                    ui.show();
                }
            }
            });
            st.addTrayIcon(ti);            
        }
    }

    /**
     * @see org.picocontainer.Startable#stop()
     */
    public void stop() {
        if (st != null) {
        st.removeTrayIcon(ti);
        }
    }

    /**
     * @see org.astrogrid.acr.system.SystemTray#displayErrorMessage(java.lang.String, java.lang.String)
     */
    public void displayErrorMessage(String caption, String message) {
        if (st != null) {
        ti.displayMessage(caption,message,TrayIcon.ERROE_MESSAGE_TYPE);
        }
    }

    /**
     * @see org.astrogrid.acr.system.SystemTray#displayInfoMessage(java.lang.String, java.lang.String)
     */
    public void displayInfoMessage(String caption, String message) {
        if (st != null) {
        ti.displayMessage(caption,message,TrayIcon.INFO_MESSAGE_TYPE);
        }
    }

    /**
     * @see org.astrogrid.acr.system.SystemTray#displayWarningMessage(java.lang.String, java.lang.String)
     */
    public void displayWarningMessage(String caption, String message) {
        if (st != null) {
        ti.displayMessage(caption,message,TrayIcon.WARNING_MESSAGE_TYPE);
        }
    }

    /**
     * @see org.astrogrid.acr.system.SystemTray#startThrobbing()
     */
    public void startThrobbing() {
        if (st != null) {
        if (++throbberCallCount > 0) {
            ti.setIcon(activeIcon);
        }
        }
    }

    /**
     * @see org.astrogrid.acr.system.SystemTray#stopThrobbing()
     */
    public void stopThrobbing() {
        if (st != null) {
        if (! (--throbberCallCount > 0)) {
            ti.setIcon(idleIcon);
        }
        }
    }

}


/* 
$Log: JDICSystemTray.java,v $
Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.2  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.1  2005/06/22 08:48:52  nw
latest changes - for 1.0.3-beta-1
 
*/