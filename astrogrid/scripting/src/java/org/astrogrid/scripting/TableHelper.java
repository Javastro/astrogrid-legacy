/*$Id: TableHelper.java,v 1.2 2004/12/06 20:03:03 clq2 Exp $
 * Created on 03-Dec-2004
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
import org.astrogrid.io.Piper;
import org.astrogrid.scripting.table.StarTableBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.StarTableOutput;

/** Helper object for working with STIL tables.
 * @author Noel Winstanley nw@jb.man.ac.uk 03-Dec-2004
 *
 */
public class TableHelper {

    /** Construct a new TableHelper
     * 
     */
    public TableHelper() {
        super();
    }

    protected final StarTableBuilder builder = new StarTableBuilder();
    protected final StarTableOutput output = new StarTableOutput();
    /**
     * @return
     */
    public StarTableBuilder getBuilder() {
        return builder;
    }
// output    
    /** @throws InaccessibleExternalValueException
     * @throws IOException
     * @script-doc write star table to an external location */
    public void writeStarTable(ExternalValue val,StarTable table, String format) throws InaccessibleExternalValueException, IOException {
        // ok. really inefficient, but must do for now.
        InputStream in = this.toInputStream(table,format);
        OutputStream os = val.write();
        Piper.pipe(in,os);
        in.close();
        os.close();
    }
    
    /** access contents of table as a string 
     * @throws IOException*/
    public String toString(StarTable table,String format) throws IOException {
        InputStream in = this.toInputStream(table,format);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Piper.pipe(in,bos);
        in.close();
        bos.close();
        return bos.toString();
    }
    
    public InputStream toInputStream(StarTable table,String format) throws IOException {
        File f = File.createTempFile("tableHelper",null);
        f.deleteOnExit();
        f.createNewFile();
        output.writeStarTable(table,f.toString(),format);
        return new FileInputStream(f);
    }


// modification.
   // need to think further about this - think I should be able to write something
    // that will use groovy closures to define the tables.
    
}


/* 
$Log: TableHelper.java,v $
Revision 1.2  2004/12/06 20:03:03  clq2
nww_807a

Revision 1.1.2.1  2004/12/06 13:27:47  nw
fixes to improvide integration with external values and starTables.
 
*/