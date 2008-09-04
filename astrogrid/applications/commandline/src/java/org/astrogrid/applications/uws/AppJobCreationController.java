/*
 * $Id: AppJobCreationController.java,v 1.3 2008/09/04 21:20:02 pah Exp $
 * 
 * Created on 9 May 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.component.CEAComponents;
import org.astrogrid.applications.description.execution.Tool;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * An application job creation controller that is not strictly UWS, but allows
 * for a Quick launch of the application by a simple POST of the parameter
 * values.
 * 
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 9 May 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
@Controller
@RequestMapping("/app/**")
public class AppJobCreationController extends AbstractAccessProtocolController {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory
	    .getLog(AppJobCreationController.class);
    public AppJobCreationController(CEAComponents manager) {
	super(manager, "/app/+([^/\\?#]+)[/\\?#]?");
    }

    @RequestMapping("*")
    public void startApplication(HttpServletRequest request,
	    HttpServletResponse response) throws IOException, CeaException {
	Tool tool = configureTool(request);
        secGuard = UWSUtils.createSecurityGuard(request);
	String jobid = manager.getExecutionController().init(tool,
		"job from simple post interface", secGuard);
	manager.getExecutionController().execute(jobid);
	UWSUtils.redirect(request, response, jobid);
//	response.sendRedirect(rURL.toString());
//	response.setContentType("text/plain");
//	PrintWriter pw = response.getWriter();
//	pw.print(jobid);
//	pw.flush();
//	pw.close();

    }
}

/*
 * $Log: AppJobCreationController.java,v $
 * Revision 1.3  2008/09/04 21:20:02  pah
 * ASSIGNED - bug 2825: support VOSpace
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2825
 * Added the basic implementation to support VOSpace  - however essentially untested on real deployement - also UWS security will not be functional
 *
 * Revision 1.2  2008/09/03 14:18:34  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.4  2008/08/29 07:40:40  pah
 * moved most of the commandline CEC into the main server - also new schema for CEAImplementation in preparation for DAL compatible service registration
 *
 * Revision 1.1.2.3  2008/08/02 13:32:23  pah
 * safety checkin - on vacation
 *
 * Revision 1.1.2.2  2008/05/17 16:41:49  pah
 * refactor into abstract common class
 * Revision 1.1.2.1 2008/05/13 16:02:47
 * pah uws with full app running UI is working
 * 
 */
