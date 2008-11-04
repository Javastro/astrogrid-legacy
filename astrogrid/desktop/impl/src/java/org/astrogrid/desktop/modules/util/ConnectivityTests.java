/**
 * 
 */
package org.astrogrid.desktop.modules.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.EnumerationUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** Self tests to verify network connectivity.
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
                final List ifaces = EnumerationUtils.toList(NetworkInterface.getNetworkInterfaces());
                assertNotNull("No network interfaces found",ifaces);
                assertTrue("No network interfaces found",ifaces.size() > 0);
                // filter the list of interfaces to those which are non-local and non-loopback
                CollectionUtils.filter(ifaces,new Predicate() {
                    public boolean evaluate(final Object arg0) {
                        final NetworkInterface ni = (NetworkInterface)arg0;
                        return CollectionUtils.exists(EnumerationUtils.toList(ni.getInetAddresses()),new Predicate() {
                            public boolean evaluate(final Object arg0) {
                                final InetAddress i = (InetAddress)arg0;
                                return ! (i.isLinkLocalAddress() 
                                        //|| i.isSiteLocalAddress() 
                                        || i.isLoopbackAddress());
                            }
                        });
                    }
                });
                logger.info("Available network interfaces:" +ifaces);
                assertTrue("No external network interfaces found",! ifaces.isEmpty());
                
            }
        });
        addTest(new TestCase("DNS") {
            protected void runTest() {
                try {
                    final InetAddress addr = InetAddress.getByName("www.google.com");
                    assertNotNull("Failed to resolve",addr);
                    
                } catch (final UnknownHostException x) {
                    logger.error("unable to resolve 'www.google.com - suspect DNS is unavailable",x);
                    fail("Unable to resolve 'www.google.com' - suspect DNS is unavailable");
                }
            }
        });
        addTest(new TestCase("Access port 80") {
            protected void runTest()  throws Throwable{
                final URL u = new URL("http://www.google.com:80");
                assertNotNull(u);
                try {
                    u.openConnection().connect();
                } catch (final IOException x) {
                    logger.error("unable to connect to port 80 at www.google.com - is firewall blocking port 80?",x);
                  fail("Unable to connect to port 80 at www.google.com - is firewall blocking port 80?");
                }
            }
        });
        addTest(new TestCase("Access port 8080") {
            protected void runTest()  throws Throwable{
                final URL u = new URL("http://rofr.ivoa.net:8080/ ");
                assertNotNull(u);
                try {
                    u.openConnection().connect();
                } catch (final IOException x) {
                    logger.error("unable to connect to port 8080 at rofr.ivoa.net - is firewall blocking port 8080?",x);
                  fail("Unable to connect to port 8080 at rofr.ivoa.net - is firewall blocking port 8080?");
                }
            }
        });
        
    }
    
    // for stand-alone development.
    public static Test suite() {
        return new ConnectivityTests();
    }

    

}
