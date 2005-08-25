/*$Id: ShutdownImpl.java,v 1.2 2005/08/25 16:59:58 nw Exp $
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

import org.astrogrid.acr.builtin.Shutdown;
import org.astrogrid.acr.builtin.ShutdownListener;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.picocontainer.PicoContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

/** implementation class for {@link Shutdown}
 * 
 * calls stop() on each module in the registry, before terminating.
 *  */
public class ShutdownImpl implements Shutdown {
    private final Map modules;

    public ShutdownImpl(Map modules) {
        super();
        this.modules = modules;
    }

    public void halt() {
        // tell everyone 
        List objections = new ArrayList();
        for (int i = 0; i < listeners.size(); i++) {
            try {
                ShutdownListener sl = (ShutdownListener)listeners.get(i);
                if (sl != null) {
                  String msg =   sl.lastChance();
                  if (msg != null) {
                      objections.add(msg);
                  }
                }
            } catch (Throwable t) {
                // really don't care - nothing can stop us
            }
        }
        // go ahead if we've no objections, otherwise prompt the user.       
        if (objections.size() ==0 || JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(null,fmt(objections),"Shutting down ACR - are you sure?",JOptionPane.OK_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE)) {
            reallyHalt();
        }
        
    }
    
    private String fmt(List l) {
        StringBuffer sb = new StringBuffer();
        sb.append("<html><b>There appears to be clients that still require the ACR</b><ul>");
        for (int i = 0; i < l.size(); i++) {
            sb.append("<li>");
            sb.append(l.get(i).toString());            
        }
        sb.append("</ul><b>Select 'OK' if you still want to halt the ACR, otherwise 'Cancel'</b></html>");
        return sb.toString();
    }

    /**
     * @see org.astrogrid.acr.builtin.Shutdown#reallyHalt()
     */
    public void reallyHalt() {
        // tell everyone 
        for (int i = 0; i < listeners.size(); i++) {
            try {
                ShutdownListener sl =(ShutdownListener)listeners.get(i);
                if (sl != null) {
                    sl.halting();
                }
            } catch (Throwable t) {
                // really don't care - nothing can stop us
            }
        }
        // do the deed
        CollectionUtils.forAllDo(modules.values(),new Closure() {
            public void execute(Object arg0) {
                ((PicoContainer)arg0).stop();
            }
        });
        System.exit(0);
    }

    protected List listeners = new ArrayList();
    /**
     * @see org.astrogrid.acr.builtin.Shutdown#addShutdownListener(org.astrogrid.acr.builtin.ShutdownListener)
     */
    public void addShutdownListener(ShutdownListener arg0) {
        listeners.add(arg0);
    }

    /**
     * @see org.astrogrid.acr.builtin.Shutdown#removeShutdownListener(org.astrogrid.acr.builtin.ShutdownListener)
     */
    public void removeShutdownListener(ShutdownListener arg0) {
        listeners.remove(arg0);
    }
}

/* 
$Log: ShutdownImpl.java,v $
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