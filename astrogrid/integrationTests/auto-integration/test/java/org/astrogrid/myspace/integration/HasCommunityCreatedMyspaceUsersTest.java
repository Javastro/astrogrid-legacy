/*
 * $Id: HasCommunityCreatedMyspaceUsersTest.java,v 1.1 2004/09/10 19:40:24 pah Exp $
 * 
 * Created on 10-Sep-2004 by Paul Harrison (pah@jb.man.ac.uk)
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.myspace.integration;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import org.astrogrid.integration.AbstractTestForIntegration;
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.VoSpaceClient;

import junit.framework.TestCase;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk) 10-Sep-2004
 * @version $Name:  $
 * @since iteration6
 */
public class HasCommunityCreatedMyspaceUsersTest extends AbstractTestForIntegration {

    /**
     * @param arg0
     */
    public HasCommunityCreatedMyspaceUsersTest(String arg0) {
        super(arg0);
        
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(HasCommunityCreatedMyspaceUsersTest.class);
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    public void testIsFrogThere()
    {
        Ivorn target = createIVORN("/frog.txt");
        try {
            VoSpaceClient vospace = new VoSpaceClient(user);
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(vospace.putStream(target)));
            pw.println("content");
            pw.close();
        }
        catch (IOException e) {
           fail("myspace users probably not created by community install - "+ e.getMessage());
        }     

    }

}
