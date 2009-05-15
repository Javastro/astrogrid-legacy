/*
 * $Id: SiapController.java,v 1.6 2009/05/15 23:12:48 pah Exp $
 * 
 * Created on 13 May 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.applications.uws;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.ivoa.uws.ExecutionPhase;

import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.component.CEAComponents;
import org.astrogrid.applications.description.execution.Tool;
import org.astrogrid.applications.manager.ExecutionController;
import org.astrogrid.applications.manager.QueryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * A controller to make the application behave like SIAP.
 * 
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 13 May 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 * @TODO this is really not finished, but sort of works...
 */
@Controller
@RequestMapping("/siap/**")
public class SiapController extends AbstractAccessProtocolController {

    public SiapController(CEAComponents manager) {
	super(manager);
    }

    @RequestMapping("")
    public void runApplication(HttpServletRequest request,
	    HttpServletResponse response) throws CeaException, IOException {
	
            if(isMetadataQuery(request, response)) {
            return;
           }
	    Tool tool = configureTool(request);
	    
            //FIXME - need split off POS into RA and DEC (if neccessary)

	    ExecutionController executionController = manager.getExecutionController();
	    String jobid = executionController.init(tool,
		    "job from siap interface", secGuard);
	    //directly run the job
	    executionController.execute(jobid, secGuard);
	    QueryService qs = manager.getQueryService();
	    //TODO improve this so that it does not use polling.
	    ExecutionPhase phase = qs.queryExecutionStatus(jobid, secGuard).getPhase();
	    while (phase.equals(ExecutionPhase.EXECUTING) || phase.equals(ExecutionPhase.QUEUED) || phase.equals(ExecutionPhase.PENDING)) {
		try {
		    Thread.sleep(1000);
		    phase = qs.queryExecutionStatus(jobid, secGuard).getPhase();
		} catch (InterruptedException e) {
		    continue; // exit this thread if interrupted - stop deadlock...
		} 
		
	    } 
	    //impl perhaps just send the result straight from here without the redirect.
	    response.sendRedirect(request.getContextPath()+"/uws/jobs/"+jobid+"/results/"+tool.getOutput().getParameter()[0].getId());
	    
	}

}

/*
 * $Log: SiapController.java,v $
 * Revision 1.6  2009/05/15 23:12:48  pah
 * ASSIGNED - bug 2911: improve authz configuration
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2911
 * combined agast and old stuff
 * refactored to a more specific CEA policy interface
 * made sure that there are decision points nearly everywhere necessary  - still needed on the saved history
 *
 * Revision 1.5  2008/10/20 10:35:41  pah
 * pull functioality up
 *
 * Revision 1.4  2008/09/25 23:14:28  pah
 * do not store appdesc - cannot be used in multi threaded environment anyway
 *
 * Revision 1.3  2008/09/04 21:20:02  pah
 * ASSIGNED - bug 2825: support VOSpace
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2825
 * Added the basic implementation to support VOSpace  - however essentially untested on real deployement - also UWS security will not be functional
 *
 * Revision 1.2  2008/09/03 14:18:34  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.2  2008/05/17 16:41:49  pah
 * refactor into abstract common class
 * Revision 1.1.2.1 2008/05/13 16:02:47 pah uws
 * with full app running UI is working
 * 
 */
