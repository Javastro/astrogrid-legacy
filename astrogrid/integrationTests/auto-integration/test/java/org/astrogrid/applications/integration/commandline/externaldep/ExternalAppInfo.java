/*$Id: ExternalAppInfo.java,v 1.1 2004/10/12 12:58:02 pah Exp $
 * Created on 30-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.integration.commandline.externaldep;

import org.astrogrid.workflow.beans.v1.Tool;

/**
 * interface that encapsulates differences between different cea applications - allows reuse of more of the test code.
 * @author Paul Harrison (pah@jb.man.ac.uk)
 *
 */

public interface ExternalAppInfo {
    /** name of the application that is to be executed */
    public String getApplicationName();
    
    /** return the name of the application Interface that is to be used **/
    public String getApplicationInterfaceName();
    
    /** populate a tool with direct parameters for this application */
    public void populateTool(Tool tool);
}

