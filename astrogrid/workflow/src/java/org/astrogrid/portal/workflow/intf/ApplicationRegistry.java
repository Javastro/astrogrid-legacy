/*$Id: ApplicationRegistry.java,v 1.3 2004/11/11 00:54:18 clq2 Exp $
 * Created on 01-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.portal.workflow.intf;


/** A component that queries a registry for information about applications.
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Mar-2004
 * @modofied - added richer application list.
 */
public interface ApplicationRegistry {

    /** list names of known tools */
    String[] listApplications() throws WorkflowInterfaceException;

    /** query registry for description of a named application
     * 
     * @param applicationName name of the application.
     * @return an description of the application
     * @throws WorkflowtInterfaceException if an error occurs - i.e. if the application is not found
     */
    ApplicationDescription getDescriptionFor(String applicationName) throws WorkflowInterfaceException;

    /** list system names and user-friendly names of applications. */
    ApplicationDescriptionSummary[] listUIApplications() throws WorkflowInterfaceException;
    
      
      
      
      
}



/* 
$Log: ApplicationRegistry.java,v $
Revision 1.3  2004/11/11 00:54:18  clq2
nww's bug590

Revision 1.2.116.1  2004/11/10 13:33:32  nw
added new method to ApplicationRegistry - listUIApplications

Revision 1.2  2004/03/11 13:53:36  nw
merged in branch bz#236 - implementation of interfaces

Revision 1.1.2.1  2004/03/09 17:42:50  nw
getting there..

Revision 1.1  2004/03/09 15:33:00  nw
renamed toolRegistry to ApplicationRegistry

Revision 1.3  2004/03/03 11:15:23  nw
tarted up javadocs, reviewed types

Revision 1.2  2004/03/03 01:36:38  nw
merged interfaces in from branch nww-int05-bz#146

Revision 1.1.2.2  2004/03/03 01:18:00  nw
commited first draft of interface design

Revision 1.1.2.1  2004/03/01 19:02:57  nw
refined interfaces. almost ready to publish
 
*/