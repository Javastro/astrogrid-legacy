/**
 * 
 */
package org.astrogrid.acr.util;

import java.net.URI;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;

/** Utility functions for working with tables.
 * Exposes some of the functionality of STIL
 * @see http://www.star.bris.ac.uk/~mbt/stil/
 * @author Noel Winstanley
 * @since 2006.03
 * @service util.tables
 */
public interface Tables {

    /** 
     * Converts a table in a file between supported formats. 
     * @param  inLocation  input location: may be a http://, file://, ivo:// , ftp://
     *                     compressed using unix compress, gzip or bzip2 
     * @param  inFormat    input handler name: generally one of  
     *                     fits, votable, ascii, csv, ipac, wdc or null 
     * @param  outLocation output location: file://, ivo://, ftp://
     * @param  outFormat   output format: generally one of 
     *                     fits, fits-plus, 
     *                     votable, votable-tabledata, votable-binary-inline, 
     *                     votable-binary-href, votable-fits-inline,  
     *                     votable-fits-href, 
     *                     text, ascii, csv, html, html-element, latex, 
     *                     latex-document or null 
     */ 
	public void convertFiles(URI inLocation, String inFormat
			, URI outLocation, String outFormat) throws InvalidArgumentException, ServiceException, NotFoundException, SecurityException ;

    /** 
     * Writes an in-memory table to a table in a file, converting between supported formats. 
     * @param  input the input table
     * @param  inFormat    input handler name: generally one of  
     *                     fits, votable, ascii, csv, ipac, wdc or null 
     * @param  outLocation output location: file://, ivo://, ftp://
     * @param  outFormat   output format: generally one of 
     *                     fits, fits-plus, 
     *                     votable, votable-tabledata, votable-binary-inline, 
     *                     votable-binary-href, votable-fits-inline,  
     *                     votable-fits-href, 
     *                     text, ascii, csv, html, html-element, latex, 
     *                     latex-document or null 
     */ 
	public void convertToFile(String input, String inFormat
			, URI outLocation, String outFormat) throws InvalidArgumentException, ServiceException, NotFoundException, SecurityException ;
	
    /** 
     * Reads a table in a file into an in-memory table, converting between supported formats
     * Will only give good results for text-based table formats.
     * @param  inLocation  input location: may be a http://, file://, ivo:// , ftp://
     *                     compressed using unix compress, gzip or bzip2 
     * @param  inFormat    input handler name: generally one of  
     *                     fits, votable, ascii, csv, ipac, wdc or null 
     * @param  outFormat   output format: generally one of 
     *                     fits, fits-plus, 
     *                     votable, votable-tabledata, votable-binary-inline, 
     *                     votable-binary-href, votable-fits-inline,  
     *                     votable-fits-href, 
     *                     text, ascii, csv, html, html-element, latex, 
     *                     latex-document or null
     *  @return the converted representation of the table. 
     */ 
	public String convertFromFile(URI inLocation, String inFormat
			, String outFormat) throws InvalidArgumentException, ServiceException, NotFoundException, SecurityException ;
	
    /** 
     * Converts an in-memory table between supported formats. 
     * Will only give good results for text-based table formats.
     * @param  input the input table.
     * @param  inFormat    input handler name: generally one of  
     *                     fits, votable, ascii, csv, ipac, wdc or null 
     * @param  outFormat   output format: generally one of 
     *                     fits, fits-plus, 
     *                     votable, votable-tabledata, votable-binary-inline, 
     *                     votable-binary-href, votable-fits-inline,  
     *                     votable-fits-href, 
     *                     text, ascii, csv, html, html-element, latex, 
     *                     latex-document or null 
     *  @return a table in the requested format.
     */     
    public String convert(String input, String inFormat, String outFormat) throws InvalidArgumentException, ServiceException;
    
    
    
    /** list the names of the table formats this module can write out as */
	public String[] listOutputFormats();
	
	/** list the names of the table formats this module can read in from */
	public String[] listInputFormats()	;
    

}
