/*
 * $Id: Publish.java,v 1.2 2007/11/13 16:47:12 pah Exp $
 * 
 * Created on 9 Nov 2007 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2007 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.acr.astrogrid;
import java.net.URI;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.w3c.dom.Document;

/** Publish to a registry.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 9 Nov 2007
 * @version $Name:  $
 * @service astrogrid.publish
 * @since 2007.03
 */
public interface Publish {

    /**
     * Publish a resource in a registry.
     * @param registry the IVOA identifier of the registry. Note this will only work with astrogrid registries.
     * @param entry the resource to be published.
     */
    void register(URI registry, Document entry) throws ServiceException, InvalidArgumentException, NotFoundException, SecurityException;
}


/*
 * $Log: Publish.java,v $
 * Revision 1.2  2007/11/13 16:47:12  pah
 * add exceptions
 *
 * Revision 1.1  2007/11/12 13:36:28  pah
 * change parameter name to structure
 *
 */
