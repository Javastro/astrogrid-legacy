/*$Id: VotableInResults.java,v 1.1 2004/09/28 15:02:13 mch Exp $
 * Created on 13-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.queriers;

import java.io.*;

import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.datacenter.queriers.status.QuerierProcessingResults;
import org.astrogrid.io.Piper;

/**
 * A results wrapper around results that are already of VOTable form; eg
 * those returned from another service
 *
 */
public class VotableInResults extends QueryResults {

    protected final InputStream in;
   
    /**  Std Constructor. votableIn is a stream containing the votable
     */
    public VotableInResults(Querier querier, InputStream votableIn) {
      super(querier);
      this.in = votableIn;
    }

    /**  Constructor where the votable is in the given File
     */
    public VotableInResults(Querier querier, File votableFile) throws IOException {
      super(querier);
      this.in = new FileInputStream(votableFile);
    }
    
    public int getCount() {
       return -1;
    }
    
    /**
     * Easy one - writes out VOTable in memory to writer
     */
    public void toVotable(Writer out, QuerierProcessingResults querierStatus) throws IOException {
        Piper.bufferedPipe(new InputStreamReader(in), out);
    }
    
    /**
     * Not quite so easy one - HTML
     */
    public void toHtml(Writer out, QuerierProcessingResults querierStatus) throws IOException {
        throw new UnsupportedOperationException("Not yet implemeneted");
    }

    /**
     * writes out VOTable in memory to writer in CSV form
     */
    public void toCSV(Writer out, QuerierProcessingResults querierStatus) throws IOException {
        throw new UnsupportedOperationException("Not yet implemeneted");
    }
    
}


/*
$Log: VotableInResults.java,v $
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
