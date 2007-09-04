/*
 * $Id: RegistryLoaderTask.java,v 1.5 2007/09/04 15:17:38 clq2 Exp $
 * 
 * Created on 13-Jul-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.deployment ;

import java.io.File ;
import java.net.URL ;

import org.apache.tools.ant.Task ;
import org.apache.tools.ant.BuildException ;

import org.astrogrid.registry.client.admin.RegistryAdminService ;
import org.astrogrid.registry.client.RegistryDelegateFactory;

/**
 * An Ant task to load a registry entry to a remote Registry.
 * @author Paul Harrison (pah@jb.man.ac.uk) 13-Jul-2004
 * @version $Name:  $
 * @since iteration5
 */
public class RegistryLoaderTask
    extends Task
    {
    public RegistryLoaderTask()
        {
        super() ;
        }

    /**
     * Public constructor.
     *
     */
    public RegistryLoaderTask(Task parent)
        {
        super() ;
        setProject(parent.getProject()) ;
        }

    /**
     * Initialise our Task.
     *
     */
    public void init()
        throws BuildException
        {
 
         }

    /**
     * Our Registry service endpoint.
     *
     */
    private String registry ;

    /**
     * Get our Registry service endpoint.
     *
     */
    public String getRegistry()
        {
        return this.registry ;
        }

    /**
     * Set our Registry service endpoint.
     *
     */
    public void setRegistry(String value)
        {
        this.registry = value ;
        }

    /**
     * Our Registry data file.
     *
     */
    private String data ;

    /**
     * Get our Registry data file.
     *
     */
    public String getData()
        {
        return this.data ;
        }

    /**
     * Set our Registry data file.
     *
     */
    public void setData(String value)
        {
        this.data = value ;
        }

    /**
     * Execute our Task.
     *
     */
    public void execute()
        throws BuildException
        {
        // Try registering our services.
        try {
            
            log("sending  "+data+" - to registry at "+ registry);
            // Create our RegistryAdminService client.
            RegistryAdminService registry = RegistryDelegateFactory.createAdmin(
                new URL(
                    this.registry
                    )
                ) ;
            //
            // Send the document to the service.
            registry.updateFromFile(
                new File(
                    this.data
                    )
                ) ;
            }
        catch (Exception ouch)
            {
            throw new BuildException(ouch) ;
            }
        }
    }

