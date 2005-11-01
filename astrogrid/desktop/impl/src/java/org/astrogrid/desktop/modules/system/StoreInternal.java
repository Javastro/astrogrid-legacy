/*$Id: StoreInternal.java,v 1.1 2005/11/01 09:19:46 nw Exp $
 * Created on 25-Oct-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.desktop.modules.system;

import jdbm.RecordManager;

/** Internal-only interface to a store component - very low-level
 * @author Noel Winstanley nw@jb.man.ac.uk 25-Oct-2005
 *
 */
public interface StoreInternal {
    /**
     * access the configured JDBM manager
     * @return
     */
    RecordManager getManager();
}

/* 
 $Log: StoreInternal.java,v $
 Revision 1.1  2005/11/01 09:19:46  nw
 messsaging for applicaitons.
 
 */