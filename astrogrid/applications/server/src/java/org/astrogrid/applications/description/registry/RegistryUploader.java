/*
 * $Id: RegistryUploader.java,v 1.2 2004/07/01 11:16:22 nw Exp $
 * 
 * Created on 24-Mar-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.description.registry;

import org.astrogrid.applications.component.ProvidesVODescription;
import org.astrogrid.component.descriptor.ComponentDescriptor;
import org.astrogrid.registry.client.admin.RegistryAdminService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.picocontainer.Startable;

import junit.framework.Test;
import junit.framework.TestCase;

/**
 * Component that will lodge this services' vodescription with the registry.
 * <p />
 * Implements the startable interface - calling {@link #start} on this component (or on the container that holds it)
 * causes the entry to be lodged in the registry. 
 * @author Paul Harrison (pah@jb.man.ac.uk) 24-Mar-2004
 * @version $Name:  $
 * @since iteration5
 */
public class RegistryUploader implements Startable, ComponentDescriptor{
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(RegistryUploader.class);

   private final ProvidesVODescription provider;

   private final RegistryAdminLocator adminLocator;

/**
 *  Construct a new RegistryUploader
 * @param provider component that provides the vodescription to upload
 * @param adminLocator compoent that provides a registry delegate.
 */
   public RegistryUploader(ProvidesVODescription provider, RegistryAdminLocator adminLocator){
      this.provider = provider;
      this.adminLocator = adminLocator;
    }
    
    public void write() throws Exception
    {
       RegistryAdminService delegate = adminLocator.getClient();
       delegate.update(provider.getVODescription());
       
    }

    /**just calls {@link #write}
     * @see org.picocontainer.Startable#start()
     */
    public void start() {
        try {
            this.write();
        } catch (Exception e) {
            logger.error("start()", e);
        }
    }

    /**
     * @see org.picocontainer.Startable#stop()
     */
    public void stop() {
    }

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getName()
     */
    public String getName() {
        return "Registry entry uploader";
    }

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        
        try {
            return "Registers application list with registry" 
            + "\n registry status is " + adminLocator.getClient().getCurrentStatus();
        }
        catch (Exception e) {
            return "problem accessing registry \n" + e.getMessage();
        }
         
    }

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getInstallationTest()
     */
    public Test getInstallationTest() {
        return new InstallationTest("testAdminLocator");
    }
    
    public class InstallationTest extends TestCase {

        public InstallationTest(String arg0) {
            super(arg0);
        }
        
        public void testAdminLocator() throws Exception{
            RegistryAdminService delegate = adminLocator.getClient();           
            assertNotNull(delegate);
            String status = delegate.getCurrentStatus();
            System.out.println(status);
        }

}

}
