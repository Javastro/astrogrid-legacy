/*$Id: IterationSixTreeClientTest.java,v 1.2 2004/11/17 16:22:34 clq2 Exp $
 * Created on 08-Nov-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.store.tree.integration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.community.common.ivorn.CommunityAccountIvornFactory;
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.adapter.aladin.AladinAdapterTest;
import org.astrogrid.store.adapter.aladin.IterationSixAladinAdapter;
import org.astrogrid.store.tree.IterationSixTreeClient;
import org.astrogrid.store.tree.TreeClientTest;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Nov-2004 
 *
 */
public class IterationSixTreeClientTest extends TreeClientTest {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(IterationSixTreeClientTest.class);

    protected void setUp() throws Exception {

        super.setUp();
        IterationSixTreeClient i6 = new IterationSixTreeClient();
        this.setTestAdapter(i6);
        
        // check this is correct - don't know this community stuff.
        Ivorn ivorn = CommunityAccountIvornFactory.createLocal("frog");        
        
        this.setTestAccount(ivorn);
        this.setTestPassword("qwerty");
        this.initContainerName();
    }

}


/* 
$Log: IterationSixTreeClientTest.java,v $
Revision 1.2  2004/11/17 16:22:34  clq2
nww-itn07-704a

Revision 1.1.2.1  2004/11/16 17:24:42  nw
added tests for new treeclient interface to myspace.

Revision 1.2  2004/11/11 17:54:18  clq2
nww-660

Revision 1.1.2.1  2004/11/11 13:10:05  nw
tests for aladin adapter
 
*/