/*$Id: ApplicationDescriptionSummary.java,v 1.3 2004/11/12 18:14:43 clq2 Exp $
 * Created on 10-Nov-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.portal.workflow.intf;

/** A little container object, that contains some interesting suymmary info about an application.
 * @author Noel Winstanley nw@jb.man.ac.uk 10-Nov-2004
 *
 */
public class ApplicationDescriptionSummary {

    /** Construct a new ApplicationDescriptionSummary
     * 
     */
    public ApplicationDescriptionSummary(String name,String uiName,String[] interfaceNames) {
        this.name = name;
        this.uiName = uiName;
        this.interfaceNames = interfaceNames;
    }
    
    protected final String name;
    protected final String uiName;
    protected final String[] interfaceNames;
    
    /** system name of the application */
    public String getName() {
        return name;
    }
    
    /** user-friendly name of the appliication */
    public String getUIName() {
        return uiName;
    }
    /** array of the interface names for this application */
    public String[] getInterfaceNames() {
        return interfaceNames;
    }
    

}


/* 
$Log: ApplicationDescriptionSummary.java,v $
Revision 1.3  2004/11/12 18:14:43  clq2
nww-itn07-590b again.

Revision 1.1.2.1  2004/11/10 13:33:32  nw
added new method to ApplicationRegistry - listUIApplications
 
*/