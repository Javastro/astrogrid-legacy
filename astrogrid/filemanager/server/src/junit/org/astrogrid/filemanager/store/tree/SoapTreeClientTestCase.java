/*$Id: SoapTreeClientTestCase.java,v 1.2 2005/03/11 13:37:06 clq2 Exp $
 * Created on 25-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.filemanager.store.tree;

import org.astrogrid.community.common.policy.data.AccountData;
import org.astrogrid.filemanager.client.FileManagerClientFactory;
import org.astrogrid.filemanager.client.FileManagerClientTest;
import org.astrogrid.filemanager.client.FileManagerNode;
import org.astrogrid.filemanager.server.TestFileManager;
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.tree.FileManagerTreeClient;
import org.astrogrid.store.tree.TreeClientTest;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 25-Feb-2005
 *
 */
public class SoapTreeClientTestCase extends TreeClientTest {

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        TestFileManager.deployLocal();
        
        FileManagerClientFactory fac = new FileManagerClientFactory();
        this.setTestAdapter( new FileManagerTreeClient(fac));

        // Setup our test account.
        this.setTestAccount(accountIvorn) ;
        this.setTestPassword("qwerty") ;        
        // copied from FileManagerClientTest#registerAccounts
        AccountData acc = FileManagerClientTest.createCommunityAccount(accountIdent);
        FileManagerClientTest.registerPassword(accountIdent,"qwerty");
        FileManagerNode accNode = FileManagerClientTest.createFileManagerAccount(accountIdent);
        Ivorn homeIvorn = new Ivorn(accNode.getMetadata().getNodeIvorn().toString());                
        acc = FileManagerClientTest.registerCommunityHome(accountIdent,homeIvorn);

        
        //
        // Setup our test container name.
        this.initContainerName() ;        
        
    }
    
    /** define custom suite, that includes a decorator that does setup of soap service once, before all tests are run */
    public static Test suite() {
        TestSuite suite = new TestSuite(SoapTreeClientTestCase.class.getName());
        TestSuite tests = new TestSuite(SoapTreeClientTestCase.class);
        suite.addTest(new TestSetup(tests){
            protected void setUp() throws Exception {
                TestFileManager.prepareWsdd();
            }            
        });
        return suite;
    }    

}


/* 
$Log: SoapTreeClientTestCase.java,v $
Revision 1.2  2005/03/11 13:37:06  clq2
new filemanager merged with filemanager-nww-jdt-903-943

Revision 1.1.2.1  2005/03/01 23:43:37  nw
split code inito client and server projoects again.

Revision 1.1.2.1  2005/03/01 15:07:36  nw
close to finished now.

Revision 1.1.2.1  2005/02/27 23:03:12  nw
first cut of talking to filestore
 
*/