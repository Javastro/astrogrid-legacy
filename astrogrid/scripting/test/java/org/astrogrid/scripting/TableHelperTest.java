/*$Id: TableHelperTest.java,v 1.3 2004/12/07 16:50:33 jdt Exp $
 * Created on 06-Dec-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.scripting;

import org.astrogrid.applications.parameter.protocol.ExternalValue;
import org.astrogrid.applications.parameter.protocol.InaccessibleExternalValueException;
import org.astrogrid.applications.parameter.protocol.UnrecognizedProtocolException;
import org.astrogrid.io.Piper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import uk.ac.starlink.table.StarTable;
import junit.framework.TestCase;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 06-Dec-2004
 *
 */
public class TableHelperTest extends TestCase {

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        helper = new TableHelper();
        URL url= this.getClass().getResource("/org/astrogrid/scripting/table/siap.votable");
        assertNotNull(url);
        table = helper.getBuilder().makeStarTable(url);
        assertNotNull(table);
    }
    protected TableHelper helper;
    protected StarTable table;
    


    public void testWriteStarTable() throws IOException, InaccessibleExternalValueException, UnrecognizedProtocolException, MalformedURLException, URISyntaxException {
        IOHelper io = new IOHelper();
        File f = File.createTempFile(this.getClass().getName(),null);
        f.deleteOnExit();        
        ExternalValue val = io.getExternalValue(f.toURL().toString());
        assertNotNull(val);
        helper.writeTable(val,table,"votable");
        assertTrue(f.exists() && f.canRead());
        //test we can read it in again..
        StarTable table1 = helper.getBuilder().makeStarTable(val);
        assertNotNull(table1);
        assertEquals(table1.getColumnCount(),table.getColumnCount());
        assertEquals(table1.getRowCount(),table.getRowCount());      

    }

    /*
     * Class under test for String toString(StarTable, String)
     */
    public void testToStringStarTableString() throws IOException {
        String s = helper.toString(table,"votable");
        StarTable table1 = helper.getBuilder().makeStarTableFromString(s);
        assertNotNull(table1);
        assertEquals(table1.getColumnCount(),table.getColumnCount());
        assertEquals(table1.getRowCount(),table.getRowCount());
        
    }

    public void testToInputStream() throws IOException {
        InputStream is =  helper.toInputStream(table,"votable");
        assertNotNull(is);
        // test we can read it in again..
        ByteArrayOutputStream content = new ByteArrayOutputStream();
        Piper.pipe(is,content);
        is.close();
        content.close();
        StarTable table1 = helper.getBuilder().makeStarTableFromString(content.toString());
        assertNotNull(table1);
        assertEquals(table1.getColumnCount(),table.getColumnCount());
        assertEquals(table1.getRowCount(),table.getRowCount());
    }

}


/* 
$Log: TableHelperTest.java,v $
Revision 1.3  2004/12/07 16:50:33  jdt
merges from scripting-nww-805

Revision 1.2.2.1  2004/12/07 14:47:58  nw
got table manipulation working.

Revision 1.2  2004/12/06 20:03:03  clq2
nww_807a

Revision 1.1.2.1  2004/12/06 13:27:47  nw
fixes to improvide integration with external values and starTables.
 
*/