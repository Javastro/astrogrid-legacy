/*$Id: MetadataService.java,v 1.2 2004/07/01 11:16:22 nw Exp $
 * Created on 21-May-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.manager;

import org.astrogrid.applications.CeaException;

/** Interface into the meta-data / decscription / registry entries part of a cea server.
 *  - base of all description for this server.
 * @author Noel Winstanley nw@jb.man.ac.uk 21-May-2004
 *
 */
public interface MetadataService {
 
    /** return the entry to lodge in the registry
     * 
     * @return stringified xmldocument of a VODescription 
     * @throws CeaException
     */
    public String returnRegistryEntry() throws CeaException;


}


/* 
$Log: MetadataService.java,v $
Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.2  2004/07/01 01:42:46  nw
final version, before merge

Revision 1.1.2.1  2004/06/17 09:21:23  nw
finished all major functionality additions to core

Revision 1.1.2.1  2004/06/14 08:56:58  nw
factored applications into sub-projects,
got packaging of wars to work again

Revision 1.1.2.1  2004/05/28 10:23:10  nw
checked in early, broken version - but it builds and tests (fail)
 
*/