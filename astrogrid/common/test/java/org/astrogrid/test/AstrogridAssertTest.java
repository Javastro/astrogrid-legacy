/*$Id: AstrogridAssertTest.java,v 1.2 2004/09/02 11:25:16 nw Exp $
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

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

/** Tests for the testing framework.
 * @author Noel Winstanley nw@jb.man.ac.uk 27-Aug-2004
 *
 */
public class AstrogridAssertTest extends TestCase {

    protected void setUp() {
        schemas = new HashMap();
        schemas.put("urn:nvo-coords",this.getClass().getResource("coords.xsd"));
        schemas.put("urn:nvo-region",this.getClass().getResource("region.xsd"));
        schemas.put("urn:nvo-stc",this.getClass().getResource("stc.xsd"));        
    }
    
    protected Map schemas;
    protected boolean seenFault = false;
    
    public void testAssertVotable() throws Exception {
        AstrogridAssert.assertVotable("<VOTABLE></VOTABLE>"); // minimal votable.
    }
    
    public void testFaultyVotable() {
        try {
            AstrogridAssert.assertVotable("<VOTABLE><wibble /></VOTABLE>");
        } catch (AssertionFailedError t) {
            seenFault=true;            
        }
        assertTrue(seenFault);
    }
    
    public void testWellFormedXML() {
        AstrogridAssert.assertWellFormedXML("<foo></foo>");
    }
    
    public void testNotSoWellFormedXML() {
        try {
            AstrogridAssert.assertWellFormedXML("<foo></bar>");

        } catch (AssertionFailedError t) {
            seenFault = true;
        }
        assertTrue(seenFault);
    }
    
    public void testAssertSchemaValid() {
        InputStream is= this.getClass().getResourceAsStream("valid.xml");
        assertNotNull(is);
        AstrogridAssert.assertSchemaValid(is,"Polygon",schemas);
        System.err.println("----");
    }
    
    public void testWrongRootElement() {
        InputStream is = this.getClass().getResourceAsStream("valid.xml");
        assertNotNull(is);
        try {
            AstrogridAssert.assertSchemaValid(is,"Ellipse",schemas);
        } catch (AssertionFailedError e) {
            seenFault=true;
        }
        assertTrue(seenFault);
    }
    
    public void testSchemaInvalid() {
        InputStream is = this.getClass().getResourceAsStream("invalid.xml");
        assertNotNull(is);
        try {
            AstrogridAssert.assertSchemaValid(is,"Polygon",schemas);
        } catch (AssertionFailedError e) {
            seenFault=true;
        }        
        assertTrue(seenFault);        
    }
    
}


/* 
$Log: AstrogridAssertTest.java,v $
Revision 1.2  2004/09/02 11:25:16  nw
tests for schema validation assertions

Revision 1.1  2004/08/27 12:47:59  nw
added tests for astrogird assertions.
tests for tests.. crackers.
 
*/