/*$Id: HelpServerImpl.java,v 1.3 2005/10/12 13:30:10 nw Exp $
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.acr.system.HelpServer;

import org.picocontainer.Startable;

import acrjavahelp.ACRJavaHelpResourceAnchor;

import java.awt.Component;
import java.awt.MenuItem;
import java.awt.event.ActionListener;
import java.net.URL;


import javax.help.CSH;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.help.InvalidHelpSetContextException;
import javax.help.Map;
import javax.help.SwingHelpUtilities;
import javax.swing.AbstractButton;
import javax.swing.Action;

/** Implementation of the help server.
 * @author Noel Winstanley nw@jb.man.ac.uk 17-Jun-2005
 *
 */
public class HelpServerImpl implements Startable, HelpServerInternal{
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(HelpServerImpl.class);

    /** Construct a new HelpServerImpl
     * 
     */
    public HelpServerImpl() {
        super();
    }
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
 
        ClassLoader cl = this.getClass().getClassLoader();
        try {
            URL u = HelpSet.findHelpSet(cl,"javahelp/workbench-helpset.hs");
            hs = new HelpSet(null, u);
            broker = hs.createHelpBroker();
        } catch (Exception e) {
            logger.error("Failed to load helpset",e);
        }
        
        cl = ACRJavaHelpResourceAnchor.class.getClassLoader();
        try {
            URL u = HelpSet.findHelpSet(cl,"acrjavahelp/jhelp.hs");
            HelpSet subsid = new HelpSet(cl,u);
            hs.add(subsid);
        } catch (Exception e) {
            logger.error("Failed to load subsidiary helpset",e);
        }
    }
    
    protected HelpBroker broker;
    protected HelpSet hs;

    /**
     * @see org.picocontainer.Startable#stop()
     */
    public void stop() {
    }
    
    
    public void showHelp() {
        Map.ID home = hs.getHomeID();
        try {
            broker.setCurrentID(home);
        } catch (InvalidHelpSetContextException e) {
            logger.warn("InvalidHelpSetContextException",e);
        }
        broker.setDisplayed(true);
    }
    
    public void showHelpForTarget(String target) {
        broker.setCurrentID(target);
        broker.setDisplayed(true);
    }

    public void enableHelpKey(Component comp, String defaultHelpId) {
        broker.enableHelpKey(comp,defaultHelpId,null);
    }
 
    public void enableHelp(Component comp, String helpId) {
        broker.enableHelp(comp,helpId,null);
    }

    public void enableHelp(MenuItem comp, String helpId) {
        broker.enableHelp(comp,helpId,null);        
    }
  
    public void enableHelpOnButton(AbstractButton b, String helpId) {
        broker.enableHelp(b,helpId,null);
    }
   
    public void enableHelpOnButton(MenuItem b, String helpId) {
        broker.enableHelp(b,helpId,null);        
    }

    public ActionListener createContextSensitiveHelpListener() {
        return new CSH.DisplayHelpAfterTracking(broker); // wonder whether we can reuse a single instance of this??
    }
    
    
    
}


/* 
$Log: HelpServerImpl.java,v $
Revision 1.3  2005/10/12 13:30:10  nw
merged in fixes for 1_2_4_beta_1

Revision 1.2.10.2  2005/10/12 09:21:38  nw
added java help system

Revision 1.2.10.1  2005/10/10 16:24:29  nw
reviewed phils workflow builder
skeletal javahelp

Revision 1.2  2005/09/02 14:03:34  nw
javadocs for impl

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.2  2005/06/23 09:08:26  nw
changes for 1.0.3 release

Revision 1.1  2005/06/20 16:56:40  nw
fixes for 1.0.2-beta-2
 
*/