/*$Id: StarTableBuilder.java,v 1.2 2004/11/22 18:26:54 clq2 Exp $
 * Created on 22-Nov-2004
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

import java.io.IOException;
import java.io.InputStream;

import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.StarTableFactory;
import uk.ac.starlink.table.TableFormatException;
import uk.ac.starlink.util.DataSource;

/** Extension of the standard StarTableFactory that also allows star tables to be contructed from ExternalValues 
 * @author Noel Winstanley nw@jb.man.ac.uk 22-Nov-2004
 *
 */
public class StarTableBuilder extends StarTableFactory {

    /** Construct a new StarTableBuilder
     * 
     */
    public StarTableBuilder() {
        super();
    }

    /** Construct a new StarTableBuilder
     * @param arg0
     */
    public StarTableBuilder(boolean arg0) {
        super(arg0);
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
        return makeStarTable(new ExternalValueDataSource(externalValue));
    }
    
    /** construct a star table from an external value
     * 
     * @param externalValue reference to a remote location
     * @param handler expected table format
     * @return a star table
     * @throws TableFormatException if table data is not in expected format
     * @throws IOException if remote location cannot be accessed
     * @see Toolbox#getProtocolLibrary() to create an {@link ExternalValue}
     */
    public StarTable makeStarTable(ExternalValue externalValue,String handler) throws TableFormatException, IOException {
        return makeStarTable(new ExternalValueDataSource(externalValue),handler);
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
Revision 1.2  2004/11/22 18:26:54  clq2
scripting-nww-715

Revision 1.1.2.1  2004/11/22 15:54:51  nw
deprecated existing scripting interface (which includes service lists).
produced new scripting interface, with more helpler objects.
 
*/