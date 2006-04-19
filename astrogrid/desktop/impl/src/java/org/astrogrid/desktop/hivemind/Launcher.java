/*$Id: Launcher.java,v 1.3 2006/04/19 01:02:24 nw Exp $
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

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.SAXParser;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.ErrorHandler;
import org.apache.hivemind.ModuleDescriptorProvider;
import org.apache.hivemind.Registry;
import org.apache.hivemind.Resource;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.hivemind.impl.RegistryBuilder;
import org.apache.hivemind.impl.XmlModuleDescriptorProvider;
import org.apache.hivemind.parse.DescriptorParser;
import org.apache.hivemind.parse.ModuleDescriptor;
import org.apache.hivemind.parse.XmlResourceProcessor;
import org.apache.hivemind.util.ClasspathResource;
import org.apache.hivemind.util.URLResource;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *Basic Launcher - assembles and starts some version of the acr.
 *
 *starts with the pre-requisite modules preloaded.
 *clients of this class should call {@link #addModuleURL(URL)}
 *or {@link #addModuleByName(String)} to add other modules into the system,
 *before calling {@link #run} or {@link #getRegistry()} to assemble and start the ACR 
 *<p>
 *order of adding resources doesn't matter
 *</p>
 *If using system requiredSystemProperties to configure the assembled system, these should be 
 *loaded before calling <tt>run()</tt>
 * 
 * @author Noel Winstanley nw@jb.man.ac.uk 15-Mar-2006
 *
 */
public final class Launcher implements Runnable {
    private final ClassResolver cl;
    private final List resources = new ArrayList();
    private Registry reg;
    // runtime defaults. - can be overridden by things in system properties
    protected final Properties defaults = new Properties(){{
        setProperty("org.apache.commons.logging.Log","org.apache.commons.logging.impl.SimpleLog");
   //     setProperty("org.apache.commons.logging.simplelog.defaultlog","error");
            setProperty("org.apache.commons.logging.simplelog.defaultlog","info");        
        setProperty("org.apache.commons.logging.simplelog.showlogname","true");
        setProperty("org.apache.commons.logging.simplelog.showShortLogname","true");
        setProperty("java.net.preferIPv4Stack","true");
        setProperty("java.net.preferIPv6Addresses","false");
        setProperty("ivoa.skynode.disabled","false");
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
        System.setSecurityManager(null); 
        Thread.currentThread().setContextClassLoader(Launcher.class.getClassLoader()); 
        Thread.currentThread().setName("Main Thread");
       // (new SwingSetup()).run();
        cl = new DefaultClassResolver(Thread.currentThread().getContextClassLoader());
        // add the preliminary resources.
        addModuleByName("hivemind");
        addModuleByName("hivemind-lib");
        addModuleByName("framework");        
        addModuleByName("builtin");
    }

    /** add a descriptor to the load set, pointed to by a URL
     * to be called before {@link #run}
  @param resourceName url pointing to to a hivemind descriptor.
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
    	// splice our defaults _behind_ existing system properties
    	for (Iterator i = defaults.entrySet().iterator(); i.hasNext(); ) {
    		Map.Entry e = (Map.Entry)i.next();
    		if (System.getProperty(e.getKey().toString()) == null) {
    			System.setProperty(e.getKey().toString(),e.getValue().toString());
    		}
    	}
    	
        // unpleasant hack - dive into internals to set entity resolver on the parser used.
        // otherwise, doesn't see external entity references.
        // doesn't seem to be a nicer way to do this.
        ModuleDescriptorProvider provider = new XmlModuleDescriptorProvider(cl,resources) {
            protected XmlResourceProcessor getResourceProcessor(ClassResolver arg0, ErrorHandler arg1) {
                // code copied from XmlResourceProcessor
                return new XmlResourceProcessor(arg0,arg1) {
                    protected ModuleDescriptor parseResource(Resource resource, SAXParser parser, DescriptorParser contentHandler) throws SAXException, IOException {
                       InputSource source = getInputSource(resource);
                       parser.parse(source, new ResolvingDescriptorParser(contentHandler));
                       return contentHandler.getModuleDescriptor();                       
                    }  
                    private InputSource getInputSource(Resource resource){
                        try{
                            URL url = resource.getResourceURL();
                            return new InputSource(url.openStream());
                        } catch (Exception e) {
                            throw new ApplicationRuntimeException("Missing Resource " + resource,
                                    resource, null, e);
                        }
                    }                    
                };
            }
        };        
        RegistryBuilder builder = new RegistryBuilder();
      //  builder.addModuleDescriptorProvider(new XmlModuleDescriptorProvider(cl)); //this one loads descriptors for libraries from default locations.
        builder.addModuleDescriptorProvider(provider); // this one loads our own descriptors
        reg = builder.constructRegistry(Locale.getDefault());        
    }
    

}


/* 
$Log: Launcher.java,v $
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