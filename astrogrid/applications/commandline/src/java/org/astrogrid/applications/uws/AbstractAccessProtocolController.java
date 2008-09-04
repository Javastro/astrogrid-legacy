/*
 * $Id: AbstractAccessProtocolController.java,v 1.3 2008/09/04 21:20:02 pah Exp $
 * 
 * Created on 14 May 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.uws;

import java.io.IOException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.component.CEAComponents;
import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.exception.ApplicationDescriptionNotFoundException;
import org.astrogrid.applications.description.execution.ListOfParameterValues;
import org.astrogrid.applications.description.execution.ParameterValue;
import org.astrogrid.applications.description.execution.Tool;
import org.astrogrid.security.SecurityGuard;
import org.springframework.web.util.UrlPathHelper;

/**
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 14 May 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 * 
 */
public class AbstractAccessProtocolController {

    /** logger for this class */
    protected static final org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory
	    .getLog(AbstractAccessProtocolController.class);
    protected final CEAComponents manager;
    public AbstractAccessProtocolController(CEAComponents manager) {
	this(manager, "/s[sit]ap/+([^/\\?#]+)[/\\?#]?");

    }
    public AbstractAccessProtocolController(CEAComponents manager, String appregexp)
    {
	this.appRegexp = appregexp;
	this.manager = manager;
	appPattern = Pattern.compile(appRegexp);
    }
    private final String appRegexp ;
    protected final Pattern appPattern;
    protected ApplicationDescription appdesc;
    protected SecurityGuard secGuard;
    /**
     * Configure a tool from the input parameters.
     * 
     * @param request
     * @return The tool document filled in, or null if the application was not
     *         found.
     *         @see AbstractAccessProtocolController#appdesc which is set by this method.
     * @throws ApplicationDescriptionNotFoundException
     * @throws CeaException
     * @throws IOException
     * 
     */
    protected Tool configureTool(HttpServletRequest request)
	    throws ApplicationDescriptionNotFoundException, CeaException,
	    IOException {
	        String reqURL = new UrlPathHelper()
	        	.getPathWithinServletMapping(request);
	        appdesc = null;
	        Matcher matcher = appPattern.matcher(reqURL);
	        if (matcher.find()) {
	            String appShortname = matcher.group(1);
	            appdesc = manager
	        	    .getApplicationDescriptionLibrary()
	        	    .getDescriptionByShortName(appShortname);
		    ApplicationInterface intf = appdesc.getInterfaces()[0];
	            Tool tool = new Tool();
	            tool.setId(appdesc.getId());
	            tool.setInterface(intf.getId());
	            ListOfParameterValues pvals = new ListOfParameterValues();
	            Enumeration en = request.getParameterNames();
	    
	            // TODO - perhaps check for outputs in the input pars
	            while (en.hasMoreElements()) {
	        	String parname = (String) en.nextElement();
	        	String[] parameterValues = request.getParameterValues(parname);
	        	for (int i = 0; i < parameterValues.length; i++) {
	        	    ParameterValue pval = new ParameterValue(parname,
	        		    parameterValues[i]);
	        	    pvals.addParameter(pval);
	        	}
	    
	            }
	            tool.setInput(pvals);
	            ListOfParameterValues outvals = new ListOfParameterValues();
	            String[] outnames = intf.getArrayofOutputs();
	            for (int i = 0; i < outnames.length; i++) {
	        	ParameterValue pval = new ParameterValue();
	        	pval.setId(outnames[i]);
	        	outvals.addParameter(pval);
	            }
	            tool.setOutput(outvals);
	            return tool;
	    
	        }
	    
	        else {
	            throw new ApplicationDescriptionNotFoundException(
	        	    "no application found in request");
	    
	        }
	    }

    
}


/*
 * $Log: AbstractAccessProtocolController.java,v $
 * Revision 1.3  2008/09/04 21:20:02  pah
 * ASSIGNED - bug 2825: support VOSpace
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2825
 * Added the basic implementation to support VOSpace  - however essentially untested on real deployement - also UWS security will not be functional
 *
 * Revision 1.2  2008/09/03 14:18:34  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.2  2008/06/10 20:10:49  pah
 * moved ParameterValue and friends to CEATypes.xsd
 *
 * Revision 1.1.2.1  2008/05/17 16:41:49  pah
 * refactor into abstract common class
 *
 */
