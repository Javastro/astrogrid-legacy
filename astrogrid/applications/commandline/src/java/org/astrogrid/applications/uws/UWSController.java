/*
 * $Id: UWSController.java,v 1.3 2008/09/18 08:46:45 pah Exp $
 * 
 * Created on 8 Apr 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.uws;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.ivoa.uws.ExecutionPhase;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.Application;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.component.CEAComponents;
import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.ParameterDescription;
import org.astrogrid.applications.description.base.ParameterTypes;
import org.astrogrid.applications.description.exception.ApplicationDescriptionNotFoundException;
import org.astrogrid.applications.description.exception.ParameterDescriptionNotFoundException;
import org.astrogrid.applications.description.execution.ExecutionSummaryType;
import org.astrogrid.applications.description.execution.ParameterValue;
import org.astrogrid.applications.description.execution.ResultListType;
import org.astrogrid.applications.manager.QueryService;
import org.astrogrid.applications.manager.ApplicationEnvironmentRetriver.StdIOType;
import org.astrogrid.applications.manager.persist.ExecutionHistory;
import org.astrogrid.applications.manager.persist.ExecutionIDNotFoundException;
import org.astrogrid.applications.manager.persist.PersistenceException;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UrlPathHelper;

/**
 * Controller to deal with the UWS operations on a particular job.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 9 Apr 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 * @TODO when returning just the xml version of the various enties
 * @TODO there is an issue with requesting a resource with or without the trailing / makes sense in some cases not in others - need to find some consistency...views need to cooperate as well otherwise relative links do not work properly...
 */
/**
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 16 Sep 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
/**
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 16 Sep 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
@Controller
@RequestMapping("/jobs/**")
public class UWSController  {

    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(UWSController.class);

    private final CEAComponents manager;

    @Autowired
    public UWSController(CEAComponents manager){
	this.manager = manager;
    }


    static final String jobRegexp = "/jobs/+([^/\\?#]+)[/\\?#]?";
    static final String resultRegexp = "/jobs/+([^/]+)/+results/+([^/]+)/*$";
    static final Pattern jobPattern = Pattern.compile(jobRegexp);
    static final Pattern resultPattern = Pattern.compile(resultRegexp);

    /*
       /jobs/cea-Macintosh.local-192.168.61.1-1210257725724-572445265/
     */
    /**
     * Sets the jobID from the context path. In spring MVC this method will be executed before the others.
     * @param req
     * @return
     * @throws ExecutionIDNotFoundException 
     */
    @ModelAttribute("JobId")
    public String discoverJobID(HttpServletRequest req) throws ExecutionIDNotFoundException
    {
	String reqURL = new UrlPathHelper().getPathWithinServletMapping(req);
	Matcher matcher = jobPattern.matcher(reqURL);
	if(matcher.find())
	{
	    String retval = matcher.group(1);
	    logger.debug("UWSController jobid="+retval);
	    return retval;
	}
	else    
	{
	    throw new ExecutionIDNotFoundException("there is no job id specified");
	}

    }


    @RequestMapping(value="phase", method=RequestMethod.GET)
    public ModelAndView viewphase( @ModelAttribute("JobId") String jobId, HttpServletRequest request, HttpServletResponse response) throws IOException, CeaException
    {
	ModelAndView mav = new ModelAndView("phase-xml");
	mav.addObject("phase",manager.getQueryService().queryExecutionStatus(jobId).getPhase());
	return mav;
    }

    @RequestMapping(value="phase", method=RequestMethod.POST)
    public void setphase( @ModelAttribute("JobId") String jobId, HttpServletRequest request, HttpServletResponse response) throws CeaException, IOException
    {
	String newPhaseStr = request.getParameter("phase");
	changeApplicationPhase(jobId, request, response, newPhaseStr);
	UWSUtils.redirect(request, response, jobId);
    }
    @RequestMapping(value="quote", method=RequestMethod.POST)
    public void acceptQuote( @ModelAttribute("JobId") String jobId, HttpServletRequest request, HttpServletResponse response) throws CeaException, IOException
    {
	String newPhaseStr = "run";
	changeApplicationPhase(jobId, request, response, newPhaseStr);

    }


    private void changeApplicationPhase(String jobId,
	    HttpServletRequest request, HttpServletResponse response,
	    String newPhaseStr) throws CeaException, IOException {
	ExecutionPhase currentPhase = manager.getQueryService().queryExecutionStatus(jobId).getPhase();

	if(newPhaseStr != null && newPhaseStr.equalsIgnoreCase("run")) {

	    if(currentPhase.equals(ExecutionPhase.PENDING))
	    {
		manager.getExecutionController().execute(jobId);
		//wait a little for the phase to change
		while (manager.getQueryService().queryExecutionStatus(jobId).getPhase().equals(ExecutionPhase.PENDING)) {
		    try {
			Thread.sleep(300);
			logger.trace("waiting for phase to change for "+jobId);
		    } catch (InterruptedException e) {
			// just continue
		    }
		}
		UWSUtils.redirect(request, response, jobId);
		return;
	    }
	    else
	    {
		response.sendError(HttpServletResponse.SC_CONFLICT,"cannot attempt to run job in phase="+currentPhase);
		return;
	    }
	} else if(newPhaseStr != null && newPhaseStr.equalsIgnoreCase("abort")){

	    switch (currentPhase) {
	    case PENDING:
	    case EXECUTING:
	    case HELD:
	    case QUEUED:
	    case SUSPENDED:
		manager.getExecutionController().abort(jobId);	
		UWSUtils.redirect(request, response, jobId);
		return;

	    default:
		response.sendError(HttpServletResponse.SC_CONFLICT,"cannot attempt to abort job in phase="+currentPhase);
	    return;
	    }

	}else {

	    response.sendError(HttpServletResponse.SC_CONFLICT,"cannot manually change job to phase="+newPhaseStr);
	    return;

	}


    }

    private ModelAndView sendError(String string) {
	ModelAndView modelAndView = new ModelAndView("general-error");
	modelAndView.addObject("errorMessage", string);
	return modelAndView;
    }

    @RequestMapping(value="termination", method=RequestMethod.GET)
    public ModelAndView termination(@ModelAttribute("JobId") String jobId, HttpServletRequest request, HttpServletResponse response) throws IOException, CeaException
    {
	ModelAndView mav = new ModelAndView("termination-xml");
	mav.addObject("termination",manager.getQueryService().getSummary(jobId).getTermination());
	return mav;
    }
    @RequestMapping(value="termination", method=RequestMethod.POST)
    public void settermination(@ModelAttribute("JobId") String jobId, HttpServletRequest request, HttpServletResponse response) throws IOException, CeaException
    {
	String dateStr = request.getParameter("TIME");
	DateTime termination = new DateTime(dateStr);
	DateTime now = new DateTime();

	if(manager.getExecutionHistoryService().isApplicationInCurrentSet(jobId))
	{
	    Application app = manager.getExecutionHistoryService().getApplicationFromCurrentSet(jobId);
	    Period diff = new Period(now,termination);
	    app.setRunTimeLimit(diff.getMillis());
	}
	UWSUtils.redirect(request, response, jobId);
	return;

    }
    @RequestMapping(value="destruction", method=RequestMethod.GET)
    public ModelAndView destruction(@ModelAttribute("JobId") String jobId, HttpServletRequest request, HttpServletResponse response) throws IOException, CeaException
    {
	ModelAndView mav = new ModelAndView("destruction-xml");
	mav.addObject("destruction",manager.getQueryService().getSummary(jobId).getDestruction());
	return mav;
    }
    @RequestMapping(value="destruction", method=RequestMethod.POST)
    public void setdestruction(@ModelAttribute("JobId") String jobId, HttpServletRequest request, HttpServletResponse response) throws IOException, CeaException
    {
	String dateStr = request.getParameter("TIME");
	DateTime termination = new DateTime(dateStr);

	if(manager.getExecutionHistoryService().isApplicationInCurrentSet(jobId))
	{
	    Application app = manager.getExecutionHistoryService().getApplicationFromCurrentSet(jobId);
	    app.setDestruction(termination.toDate());
	}
	else {
	    manager.getExecutionHistoryService().setDestructionTime(jobId, termination.toDate());
	}
	UWSUtils.redirect(request, response, jobId);
	return;

    }

    @RequestMapping(value="quote", method=RequestMethod.GET)
    public ModelAndView quote(@ModelAttribute("JobId") String jobId, HttpServletRequest request, HttpServletResponse response) throws IOException, CeaException
    {
	ModelAndView mav = new ModelAndView("quote-xml");
	mav.addObject("quote",manager.getQueryService().getSummary(jobId).getQuote());
	return mav;

    }

    @RequestMapping(value="results",method=RequestMethod.GET)
    public ModelAndView results(@ModelAttribute("JobId") String jobId, HttpServletRequest request, HttpServletResponse response) throws ExecutionIDNotFoundException, PersistenceException, ApplicationDescriptionNotFoundException, ParameterDescriptionNotFoundException
    {
	ModelAndView modelAndView = new ModelAndView("results");
	ExecutionHistory eh  = manager.getExecutionHistoryService();
	List<ResultInfo> results = new ArrayList<ResultInfo>();
	if (eh.isApplicationInCurrentSet(jobId))
	{
	    //TODO - get results as they appear...
	}else
	{
	    ExecutionSummaryType summary = eh.getApplicationFromArchive(jobId);
	    ApplicationDescription appdesc =  manager.getApplicationDescriptionLibrary().getDescription(summary.getApplicationName());
	    List<ParameterValue> resultList = summary.getResultList().getResult();
	    for (ParameterValue parameterValue : resultList) {
		ResultInfo rinfo = new ResultInfo(parameterValue);
		ParameterDescription parameterDescription = appdesc.getParameterDescription(parameterValue.getId());
		rinfo.setName(parameterDescription.getName());
		rinfo.setDescription(parameterDescription.getDescription());
		rinfo.setKind(parameterDescription.getType());
		rinfo.setMime(determinMime(parameterValue, parameterDescription));
		results.add(rinfo);
	    }
	    modelAndView.addObject("results", results);
	}
	return modelAndView;
    }

    /**
     * Result information.
     * @author Paul Harrison (paul.harrison@manchester.ac.uk) 16 Sep 2008
     * @version $Name:  $
     * @since VOTech Stage 7
     */
    public static class ResultInfo extends ParameterValue {
	public ResultInfo(ParameterValue v1) {
	    try {
		BeanUtils.copyProperties(this, v1);
	    } catch (IllegalAccessException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    } catch (InvocationTargetException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
	private ParameterTypes kind;
	private String name;
	private String description;
	public ParameterTypes getKind() {
	    return kind;
	}
	public void setKind(ParameterTypes type) {
	    this.kind = type;
	}
	public String getName() {
	    return name;
	}
	public void setName(String name) {
	    this.name = name;
	}
	public String getDescription() {
	    return description;
	}
	public void setDescription(String description) {
	    this.description = description;
	}
    }

    @RequestMapping(value="",method=RequestMethod.GET ) //really want a mapping that means just use the top level one...
    public ModelAndView showJob(@ModelAttribute("JobId") String jobId, HttpServletRequest request, HttpServletResponse response) throws CeaException
    {
	logger.info("showing JobId=" + jobId);
	//FIXME Need to check the path to only respond to exact matches...
	ModelAndView modelAndView = new ModelAndView("jobdetail");
	ExecutionHistory eh = manager.getExecutionHistoryService();
	QueryService qs = manager.getQueryService();
	ExecutionSummaryType jobSummary = qs.getSummary(jobId);
	modelAndView.addObject("theJob", jobSummary);
	return modelAndView;
    }

    @RequestMapping(value="",method=RequestMethod.POST ) 
    public void delJobByPOST(@ModelAttribute("JobId") String jobId, HttpServletRequest request, HttpServletResponse response) throws CeaException, IOException
    {
	if(request.getParameter("ACTION") != null && request.getParameter("ACTION").equalsIgnoreCase("delete"))
	{
	    deleteJob(jobId);
	    UWSUtils.redirect(request, response, null);
	}
	else
	{
	    UWSUtils.redirect(request, response, jobId);
	}

    }
    @RequestMapping(value="",method=RequestMethod.DELETE ) 
    public void delJobByDEL(@ModelAttribute("JobId") String jobId, HttpServletRequest request, HttpServletResponse response) throws CeaException, IOException
    {

	deleteJob(jobId);
	UWSUtils.redirect(request, response, null);

    }


    private String redirectToCurrent(HttpServletRequest request)
    {
	String uri = request.getRequestURI();
	return uri;
    }

    private void deleteJob(String jobId) throws ExecutionIDNotFoundException {
	manager.getControlService().deleteJob(jobId);
    }


    @RequestMapping("error")
    public void showError(@ModelAttribute("JobId") String jobId, HttpServletRequest request, HttpServletResponse response) throws IOException, CeaException
    {
	File file = manager.getQueryService().getLogFile(jobId, StdIOType.err);
	writeTextFile(response, file);
    }

    private void writeTextFile(HttpServletResponse response, File file)
    throws IOException, FileNotFoundException {
	response.setStatus(HttpServletResponse.SC_OK);
	response.setContentType("text/plain");
	BufferedReader reader = null;
	try {
	    PrintWriter pw = response.getWriter();
	    reader = new BufferedReader(new FileReader(file));
	    String input;
	    while ((input = reader.readLine()) != null) {
		pw.println(input);
	    }
	    pw.flush();
	    pw.close();
	} finally {
	    if(reader != null)
	    {
		reader.close();
	    }
	}
    }

    @RequestMapping("stdout")
    public void showStdOut(@ModelAttribute("JobId") String jobId, HttpServletRequest request, HttpServletResponse response) throws CeaException, IOException
    {
	File file = manager.getQueryService().getLogFile(jobId, StdIOType.out);
	writeTextFile(response, file);
    }

    /**
     * @param jobId
     * @param request
     * @param response
     * @throws CeaException 
     * @throws IOException 
     */
    @RequestMapping(value="results/*",method=RequestMethod.GET)
    public void showResult(@ModelAttribute("JobId") String jobId, HttpServletRequest request, HttpServletResponse response) throws CeaException, IOException
    {
	String reqURL = new UrlPathHelper().getPathWithinServletMapping(request);
	Matcher matcher = resultPattern.matcher(reqURL);
	String resultId = "";
	if(matcher.find())
	{
	    resultId = matcher.group(2);
	}

	switch (manager.getQueryService().queryExecutionStatus(jobId).getPhase()) {
	case COMPLETED:
	case ABORTED:
	case ERROR: //try to get anyway

	    //TODO what about indirect parameters - could try to resolve...
	    //TODO what about array valued parameters...
	    //IMPL how efficient is this?
	    ResultListType results = manager.getQueryService().getResults(jobId);
	    if(results.contains(resultId))
	    {
		String appId = manager.getQueryService().getSummary(jobId).getApplicationName();
		ParameterValue result = results.getResult(resultId);
		ParameterDescription pardef = manager.getApplicationDescriptionLibrary().getDescription(appId).getParameterDescription(resultId);
		response.setStatus(HttpServletResponse.SC_OK);
		if(result.isIndirect()){ //write indirect as html link for now...
		    response.setContentType("text/html");
		    PrintWriter out = response.getWriter();
		    out.println("<html><head></head><body><p><a href='"+result.getValue()+"'>"+result.getValue()+
		    "</a></p></body></html>");
		    out.close();
		}else {
		    response.setContentType(determinMime(result, pardef));
		    //FIXME this is clearly inadequate - CEA model does say that the outputs are strings though - needs to be unified... need to thing about indirect parameters in relation to this
		    response.setContentLength(result.getValue().length()); 
		    PrintWriter out = response.getWriter();
		    out.print(result.getValue());

		    out.close();
		}

	    }
	    else
	    {
		response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		response.setContentType("text/plain");
		PrintWriter pw = response.getWriter();
		pw.println("unknown result id="+resultId);
		pw.flush();
		pw.close();
	    }
	    break;
	    //every other phase is replied to with a  try later
	default:
	    response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
	response.setIntHeader("Retry-After", 30);//retry after 30 seconds
	response.setContentType("text/plain");
	PrintWriter pw = response.getWriter();
	pw.println("result not yet available");
	pw.flush();
	pw.close();
	break;
	}

    }

    /**
     * determine the mime type for a particular parameter.
     * @param result 
     * @TODO this probably deserves its own class. esp if the heuristics get more complicated. - perhaps put into
     * @param pardef
     * @return
     */
    private String determinMime(ParameterValue result, ParameterDescription pardef) {
	String retval = "text/plain";

	if (result.getMime() != null && result.getMime().length() > 0) {
	    retval = result.getMime();
	}else
	{
	    if (pardef.getMimeType() != null
		    && pardef.getMimeType().length() > 0) {
		retval = pardef.getMimeType();
	    } else {
		//make some "guesses"
		switch (pardef.getType()) {
		case BINARY:
		    retval = "application/octet-stream";
		    break;

		case TEXT:
		    retval = "text/plain";
		    break;

		case IMAGE:
		    retval = "image/fits";
		    break;

		case TABLE:
		    retval = "application/x-votable+xml";


		default:
		    retval = "application/octet-stream";
		break;
		}
	    }
	}
	return retval ;
    }



}


/*
 * $Log: UWSController.java,v $
 * Revision 1.3  2008/09/18 08:46:45  pah
 * improved javadoc
 *
 * Revision 1.2  2008/09/03 14:18:34  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.8  2008/08/29 07:40:41  pah
 * moved most of the commandline CEC into the main server - also new schema for CEAImplementation in preparation for DAL compatible service registration
 *
 * Revision 1.1.2.7  2008/06/10 20:10:49  pah
 * moved ParameterValue and friends to CEATypes.xsd
 *
 * Revision 1.1.2.6  2008/05/17 16:40:29  pah
 * joblist view * indirect result as link
 *
 * Revision 1.1.2.5  2008/05/13 16:02:47  pah
 * uws with full app running UI is working
 *
 * Revision 1.1.2.4  2008/05/08 22:44:03  pah
 * basic UWS working
 *
 * Revision 1.1.2.3  2008/05/01 15:36:32  pah
 * incorporated chiba xforms
 *
 * Revision 1.1.2.2  2008/04/23 14:13:49  pah
 * ASIGNED - bug 2749: make sure all CECs use the  ThreadPoolExecutionController
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2749
 *
 * Revision 1.1.2.1  2008/04/17 16:16:55  pah
 * removed all castor marshalling - even in the web service layer - unit tests passing
 * some uws functionality present - just the bare bones.
 *
 * ASSIGNED - bug 1611: enhancements for stdization holding bug
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
 * ASSIGNED - bug 2708: Use Spring as the container
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2708
 * ASSIGNED - bug 2739: remove dependence on castor/workflow objects
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2739
 *
 */
