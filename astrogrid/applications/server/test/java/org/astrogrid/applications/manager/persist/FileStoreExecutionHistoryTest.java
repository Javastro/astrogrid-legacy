/*$Id: FileStoreExecutionHistoryTest.java,v 1.4 2006/03/11 05:57:54 clq2 Exp $
 * Created on 16-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.manager.persist;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Jun-2004
 *
 */
public class FileStoreExecutionHistoryTest extends InMemoryExecutionHistoryTest {
    /** Construct a new FileStoreExecutionHistoryTest
     * @param arg0
     */
    public FileStoreExecutionHistoryTest(String arg0) {
        super(arg0);
    }
    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        this.eh = new FileStoreExecutionHistory(new FileStoreExecutionHistory.TemporaryStoreDir());
    }

}


/* 
$Log: FileStoreExecutionHistoryTest.java,v $
Revision 1.4  2006/03/11 05:57:54  clq2
roll back to before merged apps_gtr_1489, tagged as rolback_gtr_1489

Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.1  2004/06/17 09:21:23  nw
finished all major functionality additions to core
 
*/