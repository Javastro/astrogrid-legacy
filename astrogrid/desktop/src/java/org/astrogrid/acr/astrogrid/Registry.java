/*$Id: Registry.java,v 1.4 2005/05/12 15:59:09 clq2 Exp $
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

import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.client.query.ResourceData;

import java.net.URISyntaxException;

/** Interface to the astrogrid registry
 * @author Noel Winstanley nw@jb.man.ac.uk 18-Mar-2005
 *
 */
public interface Registry {
    /**Resolve an identifier to an endpoint 
     * 
     * @param ivorn identifier for the registered service
     * @return a URL endpoint of this service
     * @throws RegistryException
     * @throws URISyntaxException
     */
    String resolveIdentifier(String ivorn) throws RegistryException, URISyntaxException;

    /**Retreive a registry Entry
     * 
     * @param ivorn identifier of the registry entry to retrrieve
     * @return string containing xml document
     * @throws RegistryException
     * @throws URISyntaxException
     */
    String getRecord(String ivorn) throws RegistryException, URISyntaxException;

    /**
     * Retreive information about a registry entry
     * 
     * @param ivorn identifier of the registry entry to retrieve
     * @return a parsed represntation of the most significant parts of the registry entry
     * @throws RegistryException
     * @throws URISyntaxException
     */
    ResourceData getResourceData(String ivorn) throws RegistryException, URISyntaxException;
    
    
    /** perform a sadql query on the registry
     * 
     * @param adql a string query
     * @return string containing an xml document of search results.
     * @throws RegistryException
     */ 
    String search(String adql) throws RegistryException;
    
   }

/* 
 $Log: Registry.java,v $
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