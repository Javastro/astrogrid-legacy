/*$Id: BestMatchApplicationDescriptionLibrary.java,v 1.1 2005/11/01 09:19:46 nw Exp $
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

import org.astrogrid.acr.astrogrid.ApplicationInformation;
import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.BaseApplicationDescriptionLibrary;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.description.exception.ApplicationDescriptionNotFoundException;

import org.apache.commons.lang.ArrayUtils;

import java.net.URI;

/** customized app description library that tries to bridge gap between cea and acr systems.
 * also fits applications to descriptions in a more general way than just name matching
 *  - works based on the class of application description.
 * 
 * @author Noel Winstanley nw@jb.man.ac.uk 20-Oct-2005
 *
 */
public class BestMatchApplicationDescriptionLibrary extends BaseApplicationDescriptionLibrary {

    /** Construct a new BestMatchApplicationDescriptionLibrary
     * @param arg0
     */
    public BestMatchApplicationDescriptionLibrary(Registry reg,ApplicationDescriptionEnvironment arg0) {
        super(arg0);
        this.reg = reg;
    }
    private final Registry reg;
    
    
    /** returns true if this library contains an app matching the info objject
     * 
     * @param info
     * @return
     */
    public boolean hasMatch(ApplicationInformation info) {
        // @todo - test this. will only work if tests using equals(), not ==
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
Revision 1.1  2005/11/01 09:19:46  nw
messsaging for applicaitons.
 
*/