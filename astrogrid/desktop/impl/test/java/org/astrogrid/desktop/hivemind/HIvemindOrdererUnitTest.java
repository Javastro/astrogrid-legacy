/*$Id: HIvemindOrdererUnitTest.java,v 1.6 2009/04/06 11:43:29 nw Exp $
 * Created on 29-Mar-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.hivemind;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.hivemind.ErrorHandler;
import org.apache.hivemind.impl.DefaultErrorHandler;
import org.apache.hivemind.order.Orderer;

/**
 * test my understanding of the hiemind order class - don't think I'm using it correctly at the moment.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 29-Mar-2006
 *
 */
public class HIvemindOrdererUnitTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
       super.setUp();
     //  err = new StrictErrorHandler();
       err = new DefaultErrorHandler();
       orderer = new Orderer(err,"Test Orderer");
       expected = new ArrayList<String>();
    }
    
    /*
     * @see junit.framework.TestCase#tearDown()
     */
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        orderer = null;
        err = null;
        expected = null;
        
    }
    private Orderer orderer;
    private ErrorHandler err;
    private static String[] values = new String[]{"A","B","C","D","E"};
    private List<String> expected;

    /** '*' means before or after all */
    public void testSimpleWildcards() {
        orderer.add(values[0],values[0],null,"*"); // things to come after, things to come before
        expected.add(values[0]);
        orderer.add(values[1],values[1],"*",null);        
        expected.add(values[1]);
        assertEquals(expected,orderer.getOrderedObjects());
    }
    
    /** order of insertion into orderer is unimportant - as expected */
    public void testSimpleWildcardsReversed() {
        expected.add(values[0]);
        expected.add(values[1]);
        orderer.add(values[1],values[1],"*",null);        
        orderer.add(values[0],values[0],null,"*"); // things to come after, things to come before
        assertEquals(expected,orderer.getOrderedObjects());
    }    
    
    /** when wildcards clash - emits a warning, goes on insertion order*/
    public void testClashingWildcards() {
        orderer.add(values[0],values[0],null,"*"); // things to come after, things to come before
        expected.add(values[0]);
        orderer.add(values[1],values[1],null,"*");        
        expected.add(values[1]);
        assertEquals(expected,orderer.getOrderedObjects());
    }    
    
    public void testClashingWildcardsReversed() {
        orderer.add(values[1],values[1],null,"*");        
        orderer.add(values[0],values[0],null,"*"); // things to come after, things to come before
        expected.add(values[1]);
        expected.add(values[0]);
        assertEquals(expected,orderer.getOrderedObjects());
    }        
    
    /** treats 'null' same as ""  - yep. no difference in semantics, just falls back to insertion order */
    public void testEmptyStringSameAsNull() {
        expected.add(values[0]);
        expected.add(values[1]);
        orderer.add(values[0],values[0],"",null); // things to come after, things to come before
        orderer.add(values[1],values[1],null,"");        
        assertEquals(expected,orderer.getOrderedObjects());
    }
    /** confirmation of previous */
    public void testEmptyStringSameAsNullReverse() {
        expected.add(values[1]);
        expected.add(values[0]);
        orderer.add(values[1],values[1],null,"");        
        orderer.add(values[0],values[0],"",null); // things to come after, things to come before
        assertEquals(expected,orderer.getOrderedObjects());
    }    
    
    /** doesn't trim names - so ' * ' != '*', and ' ' != '' 
     * only fails when using strict handler
     * */
    public void testNoisyEmptyString() {
        orderer.add(values[0],values[0],"  ", " * "); // things to come after, things to come before
        expected.add(values[0]);
        orderer.add(values[1],values[1]," * ","  ");        
        expected.add(values[1]);
            assertEquals(expected,orderer.getOrderedObjects());
    }    
    
    public void testNamedAbsolute2() {
        expected.add(values[0]);
        expected.add(values[1]);
        orderer.add(values[1],values[1],"A",null);        
        orderer.add(values[0],values[0],null,"B"); // things to come after, things to come before
        assertEquals(expected,orderer.getOrderedObjects());
    }      
        
    public void testNamedAbsolute3() {
        expected.add(values[0]);
        expected.add(values[1]);
        expected.add(values[2]);
        orderer.add(values[1],values[1],"A","C");        
        orderer.add(values[0],values[0],null,"B,C"); // things to come after, things to come before
        orderer.add(values[2],values[2],"A,B",null);
        assertEquals(expected,orderer.getOrderedObjects());
    }   
    /** doesn't like space separated names- but works out ok based on remaining constraints */
    public void testNamedAbsolute3SpaceSeparated() {
        expected.add(values[0]);
        expected.add(values[1]);
        expected.add(values[2]);
        orderer.add(values[1],values[1],"A","C");        
        orderer.add(values[0],values[0],null,"B C"); // things to come after, things to come before
        orderer.add(values[2],values[2],"A B",null);
        assertEquals(expected,orderer.getOrderedObjects());
    }       
    
    public void testNamedAbsolute4() {
        expected.add(values[0]);
        expected.add(values[1]);
        expected.add(values[2]);
        expected.add(values[3]);
        orderer.add(values[1],values[1],"A","C,D");        
        orderer.add(values[0],values[0],null,"B,C,D"); // things to come after, things to come before
        orderer.add(values[2],values[2],"A,B","D");
        orderer.add(values[3],values[3],"A,B,C",null);
        assertEquals(expected,orderer.getOrderedObjects());
    }     
    
    /** what happens when we just secify nearest neighbours, no wildcards 
     * - works out ok.*/
    public void testNamedRelative4() {
        expected.add(values[0]);
        expected.add(values[1]);
        expected.add(values[2]);
        expected.add(values[3]);
        orderer.add(values[1],values[1],"A","C");        
        orderer.add(values[0],values[0],null,"B"); // things to come after, things to come before
        orderer.add(values[2],values[2],"B","D");
        orderer.add(values[3],values[3],"C",null);
        assertEquals(expected,orderer.getOrderedObjects());
    }         
    /** nearest neighbours and wildcards - ok */
    public void testNamedRelative4WithWildcard() {
        expected.add(values[0]);
        expected.add(values[1]);
        expected.add(values[2]);
        expected.add(values[3]);
        orderer.add(values[1],values[1],"A","C");        
        orderer.add(values[0],values[0],null,"*"); // things to come after, things to come before
        orderer.add(values[2],values[2],"B","D");
        orderer.add(values[3],values[3],"*",null);
        assertEquals(expected,orderer.getOrderedObjects());
    }            
    
    /** what happens when only overlapping constraints are given?
     * i.e. am using '*' as 'all-but' - ooh. doesn't like that */
    public void testIndistinct4() {
        expected.add(values[0]);
        expected.add(values[1]);
        expected.add(values[2]);
        expected.add(values[3]);
        orderer.add(values[1],values[1],"A","*");        
        orderer.add(values[0],values[0],null,"*"); // things to come after, things to come before
        orderer.add(values[2],values[2],"*","D");
        orderer.add(values[3],values[3],"*",null);
            assertFalse(expected.equals(orderer.getOrderedObjects())); // i.e. not the result we'd expect/ want.
            //fail("Expected to fail");
    }
    
    /** what happens when using partial constraints - assume order of inclusion
     * becomes important
     * 
     * hmm- hard to express 'don't care' - unless this is what null symbolizes.
     * in which case, how do I say 'positively none in front'? - '*' I guess
     * */
    public void testPartial4() {
        expected.add(values[0]);
        expected.add(values[1]);
        expected.add(values[2]);
        expected.add(values[3]);
        orderer.add(values[1],values[1],"A",null);        
        orderer.add(values[0],values[0],null,"B"); // things to come after, things to come before
        orderer.add(values[2],values[2],null,null);
        orderer.add(values[3],values[3],"C",null);
        assertEquals(expected,orderer.getOrderedObjects());        
    }


}


/* 
$Log: HIvemindOrdererUnitTest.java,v $
Revision 1.6  2009/04/06 11:43:29  nw
Complete - taskConvert all to generics.

Incomplete - taskVOSpace VFS integration

Revision 1.5  2009/03/26 18:01:23  nw
added override annotations

Revision 1.4  2007/01/29 10:38:40  nw
documentation fixes.

Revision 1.3  2007/01/23 11:53:38  nw
cleaned up tests, organized imports, commented out or fixed failing tests.

Revision 1.2  2007/01/09 16:12:21  nw
improved tests - still need extending though.

Revision 1.1  2006/06/15 09:18:24  nw
improved junit tests

Revision 1.2  2006/04/18 23:25:47  nw
merged asr development.

Revision 1.1.2.1  2006/04/04 10:31:26  nw
preparing to move to mac.
 
*/