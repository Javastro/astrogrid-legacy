/*$Id: IterationSixAladinAdapterTest.java,v 1.4 2005/03/11 13:36:22 clq2 Exp $
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

import org.astrogrid.community.common.ivorn.CommunityAccountIvornFactory;
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.adapter.aladin.AladinAdapterTest;
import org.astrogrid.store.adapter.aladin.IterationSixAladinAdapter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** Integration test for a deprecated interface into myspace.
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Nov-2004 
 *
 *@deprecated remove this package when we get shot of myspace
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
Revision 1.4  2005/03/11 13:36:22  clq2
with merges from filemanager

Revision 1.3.40.1  2005/03/10 19:33:16  nw
marked tests for removal once myspace is replaced by filemanager.

Revision 1.3  2004/11/17 16:22:34  clq2
nww-itn07-704a

Revision 1.2.4.1  2004/11/16 17:24:42  nw
added tests for new treeclient interface to myspace.

Revision 1.2  2004/11/11 17:54:18  clq2
nww-660

Revision 1.1.2.1  2004/11/11 13:10:05  nw
tests for aladin adapter
 
*/