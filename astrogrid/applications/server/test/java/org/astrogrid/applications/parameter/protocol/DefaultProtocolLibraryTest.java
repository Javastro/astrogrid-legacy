/*$Id: DefaultProtocolLibraryTest.java,v 1.5 2008/09/13 09:51:04 pah Exp $
 * Created on 21-Jul-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.parameter.protocol;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.astrogrid.security.SecurityGuard;



/**
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Jul-2005
 *
 */
public class DefaultProtocolLibraryTest extends AbstractProtocolTestCase {

    /*
     * @see TestCase#setUp()
     */
    private static final String A_NEW_PROTOCOL = "a-new-protocol";
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        lib = (new DefaultProtocolLibraryFactory()).createLibrary();
        assertNotNull(lib);
    }
    
    DefaultProtocolLibrary lib ;
    private SecurityGuard secGuard = new SecurityGuard();//TODO put security guard with credentials
 
    /*
     * @see TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testAddProtocol() throws InaccessibleExternalValueException, UnrecognizedProtocolException, URISyntaxException {
        Protocol p = new Protocol() {

            public String getProtocolName() {
                return A_NEW_PROTOCOL;
            }

            public ExternalValue createIndirectValue(URI reference, SecurityGuard secGuard) throws InaccessibleExternalValueException {
                return new ExternalValue() {

                    public InputStream read() throws InaccessibleExternalValueException {
                        return null;
                    }

                    public OutputStream write() throws InaccessibleExternalValueException {
                        return null;
                    }
                };
            }
        };
       lib.addProtocol(p);
       assertTrue(lib.isProtocolSupported(A_NEW_PROTOCOL));
       assertNotNull(lib.getExternalValue(A_NEW_PROTOCOL + "://something", secGuard));
    }


    /*
     * Class under test for ExternalValue getExternalValue(ParameterValue)
     */
    public void testGetExternalValueParameterValue() {
        //@todo Implement getExternalValue().
    }

    /*
     * Class under test for ExternalValue getExternalValue(URI)
     */
    public void testGetExternalValueURI() throws InaccessibleExternalValueException, UnrecognizedProtocolException, URISyntaxException {
        URI uri = new URI("http://www.slashdot.org");
        assertNotNull(lib.getExternalValue(uri, secGuard));
    }

    /*
     * Class under test for ExternalValue getExternalValue(String)
     */
    public void testGetExternalValueString() throws InaccessibleExternalValueException, UnrecognizedProtocolException, URISyntaxException {
        assertNotNull(lib.getExternalValue("http://www.slashdot.org", secGuard));    
        // can't really test reading it.
    }
    
    public void testGetExternalValueFails() throws InaccessibleExternalValueException, UnrecognizedProtocolException, URISyntaxException {
        try {
        ExternalValue ev = lib.getExternalValue("unknown-scheme://something-else", secGuard);
        fail("Expected to throw an excepion");
        } catch (UnrecognizedProtocolException e) {
            // expected
        }
    }

    public void testListSupportedProtocols() {
        String[] arr = lib.listSupportedProtocols();
        assertNotNull(arr);
        assertTrue(arr.length > 0);
    }

    public void testIsProtocolSupported() {
       assertTrue(lib.isProtocolSupported("http"));
       assertFalse(lib.isProtocolSupported("wibble"));
    }

    public void testGetName() {
        assertNotNull(lib.getName());
    }

    public void testGetDescription() {
       assertNotNull(lib.getDescription());
    }

    public void testGetInstallationTest() {
        // no installation tests at the moment - need to change this test when installation tests are added
        assertNull(lib.getInstallationTest());
        }

}


/* 
$Log: DefaultProtocolLibraryTest.java,v $
Revision 1.5  2008/09/13 09:51:04  pah
code cleanup

Revision 1.4  2008/09/04 19:10:53  pah
ASSIGNED - bug 2825: support VOSpace
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2825
Added the basic implementation to support VOSpace  - however essentially untested on real deployement

Revision 1.3  2008/09/03 14:19:00  pah
result of merge of pah_cea_1611 branch

Revision 1.2.84.1  2008/03/26 17:15:39  pah
Unit tests pass

Revision 1.2  2005/08/10 17:45:10  clq2
cea-server-nww-improve-tests

Revision 1.1.2.1  2005/07/21 18:12:38  nw
fixed up tests - got all passing, improved coverage a little.
still could do with testing the java apps.
 
*/