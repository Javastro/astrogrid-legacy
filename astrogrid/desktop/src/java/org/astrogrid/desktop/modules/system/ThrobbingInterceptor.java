/*$Id: ThrobbingInterceptor.java,v 1.5 2005/05/12 15:59:10 clq2 Exp $
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


import org.astrogrid.acr.system.UI;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/** interceptor that starts the ui throbber before processing a method.
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Mar-2005
 *
 */
public class ThrobbingInterceptor implements MethodInterceptor {

    /** Construct a new ThrobbingInterceptor
     * 
     */
    public ThrobbingInterceptor(UI ui) {
        super();
        this.ui = ui;
    }
    
    protected final UI ui;

    /**
     * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
     */
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            ui.startThrobbing();
            // doesn't work - always shows 'getEnv' - need to have it so that is only sets Status message on first invoaction, not nested ones.
            //ui.setStatusMessage("calling " + invocation.getMethod().getName());
            return invocation.proceed();
        } finally {
            ui.stopThrobbing();
          //  ui.setStatusMessage("");
        }
    }

}


/* 
$Log: ThrobbingInterceptor.java,v $
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