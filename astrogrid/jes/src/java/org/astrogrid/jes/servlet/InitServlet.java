/*$Id: InitServlet.java,v 1.3 2004/07/30 15:42:34 nw Exp $
 * Created on 14-May-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.servlet;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;

import javax.servlet.GenericServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/** servlet that passes in configuration information from the servlet context.
 * @todo not needed any more.. 
 * @author Noel Winstanley nw@jb.man.ac.uk 14-May-2004
 *
 */
public class InitServlet extends GenericServlet {
    private static final Log log = LogFactory.getLog(InitServlet.class);
    /** Construct a new InitServlet
     * 
     */
    public InitServlet() {
        super();
    }

    /**
     * @see javax.servlet.GenericServlet#service(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
     */
    public void service(ServletRequest arg0, ServletResponse arg1) throws ServletException, IOException {
        // we do nothing.
    }
    /**
     * @see javax.servlet.Servlet#init(javax.servlet.ServletConfig)
     */
    public void init(ServletConfig conf) throws ServletException {


    }

}


/* 
$Log: InitServlet.java,v $
Revision 1.3  2004/07/30 15:42:34  nw
merged in branch nww-itn06-bz#441 (groovy scripting)

Revision 1.2.20.2  2004/07/30 14:00:10  nw
first working draft

Revision 1.2.20.1  2004/07/28 16:24:23  nw
finished groovy beans.
moved useful tests from old python package.
removed python implemntation

Revision 1.2  2004/07/09 09:30:28  nw
merged in scripting workflow interpreter from branch
nww-x-workflow-extensions

Revision 1.1.2.1  2004/05/21 11:25:19  nw
first checkin of prototype scrpting workflow interpreter
 
*/