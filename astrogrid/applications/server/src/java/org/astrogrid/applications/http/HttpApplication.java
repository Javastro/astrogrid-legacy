/* $Id: HttpApplication.java,v 1.3 2011/09/02 21:55:55 pah Exp $
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
import org.astrogrid.applications.description.impl.HttpMethodType;
import org.astrogrid.applications.description.impl.Script;
import org.astrogrid.applications.description.impl.WebHttpApplicationSetup;
import org.astrogrid.applications.description.impl.WebHttpCall;
import org.astrogrid.applications.description.impl.WebHttpCall.SimpleParameter;
import org.astrogrid.applications.http.exceptions.HttpApplicationNetworkException;
import org.astrogrid.applications.http.exceptions.HttpApplicationWebServiceURLException;
import org.astrogrid.applications.http.exceptions.HttpParameterProcessingException;
import org.astrogrid.applications.http.script.IdentityPreprocessor;
import org.astrogrid.applications.http.script.Preprocessor;
import org.astrogrid.applications.http.script.XSLTPreprocessor;
import org.astrogrid.applications.parameter.InternalValue;
import org.astrogrid.applications.parameter.MutableInternalValue;
import org.astrogrid.applications.parameter.ParameterAdapter;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;
import org.astrogrid.applications.description.execution.Tool;
import org.astrogrid.applications.environment.ApplicationEnvironment;

/**
 * An Application that calls an http service, such as a SIAP service. ?
 * Responsible for extracting the relevant info from the application description
 * and passing them to a HttpServiceClient that knows how to call the web
 * service. Handles the threading (so execute doesn't block) and status settings
 * during execution. ?
 * 
 * @author jdt
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 9 Sep 2008
 */
public class HttpApplication extends AbstractApplication  {
    /**
     * Commons Logger for this class
     */
    private static final Log log = LogFactory.getLog(HttpApplication.class);
    private final WebHttpApplicationSetup appSetup;

    /**
     * Ctor
     * 
     * @param id
     * @param tool
     * @param applicationInterface
     * @param protocolLibrary
     * @param httpAppSetup 
     */
    public HttpApplication (String jobId, Tool tool, ApplicationInterface applicationInterface,  ApplicationEnvironment env,ProtocolLibrary lib, WebHttpApplicationSetup httpAppSetup) {
        super( tool, applicationInterface, env, lib);
        this.appSetup = httpAppSetup;
    }

    /**
     * Combines a the Tool and Application docs and transforms to a doc suitable
     * for calling the web service.  For this we use the pre-processing element in the CeaHttpApplictionType
     * doc, or a default if none is available.
     * @throws HttpParameterProcessingException 
     */
    private WebHttpCall createCallingDocument(final Tool tool, final WebHttpApplicationSetup httpAppInfo) throws HttpParameterProcessingException  {
        if (log.isTraceEnabled()) {
            log.trace("createCallingDocument(Tool tool = " + tool + ", CeaHttpApplicationDefinition app = " + httpAppInfo
                    + ") - start");
        }

            final Script preprocessScript = httpAppInfo.getPreProcessScript();
            
            log.debug("preprocessScript="+preprocessScript);
            if (preprocessScript!=null) {
                log.debug("preprocessScript lang="+preprocessScript.getLang());
                log.debug("preprocessScript code="+preprocessScript.getValue());
            }
            
            Preprocessor preprocessor;
            if (preprocessScript==null) {
                log.debug("Preprocessing with identity preprocessor");
                preprocessor = new IdentityPreprocessor();
            } else {
                String code = preprocessScript.getValue();
                assert code!=null;
                if (preprocessScript.getLang().equals(XSLTPreprocessor.xmlName)) {
                    log.debug("Preprocessing with XSLT preprocessor");
                    preprocessor = new XSLTPreprocessor(code);
                    throw new UnsupportedOperationException("preprocessing scripts have not been implemented yet");
                } else {
                    log.debug("Non-xslt preprocessor - cannot process");
                    throw new UnsupportedOperationException("Non-xslt scripts have not been implemented yet");
                }
            }
            
        WebHttpCall returnWebHttpCall = preprocessor.process(tool, appSetup);
        if (log.isTraceEnabled()) {
            log.trace("createCallingDocument(Tool, CeaHttpApplicationDefinition) - end - return value = "
                            + returnWebHttpCall);
        }
            return returnWebHttpCall;
    }

    @Override
    public Runnable createRunnable()  {
        log.debug("createExecutionTask() - creating worker thread");
        Runnable task = new Exec();
        return task;
    }

    private class Exec implements Runnable {
    
 
    /**
     * Where the action happens
     */
    public void run() {
        if (log.isTraceEnabled()) {
            log.trace("run() - start");
        }
        //
        //  The arguments must be processed before the tool document gets
        //  turned into a calling document.
        log.debug("Processing arguments");        
        try{
            for (Iterator i = inputParameterAdapters(); i.hasNext();) {
                ParameterAdapter a = (ParameterAdapter) i.next();
                final String name = a.getWrappedParameter().getId();
                final InternalValue value = a.getInternalValue();
                //Replace Parameters in the tool document
                //with their processed values
                //This might be a bad idea...
                //Why not just extract the values and send them to the HttpServiceClient?  I want to have an actual Tool xml
                //document with the correct parameter values that I can transform using the registry/user supplied xslt.
                //IMPL should not get the wrapped parameter and set its value directly - unless know what you are doing
                a.getWrappedParameter().setValue( value.asString());
                log.debug(name + "=" + value);
            }
            //Prepare calling document, and extract what we need for the http call
            final HttpApplicationDescription description = (HttpApplicationDescription) getApplicationDescription();
            final WebHttpCall httpCall = createCallingDocument(getTool(),appSetup);
            
            final String url = httpCall.getURL().getValue();
            final org.astrogrid.applications.description.impl.HttpMethodType requestedMethod = httpCall.getURL().getMethod();        
            HttpServiceClient.HttpServiceType method;
            if (HttpMethodType.GET.equals(requestedMethod)) {
                method = HttpServiceClient.HttpServiceType.GET;
                log.debug("run() - Using http-get");
            } else if (HttpMethodType.POST.equals(requestedMethod)) {
                method = HttpServiceClient.HttpServiceType.POST;
                log.debug("run() - using http-post");
            } else {  //@TODO refactor exceptions
                reportError("Unknown http method requested"); //this really shouldn't happen, given the constraints in the schema
                return;
            }            
            final List<SimpleParameter> en = httpCall.getSimpleParameter();
            Map inputArguments = new HashMap();
            
 		
             for (SimpleParameter parameter : en) {
               assert parameter!=null;
                assert parameter.getName()!=null;
                assert parameter.getValue()!=null;
                inputArguments.put(parameter.getName(), parameter.getValue());
            }
                    
            // Call the target service and store the returned data in a buffer. 
            setStatus(Status.RUNNING);
            log.info("Calling " + url);
            HttpServiceClient client = new HttpServiceClient(url, method);
            final Object resultData = client.call(inputArguments);
            log.info("Call to " + url + " has completed."); 
            
            // we can do this, as we know there's only ever going to be one output parameter.
            setStatus(Status.WRITINGBACK);
            Iterator outputParamsIt = outputParameterAdapters();
            ParameterAdapter result = (ParameterAdapter) outputParamsIt.next();
            assert !outputParamsIt.hasNext() : "Expect there to be only one output parameter for an HttpApplication";
            //IMPL ugly ugly
            if (resultData instanceof String) {
                String sresultData = (String) resultData;
                result.getInternalValue().setValue(sresultData);
            }
            else if (resultData instanceof byte[]){
            result.getInternalValue().setValue((byte[])resultData);
            }
            result.writeBack();
            log.info("completed call successfully");
            setStatus(Status.COMPLETED);
        } catch (HttpApplicationWebServiceURLException e) {
            log.error("run() - problem with service URL", e);
            reportError("Error 404 - Not Found from the application's URL", e);
        } catch (HttpApplicationNetworkException e) {
            log.error("run()", e);
            reportError("Network Error while contacting web server",e);
        } catch (CeaException e) {
            log.error("run() - failed to write back param values", e);
            reportError("Failed to write back parameter values", e);
        } catch (Throwable t) {
            log.error("run()", t);

            reportError("Something else gone wrong", t);
        }

        if (log.isTraceEnabled()) {
            log.trace("run() - end");
        }
    }
    }

    /**
     * @param result
     * @return
     * @TODO this method is incomplete
     */
    private String postProcess(final String result) {
        if (log.isTraceEnabled()) {
            log.trace("postProcess(String result = " + result + ") - start");
        }

        log.debug("No post-processing at present");

        if (log.isTraceEnabled()) {
            log.trace("postProcess(String) - end - return value = " + result);
        }
        return result;
    }

}

/*
 * $Log: HttpApplication.java,v $
 * Revision 1.3  2011/09/02 21:55:55  pah
 * result of merging the 2931 branch
 *
 * Revision 1.2.6.2  2009/07/16 19:48:06  pah
 * ASSIGNED - bug 2950: rework parameterAdapter
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2950
 *
 * Revision 1.2.6.1  2009/07/15 09:48:44  pah
 * redesign of parameterAdapters
 *
 * Revision 1.2  2008/09/13 09:51:06  pah
 * code cleanup
 *
 * Revision 1.1  2008/09/10 23:27:17  pah
 * moved all of http CEC and most of javaclass CEC code here into common library
 *
 * Revision 1.16  2008/09/03 14:19:02  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.15.10.3  2008/09/03 12:00:48  pah
 * still not really working
 *
 * Revision 1.15.10.2  2008/08/02 13:32:32  pah
 * safety checkin - on vacation
 *
 * Revision 1.15.10.1  2008/04/01 13:50:07  pah
 * http service also passes unit tests with new jaxb metadata config
 *
 * Revision 1.15  2007/02/19 16:19:26  gtr
 * Branch apps-gtr-1061 is merged.
 *
 * Revision 1.14.32.1  2007/01/17 18:10:35  gtr
 * The deprecated method Application.execute() has been removed.
 *
 * Revision 1.14  2006/03/17 17:50:58  clq2
 * gtr_1489_cea correted version
 *
 * Revision 1.12  2006/03/07 21:45:26  clq2
 * gtr_1489_cea
 *
 * Revision 1.9.34.3  2006/01/30 19:16:15  gtr
 * I adjusted the HTTP code to return the response body either as a String or as byte[] depending on whether the response appears to be binary.
 *
 * Revision 1.9.34.2  2006/01/26 13:17:31  gtr
 * *** empty log message ***
 *
 * Revision 1.9.34.1  2005/12/22 11:46:14  gtr
 * I eliminated enum as a variable name so that the code can be compiled with Java 5.
 *
 * Revision 1.9  2005/07/05 08:27:01  clq2
 * paul's 559b and 559c for wo/apps and jes
 *
 * Revision 1.8.80.1  2005/06/09 08:47:32  pah
 * result of merging branch cea_pah_559b into HEAD
 *
 * Revision 1.8.66.1  2005/05/31 12:51:43  pah
 * moved to v10 schema interpretation - this means that the authorityID is read directly with the applicaion "name"
 *
 * Revision 1.8  2004/10/21 10:05:12  pah
 * log the target url
 *
 * Revision 1.7  2004/09/26 23:29:38  jdt
 * Put the processing of the parameters back before the creation of the calling document.  The calling doc is
 * created using whatever values are in the Tool doc at the time, so the parameter processing must be done first.
 * Perhaps needs some rejigging if it's misleading.
 *
 * Revision 1.6  2004/09/17 01:23:01  nw
 * altered to make use of threadpooling
 *
 * Revision 1.5.18.1  2004/09/14 15:16:47  nw
 * adjusted to new threading model.
 *
 * Revision 1.5  2004/09/01 15:42:26  jdt
 * Merged in Case 3
 *
 * Revision 1.1.4.15  2004/08/30 14:54:42  jdt
 * tidied some imports
 *
 * Revision 1.1.4.14  2004/08/18 11:41:32  jdt
 * added some logging
 *
 * Revision 1.1.4.13  2004/08/15 10:57:42  jdt
 * removed extraneous stuff
 *
 * Revision 1.1.4.12  2004/08/12 13:17:11  jdt
 * factored some stuff back into its own thread
 *
 * Revision 1.1.4.11  2004/08/12 12:15:09  jdt
 * you foolish boy.
 *
 * Revision 1.1.4.10  2004/08/11 22:55:35  jdt
 * Refactoring, and a lot of new unit tests.
 *
 * Revision 1.1.4.9  2004/08/10 13:44:23  jdt
 * Following new workflow-object types...and a safety checkin.
 *
 * Revision 1.1.4.8  2004/08/09 16:37:13  jdt
 * Brought into line following pah's suggested schema changes
 *
 * Revision 1.1.4.7  2004/08/06 13:30:22  jdt
 * safety checkin
 * Revision 1.1.4.6 2004/07/30 16:59:50 jdt
 * limping along.
 * 
 * Revision 1.1.4.5 2004/07/30 13:11:28 jdt wired up the parameters
 * 
 * Revision 1.1.4.4 2004/07/29 21:30:47 jdt *** empty log message ***
 * 
 * Revision 1.1.4.3 2004/07/29 17:08:22 jdt Think about how I'm going to get
 * stuff out of the registry
 * 
 * Revision 1.1.4.2 2004/07/27 19:28:43 jdt fix build errors
 * 
 * Revision 1.1.4.1 2004/07/27 17:20:25 jdt merged from applications_JDT_case3
 * 
 * Revision 1.1.2.2 2004/07/24 18:43:29 jdt Started plumbing in the httpclient
 * Revision 1.1.2.1 2004/07/24 17:16:16 jdt Borrowed from javaclass
 * application....stealing is always quicker.
 *  
 */