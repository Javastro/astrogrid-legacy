/*$Id: StarTableBuilder.java,v 1.4 2004/12/07 18:05:52 nw Exp $
 * Created on 22-Nov-2004
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
import org.astrogrid.scripting.Toolbox;

import java.awt.datatransfer.Transferable;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.StarTableFactory;
import uk.ac.starlink.table.TableFormatException;
import uk.ac.starlink.util.DataSource;

/** Extension of the standard StarTableFactory that also allows star tables to be contructed from ExternalValues 
 * encourages factory to build random-access tables by dfault - less surprising then.
 * <p>
 * also wraps each returned starTable as a ScriptStarTable - which provides further script-friendly methods.
 * @script-summary factory for StarTables
 * @script-doc factory for ScriptStarTables - extends StarLink's StarTableFactory, but gurantees all
 * tables returned will be instances of {@link org.astrogrid.scripting.table.ScriptStarTable}<br />
 * StarTableFactory Javadoc - <a href="http://www.star.bristol.ac.uk/~mbt/stil/javadocs/uk/ac/starlink/table/StarTableFactory.html">http://www.star.bristol.ac.uk/~mbt/stil/javadocs/uk/ac/starlink/table/StarTableFactory.html</a>
 * @author Noel Winstanley nw@jb.man.ac.uk 22-Nov-2004
 *
 */
public class StarTableBuilder extends StarTableFactory {

    /** Construct a new StarTableBuilder
     * 
     */
    public StarTableBuilder() {
        super(true);
    }

   
    public StarTable makeStarTable(DataSource arg0, String arg1)
            throws TableFormatException, IOException {
        return new WrapperScriptStarTable( super.makeStarTable(arg0, arg1));
    }
    public StarTable makeStarTable(DataSource arg0)
            throws TableFormatException, IOException {
        return new WrapperScriptStarTable (super.makeStarTable(arg0));
    }
    public StarTable makeStarTable(String location, String format)
            throws TableFormatException, IOException {
        return new WrapperScriptStarTable (super.makeStarTable(location, format));
    }
    /** load the contents of a file / url into a scriptable star table */
    public StarTable makeStarTable(String location) throws TableFormatException,
            IOException {
        return new WrapperScriptStarTable (super.makeStarTable(location));
    }
    public StarTable makeStarTable(Transferable arg0) throws IOException {
        return new WrapperScriptStarTable(super.makeStarTable(arg0));
    }
    /** load the contents of a URL into a scriptable star table*/
    public StarTable makeStarTable(URL location, String format)
            throws TableFormatException, IOException {
        return new WrapperScriptStarTable(super.makeStarTable(location, format));
    }
    /** load the contents of a URL into a scriptable star table */
    public StarTable makeStarTable(URL location) throws IOException {
        return new WrapperScriptStarTable(super.makeStarTable(location));
    }
    /** return a guaranteed random-access scriptable star table */
    public StarTable randomTable(StarTable table) throws IOException {
        return new WrapperScriptStarTable(super.randomTable(table));
    }
    /** construct a star table from an external value
     * 
     * @param externalValue reference to a remote location
     * @return a star table
     * @throws TableFormatException if table data is not in a known format
     * @throws IOException if remote location cannot be accessed
     * @see Toolbox#getProtocolLibrary() to create an {@link ExternalValue}     * 
     */
    public StarTable makeStarTable(ExternalValue externalValue) throws TableFormatException, IOException {
        return this.makeStarTable(new ExternalValueDataSource(externalValue));
    }
    
    /** construct a star table from an external value
     * 
     * @param externalValue reference to a remote location
     * @param format expected table format
     * @return a star table
     * @throws TableFormatException if table data is not in expected format
     * @throws IOException if remote location cannot be accessed
     * @see Toolbox#getProtocolLibrary() to create an {@link ExternalValue}
     */
    public StarTable makeStarTable(ExternalValue externalValue,String format) throws TableFormatException, IOException {
        return this.makeStarTable(new ExternalValueDataSource(externalValue),format);
    }
   
    /** construct a starTable from the contents of a string */
    public StarTable makeStarTableFromString(String tableContent,String format) throws TableFormatException, IOException {
        return this.makeStarTable(new InlineDataSource(tableContent),format);
    }
    /** construct a starTable from the contents of a string */
    public StarTable makeStarTableFromString(String tableContent) throws TableFormatException, IOException {
        return this.makeStarTable(new InlineDataSource(tableContent));
    }
    
    /** a helper class that wraps a string as a datasource
     * 
     * @author Noel Winstanley nw@jb.man.ac.uk 24-Nov-2004
     *
     */
    public static class InlineDataSource extends DataSource {
        public InlineDataSource(String content) {
            this.content = content;
        }
        protected final String content;
        /**
         * @see uk.ac.starlink.util.DataSource#getRawInputStream()
         */
        protected InputStream getRawInputStream() throws IOException {
            return new ByteArrayInputStream(content.getBytes());
        }
    }
    
    /**
     * A helper class that wraps an ExternalValue as a Datasource - acts as a bridge between the 
     * Astrogrid and Starlink worlds.
     * @see ExternalValue
     * @author Noel Winstanley nw@jb.man.ac.uk 22-Nov-2004
     *
     */
    public static class ExternalValueDataSource extends DataSource {
        public ExternalValueDataSource(ExternalValue val) {
            this.val = val;
        }
        private final ExternalValue val;
        /**
         * @see uk.ac.starlink.util.DataSource#getRawInputStream()
         */
        protected InputStream getRawInputStream() throws IOException {
            
            try {
                return val.read();
            } catch (InaccessibleExternalValueException e) {
                throw new IOException(e.getMessage());
            }
        }
    }

}


/* 
$Log: StarTableBuilder.java,v $
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

Revision 1.3  2004/11/30 15:39:56  clq2
scripting-nww-777

Revision 1.1.2.1.2.1  2004/11/25 00:34:54  nw
configured to build random-access tables,
and added methods to build tables from string

Revision 1.1.2.1  2004/11/22 15:54:51  nw
deprecated existing scripting interface (which includes service lists).
produced new scripting interface, with more helpler objects.
 
*/