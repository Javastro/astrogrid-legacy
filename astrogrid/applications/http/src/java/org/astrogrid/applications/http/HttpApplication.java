/* $Id: HttpApplication.java,v 1.2 2004/07/27 17:49:57 jdt Exp $
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
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
import org.astrogrid.applications.AbstractApplication.IDs;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.ParameterDescription;
import org.astrogrid.applications.parameter.ParameterAdapter;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;
import org.astrogrid.workflow.beans.v1.Tool;

/**
 * An Application that calls an http service, such as a SIAP service.
 *  ? Responsible for extracting the relevant info from the application
 * description and passing them to a ? that knows how to call the web service.
 * Handles the threading (so execute doesn't block) and status settings during
 * execution. ?
 * 
 * 
 * 
 * @author jdt
 */
public class HttpApplication extends AbstractApplication implements Runnable {
    /**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	public HttpApplication(IDs arg0, Tool arg1, ApplicationInterface arg2, ProtocolLibrary arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}

	/**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(HttpApplication.class);
    private HttpServiceClient client;



    /**
     * @see org.astrogrid.applications.Application#execute(org.astrogrid.applications.ApplicationExitMonitor)
     */
    public boolean execute() throws CeaException {
        //@TODO check this out ... how parameters work
        createAdapters();
        List args = new ArrayList();
        for (Iterator i = inputParameterAdapters(); i.hasNext();) {
            ParameterAdapter a = (ParameterAdapter) i.next();
            args.add(a.process());
        }

            client = new HttpServiceClient("http://127.0.0.1",
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
        Object resultVal = null;
        try {
            //  resultVal = method.invoke(null,args);
            Map args = new HashMap();
            resultVal = client.call(args);
            // we can do this, as we know there's only ever going to be one
            // interface, and one output parameter.
            setStatus(Status.WRITINGBACK);
            ParameterAdapter result = (ParameterAdapter) outputParameterAdapters().next();
            result.writeBack(resultVal);
            setStatus(Status.COMPLETED);
        } catch (CeaException e) {
            reportError("Failed to write back parameter values", e);
        } catch (Throwable t) {
            reportError("Something else gone wrong", t);
        }
    }

    /**
     * overridden to return a JavaClassParameterAdapter.
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
 * Revision 1.2  2004/07/27 17:49:57  jdt
 * merges from case3 branch
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