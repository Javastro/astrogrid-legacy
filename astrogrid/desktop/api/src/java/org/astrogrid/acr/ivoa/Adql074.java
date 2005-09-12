/*$Id: Adql074.java,v 1.1 2005/09/12 15:21:43 nw Exp $
 * Created on 09-Sep-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr.ivoa;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.ServiceException;

import org.w3c.dom.Document;

/** Service interface for working with adql expressions
 * @author Noel Winstanley nw@jb.man.ac.uk 09-Sep-2005
 *@service ivoa.adql074
 */
public interface Adql074 {
    
    /** convert ADQL/s to ADQL/x
     * 
     * @param adqls query string
     * @return equivalen adqlx (v 7.4)
     */
    Document  s2x(String adqls) throws ServiceException;
    /** convert ADQL/x to ADQL/s
     * 
     * @param doc adqlx document (v 7.4)
     * @return equivalent adql/s
     */
    String x2s(Document doc) throws ServiceException;

}


/* 
$Log: Adql074.java,v $
Revision 1.1  2005/09/12 15:21:43  nw
added stuff for adql.
 
*/