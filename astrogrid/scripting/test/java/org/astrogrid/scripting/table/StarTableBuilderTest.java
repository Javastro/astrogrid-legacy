/*$Id: StarTableBuilderTest.java,v 1.3 2004/12/07 16:50:33 jdt Exp $
 * Created on 06-Dec-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.scripting.table;

import org.astrogrid.applications.parameter.protocol.ExternalValue;
import org.astrogrid.applications.parameter.protocol.InaccessibleExternalValueException;
import org.astrogrid.applications.parameter.protocol.UnrecognizedProtocolException;
import org.astrogrid.io.Piper;
import org.astrogrid.scripting.IOHelper;

import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.TableFormatException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 06-Dec-2004
 *
 */
public class StarTableBuilderTest extends TestCase {

    /*
     * @see TestCase#setUp()
     */
    private static final int VOTABLE_NUM_ROWS = 2;
    protected void setUp() throws Exception {
        super.setUp();
        tableURL = this.getClass().getResource("siap.votable");
        assertNotNull(tableURL);
        builder = new StarTableBuilder();
    }
    
    protected URL tableURL;
    protected StarTableBuilder builder;

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    
    /** while we're here, test what meta-data, etc a star table returns from a votable.
     * @throws IOException
     * @throws TableFormatException
     * 
     *
     */
    public void testStarTableAbilities() throws TableFormatException, IOException {
        StarTable t= builder.makeStarTable(tableURL.toString());
        System.out.println(t.getParameters()); // displays three parameters
        System.out.println(t.getName()); // name of the table.
        System.out.println(t.getColumnAuxDataInfos()); // dunno what this is.
        System.out.println(t.getColumnInfo(0));
    }
    
    /*
     * Class under test for StarTable makeStarTable(String)
     */
    public void testMakeStarTableString() throws TableFormatException, IOException {
        //@todo Implement makeStarTable().
        StarTable t = builder.makeStarTable(tableURL.toString());
        assertNotNull(t);
        assertTrue(t instanceof ScriptStarTable);
        exerciseStarTable((ScriptStarTable)t);
    }

    /*
     * Class under test for StarTable makeStarTable(URL)
     */
    public void testMakeStarTableURL() throws IOException {
        StarTable t = builder.makeStarTable(tableURL);
        assertNotNull(t);
        assertTrue(t instanceof ScriptStarTable);
        exerciseStarTable((ScriptStarTable)t);
    }

    /*
     * Class under test for StarTable makeStarTable(ExternalValue)
     */
    public void testMakeStarTableExternalValue() throws InaccessibleExternalValueException, UnrecognizedProtocolException, URISyntaxException, TableFormatException, IOException {
        IOHelper helper = new IOHelper();
        ExternalValue val = helper.getExternalValue(tableURL);
        assertNotNull(val);
        StarTable t = builder.makeStarTable(val);
        assertNotNull(t);
        assertTrue(t instanceof ScriptStarTable);
        exerciseStarTable((ScriptStarTable)t);
    }

    /*
     * Class under test for StarTable makeStarTableFromString(String)
     */
    public void testMakeStarTableFromStringString() throws TableFormatException, IOException {
        StringWriter w = new StringWriter();
        Reader r  = new InputStreamReader(tableURL.openStream());
        Piper.pipe(r,w);
        r.close();
        w.close();
        assertNotNull(w.toString());
        StarTable t = builder.makeStarTableFromString(w.toString());
        assertNotNull(t);
        assertTrue(t instanceof ScriptStarTable);
        exerciseStarTable((ScriptStarTable)t);
    }
    
    /** extercise a star table 
     * @throws IOException*/
    protected void exerciseStarTable(ScriptStarTable  t) throws IOException {
        assertEquals(VOTABLE_NUM_ROWS,t.getRowCount());
        Iterator i = t.iterator();
        assertNotNull(i);
        int count = 0;
        while(i.hasNext()) {
            count++;
            Object e = i.next();
            assertNotNull(e);
            assertTrue(e instanceof List);
            List l = (List)e;
            assertEquals(13,l.size());
        }
        assertEquals(2,count);
        
        i = t.columnIterator(2);
        assertNotNull(i);
        count = 0;
        while(i.hasNext()) {
            count++;
            Object e = i.next();
            assertNotNull(e);
            assertTrue(e instanceof Double);
        }        
        assertEquals(2,count);
    }

}


/* 
$Log: StarTableBuilderTest.java,v $
Revision 1.3  2004/12/07 16:50:33  jdt
merges from scripting-nww-805

Revision 1.2.2.1  2004/12/07 14:47:58  nw
got table manipulation working.

Revision 1.2  2004/12/06 20:03:03  clq2
nww_807a

Revision 1.1.2.1  2004/12/06 13:27:47  nw
fixes to improvide integration with external values and starTables.
 
*/