/*$Id: SingleInstanceListener.java,v 1.7 2007/09/21 16:35:13 nw Exp $
 * Created on 21-Mar-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.system.UI;
import org.astrogrid.desktop.framework.ReflectionHelper;

/**
 * implementation of the single instance listener interface
 * - used in jnlp. If running under webstart, will attempt to register as a handler.
 * @future extend to be an open-file handler too.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 21-Mar-2006
 *
 */
public class SingleInstanceListener implements InvocationHandler {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(SingleInstanceListener.class);

    public SingleInstanceListener(UI ui) {
        super();
        this.ui = ui;
        try {
            // get jnlp manager.
        Class managerClass= Class.forName("javax.jnlp.ServiceManager");
        Method lookupMethod = ReflectionHelper.getMethodByName(managerClass,"lookup");
        // try to get instance of singleInstanceService
        Object singleInstanceService = lookupMethod.invoke(null,new Object[]{"javax.jnlp.SingleInstanceService"});
        // construct a single instance listener. - pointing back to our invoke() method.
        Class listenerClass = Class.forName("javax.jnlp.SingleInstanceListener");
        Object listenerInstance = Proxy.newProxyInstance(this.getClass().getClassLoader(),new Class[]{listenerClass},this); 
        // register listener..
        Method registerMethod = ReflectionHelper.getMethodByName(singleInstanceService.getClass(),"addSingleInstanceListener");
        registerMethod.invoke(singleInstanceService,new Object[]{listenerInstance});
        } catch (Throwable t) { // oh well, can live without it..
            logger.info("Failed to register single instance listener - probably not running under Webstart");
            logger.debug(t);
        }
    }
    
    private final UI ui;
    
    /** implementation method for dynamically generated singleInstanceListener.
     * shows the gui, if hidden.
     * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        ui.show();
        return null;
        
    }

    
}


/* 
$Log: SingleInstanceListener.java,v $
Revision 1.7  2007/09/21 16:35:13  nw
improved error reporting,
various code-review tweaks.

Revision 1.6  2007/09/04 13:38:37  nw
added debugging for EDT, and adjusted UI to not violate EDT rules.

Revision 1.5  2007/01/29 11:11:36  nw
updated contact details.

Revision 1.4  2006/06/27 19:18:32  nw
adjusted todo tags.

Revision 1.3  2006/06/27 10:41:36  nw
documentation.

Revision 1.2  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.1.2.2  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.1.2.1  2006/03/22 18:01:30  nw
merges from head, and snapshot of development
 
*/