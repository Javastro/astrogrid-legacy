/*$Id: LegacyJobMonitor.java,v 1.2 2006/04/18 23:25:43 nw Exp $
 * Created on 11-Nov-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ui;

import java.net.URI;

import org.astrogrid.acr.ui.JobMonitor;
import org.astrogrid.acr.ui.Lookout;

/** backwards-compatability class - makes lookout implement old job monitor interface.
 * keep in place for old scripts / xmlrpc clients
 * @author Noel Winstanley nw@jb.man.ac.uk 11-Nov-2005
 *
 */
public class LegacyJobMonitor implements JobMonitor {

    /** Construct a new LegacyJobMonitor
     * 
     */
    public LegacyJobMonitor(Lookout lookout) {
        super();
        this.lookout = lookout;
    }
    private final Lookout lookout;

    /**
     * @see org.astrogrid.acr.ui.JobMonitor#show()
     */
    public void show() {
        lookout.show();
    }

    /**
     * @see org.astrogrid.acr.ui.JobMonitor#hide()
     */
    public void hide() {
        lookout.hide();
    }

    /**
     * @see org.astrogrid.acr.ui.JobMonitor#refresh()
     */
    public void refresh() {
        lookout.refresh();
    }

    /** does nothing
     * @see org.astrogrid.acr.ui.JobMonitor#addApplication(java.lang.String, java.net.URI)
     */
    public void addApplication(String arg0, URI arg1) {
    }

    /** does nothing
     * @see org.astrogrid.acr.ui.JobMonitor#displayApplicationTab()
     */
    public void displayApplicationTab() {
    }

    /** does nothing.
     * @see org.astrogrid.acr.ui.JobMonitor#displayJobTab()
     */
    public void displayJobTab() {
    }

}


/* 
$Log: LegacyJobMonitor.java,v $
Revision 1.2  2006/04/18 23:25:43  nw
merged asr development.

Revision 1.1.34.1  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.1  2005/11/11 10:08:18  nw
cosmetic fixes
 
*/