/*$Id: ComponentManagerTest.java,v 1.1 2004/03/03 01:13:42 nw Exp $
 * Created on 27-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.component;

import org.astrogrid.config.Config;
import org.astrogrid.config.PropertyNotFoundException;
import org.astrogrid.jes.impl.workflow.AbstractJobFactoryImpl;
import org.astrogrid.jes.impl.workflow.FileJobFactoryImpl;
import org.astrogrid.jes.job.BeanFacade;
import org.astrogrid.jes.job.JobFactory;
import org.astrogrid.jes.jobscheduler.Locator;
import org.astrogrid.jes.jobscheduler.locator.ConstantToolLocator;
import org.astrogrid.jes.jobscheduler.locator.XMLFileLocator;

import junit.framework.TestCase;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 27-Feb-2004
 *
 */
public class ComponentManagerTest extends TestCase {
    /**
     * Constructor for ComponentManagerTest.
     * @param arg0
     */
    public ComponentManagerTest(String arg0) {
        super(arg0);
    }
    
    protected void setUp() throws Exception {
        conf = new PropertiesConfig();
        cm = new ComponentManager(conf);
    }
    
    protected Config conf;
    protected ComponentManager cm;


public void testBuildFallbackFactory() {
    // should always return something, even with no config.
    assertNotNull(cm.buildFallbackFactory());
}

public void testBuildFallbackLocator() {
    assertNotNull(cm.buildFallbackLocator());
}

public void testBuildFallbackScheduler() {
    assertNotNull(cm.buildFallbackScheduler());
}

public void testBuildFacade() throws Exception {
    // no config - should return an inmemory.
    BeanFacade fac = cm.buildFacade();
    assertNotNull(fac);
    assertNotNull(fac.getJobFactory());
}

public void testBuildFileFacade() throws Exception {
    conf.setProperty(ComponentManager.JOB_FACTORY_IMPL_TYPE,"file");
    BeanFacade fac = cm.buildFacade();
    assertNotNull(fac);
    JobFactory factory = fac.getJobFactory();
    assertNotNull(factory);
    assertTrue(factory instanceof FileJobFactoryImpl);
        
    }

    public void testBuildLocator() throws Exception {
        Locator loc = cm.buildLocator();
        assertNotNull(loc);
        assertTrue(loc instanceof XMLFileLocator);
    }


    public void testBuildConstantLocator() throws Exception {
        conf.setProperty(ComponentManager.TOOL_LOCATOR_TYPE,"constant");
        conf.setProperty(ComponentManager.CONSTANT_TOOL_LOCATOR_ENDPOINT,"http://www.localhost");
        conf.setProperty(ComponentManager.CONSTANT_TOOL_LOCATOR_INTERFACE,"an.interface");
        Locator loc = cm.buildLocator();
        assertNotNull(loc);
        assertTrue(loc instanceof ConstantToolLocator);
    }
    
    public void testBuildPolicy() {
        assertNotNull(cm.buildPolicy());
    }
    
    public void testFailBuildDispatcher() throws Exception{
        try {
            cm.buildDispatcher(cm.buildFallbackLocator());
            fail("expected to fail");
        } catch (PropertyNotFoundException e) {
            // expected
        }
    }
    
    public void testAbsoluteBuildDispatcher() throws Exception {
        conf.setProperty(ComponentManager.ABSOLUTE_MONITOR_URL,"http://www.foo.org:8080/services/monitor");
        assertNotNull(cm.buildDispatcher(cm.buildFallbackLocator()));
    }

    /** know this one failes at the moment - need to work out way to resolve it */
    public void testRelativeBuildDispatcher() throws Exception {
        conf.setProperty(ComponentManager.RELATIVE_MONITOR_URL,"/services/monitor");
        assertNotNull(cm.buildDispatcher(cm.buildFallbackLocator()));
    }

}

/* 
$Log: ComponentManagerTest.java,v $
Revision 1.1  2004/03/03 01:13:42  nw
updated jes to work with regenerated workflow object model
 
*/