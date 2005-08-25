/*$Id: BuildInprocessACR.java,v 1.2 2005/08/25 16:59:58 nw Exp $
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

import org.astrogrid.acr.builtin.Shutdown;
import org.astrogrid.desktop.framework.ACRImpl;
import org.astrogrid.desktop.framework.Bootloader;
import org.astrogrid.desktop.framework.MutableACR;
import org.astrogrid.desktop.framework.descriptors.DescriptorParser;
import org.astrogrid.desktop.framework.descriptors.DigesterDescriptorParser;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
    
    public MutableACR getACR() {
        return (MutableACR)getComponentInstanceOfType(MutableACR.class);
    }


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

