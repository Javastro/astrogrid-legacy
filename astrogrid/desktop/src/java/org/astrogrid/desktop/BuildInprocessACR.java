/*$Id: BuildInprocessACR.java,v 1.1 2005/08/05 11:46:56 nw Exp $
 * Created on 28-Jul-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.builtin.Shutdown;
import org.astrogrid.desktop.framework.ACRImpl;
import org.astrogrid.desktop.framework.Bootloader;
import org.astrogrid.desktop.framework.descriptors.DescriptorParser;
import org.astrogrid.desktop.framework.descriptors.DigesterDescriptorParser;

import org.picocontainer.defaults.DefaultPicoContainer;

/** class that assembles and creates a new in-process ACR
 * @todo add options to control creation / appearance of guis?
 * @author Noel Winstanley nw@jb.man.ac.uk 28-Jul-2005
 *
 */
public class BuildInprocessACR extends DefaultPicoContainer {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(BuildInprocessACR.class);

    /** Construct a new StartACR
     * 
     */
    public BuildInprocessACR() {
        super();
        registerComponentImplementation(ACRImpl.class);
        registerComponentImplementation(DescriptorParser.class,DigesterDescriptorParser.class);
        registerComponentImplementation(Bootloader.class);
    }
    
    public ACR getACR() {
        return (ACR)getComponentInstanceOfType(ACR.class);
    }

    /*start the boot pico, assembling the acr.
     * runs later on the event dispatch thread - just to be on the safe side
     * @see org.picocontainer.Startable#start()
     
    public void start() {      
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    pico.start();
                }
            });     
    }*/
    public void start() {
        logger.info("----------------------------------------------------------------------------------------------------------------------------------------------");
        super.start();
    }
    public void stop() {
        try {
            Shutdown sh = (Shutdown)getACR().getService(Shutdown.class);
            sh.halt();             
        } catch (Throwable e) {
            logger.error("Failed to call shutdown",e);
        }
        super.stop();
        logger.info("-----------------------------------------------------------------------------------------------------------------------------------------------");
               
    }
}

