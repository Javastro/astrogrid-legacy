/*
 * $Id: JobCreationController.java,v 1.7 2009/05/15 23:12:48 pah Exp $
 * 
 * Created on 9 Apr 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.uws;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.util.ValidationEventCollector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.component.CEAComponents;
import org.astrogrid.applications.description.execution.ExecutionSummaryType;
import org.astrogrid.applications.description.execution.Tool;
import org.astrogrid.applications.description.jaxb.CEAJAXBUtils;
import org.astrogrid.applications.manager.ExecutionController;
import org.astrogrid.applications.manager.QueryService;
import org.astrogrid.applications.manager.persist.ExecutionHistory;
import org.astrogrid.security.SecurityGuard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for listing and creating Jobs.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 9 Apr 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
@Controller
@RequestMapping("/jobs")
public class JobCreationController {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory
	    .getLog(JobCreationController.class);
    private final CEAComponents manager;

    @Autowired
    public JobCreationController(CEAComponents manager){
	this.manager=manager;
    }
    
    
    @RequestMapping(method=RequestMethod.POST)
    public void createJob(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
	ExecutionController cec = manager.getExecutionController();
	PrintWriter pw = response.getWriter();
	String accept = request.getHeader("Accept");
	ValidationEventCollector handler = new ValidationEventCollector();
	try {
	Tool tool;
	    
	    // Unmarshall the file into a content object
	    tool = CEAJAXBUtils.unmarshall(request.getInputStream(), Tool.class);
	    	
	    String arg1= "not set";
	    SecurityGuard secGuard = UWSUtils.createSecurityGuard(request);
	    String jobid = cec.init(tool, arg1, secGuard);
	    if (request.getParameter("AUTORUN")!=null) {
		cec.execute(jobid, secGuard);
		Thread.sleep(300);
	    }
	    UWSUtils.redirectToJobSummary(request, response, jobid);
	    
	} catch (CeaException e) { // This is probably an error starting the job
	    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	    e.printStackTrace(pw);
	    pw.close();
	} catch (Exception e) {
	    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	    pw.println("error reading tool");
	    if (handler.hasEvents()) {
		for (int i = 0; i < handler.getEvents().length; i++) {
		    ValidationEvent event = handler.getEvents()[i];
		    pw.println(event.toString());
		}

	    }
	    e.printStackTrace(pw);
	    pw.close();
	}
    }

    @RequestMapping(method=RequestMethod.GET)
    public ModelAndView listJobs(HttpServletRequest request, HttpServletResponse response) throws CeaException, CertificateException
    {
	ExecutionHistory eh = manager.getExecutionHistoryService();
	QueryService qs = manager.getQueryService();
	ModelAndView modelAndView;
	if (UWSUtils.needsxml(request)) {
	    modelAndView = new ModelAndView("listjobs-xml");
        } else {
            modelAndView = new ModelAndView("listjobs");
        }
	 
        SecurityGuard secGuard = UWSUtils.createSecurityGuard(request);
        	List<JobOverview> jobs = new ArrayList<JobOverview>();
	String[] jobids = eh.getExecutionIDs();
	for (int i = 0; i < jobids.length; i++) {
	    if (eh.isCached(jobids[i])) {
           ExecutionSummaryType job = qs.getSummary(jobids[i], secGuard);
	           jobs.add(new JobOverview(job.getJobId(), job.getPhase().toString()));
               
            } else {
                   jobs.add(new JobOverview(jobids[i], "Archived"));
           
            }
	}
	modelAndView.addObject("jobs", jobs);
	return modelAndView;
    }
    
    /**
     * Bean to allow brief job details to be displayed.
     * @author Paul Harrison (paul.harrison@manchester.ac.uk) 7 Mar 2009
     * @version $Name:  $
     * @since VOTech Stage 8
     */
    public static class JobOverview {
        private String jobId;
        private String phase;
        public JobOverview(String id, String phase) {
            this.jobId = id;
            this.phase = phase;
        }
        public String getJobId() {
            return jobId;
        }
        public String getPhase() {
            return phase;
        }
        
    }
    

}


/*
 * $Log: JobCreationController.java,v $
 * Revision 1.7  2009/05/15 23:12:48  pah
 * ASSIGNED - bug 2911: improve authz configuration
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2911
 * combined agast and old stuff
 * refactored to a more specific CEA policy interface
 * made sure that there are decision points nearly everywhere necessary  - still needed on the saved history
 *
 * Revision 1.6  2009/03/07 09:40:14  pah
 * RESOLVED - bug 2891: upgrade performance of record keeping
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2891
 *
 * Revision 1.5  2008/09/25 23:14:18  pah
 * new redirect functions
 *
 * Revision 1.4  2008/09/24 13:43:09  pah
 * add xml output
 *
 * Revision 1.3  2008/09/04 21:20:02  pah
 * ASSIGNED - bug 2825: support VOSpace
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2825
 * Added the basic implementation to support VOSpace  - however essentially untested on real deployement - also UWS security will not be functional
 *
 * Revision 1.2  2008/09/03 14:18:34  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.5  2008/08/29 07:40:40  pah
 * moved most of the commandline CEC into the main server - also new schema for CEAImplementation in preparation for DAL compatible service registration
 *
 * Revision 1.1.2.4  2008/08/02 13:32:23  pah
 * safety checkin - on vacation
 *
 * Revision 1.1.2.3  2008/05/13 16:02:47  pah
 * uws with full app running UI is working
 *
 * Revision 1.1.2.2  2008/05/08 22:44:03  pah
 * basic UWS working
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
