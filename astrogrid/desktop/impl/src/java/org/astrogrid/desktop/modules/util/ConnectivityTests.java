/**
 * 
 */
package org.astrogrid.desktop.modules.util;

import org.apache.axis.utils.NetworkUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.List;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestListener;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.EnumerationUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.collections.Predicate;
import org.astrogrid.acr.ServiceException;

/** Run through some tests to verify the connectivity.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Oct 9, 20072:40:19 PM
 */
public class ConnectivityTests extends TestSuite{
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(ConnectivityTests.class);
    /**
     * 
     */
    public ConnectivityTests() {
        addTest(new TestCase("Network interfaces"){
            protected void runTest() throws Throwable {
                List ifaces = EnumerationUtils.toList(NetworkInterface.getNetworkInterfaces());
                assertNotNull("No network interfaces found",ifaces);
                assertTrue("No network interfaces found",ifaces.size() > 0);
                // filter the list of interfaces to those which are non-local and non-loopback
                CollectionUtils.filter(ifaces,new Predicate() {
                    public boolean evaluate(Object arg0) {
                        NetworkInterface ni = (NetworkInterface)arg0;
                        return CollectionUtils.exists(EnumerationUtils.toList(ni.getInetAddresses()),new Predicate() {
                            public boolean evaluate(Object arg0) {
                                InetAddress i = (InetAddress)arg0;
                                return ! (i.isLinkLocalAddress() 
                                        //|| i.isSiteLocalAddress() 
                                        || i.isLoopbackAddress());
                            }
                        });
                    }
                });
             //   logger.info("Available network interfaces:" +ifaces);
                assertTrue("No external network interfaces found",! ifaces.isEmpty());
                
            }
        });
        addTest(new TestCase("DNS") {
            protected void runTest() {
                try {
                    InetAddress addr = InetAddress.getByName("www.google.com");
                    assertNotNull("Failed to resolve",addr);
                    
                } catch (UnknownHostException x) {
                    fail("Unable to resolve 'www.google.com' - suspect DNS is unavailable");
                }
            }
        });
        addTest(new TestCase("Access port 80") {
            protected void runTest()  throws Throwable{
                URL u = new URL("http://www.google.com:80");
                assertNotNull(u);
                try {
                    u.openConnection().connect();
                } catch (IOException x) {
                  fail("Unable to connect to port 80 at www.google.com");
                }
            }
        });
        /*@todo find a well-known webservice that responds to 8080
        addTest(new TestCase("Access port 8080") {
            protected void runTest()  throws Throwable{
                URL u = new URL("http://www.google.com:8080");
                assertNotNull(u);
                try {
                    u.openConnection().connect();
                } catch (IOException x) {
                  fail("Unable to connect to port 8080 at www.google.com");
                }
            }
        });
        */
    }
    
    // for stand-alone development.
    public static Test suite() {
        return new ConnectivityTests();
    }

    

}
