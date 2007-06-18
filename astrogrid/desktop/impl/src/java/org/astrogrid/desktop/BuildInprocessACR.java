/*$Id: BuildInprocessACR.java,v 1.8 2007/06/18 16:19:14 nw Exp $
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

/** class that assembles and creates a new in-process ACR.
 *
 *
 *Poorly named, but is basically a factory for an in-process ACR.
 *Name can't be changed now - as is referenced in the Finder class in the public api.
 *
 *will retun a instance of ACR - seems like the most sensible variant to provide for in-process finder.
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
			main = Class.forName("org.astrogrid.Workbench1");
			m = ReflectionHelper.getMethodByName(main, "configureLauncherAsWorkbench");
			logger.info("Starting as Workbench");
		} catch (Exception e) {
			try {
				main = Class.forName("org.astrogrid.AstroRuntime1");
				m = ReflectionHelper.getMethodByName(main, "configureLauncherAsACR");
				logger.info("Starting as Astro Runtime");
			} catch (Exception f) {
				try {
					main = Class.forName("org.astrogrid.HeadlessAstroRuntime");
					m = ReflectionHelper.getMethodByName(main, "configureLauncherAsASR");					
					logger.info("Starting as Headless Astro Runtime");
				} catch(Exception g) {
					try {
						main = Class.forName("org.astrogrid.PlasticHub1");

						m = ReflectionHelper.getMethodByName(main, "configureLauncherAsHub");						
						logger.info("Starting as Plastic Hub");
					} catch (Exception h) {
						logger.fatal("Failed to find any AR main classes on classpath");
						return;
					}
				} 
			}
		}
		if (main == null || m == null) { // double check.
			logger.fatal("Failed to find any AR main class on the classpath");
		}
		
		try {
			m.invoke(null, new Object[] {launcher});
		} catch (Exception x) {
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
		SessionManagerInternal s = (SessionManagerInternal)getHivemindRegistry().getService(SessionManagerInternal.class);
		Principal principal = s.findSessionForKey(s.getDefaultSessionId());
		s.adoptSession(principal);    	
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

