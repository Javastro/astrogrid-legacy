/*$Id: HttpApplicationCEAServerTest.java,v 1.6 2004/09/07 08:13:20 pah Exp $
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

import java.net.URL;

import junit.framework.TestCase;

import org.astrogrid.applications.component.CEAComponentManager;
import org.astrogrid.applications.component.EmptyCEAComponentManager;
import org.astrogrid.applications.component.JavaClassCEAComponentManager;
import org.astrogrid.applications.http.registry.RegistryQuerier;
import org.astrogrid.applications.http.test.TestRegistryQuerier;

import org.astrogrid.applications.manager.MetadataService;
import org.astrogrid.config.SimpleConfig;

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
    	super.setUp();
    	manager = new HttpApplicationCEAComponentManager();
        //@TODO temp hack to replace the registry querier with our test one
    	manager.getContainer().unregisterComponent(RegistryQuerier.class);
    	manager.getContainer().registerComponentInstance(RegistryQuerier.class, new TestRegistryQuerier());
    }
    
    protected CEAComponentManager manager;
    
     public void testIsValid() {
        manager.getContainer().verify();
    }
    
    // another validity test - should always be true, but we'll test it anyhow.
    public void testVerifyRequiredComponents() {
        assertNotNull(manager.getContainer().getComponentInstanceOfType(EmptyCEAComponentManager.VerifyRequiredComponents.class));
    }
    public void testGetController() {
        assertNotNull(manager.getExecutionController());
    }
    public void testGetMetaData() {
        assertNotNull(manager.getMetadataService());
    }

    public void testInformation() {
        System.out.println(manager.information());
    }    
    
    
}


/* 
$Log: HttpApplicationCEAServerTest.java,v $
Revision 1.6  2004/09/07 08:13:20  pah
remove incorrect import

Revision 1.5  2004/09/01 16:36:50  jdt
Was failing....perhaps a change in the server code since we branched?

Revision 1.4  2004/09/01 15:42:26  jdt
Merged in Case 3


 
*/