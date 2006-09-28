/*$Id: ShutdownImpl.java,v 1.6 2006/09/28 15:12:28 jdt Exp $
 * Created on 17-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.framework;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hivemind.ShutdownCoordinator;
import org.astrogrid.acr.builtin.Shutdown;
import org.astrogrid.acr.builtin.ShutdownListener;

import EDU.oswego.cs.dl.util.concurrent.SynchronizedBoolean;

/** implementation class for {@link Shutdown}
 * 
 * hides details of hivemind - and means that rest of code doesn't need to 
 * be tied to that particular container system.
 * @todo remove gui aspect - or provide non-gui alternative.
 *  */
public class ShutdownImpl  extends Thread implements Shutdown{
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(ShutdownImpl.class);

    public ShutdownImpl(ShutdownCoordinator coord, boolean callSystemExit) {
        this.coord = coord;
        this.callSystemExit = callSystemExit;
        Runtime.getRuntime().addShutdownHook(this);
    }
    private final boolean callSystemExit;
    private final ShutdownCoordinator coord;
    private boolean confirmIfObjections = true;
    
  
    
    
    /** if true (default), user is prompted for confirmation if there's any objections
     * to shutdown 
     * @param v
     */
    public void setConfirmIfObjections(boolean v) {
    	this.confirmIfObjections = v;
    }


    public void halt() {
        logger.debug("halt() - start");

        // tell everyone 
        List objections = new ArrayList();
        for (Iterator i = listeners.iterator(); i.hasNext(); ) {
            try {
                ShutdownListener sl = (ShutdownListener)i.next();
                if (sl != null) {
                  String msg =   sl.lastChance();
                  if (msg != null) {
                      objections.add(msg);
                  }
                }
            } catch (Throwable t) {
                logger.error("halt()", t);

                // really don't care - nothing can stop us
            }
        }
        // go ahead if we've no objections, otherwise prompt the user.       
        if (objections.size() ==0 || ! confirmIfObjections || JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(null,fmt(objections),"Shutting down ACR - are you sure?",JOptionPane.OK_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE)) {
            reallyHalt();
        }
        

        logger.debug("halt() - end");
    }
    
    private String fmt(List l) {
        logger.debug("fmt(List) - start");

        StringBuffer sb = new StringBuffer();
        sb.append("<html><b>There appears to be clients that still require the ACR</b><ul>");
        for (int i = 0; i < l.size(); i++) {
            sb.append("<li>");
            sb.append(l.get(i).toString());            
        }
        sb.append("</ul><b>Select 'OK' if you still want to halt the ACR, otherwise 'Cancel'</b></html>");
        String returnString = sb.toString();
        logger.debug("fmt(List) - end");
        return returnString;
    }

    
    // flag set to prevent duplicate execution of 'reallyHalt()'
    private final SynchronizedBoolean alreadyHalting = new SynchronizedBoolean(false);
    /**
     * @see org.astrogrid.acr.builtin.Shutdown#reallyHalt()
     */
    public void reallyHalt() {
    	run();
        if (callSystemExit) {
        	System.exit(0);
        }

    }
    
    
    // entry point for the runtime shutdown hook.
    public void run() {
    	if (alreadyHalting.set(true)) { // atomic action - sets, and returns previous value. 		
    		return;
    	}
        logger.debug("reallyHalt() - start");

        // tell everyone 
        List copy = new ArrayList(listeners);
        for (Iterator i = copy.iterator(); i.hasNext(); ) {
            try {
                ShutdownListener sl = (ShutdownListener)i.next();
                if (sl != null) {
                    sl.halting();
                }
            } catch (Throwable t) {
                logger.error("reallyHalt()", t);

                // really don't care - nothing can stop us
            }
        }
        // do the deed
        coord.shutdown();
        
    }   

    protected Set listeners = new HashSet();
    /**
     * @see org.astrogrid.acr.builtin.Shutdown#addShutdownListener(org.astrogrid.acr.builtin.ShutdownListener)
     */
    public void addShutdownListener(ShutdownListener arg0) {
    	if (arg0 == null) {
    		logger.warn("Attempt to add null listener");
    		return;
    	}
        logger.debug("addShutdownListener(ShutdownListener) - start");
        logger.debug(arg0);
        // JDT1726 http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1726
        // This is a sledgehammer solution to a problem I don't fully understand.
        try {
            listeners.add(arg0);
        } catch (Exception e) {
            logger.error("Error registering shutdown listener.  In the absence of any better ideas, ignoring.",e);
        }

        logger.debug("addShutdownListener(ShutdownListener) - end");
    }

    /**
     * @see org.astrogrid.acr.builtin.Shutdown#removeShutdownListener(org.astrogrid.acr.builtin.ShutdownListener)
     */
    public void removeShutdownListener(ShutdownListener arg0) {
    	if (arg0 == null) {
    		logger.warn("Attempt to remove null listener");
    		return;
    	}
        logger.debug("removeShutdownListener(ShutdownListener) - start");
        logger.debug(arg0);
        listeners.remove(arg0);

        logger.debug("removeShutdownListener(ShutdownListener) - end");
    }


}

/* 
$Log: ShutdownImpl.java,v $
Revision 1.6  2006/09/28 15:12:28  jdt
Fix for bug http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1726

Revision 1.5  2006/08/31 21:10:38  nw
added shutdown hook

Revision 1.4  2006/06/15 09:40:18  nw
improvements coming from unit testing

Revision 1.3  2006/04/18 23:25:46  nw
merged asr development.

Revision 1.2.66.5  2006/04/18 18:49:04  nw
version to merge back into head.

Revision 1.2.66.4  2006/04/14 02:45:03  nw
finished code.extruded plastic hub.

Revision 1.2.66.3  2006/04/04 10:31:26  nw
preparing to move to mac.

Revision 1.2.66.2  2006/03/28 13:47:35  nw
first webstartable version.

Revision 1.2.66.1  2006/03/22 18:01:31  nw
merges from head, and snapshot of development

Revision 1.2  2005/08/25 16:59:58  nw
1.1-beta-3

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.6  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.5  2005/05/12 15:59:11  clq2
nww 1111 again

Revision 1.3.8.1  2005/05/11 14:25:25  nw
javadoc, improved result transformers for xml

Revision 1.3  2005/04/27 13:42:41  clq2
1082

Revision 1.2.2.2  2005/04/25 11:18:51  nw
split component interfaces into separate package hierarchy
- improved documentation

Revision 1.2.2.1  2005/04/22 15:59:26  nw
made a star documenting desktop.

Revision 1.2  2005/04/13 12:59:11  nw
checkin from branch desktop-nww-998

Revision 1.1.2.2  2005/03/23 14:36:18  nw
got pw working

Revision 1.1.2.1  2005/03/18 12:09:31  nw
got framework, builtin and system levels working.
 
*/