/*$Id: LoggingInterceptor.java,v 1.2 2005/04/13 12:59:12 nw Exp $
 * Created on 21-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.framework;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** trial interceptor - logs entry and exit to component methods.
 * 
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Mar-2005
 *
 */
public class LoggingInterceptor implements MethodInterceptor{
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog("OPERATION");

    /** Construct a new LoggingInterceptor
     * 
     */
    public LoggingInterceptor() {
        super();
    }

    /**
     * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
     */
    public Object invoke(MethodInvocation invocation) throws Throwable { 
        if (invocation.getMethod().getName() != null) {
        logger.info(invocation.getMethod().getName() + " - start");
        Object result = invocation.proceed();
        logger.info(invocation.getMethod().getName() + "- end");
        return result;
        } else{
            return invocation.proceed();
        }
    }

}


/* 
$Log: LoggingInterceptor.java,v $
Revision 1.2  2005/04/13 12:59:12  nw
checkin from branch desktop-nww-998

Revision 1.1.2.2  2005/04/04 08:49:27  nw
working job monitor, tied into pw launcher.

Revision 1.1.2.1  2005/03/22 12:04:03  nw
working draft of system and ag components.
 
*/