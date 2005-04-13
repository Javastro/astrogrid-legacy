/*$Id: ThrobbingInterceptor.java,v 1.2 2005/04/13 12:59:12 nw Exp $
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


import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/** interceptor that starts the throbber before processing a method.
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
            return invocation.proceed();
        } finally {
            ui.stopThrobbing();
        }
    }

}


/* 
$Log: ThrobbingInterceptor.java,v $
Revision 1.2  2005/04/13 12:59:12  nw
checkin from branch desktop-nww-998

Revision 1.1.2.1  2005/03/22 12:04:03  nw
working draft of system and ag components.
 
*/