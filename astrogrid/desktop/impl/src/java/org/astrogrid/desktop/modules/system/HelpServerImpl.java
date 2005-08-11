/*$Id: HelpServerImpl.java,v 1.1 2005/08/11 10:15:00 nw Exp $
 * Created on 17-Jun-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system;

import org.astrogrid.acr.system.HelpServer;

import org.picocontainer.Startable;

import com.jstatcom.component.JHelpAction;

import javax.help.SwingHelpUtilities;
import javax.swing.Action;

/** Implementation of the help server.
 * @author Noel Winstanley nw@jb.man.ac.uk 17-Jun-2005
 *
 */
public class HelpServerImpl implements Startable, HelpServer{

    /** Construct a new HelpServerImpl
     * 
     */
    public HelpServerImpl() {
        super();
    }
    private static String HELPSET_PATH = "/helpset/desktop-helpset.xml";
    /** loads the helpset in a background thread.
     * @see org.picocontainer.Startable#start()
     */
    public void start() {
        // if jdic browser is available, set help browser to use that.
        Check jdic  = new CheckJDICBrowserPresent();
        if (jdic.check()) {
            SwingHelpUtilities.setContentViewerUI
                ("javax.help.plaf.basic.BasicNativeContentViewerUI");
        }
        JHelpAction.startHelpWorker(HELPSET_PATH);
    }

    /**
     * @see org.picocontainer.Startable#stop()
     */
    public void stop() {
    }
    
    
    public void showHelp() {
        JHelpAction.showHelp();
    }
    
    public void showHelpForTarget(String target) {
        JHelpAction.showHelp(target);
    }
    
    public void showHelpFromFocus() {
        JHelpAction.showHelpFromFocus();
    }
    
    public void trackFieldHelp() {
        JHelpAction.trackFieldHelp();
    }

 
    public Action getShowHelpForTargetInstance(String target) {
        return JHelpAction.getShowHelpInstance(target);
    }
    
    public Action getShowIDInstance(String actionName, String helpId) {
        return JHelpAction.getShowIDInstance(actionName,helpId);
    }
    
}


/* 
$Log: HelpServerImpl.java,v $
Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.2  2005/06/23 09:08:26  nw
changes for 1.0.3 release

Revision 1.1  2005/06/20 16:56:40  nw
fixes for 1.0.2-beta-2
 
*/