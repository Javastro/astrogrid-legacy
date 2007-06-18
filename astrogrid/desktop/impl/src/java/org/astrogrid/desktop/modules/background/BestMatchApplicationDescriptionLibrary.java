/*$Id: BestMatchApplicationDescriptionLibrary.java,v 1.7 2007/06/18 16:33:31 nw Exp $
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
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;

import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.ConeService;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.SiapService;
import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.BaseApplicationDescriptionLibrary;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.description.exception.ApplicationDescriptionNotFoundException;

/** customized app description library that tries to bridge gap between cea and acr systems.
 * also fits applications to descriptions in a more general way than just name matching
 *  - works based on the kind of service being invoked.
 * @FIXME HARD-CODED for now - will come up with a better solution later.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 20-Oct-2005
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
    
    

    public boolean hasMatch(CeaApplication info) {
    	return (info instanceof ConeService || info instanceof SiapService);
    } 
    
    
    /** overridden - will return a matching application, not necessarily named the same */
    public ApplicationDescription getDescription(String arg1)
            throws ApplicationDescriptionNotFoundException {
       // bah - got to back-track a little here. irritating..
        Resource info = null;
			try {
				URI uri = new URI(( arg1.startsWith("ivo://") ? "" : "ivo://" ) + arg1);
				info = reg.getResource(uri);
			} catch (URISyntaxException x) {
	            throw new ApplicationDescriptionNotFoundException("URISyntaxException");
			} catch (NotFoundException x) {
				throw new ApplicationDescriptionNotFoundException(arg1);
			} catch (ServiceException x) {
				throw new ApplicationDescriptionNotFoundException(x.getMessage());
			}

        if (info instanceof ConeService ) {
        	return super.getDescription(ConeService.class.getName());
        } else if (info instanceof SiapService) {
        	return super.getDescription(SiapService.class.getName());        	
        } else {
        	throw new ApplicationDescriptionNotFoundException(arg1);
        }  
    }
}


/* 
$Log: BestMatchApplicationDescriptionLibrary.java,v $
Revision 1.7  2007/06/18 16:33:31  nw
javadoc fixes.

Revision 1.6  2007/01/29 11:11:36  nw
updated contact details.

Revision 1.5  2006/09/02 00:48:15  nw
cleaned up exeception handling that was hiding a bug.

Revision 1.4  2006/08/15 10:15:34  nw
migrated from old to new registry models.

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