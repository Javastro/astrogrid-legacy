/*$Id: FileManagerClientTest.java,v 1.2 2005/03/11 13:36:22 clq2 Exp $
 * Created on 02-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.filemanager.integration;

import org.astrogrid.filemanager.BaseTest;
import org.astrogrid.store.Ivorn;

import java.io.IOException;
import java.net.URISyntaxException;

/** reuse one of the system-tests that dave wrote, as an integration test.
 * @author Noel Winstanley nw@jb.man.ac.uk 02-Mar-2005
 */
public class FileManagerClientTest extends org.astrogrid.filemanager.client.FileManagerClientTest {

    /** Construct a new FileManagerClientTest
     * 
     */
    public FileManagerClientTest() {
        super();
    }

    protected void setUp() throws Exception {   
        super.setUp();
    }
    /** override this - in original test, this sets up keys for mocks, etc.
     * in this setting we want none of this - rely on the environment we've got already.
     * @see org.astrogrid.filemanager.BaseTest#setupKeys()
     */
    protected void setupKeys() throws IOException {
        // override unit test settings to points to stores deployed in integration tests.
        try {
            BaseTest.FILESTORE_1 = new Ivorn("ivo://org.astrogrid.localhost/filestore-one");
            BaseTest.FILESTORE_2 = new Ivorn("ivo://org.astrogrid.localhost/filestore-two");            
        } catch (URISyntaxException e) {
            fail(e.getMessage());
        }
        BaseTest.filestores = new Ivorn[]{BaseTest.FILESTORE_1,BaseTest.FILESTORE_2};        

    }
}


/* 
$Log: FileManagerClientTest.java,v $
Revision 1.2  2005/03/11 13:36:22  clq2
with merges from filemanager

Revision 1.1.4.2  2005/03/10 19:32:49  nw
bunch of tests for filemanager

Revision 1.1.4.1  2005/03/02 01:49:17  nw
made a start on integration tests for filemanager
 
*/