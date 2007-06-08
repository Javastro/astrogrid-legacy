/*$Id: DatacenterApplication.java,v 1.8 2007/06/08 13:16:08 clq2 Exp $
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
import java.net.URISyntaxException;
import java.net.URL;
import java.security.Principal;
import java.util.StringTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.AbstractApplication;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.Status;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;
import org.astrogrid.dataservice.queriers.Querier;
import org.astrogrid.dataservice.queriers.QuerierListener;
import org.astrogrid.dataservice.queriers.status.QuerierStatus;
import org.astrogrid.dataservice.service.AxisDataServer;
import org.astrogrid.dataservice.service.DataServer;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;
import org.astrogrid.io.account.LoginAccount;
import org.astrogrid.io.mime.MimeNames;
import org.astrogrid.query.Query;
import org.astrogrid.query.returns.ReturnTable;
import org.astrogrid.query.QueryException;
import org.astrogrid.query.QueryState;
import org.astrogrid.slinger.sourcetargets.HomespaceSourceTarget;
import org.astrogrid.slinger.sourcetargets.URISourceTargetMaker;
import org.astrogrid.slinger.targets.TargetIdentifier;
import org.astrogrid.slinger.targets.WriterTarget;
import org.astrogrid.workflow.beans.v1.Tool;
import org.xml.sax.SAXException;
import org.astrogrid.slinger.homespace.HomespaceName;
import org.astrogrid.slinger.ivo.IVORN;

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
   
   /** Construct a new DatacetnerApplication
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
     //logger.debug("GOT INTO BUILDQUERY");

      if (interf.getName().equals(DatacenterApplicationDescription.CONE_IFACE)) {
         //logger.debug("GOT INTO BUILDQUERY CONE");
        /*
         return SimpleQueryMaker.makeConeQuery(
            Double.parseDouble((String)findInputParameterAdapter(DatacenterApplicationDescription.RA).process())
               , Double.parseDouble((String)findInputParameterAdapter(DatacenterApplicationDescription.DEC).process())
               , Double.parseDouble((String)findInputParameterAdapter(DatacenterApplicationDescription.RADIUS).process())
         );
         */
         String catalogName = (String)findInputParameterAdapter(
               DatacenterApplicationDescription.CATALOG).process();
         String tableName = (String)findInputParameterAdapter(
               DatacenterApplicationDescription.TABLE).process();
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
   
   /**
    * 
    */
   public void run() {
      logger.debug("Starting to run CEA task with querier id " + getQuerierId());
      try {
         createAdapters();
         ParameterValue resultTarget = findOutputParameter(DatacenterApplicationDescription.RESULT);
         TargetIdentifier ti = null;
         if (resultTarget.getIndirect()==true) {
            //results will go to the URI given in the parameter
            if ((resultTarget.getValue() == null) || (resultTarget.getValue().trim().length() == 0)) {
               //throw new CeaException("ResultTarget is indirect but value is empty");
               setStatus(Status.ERROR);
               this.reportError("Query parameter error: ResultTarget is indirect but value is empty");
               return;
            }
            
            //special botch for converting ivo:// to homespace:// etc
            String targetUri = transformTarget(resultTarget.getValue());
            
            ti = URISourceTargetMaker.makeSourceTarget(targetUri);
         } else {
            //direct-to-cea target, so results must get written to a string to be inserted into the
            //parametervalue when complete.  See queryStatusChanged for what happens to the results
            //when the query is complete
            ti = new WriterTarget(new StringWriter(), true); //close stream when finished writing to it
         }

         String resultsFormat = MimeNames.getMimeType( findInputParameterAdapter(DatacenterApplicationDescription.FORMAT).process().toString());
         logger.debug("Selected results format is " + resultsFormat);
         
         /*
         ReturnTable returnTable = new ReturnTable(ti,resultsFormat);
         */
         Query query = buildQuery(getApplicationInterface());
         query.getResultsDef().setTarget(ti);
         query.getResultsDef().setFormat(resultsFormat);
         Querier querier = Querier.makeQuerier(acc,query, "CEA from "+AxisDataServer.getSource()+" [Job "+ids.getJobStepId()+"]");
         querier.addListener(this);
         
         setStatus(Status.INITIALIZED);
         querierID = serv.submitQuerier(querier);
         logger.info("assigned QuerierID " + querierID);
      }
      catch (Throwable e) {
         this.reportError(e+" executing "+this.getTool().getName(),e);
         // run() shouldn't really throw exceptions, better to report
         // error in normal framework
         /*
         if (e instanceof CeaException) {
            throw (CeaException) e;
         }
         else {
            throw new CeaException(e+", executing application "+this.getTool().getName(),e);
         }
         */
      }
   }

   /**
    * If the incoming URI describing the target location is an IVORN, it might be
    * an account or it might be a real IVORN.  Examines it to see if it is of the
    * form of an account (ie ivo://community/individual) and if so, checks to see
    * if the Registry can resolve it.  If it is of the right form, and the registry
    * cannot resolve it, turns it into a homespacename
    */
   public String transformTarget(String targetUri) throws IllegalArgumentException, URISyntaxException {
      
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
         this.reportMessage(qs.toString());
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
         this.reportMessage("Unkown state encountered " + state.toString());
      }
      
   }
   
   
   /** overridden, to return instances of datacenter parameter adapter.
    * @see org.astrogrid.applications.AbstractApplication#instantiateAdapter(org.astrogrid.applications.beans.v1.parameters.ParameterValue, org.astrogrid.applications.description.ParameterDescription, org.astrogrid.applications.parameter.indirect.IndirectParameterValue)
    *
   protected ParameterAdapter instantiateAdapter(ParameterValue arg0,
                                                 ParameterDescription arg1, ExternalValue arg2) {
      return new DatacenterParameterAdapter(arg0, arg1, arg2);
   }
    */
}


/*
 $Log: DatacenterApplication.java,v $
 Revision 1.8  2007/06/08 13:16:08  clq2
 KEA-PAL-2169

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

