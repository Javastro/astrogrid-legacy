/*$Id: BackdoorServlet.java,v 1.4 2004/08/13 09:07:26 nw Exp $
 * Created on 21-Apr-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.servlet;

import org.astrogrid.jes.JesException;
import org.astrogrid.jes.component.JesComponentManager;
import org.astrogrid.jes.component.JesComponentManagerFactory;
import org.astrogrid.jes.jobscheduler.Locator;
import org.astrogrid.workflow.beans.v1.Tool;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Backdoor into some of the jes componets, for easy server-side testing.
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Apr-2004
 *
 */
public class BackdoorServlet extends HttpServlet {
    /** Construct a new BackdoorServlet
     * 
     */
    public BackdoorServlet() {
        super();
    }
    /**
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action.equalsIgnoreCase("locate")) {
            String toolName = request.getParameter("name");
            JesComponentManager manager = JesComponentManagerFactory.getInstance();
            Locator locator = (Locator)manager.getContainer().getComponentInstance(Locator.class);
            if (locator == null) {
                throw new ServletException("locator object was null");
            }
            Tool tool = new Tool();
            tool.setName(toolName);
            try {
                String url = locator.locateTool(tool);
                PrintWriter writer = response.getWriter();
                writer.println(url);
            } catch (JesException e) {
                throw new ServletException("Jes Barfed with exception",e);
            }
        } 
    }

}


/* 
$Log: BackdoorServlet.java,v $
Revision 1.4  2004/08/13 09:07:26  nw
tidied imports

Revision 1.3  2004/08/03 16:31:25  nw
simplified interface to dispatcher and locator components.
removed redundant implementations.

Revision 1.2  2004/07/09 09:30:28  nw
merged in scripting workflow interpreter from branch
nww-x-workflow-extensions

Revision 1.1  2004/04/21 10:06:21  nw
added backdoor servlet
 
*/