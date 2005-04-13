/*$Id: WebServer.java,v 1.2 2005/04/13 12:59:12 nw Exp $
 * Created on 15-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.desktop.modules.system;

import org.picocontainer.Startable;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 15-Mar-2005
 *
 */
public interface WebServer  extends Startable, UrlRoot{
    /** key that module registry is accessible from servlet context by */
    String MODULE_REGISTRY = "module-registry";

    public abstract String getKey();

    public abstract int getPort();
}

/* 
 $Log: WebServer.java,v $
 Revision 1.2  2005/04/13 12:59:12  nw
 checkin from branch desktop-nww-998

 Revision 1.1.2.1  2005/03/18 12:09:31  nw
 got framework, builtin and system levels working.
 
 */