/*$Id: VisitorTest.java,v 1.1 2003/10/14 12:44:32 nw Exp $
 * Created on 28-Aug-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.adql;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.astrogrid.datacenter.adql.generated.Select;
/** Run through a set of sample files, running visitor object over each in turn.
 *  just to test visitor pattern is funcitoning.
 *  Extends Examples test - in this way we can add more examples to that test, 
 * and have the visitor tests in the class run over them too.
 * @author Noel Winstanley nw@jb.man.ac.uk 28-Aug-2003
 *
 */
public class VisitorTest extends ExamplesTest{

    /**
     * Constructor for ExamplesTest.
     * @param arg0
     */
    public VisitorTest(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(VisitorTest.class);
    }

// tsts inherited from ExamplesTest

    protected void processFile(String path) throws Exception {
        InputStream is = this.getClass().getResourceAsStream(path);
        assertNotNull(is);
        Reader reader = new InputStreamReader(is);
        Select query = Select.unmarshalSelect(reader);
        assertNotNull(query);
        assertTrue(query.isValid());

        TestVisitor visitor = new TestVisitor();
        query.acceptTopDown(visitor);
        visitor.check();
        
        TestVisitor visitor1 = new TestVisitor();
        query.acceptBottomUp(visitor1);
        visitor1.check();
        
        assertEquals(visitor,visitor1); // i.e. we've seen the same number of nodes in both directions.

    }
    
    public static class TestVisitor implements DynamicVisitor {

        private boolean selectSeen = false;
        private int count = 0; 
        public void check() {
            assertTrue(selectSeen);
            assertTrue(count > 1);
        }

        public void visit(Select s) throws Exception {
            assertNotNull(s);
            selectSeen = true;    
        }

        /* (non-Javadoc)
         * @see org.astrogrid.datacenter.adql.Visitor#visit(org.astrogrid.datacenter.adql.QOM)
         */
        public void visit(QOM q) throws Exception {
            assertNotNull(q);
            count++;
            
        }
        
        /* (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        public boolean equals(Object obj) {
            TestVisitor other = (TestVisitor)obj;
            return this.selectSeen == other.selectSeen && this.count == other.count;
        }

    }

}


/* 
$Log: VisitorTest.java,v $
Revision 1.1  2003/10/14 12:44:32  nw
factored out from test hierarchy in parent project.

Revision 1.3  2003/09/26 11:02:07  nw
tests bottom-up traversal now as well as top-down traversal.

Revision 1.2  2003/09/17 14:53:02  nw
tidied imports

Revision 1.1  2003/09/02 14:41:15  nw
added tests for ADQL parser

Revision 1.1  2003/08/28 22:45:47  nw
added unit test that runs a set of sample ADQL documents through the object model
 
*/