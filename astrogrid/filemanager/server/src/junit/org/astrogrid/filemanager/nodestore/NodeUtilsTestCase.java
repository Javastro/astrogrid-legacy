/*$Id: NodeUtilsTestCase.java,v 1.2 2005/03/11 13:37:06 clq2 Exp $
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

import org.astrogrid.filemanager.common.Node;
import org.astrogrid.filemanager.common.NodeIvorn;
import org.astrogrid.filemanager.common.NodeName;
import org.astrogrid.filemanager.common.NodeTypes;
import org.astrogrid.filemanager.common.NodeUtils;

import org.apache.axis.types.URI;

import junit.framework.TestCase;

/** small unit tests for node utils.
 * @author Noel Winstanley nw@jb.man.ac.uk 24-Feb-2005
 *
 */
public class NodeUtilsTestCase extends TestCase {

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        n1 = new NodeIvorn("ivo://test/foo#n1");
        n2 = new NodeIvorn("ivo://test/foo#n2");
        n3 = new NodeIvorn("ivo://test/foo#n3");
        defaultLocation= new URI("ivo://default-filestore");
        name = new NodeName("fred");
        name1 = new NodeName("barney");
        folder = NodeUtils.newNode(n1,name,NodeTypes.FOLDER,n2,defaultLocation);
        file = NodeUtils.newNode(n1,name1,NodeTypes.FOLDER,n3,defaultLocation);
    }
    NodeIvorn n1;
    NodeIvorn n2;
    NodeIvorn n3;
    NodeName name;
    NodeName name1;
    Node folder;
    Node file;
    URI defaultLocation;

    public void testNewNode() {
        assertNotNull(folder);
        assertNotNull(file);
        assertEquals(name,folder.getName());
        assertEquals(name1,file.getName());
        
    }

    public void testCloneFileNode() {
        Node copy = NodeUtils.cloneNode(file);
        assertNotNull(copy);
        assertNotSame(copy,file);        
        assertEquals(copy,file);
    }
    
    public void testCloneFolderNode() {
        Node copy = NodeUtils.cloneNode(folder);
        assertNotNull(copy);
        assertNotSame(copy,folder);        
        assertEquals(copy,folder);        
    }

    public void testFindMissingChild() {
        assertNull(NodeUtils.findChild(folder,name1));
    }

    public void testAddFindRemoveChild() {
        Node copy = NodeUtils.cloneNode(folder);
        NodeUtils.addChild(folder,name1,n3);
        assertTrue(folder.getChild().length == copy.getChild().length +1);
        NodeIvorn found = NodeUtils.findChild(folder,name1);
        assertNotNull(found);
        NodeUtils.removeChild(folder,name1);
        assertNull(NodeUtils.findChild(folder,name1));
        assertEquals(folder.getChild().length,copy.getChild().length);
        assertEquals(folder,copy);
    }

    public void testRemoveMissingChild() {
        // should do nothing.
       NodeUtils.removeChild(folder,name1);
    }

}


/* 
$Log: NodeUtilsTestCase.java,v $
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