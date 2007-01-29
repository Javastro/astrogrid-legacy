/*$Id: JDICSystemTray.java,v 1.5 2007/01/29 11:11:37 nw Exp $
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.SwingUtilities;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.builtin.Shutdown;
import org.astrogrid.acr.builtin.ShutdownListener;
import org.astrogrid.desktop.icons.IconHelper;
import org.jdesktop.jdic.tray.SystemTray;
import org.jdesktop.jdic.tray.TrayIcon;

/** Implementation of System Tray, using JDIC extensions.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 21-Jun-2005
 *
 */
public class JDICSystemTray implements org.astrogrid.acr.system.SystemTray,ShutdownListener {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(JDICSystemTray.class);

    /** Construct a new JDICSystemTray
     * 
     */
    public JDICSystemTray(final UIInternal ui, final Shutdown shutdown) {
        super();
        this.ui = ui;
        this.shutdown = shutdown;
        SystemTray st1 = null;
        try {
            st1 = SystemTray.getDefaultSystemTray();
        } catch (UnsatisfiedLinkError e) { // being really cautious - as have already 'checked' for this - but best to be safe.
            logger.error("Could not create system tray component");
        }
        st = st1;
       //     idleIcon = IconHelper.loadIcon("AGlogo16x16.png"); //@
        	idleIcon = IconHelper.loadIcon("server.png");
            activeIcon = IconHelper.loadIcon("flashpoint.gif");
            start();

    }
    protected final SystemTray st;
    protected final UIInternal ui;
    protected final Shutdown shutdown;
    protected TrayIcon ti;
    protected final Icon idleIcon;
    protected final Icon activeIcon;
    protected int throbberCallCount = 0;

    /**
     * @see org.picocontainer.Startable#start()
     */
    public void start() {
        if (st != null) {
            ti = new TrayIcon(idleIcon,"AstroGrid");
            ti.setIconAutoSize(true);
            if (ui.getComponent() != null ) { // otherwise, ui is disabled
            ti.setToolTip("Left-click to show / hide User Interface");   
            ti.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (ui.getComponent().isVisible()) {
                    ui.hide();
                } else {
                    ui.show();
                }
            }
            });
            } else {
            	ti.setToolTip("Left-click to halt and exit ACR");
            	ti.addActionListener(new ActionListener() {
            
            	public void actionPerformed(ActionEvent e) {
            		shutdown.halt();
            	}
            	});
            }
            st.addTrayIcon(ti);            
        }
    }


    /**
     * @see org.astrogrid.acr.system.SystemTray#displayErrorMessage(java.lang.String, java.lang.String)
     */
    public void displayErrorMessage(String caption, String message) {
        if (st != null) {
        ti.displayMessage(caption,message,TrayIcon.ERROR_MESSAGE_TYPE);
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
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    if (++throbberCallCount > 0) {
                        ti.setIcon(activeIcon);
                    }
                }
            });
        }
    }

    /**
     * @see org.astrogrid.acr.system.SystemTray#stopThrobbing()
     */
    public void stopThrobbing() {
        if (st != null) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {            
                    if (! (--throbberCallCount > 0)) {
                        ti.setIcon(idleIcon);
                    }
                }
            });
        }
    }

    // probably unnecessary, but still.
    public void halting() {
        if (st != null) {
            st.removeTrayIcon(ti);
            }        
    }

    public String lastChance() {
        return null;
    }

}


/* 
$Log: JDICSystemTray.java,v $
Revision 1.5  2007/01/29 11:11:37  nw
updated contact details.

Revision 1.4  2006/10/11 10:39:15  nw
changed icons.

Revision 1.3  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.2.66.3  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.2.66.2  2006/04/04 10:31:26  nw
preparing to move to mac.

Revision 1.2.66.1  2006/03/22 18:01:30  nw
merges from head, and snapshot of development

Revision 1.2  2005/08/25 16:59:58  nw
1.1-beta-3

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.2  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.1  2005/06/22 08:48:52  nw
latest changes - for 1.0.3-beta-1
 
*/