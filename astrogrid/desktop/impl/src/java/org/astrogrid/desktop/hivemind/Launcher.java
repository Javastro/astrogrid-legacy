/*$Id: Launcher.java,v 1.32 2009/11/16 11:38:12 mbt Exp $
 * Created on 15-Mar-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.hivemind;

import java.net.URL;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.ModuleDescriptorProvider;
import org.apache.hivemind.Registry;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.hivemind.impl.RegistryBuilder;
import org.apache.hivemind.impl.XmlModuleDescriptorProvider;
import org.apache.hivemind.util.AbstractResource;
import org.apache.hivemind.util.ClasspathResource;
import org.apache.hivemind.util.URLResource;
import org.astrogrid.desktop.modules.system.HivemindFileSystemManager;
import org.astrogrid.desktop.modules.system.pref.WorkaroundTransformerFactory;

/**
 *Assembles hivemind descriptors and instantiates AR.
 *
 *<p/>
 * Called from the main classes in {@link org.astrogrid}
 *
 *<p/>
 *starts with the pre-requisite modules preloaded.
 *clients of this class should call {@link #addModuleURL(URL)}
 *or {@link #addModuleByName(String)} to add other modules into the system,
 *before calling {@link #run} or {@link #getRegistry()} to assemble and start the ACR 
 *<p>
 *order of adding resources doesn't matter
 *</p>
 *If using system properties to configure the assembled system, these should be 
 *loaded before calling <tt>run()</tt>
 * 
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 15-Mar-2006
 *
 */
public class Launcher implements Runnable {

    /** a URLStreamHandlerFactory that allows additional handler factories to be specified later.
	 * @author Noel.Winstanley@manchester.ac.uk
	 * @since Jul 17, 20071:13:04 PM
	 */
	private static final class ExtensibleStreamHandler implements
			URLStreamHandlerFactory {
		private URLStreamHandlerFactory followOn;

		public URLStreamHandler createURLStreamHandler(final String protocol) {
			if ("classpath".equals(protocol)) {
				return new org.astrogrid.desktop.protocol.classpath.Handler();
			} else if ("fallback".equals(protocol)) {
				return new org.astrogrid.desktop.protocol.fallback.Handler();
			} else {
				if (followOn == null) {
					return null; // defers back to system protocol handers.
				} else {
					return followOn.createURLStreamHandler(protocol);
				}
			}
		}

		/** add in a subsequent handler factory - can only be called once.
		 * @param streamHandlerFactory
		 */
		public void setFollowOnHandler(final URLStreamHandlerFactory streamHandlerFactory) {
			followOn = streamHandlerFactory;
		}
	}

	// runtime defaults. - can be overridden by things in system properties
    public static final Properties defaults = new Properties(){{
        // set in preferences now.
    	//setProperty("java.net.preferIPv4Stack","true");
       // setProperty("java.net.preferIPv6Addresses","false");
        
        setProperty("ivoa.skynode.disabled","true");
        // log4j
        setProperty("log4j.configuration","default-log4j.properties");
        // l & f - @todo look at what configuration of fonts, etc takes place in topcat.
        // properties for apple - ignored in other contexts.
        // under java 1.4 on macs, I keep getting odd 3 > 2 errors.
        setProperty("apple.laf.useScreenMenuBar",Boolean.toString(
        			! System.getProperty("java.specification.version").equals("1.4")));

//	    setProperty("apple.awt.antialiasing","true");
//	    setProperty("apple.awt.textantialiasing","true");
        // disable this before release.
        //setProperty("acr.debug","true");
    }};    

    //static initializer - ensures this gets done really early on
    // important - as needs to happen before first logged message.
    // must come before the declaration of the logger.
    static {
    	spliceInDefaults();    	
    }    
    
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog("startup");

    private final ClassResolver cl;
    private final List<AbstractResource> resources = new ArrayList<AbstractResource>();
  
    private Registry reg;
    
    /** access the hivemind registry. creates acr if necessary */
    public Registry getRegistry() {
        if (reg == null) {
            this.run();
        } 
        return reg;
    }
      
    /** write out some initial properties to the log */
    private void writeLogHeader() {
    	logger.info("Java version: " + System.getProperty("java.version"));
    	logger.info("Java vendor: " + System.getProperty("java.vendor"));
    	logger.info("Java home: " + System.getProperty("java.home"));
    	logger.info("Operating system: " + System.getProperty("os.name") + " " + System.getProperty("os.version") +  " " + System.getProperty("os.arch"));
    }
    

    public Launcher() {

    	writeLogHeader();
        // try fixing class loading bugs under jnlp
    	// original, no-security method - fixes classloading bugs under jnlp
        // TODO: doubt this is needed now, as we're not doing JNLP anymore. 
    	System.setSecurityManager(null); 
        Thread.currentThread().setContextClassLoader(Launcher.class.getClassLoader()); 
        Thread.currentThread().setName("Main Thread");
        cl = new DefaultClassResolver(Thread.currentThread().getContextClassLoader());
        
        /* experimental - not finisheid yet
    	if (System.getSecurityManager() == null) {
    		System.setSecurityManager(new SecurityManager());
    	}
    	*/
        
        // should be enough - but isn't/
       // System.setProperty("java.protocol.handler.pkgs","org.astrogrid.desktop.protocol");
        // try fixing Protocol Handler loading bugs under jnlp
        // see http://forum.java.sun.com/thread.jspa?threadID=269651&messageID=1031648
        // however, the proposed work-around is quite brittle  - not very confiednet about this
        // also need to ensure that this method is only called once per JVM.
        extensibleStreamHandler = new ExtensibleStreamHandler();
        synchronized (Launcher.class) {
        	if (!haveSetHandlerFactory) {
        		logger.info("Programmatically setting url handlers");
        		haveSetHandlerFactory = true;
				URL.setURLStreamHandlerFactory(extensibleStreamHandler);
        	}
        }

        // work around bug with java1.6 and writing java.util.prefs.
        WorkaroundTransformerFactory.fixJaxp();
       
        // add the preliminary resources.
        addModuleByName("hivemind");
        addModuleByName("hivemind-lib");
        addModuleByName("hiveutils");
        addModuleByName("hivelock.core");
        
        addModuleByName("framework");        
        addModuleByName("builtin");
        addModuleByName("test");
        addModuleByName("performance");
    }

    // internal flag to keep track of whether the handler factory has been set in this vm yet.
    private static boolean haveSetHandlerFactory = false;

	private final  ExtensibleStreamHandler extensibleStreamHandler;
    
    /** add a descriptor to the load set, pointed to by a URL
     * to be called before {@link #run}
  @param resourceURL url pointing to to a hivemind descriptor.
     *  */
    public void addModuleURL(final URL resourceURL){
    	logger.info("Adding moduleURL " + resourceURL);
    	this.resources.add(new URLResource(resourceURL));
    }

    /** add a descriptor to the load set, by name
     * to be called before {@link #run} 
     * @param resourceName for <i>resourceName</i>, there must be a resource named on the classpath named <tt>/org/astrogrid/desktop/hivemind/<i>resourceName</i>.xml</tt>
*/
     public  void addModuleByName(final String resourceName) {   
    	 logger.info("Adding module " + resourceName);
       this.resources.add(new ClasspathResource(cl,"/org/astrogrid/desktop/hivemind/" + resourceName + ".xml"));
    }

    /** assemble the service and start it running */
    public void run() { 
    	logger.info("Running");
    	
        final RegistryBuilder builder = new RegistryBuilder();
        builder.addModuleDescriptorProvider(createModuleDescriptorProvider()); // this one loads our own descriptors
        reg = builder.constructRegistry(Locale.getDefault());      
        
        // extract vfs, and splice into stream handler..
        logger.info("Setting vfs as url protocol handler");
        final FileSystemManager vfs = (FileSystemManager) reg.getService(HivemindFileSystemManager.class);
        extensibleStreamHandler.setFollowOnHandler(vfs.getURLStreamHandlerFactory());
    }

	/**
	 * 
	 */
	public static void spliceInDefaults() {
		// splice our defaults _behind_ existing system properties
    	for (final Iterator i = defaults.entrySet().iterator(); i.hasNext(); ) {
    		final Map.Entry e = (Map.Entry)i.next();
    		if (System.getProperty(e.getKey().toString()) == null) {
    			System.setProperty(e.getKey().toString(),e.getValue().toString());
    		}
    	}
	}

	/** Creates the provider / factory for descirptors
	 * @return a module provider.
	 */
	public ModuleDescriptorProvider createModuleDescriptorProvider() {
        return  new XmlModuleDescriptorProvider(cl,resources) ;
	}
    

}


/* 
$Log: Launcher.java,v $
Revision 1.32  2009/11/16 11:38:12  mbt
remove hardcoded loopback for JSAMP server - now defaults to loopback

Revision 1.31  2009/04/24 17:00:33  nw
release candidate fixes

Revision 1.30  2009/04/08 12:40:55  nw
samp configuraiton fix.

Revision 1.29  2009/04/06 11:29:15  nw
added generic types

Revision 1.28  2008/12/01 23:29:23  nw
removed some jnlp stuff

Revision 1.27  2008/11/04 14:35:49  nw
javadoc polishing

Revision 1.26  2008/04/23 10:51:04  nw
removed unused protocol handler.

Revision 1.25  2008/03/06 12:03:55  nw
Complete - task 330: recategorize preferences.
disabled jackdaw by default.
removed startup-zapping of ipv4 setting - now set in preferences instead.

Revision 1.24  2008/02/27 13:16:35  mbt
Invoke WorkaroundTransformerFactory.fixJaxp() - see bugzilla 2595

Revision 1.23  2007/11/26 12:01:48  nw
added framework for progress indication for background processes

Revision 1.22  2007/09/21 16:35:15  nw
improved error reporting,
various code-review tweaks.

Revision 1.21  2007/08/13 19:29:47  nw
merged mark's and noel's changes.

Revision 1.20.4.1  2007/08/09 19:08:20  nw
Complete - task 126: Action to 'Reveal' location of remote results in FileExplorer

Complete - task 122: plastic messaging of myspace resources

Revision 1.20  2007/07/26 18:21:44  nw
merged mark's and noel's branches

Revision 1.19.2.1  2007/07/26 11:15:10  nw
Incomplete - task 49: Implement file Tasks

Incomplete - task 105: polish vfs for cea.

Revision 1.19  2007/07/23 12:05:28  nw
Complete - task 68: fix build, and logger config.

Revision 1.18  2007/07/17 15:22:55  nw
Complete - task 55: VFS stream handler

Revision 1.17  2007/06/18 16:20:33  nw
improved startup logging.

Revision 1.16  2007/04/18 15:47:10  nw
tidied up voexplorer, removed front pane.

Revision 1.15  2007/03/22 19:03:48  nw
added support for sessions and multi-user ar.

Revision 1.14  2007/03/20 10:19:00  nw
possible fix to laf problems on linux.

Revision 1.13  2007/03/08 17:44:02  nw
first draft of voexplorer

Revision 1.12  2007/02/05 18:50:45  nw
worked-around jnlp bug.

Revision 1.11  2007/01/29 11:11:37  nw
updated contact details.

Revision 1.10  2007/01/09 16:27:35  nw
uses protocol handlers instead of overriding the resolver.

Revision 1.9  2006/10/11 10:35:25  nw
started thinking about security managers

Revision 1.8  2006/08/31 21:11:10  nw
improved look and feel usage.

Revision 1.7  2006/06/28 11:35:34  nw
added apple properties for all platforms

Revision 1.6  2006/06/15 09:41:27  nw
added test module

Revision 1.5  2006/05/17 23:57:46  nw
documentation improvements.

Revision 1.4  2006/04/21 13:48:11  nw
mroe code changes. organized impoerts to reduce x-package linkage.

Revision 1.3  2006/04/19 01:02:24  nw
turned up logging for now

Revision 1.2  2006/04/18 23:25:43  nw
merged asr development.

Revision 1.1.2.4  2006/04/14 02:45:01  nw
finished code.
extruded plastic hub.

Revision 1.1.2.3  2006/04/04 10:31:25  nw
preparing to move to mac.

Revision 1.1.2.2  2006/03/28 13:47:35  nw
first webstartable version.

Revision 1.1.2.1  2006/03/22 18:01:30  nw
merges from head, and snapshot of development
 
*/
