/*$Id: FileJobFactoryImplTest.java,v 1.3 2004/03/07 21:04:38 nw Exp $
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
                baseDir = File.createTempFile("filejobfactory-test-basedir",null);           
                baseDir.deleteOnExit();     
                assertNotNull(baseDir);
                assertTrue(baseDir.exists());
                baseDir.delete();
                FileJobFactoryImpl.BaseDirectory finder = new FileJobFactoryImpl.BaseDirectory() {

                    public File getDir() {
                        return baseDir;
                    }
                };
                jf = new FileJobFactoryImpl(finder);
            }
        };
    }

}


/* 
$Log: FileJobFactoryImplTest.java,v $
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