/*$Id: FileManagerTreeClientTest.java,v 1.2 2005/03/11 13:36:22 clq2 Exp $
 * Created on 10-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.filemanager.integration;

import org.astrogrid.community.common.ivorn.CommunityAccountIvornFactory;
import org.astrogrid.filemanager.BaseTest;
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.tree.FileManagerTreeClient;
import org.astrogrid.store.tree.TreeClient;
import org.astrogrid.store.tree.TreeClientTest;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 10-Mar-2005
 * *@todo all failing at the moment - not of prime importance. it's a legacy interface- to get working later.
 
 */
public class FileManagerTreeClientTest extends TreeClientTest {

    /** Construct a new FileManagerTreeClientTest
     * 
     */
    public FileManagerTreeClientTest() {
        super();
    }
    
    protected void setUp() throws Exception {
        super.setUp();
        TreeClient tc = new FileManagerTreeClient();
        this.setTestAdapter(tc);
        
        // check this is correct - don't know this community stuff.
        Ivorn ivorn = CommunityAccountIvornFactory.createLocal("frog");        
        
        this.setTestAccount(ivorn);
        this.setTestPassword("qwerty");
        this.initContainerName();       
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
        //clq2 commented temporaly on Noel's instruction
        //BaseTest.filestores = new Ivorn[]{BaseTest.FILESTORE_1,BaseTest.FILESTORE_2};        

    }    


}


/* 
$Log: FileManagerTreeClientTest.java,v $
Revision 1.2  2005/03/11 13:36:22  clq2
with merges from filemanager

Revision 1.1.2.1  2005/03/10 19:32:49  nw
bunch of tests for filemanager
 
*/