/*$Id: MockRegistryAdminService.java,v 1.2 2004/07/01 11:16:22 nw Exp $
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
import org.astrogrid.registry.beans.resource.VODescription;
import org.astrogrid.registry.client.admin.RegistryAdminService;

import org.apache.axis.utils.XMLUtils;
import org.w3c.dom.Document;


public class MockRegistryAdminService extends RegistryAdminService {
    public int callCount = 0;
    public Document update(VODescription desc) throws RegistryException{
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
Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.1  2004/06/14 08:56:58  nw
factored applications into sub-projects,
got packaging of wars to work again
 
*/