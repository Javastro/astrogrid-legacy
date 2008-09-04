/*
 * $Id: SiapController.java,v 1.3 2008/09/04 21:20:02 pah Exp $
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
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.ivoa.uws.ExecutionPhase;

import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.component.CEAComponents;
import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.ParameterDescription;
import org.astrogrid.applications.description.base.ParameterTypes;
import org.astrogrid.applications.description.exception.ParameterDescriptionNotFoundException;
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
	
	    Tool tool = configureTool(request);
	    if(request.getParameterMap().containsKey("FORMAT") && 
		    request.getParameter("FORMAT").equals("METADATA")){
		sendMetadata(appdesc, response.getWriter());
		return;
	    }
	    
            //FIXME - need split off POS into RA and DEC (if neccessary)

	    ExecutionController executionController = manager.getExecutionController();
	    String jobid = executionController.init(tool,
		    "job from siap interface", secGuard);
	    //directly run the job
	    executionController.execute(jobid);
	    QueryService qs = manager.getQueryService();
	    //TODO improve this so that it does not use polling.
	    ExecutionPhase phase = qs.queryExecutionStatus(jobid).getPhase();
	    while (phase.equals(ExecutionPhase.EXECUTING) || phase.equals(ExecutionPhase.QUEUED) || phase.equals(ExecutionPhase.PENDING)) {
		try {
		    Thread.sleep(1000);
		    phase = qs.queryExecutionStatus(jobid).getPhase();
		} catch (InterruptedException e) {
		    continue; // exit this thread if interrupted - stop deadlock...
		} 
		
	    } 
	    //impl perhaps just send the result straight from here without the redirect.
	    response.sendRedirect(request.getContextPath()+"/uws/jobs/"+jobid+"/results/"+tool.getOutput().getParameter()[0].getId());
	    
	}
    

    private void sendMetadata(ApplicationDescription appdesc,
	    PrintWriter pw)  {
	pw.println("<?xml version='1.0'?>");
	pw.println("<VOTABLE version='1.1' xmlns='http://www.ivoa.net/xml/VOTable/v1.1'>");
	pw.println("<DESCRIPTION>Metadata response for "+appdesc.getId()+"</DESCRIPTION>");
	pw.println("<COOSYS ID='J2000' equinox='J2000.' epoch='J2000.' system='eq_FK5'/>");
	pw.println("<RESOURCE type='results'>");
	pw.println(" <DESCRIPTION>Simple Image Access Service "+appdesc.getId()+"</DESCRIPTION>");
	pw.println(" <INFO name='QUERY_STATUS' value='OK'/>");
	ApplicationInterface iface = appdesc.getInterfaces()[0];
	String[] inputs = iface.getArrayofInputs();
	for (int i = 0; i < inputs.length; i++) {
	    try {
		ParameterDescription pdesc = appdesc.getParameterDescription(inputs[i]);
		pw.print("<PARAM name='");
		pw.print(pdesc.getId()+"'");
		pw.print(" datatype='"+convertType(pdesc.getType())+"'");
		pw.print(" ucd='"+pdesc.getUcd()+"'");
		pw.print(" unit='"+pdesc.getUnit()+"'");
		//FIXME get the default value in as well as any min/max
		
		pw.print(">");
	        pw.println("<DESCRIPTION>"+pdesc.getDescription()+"</DESCRIPTION>");
	        
		
		pw.println("</PARAM>");
	    } catch (ParameterDescriptionNotFoundException e) {
		logger.fatal("programming error", e);
	    }

	}
	pw.println("<TABLE>");
	//FIXME - need standardized way of getting the column information...
	pw.println("</TABLE>");
	pw.println("</RESOURCE>");
	pw.println("</VOTABLE>");
    }

    private String convertType(ParameterTypes type) {
	//FIXME the convert type is not really outputting legal types
	return type.toString();
    }

}

/*
 * $Log: SiapController.java,v $
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
