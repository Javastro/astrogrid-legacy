/*
 * $Id: SyncJobCreationController.java,v 1.2 2009/05/15 23:12:48 pah Exp $
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
import java.security.cert.CertificateException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.component.CEAComponents;
import org.astrogrid.applications.description.execution.ExecutionSummaryType;
import org.astrogrid.applications.description.execution.Tool;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UrlPathHelper;

/**
 * An application job creation controller that is not strictly UWS, behaves like
 * a DAL v1. synchronous service while actually creating jobs
 * 
 * <ol>
 * <li>POST to /sync/appname to create job</li>
 * <li>returns 303 redirect to the</li>
 * </ol>
 * 
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 9 May 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
@Controller
@RequestMapping("/sync/**")
public class SyncJobCreationController extends AbstractAccessProtocolController {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(SyncJobCreationController.class);

    public SyncJobCreationController(CEAComponents manager) {
        super(manager, "/sync/+([^/\\?#]+)([/\\?#]?)");
    }

    @RequestMapping("*/*")
    public void startApplication(HttpServletRequest request,
            HttpServletResponse response) throws IOException, CeaException,
            CertificateException {

        Tool tool = configureTool(request);
        secGuard = UWSUtils.createSecurityGuard(request);
        String jobid = manager.getExecutionController().init(tool,
                "job from simple post interface", secGuard);
        manager.getExecutionController().execute(jobid, secGuard);
        StringBuffer redirURL = new StringBuffer("/sync/");
        redirURL.append(parsAppName(request));
        redirURL.append("/");
        redirURL.append(jobid);
        UWSUtils.redirectTo(request, response, redirURL.toString());
    }

    @RequestMapping("*/*/*")
    public void waitForJob(HttpServletRequest request,
            HttpServletResponse response) throws IOException, CeaException {
        String appName = parsAppName(request);
        String pathWithinServletMapping = new UrlPathHelper()
                .getPathWithinServletMapping(request);
        int appidx = pathWithinServletMapping.lastIndexOf(appName);
        String jobId = pathWithinServletMapping.substring(appidx
                + appName.length() + 1);
        
        ExecutionSummaryType exSummary = manager.getQueryService().getSummary(jobId, secGuard);
        if (!manager.getApplicationDescriptionLibrary().getDescription(exSummary.getApplicationName()).getName().equals(appName))
        {
            throw new CeaException("application name does not match jobid");
        }
        //Block here wait for job to finish
        //IMPL would be better to attach as a listener to the executing application
        while(UWSUtils.notFinished(manager.getQueryService().queryExecutionStatus(jobId, secGuard).getPhase()))
        {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }
        String result = manager.getQueryService().getResults(jobId, secGuard).getResult().get(0).getId();
        StringBuffer redirURL = new StringBuffer("/jobs/");
        redirURL.append(jobId);
        redirURL.append("/results/");
        redirURL.append(result);
        UWSUtils.redirectTo(request, response, redirURL.toString());
        
    }
}

/*
 * $Log: SyncJobCreationController.java,v $
 * Revision 1.2  2009/05/15 23:12:48  pah
 * ASSIGNED - bug 2911: improve authz configuration
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2911
 * combined agast and old stuff
 * refactored to a more specific CEA policy interface
 * made sure that there are decision points nearly everywhere necessary  - still needed on the saved history
 *
 * Revision 1.1  2008/09/25 23:16:44  pah
 * synchronous application execution
 * Revision 1.4 2008/09/15 17:19:05 pah
 * get securityguard into UWS chain
 * 
 * Revision 1.3 2008/09/04 21:20:02 pah ASSIGNED - bug 2825: support VOSpace
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2825 Added the basic
 * implementation to support VOSpace - however essentially untested on real
 * deployement - also UWS security will not be functional
 * 
 * Revision 1.2 2008/09/03 14:18:34 pah result of merge of pah_cea_1611 branch
 * 
 * Revision 1.1.2.4 2008/08/29 07:40:40 pah moved most of the commandline CEC
 * into the main server - also new schema for CEAImplementation in preparation
 * for DAL compatible service registration
 * 
 * Revision 1.1.2.3 2008/08/02 13:32:23 pah safety checkin - on vacation
 * 
 * Revision 1.1.2.2 2008/05/17 16:41:49 pah refactor into abstract common class
 * Revision 1.1.2.1 2008/05/13 16:02:47 pah uws with full app running UI is
 * working
 */
