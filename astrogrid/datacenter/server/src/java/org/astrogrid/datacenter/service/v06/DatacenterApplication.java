/*$Id: DatacenterApplication.java,v 1.1 2004/07/13 17:11:09 nw Exp $
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

import org.astrogrid.applications.AbstractApplication;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.Status;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.ParameterDescription;
import org.astrogrid.applications.parameter.DefaultParameterAdapterFactory;
import org.astrogrid.applications.parameter.ParameterAdapter;
import org.astrogrid.applications.parameter.ParameterAdapterFactory;
import org.astrogrid.applications.parameter.indirect.IndirectParameterValue;
import org.astrogrid.applications.parameter.indirect.IndirectionProtocolLibrary;
import org.astrogrid.community.Account;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QuerierListener;
import org.astrogrid.datacenter.queriers.status.QuerierStatus;
import org.astrogrid.datacenter.query.AdqlQuery;
import org.astrogrid.datacenter.query.ConeQuery;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.datacenter.query.QueryState;
import org.astrogrid.datacenter.service.DataServer;
import org.astrogrid.workflow.beans.v1.Tool;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Jul-2004
 *
 */
public class DatacenterApplication extends AbstractApplication implements QuerierListener {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(DatacenterApplication.class);

    /** Construct a new DatacetnerApplication
     * @param arg0
     * @param arg1
     * @param arg2
     * @param arg3
     */
    public DatacenterApplication(IDs ids, Tool tool, ApplicationInterface interf, IndirectionProtocolLibrary arg3,DataServer serv) throws IOException {
        super(ids, tool,interf, arg3);
        this.serv = serv;
        String resultsFormat = tool.getXPathFor("input/parameter[name='" + DatacenterApplicationDescription.FORMAT+ "']");
        this.acc = new Account(ids.getUser().getUserId(),ids.getUser().getCommunity(),ids.getUser().getToken());        
        Query query = buildQuery(tool,interf);        
        this.querier = Querier.makeQuerier(acc,query,ti,resultsFormat);
        this.querier.addListener(this);
    }
    protected final Account acc;
    protected final DataServer serv;   
    protected final Querier querier;
    protected final CEATargetIndicator ti = CEATargetIndicator.newInstance();
    /** id allocated by datacenter to this querier */
    protected String querierID;
    /** construct a query from the contents of the tool
     * @param t
     * @return
     */
    protected final Query buildQuery(Tool t,ApplicationInterface interf) {
        if (interf.getName().equals(DatacenterApplicationDescription.CONE_IFACE)) {
            return new ConeQuery(
                Double.parseDouble((String)t.findXPathValue("input/parameter[name='" + DatacenterApplicationDescription.RA + "']"))
                , Double.parseDouble((String)t.findXPathValue("input/parameter[name='" + DatacenterApplicationDescription.DEC + "']"))
                , Double.parseDouble((String)t.findXPathValue("input/parameter[name='" + DatacenterApplicationDescription.RADIUS + "']"))
                );
        } else if (interf.getName().equals(DatacenterApplicationDescription.ADQL_IFACE)) {
            return new AdqlQuery((String)t.findXPathValue("input/parameter[name='" + DatacenterApplicationDescription.QUERY + "']"));
        } else { // can't get here - as would have barfed during 'initializeApplication' in DatacenterApplicatonDescription
            logger.fatal("Programming logic error - unknown interface" + interf.getName());
            throw new IllegalArgumentException("Programming logic error - unknown interface" + interf.getName());
        }
    }
    
    
    /**
     * @see org.astrogrid.applications.Application#execute()
     */
    public boolean execute() throws CeaException {
        results.clearResult();
        
        try {
            setStatus(Status.INITIALIZED);
            querierID = serv.submitQuerier(querier);                  
            return true;
        }
        catch (Throwable e) {
            throw new CeaException("Could not execute application",e);
        }
    }
    /**
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


    /** listener to the associated querier - updates state of application, propagates notifications, etc. 
     * @see org.astrogrid.datacenter.queriers.QuerierListener#queryStatusChanged(org.astrogrid.datacenter.queriers.Querier)
     */
    public void queryStatusChanged(Querier querier) {
       QuerierStatus qs = querier.getStatus();
       QueryState state = qs.getState();
       // would be nice to use a switch here, but can't.
       if (state.equals(QueryState.CONSTRUCTED)) {
           this.setStatus(Status.INITIALIZED);
           
       } else if (state.equals(QueryState.STARTING)) {
           this.setStatus(Status.RUNNING);
                      
       } else if (state.equals(QueryState.RUNNING_QUERY)) {
           this.reportMessage(qs.toString());
           
       } else if (state.equals(QueryState.QUERY_COMPLETE)) {
           this.reportMessage(qs.toString());
           
       } else if (state.equals(QueryState.RUNNING_RESULTS)) {
           this.setStatus(Status.WRITINGBACK);
           final ParameterAdapter result = (ParameterAdapter)outputAdapters.get(0);
           //necessary to perform write-back in separate thread - as we don't know what thread is calling this callback
           // and it mustn't be the same one as is going to write out the output - otherwise we'll deadlock on the pipedReader/Writer pair. 
           Thread worker = new Thread() {
               public void run() {
                    try {               
                        result.writeBack(ti);
                    } catch (CeaException e) {
                        reportError("Failed to write back parameter values",e);
                    }
                }
           };
           worker.start();
       } else if (state.equals(QueryState.ABORTED)) {
           this.reportMessage(qs.toString());
            this.setStatus(Status.ERROR); // this is the convention.
            
       } else if (state.equals(QueryState.ERROR)) {
           this.reportError(qs.toString()); // sets us in error state.
           
       } else if (state.equals(QueryState.FINISHED)) {
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
     * @see org.astrogrid.applications.AbstractApplication#createAdapterFactory()
     */
    protected ParameterAdapterFactory createAdapterFactory() {
        return new DefaultParameterAdapterFactory(lib) {
            protected ParameterAdapter instantiateAdapter(ParameterValue pval, ParameterDescription descr, IndirectParameterValue indirectVal) {
               return new DatacenterParameterAdapter(pval,descr,indirectVal);
            }
        };
    }

}


/* 
$Log: DatacenterApplication.java,v $
Revision 1.1  2004/07/13 17:11:09  nw
first draft of an itn06 CEA implementation for datacenter
 
*/