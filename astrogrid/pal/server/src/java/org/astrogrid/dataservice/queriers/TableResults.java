/*
 * $Id: TableResults.java,v 1.6 2005/03/30 18:54:03 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.queriers;

import org.astrogrid.tableserver.out.*;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.Principal;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.cfg.ConfigException;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.dataservice.DatacenterException;
import org.astrogrid.dataservice.queriers.status.QuerierProcessingResults;
import org.astrogrid.dataservice.queriers.status.QuerierStatus;
import org.astrogrid.query.returns.ReturnSpec;
import org.astrogrid.query.returns.ReturnTable;
import org.astrogrid.slinger.SRI;
import org.astrogrid.slinger.targets.TargetIdentifier;
import org.astrogrid.slinger.vospace.HomespaceName;
import org.astrogrid.slinger.vospace.IVOSRN;
import uk.ac.starlink.fits.FitsTableWriter;

/** A container interface that holds the results of a query that is in some
 * way a table.  Implementations might be SqlResults.
 *
 * @author M Hill
 */

public abstract class TableResults implements QueryResults
{
   
   Log log = LogFactory.getLog(TableResults.class);
   
   public final static String TABLE_FILTERS_KEY = "datacenter.table.filter";
   
   protected Querier querier = null;

   /** Construct with a link to the Querier that spawned these results, so we
    * can include info from it if need be */
   public TableResults(Querier parentQuerier) {
      this.querier = parentQuerier;
   }
   
   /** Subclasses implement suitable ways of writing their results to the given TableWriter    */
   public abstract void writeTable(TableWriter tableWriter, QuerierStatus statusToUpdate) throws IOException;

   /** returns the formats that this result implementation can produce (ie VOTABLE, HTML, CSV, etc) */
   public static String[] listFormats() {
      return new String[] { ReturnTable.VOTABLE, ReturnTable.CSV, ReturnTable.HTML };
   }
   
   /** Returns the number of results - or -1 if unknown */
   public abstract int getCount() throws IOException;

   /** This is a helper method for plugins; it is meant to be called
    * from the askQuery method.  It transforms the results and sends them
    * as required, updating the querier status appropriately.
    */
   public void send(ReturnSpec returns, Principal user) throws IOException {
      if (returns instanceof ReturnTable) {
         sendTable( (ReturnTable) returns, user);
      }
      else {
         throw new UnsupportedOperationException("Unknown return type "+returns.getClass().getName());
      }
   }
   
   /** Sends a table */
   public void sendTable(ReturnTable returns, Principal user) throws IOException {
      
      QuerierProcessingResults status = new QuerierProcessingResults(querier.getStatus());
      querier.setStatus(status);

      log.info(querier+", sending results to "+returns);

      TargetIdentifier target = returns.getTarget();
      String format = returns.getFormat();

      if (target == null) {
         throw new DatacenterException("No Target given for results");
      }

      status.setMessage("Sending results to "+target.toString()+" as "+format);

      //Set mime type before sending
      target.setMimeType(format, querier.getUser());
      
      Writer out = target.resolveWriter(querier.getUser());
      assert (out != null);

      TableWriter tableWriter = null;

      if (format.equals(ReturnTable.VOTABLE)) {
         tableWriter = new VoTableWriter(out, "Query Results");
      }
      else if (format.equals(ReturnTable.CSV)) {
         tableWriter = new XsvTableWriter(out, "Query Results", ",");
      }
      else if (format.equals(ReturnTable.TSV)) {
         tableWriter = new XsvTableWriter(out, "Query Results", "\t");
      }
      else if (format.equals(ReturnTable.HTML)) {
         tableWriter = new HtmlTableWriter(out, "Query Results", querier.getQuery().toString());
      }
//      else if (format.equals(ReturnTable.FITS)) {
//         tableWriter = new StilStarTableWriter(new FitsTableWriter(), out);
//      }
      else if (format.equals(ReturnTable.DEFAULT)) {
         format = ReturnTable.VOTABLE;
         tableWriter = new VoTableWriter(out, "Query Results");
      }
      else {
         throw new IllegalArgumentException("Unknown results format in return spec "+returns+" given");
      }
      
      //add  a filteredtablewriters if any
      Class filterClass = ConfigFactory.getCommonConfig().getClass(TABLE_FILTERS_KEY,  null);
      if (filterClass != null) {
         //wrap plugin around existing one
         tableWriter = makeTableWriter(filterClass, tableWriter);
      }

      //call overridden method that will know what form the data is in
      writeTable(tableWriter, status);

      if (querier.isAborted()) {
         return;
      }
      
      String s = "Results sent as table ("+format+") to ";

      if (returns.getTarget() instanceof SRI) {
         s =s+"<a href='ViewFile?"+((SRI) target).toURI()+"'>"+target+"</a>";
      }
      else {
         s =s+target;
      }
      //show resolving down
      if (target instanceof IVOSRN) {
         s = s + " -> "+((IVOSRN) target).resolve();
      }
      try {
         if (target instanceof HomespaceName) {
               s = s + " -> "+( (HomespaceName) target).resolveIvosrn();
               s = s + " -> "+((HomespaceName) target).toLocation(user);
         }
      }
      catch (Exception e) {
         log.error(e+" resolving "+target+" for adding to status ",e);
      }

      status.addDetail(s);
      status.setMessage("");
        
      log.info(querier+" results sent");
   }


   public TableWriter makeTableWriter(Class filter, TableWriter writer) {
      if (!filter.isInstance(FilteredTableWriter.class)) {
         throw new ConfigException("tablewriter filter class given by "+TABLE_FILTERS_KEY+" is not a FilteredTableWriter");
      }
      try {
         Constructor constr = filter.getConstructor(new Class[] { TableWriter.class });
         return (TableWriter) constr.newInstance(new Object[] { writer } );
      }
      catch (NoSuchMethodException nsme) {
         throw new ConfigException("Class "+filter+" given by "+TABLE_FILTERS_KEY+" must extend FilteredTableWriter and have a constructor that takes only a TableWriter");
      }
      catch (InvocationTargetException e) {
         throw new ConfigException("Class "+filter+" given by "+TABLE_FILTERS_KEY+" fails to construct",e);
      }
      catch (IllegalAccessException e) {
         throw new ConfigException("Class "+filter+" given by "+TABLE_FILTERS_KEY+" fails to construct",e);
      }
      catch (InstantiationException e) {
         throw new ConfigException("Class "+filter+" given by "+TABLE_FILTERS_KEY+" fails to construct",e);
      }
   }

}




