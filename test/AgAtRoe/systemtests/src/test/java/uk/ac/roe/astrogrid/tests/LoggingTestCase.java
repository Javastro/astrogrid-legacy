package uk.ac.roe.astrogrid.tests;

import org.apache.log4j.Logger;

import junit.framework.TestCase;
/**
 * Makes a logger available to subclasses.
 * @author jdt
 *
 */
public abstract class LoggingTestCase extends TestCase {
    
    /**
     * Logger for this class
     */
    protected  Logger getLog() {
        return logger;
    }
    private Logger logger = Logger.getLogger(this.getClass()); 

    
}
