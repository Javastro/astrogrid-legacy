/*
 * $Id: SubmissionResponseServlet.java,v 1.2 2008/09/03 14:18:40 pah Exp $
 * 
 * Created on 2 May 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.chiba;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.chiba.web.WebFactory;
import org.chiba.web.servlet.WebUtil;
import org.chiba.web.session.XFormsSession;
import org.chiba.web.session.XFormsSessionManager;

public class SubmissionResponseServlet extends HttpServlet {
    /**
     * Logger for this class
     */
    private static final Log LOGGER = LogFactory
	    .getLog(SubmissionResponseServlet.class);

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // lookup session
        HttpSession session = request.getSession(false);

        XFormsSession xFormsSession = WebUtil.getXFormsSession(request,session);
        if (session != null) {
            if(LOGGER.isDebugEnabled()){
                Enumeration keys = session.getAttributeNames();
                if(keys.hasMoreElements()){
                    LOGGER.debug("--- existing keys in session --- ");
                }
                while (keys.hasMoreElements()) {
                    String s = (String) keys.nextElement();
                    LOGGER.debug("existing sessionkey: " + s + ":" + session.getAttribute(s));
                }
            }

            // lookup attribute containing submission response map
            Map submissionResponse = (Map) xFormsSession.getProperty(WebFactory.CHIBA_SUBMISSION_RESPONSE);
            if (submissionResponse != null) {

                if(LOGGER.isDebugEnabled()){
                    LOGGER.debug("handling submission/@replace='all'");
                    Enumeration keys = session.getAttributeNames();
                    if(keys.hasMoreElements()){
                        LOGGER.debug("--- existing keys in session  --- ");
                        while (keys.hasMoreElements()) {
                            String s = (String) keys.nextElement();
                            LOGGER.debug("existing sessionkey: " + s + ":" + session.getAttribute(s));
                        }
                    }else{
                        LOGGER.debug("--- no keys left in session  --- ");
                    }
                }

                // copy header fields
                Map headerMap = (Map) submissionResponse.get("header");
                String name;
                String value;
                Iterator iterator = headerMap.keySet().iterator();
                while (iterator.hasNext()) {
                    name = (String) iterator.next();
                    if (name.equalsIgnoreCase("Transfer-Encoding")) {
                        // Some servers (e.g. WebSphere) may set a "Transfer-Encoding"
                        // with the value "chunked". This may confuse the client since
                        // ChibaServlet output is not encoded as "chunked", so this
                        // header is ignored.
                        continue;
                    }
                    if(name.equalsIgnoreCase("status"))
                	{
                	//this is the naughty thing that the @link{org.astrogrid.applications.chiba.RedirectingHttpSubmissionHandler} puts into the header map...
                	continue;
                	}

                    value = (String) headerMap.get(name);
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("added header: " + name + "=" + value);
                    }

                    response.setHeader(name, value);
                }
                //This is added into the Header -naughty really...
                response.setStatus((Integer) headerMap.get("Status"));

                // copy body stream
                InputStream bodyStream = (InputStream) submissionResponse.get("body");
                OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
                for (int b = bodyStream.read(); b > -1; b = bodyStream.read()) {
                    outputStream.write(b);
                }

                // close streams
                bodyStream.close();
                outputStream.close();

                //kill XFormsSession
                XFormsSessionManager manager = (XFormsSessionManager) session.getAttribute(XFormsSessionManager.XFORMS_SESSION_MANAGER);
                manager.deleteXFormsSession(xFormsSession.getKey());

                return;
            }
        }

        response.sendError(HttpServletResponse.SC_FORBIDDEN, "no submission response available");
    }

    
}


/*
 * $Log: SubmissionResponseServlet.java,v $
 * Revision 1.2  2008/09/03 14:18:40  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.1  2008/05/08 22:44:04  pah
 * basic UWS working
 *
 */
