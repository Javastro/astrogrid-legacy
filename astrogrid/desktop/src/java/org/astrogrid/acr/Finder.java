/*$Id: Finder.java,v 1.1 2005/08/05 11:46:55 nw Exp $
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

import org.astrogrid.acr.builtin.ACR;

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

/** Find or create a running instance of the ACR
 * @author Noel Winstanley nw@jb.man.ac.uk 26-Jul-2005
 *
 */
public class Finder {
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
    
    /** find or create a running instance of the acr 
     * 
     * first attempts to connect to a running instance, on a port defined in a configuration file.
     * failing that, trys to create an instance internally,
     * if this fails, will throws a NoAvailableACRException
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
           return new RmiLiteACR(port);
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
Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/