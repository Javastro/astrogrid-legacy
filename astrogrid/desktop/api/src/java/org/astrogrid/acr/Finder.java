/*$Id: Finder.java,v 1.2 2005/08/12 08:45:15 nw Exp $
 * Created on 26-Jul-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr;

import net.ladypleaser.rmilite.Client;

import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.system.ApiHelp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/** Find or create an ACR server, and return an interface to that service.
 * 
 * <p>
      * first attempts to connect to a running instance using RMI, on a port defined in the file <tt>~/.acr-rmi-port</tt> (which is written by a running ACR instance<p>
     * failing that, tries to create an external instance (will only work if running under java web start), and then connect to that using RMI<p>
     * failing that, trys to create an instance internally (will only work if implementation classes are on classpath),<p>     
     * the interface returned will either be a  rmi stub or direct instance, depending on how the ACR was found.
 * @author Noel Winstanley nw@jb.man.ac.uk 26-Jul-2005
 * @example
 * <pre>
 * import org.astrogrid.acr.builtin.ACR;
 * import org.astrogrid.acr.Finder;
 * Finder f = new Finder();
 * ACR acr = f.find(); 
 * </pre>
 *@see org.astrogrid.acr.builtin.ACR
 */
public class Finder {
    /** Webstart URL for the ACR */
    public static final String ACR_JNLP_URL = "http://software.astrogrid.org/jnlp/astrogrid-desktop/astrogrid-desktop.jnlp";
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(Finder.class);

    /** Construct a new Finder
     * 
     */
    public Finder() {
        super();
    }
    
    /** find or create a running ACR server
     * 
     * <p>
     * first attempts to connect to a running instance, on a port defined in the file <tt>~/.acr-rmi-port</tt> (which is written by a running ACR instance<p>
     * failing that, tries to create an external instance (will only work if running under java web start) and then connect to that using RMI<p>
     * failing that, trys to create an instance internally (will only work if implementation classes are on classpath),<p>     
     * @return an interface to the running ACR - depending on how connected will either be a direct instance or a remote stub - although this makes no difference to the consumer.
     * @throws ACRException if all options fail
     * 
     * */
    public synchronized ACR find()  throws ACRException{
        if (acr == null) {
            acr= createACR();
        } 
        return acr;

    }
    
    /**
     * @throws NoAvailableACRException
     */
    private ACR createACR() throws ACRException {
        logger.info("Searching for acr");
        ACR result = null;
            try {
                result = connectExternal();
                if (result != null) {
                    return result;
                }
            } catch (Exception e) {
                logger.warn("Failed to connect to external acr",e);
            }                    
        // hmm, try something else then.
        try {
            createExternal();
            // need to wait some time to allow external to bootup (and maybe download).
            // @todo make the delays configurable later.
            long now = System.currentTimeMillis();
            long tooLong = now + (2 * 60 * 1000) ; // 2 minutes
            while (! configurationFile().exists() && System.currentTimeMillis() < tooLong) {
                Thread.sleep(5000); // 5 seconds.
            }
            result = connectExternal();
            if (result != null) {
                return result;
            }
        } catch (Exception e) {
              logger.warn("Failed to create external acr",e);
        }
        try {
                result = createInternal();
                if (result != null) {
                    return result;
                }    
            } catch (Exception e) {
                logger.warn("Failed to create internal acr",e);
            }       
            // fallen through everything.
            throw new ACRException("Failed to find or create an ACR to connect to");
    }

    /** connect to an external acr.
     * @return
     * @throws FileNotFoundException
     * @throws NumberFormatException
     * @throws IOException
     * @throws RemoteException
     * @throws NotBoundException
     */
    private ACR connectExternal() throws FileNotFoundException, NumberFormatException, IOException, RemoteException, NotBoundException {
        File conf = configurationFile();                
            if (conf.exists()) {
           logger.info("configuration file indicates an acr is already running");                
           BufferedReader br = new BufferedReader(new FileReader(conf));                
           int port = Integer.parseInt(br.readLine());
           logger.info("Port determined to be " + port);
           final Client client = new Client("localhost",port);
           final ApiHelp api = (ApiHelp)client.lookup(ApiHelp.class);
           return new ACR() {

            public Object getService(Class interfaceClass) throws ACRException, NotFoundException {
                if (interfaceClass.equals(ACR.class)) {
                    return this;
                }
                try {
                    registerListeners(interfaceClass);
                    return client.lookup(interfaceClass);
                } catch (RemoteException e) {
                    throw new ACRException(e);
                } catch (NotBoundException e) {
                    throw new NotFoundException(e);
                }
            }

            private void registerListeners(Class c) {
                Method[] arr = c.getMethods();
                for (int i = 0; i < arr.length; i++) {
                    Method m = arr[i];
                    Class[] ps = m.getParameterTypes();
                    for (int j = 0; j < ps.length; j++) {
                        maybeRegister(ps[j]);
                    }
                    Class ret = m.getReturnType();
                    maybeRegister(ret);
                }
            }
            private void maybeRegister(Class c) {
                if (c.isInterface() &&   c.getName().endsWith("Listener")) {
                    logger.debug("Exporting interface " + c.getName());
                    client.exportInterface(c);
                }
            }
            
            
            public Object getService(String componentName) throws ACRException, NotFoundException {
                String className = api.interfaceClassName(componentName);
                Class clazz = null;
                try {
                    clazz = Class.forName(componentName); // makes the interface more user friendly.
                } catch (ClassNotFoundException e) {
                    // try to resolve on the server
                    try {
                        clazz = Class.forName(className);
                    } catch (ClassNotFoundException e1) {
                        throw new NotFoundException(e1);
                    }
                }
               return getService(clazz);
            }
           };           
            } else {
                logger.info("No configuration file - suggests an acr instance is not running at the moment");
                return null;
            }
    }
   
    
    private static ACR acr;
    
    public static final File configurationFile() {
        File homeDir = new File(System.getProperty("user.home"));
        return new File(homeDir,".acr-rmi-port");
    }
    
    
    
    /** create an external instance of the acr 
     * would like to be able to do this by fetching the jnlp file - however, don't know that this process is 
     * running under javaws - and so need to find another library to control the system browser.
     * blechh.
     * for now, only create an external acr if running under javaws.
     * @todo add browser control lib to do this in other circumstances.
     * @todo think about using jnlp installer extensions to do this?
     * @return
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws MalformedURLException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws ClassNotFoundException
     */
    private void createExternal() throws SecurityException, NoSuchMethodException, MalformedURLException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        
        try {
             Class managerClass = Class.forName("javax.jnlp.ServiceManager");
             Method lookupMethod= managerClass.getMethod("lookup",new Class[]{String.class});
             Object basicService = lookupMethod.invoke(null,new Object[]{"javax.jnlp.BasicService"});
             Method showMethod = basicService.getClass().getMethod("showDocument",new Class[]{URL.class});
             URL url = new URL(ACR_JNLP_URL);
             showMethod.invoke(basicService,new Object[]{url});
             // that should have fired off the jnlp client - starting / downloading the acr.
        } catch (ClassNotFoundException e) {
            logger.info("Not running under java web start - unable to start an external acr");
            throw e;
        } 

    }
    
    private ACR createInternal() throws InstantiationException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        // all done without referencing the class by name - as may not be available on the classpath.
        try {
            Class buildClass = Class.forName("org.astrogrid.desktop.BuildInprocessACR");
            Object o = buildClass.newInstance();
            // start the acr.
            Method m = buildClass.getMethod("start",null);
            m.invoke(o,null);
            // return the acr instance
            m = buildClass.getMethod("getACR",null);
            return (ACR)m.invoke(o,null);
        } catch (ClassNotFoundException e) {            
            logger.info("ACR implementation classes not available - must connect to a remote acr",e);
        } 
        return null;
    }

}


/* 
$Log: Finder.java,v $
Revision 1.2  2005/08/12 08:45:15  nw
souped up the javadocs

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/