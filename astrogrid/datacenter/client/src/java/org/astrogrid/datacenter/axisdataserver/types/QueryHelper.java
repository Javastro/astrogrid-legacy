/*$Id: QueryHelper.java,v 1.2 2004/01/13 00:32:47 nw Exp $
 * Created on 18-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.axisdataserver.types;

import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.adql.ADQLUtils;
import org.astrogrid.datacenter.adql.generated.Select;
import org.exolab.castor.xml.Marshaller;
import org.w3c.dom.Document;

/** helper class for building query objects 
 * @author Noel Winstanley nw@jb.man.ac.uk 18-Nov-2003
 *
 */
public class QueryHelper {

    /**
     * 
     */
    private QueryHelper() {
    }
    
    /** construct the simplest valid query object */
    public static Query buildMinimalQuery() throws Exception {
        Query q = new Query();
        Select select = ADQLUtils.buildMinimalQuery();
        Document doc = XMLUtils.newDocument();
        Marshaller.marshal(select,doc);        
        q.setQueryBody(doc.getDocumentElement());
           
        return q;
    }

}


/* 
$Log: QueryHelper.java,v $
Revision 1.2  2004/01/13 00:32:47  nw
Merged in branch providing
* sql pass-through
* replace Certification by User
* Rename _query as Query

Revision 1.1.10.2  2004/01/08 09:42:26  nw
tidied imports

Revision 1.1.10.1  2004/01/07 11:49:55  nw
found out how to get wsdl to generate nice java class names.
Replaced _query with Query throughout sources.

Revision 1.1  2003/11/27 17:27:15  nw
build tweaks

Revision 1.4  2003/11/27 00:49:52  nw
added community bean to query

Revision 1.3  2003/11/26 16:31:46  nw
altered transport to accept any query format.
moved back to axis from castor

Revision 1.2  2003/11/21 17:30:19  nw
improved WSDL binding - passes more strongly-typed data

Revision 1.1  2003/11/18 14:24:16  nw
helper class for testing. may be able to remove later.
 
*/