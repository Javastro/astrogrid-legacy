/*$Id: QueryTranslatorTest.java,v 1.2 2003/09/05 01:04:02 nw Exp $
 * Created on 02-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.queriers;

import org.astrogrid.datacenter.adql.QOM;
import java.util.*;

import junit.framework.*;

/** test behaviour of the QueryTranslator isInternalNode method.
 * @author Noel Winstanley nw@jb.man.ac.uk 02-Sep-2003
 *
 */
public class QueryTranslatorTest extends TestCase {

    /**
     * Constructor for QueryTranslatorTest.
     * @param arg0
     */
    public QueryTranslatorTest(String arg0) {
        super(arg0);
    }
    public static Test suite() {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(QueryTranslatorTest.class);
    }
    protected void setUp() {
        q = new NullQueryTranslator();
    }
    protected QueryTranslator q;

    public void testEmpty() {
        assertFalse(q.isInternalProcessingNode(ArrayList.class)); // arbitrary class
    }
    
    public void testDirect() {
        q.requiresFrame(ArrayList.class);
        assertTrue(q.isInternalProcessingNode(ArrayList.class));        
        assertFalse(q.isInternalProcessingNode(AbstractList.class));
    }
    
    public void testSuperclass() {
        q.requiresFrame(AbstractList.class);
        assertTrue(q.isInternalProcessingNode(ArrayList.class));        
        assertTrue(q.isInternalProcessingNode(AbstractList.class));                
    }

    private static class NullQueryTranslator extends QueryTranslator {

        /* (non-Javadoc)
         * @see org.astrogrid.datacenter.adql.DynamicVisitor#visit(org.astrogrid.datacenter.adql.QOM)
         */
        public void visit(QOM q) throws Exception {
            
        }
        
    }

}


/* 
$Log: QueryTranslatorTest.java,v $
Revision 1.2  2003/09/05 01:04:02  nw
minor bugfix

Revision 1.1  2003/09/02 14:42:00  nw
added test
 
*/