/*$Id: ApplicationRegistry.java,v 1.1 2004/03/09 15:33:00 nw Exp $
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
 *
 */
public interface ApplicationRegistry {

    /** list names of known tools 
     * @todo - do we want to return a little more here - a summary of info for each tool maybe*/
    String[] listApplications() throws WorkflowInterfaceException;

    /** query registry for description of a named application
     * 
     * @param applicationName name of the application.
     * @return an description of the application
     * @throws WorkfactInterfaceException if an error occurs - i.e. if the application is not found
     */
    ApplicationDescription getDescriptionFor(String applicationName);

    
    
      
      
      
      
}



/* 
$Log: ApplicationRegistry.java,v $
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