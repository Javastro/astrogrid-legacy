/*$Id: XmlResults.java,v 1.2 2004/10/18 13:11:30 mch Exp $
 * Created on 13-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.queriers.xql;

import java.io.*;

import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.datacenter.queriers.status.QuerierProcessingResults;
import org.astrogrid.io.Piper;

/**
 * A results wrapper around an XML document stream
 *
 */
public class XmlResults extends QueryResults {

    protected final InputStream in;
   
    /**  Std Constructor. xmlIn is a stream containing the xml document
     */
    public XmlResults(Querier querier, InputStream xmlIn) {
      super(querier);
      this.in = xmlIn;
    }

    /**  Constructor where the xml doc is in the given File
     */
    public XmlResults(Querier querier, File xmlFile) throws IOException {
      super(querier);
      this.in = new FileInputStream(xmlFile);
    }
    
    public int getCount() {
       return -1;
    }

    /**
     * Writes out raw - as is
     */
    public void writeRaw(Writer out, QuerierProcessingResults querierStatus) throws IOException {
        Piper.bufferedPipe(new InputStreamReader(in), out);
    }
    
    /**
     * writes out in Votable
     */
    public void writeVotable(Writer out, QuerierProcessingResults querierStatus) throws IOException {
        throw new UnsupportedOperationException("Not yet implemeneted");
    }
    
    /**
     * writes out in HTML
     */
    public void writeHtml(Writer out, QuerierProcessingResults querierStatus) throws IOException {
        throw new UnsupportedOperationException("Not yet implemeneted");
    }

    /**
     * writes out in CSV form
     */
    public void writeCSV(Writer out, QuerierProcessingResults querierStatus) throws IOException {
        throw new UnsupportedOperationException("Not yet implemeneted");
    }
    
}


/*
$Log: XmlResults.java,v $
Revision 1.2  2004/10/18 13:11:30  mch
Lumpy Merge

Revision 1.1.2.1  2004/10/15 19:59:06  mch
Lots of changes during trip to CDS to improve int test pass rate

Revision 1.2  2004/09/29 18:43:51  mch
doc change

Revision 1.1  2004/09/28 15:02:13  mch
Merged PAL and server packages

Revision 1.1  2004/09/07 00:54:20  mch
Tidied up Querier/Plugin/Results, and removed deprecated SPI-visitor-SQL-translator

Revision 1.1  2004/09/06 21:37:26  mch
Factored out VotableResults

Revision 1.3  2004/09/01 12:10:58  mch
added results.toHtml

Revision 1.2  2004/03/14 03:04:57  mch
Added CSV writer

Revision 1.1  2004/03/13 23:40:59  mch
Changes to adapt to It05 refactor

Revision 1.2  2003/12/09 16:25:08  nw
wrote plugin documentation

Revision 1.1  2003/11/18 11:23:49  nw
mavenized cds delegate

Revision 1.1  2003/11/18 11:10:05  nw
mavenized cds delegate
 
*/
