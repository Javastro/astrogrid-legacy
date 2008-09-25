/**
 * 
 */
package org.astrogrid.acr.ivoa;

import org.astrogrid.acr.InvalidArgumentException;
import org.w3c.dom.Document;

/** AR Service: Support for working with ADQL queries.
 * @exclude 
 * not much use to coders - as adql/x isn't a standard format any more.
 * @service ivoa.adql
 * @author Noel.Winstanley@manchester.ac.uk
 */
public interface Adql {
	
	/** convert an adql/s string to an adql/x document 	 
	 * @param s
	 * @return xml equivalent of the adql/s input
	 * @throws InvalidArgumentException if document cannot be parsed.
	 */
	public Document s2x(String s) throws InvalidArgumentException;
	/* convert an adql/x document to an adql/s string 
	 * 
	 * @param d
	 * @return
	 * @throws InvalidArgumentException if document cannot be parsed
	 */
//	public String x2s(Document d) throws InvalidArgumentException;

}
