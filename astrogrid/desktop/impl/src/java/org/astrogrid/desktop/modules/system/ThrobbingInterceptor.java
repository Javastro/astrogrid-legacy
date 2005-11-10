/*$Id: ThrobbingInterceptor.java,v 1.4 2005/11/10 12:05:53 nw Exp $
 * Created on 21-Mar-2005
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


import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.system.SystemTray;
import org.astrogrid.acr.system.UI;
import org.astrogrid.desktop.modules.ag.MessagingInternal;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/** an AOP interceptor that starts the ui throbber before processing a method.
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Mar-2005
 *@todo maybe change way this is registered? 
 */
public class ThrobbingInterceptor implements MethodInterceptor {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(ThrobbingInterceptor.class);

    /** Construct a new ThrobbingInterceptor
     * @throws ACRException
     * @throws InvalidArgumentException
     * 
     */
    public ThrobbingInterceptor(ACR acr) throws InvalidArgumentException, ACRException {
        this.acr = acr;
        try {
            this.ui = (UI)acr.getService(UI.class);
        } catch (NotFoundException e) {
            this.ui = null;
            logger.warn("No UI to report to");
        }
        try {
            this.tray = (SystemTray)acr.getService(SystemTray.class);
        } catch (NotFoundException e) {
            this.tray = null;
            logger.warn("No Sys tray to report to");
        }
      
        
    }

    protected ACR acr;
    protected UI ui;
    protected SystemTray tray;
    protected MessagingInternal reporter;

    /**
     * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
     */
    public final Object invoke(MethodInvocation invocation) throws Throwable {

           
        try {
            if (ui != null) {
                ui.startThrobbing();
            }
            // doesn't work - always shows 'getEnv' - need to have it so that is only sets Status message on first invoaction, not nested ones.
            //ui.setStatusMessage("calling " + invocation.getMethod().getName());
            if (tray != null) {
                tray.startThrobbing();
            }
            if (reporter == null) { // got to be here somewhere..
                try {
                    // have to do this, as can't get at it at creation time - irritating.
                    this.reporter = (MessagingInternal)acr.getService(MessagingInternal.class);
                    logger.info("Found messager");
                } catch (NotFoundException e) {
                    // ho hum. maybe it's not here.
                }
            }
            if (reporter != null) {
                reporter.sendTrackingMessage(invocation);
            }
            return invocation.proceed();
            
        } catch (Exception t) { // throwables are probably too low-level.
            if (reporter != null) {
                reporter.sendErrorMessage(invocation,t);                
            }
            throw t; // @todo verify that this preserves the original stack trace.
            
        } finally {
            if (ui != null) {
                ui.stopThrobbing();
            }
            if (tray != null) {
                tray.stopThrobbing();
            }
          //  ui.setStatusMessage("");
        }
    }

}


/* 
$Log: ThrobbingInterceptor.java,v $
Revision 1.4  2005/11/10 12:05:53  nw
big change around for vo lookout

Revision 1.3  2005/11/01 09:19:46  nw
messsaging for applicaitons.

Revision 1.2  2005/09/02 14:03:34  nw
javadocs for impl

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.7  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.6  2005/06/22 08:48:52  nw
latest changes - for 1.0.3-beta-1

Revision 1.5  2005/05/12 15:59:10  clq2
nww 1111 again

Revision 1.3.8.1  2005/05/11 14:25:23  nw
javadoc, improved result transformers for xml

Revision 1.3  2005/04/27 13:42:41  clq2
1082

Revision 1.2.2.1  2005/04/25 11:18:51  nw
split component interfaces into separate package hierarchy
- improved documentation

Revision 1.2  2005/04/13 12:59:12  nw
checkin from branch desktop-nww-998

Revision 1.1.2.1  2005/03/22 12:04:03  nw
working draft of system and ag components.
 
*/