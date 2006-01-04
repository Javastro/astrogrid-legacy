/*$Id: FileJobFactoryImplTest.java,v 1.5 2006/01/04 09:52:32 clq2 Exp $
 * Created on 11-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.impl.workflow;

import java.io.File;
import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.astrogrid.jes.util.BaseDirectory;
import org.astrogrid.jes.util.TemporaryBaseDirectory;

/** Test functionality of the file-backed job factory
 * <p/> 
 * extends the test for the in memory job factory - just a matter of setting up a different factory implementation. all tests remain the same.
 * @author Noel Winstanley nw@jb.man.ac.uk 11-Feb-2004
 *
 */
public class FileJobFactoryImplTest extends InMemoryJobFactoryImplTest {
    /**
     * Constructor for FileJobFactoryImplTest.
     * @param arg0
     */
    public FileJobFactoryImplTest(String arg0) {
        super(arg0);
    }
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
    }
    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public static Test suite() {

        TestSuite tests = new TestSuite(FileJobFactoryImplTest.class);
        tests.addTest(new FileJobFactoryImplTest("finallyTestListJobs"));        
        tests.addTest(new FileJobFactoryImplTest("finallyTestDeleteLast"));
        tests.addTest(new FileJobFactoryImplTest("finallyTestListUnknownJobs"));
        tests.addTest(new FileJobFactoryImplTest("finallyTestUnknownJobs"));
        return new TestSetup(tests) {

            protected File baseDir ;
            protected void setUp() throws Exception {
              BaseDirectory finder = new TemporaryBaseDirectory();
              this.baseDir = finder.getDir();
              jf = new CachingFileJobFactory(finder);
            }
        };
    }

}


/* 
$Log: FileJobFactoryImplTest.java,v $
Revision 1.5  2006/01/04 09:52:32  clq2
jes-gtr-1462

Revision 1.4.42.1  2005/12/09 23:11:55  gtr
I refactored the base-directory feature out of its inner class and interface in FileJobFactory and into org.aastrogrid.jes.util. This addresses part, but not all, of BZ1487.

Revision 1.4  2005/04/25 12:13:54  clq2
jes-nww-776-again

Revision 1.3.170.1  2005/04/11 16:31:11  nw
updated version of xstream.
added caching to job factory

Revision 1.3  2004/03/07 21:04:38  nw
merged in nww-itn05-pico - adds picocontainer

Revision 1.2.4.1  2004/03/07 20:42:31  nw
updated tests to work with picocontainer

Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.2  2004/02/17 12:57:11  nw
improved documentation

Revision 1.1.2.1  2004/02/12 01:12:54  nw
skeleton unit tests for the castor object model
 
*/