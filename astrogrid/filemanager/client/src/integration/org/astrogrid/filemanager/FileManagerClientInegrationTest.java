/*
 * $Id: FileManagerClientInegrationTest.java,v 1.1 2008/02/05 11:38:00 pah Exp $
 * 
 * Created on 4 Feb 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.filemanager;

import java.net.URISyntaxException;
import java.rmi.RemoteException;

import org.astrogrid.community.common.exception.CommunityException;
import org.astrogrid.filemanager.client.FileManagerClient;
import org.astrogrid.filemanager.client.FileManagerClientFactory;
import org.astrogrid.filemanager.client.FileManagerNode;
import org.astrogrid.filemanager.common.DuplicateNodeFault;
import org.astrogrid.filemanager.common.FileManagerFault;
import org.astrogrid.filemanager.common.NodeNotFoundFault;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.store.Ivorn;

import junit.framework.TestCase;

public class FileManagerClientInegrationTest extends TestCase {

    public FileManagerClientInegrationTest(String name) {
	super(name);
    }

    protected void setUp() throws Exception {
	super.setUp();
    }
    
    public void testNewSyntax() throws FileManagerFault, DuplicateNodeFault, NodeNotFoundFault, RemoteException, RegistryException, CommunityException, URISyntaxException{
	Ivorn id = new Ivorn("ivo://paul@org.astrogrid.regtest/community#/test_folder/new_file0.vot" );  // some ivorn or another
	FileManagerClientFactory factory = new FileManagerClientFactory();
	FileManagerClient delegate = factory.login();

	FileManagerNode node = null;
	//if the file doesn't exist, we need to make it
	if (delegate.exists(id) == null) {
	   node = delegate.createFile(id);
	   assertNotNull(node);
	}
	else {
	   node = delegate.node(id);
	   assertNotNull(node);
	}
    }

}


/*
 * $Log: FileManagerClientInegrationTest.java,v $
 * Revision 1.1  2008/02/05 11:38:00  pah
 * RESOLVED - bug 2545: Problem with IVORN resolution
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2545
 *
 */
