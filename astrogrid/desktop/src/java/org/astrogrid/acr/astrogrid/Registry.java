/*$Id: Registry.java,v 1.5 2005/08/05 11:46:55 nw Exp $
 * Created on 18-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.acr.astrogrid;

import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;

import org.w3c.dom.Document;

import java.net.URI;
import java.net.URL;

/** Interface to the astrogrid registry
 * @author Noel Winstanley nw@jb.man.ac.uk 18-Mar-2005
 *
 */
public interface Registry {
    /**Resolve an identifier to an endpoint 
     * 
     * @param ivorn identifier for the registered service
     * @return a URL endpoint of this service
     * @throws NotFoundException if this resource does not exist
     * @throws ServiceException if an error occurs talking to the service
     */
    URL resolveIdentifier(URI ivorn) throws NotFoundException, ServiceException;

    /**Retreive a registry Entry
     * 
     * @param ivorn identifier of the registry entry to retrrieve
     * @return xml document of the registry entry
     * @throws NotFoundException if this resource does not exist
     * @throws ServiceException if an error occurs talking to the service
     */
    Document getRecord(URI ivorn) throws NotFoundException, ServiceException;

    /**
     * Retreive information about a registry entry
     * 
     * @param ivorn identifier of the registry entry to retrieve
     * @return a  summary of the most significant parts of the registry entry
     * @throws NotFoundException if this resource does not exist
     * @throws ServiceException if an error occurs talking to the service
     */
    ResourceInformation getResourceInformation(URI ivorn) throws  NotFoundException, ServiceException;
    
    
    /** perform a query on the registry
     * 
     * @param adql a string query (string form of ADQL)
     * @return  xml document of search results.
     * @throws ServiceException if an error occurs talking to the service
     */ 
    Document search(String adql) throws ServiceException;
    
   }

/* 
 $Log: Registry.java,v $
 Revision 1.5  2005/08/05 11:46:55  nw
 reimplemented acr interfaces, added system tests.

 Revision 1.4  2005/05/12 15:59:09  clq2
 nww 1111 again

 Revision 1.2.8.1  2005/05/11 09:52:45  nw
 exposed new registry methods

 Revision 1.2  2005/04/27 13:42:41  clq2
 1082

 Revision 1.1.2.1  2005/04/25 11:18:51  nw
 split component interfaces into separate package hierarchy
 - improved documentation

 Revision 1.2  2005/04/13 12:59:11  nw
 checkin from branch desktop-nww-998

 Revision 1.1.2.2  2005/03/22 12:04:03  nw
 working draft of system and ag components.
 
 */