/*$Id: ServerInfo.java,v 1.4 2004/11/24 19:49:22 clq2 Exp $
 * Created on 30-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.integration;

import org.astrogrid.workflow.beans.v1.Tool;

/**
 * interface that encapsulates characteristics of a cea server. Used to plug into cea tests to customize for different services, and also used in workflow tests
 * to assist with constructing tool-call steps.
 * @author Noel Winstanley nw@jb.man.ac.uk 30-Jun-2004
 *
 */

public interface ServerInfo {
    /** name of the application that is to be executed */
    public String getApplicationName();
    /** string to search for in service list */
    public String getServerSearchString();
    /** list of applications supported by this server*/
    public String[] getApplicationNames();
    
    /** populate a tool with direct parameters for this application */
    public void populateDirectTool(Tool tool);
    /** populate a tool with indirect parameters for this application 
     * <p>
     * assumed that, for testing, ther'll be one input indirect parameter, and one output indirect parameter
     * @param tool tool to populate
     * @param inputURI uri coontaining input data
     * @param outputURI uri where result data will be placed.
     */
    public void populateIndirectTool(Tool tool, String inputURI, String outputURI);
}

/* 
$Log: ServerInfo.java,v $
Revision 1.4  2004/11/24 19:49:22  clq2
nww-itn07-659

Revision 1.1.104.1  2004/11/18 10:52:01  nw
javadoc, some very minor tweaks.

Revision 1.1  2004/07/01 11:43:33  nw
cea refactor
 
*/