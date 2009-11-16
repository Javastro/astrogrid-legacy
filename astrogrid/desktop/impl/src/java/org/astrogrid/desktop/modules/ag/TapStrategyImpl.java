/**
 * 
 */
package org.astrogrid.desktop.modules.ag;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.Principal;
import java.util.Date;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.ExecutionInformation;
import org.astrogrid.acr.astrogrid.ExecutionMessage;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.TapService;
import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.desktop.framework.SessionManagerInternal;
import org.astrogrid.desktop.modules.system.SchedulerInternal;
import org.astrogrid.desktop.modules.system.SchedulerInternal.DelayedContinuation;
import org.astrogrid.desktop.modules.ui.comp.ExceptionFormatter;
import org.astrogrid.workflow.beans.v1.Tool;
import org.w3c.dom.Document;

/**
 * A remote-process stategy for TAP 1.0-20090607 (WD).
 * This drives only the ADQL/asynchronous aspect of the protocol.
 *
 * @todo add hooks to delete a job too - find out from guy how long they live for.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Oct 22, 200812:30:12 PM
 */
public class TapStrategyImpl extends AbstractToolBasedStrategy implements RemoteProcessStrategy {

    private final FileSystemManager vfs;
    private final SchedulerInternal sched;  
    private final SessionManagerInternal sess;
    private final HttpClient http;

    /**
     * @param reg
     */
    public TapStrategyImpl(final Registry reg, final FileSystemManager vfs, final SchedulerInternal sched
            ,final SessionManagerInternal sess, final HttpClient http) {
        super(reg);
        this.vfs = vfs;
        this.sched = sched;
        this.sess = sess;
        this.http = http;
    }

    
    public boolean canProcess(final URI execId) {
        
        return canProcessSupport(execId,TapService.class);
    }

    public String canProcess(final Document doc) {
        return canProcessSupport(doc,TapService.class);
      
    }

    public ProcessMonitor create(final Document doc) throws InvalidArgumentException,
            ServiceException {

        final Tool tool = AbstractToolBasedStrategy.parseTool(doc);
        TapService service;
        try {
            final URI id = AbstractToolBasedStrategy.getResourceId(tool);
            final Resource res = reg.getResource(id);
            if (res instanceof TapService) {
                service = (TapService)res;
            } else {
                throw new InvalidArgumentException(id +" : is not a TapService");
            }
        } catch (final NotFoundException x1) {
            throw new InvalidArgumentException(x1);                
        } 

        return new TapTaskMonitor(tool,service);
    }
    
    private class TapTaskMonitor extends TimerDrivenProcessMonitor implements ProcessMonitor.Advanced {

        private final TapService service;
        private final Tool tool;
        private URL jobID;
        private String destinationURL;
        /** create a new tap task monitor
         * 
         * @param query adql query
         * @param format format to resturn results
         * @param dest destiation to pass results to (may be null)         
         * @param service service to query.
         */
        public TapTaskMonitor(final Tool tool,final TapService service) {
            super(vfs);
            this.tool = tool;
            this.service = service;
            this.name = service.getTitle();
            this.description = service.getContent().getDescription();
        }

        
        /** the method is called halt, but it's called by the 
         * UI 'delete' button (as well as the AR api halt() method
         * so, considering that abort doesn't seem to work, 
         * should I implmenet delete instead? unsure yet.
         */
        public void halt() throws NotFoundException, InvalidArgumentException,
                ServiceException, SecurityException {
            info("Halting");
            postPhaseCommand("ABORT");
            // implement delete here instead.?
        }

        /** not perfect - refreshes immediately, but inteacts badly with the shceduled task.
         * would like to reset the scheduled task time, so that it runs immediately.
         * but can't do that.
         * instead, we run the refresh, and then the next time the scheduled task runs, the refresh-period
         * will be set to SHORTEST - so that it runs soon the time after that.
         * 
         * however, this means that there might still be large pause between a refresh and the next 
         * scheduled operation.
         */
        public void refresh() {            
            runAgain = SHORTEST; // 
            execute(false);
        }
        public void start(final URI serviceId) throws ServiceException,
                NotFoundException {
            start();
        }

        public void start() throws ServiceException, NotFoundException {
            info("Initializing query");
            //future - might nee to make myspace ivorns concrete before we go any further.

            // Find the URI for the UWS. It is a child of the resource for which the URI
            // is given in the registration.
            final URI tap = 
                service.findTapCapability().getInterfaces()[0].getAccessUrls()[0].getValueURI();
            final URI endpoint = resolveUri(tap, "async");
            info("Endpoint: " + endpoint);
            
            final PostMethod createMethod = new PostMethod(endpoint.toString());
            try {

                // extract the parameters we're interested with.
                final String query = (String)tool.findXPathValue("input/parameter[name='ADQL']/value");
                String format = (String)tool.findXPathValue("input/parameter[name='FORMAT']/value");
                final ParameterValue dest = (ParameterValue)tool.findXPathValue("output/parameter[name='DEST']");
                if (format == null) {
                    format = "application/x-votable+xml";
                }
                createMethod.addParameters(new NameValuePair[] {
                        new NameValuePair("QUERY", query),
                        new NameValuePair("LANG", "ADQL"),
                        new NameValuePair("FORMAT", format) // this parameter is ignored by current dsa.                      
                });
                
                // The data destination is specified by the caller;
                // the data end up outside this service.
                if (dest != null && dest.getIndirect()) {
                    // store the original, unmangled destination
                    destinationURL = dest.getValue();
                    final Tool mangled = tool; // try without mangling.makeMySpaceIvornsConcrete(tool);
                    final ParameterValue mangledDest = (ParameterValue)mangled.findXPathValue("output/parameter[name='DEST']");

                    createMethod.addParameter(new NameValuePair("DEST",mangledDest.getValue()));
                    createMethod.addParameter(new NameValuePair("TargetURI",mangledDest.getValue()));
                }
                // The data destination is not specified in the request. The
                // data will fetch up inside the TAP service. We'll the URL
                // for the data whene we get the job ID.
                else {
                    destinationURL = null;
                }
                final int code = http.executeMethod(createMethod); 
                checkCode(code,createMethod);

                // Our "job ID" is the URI for the job object in the TAP service.
                // It's an absolute URI in the HTTP scheme.
                final Header location = createMethod.getResponseHeader("Location");
                jobID =new URL(location.getValue());
                info("JobID: " + jobID);
                setId(mkGlobalExecId(jobID.toString(),service));
                
                
                // Now we have the job ID, we can predict where the data are
                // by applying the rules of the TAP standard.
                if (destinationURL == null) {
                    destinationURL = String.format("%s/results/result", jobID);
                    LogFactory.getLog(TapStrategyImpl.class).info("Results at: " + destinationURL);
                }

                // ok. kick it off.
                postPhaseCommand("RUN");
                info("Started query");  
                sched.schedule(this); // register this monitor with the scheduler.


            } catch (final IOException x) {
                error("Failed to execute query",x);
                throw new ServiceException(x.getMessage());            
     //       } catch (final InvalidArgumentException x) {
     //           error("Failed to execute query",x);
     //           throw new ServiceException(x.getMessage());                   
            } finally {
                createMethod.releaseConnection();
            }

        }
        
        /** post a request to change to a particular phase */
        private final void postPhaseCommand(final String phaseCommand) throws ServiceException{
            info("Requesting phase change to " + phaseCommand);
            PostMethod phaseMethod = null;
            try {
                final URL phaseURL = mkSubURL(jobID,"phase");
                phaseMethod = new PostMethod(phaseURL.toString());
                phaseMethod.setRequestBody(new NameValuePair[] {
                        new NameValuePair("PHASE",phaseCommand)
                });        
                final int code = http.executeMethod(phaseMethod);
                checkCode(code,phaseMethod);
            } catch (final IOException x) {
               error("Failed to change phase to "+ phaseCommand,x);
               throw new ServiceException(x.getMessage());
            } finally {
                if (phaseMethod != null) {
                    phaseMethod.releaseConnection();
                }
            }
        }
        
        /** get the current phase */
        private final String getPhase() throws ServiceException{
            info("Checking progress");
            GetMethod phaseMethod = null;
            XMLStreamReader in = null;
            try {
                final URL phaseURL = mkSubURL(jobID,"phase");
                phaseMethod = new GetMethod(phaseURL.toString());       
                final int code = http.executeMethod(phaseMethod);
                checkCode(HttpStatus.SC_OK,code,phaseMethod);
                /* This code applies when XML is returned, but UWS returns text/plain now.
                final XMLInputFactory fac = XMLInputFactory.newInstance();
                in = fac.createXMLStreamReader(phaseMethod.getResponseBodyAsStream());
                while (in.hasNext()){
                    in.next();
                    if (in.isStartElement() && "phase".equalsIgnoreCase(in.getLocalName())) {
                        // handles case when a fuller error message is returned. 
                        return StringUtils.substringBefore(StringUtils.trim(in.getElementText()),":");
                    }
                }
                 */
                return phaseMethod.getResponseBodyAsString();
                /*error("Unable to parse phase information");
                throw new ServiceException("Unable to parse phase information");*/         
            } catch (final IOException x) {
                error("Failed to check progress",x);
                throw new ServiceException(x.getMessage());
            } /*catch (final XMLStreamException x) {
                error("Failed to check progress",x);
                throw new ServiceException(x.getMessage());            
            } */
            finally {
                if (phaseMethod != null) {
                    phaseMethod.releaseConnection();
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (final XMLStreamException x) {
                        //meh
                    }
                }
            }
        }
        
        /** retrieve the erorr message */
        private final String getError() throws ServiceException{
            info("Retrieving error details");
            GetMethod errorMethod = null;
            try {
                final URL errorURL = mkSubURL(jobID,"error");
                errorMethod = new GetMethod(errorURL.toString());       
                final int code = http.executeMethod(errorMethod);
                checkCode(HttpStatus.SC_OK,code,errorMethod);
                final String body =  errorMethod.getResponseBodyAsString();                                
                // remove any html / xml formatting. - dirty.
                return body.replaceAll("\\<.*?\\>", "");
            } catch (final IOException x) {
               error("Failed to check progress",x);
               throw new ServiceException(x.getMessage());                       
            } finally {
                if (errorMethod != null) {
                    errorMethod.releaseConnection();
                }
            }
        }
        
        /** check the actual code is the same as the expected response code, and if not log, and then throw */
        private void checkCode(final int expected, final int code, final HttpMethod m) throws ServiceException {
            if (expected != code) {
              if (!(isRedirect(expected) && isRedirect(code))) {
                try { // try to report the response body.
                    final String s = m.getResponseBodyAsString();
                    error("Unexpected response code " + code + "<br>" + s);
                } catch (final IOException e) {
                    // never mind.
                    error("Unexpected response code " + code);
                }
                throw new ServiceException("Unexpected response code " + code);
              }
            }
        }
        
        /** verify that code is a SC_SEE_OTHER, otherwise will
         * try to get the response message, log it, and then bail out with exception
         * @param code
         * @param m
         * @throws ServiceException
         */
        private void checkCode(final int code, final HttpMethod m) throws ServiceException {
            checkCode(HttpStatus.SC_SEE_OTHER,code,m);
        }

     /**
      * Determines whether an HHTP status-code indicates a redirect response that
      * the strategy can follow. Some of the redirection codes are not appropriate
      * to TAP and are not acknowledged here as redirections.
      *
      * @param code The code to be inspected.
      * @return True if the code is 301, 302, 303 or 307; false otherwise.
      */
     private boolean isRedirect(int code) {
       return (code == 301 || code == 302 || code == 303);
     }
        

        /** create a sub url, respecting existing path of root, and taking care of trailing /, etc 
         * @throws MalformedURLException */
        private URL mkSubURL(final URL root, final String sub) throws MalformedURLException {
            final String path = StringUtils.stripEnd(root.toString(),"/");
            final String particle = StringUtils.stripStart(sub,"/");
            return new URL(path + "/" + particle);
        }        
        
        @Override
        protected DelayedContinuation execute(final boolean increaseStandoff) {
            if (getStatus().equals("ERROR") 
                    ||getStatus().equals("COMPLETED")
                    || getStatus().equals("ABORTED")) {
                // we're done already (probably by an intermediate refresh..)
                return null; // halts the progress checking.
            }
            info("Checking progress");
            try {
                final String nuStatus = getPhase();
                // assume I can use the set of codes returned direct from Tap, without having to translate into 
                // execution phase (mostly very similar anyhow)
                if (getStatus().equals(nuStatus)) { // nothing changed;
                    standOff(increaseStandoff);
                    return this;
                }
                
                // something interesting has happened.
                runAgain = SHORTEST;

                if (getStatus() == ExecutionInformation.UNKNOWN) {// brand new
                    // use the messae time as our execution start - a bit of a fudge.
                    startTime = new Date();
                }

                // ok, send a status-change message
                final ExecutionMessage em = new ExecutionMessage(
                        getId().toString()
                        ,"information"
                        ,nuStatus
                        ,new Date()
                        , nuStatus
                );
                addMessage(em);
                setStatus(nuStatus);

                // tackle different kinds of status code.
                if ("ERROR".equals(nuStatus)) {
                    finishTime = new Date();
                    final String errorBody = getError();
                    final ExecutionMessage erm = new ExecutionMessage(
                            getId().toString()
                            ,"error"
                            ,nuStatus
                            , new Date()
                            ,errorBody
                    );
                    addMessage(erm);
                    return null;
                } else if ("COMPLETED".equals(nuStatus)) {
                    finishTime = new Date();
                    try {
                        if (destinationURL == null) { // user didn't specify a destination
                            destinationURL = mkSubURL(jobID,"results/result").toString();
                            final FileObject src = vfs.resolveFile(destinationURL);                         
                            sys.addJunction("query-result.vot",src);
                            resultMap.put("query-result",destinationURL);
                        } else { // saved elsewhere - probably myspace.
                            final FileObject src = vfs.resolveFile(destinationURL);
                            src.refresh();
                            if (! src.exists()) {
                                System.err.println("Does not exist" + destinationURL);
                            }
                            addResult("query-result",src);
                        }
                    } catch (final FileSystemException e) {
                        e.printStackTrace();
                        warn("Failed to retreive result<br>" + exFormatter.format(e,ExceptionFormatter.ALL));   
                    } catch (final MalformedURLException x) {
                        warn("Failed to produce result url"); // unlikely.
                    }

                    fireResultsReceived(resultMap);
                    return null;
                } else if ("ABORTED".equals(nuStatus)) {
                    setStatus("COMPLETED"); // dunno if this status is used elsewhere in the system
                    // if it is, better set it to a known value.
                    return null;
                } else {
                    return this; // continue to monitor
                }

            } catch (final ServiceException x) {
                standOff(increaseStandoff);
                warn("Failed: " + exFormatter.format(x,ExceptionFormatter.ALL));
                return this;
            }

        }
        
        /** convert a tap phase to a cea execution phase
         * , the string representation of which is used within the process monitor framework (I think).
         * @param phase
         * @return
         */
        private ExecutionPhase convertTapPhaseToExecutionPhase(final String phase) {
            if ("PENDING".equals(phase)) {
                return ExecutionPhase.PENDING;
            } else if ("QUEUED".equals(phase)) {
                return ExecutionPhase.PENDING;                
            } else if ("EXECUTING".equals(phase)) {
                return ExecutionPhase.RUNNING;
            } else if ("COMPLETED".equals(phase)) {
                return ExecutionPhase.COMPLETED;
            } else if ("ERROR".equals(phase)) {
                return ExecutionPhase.ERROR;
            } else if ("UNKNOWN".equals(phase)) {
                return ExecutionPhase.UNKNOWN;                
            } else if ("HELD".equals(phase)) {
                return ExecutionPhase.PENDING;                
            } else if ("SUSPENDED".equals(phase)) {
                return ExecutionPhase.PENDING;                
            } else if ("ABORTED".equals(phase)) {
                return ExecutionPhase.COMPLETED;
            } else {                
                return ExecutionPhase.UNKNOWN;
            }
        }


        public Principal getPrincipal() {
            return sess.currentSession();
        }

        public String getTitle() {
            return "Tracking query against " + service.getTitle();
        }

        public Tool getInvocationTool() {
            return tool;
        }
    }
    
    /**
     * Resolves a relative URI against an absolute URI, returning
     * a new, absolute URI. This is a workaround for the bug in
     * java.net.URI.resolve(URI,String).
     *
     * @param base The absolute URI against which to resolve.
     * @param relative The relative URI to be resolved.
     * @return The resolved, absolute URI.
     */
    private URI resolveUri(URI base, String relative) {
        try {
            return new URI(base.toString() + "/" + relative);
        }
        catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
