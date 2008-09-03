/*$Id: FileStoreExecutionHistoryTest.java,v 1.6 2008/09/03 14:18:59 pah Exp $
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

import org.astrogrid.applications.contracts.MockNonSpringConfiguredConfig;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Jun-2004
 *
 */
public class FileStoreExecutionHistoryTest extends InMemoryExecutionHistoryTest {
    /** Construct a new FileStoreExecutionHistoryTest
     * @param arg0
     */
    public FileStoreExecutionHistoryTest() {
       
    }
    /**
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() throws Exception {
        super.setUp();
        this.eh = new FileStoreExecutionHistory(new MockNonSpringConfiguredConfig());
    }

}


/* 
$Log: FileStoreExecutionHistoryTest.java,v $
Revision 1.6  2008/09/03 14:18:59  pah
result of merge of pah_cea_1611 branch

Revision 1.5.54.2  2008/09/03 12:22:54  pah
improve unit tests so that they can run in single eclipse gulp

Revision 1.5.54.1  2008/04/04 15:46:08  pah
Have got bulk of code working with spring - still need to remove all picocontainer refs
ASSIGNED - bug 1611: enhancements for stdization holding bug
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611

Revision 1.5  2006/03/17 17:50:58  clq2
gtr_1489_cea correted version

Revision 1.3  2006/03/07 21:45:26  clq2
gtr_1489_cea

Revision 1.2.222.1  2006/01/26 11:05:19  gtr
The new configuration service is used instead of the separate configuration interfaces.

Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.1  2004/06/17 09:21:23  nw
finished all major functionality additions to core
 
*/