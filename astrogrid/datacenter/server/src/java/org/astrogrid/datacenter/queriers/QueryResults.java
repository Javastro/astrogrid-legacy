/*
 * $Id: QueryResults.java,v 1.8 2004/03/14 02:17:07 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers;

import java.io.IOException;
import java.io.Writer;
import org.astrogrid.datacenter.queriers.status.QuerierProcessingResults;

/** A container interface that holds the results of a query until needed.
 * <p>
 *   Basically we
 * don't know what format the raw results will be in (eg, they may be SqlResults for
 * an JDBC connection, but something else altogether for other catalogue formats)
 * so this is a 'container' to hold those results until it needs to be
 * translated. It would be fully
 * implemented by the same package that implements the DatabaseQuerier and
 * QueryTranslater.
 *
 * @author M Hill
 */

public abstract class QueryResults
{
   public static final String FORMAT_VOTABLE = "VOTABLE"; //request results to be in votable format
   public static final String FORMAT_CSV     = "CSV";    //request results in CSV
   
   /** All Virtual Observatories must be able to provide the results in VOTable
    * format.  The statusToUpdate can be used to change the querier's status so that
    * monitors can see how things are going.
    */
   public abstract void toVotable(Writer out, QuerierProcessingResults statusToUpdate) throws IOException;
   
   /** Comma Seperated Variable format does not contain the metadata of VOtable, but is
    * very common and can be put straight into spreadsheets, etc.
    */
   public abstract void toCSV(Writer out, QuerierProcessingResults statusToUpdate) throws IOException;
   
   /** Returns the number of results - or -1 if unknown */
   public abstract int getCount() throws IOException;
   
   /** Looks at given format and decides which output method to use */
   public void write(Writer out, QuerierProcessingResults statusToUpdate, String format) throws IOException {
      if (format.toUpperCase().equals(FORMAT_VOTABLE)) {
         toVotable(out, statusToUpdate);
      }
      else if (format.toUpperCase().equals(FORMAT_CSV)) {
         toCSV(out, statusToUpdate);
      }
      else {
         throw new IllegalArgumentException("Unknown results format "+format+" given");
      }
   }
}

