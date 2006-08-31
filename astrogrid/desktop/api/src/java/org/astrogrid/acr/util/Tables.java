/**
 * 
 */
package org.astrogrid.acr.util;

import java.io.IOException;

/** Utility functions for working with tables.
 * Exposes some of the functionality of STIL
 * @see http://www.star.bris.ac.uk/~mbt/stil/
 * @author Noel Winstanley
 * @since 2006.03
 * @service util.tables
 */
public interface Tables {

    /** 
     * Converts a table between supported formats. 
     * <code>inFormat</code> can be null only for autodetected types: 
     * these are FITS and VOTable. 
     * <code>outFormat</code> can be null only if the format can be guessed 
     * from <code>outLocation</code>. 
     * @param  inLocation  input location: may be a URL (including a JDBC URL), 
     *                     or filename  may be 
     *                     compressed using unix compress, gzip or bzip2 
     * @param  inFormat    input handler name: generally one of  
     *                     fits, votable, ascii, csv, ipac, wdc or null 
     * @param  outLocation output location: filename or JDBC URL 
     * @param  outFormat   output format: generally one of 
     *                     fits, fits-plus, 
     *                     votable, votable-tabledata, votable-binary-inline, 
     *                     votable-binary-href, votable-fits-inline,  
     *                     votable-fits-href, 
     *                     text, ascii, csv, html, html-element, latex, 
     *                     latex-document or null 
     */ 
    public void convert( String inLocation, String inFormat, 
                                String outLocation, String outFormat ) 
            throws IOException;
}
