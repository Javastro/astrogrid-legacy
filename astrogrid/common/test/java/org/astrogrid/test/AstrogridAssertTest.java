/*$Id: AstrogridAssertTest.java,v 1.1 2004/08/27 12:47:59 nw Exp $
 * Created on 27-Aug-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.test;

import junit.framework.TestCase;

/** Tests for the testing framework.
 * @author Noel Winstanley nw@jb.man.ac.uk 27-Aug-2004
 *
 */
public class AstrogridAssertTest extends TestCase {

    public void testAssertVotable() throws Exception {
        AstrogridAssert.assertVotable("<VOTABLE></VOTABLE>"); // minimal votable.
    }
    
}


/* 
$Log: AstrogridAssertTest.java,v $
Revision 1.1  2004/08/27 12:47:59  nw
added tests for astrogird assertions.
tests for tests.. crackers.
 
*/