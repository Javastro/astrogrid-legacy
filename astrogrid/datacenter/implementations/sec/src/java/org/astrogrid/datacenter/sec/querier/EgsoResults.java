/*$Id: EgsoResults.java,v 1.3 2004/09/01 12:10:58 mch Exp $
 * Created on 13-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.sec.querier;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.datacenter.queriers.status.QuerierProcessingResults;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/** Trivial Implementation of {@link org.astrogrid.datacenter.queriers.QueryResults} backed by a {@link org.w3c.dom.Document}
 * @author Noel Winstanley nw@jb.man.ac.uk 13-Nov-2003
 *
 */
public class EgsoResults extends QueryResults {

    protected final Document doc;

    /**
     *
     
    public VotableResults(Document doc, QuerierProcessingResults querierStatus) {
        this.doc = doc;
    }
    */

   public EgsoResults(Document doc) {
       this.doc = doc;
   }

    
    public int getCount() {
       return -1;
    }
    
    /**
     * Easy one - writes out VOTable in memory to writer
     */
    public void toVotable(Writer out, QuerierProcessingResults querierStatus) throws IOException {
        XMLUtils.DocumentToWriter(doc,out);
    }

    /**
     * Writes out results in HTML form
     */
    public void toHtml(Writer out, QuerierProcessingResults querierStatus) throws IOException {
        throw new UnsupportedOperationException("Not yet impelmeneted");
    }
    
    /**
     * writes out VOTable in memory to writer in CSV form
     */
    public void toCSV(Writer out, QuerierProcessingResults querierStatus) throws IOException {
        throw new UnsupportedOperationException("Not yet impelmeneted");
    }
    
}


/*
$Log: EgsoResults.java,v $
Revision 1.3  2004/09/01 12:10:58  mch
added results.toHtml

Revision 1.2  2004/07/07 14:32:54  KevinBenson
Few small changes because I had it referencing "cds" at the moment.

Revision 1.1  2004/07/07 09:17:40  KevinBenson
New SEC/EGSO proxy to query there web service on the Solar Event Catalog

Revision 1.2  2004/03/14 03:04:57  mch
Added CSV writer

Revision 1.1  2004/03/13 23:40:59  mch
Changes to adapt to It05 refactor

Revision 1.2  2003/12/09 16:25:08  nw
wrote plugin documentation

*/
