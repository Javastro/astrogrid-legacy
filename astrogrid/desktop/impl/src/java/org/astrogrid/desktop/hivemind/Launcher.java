/*$Id: Launcher.java,v 1.12 2007/02/05 18:50:45 nw Exp $
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

import javax.swing.UIManager;

import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.ModuleDescriptorProvider;
import org.apache.hivemind.Registry;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.hivemind.impl.RegistryBuilder;
import org.apache.hivemind.impl.XmlModuleDescriptorProvider;
import org.apache.hivemind.util.ClasspathResource;
import org.apache.hivemind.util.URLResource;

/**
 *Assembles and starts some flavour of AR. Used by the main classes in {@link org.astrogrid}
 *
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
    private final ClassResolver cl;
    private final List resources = new ArrayList();
  
    private Registry reg;
    // runtime defaults. - can be overridden by things in system properties
    public static final Properties defaults = new Properties(){{
    	setProperty("java.net.preferIPv4Stack","true");
        setProperty("java.net.preferIPv6Addresses","false");
        
        setProperty("ivoa.skynode.disabled","true");
        // log4j
        setProperty("log4j.configuration","default-log4j.properties");
        // l & f
        String lafName = UIManager.getSystemLookAndFeelClassName();
        // for linux... see if we can do better than the Motif..
        if (lafName.equals("com.sun.java.swing.plaf.motif.MotifLookAndFeel")) {// yo mamma's so ugly.
        	try {
        		// see if GTK  is available.. java 1.4+
        		Class.forName("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
        		lafName = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
        	} catch (Exception noMatter) {
        		// don't care.
        	}
        } 
//        lafName = UIManager.getCrossPlatformLookAndFeelClassName();
        setProperty("swing.defaultlaf",lafName);
        // properties for apple - ignored in other contexts.
        // under java 1.4 on macs, I keep getting odd 3 > 2 errors.
        setProperty("apple.laf.useScreenMenuBar",Boolean.toString(
        			! System.getProperty("java.specification.version").equals("1.4")));

//	    setProperty("apple.awt.antialiasing","true");
//	    setProperty("apple.awt.textantialiasing","true");
        // disable this before release.
        //setProperty("acr.debug","true");
    }};    
    
    /** access the hivemind registry. creates acr if necessary */
    public Registry getRegistry() {
        if (reg == null) {
            this.run();
        } 
        return reg;
    }
        
    public Launcher() {
        // try fixing class loading bugs under jnlp
    	// original, no-security method - fixes classloading bugs under jnlp
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
        System.setProperty("java.protocol.handler.pkgs","org.astrogrid.desktop.protocol");
        // try fixing Protocol Handler loading bugs under jnlp
        // see http://forum.java.sun.com/thread.jspa?threadID=269651&messageID=1031648
        // however, the proposed work-around is quite brittle  - not happy doing this.
        // I need to enumerate all custom url protocols here, and make sure it's only called once.
        synchronized (Launcher.class) {
        	if (!haveSetHandlerFactory) {
        		haveSetHandlerFactory = true;
        		URL.setURLStreamHandlerFactory(new URLStreamHandlerFactory() {

        			public URLStreamHandler createURLStreamHandler(String protocol) {
        				if ("classpath".equals(protocol)) {
        					return new org.astrogrid.desktop.protocol.classpath.Handler();
        				} else if ("fallback".equals(protocol)) {
        					return new org.astrogrid.desktop.protocol.fallback.Handler();
        				} else if ("httpclient".equals(protocol)) {
        					return new org.astrogrid.desktop.protocol.httpclient.Handler();
        				} else {
        					return null; // defers back to system protocol handers.
        				}
        			} 
        		});
        	}
        }
       
        // add the preliminary resources.
        addModuleByName("hivemind");
        addModuleByName("hivemind-lib");
        addModuleByName("framework");        
        addModuleByName("builtin");
        addModuleByName("test");
    }

    // internal flag to keep track of whether the handler factory has been set in this vm yet.
    private static boolean haveSetHandlerFactory = false;
    
    /** add a descriptor to the load set, pointed to by a URL
     * to be called before {@link #run}
  @param resourceURL url pointing to to a hivemind descriptor.
     *  */
    public void addModuleURL(URL resourceURL){
    	this.resources.add(new URLResource(resourceURL));
    }

    /** add a descriptor to the load set, by name
     * to be called before {@link #run} 
     * @param resourceName for <i>resourceName</i>, there must be a resource named on the classpath named <tt>/org/astrogrid/desktop/hivemind/<i>resourceName</i>.xml</tt>
*/
     public  void addModuleByName(String resourceName) {   
       this.resources.add(new ClasspathResource(cl,"/org/astrogrid/desktop/hivemind/" + resourceName + ".xml"));
    }

    /** assemble the service and start it running */
    public void run() { 
    	spliceInDefaults();
    	
        RegistryBuilder builder = new RegistryBuilder();
        builder.addModuleDescriptorProvider(createModuleDescriptorProvider()); // this one loads our own descriptors
        reg = builder.constructRegistry(Locale.getDefault());        
    }

	/**
	 * 
	 */
	protected void spliceInDefaults() {
		// splice our defaults _behind_ existing system properties
    	for (Iterator i = defaults.entrySet().iterator(); i.hasNext(); ) {
    		Map.Entry e = (Map.Entry)i.next();
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
finished code.extruded plastic hub.

Revision 1.1.2.3  2006/04/04 10:31:25  nw
preparing to move to mac.

Revision 1.1.2.2  2006/03/28 13:47:35  nw
first webstartable version.

Revision 1.1.2.1  2006/03/22 18:01:30  nw
merges from head, and snapshot of development
 
*/