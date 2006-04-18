/*$Id: BestMatchApplicationDescriptionLibrary.java,v 1.3 2006/04/18 23:25:43 nw Exp $
 * Created on 20-Oct-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.background;

import java.net.URI;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.astrogrid.acr.astrogrid.ApplicationInformation;
import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.BaseApplicationDescriptionLibrary;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.description.exception.ApplicationDescriptionNotFoundException;

/** customized app description library that tries to bridge gap between cea and acr systems.
 * also fits applications to descriptions in a more general way than just name matching
 *  - works based on the class of application description.
 * 
 * @author Noel Winstanley nw@jb.man.ac.uk 20-Oct-2005
 *
 */
public class BestMatchApplicationDescriptionLibrary extends BaseApplicationDescriptionLibrary implements IBestMatchApplicationDescriptionLibrary {

    /** Construct a new BestMatchApplicationDescriptionLibrary
     * @param arg0
     */
    public BestMatchApplicationDescriptionLibrary(Registry reg,ApplicationDescriptionEnvironment arg0,List descs) {
        super(arg0);
        this.reg = reg;
        for (Iterator i = descs.iterator(); i.hasNext(); ) {
            ApplicationDescription a = (ApplicationDescription)i.next();
            this.addApplicationDescription(a);
        }
    }
    private final Registry reg;
    
    
    /**
     * @see org.astrogrid.desktop.modules.background.IBestMatchApplicationDescriptionLibrary#hasMatch(org.astrogrid.acr.astrogrid.ApplicationInformation)
     */
    public boolean hasMatch(ApplicationInformation info) {
        return ArrayUtils.contains(getApplicationNames(),info.getClass().getName());
    } 
    
    
    /** overridden - will return a matching application, not necessarily named the same */
    public ApplicationDescription getDescription(String arg1)
            throws ApplicationDescriptionNotFoundException {
       // bah - got to back-track a little here. irritating..
        ResourceInformation info = null;
        try {
            URI uri = new URI(( arg1.startsWith("ivo://") ? "" : "ivo://" ) + arg1);
            info = reg.getResourceInformation(uri);
        } catch (Exception e) { // badly-propagated exceptions, but all pretty unlikely - as we've checked it exists first.
            throw new ApplicationDescriptionNotFoundException(e.getMessage());
        }
        return super.getDescription(info.getClass().getName());     
    }
}


/* 
$Log: BestMatchApplicationDescriptionLibrary.java,v $
Revision 1.3  2006/04/18 23:25:43  nw
merged asr development.

Revision 1.2.34.2  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.2.34.1  2006/03/28 13:47:35  nw
first webstartable version.

Revision 1.2  2005/11/10 10:46:58  nw
big change around for vo lookout

Revision 1.1  2005/11/01 09:19:46  nw
messsaging for applicaitons.
 
*/