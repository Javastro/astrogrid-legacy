/*$Id: WebstartBrowserControl.java,v 1.4 2007/01/29 11:11:36 nw Exp $
 * Created on 01-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system;

import java.lang.reflect.Method;
import java.net.URL;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.system.WebServer;
import org.astrogrid.desktop.alternatives.FallbackBrowserControl;
import org.astrogrid.desktop.framework.ReflectionHelper;

/** Implementation of browsercontrol using the webstart additional APIs.
 * if it fails to get a service, will fallback to BrowserLauncherBrowserControl.
 * Likewise, will fallback if it fails to open the url.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 01-Feb-2005
 *
 */
public class WebstartBrowserControl extends BrowserLauncherBrowserControl implements BrowserControl {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(WebstartBrowserControl.class);

    /** Construct a new WebstartBrowserControl
     * 
     */
    public WebstartBrowserControl(WebServer root, UIInternal ui)  {
        super(root,ui);
        try {
        Class managerClass = Class.forName("javax.jnlp.ServiceManager");
        if (managerClass != null) {
                Method lookupMethod =ReflectionHelper.getMethodByName(managerClass,"lookup");                     
                basicService = lookupMethod.invoke(null,new Object[]{"javax.jnlp.BasicService"});
        }
        } catch (Exception e) {
            logger.warn("Could not create basic jnlp service");
            basicService = null;
        }
        
    }
    protected Object basicService;



    /**
     * @throws ACRException
     * @see org.astrogrid.acr.system.BrowserControl#openURL(java.net.URL)
     */
    public void openURL(URL url) throws ACRException  {
        if (basicService == null) {
            logger.warn("no basic jnlp service");
            super.openURL(url);
        } else {
            try {
                MethodUtils.invokeMethod(basicService,"showDocument",url);
            } catch (Exception e) {
            	logger.warn("Failed to use webstart to control browser - falling back",e);
            	super.openURL(url);
            } 
        }
    }



}


/* 
$Log: WebstartBrowserControl.java,v $
Revision 1.4  2007/01/29 11:11:36  nw
updated contact details.

Revision 1.3  2006/11/09 12:08:33  nw
final set of changes for 2006.4.rc1

Revision 1.2  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.1.68.1  2006/04/14 02:45:02  nw
finished code.extruded plastic hub.

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.6  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.5  2005/05/12 15:59:10  clq2
nww 1111 again

Revision 1.3.8.1  2005/05/11 11:55:30  nw
removed unused interface.

Revision 1.3  2005/04/27 13:42:41  clq2
1082

Revision 1.2.2.1  2005/04/25 11:18:51  nw
split component interfaces into separate package hierarchy
- improved documentation

Revision 1.2  2005/04/13 12:59:12  nw
checkin from branch desktop-nww-998

Revision 1.1.2.1  2005/03/18 12:09:31  nw
got framework, builtin and system levels working.

Revision 1.2  2005/02/22 01:10:31  nw
enough of a prototype here to do a show-n-tell on.

Revision 1.1  2005/02/21 11:25:07  nw
first add to cvs
 
*/