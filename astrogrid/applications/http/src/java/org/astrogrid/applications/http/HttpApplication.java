/* $Id: HttpApplication.java,v 1.3 2004/07/30 14:54:47 jdt Exp $
 * Created on Jul 24, 2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.applications.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.AbstractApplication;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.Status;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.parameter.ParameterAdapter;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;
import org.astrogrid.workflow.beans.v1.Tool;

/**
 * An Application that calls an http service, such as a SIAP service.
 *  ? Responsible for extracting the relevant info from the application
 * description and passing them to a HttpServiceClient that knows how to call the web service.
 * Handles the threading (so execute doesn't block) and status settings during
 * execution. ?
 * 
 * 
 * 
 * @author jdt
 */
public class HttpApplication extends AbstractApplication implements Runnable {
    /**
     * Commons Logger for this class
     */
    private static final Log log = LogFactory.getLog(HttpApplication.class);
    private HttpServiceClient client;
    private final Map inputArguments = new HashMap();

    

	/**
     * Ctor
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	public HttpApplication(IDs arg0, Tool arg1, ApplicationInterface arg2,
			ProtocolLibrary arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}
    /**
     * @see org.astrogrid.applications.Application#execute(org.astrogrid.applications.ApplicationExitMonitor)
     */
    public boolean execute() throws CeaException {
        //@TODO check this out ... how parameters work
        createAdapters();
        log.debug("Processing arguments");
        for (Iterator i = inputParameterAdapters(); i.hasNext();) {
            ParameterAdapter a = (ParameterAdapter) i.next();
            final String name = a.getWrappedParameter().getName();
            final Object value = a.process();
            inputArguments.put(name,value);
            log.debug(name+"="+value);
        }
        HttpApplicationDescription description = (HttpApplicationDescription)getApplicationDescription();            

        //@TODO can we get this set up in a more picocontainerish way to faciliate testing
        client = new HttpServiceClient(description.getUrl(),
                                HttpServiceClient.HttpServiceType.GET);

        Thread task = new Thread(this);
        setStatus(Status.INITIALIZED);
        task.start();
        return true;
    }

    /**
     * Where the action happens
     * 
     * @TODO restore this to a nested class later for better encapsulation.
     */
    public void run() {
        setStatus(Status.RUNNING);
        String resultVal = null;
        try {
            resultVal = client.call(inputArguments);
            // we can do this, as we know there's only ever going to be one
            // interface, and one output parameter.
            setStatus(Status.WRITINGBACK);
            Iterator outputParamsIt = outputParameterAdapters();
            ParameterAdapter result = (ParameterAdapter) outputParamsIt;
            assert !outputParamsIt.hasNext() : "Expect there to be only one output parameter for an HttpApplication";
            result.writeBack(resultVal);
            setStatus(Status.COMPLETED);
        } catch (CeaException e) {
            reportError("Failed to write back parameter values", e);
        } catch (Throwable t) {
            reportError("Something else gone wrong", t);
        }
    }

    /**
     * @TODO - probably not needed since all params will
     * be strings so Default will do.
     * 
     * @see org.astrogrid.applications.AbstractApplication#instantiateAdapter(org.astrogrid.applications.beans.v1.parameters.ParameterValue,
     *      org.astrogrid.applications.description.ParameterDescription,
     *      org.astrogrid.applications.parameter.indirect.IndirectParameterValue)
     */
    /*
     * protected ParameterAdapter instantiateAdapter(ParameterValue pval,
     * ParameterDescription descr, IndirectParameterValue indirectVal) { return
     * new HttpParameterAdapter(pval, descr, indirectVal); }
     */ 
}

/*
 * $Log: HttpApplication.java,v $
 * Revision 1.3  2004/07/30 14:54:47  jdt
 * merges in from case3 branch
 *
 * Revision 1.1.4.5  2004/07/30 13:11:28  jdt
 * wired up the parameters
 *
 * Revision 1.1.4.4  2004/07/29 21:30:47  jdt
 * *** empty log message ***
 *
 * Revision 1.1.4.3  2004/07/29 17:08:22  jdt
 * Think about how I'm going to get stuff out of the registry
 *
 * Revision 1.1.4.2  2004/07/27 19:28:43  jdt
 * fix build errors
 *
 * Revision 1.1.4.1  2004/07/27 17:20:25  jdt
 * merged from applications_JDT_case3
 *
 * Revision 1.1.2.2  2004/07/24 18:43:29  jdt
 * Started plumbing in the httpclient
 * Revision 1.1.2.1 2004/07/24 17:16:16 jdt
 * Borrowed from javaclass application....stealing is always quicker.
 *  
 */