/*$Id: SysTrayRemoteProcessListener.java,v 1.3 2006/05/08 15:52:49 nw Exp $
 * Created on 03-Apr-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ag;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.astrogrid.ExecutionMessage;
import org.astrogrid.acr.astrogrid.RemoteProcessListener;
import org.astrogrid.acr.system.SystemTray;
import org.astrogrid.desktop.modules.ag.MessageRecorderInternal.Folder;
import org.astrogrid.desktop.modules.background.JesStrategyImpl;
/**
 * displays process events in the system tray.
 * @author Noel Winstanley nw@jb.man.ac.uk 03-Apr-2006
 *
 */
public final class SysTrayRemoteProcessListener implements RemoteProcessListener {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(SysTrayRemoteProcessListener.class);


    private final MessageRecorderInternal recorder;

    private final SystemTray tray;

    public SysTrayRemoteProcessListener(MessageRecorderInternal recorder, SystemTray tray) {
        super();
        this.recorder = recorder;
        this.tray = tray;
    }

    public void statusChanged(URI arg0, String arg1) {
        if (JesStrategyImpl.isCompletedOrError(arg1)) {
            try {
            Folder f = this.recorder.getFolder(arg0);
            String name = f.getInformation().getName();
            if (arg1.equals("ERROR")) {
                this.tray.displayWarningMessage(name + " ended in error","See VO Lookout for details");
            } else {
               this.tray.displayInfoMessage(name + " completed successfuly","See VO Lookout for results");
            }                    
            } catch (IOException e) {
                this.logger.fatal("Failed to find folder for id - " + arg0,e);
            }
        } else {
            System.err.println(arg0 + " " + arg1);
        }
        
    }

    public void messageReceived(URI arg0, ExecutionMessage arg1) {
        
    }

    public void resultsReceived(URI arg0, Map arg1) {
    }
}

/* 
$Log: SysTrayRemoteProcessListener.java,v $
Revision 1.3  2006/05/08 15:52:49  nw
fixed constructor visibility

Revision 1.2  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.1.2.2  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.1.2.1  2006/04/04 10:31:25  nw
preparing to move to mac.
 
*/