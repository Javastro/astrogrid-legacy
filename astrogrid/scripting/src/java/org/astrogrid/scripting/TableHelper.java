/*$Id: TableHelper.java,v 1.4 2004/12/07 18:05:52 nw Exp $
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
import org.astrogrid.scripting.table.MutableScriptStarTable;
import org.astrogrid.scripting.table.StarTableBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

import uk.ac.starlink.table.ColumnInfo;
import uk.ac.starlink.table.RowListStarTable;
import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.StarTableOutput;

/** Helper object for working with STIL tables <p />
 * 
 * See Userguide - <a href="http://www.star.bristol.ac.uk/~mbt/stil/sun252.html">http://www.star.bristol.ac.uk/~mbt/stil/sun252.html</a><br />
 * See JavaDoc - <a href="http://www.star.bristol.ac.uk/~mbt/stil/javadocs/uk/ac/starlink/table/package-summary.html">http://www.star.bristol.ac.uk/~mbt/stil/javadocs/uk/ac/starlink/table/package-summary.html</a><br />
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
     * @script-doc access the table builder
     */
    public StarTableBuilder getBuilder() {
        return builder;
    }
// output    
    /** @throws InaccessibleExternalValueException
     * @throws IOException
     * @script-doc write star table to an external location */
    public void writeTable(ExternalValue val,StarTable table, String format) throws InaccessibleExternalValueException, IOException {
        // ok. really inefficient, but must do for now.
        InputStream in = this.toInputStream(table,format);
        OutputStream os = val.write();
        Piper.pipe(in,os);
        in.close();
        os.close();
    }
    
    /** @script-doc access contents of table as a string 
     * @throws IOException*/
    public String toString(StarTable table,String format) throws IOException {
        InputStream in = this.toInputStream(table,format);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Piper.pipe(in,bos);
        in.close();
        bos.close();
        return bos.toString();
    }
    /** @script-doc access contents of a table as a stream */
    public InputStream toInputStream(StarTable table,String format) throws IOException {
        File f = File.createTempFile("tableHelper",null);
        f.deleteOnExit();
        f.createNewFile();
        output.writeStarTable(table,f.toString(),format);
        return new FileInputStream(f);
    }

    /**@script-doc create a new empty mutable table */
    public MutableScriptStarTable newMutableTable(ColumnInfo[] info) {
        return new MutableScriptStarTable(info);
    }
    
    /** @script-doc create a new empty mutable table, with the same structure as a template table*/
    public MutableScriptStarTable newMutableTableFromTemplate(StarTable s) {
        return new MutableScriptStarTable(s);
    }
    /** @script-doc access the STIL library object for writing out tables - see <a href="http://www.star.bristol.ac.uk/~mbt/stil/javadocs/uk/ac/starlink/table/StarTableOutput.html">http://www.star.bristol.ac.uk/~mbt/stil/javadocs/uk/ac/starlink/table/StarTableOutput.html</a> */
    public StarTableOutput getOutput() {
        return this.output;
    }
    /**@script-doc create a new column info object - see <a href="http://www.star.bristol.ac.uk/~mbt/stil/javadocs/uk/ac/starlink/table/ColumnInfo.html">http://www.star.bristol.ac.uk/~mbt/stil/javadocs/uk/ac/starlink/table/ColumnInfo.html</a> */
    public ColumnInfo newColumnInfo(String name) {
        return new ColumnInfo(name);
    }
    /**@script-doc create a new column info object - see <a href="http://www.star.bristol.ac.uk/~mbt/stil/javadocs/uk/ac/starlink/table/ColumnInfo.html">http://www.star.bristol.ac.uk/~mbt/stil/javadocs/uk/ac/starlink/table/ColumnInfo.html</a> */
        
    public ColumnInfo newColumnInfo(String name,Class contentType,String description) {
        return new ColumnInfo(name,contentType,description);
    }
    
}


/* 
$Log: TableHelper.java,v $
Revision 1.4  2004/12/07 18:05:52  nw
javadoc fixes

Revision 1.3  2004/12/07 16:50:33  jdt
merges from scripting-nww-805

Revision 1.2.2.1  2004/12/07 14:47:58  nw
got table manipulation working.

Revision 1.2  2004/12/06 20:03:03  clq2
nww_807a

Revision 1.1.2.1  2004/12/06 13:27:47  nw
fixes to improvide integration with external values and starTables.
 
*/