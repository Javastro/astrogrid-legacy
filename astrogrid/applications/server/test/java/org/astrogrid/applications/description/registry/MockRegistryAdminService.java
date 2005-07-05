/*$Id: MockRegistryAdminService.java,v 1.5 2005/07/05 08:27:00 clq2 Exp $
 * Created on 02-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.description.registry;

import org.astrogrid.registry.RegistryException;
//FIXME this is using v9 semantics
import org.astrogrid.registry.beans.resource.VODescription;
import org.astrogrid.registry.client.admin.UpdateRegistry;

import org.apache.axis.utils.XMLUtils;
import org.w3c.dom.Document;


public class MockRegistryAdminService extends UpdateRegistry {
    public int callCount = 0;
    public Document update(Document doc) throws RegistryException{
        callCount++;
        try {
            return XMLUtils.newDocument();
        } catch (Exception e) {
            throw new RegistryException(e);
        }
    }
}

/* 
$Log: MockRegistryAdminService.java,v $
Revision 1.5  2005/07/05 08:27:00  clq2
paul's 559b and 559c for wo/apps and jes

Revision 1.4.120.1  2005/06/09 08:47:32  pah
result of merging branch cea_pah_559b into HEAD

Revision 1.4.106.1  2005/05/31 12:58:26  pah
moved to v10 schema interpretation - this means that the authorityID is read directly with the applicaion "name"

Revision 1.4  2004/08/28 07:13:42  pah
change the update override to the dom version

Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.1  2004/06/14 08:56:58  nw
factored applications into sub-projects,
got packaging of wars to work again
 
*/