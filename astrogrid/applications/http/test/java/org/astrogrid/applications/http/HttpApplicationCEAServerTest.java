/*$Id: HttpApplicationCEAServerTest.java,v 1.4 2004/09/01 15:42:26 jdt Exp $
 * Created on 30-July-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.http;

import junit.framework.TestCase;

import org.astrogrid.applications.component.CEAComponentManager;
import org.astrogrid.applications.component.HttpApplicationCEAComponentManager;
import org.astrogrid.applications.http.registry.RegistryQuerier;
import org.astrogrid.applications.http.test.TestRegistryQuerier;
import org.astrogrid.applications.manager.MetadataService;

/** test of a cea server configured with the javaclass backend.
 * @author jdt/Noel Winstanley nw@jb.man.ac.uk 21-Jun-2004
 * @todo exercise other components of server here..
 *
 */
public class HttpApplicationCEAServerTest extends TestCase {
    /**
     * Constructor 
     * @param arg0
     */
    public HttpApplicationCEAServerTest(String arg0) {
        super(arg0);
    }
    
    protected void setUp() throws Exception {
        container = new HttpApplicationCEAComponentManager();
        //temp hack to replace the registry querier with our test one
        container.getContainer().unregisterComponent(RegistryQuerier.class);
        container.getContainer().registerComponentInstance(RegistryQuerier.class, new TestRegistryQuerier());
    }
    
    protected CEAComponentManager container;
    
    public void testMetadata() throws Exception {
        MetadataService ms = container.getMetadataService();
        assertNotNull(ms);
        String reg = ms.returnRegistryEntry();
        assertNotNull(reg);
        
    }
    
    
}


/* 
$Log: HttpApplicationCEAServerTest.java,v $
Revision 1.4  2004/09/01 15:42:26  jdt
Merged in Case 3

Revision 1.1.2.3  2004/08/02 18:05:28  jdt
Added more tests and refactored the test apps to be set up
from xml.

Revision 1.1.2.2  2004/07/30 16:59:50  jdt
limping along.

Revision 1.1.2.1  2004/07/30 11:02:30  jdt
Added unit tests, refactored the RegistryQuerier anf finished off
HttpApplicationCEAComponentManager.

Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.1  2004/07/01 01:42:46  nw
final version, before merge
 
*/