/**
 * 
 */
package org.astrogrid.acr.ivoa;

import org.astrogrid.acr.InvalidArgumentException;
import org.w3c.dom.Document;

/** Support for working with ADQL queries.
 * @service ivoa.adql
 * @author Noel.Winstanley@manchester.ac.uk
 * @since 2007.2
 */
public interface Adql {
	
	/** convert an adq/s string to an adql/x document 
	 * 
	 * @param s
	 * @return
	 * @throws InvalidArgumentException if document cannot be parsed.
	 */
	public Document s2x(String s) throws InvalidArgumentException;
	/** convert an adql/x document to an adql/s string 
	 * 
	 * @param d
	 * @return
	 * @throws InvalidArgumentException if document cannot be parsed
	 */
//	public String x2s(Document d) throws InvalidArgumentException;

}
