package org.astrogrid.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.builtin.Shutdown;
import org.astrogrid.acr.builtin.ShutdownListener;

/**
 * This is an amended version of org.astrogrid.acr.Finder
 * It does not attempt to connect to an externally running AR,
 * but creates an internal instance.
 * 
 * 
 * @author jl99
 * @author noel winstanley
 */
public class Finder {
    
    // configure the logger earliest.
    static {
        if (System.getProperty("log4j.configuration") == null && isArOnClasspath() ) {
            System.setProperty("log4j.configuration","default-log4j.properties");
        }
    }
    
    /**Part of the internal implementation 
     * @exclude
     * <p />
     * Refactored as an static public class - previously was an anonymous class, and RmiLite seemed to be unable to call it
     *  - producing a nice stack trace on shutdown. Same code, but as a named public static class works fine.
     * @author Noel Winstanley

     */
    public static final class FinderCleanupShutdownListener implements ShutdownListener {
        public FinderCleanupShutdownListener(final Finder f) {
            this.f = f;
        }
        private final Finder f;
        public void halting() {
            logger.info("Host AR shutting down");
            f.acr = null;
        }

        public String lastChance() {
            return null; // won't ever object.
        }
    }

    /**
     * Commons Logger for this class
     * @exclude
     */
    protected static final Log logger = LogFactory.getLog(Finder.class);
    
    private String pathToPropertiesFile ; 

    /** Construct a new Finder
     * @param pathToPropertiesFile For setting system properties prior to starting the AR
     */
    public Finder( String pathToPropertiesFile ) {
        super();
        this.pathToPropertiesFile = pathToPropertiesFile ;
    }
    
    /** Construct a new Finder
     * 
     */
    public Finder() {
    	this( null ) ;
    }

    /** Find or create an Astro Runtime (AR) instance.
     * @return an interface to the Astro Runtime - in this version of Finder, a direct instance.
     * @note The instance returned is a singleton - i.e. all subsequent calls to  this method
     * will return the same object.
     * @throws ACRException if all options fail
     * */
    public ACR find() throws ACRException {
        if (acr == null) {
            acr= createACR();
            try { // attempt to register a listener, it it'll let me: use it to remove singleton when host vanishes.
                final Shutdown sd = (Shutdown)acr.getService(Shutdown.class);               
                sd.addShutdownListener(new FinderCleanupShutdownListener(this));

            } catch (final ACRException e) {
                logger.warn("Failed to register shutdown listener - no matter",e);
            }

        }
        return acr;
    }

    /**
     * This version of Finder only attempts to create an internal AR (ie: on the classpath). 
     * @throws ACRException
     */
    private ACR createACR() throws ACRException {
    	logger.info("Searching for AR");
    	ACR result = null;    

    	// try starting internal service
    	if (isArOnClasspath()) {
    		try {
   			
    			//
    			// If a properties file was present
    			// Set up the enclosed properties ...
    			if( pathToPropertiesFile != null ) {
    				Properties props = new Properties() ;
        			props.load( new FileInputStream( new File( pathToPropertiesFile ) ) ) ;
        			Enumeration<Object> keys = props.keys() ;
        			while( keys.hasMoreElements() ) {
        				String key = (String)keys.nextElement() ;
        				System.setProperty( key, props.getProperty(key) ) ;
        			}
    			}
    			    			
    			result = createInternal();
    			if (result != null) {
    				return result;
    			}     
    		} catch (final Exception e) {
    			logger.warn("Failed to create internal AR",e);
    			e.printStackTrace();
    		}
    	}            
    	// fallen through everything.
    	throw new ACRException("Failed to start internal AR on the classpath");
    }


    /**

     * @throws FileNotFoundException
     * @throws IOException
     */
    protected int parseConfigFile() throws IOException {
        final File conf = configurationFile();  
        if (!conf.exists()) {
            logger.info("No configuration file - suggests an AR instance is not running at the moment");
            throw new FileNotFoundException(conf.getAbsolutePath());
        }

        logger.info("configuration file indicates an AR is already running");                
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(conf));
            final int port = Integer.parseInt(br.readLine());
            logger.info("Port determined to be " + port);
            return port;
        } finally {
            if (br != null) {
                try {
                    br.close(); //Otherwise the file can be locked and left undeleted when the ACR shuts down.
                } catch (final IOException e) {
                }
            }
        }
    }


    private ACR acr;

    /** The AR RMI connection file.
     * <p/>
     * This is <tt>~/.acr-rmi-port</tt> */
    public static final File configurationFile() {
        final File homeDir = new File(System.getProperty("user.home"));
        return new File(homeDir,".acr-rmi-port");
    }


    /** attempt to start an in-process ar
     * pre-req - isArOnClasspath == true
     * @return
     * @throws InstantiationException
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private ACR createInternal() throws InstantiationException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        // all done without referencing the class by name - as may not be available on the classpath.
        final Class buildClass = findInProcessARClass(); 
        final Object o = buildClass.newInstance();
        // start the acr.
        Method m = buildClass.getMethod("start",(Class[])null);
        m.invoke(o,(Object[])null);
        // return the acr instance
        m = buildClass.getMethod("getACR",(Class[])null);
        final ACR acr =  (ACR)m.invoke(o,(Object[])null);
        return acr;
    }

    /**
     * @return
     */
    private static Class findInProcessARClass() {
        Class buildClass;
        try {
            buildClass = Class.forName("org.astrogrid.desktop.BuildInprocessACR");
        } catch (final ClassNotFoundException e) {            
            //can't log at this point.
            //logger.info("AstroRuntime implementation classes not on classpath - must connect to a remote AR",e);
            return null;
        }
        return buildClass;
    }

    private static boolean isArOnClasspath() {
        return findInProcessARClass() != null;
    }



}
