/*
 * $Id: TapServlet.java,v 1.5 2008/10/22 11:17:36 gtr Exp $
 */

package org.astrogrid.dataservice.service.tap;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.FileOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.io.mime.MimeTypes;
import org.astrogrid.io.Piper;
import org.astrogrid.webapp.DefaultServlet;
import org.astrogrid.query.Query;
import org.astrogrid.query.QueryException;
import org.astrogrid.query.returns.ReturnSpec;
import org.astrogrid.dataservice.service.DataServer;
import org.astrogrid.dataservice.service.ServletHelper;
import org.astrogrid.dataservice.queriers.DatabaseAccessException;
import org.astrogrid.dataservice.queriers.status.QuerierStatus;
import org.astrogrid.dataservice.queriers.status.QuerierError;
import org.astrogrid.tableserver.test.SampleStarsPlugin;
import org.astrogrid.slinger.targets.StreamTarget;
/*
import java.net.URL;
import org.astrogrid.dataservice.metadata.queryable.SearchGroup;
import org.astrogrid.slinger.targets.WriterTarget;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;
import org.astrogrid.io.mime.MimeTypes;
*/


/**
 * A servlet for processing TAP jobs queries.
 *
 *
 * POST to /jobs - creates a new job (let's call it job 42)
 * GET /jobs/42/phase - checks current state of job
 * GET /jobs/42/results - gets the results 
 *
 * DELETE /jobs/42 - cancels the job
 * @author K Andrews
 */
public class TapServlet extends DefaultServlet 
{
        static private Log log = LogFactory.getLog(TapServlet.class);
        
	// Keep this in sync with the context specified in web.xml
	private static String TAP_STEM = "tapservice";
	private DataServer server = new DataServer();
	private String baseUrl = "";

	public static String PHASE_PARAM_NAME = "PHASE";
	public static String START_COMMAND = "RUN";
	public static String ABORT_COMMAND = "ABORT";

	public static String ACTION_PARAM_NAME = "ACTION";
	public static String DELETE_COMMAND = "DELETE";

	protected static String PHASE_SUFFIX = "phase";
	protected static String TERMINATION_SUFFIX = "termination";
	protected static String DESTRUCTION_SUFFIX = "destruction";
	protected static String ERROR_SUFFIX = "error";
	protected static String QUOTE_SUFFIX = "quote";
	protected static String RESULTS_SUFFIX = "results";
 
	public TapServlet() {
		super();
		doInitialisation();
		baseUrl = ServletHelper.getUrlStem();
		if (!baseUrl.endsWith("/")) {
			baseUrl = baseUrl + "/";
		}
		//System.out.println("BASE URL IS " + baseUrl);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{
		if (isJobCreate(request)) {
			createNewJob(request,response);
		}
		else if (isJobStart(request)) {
			startJob(request,response);
		}
		else if (isJobAbort(request)) {
			abortJob(request,response);
		}
		else if (isJobDelete(request)) {
			deleteJob(request,response);
		}
		else {
			String errorResponseString = 
			"Unrecognised POST command has been rejected\n";
			LogFactory.getLog(request.getContextPath()).error(errorResponseString);
			//KLUDGE TOFIX
			doTypedError(request, response, errorResponseString, new Exception(errorResponseString));
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{
		if (isResultsFetch(request)) {
			returnResults(request,response);
		}
		else if (isErrorFetch(request)) {
			returnError(request,response);
		}
		else {
			String errorResponseString = 
			"Unrecognised GET command has been rejected\n";
			LogFactory.getLog(request.getContextPath()).error(errorResponseString);
			//KLUDGE TOFIX
			doTypedError(request, response, errorResponseString, new Exception(errorResponseString));
		}
	}

	public void doDelete(HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException 
	{
		if (isJobDelete(request)) {
			deleteJob(request,response);
		}
		else {
			String errorResponseString = 
			"Unrecognised POST command has been rejected\n";
			LogFactory.getLog(request.getContextPath()).error(errorResponseString);
			//KLUDGE TOFIX
			doTypedError(request, response, errorResponseString, new Exception(errorResponseString));
		}
	}

	protected void createNewJob(HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException 
	{
		String adqlString = "";
		try {
			// DEAL WITH A NEW JOB
			adqlString = ServletHelper.getQuery(request);

			ReturnSpec returnSpec = ServletHelper.makeReturnSpec(request);
			// Create a new job from the input ADQL
			Query adqlQuery = new Query(adqlString, returnSpec);

			// This one is a non-blocking request
		//	String id = server.submitQuery(ServletHelper.getUser(request), adqlQuery, request.getRemoteHost()+" ("+request.getRemoteAddr()+") via TAP servlet");
			String id = server.submitPendingQuery(
					ServletHelper.getUser(request), adqlQuery, 
					request.getRemoteHost()+" ("+request.getRemoteAddr()+
					") via TAP servlet");

			// For now, just write results to a file
			StreamTarget streamTarget = new StreamTarget(
					new FileOutputStream("/tmp/dsa/"+id));
			returnSpec.setTarget(streamTarget);
			adqlQuery.setResultsDef(returnSpec);

			// Now return 303 with the job resource in the Location: header
			response.setStatus(HttpServletResponse.SC_SEE_OTHER);
			response.setHeader("Location", baseUrl + TAP_STEM + "/" + id + "/");
			return;
		}
		catch (java.lang.IllegalArgumentException ex) {
		  //Typically thrown if a bad table name is given
		  doPotentialConfigError(request, response, adqlString, ex);
		} 
		catch (QueryException qe) {
		  //Typically thrown if the input query is bad
			String errorResponseString = 
			"TAP query with query string= " + adqlString +"</p>" +
			"<p>Your query has been rejected; see below for details.\n";
			LogFactory.getLog(request.getContextPath()).error( qe+errorResponseString,qe);
			doTypedError(request, response, errorResponseString, qe);
		} 
		catch (Throwable th) {
			String errorResponseString = 
			"TAP query with query string= " + adqlString +"</p>" +
			"<p>Your TAP job has failed; see below for details.\n";
			LogFactory.getLog(request.getContextPath()).error( th+errorResponseString,th);
			doTypedError(request, response, errorResponseString, th);
		}
	}

	protected void startJob(HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException 
	{
		String jobID = getJobID(request);
		server.startPendingQuery(ServletHelper.getUser(request), jobID);
	}

	protected void abortJob(HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException 
	{
		String jobID = getJobID(request);
		server.abortQuery(ServletHelper.getUser(request), jobID);
	}

	protected void deleteJob(HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException 
	{
		String jobID = getJobID(request);
		server.deleteQuery(ServletHelper.getUser(request), jobID);
	}

	protected void returnResults(HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException 
	{
		String jobID = getJobID(request);
		Reader reader = new FileReader("/tmp/dsa/"+jobID);
		Writer writer = response.getWriter();
		Piper.bufferedPipe(reader,writer);
	}
	protected void returnError(HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException 
	// TOFIX - in the end, have custom return here (don't rely on
	// doTypedError etc)
	{
	
		String msg = "";
		String jobID = getJobID(request);
		QuerierStatus status = server.getQueryStatus(
				ServletHelper.getUser(request), jobID);
		if (!(status instanceof QuerierError)) {
			// There is no error
			msg = "No error has occurred!";
		}
		else {
			msg = status.getMessage() + " ";
			String[] details= status.getDetails();
         for (int i=0;i<details.length;i++) {
				// TOFIX add <p> tags here for HTML returns?
            msg = msg + details[i]+" \n";
         }
		}
		// TOFIX:  Use headers to choose appropriate return mime type
		// This one for HTML
		doErrorMessage(response,"TAP job error",msg); // HTML version
		// This one for VOTABLE
		//doTypedError(request, response, msg, new Exception(msg));
	}

	/*
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{
		doInitialisation();

		// Extract the query parameters
		double ra=0, dec=0, radius=0;
		String catalogName="", tableName="";
		try {
			catalogName = ServletHelper.getCatalogName(request);
			tableName = ServletHelper.getTableName(request);
			radius = ServletHelper.getRadius(request);
			ra = ServletHelper.getRa(request);
			dec = ServletHelper.getDec(request);
			ReturnSpec returnSpec = ServletHelper.makeReturnSpec(request);

			String raColName = TableMetaDocInterpreter.getConeRAColumnByName(
				 catalogName, tableName);
			String decColName = TableMetaDocInterpreter.getConeDecColumnByName(
				 catalogName, tableName);
			String units = TableMetaDocInterpreter.getConeUnitsByName(
				 catalogName, tableName);

			// Create the query
			Query coneQuery = new Query(
					catalogName, tableName, units, raColName, decColName, 
					ra, dec, radius, returnSpec);

			if (returnSpec.getTarget() == null) {
				//if a target is not given, we do an (ask) Query 
				// to the response stream.
				returnSpec.setTarget(new WriterTarget(response.getWriter(), false));
				
				if (ServletHelper.isCountReq(request)) {
					// This one is a blocking request
					long count = server.askCount(ServletHelper.getUser(request), coneQuery, request.getRemoteHost()+" ("+request.getRemoteAddr()+") via SubmitCone servlet");
					response.setContentType(MimeTypes.PLAINTEXT);
					response.getWriter().write(""+count);
				}
				else {
					// This one is a blocking request
					// TOFIX KONA CHECK VALID CONTENT TYPE FOR NORMAL CONE
					// (NOT JUST BROWSER)
					response.setContentType(coneQuery.getResultsDef().getFormat());
					server.askQuery(ServletHelper.getUser(request), coneQuery, request.getRemoteHost()+" ("+request.getRemoteAddr()+") via SubmitCone servlet");
				}
			}
			else {
				//otherwise we direct the response to the target and put status 
				//info to the browser
				response.setContentType(MimeTypes.HTML);

				response.getWriter().println(
					"<html>"+
					"<head><title>Submitting Query</title></head>"+
					"<body>"+
					"<p>Submitting, please wait...</p>");
				response.getWriter().flush();

				// This one is a non-blocking request
				String id = server.submitQuery(ServletHelper.getUser(request), coneQuery, request.getRemoteHost()+" ("+request.getRemoteAddr()+") via SubmitCone servlet");
		
				URL statusUrl = new URL ("http",request.getServerName(),request.getServerPort(), request.getContextPath()+"/admin/queryStatus.jsp");
				//indicate status
				response.getWriter().println("Cone Query has been submitted, and assigned ID "+id+"."+
														"<a href='"+statusUrl+"?ID="+id+"'>Query Status Page</a>\n");
				response.getWriter().flush();

				//redirect to status
				response.getWriter().write("<META HTTP-EQUIV='Refresh' CONTENT='0;URL="+statusUrl+"?ID="+id+"'>"+
												  "</body></html>");
				response.getWriter().flush();
			}
		}
		catch (java.lang.IllegalArgumentException ex) {
		  //Typically thrown if a bad table name is given
		  doPotentialConfigError(request, response, ra, dec, radius, ex);
		} 
		catch (java.lang.NullPointerException ex) {
		  //Typically thrown if a bad column name is given
		  doPotentialConfigError(request, response, ra, dec, radius, ex);
		}
		catch (Throwable th) {
			LogFactory.getLog(request.getContextPath()).error(th+
				 "conesearching table '" +tableName +"' in catalog '" +
				 catalogName +"' with RA= " + Double.toString(ra) + 
				 ", Dec = " + Double.toString(dec) + 
				 ", radius = " + Double.toString(radius),
				 th);

			doTypedError(request, response, 
				 "conesearching table '" +tableName +"' in catalog '" +
				 catalogName +"' with RA = " + Double.toString(ra) + 
				 ", Dec = " + Double.toString(dec) + 
				 ", radius = " + Double.toString(radius),
				 th);
		}
	}
*/

	protected boolean isPost(HttpServletRequest request)
	{
		if ("POST".equals(request.getMethod())) {
			return true;
		}
		return false;
	}
	protected boolean isGet(HttpServletRequest request)
	{
		if ("GET".equals(request.getMethod())) {
			return true;
		}
		return false;
	}
	protected boolean isDelete(HttpServletRequest request)
	{
		if ("DELETE".equals(request.getMethod())) {
			return true;
		}
		return false;
	}


	protected boolean isJobCreate(HttpServletRequest request) 
			throws IOException
	{
		if (isPost(request) && getJobID(request) == null) {
			return true;
		}
		return false;
	}
	protected boolean isJobStart(HttpServletRequest request) 
			throws IOException
	{
		String jobID = getJobID(request);
		if (jobID == null) {
			return false;	// No job specified
		}
		String taskSuffix = getTaskSuffix(request);
		if (isPost(request) && PHASE_SUFFIX.equals(taskSuffix)) {
			String newphase = request.getParameter(PHASE_PARAM_NAME);
			if (START_COMMAND.equals(newphase)) {
				return true;
			}
			return false;
		}
		return false;
	}
	protected boolean isJobAbort(HttpServletRequest request) 
			throws IOException
	{
		String jobID = getJobID(request);
		if (jobID == null) {
			return false;	// No job specified
		}
		String taskSuffix = getTaskSuffix(request);
		if (isPost(request) && PHASE_SUFFIX.equals(taskSuffix)) {
			String newphase = request.getParameter(PHASE_PARAM_NAME);
			if (ABORT_COMMAND.equals(newphase)) {
				return true;
			}
			return false;
		}
		return false;
	}
	protected boolean isJobDelete(HttpServletRequest request) 
			throws IOException
	{
		// Two options here:  POST with "ACTION=DELETE" or just DELETE
		// In either case there should be no suffix after the job ID
		// TOFIX
		String jobID = getJobID(request);
		if (jobID == null) {
			return false;	// No job specified
		}
		String taskSuffix = getTaskSuffix(request);
		if (taskSuffix != null) {
			return false;	// Not a valid job deletion request
		}
		if (isDelete(request)) {
			return true;
		}
		if (isPost(request)) {
			String command = request.getParameter(ACTION_PARAM_NAME);
			if (DELETE_COMMAND.equals(command)) {
				return true;
			}
		}
		return false;
	}
	protected boolean isStatusFetch(HttpServletRequest request) 
			throws IOException
	{
		String jobID = getJobID(request);
		if (jobID == null) {
			return false;	// No job specified
		}
		String taskSuffix = getTaskSuffix(request);
		if (PHASE_SUFFIX.equals(taskSuffix)) {
			return true;
		}
		return false;
	}
	protected boolean isResultsFetch(HttpServletRequest request) 
			throws IOException
	{
		String jobID = getJobID(request);
		if (jobID == null) {
			return false;	// No job specified
		}
		String taskSuffix = getTaskSuffix(request);
		if (RESULTS_SUFFIX.equals(taskSuffix)) {
			return true;
		}
		return false;
	}
	protected boolean isErrorFetch(HttpServletRequest request)
			throws IOException
	{
		String jobID = getJobID(request);
		if (jobID == null) {
			return false;	// No job specified
		}
		if (request.getRequestURI().endsWith("error")) {
			return true;
		}
		return false;
	}

	private void doPotentialConfigError(
		 HttpServletRequest request, HttpServletResponse response, 
		 String query, Exception ex) throws IOException {

		String errorResponseString = 
			"TAP query with query string= " + query +"</p>" +
			"<p>An error has occurred (see below); this may be because this datacenter installation is misconfigured.\n" +
			"<p>Admin note: please check the datacenter metadoc file to see that your conesearchable tables are correctly configured.\n";
			 
		LogFactory.getLog(request.getContextPath()).error( ex+errorResponseString,ex);
		doTypedError(request, response, errorResponseString, ex);
	}
/*
*/

	protected void doTypedError(HttpServletRequest request, HttpServletResponse response, String title, Throwable th) throws IOException {
		String format = request.getParameter("Format");
		if ( (format == null) || (format.trim() == "") ||
			(format.toLowerCase().equals("votable")) ) {
			try {
			  response.setContentType(MimeTypes.VOTABLE);
			}
			catch (RuntimeException re) {
			}
			String error = "There was no error information supplied";
			if (th != null) {
				error = th.getMessage();
/*
				if (error != null) {
					error = error.replaceAll("&", "&amp;"); 
					error = error.replaceAll("<", "&lt;");
					error = error.replaceAll(">", "&gt;");
				}
*/
			}
			PrintWriter writer = response.getWriter();
			writer.println("<?xml version='1.0' encoding='UTF-8'?>");
			writer.println("<!DOCTYPE VOTABLE SYSTEM \"http://us-vo.org/xml/VOTable.dtd\">");
			writer.println("<VOTABLE version=\"1.0\">");
			writer.println("<DESCRIPTION>Error processing query</DESCRIPTION>");
			writer.println("<INFO ID=\"Error\" name=\"Error\" value=\"" +
				  error + "\"/>");
			writer.println("</VOTABLE>");
		}
		else {
			doError(response, title, th);
		}
	}

	protected String getTaskSuffix(HttpServletRequest request) throws IOException
   {
      String fragments = getUrlFragments(request);
      StringTokenizer tokenizer = new StringTokenizer(fragments,"/");
      // First token is sac name
      if (tokenizer.hasMoreTokens()) {
        	tokenizer.nextToken(); //Throw second token (job ID) away
      	if (tokenizer.hasMoreTokens()) {
         	tokenizer.nextToken(); //Throw second token (job ID) away
      		if (tokenizer.hasMoreTokens()) {
         		String suffix = tokenizer.nextToken().trim();
					if ((suffix != null) && (!suffix.equals(""))) {
						return suffix;
					}
				}
			}
      }
      return null;
   }

   protected String getJobID(HttpServletRequest request) throws IOException
   {
      log.debug("getPathInfo() returned " + request.getPathInfo());
      StringTokenizer tokenizer = new StringTokenizer(request.getPathInfo(),"/");
      // First token is /jobs/ (or whatever
      // Second token is JobID ID
      // Third token (if any) is task suffix
      if (tokenizer.hasMoreTokens()) {
         // Throw this away. Calling nextToken() without using the result doesn't
         // work; the optimize removes the call.
         log.debug("Discarded: " + tokenizer.nextToken());
         if (tokenizer.hasMoreTokens()) {
            String claimID = tokenizer.nextToken().trim();
            if ((claimID != null) && (!claimID.equals(""))) {
               log.debug("Found job ID in request URL: " + claimID);
               return claimID;
            }
            return null;
         }
         return null;
      }
      return null;
   }

   protected String getUrlFragments(HttpServletRequest request) throws IOException
   {
      String context = request.getContextPath();
      String fullTail = request.getRequestURI();
      // Subtract context path
      fullTail = fullTail.substring(context.length());
      if ("".equals(fullTail) || fullTail == null) {
         fullTail = "/";
      }
      if (fullTail.charAt(0) != '/') {
         fullTail = "/" + fullTail;
      }
      if (fullTail.charAt(fullTail.length()-1) != '/') {
         fullTail = fullTail + "/";
      }
		// Check that our suffix starts with the expected stem
      StringTokenizer tokenizer = new StringTokenizer(fullTail,"/");
		String stem = tokenizer.nextToken(); //Check for valid suffix
		if (!TAP_STEM.equals(stem)) {
			// Shouldn't get here, unless this class is inconsistent with
			// the web.xml mapping putting TAP service at TAP_STEM context
			throw new IOException("KONA TOFIX");
		}
      return fullTail;
   }


	protected void doInitialisation() 
	{
		// Initialise SampleStars plugin if required (may not be initialised
		// if admin has not run the self-tests)
		try {
			String plugin = ConfigFactory.getCommonConfig().getString(
					"datacenter.querier.plugin");
			if (plugin.equals("org.astrogrid.tableserver.test.SampleStarsPlugin")) {
					// This has no effect if the plugin is already initialised
					SampleStarsPlugin.initialise();  // Just in case
			}
		}
		catch (DatabaseAccessException dae) {
			// TOFIX KLUDGE
		}
	}
}
