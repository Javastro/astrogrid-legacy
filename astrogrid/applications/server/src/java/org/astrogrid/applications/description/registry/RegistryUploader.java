/*
 * $Id: RegistryUploader.java,v 1.8 2008/09/03 14:19:02 pah Exp $
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

import java.net.URL;


import junit.framework.Test;
import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.manager.MetadataService;
import org.astrogrid.component.descriptor.ComponentDescriptor;
import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.registry.client.admin.RegistryAdminService;

/**
 * Component that will lodge this services' vodescription with the registry.
 * <p />
 * Implements the startable interface - calling {@link #start} on this component (or on the container that holds it)
 * causes the entry to be lodged in the registry. 
 * @author Paul Harrison (pah@jb.man.ac.uk) 24-Mar-2004
 * @version $Name:  $
 * @since iteration5
 */
public class RegistryUploader implements  ComponentDescriptor{
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(RegistryUploader.class);

   private final MetadataService provider;

   private final RegistryAdminLocator adminLocator;

/**
 *  Construct a new RegistryUploader
 * @param provider component that provides the vodescription to upload
 * @param adminLocator compoent that provides a registry delegate.
 */
   public RegistryUploader(MetadataService provider, RegistryAdminLocator adminLocator){
      this.provider = provider;
      this.adminLocator = adminLocator;
    }
    
    /** performs the upload of the vodescription to the server.
     * @throws Exception
     * @see #start()
     */
    public void write(String endpoint) throws Exception
    {
       RegistryAdminService delegate;
       if(endpoint == null)
       {
       delegate = adminLocator.getClient();
       }
       else
       {
          delegate = RegistryDelegateFactory.createAdmin(new URL(endpoint));
       }
       logger.info("registering this service with registry");//TODO would be nice if the registry delegate would identify the endpoint
       delegate.update(provider.getServerDescription());
       
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
    
    /**Installation test for {@link RegistryUploader} - tests that the provided admin locator does provide a connection to a valid registry service 
     * @author Noel Winstanley nw@jb.man.ac.uk 26-Jul-2004
     *
     */
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
