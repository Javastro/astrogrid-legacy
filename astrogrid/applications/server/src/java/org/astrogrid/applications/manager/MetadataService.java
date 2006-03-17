/*$Id: MetadataService.java,v 1.8 2006/03/17 17:50:58 clq2 Exp $
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

import java.net.URL;
import org.w3c.dom.Document;
import org.astrogrid.applications.CeaException;
import org.astrogrid.registry.beans.v10.wsinterface.VOResources;

/** Interface into the meta-data / decscription / registry entries part of a cea server.
 *  - base of all description for this server. This is now the sole place where interactions that either set or get registry metadata about the services implemented by CEA.
 * @author Noel Winstanley nw@jb.man.ac.uk 21-May-2004
 * @author pharriso@eso.org 02-Jun-2005 - merged in the former ProvidesVODescription interface - no reason for them to be separate
 *
 */
public interface MetadataService {
 
    /** return the entry to lodge in the registry
     * 
     * @return  xmldocument of a VODescription 
     * @throws CeaException
     */
    public Document returnRegistryEntry() throws CeaException;
    
    /**
     *  access the vodescription for this server.
     * @return the object model of the VO description for this service
     */
    public VOResources getVODescription()  throws Exception;
    
    /**
     * Get a URL leading to the template document for the 
     * service and application registration.
     */
    public URL getRegistrationTemplate();

}


/* 
$Log: MetadataService.java,v $
Revision 1.8  2006/03/17 17:50:58  clq2
gtr_1489_cea correted version

Revision 1.6  2006/03/07 21:45:26  clq2
gtr_1489_cea

Revision 1.3.38.2  2005/12/22 10:13:26  gtr
I removed unused methods.

Revision 1.3.38.1  2005/12/21 14:44:35  gtr
Changed to make the registration template available through the InitServlet.

Revision 1.3  2005/07/05 08:27:00  clq2
paul's 559b and 559c for wo/apps and jes

Revision 1.2.172.1  2005/06/09 08:47:32  pah
result of merging branch cea_pah_559b into HEAD

Revision 1.2.158.2  2005/06/03 16:01:48  pah
first try at getting commandline execution log bz#1058

Revision 1.2.158.1  2005/06/02 14:57:28  pah
merge the ProvidesVODescription interface into the MetadataService interface

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