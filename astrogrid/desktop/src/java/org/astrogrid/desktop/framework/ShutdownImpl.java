/*$Id: ShutdownImpl.java,v 1.2 2005/04/13 12:59:11 nw Exp $
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

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.picocontainer.PicoContainer;

import java.util.Map;


public class ShutdownImpl implements Shutdown {
    private final DefaultModuleRegistry registry;
    private final Map modules;

    public ShutdownImpl(DefaultModuleRegistry registry, Map modules) {
        super();
        this.registry = registry;
        this.modules = modules;
    }

    public void halt() {
        CollectionUtils.forAllDo(modules.values(),new Closure() {
            public void execute(Object arg0) {
                ((PicoContainer)arg0).stop();
            }
        });
        System.exit(0);
    }
}

/* 
$Log: ShutdownImpl.java,v $
Revision 1.2  2005/04/13 12:59:11  nw
checkin from branch desktop-nww-998

Revision 1.1.2.2  2005/03/23 14:36:18  nw
got pw working

Revision 1.1.2.1  2005/03/18 12:09:31  nw
got framework, builtin and system levels working.
 
*/