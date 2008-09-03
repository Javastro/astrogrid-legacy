/*$Id: MockRegistryAdminService.java,v 1.2 2008/09/03 14:19:02 pah Exp $
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
import org.astrogrid.registry.client.admin.RegistryAdminService;
import org.astrogrid.registry.client.admin.v0_1.UpdateRegistry;

import org.apache.axis.utils.XMLUtils;
import org.w3c.dom.Document;


public class MockRegistryAdminService extends UpdateRegistry implements RegistryAdminLocator{
    public int callCount = 0;
    
    public MockRegistryAdminService() {
    	//hello
    }
    
    public Document update(Document doc) throws RegistryException{
        callCount++;
        try {
            return XMLUtils.newDocument();
        } catch (Exception e) {
            throw new RegistryException(e);
        }
    }

    public RegistryAdminService getClient() throws Exception {
	return this;
    }
}

/* 
$Log: MockRegistryAdminService.java,v $
Revision 1.2  2008/09/03 14:19:02  pah
result of merge of pah_cea_1611 branch

Revision 1.1.2.1  2008/09/03 12:22:54  pah
improve unit tests so that they can run in single eclipse gulp

Revision 1.7.2.1  2008/04/04 15:46:08  pah
Have got bulk of code working with spring - still need to remove all picocontainer refs
ASSIGNED - bug 1611: enhancements for stdization holding bug
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611

Revision 1.7  2007/09/28 18:03:36  clq2
apps_gtr_2303

Revision 1.5.100.1  2007/09/24 13:15:05  gtr
This is recovered from the trunk to deal with the upgraded registry.

Revision 1.6  2007/09/04 15:42:41  clq2
KMB changes for PAL

Revision 1.5.98.1  2007/08/16 12:22:01  KevinBenson
small change to use some changes made to the registry client dealing with 0.1 and 1.0 contracts and now an abstract super class in the reg client.

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