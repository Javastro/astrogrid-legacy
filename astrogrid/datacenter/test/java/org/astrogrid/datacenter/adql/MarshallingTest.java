/*$Id: MarshallingTest.java,v 1.2 2003/08/28 22:45:47 nw Exp $
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

import junit.framework.TestCase;
import org.astrogrid.datacenter.adql.generated.*;
import org.astrogrid.datacenter.adql.ADQLUtils;
import java.io.*;
/** unit test to exercise the marshalling / unmarshalling / validation framework of the generated castor classes
 * <p>
 * NB - to run this test, must have the castor-xml jar on classpath, and <i>also</i> the xerces.jar . Any other xml parser wont't do - 
 * castor uses other classes from xerces.
 * <p>
 * would like to have a set of sample ADQL xml files to run over, but can't find any examples anywhere.
 * Maybe there's a tool that will generate examples from a schema somewhere..
 * @author Noel Winstanley nw@jb.man.ac.uk 28-Aug-2003
 *
 */
public class MarshallingTest extends TestCase {

    /**
     * Constructor for MarshallingTest
     * @param arg0
     */
    public MarshallingTest(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(MarshallingTest.class);
    }

    
    public void testEmptySelectIsInvalid() {
        Select select = new Select();
        assertFalse(select.isValid());        
    }
    
    /** check the minimal query validates - will catch changes from one schema version to the next */
    public void testMinimalQueryIsValid() throws Exception {
        Select s = ADQLUtils.buildMinimalQuery();
        s.validate(); // throws a more informative message than an assertion.
        assertTrue(s.isValid());
    }
    
    public void testMarshalling() throws Exception {
        Select s = ADQLUtils.buildMinimalQuery();
        String xml = ADQLUtils.queryToString(s);
        assertNotNull(xml);
        assertTrue(xml.length() > 0);
        // lets take a look at it.
        System.out.println(xml);
    }
    
    /* found the generated equality methods do not work - i.e. this test fails.
     * so just disabled generation of equality methods for now.     
    public void testEquality() {
        Select s = ADQLUtils.buildMinimalQuery();
        assertEquals(s,s);
        Select s1 = ADQLUtils.buildMinimalQuery();
        assertEquals(s,s1);
    }*/
    
    public void testRoundTrip() throws Exception {
        Select s = ADQLUtils.buildMinimalQuery();
        String xml = ADQLUtils.queryToString(s);
        assertNotNull(xml);
        
        Reader in = new StringReader(xml);
        Select s1 = Select.unmarshalSelect(in);
        assertNotNull(s1);
        assertTrue(s1.isValid());
        // can't compare s1 , s2 for equality..
        // but can compare xml strings.
        String xml1 = ADQLUtils.queryToString(s1);
        assertNotNull(xml1);
        assertEquals(xml,xml1);
    }
    


}


/* 
$Log: MarshallingTest.java,v $
Revision 1.2  2003/08/28 22:45:47  nw
added unit test that runs a set of sample ADQL documents through the object model

Revision 1.1  2003/08/28 15:26:44  nw
unit tests for adql
 
*/