/*$Id: HTMLDriver.java,v 1.1 2004/04/06 08:29:21 nw Exp $
 * Created on 05-Apr-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.servlet;

import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.jes.JesException;
import org.astrogrid.jes.component.ComponentManager;
import org.astrogrid.jes.component.ComponentManagerFactory;
import org.astrogrid.jes.job.JobException;
import org.astrogrid.jes.job.SubmitJobRequest;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

import org.apache.axis.utils.XMLUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/** Simple servlet that allows JES to be driven from a HTML interface.
 * @author Noel Winstanley nw@jb.man.ac.uk 05-Apr-2004
 *
 */
public class HTMLDriver extends HttpServlet {
    public final static String INSPECT = "inspect";
    public final static String SUBMIT = "submit";
    public final static String LIST = "list";
    public final static String WORKFLOW = "workflow";
    
    public final static String ACTION = "action";
    public final static String URN = "urn";
    
    public final static String USERNAME = "username";
    public final static String COMMUNITY = "community";
    /** Construct a new HTMLDriver
     * 
     */
    public HTMLDriver() {
        super();
    }
    /**
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter(ACTION);
        try {
        if (INSPECT.equalsIgnoreCase(action)) {
            doInspect(req,resp);
        } else {
            doList(req,resp);
        }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    /**
     * @param req
     * @param resp
     */
    private void doList(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        ComponentManager man =ComponentManagerFactory.getInstance();
        Account acc = new Account();
        acc.setCommunity(req.getParameter(COMMUNITY));
        acc.setName(req.getParameter(USERNAME));
        PrintWriter out = resp.getWriter();
        out.println("<html><body>");
        out.println("<h1>Jobs for " + acc.getName() + "@" + acc.getCommunity());
        Iterator i = man.getFacade().getJobFactory().findUserJobs(acc);
        out.println("<table>");        
        while (i.hasNext()) {
            Workflow wf = (Workflow)i.next();
            out.println("<tr>");
            out.println("<td>" + wf.getName() + "</td>");
            String urn = wf.getJobExecutionRecord().getJobId().getContent();
            out.println("<td><a href='html-driver?action=inspect&urn=" + URLEncoder.encode(urn) + "'>"
                + urn + "</a></td>");
            out.println("</tr>");
        }
        out.println("</table>");
        out.println("</body></html>");
    }
    /**
     * @param req
     * @param resp
     */
    private void doInspect(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        ComponentManager man = ComponentManagerFactory.getInstance();
        JobURN urn = new JobURN();
        urn.setContent(req.getParameter(URN).trim());
        Workflow wf = man.getFacade().getJobFactory().findJob(urn);
        TransformerFactory fac = TransformerFactory.newInstance();
        Transformer trans = fac.newTransformer(
            new StreamSource(this.getClass().getResourceAsStream("workflow.xsl"))
            );
        
        StringWriter sw = new StringWriter();
        wf.marshal(sw);
        sw.close();
        trans.setOutputProperty(OutputKeys.METHOD,"html");
        trans.transform(
            new StreamSource(new StringReader(sw.toString()))
            , new StreamResult(resp.getOutputStream())
            );
    }
    /**
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
        ComponentManager man = ComponentManagerFactory.getInstance();
        SubmitJobRequest subReq = man.getFacade().createSubmitJobRequest(req.getParameter(WORKFLOW));
        man.getFacade().getJobFactory().createJob(subReq);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }



}


/* 
$Log: HTMLDriver.java,v $
Revision 1.1  2004/04/06 08:29:21  nw
start of a html interface
 
*/