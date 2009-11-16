/*
 * $Id: TableResults.java,v 1.4 2009/11/16 10:11:02 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.queriers;

import java.io.IOException;
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
import org.astrogrid.query.Query;
import org.astrogrid.query.returns.ReturnSpec;
import org.astrogrid.query.returns.ReturnTable;
import org.astrogrid.slinger.sourcetargets.UrlSourceTarget;
import org.astrogrid.slinger.targets.TargetIdentifier;
import org.astrogrid.tableserver.out.ConeSearchVoTableWriter;
import org.astrogrid.tableserver.out.FilteredTableWriter;
import org.astrogrid.tableserver.out.HtmlTableWriter;
import org.astrogrid.tableserver.out.TableWriter;
import org.astrogrid.tableserver.out.VoTableWriter;
import org.astrogrid.tableserver.out.VoTableBinaryWriter;
import org.astrogrid.tableserver.out.XsvTableWriter;

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
      return new String[] { ReturnTable.VOTABLE, ReturnTable.VOTABLE_BINARY, ReturnTable.CSV, ReturnTable.HTML };
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
   
   /** Subclasses override to make spocial table writers.  requested format is given
    * as a mime type*/
   public TableWriter makeTableWriter(TargetIdentifier target, String requestedFormat, Principal user) throws IOException {

     if (requestedFormat.equals(ReturnTable.VOTABLE) ||
         requestedFormat.equals(ReturnTable.VOTABLE_GENERIC) ||
         requestedFormat.equals(ReturnTable.VOTABLE_TEXT) ||
         requestedFormat.equals(ReturnTable.DEFAULT)) {
       if (querier.getQuery().getQuerySource().equals(Query.CONE_SOURCE)) {
         return new ConeSearchVoTableWriter(target, "Cone-search Results", user);
       }
       else {
         return new VoTableWriter(target, "Query Results", user);
       }
     }
      else if (requestedFormat.equals(ReturnTable.VOTABLE_BINARY)) {
         return new VoTableBinaryWriter(target, "Query Results", user);
      }
      else if (requestedFormat.equals(ReturnTable.CSV)) {
         return new XsvTableWriter(target, "Query Results", ",", user);
      }
      else if (requestedFormat.equals(ReturnTable.TSV)) {
         return new XsvTableWriter(target, "Query Results", "\t", user);
      }
      else if (requestedFormat.equals(ReturnTable.HTML)) {
         return new HtmlTableWriter(target, "Query Results", querier.getQuery().toHTMLString(), user);
      }
      else {
         throw new IllegalArgumentException("Unknown results format "+requestedFormat+" ws requested");
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

      TableWriter tableWriter = makeTableWriter(target, format, user);
      
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

      if (target instanceof UrlSourceTarget) {
         s =s+"<a href='ViewFile?"+((UrlSourceTarget) target).toURI()+"'>"+target+"</a>";
      }
      else {
         s =s+target;
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




