/*$Id: DocumentQueryResults.java,v 1.1 2003/11/18 11:23:49 nw Exp $
 * Created on 13-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.cds.querier;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 13-Nov-2003
 *
 */
public class DocumentQueryResults implements QueryResults {

    /**
     * 
     */
    public DocumentQueryResults(Document doc) {
        this.doc = doc;
    }
    protected final Document doc;

    /* (non-Javadoc)
     * @see org.astrogrid.datacenter.queriers.QueryResults#toVotable()
     */
    public Document toVotable() throws IOException, SAXException {
        return doc;
    }

    /* (non-Javadoc)
     * @see org.astrogrid.datacenter.queriers.QueryResults#toVotable(java.io.OutputStream)
     */
    public void toVotable(OutputStream out) throws IOException, SAXException {
        XMLUtils.DocumentToStream(doc,out);
    }

}


/* 
$Log: DocumentQueryResults.java,v $
Revision 1.1  2003/11/18 11:23:49  nw
mavenized cds delegate

Revision 1.1  2003/11/18 11:10:05  nw
mavenized cds delegate
 
*/