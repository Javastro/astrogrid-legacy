/*$Id: SsapImpl.java,v 1.9 2007/03/09 15:07:03 KevinBenson Exp $
 * Created on 27-Jan-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ivoa;

import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.Ssap;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;

/** implementation of a component that does ssap queries.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 27-Jan-2006
 * @future - at moment inherits most of functionality from the siap impl. need to watch when specs diverge.
 */
public class SsapImpl extends SiapImpl implements Ssap {

    /** Construct a new SsapImpl
     * @param reg
     * @param ms
     */
    public SsapImpl(Registry reg, MyspaceInternal ms) {
        super(reg, ms);
    }

    /**
     * @see org.astrogrid.acr.ivoa.Ssap#getRegistryQuery()
     */
    public String getRegistryAdqlQuery() {
        return "Select * from Registry where " +
        " @xsi:type like '%SimpleSpectrumAccess'  " ;
        //@issue       " and (not (@status = 'inactive' or @status='deleted') )";
    }
    public String getRegistryQuery() {
    	return getRegistryAdqlQuery();
    }
    
    public String getRegistryXQuery() {
		return "//RootResource[matches(@xsi:type,'SimpleSpectrumAccess') and ( @status = 'active')]";
    	
    }

  
    
}


/* 
$Log: SsapImpl.java,v $
Revision 1.9  2007/03/09 15:07:03  KevinBenson
Changees to use RootResource to signal root node of registry query and to use standard xquery matches method.

Revision 1.8  2007/01/29 11:11:36  nw
updated contact details.

Revision 1.7  2006/08/31 21:34:46  nw
minor tweaks and doc fixes.

Revision 1.6  2006/08/15 10:13:50  nw
migrated from old to new registry models.

Revision 1.5  2006/06/27 19:13:07  nw
adjusted todo tags.

Revision 1.4  2006/06/15 09:49:08  nw
improvements coming from unit testing

Revision 1.3  2006/04/21 13:48:11  nw
mroe code changes. organized impoerts to reduce x-package linkage.

Revision 1.2  2006/03/13 18:29:32  nw
fixed queries to not restrict to @status='active'

Revision 1.1  2006/02/02 14:49:49  nw
added starter implementation of ssap.
 
*/