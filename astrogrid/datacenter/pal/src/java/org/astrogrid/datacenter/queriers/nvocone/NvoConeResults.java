/*
 * $Id: NvoConeResults.java,v 1.1 2004/09/28 15:02:13 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.queriers.nvocone;

import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.datacenter.queriers.status.QuerierProcessingResults;

/**
 * The National Virtual Observatory, an American effort, defined a simple
 * cone search service:
 * @see http://www.us-vo.org/metadata/conesearch/
 * <p>
 * Cunning plan - there is no real need for this plugin to connect to the URL
 * unless it needs to stream the results back to the front end.  The URL can
 * just be given to the StoreClient to connect to.  No status info though...
 *
 * @author M Hill
 */

public class NvoConeResults extends QueryResults {
   
   public NvoConeResults(Querier parentQuerier, URL coneUrl)  {
      super(parentQuerier);
      resultsUrl = coneUrl;
   }

   /** All Virtual Observatories must be able to provide the results in VOTable
    * format.  The statusToUpdate can be used to change the querier's status so that
    * monitors can see how things are going.
    */
   public void toVotable(Writer out, QuerierProcessingResults statusToUpdate) throws IOException {
      throw new UnsupportedOperationException("Don't support VOTable yet");
   }
   
   /**
    * Converts results to HTML to given writer
    */
   public void toHtml(Writer out, QuerierProcessingResults statusToUpdate) throws IOException
   {
      throw new UnsupportedOperationException("Don't support HTML yet");
   }

   /** Comma Seperated Variable format does not contain the metadata of VOtable, but is
    * very common and can be put straight into spreadsheets, etc.
    */
   public void toCSV(Writer out, QuerierProcessingResults statusToUpdate) throws IOException {
      throw new UnsupportedOperationException("Don't support HTML yet");
   }
   
   /** Returns the number of results - or -1 if unknown */
   public int getCount() throws IOException {
      return -1;
   }
   
   Log log = LogFactory.getLog(NvoConeResults.class);

   /** url to connect to  */
   protected URL resultsUrl = null;

}

/*
$Log: NvoConeResults.java,v $
Revision 1.1  2004/09/28 15:02:13  mch
Merged PAL and server packages

Revision 1.1  2004/09/07 00:54:20  mch
Tidied up Querier/Plugin/Results, and removed deprecated SPI-visitor-SQL-translator




*/


