/*$Id: QueryTranslatorTest.java,v 1.3 2003/11/28 16:10:30 nw Exp $
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

import java.util.AbstractList;
import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.astrogrid.datacenter.adql.QOM;

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

        /* (non-Javadoc)
         * @see org.astrogrid.datacenter.queriers.spi.Translator#getResultType()
         */
        public Class getResultType() {
            return null;
        }
        
    }

}


/* 
$Log: QueryTranslatorTest.java,v $
Revision 1.3  2003/11/28 16:10:30  nw
finished plugin-rewrite.
added tests to cover plugin system.
cleaned up querier & queriermanager. tested

Revision 1.2  2003/11/27 00:52:58  nw
refactored to introduce plugin-back end and translator maps.
interfaces in place. still broken code in places.

Revision 1.1  2003/11/14 00:38:30  mch
Code restructure

Revision 1.3  2003/09/17 14:53:02  nw
tidied imports

Revision 1.2  2003/09/05 01:04:02  nw
minor bugfix

Revision 1.1  2003/09/02 14:42:00  nw
added test
 
*/