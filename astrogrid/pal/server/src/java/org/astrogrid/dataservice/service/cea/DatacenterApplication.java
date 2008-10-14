/*$Id: DatacenterApplication.java,v 1.19 2008/10/14 12:28:51 clq2 Exp $
 * Created on 12-Jul-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.dataservice.service.cea;

import EDU.oswego.cs.dl.util.concurrent.Executor;
import java.io.IOException;
import java.io.StringWriter;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.ByteArrayInputStream;

import java.net.URISyntaxException;
import java.net.URL;
import java.security.Principal;
import java.util.StringTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.cfg.PropertyNotFoundException;

import org.astrogrid.community.resolver.CommunityAccountSpaceResolver;
import org.astrogrid.store.Ivorn;

import org.astrogrid.applications.AbstractApplication;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.Status;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;
import org.astrogrid.applications.parameter.ParameterAdapter;
import org.astrogrid.applications.description.ParameterDescription;
import org.astrogrid.applications.parameter.protocol.ExternalValue;

import org.astrogrid.dataservice.queriers.Querier;
import org.astrogrid.dataservice.queriers.QuerierListener;
import org.astrogrid.dataservice.queriers.status.QuerierStatus;
import org.astrogrid.dataservice.service.AxisDataServer;
import org.astrogrid.dataservice.service.DataServer;
import org.astrogrid.dataservice.service.Queues;
import org.astrogrid.dataservice.service.TokenQueue;
import org.astrogrid.dataservice.service.multicone.DirectConeSearcher;
import org.astrogrid.dataservice.service.multicone.DsaConeSearcher;
import org.astrogrid.dataservice.service.multicone.DsaQuerySequenceFactory;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;
import org.astrogrid.dataservice.metadata.MetadataException;
import org.astrogrid.io.account.LoginAccount;
import org.astrogrid.io.mime.MimeNames;
import org.astrogrid.query.Query;
import org.astrogrid.query.returns.ReturnTable;
import org.astrogrid.query.QueryException;
import org.astrogrid.query.QueryState;
import org.astrogrid.slinger.sourcetargets.HomespaceSourceTarget;
import org.astrogrid.slinger.sourcetargets.URISourceTargetMaker;
import org.astrogrid.slinger.targets.TargetIdentifier;
import org.astrogrid.slinger.sources.SourceIdentifier;
import org.astrogrid.slinger.targets.WriterTarget;
import org.astrogrid.slinger.agfm.FileManagerConnection;
import org.astrogrid.workflow.beans.v1.Tool;
import org.xml.sax.SAXException;
import org.astrogrid.slinger.homespace.HomespaceName;
import org.astrogrid.slinger.ivo.IVORN;

// For the multicone stuff
import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.StarTableWriter;
import uk.ac.starlink.votable.VOTableWriter;
import uk.ac.starlink.votable.DataFormat;
import uk.ac.starlink.table.JoinFixAction;
import uk.ac.starlink.task.TaskException;
import uk.ac.starlink.ttools.cone.ConeMatcher;
import uk.ac.starlink.ttools.cone.ConeSearcher;
import uk.ac.starlink.ttools.task.TableProducer;
import uk.ac.starlink.util.DataSource;
import uk.ac.starlink.table.OnceRowPipe;
import uk.ac.starlink.table.TableBuilder;
import uk.ac.starlink.votable.VOTableBuilder;

/** Represents a query running against the datacenter.
 * <p />
 * Datacenter already has a framework for starting asynchronous queries and then monitoring their progress.
 * This class wraps the datacenter framework, mapping querier events into events in the CEA framework.
 *
 * There is one instance of this class for each query
 *
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Jul-2004
 * @author K Andrews
 *
 */
public class DatacenterApplication extends AbstractApplication implements QuerierListener, Runnable {
   
   /**
    * Commons Logger for this class
    */
   private static final Log logger = LogFactory.getLog(DatacenterApplication.class);
   
   protected final Principal acc;
   /** the datacenter system object - a static, which provides access to the datacenter query framework */
   protected final DataServer serv;
   /** the executor for background tasks */
   Executor exec;
   
   /** id allocated by datacenter to this querier */
   protected String querierID;
   
   public String getQuerierId() { return querierID;   }
   
   /** Construct a new DatacenterApplication
    * @param arg0
    * @param arg1
    * @param arg2
    * @param arg3
    */
   public DatacenterApplication(IDs ids, Tool tool, ApplicationInterface interf, ProtocolLibrary arg3,DataServer serv,Executor exec) {
      super(ids, tool,interf, arg3);
      this.serv = serv;
      this.exec = exec;
      this.acc = new LoginAccount(ids.getUser().getUserId(),ids.getUser().getCommunity());
      logger.info("CEA DSA initialised, Job ID="+ids.getJobStepId());
   }

   /**
    * 
    */
   public void run() {
      logger.debug("Starting to run CEA task with querier id " + getQuerierId());
      try {
         createAdapters();

         // Get result target and format
         TargetIdentifier ti = getResultTargetIdentifier();
         String resultsFormat = getResultsFormat();
         logger.debug("Selected results format is " + resultsFormat);
         
         String iName = getApplicationInterface().getName();
         if (
           DatacenterApplicationDescription.MULTICONE_IFACE.equals(iName)
         ) { 
              // Handle the multicone application as a special case.
              // Note: the doMultiConeService method will set the
              // CEA status as required
              logger.info(
                 "Got a multicone job, handing off to multicone service");
              doMulticoneService(ti);
         }
         else {
            // For regular cone or adql query, build and run a single query
            // Get catalog name if set in Ivorn
            String catName = getCatalogNameFromAppIvorn(
                  getApplicationDescription().getName());
            // Make sure that it's legit
            runCatalogNameCheck(catName);

            Query query = buildQuery(getApplicationInterface());
            if ((catName != null) && (!"".equals(catName))) {
               query.setParentCatalogReferences(catName);
            }
            query.getResultsDef().setTarget(ti);
            query.getResultsDef().setFormat(resultsFormat);
            Querier querier = Querier.makeQuerier(acc,query, "CEA from "+AxisDataServer.getSource()+" [Job "+ids.getJobStepId()+"]");
            querier.addListener(this);
            
            setStatus(Status.INITIALIZED);
            querierID = serv.submitQuerier(querier,false);
            logger.info("assigned QuerierID " + querierID);
         }
      }
      catch (Throwable e) {
         this.setStatus(Status.ERROR);
         this.reportFailure(e.getMessage());
         this.reportError(e+" executing "+this.getTool().getName(),e);
         return;
      }
   }

   
   /** construct a query from the contents of the tool
    * (mch) not entirely happy with this following changes to Query that include
    * the result spec.  I've set it to table here and hope that targets etc get
    * set properly later (as it would have done before)
    * FURTHER COMMENT BY KEA:  Explicitly setting the ReturnSpec in the 
    * CONE_IFACE query now, to a ReturnTable.
    * @param t
    * @return
    */
   protected final Query buildQuery(ApplicationInterface interf)  throws IOException, CeaException, QueryException {
      if (interf.getName().equals(
               DatacenterApplicationDescription.CONE_IFACE)) {
         String catalogName = "", tableName = "";
         String fullName = (String)findInputParameterAdapter(
               DatacenterApplicationDescription.CATTABLE).process();
         int dotIndex = fullName.lastIndexOf('.');
         if (dotIndex == -1) {   //Not found
            throw new QueryException(
                "Expected table name of the form 'catalog.table', but got '"
                + fullName + "'");
         }
         else {
            catalogName = fullName.substring(0,dotIndex);
            tableName = fullName.substring(dotIndex+1);
         }
         String raColName = TableMetaDocInterpreter.getConeRAColumnByName(
               catalogName, tableName);
         String decColName = TableMetaDocInterpreter.getConeDecColumnByName(
               catalogName, tableName);
         String units = TableMetaDocInterpreter.getConeUnitsByName(
               catalogName, tableName);
         
         return new Query(
            catalogName, tableName, units, raColName, decColName,
            Double.parseDouble((String)findInputParameterAdapter(DatacenterApplicationDescription.RA).process()),
            Double.parseDouble((String)findInputParameterAdapter(DatacenterApplicationDescription.DEC).process()),
            Double.parseDouble((String)findInputParameterAdapter(DatacenterApplicationDescription.RADIUS).process()),
            // Close the stringwriter stream when you have finished writing to it.
            // Is this a potential source of out-of-memory problems?
            new ReturnTable(new WriterTarget(new StringWriter(),true))
         );
      }
      else if (interf.getName().equals(DatacenterApplicationDescription.ADQL_IFACE)) {
         //logger.debug("GOT INTO BUILDQUERY ADQL");
         String querySource = findInputParameter(DatacenterApplicationDescription.QUERY).getValue();
         String queryString = (String)findInputParameterAdapter(DatacenterApplicationDescription.QUERY).process();
         if ((queryString == null) || (queryString.trim().length() == 0)) {
            throw new IOException("Read empty string at "+querySource);
         }
         //logger.debug("Query will be " + queryString);
         return new Query(queryString);
      }
      else
      {
         // shouldn't get here - as would have barfed during 'initializeApplication' in DatacenterApplicatonDescription
         logger.fatal("Programming logic error - unknown interface" + interf.getName());
         throw new IllegalArgumentException("Programming logic error - unknown interface" + interf.getName());
      }
   }
   
   /**
    * @see org.astrogrid.applications.Application#createExecutionTask()
    */
   public Runnable createExecutionTask() throws CeaException {
     return this;
   }
   

   protected TargetIdentifier getResultTargetIdentifier() throws CeaException
   {
      try {
         ParameterValue resultTarget = findOutputParameter(DatacenterApplicationDescription.RESULT);
         if (resultTarget.getIndirect()==true) {
            //results will go to the URI given in the parameter
            if ((resultTarget.getValue() == null) || (resultTarget.getValue().trim().length() == 0)) {
               throw new CeaException("Query parameter error: ResultTarget is indirect but value is empty");
            }
         }
         if (resultTarget.getIndirect()==true) {
            return URISourceTargetMaker.makeSourceTarget(resultTarget.getValue());
         } else {
            //direct-to-cea target, so results must get written to a string 
            //to be inserted into the parametervalue when complete.  
            //See queryStatusChanged for what happens to the results
            //when the query is complete
            //close stream when finished writing to it
            return new WriterTarget(new StringWriter(), true); 
         }
      }
      catch (Exception ex) {
         throw new CeaException(
               "Query parameter error: couldn't process results destination: "+
               ex.getMessage());
      }
   }

   protected String getResultsFormat() throws CeaException
   {
      try {
         ParameterAdapter formatAdapter = findInputParameterAdapter(
               DatacenterApplicationDescription.FORMAT);
         if (formatAdapter == null) {
            // Default to VOTable - for multicone
            return MimeNames.getMimeType("VOTable"); 
         }
         else {
            return MimeNames.getMimeType(formatAdapter.process().toString());
         }
      }
      catch (Exception ex) {
         throw new CeaException(
               "Query parameter error: couldn't process results format: "+
               ex.getMessage());
      }
   }

   public String getCatalogNameFromAppIvorn(String ivorn) throws CeaException
   {
      // Application ivorns/names here take the following form:
      //  auth_id/resource_id/{DATABASE_NAME}/ceaApplication
      //  The {DATABASE_NAME/} bit is only present in a multi-catalog
      //  DSA - but if it is present, we need to make sure that table
      //  names in the ADQL query have the appropriate database prefix
      //  (we can't currently rely on the QB etc to put the db prefix in)
      //
      //  IMPORTANT NOTE:  The resource_id might legitimately contain a "/"
      //  character
      // First check that we haven't got *no* cat name
      //
      if (ivorn == null || "".equals(ivorn)) {
         // Just for sanity
         return "";
      }
      String ceaSuffix = "/ceaApplication";
      String name = ivorn;
      if (name.startsWith("ivo://")) {
         name = name.substring(6);
      }
      String authID = "", resKey = "", autoIvorn="",autoPrefix="";
      try {
         authID =  ConfigFactory.getCommonConfig().getString(
            "datacenter.authorityId");
         resKey =  ConfigFactory.getCommonConfig().getString(
            "datacenter.resourceKey");
         autoPrefix = authID+"/"+resKey;
         autoIvorn = autoPrefix+ceaSuffix;
      }
      catch (PropertyNotFoundException pnfe) {
         // NB Shouldn't get here
         throw new CeaException("Configuration error: Properties 'datacenter.authorityId' and 'datacenter.resourceKey' must both be set, please check your configuration.");
      }
      if (autoIvorn.equals(name)) {
         //Simplest case
         return "";     // Definitely no catalog name present
      }
      // We expect the registration to start with the autoPrefix
      if (!name.startsWith(autoPrefix)) {
         throw new CeaException("Programming logic error:  expected CEA Application IVORN to commence with '" + autoPrefix + "';  instead it is " + ivorn);
      }
      // We expect the registration to end with "/ceaApplication"
      if (!name.endsWith(ceaSuffix)) {
         throw new CeaException("Programming logic error:  expected CEA Application IVORN to end with '" + ceaSuffix + "';  instead it is " + ivorn);
      }
      // We must have some additional material present in the ivorn apart
      // from prefix and suffix;  this means that there will also be an
      // extra / after the prefix
      // Remove prefix 
      name = name.substring(autoPrefix.length()+1);
      // Remove suffix
      name = name.substring(0,name.length()-ceaSuffix.length());
      return name;
   }

   protected void runCatalogNameCheck(String name) throws CeaException
   {
      try {
         if ( ! ((name == null) || (name.equals(""))) ) {
            // If a catalog name is included in the IVORN, check that
            // it is valid
            boolean catnameFound = false;
            try {
               String[] catalogNames = 
                  TableMetaDocInterpreter.getCatalogNames();
               for (int c = 0; c < catalogNames.length; c++) {
                  if (name.equals(catalogNames[c])) {
                     catnameFound = true;
                  }
               }
            }
            catch (MetadataException me) {
               throw new CeaException(
                 "System error: this datacenter is misconfigured, please consult the system administrator: " + me.getMessage());
            }
            if (!catnameFound) {
               throw new CeaException(
                     "System error: the CEA application you are using " +
                     "refers to a catalog '" + name + 
                     "' but this catalog is not recognised. " +
                     "Please ask your DSA administrator to check the registry for out-of-date CEA registrations."
               );
            }
         }
         else {
            // We *don't* have a catalog name - let's just check that we
            // don't have multiple catalogs in this DSA (we might get 
            // an out-of-date registration if a DSA gets upgraded from
            // single-cat to multi-cat)
            String[] catalogNames = TableMetaDocInterpreter.getCatalogNames();
            if ((catalogNames != null) && (catalogNames.length > 1)) {
               throw new CeaException(
                     "System error: the CEA application you are using " + 
                     "does not refer to a particular catalog but this DSA wraps multiple catalogs and needs to know which one you want.  Please ask your DSA administrator to check the registry for out-of-date CEA registrations."
               );
            }
         }
      }
      catch (MetadataException me) {
         throw new CeaException(
           "System error: this datacenter is misconfigured, please consult the system administrator: " + me.getMessage());
      }
   }


   protected void doMulticoneService(TargetIdentifier ti) 
   {
      // The multicone application needs special handling
      try {
         // First work out which table needs to be searched
         String catalogName = "", tableName = "";
         String fullName = (String)findInputParameterAdapter(
               DatacenterApplicationDescription.CATTABLE).process();
         int dotIndex = fullName.lastIndexOf('.');
         if (dotIndex == -1) {   //Not found
            throw new QueryException(
                "Expected table name of the form 'catalog.table', but got '"
                + fullName + "'");
         }
         else {
            catalogName = fullName.substring(0,dotIndex);
            tableName = fullName.substring(dotIndex+1);
         }
      
         // Now work out which are the RA and Dec columns in the
         // input VOTable, and the search radius
         // All these ones will be set (they are compulsory)
         String raCol = (String)findInputParameterAdapter(
               DatacenterApplicationDescription.RA_COL).process();
         String decCol = (String)findInputParameterAdapter(
               DatacenterApplicationDescription.DEC_COL).process();
         double radius = Double.parseDouble((String)findInputParameterAdapter(
               DatacenterApplicationDescription.RADIUS).process());
         // Bit naughty casting here but we need to get at some 
         // datacenter-specific bits of the parameter
         DatacenterParameterAdapter votableAdapter = 
               (DatacenterParameterAdapter)findInputParameterAdapter(
               DatacenterApplicationDescription.INPUT_VOTABLE);
         String inputVotable = (String)votableAdapter.process();
         boolean isIndirect = votableAdapter.isIndirect();

         // This one is optional
         boolean bestOnly = false;
         String findMode = null;
         ParameterAdapter findAdapter = findInputParameterAdapter(DatacenterApplicationDescription.FIND_MODE);
         if ((findAdapter == null) || ("".equals(findAdapter))) {
            // Shouldn't get here
            bestOnly = false;
         }
         else {
            findMode = (String)findAdapter.process();
            if ("BEST".equalsIgnoreCase(findMode)) {
               bestOnly = true;
            }
            else if ("ALL".equalsIgnoreCase(findMode)) {
               bestOnly = false;
            }
            else {
               throw new IllegalArgumentException("Find Mode must be BEST or ALL - unrecognised value "+findMode);
            }
         }
         // Check if our INPUT_VOTABLE parameter is direct or indirect,
         // and choose behaviour accordingly
         final InputStream in;
         if (isIndirect) {
            SourceIdentifier si = null;
            String sourceUri = transformTarget(inputVotable);
            //String sourceUri = inputVotable;
            si = URISourceTargetMaker.makeSourceTarget(sourceUri);
            in = si.openInputStream();
         }
         else {
            in = new ByteArrayInputStream(inputVotable.getBytes());
         }

         final TableBuilder inputHandler = new VOTableBuilder();
         TableProducer inProd = new TableProducer() {
            public StarTable getTable() throws IOException {
               OnceRowPipe rowStore = new OnceRowPipe();
               inputHandler.streamStarTable(in, rowStore, null);
               return rowStore.waitForStarTable();
            }
         };

         // Get a suitable ConeSearcher object.  There are several possible
         // implementations.  Currently a DirectConeSearcher is used by
         // default, which uses a direct JDBC connection to the database, 
         // bypassing the expensive repeated ADQL translation layer.  
         // To provide throttling, we block waiting for a free slot in the 
         // global AsyncConnectionQueue before proceeding.
         final ConeSearcher coneSearcher;
         String forceadql = "false";
         try {
            forceadql =  ConfigFactory.getCommonConfig().getString(
               "datacenter.multicone.forceadql","false");
         }
         catch (PropertyNotFoundException pnfe) {
            // Just ignore if property is unset
         }
         if (!("true".equalsIgnoreCase(forceadql))) {
             TokenQueue.Token token = Queues.getAsyncConnectionQueue().waitForToken();  // may block if server is busy
             coneSearcher = DirectConeSearcher
                           .createConeSearcher(token, catalogName, tableName, bestOnly);
         }
         else {
             coneSearcher = new DsaConeSearcher(catalogName, tableName, acc, "CEA multicone application");
         }
         TableProducer matcher = new ConeMatcher(
             coneSearcher, inProd, new DsaQuerySequenceFactory(raCol, decCol, radius), bestOnly);

         // Obtains the output table object.
         StarTable outTable;
         outTable = matcher.getTable();
         //
         // Serializes the output table to the TargetIdentifier output stream
         StarTableWriter outputHandler =
               new VOTableWriter(DataFormat.TABLEDATA, true);
         //response.setContentType(outputHandler.getMimeType());
         OutputStream ostrm = ti.openOutputStream();
         outputHandler.writeStarTable(outTable, ostrm);
         ostrm.flush();
         ostrm.close();
         this.setStatus(Status.COMPLETED);
      }
      catch (Throwable e) {
         this.setStatus(Status.ERROR);
         reportFailure("An exception occurred in processing this job: " + 
               e.toString());
         this.reportError(e.toString()+" executing "+this.getTool().getName(),e);
         return;
      }
   }
   private void reportFailure(String message) {
      try {
         // Make a value object to carry the report as a parameter.
         ParameterValue report = new ParameterValue();
         report.setName("cea-error");
         report.setIndirect(false);
         report.setValue(message);
         //
         // Add the packaged report to the list of results such that the client
         // gets it together with (or instead of) the output parameters.
         this.getResult().addResult(report);
      }
      // If the error reporting fails, then there's nothing to do but log it and
      // go on.
      catch (Exception ex) {
         this.logger.error("Failed to make an error report to the client: ", ex);
      }
   }


   /**
    * If the incoming URI describing the target location is an IVORN, it might be
    * an account or it might be a real IVORN.  Examines it to see if it is of the
    * form of an account (ie ivo://community/individual) and if so, checks to see
    * if the Registry can resolve it.  If it is of the right form, and the registry
    * cannot resolve it, turns it into a homespacename
    * KONA Jan 2008 NOTE - This seems to work with latest VOExplorer for 
    * input parameter urls, but worth checking again later
    */
   protected String transformTarget(String targetUri) throws IllegalArgumentException, URISyntaxException {
      
      int hashIdx = targetUri.indexOf("#");
      String key = targetUri.substring(0,hashIdx).substring(6);
      int slashIdx = key.indexOf("/");

      /* just assume it's a homespace ivorn for now
      if (!IVORN.isIvorn(targetUri)) { return targetUri; } //some other URI

      int hashIdx = targetUri.indexOf("#");
      if (hashIdx == -1) {
         throw new IllegalArgumentException("Target URI "+targetUri+" has no path given");
      }
      String key = targetUri.substring(0,hashIdx).substring(6);
      int slashIdx = key.indexOf("/");
      if ((slashIdx != -1) && (slashIdx == key.lastIndexOf("/"))) { //if there is only one slash
         
         //check it can't be resolved
         try {
            new IVORN(targetUri).resolve();
            //it resolved OK, so it's a real IVORN.  Leave unchanged
            return targetUri;
         }
         catch (IOException rre) { } //carry on, it's not resolvable (normally), so...
       */
         //assume any exception means it can't be found, so it must be a 'homespace' id.  Can't do much better than that at the mo anyway
         HomespaceName homespace = HomespaceName.fromIvorn(targetUri);
         logger.info("Converting incoming '"+targetUri+"' to '"+homespace+"'");
         return homespace.toURI();
//      }
//      return targetUri;
   }

   /** Implemented by calling abot on the querier object - so if the underlyng database back end supports abort, the cec does too
    * @see org.astrogrid.applications.Application#attemptAbort()
    */
   public boolean attemptAbort() {
      try {
         QuerierStatus stat = serv.abortQuery(acc,querierID);
         if (stat.getState().equals(QueryState.ABORTED)) {
            return true;
         } else {
            return false; // assume this means we couldn't abort.
         }
      } catch (IOException e) {
         logger.warn("Attempted abort failed with exception",e);
         return false;
      }
   }
   
   
   /** callback method for our associated querier
    * <p />Through this method, this class receives notifications in changes of state of the running query.
    * These state changes and messages are propagated onto the cea framework,
    * and results are grabbed from the query at the appropriate time.
    * @see org.astrogrid.datacenter.queriers.QuerierListener#queryStatusChanged(org.astrogrid.datacenter.queriers.Querier)
    */
   public void queryStatusChanged(final Querier querier) {
      QuerierStatus qs = querier.getStatus();
      QueryState state = qs.getState();
      logger.debug("CEA seen DSA state="+state);
      // would be nice to use a switch here, but can't.
      if (state.equals(QueryState.CONSTRUCTED) || state.equals(QueryState.QUEUED)) {
         this.setStatus(Status.INITIALIZED);
         
      } 
      else if (state.equals(QueryState.STARTING) || state.equals(QueryState.RUNNING_QUERY)) {
         this.setStatus(Status.RUNNING);
         
      } 
      else if (state.equals(QueryState.QUERY_COMPLETE)) {
         this.reportMessage(qs.toString());
      } 
      else if (state.equals(QueryState.RUNNING_RESULTS)) {
         this.setStatus(Status.WRITINGBACK);
      } 
      else if (state.equals(QueryState.ABORTED)) {
         this.reportMessage(qs.toString());
         this.setStatus(Status.ERROR); // this is the convention.
      } 
      else if (state.equals(QueryState.ERROR)) {
         /*this.reportError(qs.toString()); // sets us in error state.*/
         //this.reportMessage(qs.toString());
         this.reportFailure(qs.toString());
         this.setStatus(Status.ERROR);
      } 
      else if (state.equals(QueryState.FINISHED)) {
         ParameterValue result = findOutputParameter(DatacenterApplicationDescription.RESULT);
         if (result.getIndirect() != true) {
            //if the results were to be directed to CEA, they will be stored in a StringWriter in
            //the WriterTarget
            WriterTarget target = (WriterTarget) querier.getQuery().getTarget();
            StringWriter sw = (StringWriter) target.openWriter();
            result.setValue(sw.toString() );
         }

         this.setStatus(Status.COMPLETED);
         this.reportMessage(qs.toString());
         
      } 
      else if (state.equals(QueryState.UNKNOWN)) {
         this.setStatus(Status.UNKNOWN);
         this.reportMessage(qs.toString());
         
      } else {
         logger.fatal("Programming error - unknown state encountered" + state.toString());
         // would like to throw, but won't - as don't know who called me.
         this.setStatus(Status.UNKNOWN);
         this.reportMessage("Unknown state encountered " + state.toString());
      }
      
   }
   
   /** overridden, to return instances of datacenter parameter adapter.
    * @see org.astrogrid.applications.AbstractApplication#instantiateAdapter(org.astrogrid.applications.beans.v1.parameters.ParameterValue, org.astrogrid.applications.description.ParameterDescription, org.astrogrid.applications.parameter.indirect.IndirectParameterValue)
    */
   protected ParameterAdapter instantiateAdapter(ParameterValue pval,
        ParameterDescription descr, ExternalValue indirectVal) {
      return new DatacenterParameterAdapter(pval, descr, indirectVal);
   }
}


/*
 $Log: DatacenterApplication.java,v $
 Revision 1.19  2008/10/14 12:28:51  clq2
 PAL_KEA_2804

 Revision 1.17.12.1  2008/10/13 10:57:35  kea
 Updating prior to maintenance merge.

 Revision 1.17  2008/03/14 16:09:21  clq2
 PAL_KEA_2619

 Revision 1.16.2.1  2008/03/11 15:29:50  kea
 Tweaks for multicone, and conesearch compulsory UCDs.

 Revision 1.16  2008/03/06 14:46:45  clq2
 PAL_KEA_2588

 Revision 1.15.4.1  2008/03/06 13:01:52  kea
 Tweaks.

 Revision 1.15  2008/02/07 17:27:45  clq2
 PAL_KEA_2518

 Revision 1.13.8.1  2008/02/07 16:36:15  kea
 Further fixes for 1.0 support, and also MBT's changes merged into my branch.

 Revision 1.14  2008/02/06 14:10:41  clq2
 pal-mbt-multicone2 merged

 Revision 1.13.4.4  2008/01/29 14:06:47  mbt
 Use a direct JDBC connection for efficiency

 Revision 1.13.4.3  2008/01/25 13:29:41  mbt
 Name and slight functionality change from SkyConeMh2Producer to ConeMatcher

 Revision 1.13.4.2  2008/01/23 14:44:27  mbt
 SkyConeMatch2Producer constructor changed again

 Revision 1.13.4.1  2008/01/15 15:50:20  mbt
 SkyConeMatch2Producer constructor signature changed

 Revision 1.13  2007/12/04 17:31:39  clq2
 PAL_KEA_2378

 Revision 1.11.2.1  2007/11/15 18:19:15  kea
 Multicone fixes, various bugzilla ticket fixes, tweaks after profiling.

 Revision 1.12  2007/11/01 11:25:46  kea
 Merging MBT's branch pal-mbt-multicone1.

 Revision 1.11.4.1  2007/10/25 14:26:59  mbt
 Fixed to match modified multicone interface

 Revision 1.11  2007/10/17 09:58:20  clq2
 PAL_KEA-2314

 Revision 1.10.2.2  2007/10/11 13:53:19  kea
 Still working on multicone stuff.

 Revision 1.10.2.1  2007/09/25 17:17:29  kea
 Working on CEA interface for multicone service.

 Revision 1.10  2007/09/07 09:30:51  clq2
 PAL_KEA_2235

 Revision 1.9.4.3  2007/09/04 12:27:03  kea
 Tweaks

 Revision 1.9.4.1  2007/08/10 14:13:28  kea
 Work-in-progress update.

 Revision 1.9  2007/06/12 12:12:00  kea
 Adding cone cea interface back.

 Revision 1.7.2.3  2007/06/12 11:54:08  kea
 Putting back CEA conesearch.

 Revision 1.7.2.2  2007/05/18 16:34:12  kea
 Still working on new metadoc / multi conesearch.

 Revision 1.7.2.1  2007/05/16 11:03:52  kea
 Removing siap stuff, not in use.

 Revision 1.7  2007/03/14 16:26:49  kea
 Work in progress re VOResource v1.0 registrations, and re out-of-memory
 error (now cleaning up Query inside Querier once query has been run to
 conserve memory - but need to do something more sensible with the
 completed jobs queue too).

 Revision 1.6  2007/03/02 13:49:09  kea
 Syntactic changes only.

 Revision 1.5  2007/02/20 12:22:14  clq2
 PAL_KEA_2062

 Revision 1.4.20.2  2007/02/13 15:58:59  kea
 Added proper OptionList for supported output types (including new
 support for binary VOTable).

 Revision 1.4.20.1  2007/01/18 13:15:33  kea
 Checking in current state of project files to get help with debugging.
 Also made changes to DatacenterApplication re bug 2064 (but not
 fully tested this yet).

 Revision 1.4  2006/06/15 16:50:08  clq2
 PAL_KEA_1612

 Revision 1.3.64.3  2006/05/10 13:25:22  kea
 Conesearch and HSQLDB fixes/enhancements;  moving properties to web.xml
 like other AG services.

 Revision 1.3.64.2  2006/04/21 11:54:05  kea
 Changed QueryException from a RuntimeException to an Exception.

 Revision 1.3.64.1  2006/04/19 13:57:31  kea
 Interim checkin.  All source is now compiling, using the new Query model
 where possible (some legacy classes are still using OldQuery).  Unit
 tests are broken.  Next step is to move the legacy classes sideways out
 of the active tree.

 Revision 1.3  2005/05/27 16:21:08  clq2
 mchv_1

 Revision 1.2.16.4  2005/05/16 14:10:54  mch
 use new fromIvorn method

 Revision 1.2.16.3  2005/05/13 16:56:31  mch
 'some changes'

 Revision 1.2.16.2  2005/04/25 14:41:14  mch
 better comment

 Revision 1.2.16.1  2005/04/21 17:20:51  mch
 Fixes to output types

 Revision 1.2  2005/03/21 18:45:55  mch
 Naughty big lump of changes

 Revision 1.1.1.1  2005/02/17 18:37:35  mch
 Initial checkin

 Revision 1.1.1.1  2005/02/16 17:11:24  mch
 Initial checkin

 Revision 1.12.2.11  2005/02/11 15:58:27  mch
 Some fixes before split

 Revision 1.12.2.10  2004/12/08 18:36:40  mch
 Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults

 Revision 1.12.2.9  2004/12/07 21:21:09  mch
 Fixes after a days integration testing

 Revision 1.12.2.8  2004/12/07 00:02:17  mch
 added tentative ivorn->homespace converter

 Revision 1.12.2.7  2004/11/30 01:04:02  mch
 Rationalised tablewriters, reverted AxisDataService06 to string

 Revision 1.12.2.6  2004/11/29 21:52:18  mch
 Fixes to skynode, log.error(), getstem, status logger, etc following tests on grendel

 Revision 1.12.2.5  2004/11/25 08:29:41  mch
 added table writers modelled on STIL

 Revision 1.12.2.4  2004/11/23 17:46:52  mch
 Fixes etc

 Revision 1.12.2.3  2004/11/23 12:34:01  mch
 renamed to makeTarget

 Revision 1.12.2.2  2004/11/23 11:55:06  mch
 renamved makeTarget methods

 Revision 1.12.2.1  2004/11/22 00:57:16  mch
 New interfaces for SIAP etc and new slinger package

 Revision 1.12  2004/11/11 20:42:50  mch
 Fixes to Vizier plugin, introduced SkyNode, started SssImagePlugin

 Revision 1.11  2004/11/10 22:01:50  mch
 skynode starts and some fixes

 Revision 1.10  2004/11/09 17:42:22  mch
 Fixes to tests after fixes for demos, incl adding closable to targetIndicators

 Revision 1.9  2004/11/08 02:58:44  mch
 Various fixes and better error messages

 Revision 1.8  2004/11/03 00:17:56  mch
 PAL_MCH Candidate 2 merge

 Revision 1.3.8.4  2004/11/02 21:51:54  mch
 Replaced AgslTarget with UrlTarget and MySpaceTarget

 Revision 1.3.8.3  2004/11/02 19:48:43  mch
 Split TargetIndicator to indicator and maker

 Revision 1.3.8.2  2004/11/02 19:41:26  mch
 Split TargetIndicator to indicator and maker

 Revision 1.3.8.1  2004/10/20 18:12:45  mch
 CEA fixes, resource tests and fixes, minor navigation changes

 Revision 1.4.2.1  2004/10/20 12:43:28  mch
 Fixes to CEA interface to write directly to target

 Revision 1.4  2004/10/20 08:10:55  pah
 taken Principal of new backend phase and tidied up some logging

 Revision 1.3  2004/10/07 10:34:44  mch
 Fixes to Cone maker functions and reading/writing String comparisons from Query

 Revision 1.2  2004/10/06 21:12:17  mch
 Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

 Revision 1.1  2004/09/28 15:02:13  mch
 Merged PAL and server packages

 Revision 1.8  2004/09/17 01:27:21  nw
 added thread management.

 Revision 1.7  2004/09/06 15:20:47  mch
 Added logger message for execute()

 Revision 1.6  2004/08/25 23:38:34  mch
 (Days changes) moved many query- and results- related classes, renamed packages, added tests, added CIRCLE to sql/adql parsers

 Revision 1.5  2004/08/17 20:19:36  mch
 Moved TargetIndicator to client

 Revision 1.4  2004/07/27 13:48:33  nw
 renamed indirect package to protocol,
 renamed classes and methods within protocol package

 Revision 1.3  2004/07/22 16:31:22  nw
 cleaned up application / parameter adapter interface.

 Revision 1.2  2004/07/20 02:14:48  nw
 final implementaiton of itn06 Datacenter CEA interface

 Revision 1.1  2004/07/13 17:11:09  nw
 first draft of an itn06 CEA implementation for datacenter
 
 */





