/*$Id: IterationSixAladinAdapterTest.java,v 1.2 2004/11/11 17:54:18 clq2 Exp $
 * Created on 08-Nov-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.store.adapter.aladin.integration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.community.common.ivorn.CommunityAccountIvornFactory;
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.adapter.aladin.AladinAdapterTest;
import org.astrogrid.store.adapter.aladin.IterationSixAladinAdapter;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Nov-2004
 *
 */
public class IterationSixAladinAdapterTest extends AladinAdapterTest {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(IterationSixAladinAdapterTest.class);

    protected void setUp() throws Exception {

        super.setUp();
        IterationSixAladinAdapter i6 = new IterationSixAladinAdapter();
        this.setTestAdapter(i6);
        
        // check this is correct - don't know this community stuff.
        Ivorn ivorn = CommunityAccountIvornFactory.createLocal("frog");        
        
        this.setTestAccount(ivorn);
        this.setTestPassword("qwerty");
        this.initContainerName();
    }

}


/* 
$Log: IterationSixAladinAdapterTest.java,v $
Revision 1.2  2004/11/11 17:54:18  clq2
nww-660

Revision 1.1.2.1  2004/11/11 13:10:05  nw
tests for aladin adapter
 
*/