/*$Id: NodeIvornFactoryTestCase.java,v 1.2 2005/03/11 13:37:06 clq2 Exp $
 * Created on 24-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.filemanager.nodestore;

import org.astrogrid.filemanager.common.FileManagerFault;
import org.astrogrid.filemanager.common.NodeIvorn;
import org.astrogrid.filemanager.nodestore.file.FileNameGen;
import org.astrogrid.filemanager.server.HardCodedFileManagerConfig;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

/** simple unit test for NodeIvornFactory
 * @author Noel Winstanley nw@jb.man.ac.uk 24-Feb-2005
 *
 */
public class NodeIvornFactoryTestCase extends TestCase {

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        File baseDir = File.createTempFile("NodeIvornFactoryTestCase",null);
        baseDir.delete();
        baseDir.mkdir();
        baseDir.deleteOnExit();
        fac = new NodeIvornFactory(new FileNameGen(baseDir),new HardCodedFileManagerConfig());
    }
    
    protected NodeIvornFactory fac;
    
    public void testNewNodeIvorn() throws FileManagerFault {
        // whizz around and test a few..
        Set s = new HashSet();
        for (int i = 0; i < 1000; i++) {
            NodeIvorn iv = fac.createNewNodeIvorn();            
            assertNotNull(iv);
            assertFalse(s.contains(iv));
            s.add(iv);
        }
        
    }

}


/* 
$Log: NodeIvornFactoryTestCase.java,v $
Revision 1.2  2005/03/11 13:37:06  clq2
new filemanager merged with filemanager-nww-jdt-903-943

Revision 1.1.2.1  2005/03/01 23:43:39  nw
split code inito client and server projoects again.

Revision 1.1.2.2  2005/03/01 15:07:29  nw
close to finished now.

Revision 1.1.2.1  2005/02/27 23:03:12  nw
first cut of talking to filestore

Revision 1.1.2.1  2005/02/25 12:33:27  nw
finished transactional store
 
*/