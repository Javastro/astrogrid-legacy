/* $Id: HttpApplication.java,v 1.11 2006/01/10 11:26:52 clq2 Exp $
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

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.AbstractApplication;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.Status;
import org.astrogrid.applications.beans.v1.Script;
import org.astrogrid.applications.beans.v1.SimpleParameter;
import org.astrogrid.applications.beans.v1.WebHttpApplicationSetup;
import org.astrogrid.applications.beans.v1.WebHttpCall;
import org.astrogrid.applications.beans.v1.types.HttpMethodType;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.http.HttpServiceClient.HttpServiceType;
import org.astrogrid.applications.http.exceptions.HttpApplicationNetworkException;
import org.astrogrid.applications.http.exceptions.HttpApplicationWebServiceURLException;
import org.astrogrid.applications.http.script.IdentityPreprocessor;
import org.astrogrid.applications.http.script.Preprocessor;
import org.astrogrid.applications.http.script.XSLTPreprocessor;
import org.astrogrid.applications.parameter.ParameterAdapter;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;
import org.astrogrid.registry.beans.v10.cea.CeaHttpApplicationType;
import org.astrogrid.workflow.beans.v1.Tool;

/**
 * An Application that calls an http service, such as a SIAP service. ?
 * Responsible for extracting the relevant info from the application description
 * and passing them to a HttpServiceClient that knows how to call the web
 * service. Handles the threading (so execute doesn't block) and status settings
 * during execution. ?
 * 
 * 
 * 
 * @author jdt
 */
public class HttpApplication extends AbstractApplication  {
    /**
     * Commons Logger for this class
     */
    private static final Log log = LogFactory.getLog(HttpApplication.class);

    /**
     * Ctor
     * 
     * @param id
     * @param tool
     * @param applicationInterface
     * @param protocolLibrary
     */
    public HttpApplication(IDs id, Tool tool, ApplicationInterface applicationInterface, ProtocolLibrary protocolLibrary) {
        super(id, tool, applicationInterface, protocolLibrary);
    }

    /**
     * Combines a the Tool and Application docs and transforms to a doc suitable
     * for calling the web service.  For this we use the pre-processing element in the CeaHttpApplictionType
     * doc, or a default if none is available.
     */
    private WebHttpCall createCallingDocument(final Tool tool, final CeaHttpApplicationType app)  {
        if (log.isTraceEnabled()) {
            log.trace("createCallingDocument(Tool tool = " + tool + ", CeaHttpApplicationType app = " + app
                    + ") - start");
        }

            final WebHttpApplicationSetup httpAppInfo = app.getCeaHttpAdapterSetup();
            final Script preprocessScript = httpAppInfo.getPreProcessScript();
            
            log.debug("preprocessScript="+preprocessScript);
            if (preprocessScript!=null) {
                log.debug("preprocessScript lang="+preprocessScript.getLang());
                log.debug("preprocessScript code="+preprocessScript.getCode());
            }
            
            Preprocessor preprocessor;
            if (preprocessScript==null) {
                log.debug("Preprocessing with identity preprocessor");
                preprocessor = new IdentityPreprocessor();
            } else {
                String code = preprocessScript.getCode();
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
            
        WebHttpCall returnWebHttpCall = preprocessor.process(tool, app);
        if (log.isTraceEnabled()) {
            log.trace("createCallingDocument(Tool, CeaHttpApplicationType) - end - return value = "
                            + returnWebHttpCall);
        }
            return returnWebHttpCall;
    }

    public Runnable createExecutionTask() throws CeaException {
        createAdapters();
        log.debug("createExecutionTask() - creating worker thread");
        Runnable task = new Exec();
        setStatus(Status.INITIALIZED);
        return task;
    }

public boolean execute() throws CeaException {
    if (log.isTraceEnabled()) {
        log.trace("execute() - start");
    }
    log.debug("execute() - starting thread");
    (new Thread(createExecutionTask())).start();
    if (log.isTraceEnabled()) {
            log.trace("execute() - end - return value = " + true);
    }        
    return true;
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
                final String name = a.getWrappedParameter().getName();
                final Object value = a.process();
                //Replace Parameters in the tool document
                //with their processed values
                //This might be a bad idea...
                //Why not just extract the values and send them to the HttpServiceClient?  I want to have an actual Tool xml
                //document with the correct parameter values that I can transform using the registry/user supplied xslt.
                a.getWrappedParameter().setValue((String) value);
                log.debug(name + "=" + value);
            }
            //Prepare calling document, and extract what we need for the http call
            final HttpApplicationDescription description = (HttpApplicationDescription) getApplicationDescription();
            final CeaHttpApplicationType app = description.getApplication();
            final WebHttpCall httpCall = createCallingDocument(getTool(),app);
            
            final String url = httpCall.getURL().getContent();
            final HttpMethodType requestedMethod = httpCall.getURL().getMethod();        
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
            final Enumeration enum = httpCall.enumerateSimpleParameter();
            Map inputArguments = new HashMap();
            while (enum.hasMoreElements()) {
                final SimpleParameter parameter = (SimpleParameter) enum.nextElement();
                assert parameter!=null;
                assert parameter.getName()!=null;
                assert parameter.getValue()!=null;
                inputArguments.put(parameter.getName(), parameter.getValue());
            }
                    
            setStatus(Status.RUNNING);
            log.info("calling url="+url);
            HttpServiceClient client = new HttpServiceClient(url, method);
            final String resultText = client.call(inputArguments);
            log.debug("run() - unprocessed result:  : resultText = " + resultText);
            
            final String processedResult = postProcess(resultText);
            
            // we can do this, as we know there's only ever going to be one output parameter.
            setStatus(Status.WRITINGBACK);
            Iterator outputParamsIt = outputParameterAdapters();
            ParameterAdapter result = (ParameterAdapter) outputParamsIt.next();
            assert !outputParamsIt.hasNext() : "Expect there to be only one output parameter for an HttpApplication";
            result.writeBack(processedResult);
            log.info("completed call successfully");
            setStatus(Status.COMPLETED);
        } catch (CeaException e) {
            log.error("run() - failed to write back param values", e);
            reportError("Failed to write back parameter values", e);
        } catch (HttpApplicationWebServiceURLException e) {
            log.error("run() - problem with service URL", e);
            reportError("Error 404 - Not Found from the application's URL", e);
        } catch (HttpApplicationNetworkException e) {
            log.error("run()", e);
            reportError("Network Error while contacting web server",e);
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
 * Revision 1.11  2006/01/10 11:26:52  clq2
 * rolling back to before gtr_1489
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