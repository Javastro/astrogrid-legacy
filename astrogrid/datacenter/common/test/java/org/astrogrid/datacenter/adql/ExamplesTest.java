/*$Id: ExamplesTest.java,v 1.1 2003/10/14 12:44:32 nw Exp $
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
import java.io.StringReader;

import junit.framework.TestCase;

import org.astrogrid.datacenter.adql.generated.Select;
/** Run through a set of sample files, loading and outputting each in turn.
 * @author Noel Winstanley nw@jb.man.ac.uk 28-Aug-2003
 *
 */
public class ExamplesTest extends TestCase {

    /**
     * Constructor for ExamplesTest.
     * @param arg0
     */
    public ExamplesTest(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(ExamplesTest.class);
    }

    public void test1() throws Exception {
        processFile("sample1.xml");
    }

    public void test2() throws Exception {
        processFile("sample2.xml");
    }

    public void test3() throws Exception {
        processFile("sample3.xml");
    }
    
    public void test4() throws Exception {
        processFile("sample4.xml");
    }
    
    public void test5() throws Exception {
        processFile("sample5.xml");
    }
    public void test6() throws Exception {
        processFile("sample6.xml");
    }

    /** muck about with the input file a bit */
    protected void processFile(String path) throws Exception {
        InputStream is = this.getClass().getResourceAsStream(path);
        assertNotNull(is);
        Reader reader = new InputStreamReader(is);
        Select query = Select.unmarshalSelect(reader);
        assertNotNull(query);
        assertTrue(query.isValid());
        // now write it out to a temporary buffer, read back in again., write out again.
        // verify two outputted xml buffers are equal.
        // sadly can't easily compare with original XML document
        String xml = ADQLUtils.queryToString(query);
        assertNotNull(xml);
        
        Reader in = new StringReader(xml);
        Select query1 = Select.unmarshalSelect(in);
        assertNotNull(query1);
        assertTrue(query1.isValid());
        // can't compare s1 , s2 for equality..
        // but can compare xml strings.
        String xml1 = ADQLUtils.queryToString(query1);
        assertNotNull(xml1);
        assertEquals(xml,xml1);
    }

}


/* 
$Log: ExamplesTest.java,v $
Revision 1.1  2003/10/14 12:44:32  nw
factored out from test hierarchy in parent project.

Revision 1.4  2003/09/26 11:04:12  nw
added new ADQL query that exercises more of the translator / castor generated classes.
bumps the clover score up by 10%!

Revision 1.3  2003/09/17 14:53:02  nw
tidied imports

Revision 1.2  2003/09/02 14:41:15  nw
added tests for ADQL parser

Revision 1.1  2003/08/28 22:45:47  nw
added unit test that runs a set of sample ADQL documents through the object model
 
*/