/*$Id: BaseApplicationDescriptionLibrary.java,v 1.2 2004/07/01 11:16:22 nw Exp $
 * Created on 17-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.description;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.applications.description.exception.ApplicationDescriptionNotFoundException;
import org.astrogrid.component.descriptor.ComponentDescriptor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import junit.framework.Test;

/** Basic implementation of an application description library, based on a map. 
 * @author Noel Winstanley nw@jb.man.ac.uk 17-Jun-2004
 *
 */
public class BaseApplicationDescriptionLibrary implements ApplicationDescriptionLibrary, ComponentDescriptor {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(BaseApplicationDescriptionLibrary.class);

    /** Construct a new BaseApplicationDescriptionLibrary
     * 
     */
    public BaseApplicationDescriptionLibrary() {
        super();
    }

    /**
         * @see org.astrogrid.applications.description.ApplicationDescriptionLibrary#getDescription(java.lang.String)
         */
    public ApplicationDescription getDescription(String name) throws ApplicationDescriptionNotFoundException {
        ApplicationDescription ad = (ApplicationDescription) descMap.get(name);
        if (ad == null) {
            throw new ApplicationDescriptionNotFoundException(name);
        }
        return ad;
    }
    /**
         * @see org.astrogrid.applications.description.ApplicationDescriptionLibrary#getApplicationNames()
         */
    public String[] getApplicationNames() {
    return (String[])descMap.keySet().toArray(new String[0]);
      }
    private final Map descMap = new HashMap();
    
    /** add an application description to the library */
    public void addApplicationDescription(ApplicationDescription desc) {
        logger.info("Adding description for " + desc.getName());
        descMap.put(desc.getName(),desc);
    }
    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getName()
     */
    public String getName() {
        return "Basic Application Description Library";
    }
    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        return this.toString();
    }
     
   public String toString() {     
        StringBuffer appList = new StringBuffer();
        for (Iterator i = descMap.values().iterator(); i.hasNext(); ) {
            ApplicationDescription desc = (ApplicationDescription)i.next();
            appList.append("\n");
            appList.append(desc.toString());
            appList.append("\n");
        }
        return "Applications in Library:'"  + appList.toString();
    }
    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getInstallationTest()
     */
    public Test getInstallationTest() {
        return null;
    }    
}


/* 
$Log: BaseApplicationDescriptionLibrary.java,v $
Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.1  2004/07/01 01:42:46  nw
final version, before merge
 
*/