/*$Id: DatacenterApplication.java,v 1.6 2009/05/27 15:25:57 gtr Exp $
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
import java.security.Principal;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Hashtable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.applications.AbstractApplication;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.Status;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;
import org.astrogrid.applications.parameter.ParameterAdapter;
import org.astrogrid.applications.description.ParameterDescription;
import org.astrogrid.applications.parameter.protocol.ExternalValue;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.cfg.PropertyNotFoundException;
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
import org.astrogrid.security.SecurityGuard;
import org.astrogrid.slinger.sourcetargets.URISourceTargetMaker;
import org.astrogrid.slinger.targets.TargetIdentifier;
import org.astrogrid.slinger.sources.SourceIdentifier;
import org.astrogrid.slinger.targets.WriterTarget;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.slinger.homespace.HomespaceName;
import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.StarTableWriter;
import uk.ac.starlink.votable.VOTableWriter;
import uk.ac.starlink.votable.DataFormat;
import uk.ac.starlink.ttools.cone.ConeMatcher;
import uk.ac.starlink.ttools.cone.ConeSearcher;
import uk.ac.starlink.ttools.task.TableProducer;
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
   
  private static final Log logger = LogFactory.getLog(DatacenterApplication.class);

  /**
   * The credentials held for each job. Values in the map are
   * containers of sets of matched credentials. Keys are job IDs.
   * This cache is internally synchronized.
   */
  private static Hashtable<String,SecurityGuard> credentials;

  /**
   * The job identifier.
   */
  protected final String job;
   
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
      job = ids.getId();
      credentials = new Hashtable<String,SecurityGuard>();
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
            logger.debug("assigned QuerierID " + querierID);
         }
      }
      catch (Throwable e) {
         this.setStatus(Status.ERROR);
         this.reportFailure(e.getMessage());
         this.reportError(e+" executing "+this.getTool().getName(),e);
         return;
      }
      finally {
        credentials.remove(job);
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
    * Tells the CEA library to use this object as the Runnable for executing
    * the job.
    *
    * @see org.astrogrid.applications.Application#createExecutionTask()
    */
   @Override
   public Runnable createExecutionTask() throws CeaException {
     return this;
   }

   /**
    * Creates the "target identifier" which controls disposition of the
    * query results. Handles credentials for authentication where appropriate.
    *
    * @return The identifier
    * @throws CeaException For any failure.
    */
   protected TargetIdentifier getResultTargetIdentifier() throws CeaException {
     try {
       ParameterValue resultTarget = findOutputParameter(DatacenterApplicationDescription.RESULT);

       // Results will be written to the URI given as the parameter value.
       // The transaction with that URI may be authenticated; the passed
       // principal can identify a named user or an anonymous call.
       if (resultTarget.getIndirect()==true) {
         if ((resultTarget.getValue() == null) ||
             (resultTarget.getValue().trim().length() == 0)) {
           throw new CeaException("Query parameter error: ResultTarget is indirect but value is empty");
         }
         return URISourceTargetMaker.makeSourceTarget(resultTarget.getValue(), getGuard());
       }
       
       // Results will be kept in the server and called out thence by the client.
       // Hence, results must be written to the parameter value as a string.
       // See queryStatusChanged for what happens to the results when the query 
       // is complete.
       else {
         return new WriterTarget(new StringWriter(), true); 
       }
     }
     catch (Exception ex) {
       throw new CeaException(
           "Query parameter error: couldn't process results destination: "+
           ex.getMessage()
       );
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
         if (findAdapter == null) {
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

  /**
   * Determines the results of authentication.
   * The results are returned packed in a security-guard object. If the
   * caller is authenticated, this contains the authenticated principal and
   * any delegated credentials; otherwise, an empty guard is returned.
   * 
   * @return The guard (never null, even for anonymous callers).
   */
  private SecurityGuard getGuard() throws CertificateException {
    logger.debug(String.format("Looking for credentials for job %s", job));
    SecurityGuard sg = credentials.get(job);
    if (sg == null) {
      logger.debug("Nothing found; making an empty SecurityGuard.");
      sg = new SecurityGuard();
    }
    else {
      logger.debug("Credentials found; loading delegated proxy.");
      sg.loadDelegation();
    }
    
    X509Certificate[] chain2 = sg.getCertificateChain();
	  for (int i = 0; i < chain2.length; i++) {
			X509Certificate c = chain2[i];
			logger.debug(c.getSubjectDN() + " issued by " + c.getIssuerDN());
		}
    return sg;
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
         logger.error("Failed to make an error report to the client: ", ex);
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
   @Override
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
   @Override
   protected ParameterAdapter instantiateAdapter(ParameterValue pval,
        ParameterDescription descr, ExternalValue indirectVal) {
      return new DatacenterParameterAdapter(pval, descr, indirectVal);
   }

  /**
   * Sets the credentials for a given job.
   *
   * @param job The job identifier.
   * @param g1 The credentials.
   */
  static public void putCredentials(String job, SecurityGuard g1) {
    SecurityGuard g2 = (g1 == null)? new SecurityGuard() : new SecurityGuard(g1);
    credentials.put(job, g2);
    logger.debug(String.format("Credentials are cached for job %s", job));
  }

}