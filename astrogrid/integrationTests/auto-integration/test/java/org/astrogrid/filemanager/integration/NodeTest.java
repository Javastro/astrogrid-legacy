/*$Id: NodeTest.java,v 1.2 2005/03/11 13:36:22 clq2 Exp $
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

import org.astrogrid.filemanager.BaseTest;
import org.astrogrid.filemanager.client.delegate.NodeDelegate;
import org.astrogrid.filemanager.common.BundlePreferences;
import org.astrogrid.filemanager.resolver.NodeDelegateResolver;
import org.astrogrid.filemanager.resolver.NodeDelegateResolverImpl;
import org.astrogrid.store.Ivorn;

import java.io.IOException;
import java.net.URISyntaxException;

/** reuse one of the system tests that dave wrote, as an integration test.
 * @author Noel Winstanley nw@jb.man.ac.uk 10-Mar-2005
 *
 */
public class NodeTest extends org.astrogrid.filemanager.client.delegate.NodeTest {

    /** Construct a new NodeTest
     * 
     */
    public NodeTest() {
        super();
    }

    /**
     * @see org.astrogrid.filemanager.client.delegate.NodeDelegateTest#createDelegate()
     */
    protected NodeDelegate createDelegate() throws Exception {
        Ivorn ivorn = new Ivorn("ivo://org.astrogrid.localhost/filemanager-one");
        BundlePreferences bundlePrefs = new BundlePreferences();        
        NodeDelegateResolver resolver = new NodeDelegateResolverImpl(bundlePrefs);
        return  resolver.resolve(ivorn);
    }

    /** disable setup of any keys
     * 
     *  override this - in original test, this sets up keys for mocks, etc.
     * in this setting we want none of this - rely on the environment we've got already.
     * 
     * also adjust filestore name to fit with expected for int.test configuration.
     *  */
    protected void setupKeys() throws IOException {
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
$Log: NodeTest.java,v $
Revision 1.2  2005/03/11 13:36:22  clq2
with merges from filemanager

Revision 1.1.2.1  2005/03/10 19:32:49  nw
bunch of tests for filemanager
 
*/