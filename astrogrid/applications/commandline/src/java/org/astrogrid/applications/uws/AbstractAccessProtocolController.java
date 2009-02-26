/*
 * $Id: AbstractAccessProtocolController.java,v 1.6 2009/02/26 12:22:54 pah Exp $
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
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.component.CEAComponents;
import org.astrogrid.applications.description.ApplicationDefinition;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.ParameterDescription;
import org.astrogrid.applications.description.base.ParameterTypes;
import org.astrogrid.applications.description.exception.ApplicationDescriptionNotFoundException;
import org.astrogrid.applications.description.exception.ParameterDescriptionNotFoundException;
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

    public AbstractAccessProtocolController(CEAComponents manager,
            String appregexp) {
        this.appRegexp = appregexp;
        this.manager = manager;
        appPattern = Pattern.compile(appRegexp);
    }

    private final String appRegexp;
    protected final Pattern appPattern;
    protected SecurityGuard secGuard;

    protected String parsAppName(HttpServletRequest request) {
        String reqURL = new UrlPathHelper()
                .getPathWithinServletMapping(request);
        Matcher matcher = appPattern.matcher(reqURL);
        if (matcher.find()) {
            String appShortname = matcher.group(1);
            return appShortname;
        } else {
            return null;
        }

    }

    /**
     * Configure a tool from the input parameters.
     * 
     * @param request
     * @return The tool document filled in, or null if the application was not
     *         found.
     * @see AbstractAccessProtocolController#appdesc which is set by this
     *      method.
     * @throws ApplicationDescriptionNotFoundException
     * @throws CeaException
     * @throws IOException
     * 
     */
    protected Tool configureTool(HttpServletRequest request)
            throws ApplicationDescriptionNotFoundException, CeaException,
            IOException {
            String appShortname = parsAppName(request);
            if(appShortname != null){
                ApplicationDefinition appdesc;    
            appdesc = manager.getApplicationDescriptionLibrary()
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

    protected void sendMetadata(ApplicationDefinition appdesc, PrintWriter pw) {
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

    /**
     * Checks if a metadata query is requested. Sends the metadata if necessary.
     * @param request
     * @param response
     * @return true if metadata requested.
     * @throws ApplicationDescriptionNotFoundException
     * @throws IOException
     */
    protected boolean isMetadataQuery(HttpServletRequest request, HttpServletResponse response)
            throws ApplicationDescriptionNotFoundException, IOException {
                if(request.getParameterMap().containsKey("FORMAT") && 
                	    request.getParameter("FORMAT").equals("METADATA")){
                	sendMetadata(manager.getApplicationDescriptionLibrary()
                                    .getDescriptionByShortName(parsAppName(request)), response.getWriter());
                	return true;
                    }
                else {
                    return false;
                }
            }

}

/*
 * $Log: AbstractAccessProtocolController.java,v $
 * Revision 1.6  2009/02/26 12:22:54  pah
 * separate more out into cea-common for both client and server
 *
 * Revision 1.5  2008/10/20 10:35:40  pah
 * pull functioality up
 *
 * Revision 1.4  2008/09/25 23:12:50  pah
 * do not store appdesc - cannot be used in multi threaded environment anyway
 * Revision 1.3 2008/09/04
 * 21:20:02 pah ASSIGNED - bug 2825: support VOSpace
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2825 Added the basic
 * implementation to support VOSpace - however essentially untested on real
 * deployement - also UWS security will not be functional
 * 
 * Revision 1.2 2008/09/03 14:18:34 pah result of merge of pah_cea_1611 branch
 * 
 * Revision 1.1.2.2 2008/06/10 20:10:49 pah moved ParameterValue and friends to
 * CEATypes.xsd
 * 
 * Revision 1.1.2.1 2008/05/17 16:41:49 pah refactor into abstract common class
 */
