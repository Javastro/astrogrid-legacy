/*$Id: Adql074.java,v 1.4 2007/01/24 14:04:45 nw Exp $
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

/** Routines for  with Astronomy Data Query Language (ADQL)
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 09-Sep-2005
 *@service ivoa.adql074
 *@deprecated implemented against the nvo webservice, which is poorly-specified and prone to err.
 */
public interface Adql074 {
    
    /** convert ADQL/s to ADQL/x
     * 
     * @param adqls query string (human readable)
     * @return equivalent adqlx (xml, v 7.4)
     */
    Document  s2x(String adqls) throws ServiceException;
    /** convert ADQL/x to ADQL/s
     * 
     * @param doc adqlx document (xml, v 7.4)
     * @return equivalent adql/s (human readable)
     */
    String x2s(Document doc) throws ServiceException;

}


/* 
$Log: Adql074.java,v $
Revision 1.4  2007/01/24 14:04:45  nw
updated my email address

Revision 1.3  2006/08/15 09:48:55  nw
added new registry interface, and bean objects returned by it.

Revision 1.2  2006/02/02 14:19:47  nw
fixed up documentation.

Revision 1.1  2005/09/12 15:21:43  nw
added stuff for adql.
 
*/