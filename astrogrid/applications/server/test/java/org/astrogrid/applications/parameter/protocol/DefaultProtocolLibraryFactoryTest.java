/*$Id: DefaultProtocolLibraryFactoryTest.java,v 1.3 2008/09/03 14:19:00 pah Exp $
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
import junit.framework.TestCase;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Jul-2005
 *
 */
public class DefaultProtocolLibraryFactoryTest extends AbstractProtocolTestCase {
    
    

    public void testCreateLibrary() {
        DefaultProtocolLibraryFactory fac = new DefaultProtocolLibraryFactory();
        ProtocolLibrary lib = fac.createLibrary();
        assertNotNull(lib);
        assertTrue(lib.isProtocolSupported("http"));
        assertTrue(lib.isProtocolSupported("ftp"));
        assertTrue(lib.isProtocolSupported("ivo"));
        assertTrue(lib.isProtocolSupported("file"));
    }

}


/* 
$Log: DefaultProtocolLibraryFactoryTest.java,v $
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