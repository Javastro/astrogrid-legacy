/*$Id: BuildInprocessACR.java,v 1.11 2008/11/04 14:35:52 nw Exp $
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

import java.lang.reflect.Method;
import java.security.Principal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hivemind.Registry;
import org.astrogrid.acr.builtin.Shutdown;
import org.astrogrid.desktop.framework.ACRInternal;
import org.astrogrid.desktop.framework.ReflectionHelper;
import org.astrogrid.desktop.framework.SessionManagerInternal;
import org.astrogrid.desktop.hivemind.Launcher;

/** Assembles and creates a new in-process AR.
 *
 *
 *Poorly named, but is basically a factory for an in-process ACR.
 *Name can't be changed now - as is referenced in the Finder class in the public api.
 *
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 28-Jul-2005
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

	protected  void configureLauncher() {
		
		//AstroRuntime1.configureLauncherAsACR(launcher);
		// see what 'main' main classes are available, and use these, falling back by order of functinality
		Class main;
		Method m;
		try {
			main = Class.forName("org.astrogrid.VODesktop1");
			m = ReflectionHelper.getMethodByName(main, "configureLauncherAsVODesktop");
			logger.info("Starting VODesktop");
		} catch (final Exception e) {
						logger.fatal("Failed to find AR main class on classpath");
						return;					
		}
		if (main == null || m == null) { // double check.
			logger.fatal("Failed to find any AR main class on the classpath");
			return;
		}
		
		try {
			m.invoke(null, launcher);
		} catch (final Exception x) {
			logger.fatal("Failed to configure launcher",x);
		} 
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
		// now associate the current thread with the default AR session
    	//@fixme - document how to adopt a session when calling AR from new threads.
		final SessionManagerInternal s = (SessionManagerInternal)getHivemindRegistry().getService(SessionManagerInternal.class);
		final Principal principal = s.findSessionForKey(s.getDefaultSessionId());
		s.adoptSession(principal);    	
    }
    /** stop the ACR */
    public void stop() {
        try {
           
            final Shutdown sh = (Shutdown)launcher.getRegistry().getService(Shutdown.class);
            sh.halt();       
        } catch (final Throwable e) {
            logger.error("Failed to call shutdown",e);
        }       
              
    }
}

