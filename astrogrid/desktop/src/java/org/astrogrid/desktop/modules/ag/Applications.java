/*$Id: Applications.java,v 1.2 2005/04/13 12:59:11 nw Exp $
 * Created on 21-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.desktop.modules.ag;

import org.astrogrid.portal.workflow.intf.ApplicationDescriptionSummary;
import org.astrogrid.portal.workflow.intf.ToolValidationException;
import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;
import org.astrogrid.workflow.beans.v1.Tool;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import java.io.IOException;
import java.net.URL;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Mar-2005
 *
 */
public interface Applications {
    String[] list() throws WorkflowInterfaceException;

    ApplicationDescriptionSummary[] fullList() throws WorkflowInterfaceException;

    String getRegEntry(String applicationName) throws WorkflowInterfaceException;

    String getInfo(String applicationName) throws WorkflowInterfaceException;

    Tool getToolTemplate(String applicationName, String interfaceName)
            throws WorkflowInterfaceException;

    boolean validateTool(String applicationName, Tool document) throws WorkflowInterfaceException,
            MarshalException, ValidationException, ToolValidationException;

    boolean validateToolFile(String applicationName, URL toolDocumentURL)
            throws WorkflowInterfaceException, MarshalException, ValidationException,
            ToolValidationException, IOException;
}

/* 
 $Log: Applications.java,v $
 Revision 1.2  2005/04/13 12:59:11  nw
 checkin from branch desktop-nww-998

 Revision 1.1.2.2  2005/03/22 12:04:03  nw
 working draft of system and ag components.
 
 */