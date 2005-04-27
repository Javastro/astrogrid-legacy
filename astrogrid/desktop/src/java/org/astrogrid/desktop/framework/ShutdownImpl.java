/*$Id: ShutdownImpl.java,v 1.3 2005/04/27 13:42:41 clq2 Exp $
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

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.picocontainer.PicoContainer;

import java.util.Map;

/** implementation class for {@link Shutdown}
 * 
 * calls stop() on each module in the registry, before terminating.
 * @todo - dunno why this requires a reference to the registyr - doesn't seem to use it.
 *  */
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