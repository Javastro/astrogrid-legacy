/*$Id: SqlQueryTranslator.java,v 1.4 2003/11/28 16:10:30 nw Exp $
 * Created on 27-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.queriers.sql;

import org.astrogrid.datacenter.queriers.spi.Translator;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/** Simple translator that expects following documents in following format
 * &lt;sql xmlns="urn:sql" &gt;
 *   select * from ...
 * &lt;/sql &gt;
 * @author Noel Winstanley nw@jb.man.ac.uk 27-Nov-2003
 * @todo write unit test.
 */
public class SqlQueryTranslator implements Translator {

    /* (non-Javadoc)
     * @see org.astrogrid.datacenter.queriers.spi.Translator#translate(org.w3c.dom.Element)
     */
    public Object translate(Element e) throws Exception {
        if (e.getLocalName().equals("sql")) {
            return e.getFirstChild().getNodeValue();
        } else {
            NodeList nodes = e.getElementsByTagName("sql");
            if (nodes.getLength() == 0) {
                throw new IllegalArgumentException("No element named 'sql' found in document");
            }
            if (nodes.getLength() > 1) {
                throw new IllegalArgumentException("More than one element named 'sql' found in document - there can be only 0ne");
            }
            if (nodes.item(0).getFirstChild() != null) {
               return nodes.item(0).getFirstChild().getNodeValue();
            } else {                
               return nodes.item(0).getNodeValue();
            }
        }
        

    }

    /* (non-Javadoc)
     * @see org.astrogrid.datacenter.queriers.spi.Translator#getResultType()
     */
    public Class getResultType() {        
        return String.class;
    }

}


/* 
$Log: SqlQueryTranslator.java,v $
Revision 1.4  2003/11/28 16:10:30  nw
finished plugin-rewrite.
added tests to cover plugin system.
cleaned up querier & queriermanager. tested

Revision 1.3  2003/11/27 00:52:58  nw
refactored to introduce plugin-back end and translator maps.
interfaces in place. still broken code in places.
 
*/