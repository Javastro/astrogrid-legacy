/*$Id: JavaClassCEAComponentManagerTest.java,v 1.2 2004/07/01 11:16:22 nw Exp $
 * Created on 10-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.component;

import org.astrogrid.config.SimpleConfig;

import java.net.URL;

import junit.framework.TestCase;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 10-Jun-2004
 *
 */
public class JavaClassCEAComponentManagerTest extends TestCase {
    /**
     * Constructor for JavaClassCEAComponentManagerTest.
     * @param arg0
     */
    public JavaClassCEAComponentManagerTest(String arg0) {
        super(arg0);
    }
    protected void setUp() throws Exception {
        super.setUp();
        setupConfigForJavaClassComponentManager();
        manager = createManager();
    }
    protected CEAComponentManager createManager() {
        return new JavaClassCEAComponentManager();
    }


    public static void setupConfigForJavaClassComponentManager() {
        URL registryURL = JavaClassCEAComponentManagerTest.class.getResource("/CEARegistryTemplate.xml");
         assertNotNull(registryURL);
        SimpleConfig.getSingleton().setProperty(EmptyCEAComponentManager.REGISTRY_TEMPLATE_URL,registryURL.toString());
         SimpleConfig.getSingleton().setProperty(EmptyCEAComponentManager.SERVICE_ENDPOINT_URL,"http://astrogrid.unit.test");
                
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
$Log: JavaClassCEAComponentManagerTest.java,v $
Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.2  2004/06/17 09:21:23  nw
finished all major functionality additions to core

Revision 1.1.2.1  2004/06/14 08:56:58  nw
factored applications into sub-projects,
got packaging of wars to work again
 
*/