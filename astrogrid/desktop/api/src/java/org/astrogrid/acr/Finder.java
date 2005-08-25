/*$Id: Finder.java,v 1.5 2005/08/25 16:59:44 nw Exp $
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
import org.astrogrid.acr.builtin.Shutdown;
import org.astrogrid.acr.builtin.ShutdownListener;
import org.astrogrid.acr.system.ApiHelp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import java.awt.HeadlessException;
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

import javax.swing.JOptionPane;

/** Find or create an ACR server, and return an interface to that service.
 * 
 * <p>
      * first attempts to connect to a running instance using RMI, on a port defined in the file <tt>~/.acr-rmi-port</tt> (which is written by a running ACR instance<p>
     * failing that, tries to create an external instance (will only work if running under java web start), and then connect to that using RMI<p>
     * failing that, trys to create an instance internally (will only work if implementation classes are on classpath),<p>     
     * the interface returned will either be a  rmi stub or direct instance, depending on how the ACR was found.
     * <p>
     * No matter how the acr is found, the ACR returned is a singleton - it is stored in this class for simple access the next time
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
     * The instance returned is a singleton - i.e. all subsequent calls to {@link #find} will return the same object.
     * @throws ACRException if all options fail
     * 
     * */
    public synchronized ACR find()  throws ACRException{
        if (acr == null) {
            acr= createACR();
            try { // attempt to register a listener, it it'll let me: use it to remove singleton when host vanishes.
                Shutdown sd = (Shutdown)acr.getService(Shutdown.class);
                sd.addShutdownListener(new ShutdownListener() {
                    public void halting() {
                        logger.info("Host ACR shutting down");
                        Finder.this.acr = null;
                    }
                    public String lastChance() {
                        return null; // won't ever object.
                    }
                });
            } catch (ACRException e) {
                logger.warn("Failed to register shutdown listener - no matter",e);
            }
                
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
     
        try {
                result = createInternal();
                if (result != null) {
                    return result;
                }    
            } catch (Exception e) {
                logger.warn("Failed to create internal acr",e);
            }
            // hmm, try starting external service
            try {
                createExternal();
                // need to wait some time to allow external to bootup (and maybe download).
                // @todo make this a dialogue for information / cancel, and a retry-loop.
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
            // finally - try prompting the user.
            int dialogueResult;
            try {
            dialogueResult = JOptionPane.showConfirmDialog(null,"<html><b>Please start the ACR by hand</b><br>When started press 'Ok'. To halt press 'Cancel'","Unable to automatically start ACR",JOptionPane.OK_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE);
            } catch (HeadlessException e) {
                logger.warn("Not running in a ui environment - can't promt");
                dialogueResult = JOptionPane.NO_OPTION;
            }
            
            if (dialogueResult == JOptionPane.YES_OPTION) { // try to connect again.
                try {
                    result = connectExternal();
                    if (result != null) {
                        return result;
                    }
                } catch (Exception e) {
                    logger.warn("Failed to connect to external acr, after user claimed to start one.",e);
                    // could loop here?
                }                  
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
   
    
    private ACR acr;
    
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
     * @throws ClassNotFoundException
     */
    private void createExternal() throws SecurityException, NoSuchMethodException, MalformedURLException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        Method showMethod = null;
        Object methodTarget = null;        
        try {
             Class managerClass = Class.forName("javax.jnlp.ServiceManager");
             Method lookupMethod= managerClass.getMethod("lookup",new Class[]{String.class});
             methodTarget = lookupMethod.invoke(null,new Object[]{"javax.jnlp.BasicService"});
             showMethod = methodTarget.getClass().getMethod("showDocument",new Class[]{URL.class});
        } catch (ClassNotFoundException e) {
            logger.info("Not running under java web start");
        }
        if (showMethod == null) { // try something else.
            try {
            Class jdicClass = Class.forName("org.jdesktop.jdic.desktop.Desktop");
            showMethod= jdicClass.getMethod("browse",new Class[]{URL.class});    
            } catch (ClassNotFoundException e1) {
                logger.info("Not running with jdic libs");
            }
        }
        
        if (showMethod == null) {
            throw new ClassNotFoundException("Can't find any class that can control the system browser");
        }
        URL url = new URL(ACR_JNLP_URL);
        showMethod.invoke(methodTarget,new Object[]{url});        
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
Revision 1.5  2005/08/25 16:59:44  nw
1.1-beta-3

Revision 1.4  2005/08/16 13:16:23  nw
doc fix

Revision 1.3  2005/08/12 12:42:05  nw
finished documentation effort.

Revision 1.2  2005/08/12 08:45:15  nw
souped up the javadocs

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/