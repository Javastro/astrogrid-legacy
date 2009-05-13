/*
 * $Id: UrlListResults.java,v 1.1 2009/05/13 13:20:26 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.queriers;

import java.io.IOException;
import java.security.Principal;
import org.astrogrid.dataservice.queriers.Querier;
import org.astrogrid.dataservice.queriers.TableResults;
import org.astrogrid.dataservice.queriers.status.QuerierStatus;
import org.astrogrid.query.returns.ReturnTable;
import org.astrogrid.slinger.targets.TargetIdentifier;
import org.astrogrid.tableserver.metadata.ColumnInfo;
import org.astrogrid.tableserver.out.HtmlTableWriter;
import org.astrogrid.tableserver.out.TableWriter;
import org.astrogrid.tableserver.out.VoTableFitsWriter;
import org.astrogrid.tableserver.out.VoTableWriter;
import org.astrogrid.tableserver.out.XsvTableWriter;

/**
 * Results which are a list of URLs
 *
 * @author M Hill
 */

public class UrlListResults extends TableResults {
   
   
   
   /** List of URLs to (probably) FITS files  */
   protected String[] urls;
   
   /**
    * Construct this wrapper around the given list of results
    */
   public UrlListResults(Querier parentQuerier, String[] results) {
      super(parentQuerier);
      this.urls = results;
   }
   
   /** Returns number of found files */
   public int getCount() throws IOException {
      return urls.length;
   }

   /** Overriden to return VoTableFitsWriter instead of VotableWriter */
   public TableWriter makeTableWriter(TargetIdentifier target, String requestedFormat, Principal user) throws IOException {

      if (requestedFormat.equals(ReturnTable.VOTABLE)) {
         return new VoTableFitsWriter(target, "Query Results", user);
      }

      return super.makeTableWriter(target, requestedFormat, user);
   }
   
   
   /** Subclasses implement suitable ways of writing their results to the given TableWriter    */
   public void writeTable(TableWriter tableWriter, QuerierStatus statusToUpdate) throws IOException {

      tableWriter.open();
      
      //need to do some better work on the metadata...
      
      tableWriter.startTable(new ColumnInfo[] {});
      
      if (statusToUpdate != null) {
         statusToUpdate.newProgress("Adding file", getCount());
      }
      
      for (int i=0;i<urls.length;i++) {
         if (querier.isAborted()) {
            tableWriter.abort();
            return;
         }
         if (statusToUpdate != null) {
            statusToUpdate.setProgress(i);
         }
         tableWriter.writeRow(new String[] { urls[i] });
      }

      tableWriter.endTable();
      
      if (statusToUpdate != null)   { statusToUpdate.clearProgress(); }
      
      tableWriter.close();
   }

   
}


