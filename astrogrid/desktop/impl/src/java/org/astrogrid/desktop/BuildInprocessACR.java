/*$Id: BuildInprocessACR.java,v 1.3 2006/04/18 23:25:47 nw Exp $
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
import org.astrogrid.acr.builtin.Shutdown;
import org.astrogrid.desktop.framework.ACRInternal;
import org.astrogrid.desktop.hivemind.Launcher;

/** class that assembles and creates a new in-process ACR
 * @todo resolve where this fits in.
 * @author Noel Winstanley nw@jb.man.ac.uk 28-Jul-2005
 * @deprecated kept around for now for backwards compatability.
 */
public class BuildInprocessACR  {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(BuildInprocessACR.class);

    
    public BuildInprocessACR() {
        launcher = new Launcher();
    }
    
    private final Launcher launcher;
 
    
    public ACRInternal getACR() {
        return (ACRInternal)launcher.getRegistry().getService(ACRInternal.class);
    }


    public void start() {
        logger.info("----------------------------------------------------------------------------------------------------------------------------------------------");
        launcher.run();
    }
    public void stop() {
        try {
           
            Shutdown sh = (Shutdown)launcher.getRegistry().getService(Shutdown.class);
            sh.halt();             
        } catch (Throwable e) {
            logger.error("Failed to call shutdown",e);
        }       
        logger.info("-----------------------------------------------------------------------------------------------------------------------------------------------");
               
    }
}

