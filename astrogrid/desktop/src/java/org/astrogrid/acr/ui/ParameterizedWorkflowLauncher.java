/*$Id: ParameterizedWorkflowLauncher.java,v 1.2 2005/04/27 13:42:41 clq2 Exp $
 * Created on 23-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.acr.ui;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 23-Mar-2005
 *
 */
public interface ParameterizedWorkflowLauncher {
    void run();
}

/* 
 $Log: ParameterizedWorkflowLauncher.java,v $
 Revision 1.2  2005/04/27 13:42:41  clq2
 1082

 Revision 1.1.2.1  2005/04/25 11:18:51  nw
 split component interfaces into separate package hierarchy
 - improved documentation

 Revision 1.2  2005/04/13 12:59:18  nw
 checkin from branch desktop-nww-998

 Revision 1.1.2.2  2005/03/29 16:10:59  nw
 integration with the portal
 
 */