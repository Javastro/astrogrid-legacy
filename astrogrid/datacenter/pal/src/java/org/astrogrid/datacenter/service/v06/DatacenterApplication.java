/*$Id: DatacenterApplication.java,v 1.12 2004/11/11 20:42:50 mch Exp $
 * Created on 12-Jul-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.datacenter.service.v06;

import EDU.oswego.cs.dl.util.concurrent.Executor;
import java.io.IOException;
import java.io.StringWriter;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.AbstractApplication;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.Status;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;
import org.astrogrid.community.Account;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QuerierListener;
import org.astrogrid.datacenter.queriers.status.QuerierStatus;
import org.astrogrid.datacenter.query.AdqlQueryMaker;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.datacenter.query.QueryState;
import org.astrogrid.datacenter.query.SimpleQueryMaker;
import org.astrogrid.datacenter.service.DataServer;
import org.astrogrid.slinger.targets.TargetIndicator;
import org.astrogrid.slinger.targets.TargetMaker;
import org.astrogrid.slinger.targets.WriterTarget;
import org.astrogrid.workflow.beans.v1.Tool;
import org.xml.sax.SAXException;

/** Represents a query running against the datacenter.
 * <p />
 * Datacenter already has a framework for starting asynchronous queries and then monitoring their progress.
 * This class wraps the datacenter framework, mapping querier events into events in the CEA framework.
 *
 * There is one instance of this class for each query
 *
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Jul-2004
 *
 */
public class DatacenterApplication extends AbstractApplication implements QuerierListener {
   
   /**
    * Commons Logger for this class
    */
   private static final Log logger = LogFactory.getLog(DatacenterApplication.class);
   
   protected final Account acc;
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
      this.acc = new Account(ids.getUser().getUserId(),ids.getUser().getCommunity(),ids.getUser().getToken());
      logger.info("CEA DSA initialised, Job ID="+ids.getJobStepId());
   }
   
   /** construct a query from the contents of the tool
    * (mch) not entirely happy with this following changes to Query that include
    * the result spec.  I've set it to table here and hope that targets etc get
    * set properly later (as it would have done before)
    * @param t
    * @return
    */
   protected final Query buildQuery(ApplicationInterface interf)  throws IOException, CeaException {

      if (interf.getName().equals(DatacenterApplicationDescription.CONE_IFACE)) {
         return SimpleQueryMaker.makeConeQuery(
            Double.parseDouble((String)findInputParameterAdapter(DatacenterApplicationDescription.RA).process())
               , Double.parseDouble((String)findInputParameterAdapter(DatacenterApplicationDescription.DEC).process())
               , Double.parseDouble((String)findInputParameterAdapter(DatacenterApplicationDescription.RADIUS).process())
         );
      }
      else if (interf.getName().equals(DatacenterApplicationDescription.ADQL_IFACE)) {
         String querySource = findInputParameter(DatacenterApplicationDescription.QUERY).getValue();
         String queryString = (String)findInputParameterAdapter(DatacenterApplicationDescription.QUERY).process();
         if ((queryString == null) || (queryString.trim().length() == 0)) {
            throw new IOException("Read empty string at "+querySource);
         }
         logger.debug("Query will be " + queryString);
         try {
            return AdqlQueryMaker.makeQuery(queryString);
         }
         catch (SAXException e) { throw new IllegalArgumentException(e+", reading Adql at "+querySource); }
         catch (ParserConfigurationException e) { throw new CeaException("Server error:",e); }
      }
      else
      {
         // shouldn't get here - as would have barfed during 'initializeApplication' in DatacenterApplicatonDescription
         logger.fatal("Programming logic error - unknown interface" + interf.getName());
         throw new IllegalArgumentException("Programming logic error - unknown interface" + interf.getName());
      }
      
   }
   
   
   /**
    * @see org.astrogrid.applications.Application#execute()
    */
   public boolean execute() throws CeaException {
      createAdapters();
      try {
         ParameterValue resultTarget = findOutputParameter(DatacenterApplicationDescription.RESULT);
         TargetIndicator ti = null;
         if (resultTarget.getIndirect()==true) {
            //results will go to the URI given in the parameter
            if ((resultTarget.getValue() == null) || (resultTarget.getValue().trim().length() == 0)) {
               throw new CeaException("ResultTarget is indirect but value is empty");
            }
            ti = TargetMaker.makeIndicator(resultTarget.getValue());
         } else {
            //direct-to-cea target, so results must get written to a string to be inserted into the
            //parametervalue when complete (see queryStatusChanged)
            ti = TargetMaker.makeIndicator(new StringWriter(), true); //close stream when finished writing to it
         }

         String resultsFormat = (String)findInputParameterAdapter(DatacenterApplicationDescription.FORMAT).process();
         Query query = buildQuery(getApplicationInterface());
         query.getResultsDef().setTarget(ti);
         query.getResultsDef().setFormat(resultsFormat);
         Querier querier = Querier.makeQuerier(acc,query, this);
         querier.addListener(this);
         
         setStatus(Status.INITIALIZED);
         querierID = serv.submitQuerier(querier);
         logger.info("assigned QuerierID " + querierID);
         return true;
      }
      catch (Throwable e) {
         reportError(e+" executing "+this.getTool().getName(),e);
         if (e instanceof CeaException) {
            throw (CeaException) e;
         }
         else {
            throw new CeaException(e+", executing application "+this.getTool().getName(),e);
         }
      }
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
         
      } else if (state.equals(QueryState.STARTING) || state.equals(QueryState.RUNNING_QUERY)) {
         this.setStatus(Status.RUNNING);
         
      } else if (state.equals(QueryState.QUERY_COMPLETE)) {
         this.reportMessage(qs.toString());
         
      } else if (state.equals(QueryState.RUNNING_RESULTS)) {
         this.setStatus(Status.WRITINGBACK);
         /* this is all handled by the target indicators and the 'finish' below
          final ParameterAdapter result = (ParameterAdapter)outputParameterAdapters().next();
          //necessary to perform write-back in separate thread - as we don't know what thread is calling this callback
          // and it mustn't be the same one as is going to write out the output - otherwise we'll deadlock on the pipe.
          Runnable worker = new Runnable() {
          public void run() {
          try {
          CEATargetIndicator ti = (CEATargetIndicator)querier.getQuery().getTarget();
          result.writeBack(ti);
          setStatus(Status.COMPLETED); // now the application has completed..
          } catch (CeaException e) {
          reportError("Failed to write back parameter values",e);
          }
          }
          };
          try {
          exec.execute(worker);
          }catch (InterruptedException e) {
          reportMessage("couldn't start worker thread to read results");
          }
          */
      } else if (state.equals(QueryState.ABORTED)) {
         this.reportMessage(qs.toString());
         this.setStatus(Status.ERROR); // this is the convention.
         
      } else if (state.equals(QueryState.ERROR)) {
         this.reportError(qs.toString()); // sets us in error state.
         
      } else if (state.equals(QueryState.FINISHED)) {
         ParameterValue result = findOutputParameter(DatacenterApplicationDescription.RESULT);
         if (result.getIndirect() != true) {
            //if the results were to be directed to CEA, they will be stored in a StringWriter in
            //the WriterTarget
            WriterTarget target = (WriterTarget) querier.getQuery().getTarget();
            StringWriter sw = (StringWriter) target.resolveWriter(acc);
            result.setValue(sw.toString() );
         }

         this.setStatus(Status.COMPLETED);
         this.reportMessage(qs.toString());
         
      } else if (state.equals(QueryState.UNKNOWN)) {
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
 taken account of new backend phase and tidied up some logging

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

