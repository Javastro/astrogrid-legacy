/*$Id: StoreInternal.java,v 1.4 2007/06/18 16:27:15 nw Exp $
 * Created on 25-Oct-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.desktop.modules.ag;

import jdbm.RecordManager;

/** Internal-only interface to a store component - very low-level - hence internal only.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 25-Oct-2005
 *
 */
public interface StoreInternal {
    /**
     * access the configured JDBM manager
     
     */
    RecordManager getManager();
}

/* 
 $Log: StoreInternal.java,v $
 Revision 1.4  2007/06/18 16:27:15  nw
 javadoc

 Revision 1.3  2007/01/29 11:11:35  nw
 updated contact details.

 Revision 1.2  2005/11/24 01:13:24  nw
 merged in final changes from release branch.

 Revision 1.1.2.1  2005/11/17 21:06:26  nw
 moved store to be user-dependent
 debugged message monitoring.

 Revision 1.2  2005/11/10 12:05:53  nw
 big change around for vo lookout

 Revision 1.1  2005/11/01 09:19:46  nw
 messsaging for applicaitons.
 
 */