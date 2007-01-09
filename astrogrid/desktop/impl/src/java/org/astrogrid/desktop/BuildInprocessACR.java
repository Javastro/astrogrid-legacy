/*$Id: BuildInprocessACR.java,v 1.5 2007/01/09 16:26:19 nw Exp $
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
import org.apache.hivemind.Registry;
import org.astrogrid.AstroRuntime1;
import org.astrogrid.acr.builtin.Shutdown;
import org.astrogrid.desktop.framework.ACRInternal;
import org.astrogrid.desktop.hivemind.Launcher;

/** class that assembles and creates a new in-process ACR.
 *
 *
 *Poorly named, but is basically a factory for an in-process ACR.
 *Name can't be changed now - as is referenced in the Finder class in the public api.
 *
 *will retun a instance of ACR - seems like the most sensible variant to provide for in-process finder.
 * @author Noel Winstanley nw@jb.man.ac.uk 28-Jul-2005
 *
 */
public class BuildInprocessACR  {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(BuildInprocessACR.class);

    
    public BuildInprocessACR() {
        launcher = new Launcher();
        configureLauncher();
    }

	/** configure the launcher as the particular variant of acr to use.
	 * 
	 */
	protected void configureLauncher() {
		AstroRuntime1.configureLauncherAsACR(launcher);
	}
    
    protected final Launcher launcher;
 
    
    /** access the ACR instance, crreating and starting it if necessary */
    public ACRInternal getACR() {
    	// cuses acr to run on first access.
        return (ACRInternal)launcher.getRegistry().getService(ACRInternal.class);
    }
    
    /** access the hivemind registry, creating and starting it if ncessary */
    public Registry getHivemindRegistry() {
    	return launcher.getRegistry();
    }

    /** create ansd start the ACR instance */
    public void start() {
    	launcher.run();
    }
    /** stop the ACR */
    public void stop() {
        try {
           
            Shutdown sh = (Shutdown)launcher.getRegistry().getService(Shutdown.class);
            sh.halt();       
        } catch (Throwable e) {
            logger.error("Failed to call shutdown",e);
        }       
              
    }
}

