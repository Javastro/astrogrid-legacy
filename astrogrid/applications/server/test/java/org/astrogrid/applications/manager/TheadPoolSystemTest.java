/*$Id: TheadPoolSystemTest.java,v 1.2 2004/09/17 01:22:12 nw Exp $
 * Created on 14-Sep-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.manager;

import EDU.oswego.cs.dl.util.concurrent.PooledExecutor;

/** same as the ecisting system test, but using the thread pool execution controller.
 * @author Noel Winstanley nw@jb.man.ac.uk 14-Sep-2004
 *
 */
public class TheadPoolSystemTest extends SystemTest {

    /** Construct a new TheadPoolSystemTest
     * @param arg0
     */
    public TheadPoolSystemTest(String arg0) {
        super(arg0);
    }
    
    protected void setUp() throws Exception {
        super.setUp();
        PooledExecutor pool = new CeaThreadPool();
        controller = new ThreadPoolExecutionController(lib,history,pool);
    }

}


/* 
$Log: TheadPoolSystemTest.java,v $
Revision 1.2  2004/09/17 01:22:12  nw
updated tests

Revision 1.1.2.1  2004/09/14 13:44:38  nw
added tests for thread pool implementation
 
*/