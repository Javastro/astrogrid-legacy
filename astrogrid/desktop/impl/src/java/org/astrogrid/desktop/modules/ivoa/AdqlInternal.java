/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.ivoa.Adql;

/** Extension of public adql interface.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 17, 20071:05:27 AM
 */
public interface AdqlInternal extends Adql {
/**
 * parse adql/s into adql/x (as a string)
 * @param arg0 adqls
 * @return equivalent adqlx.
 * @throws InvalidArgumentException
 */
    public String s2xs(String arg0) throws InvalidArgumentException;
}
