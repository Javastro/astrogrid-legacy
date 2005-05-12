/*$Id: Configuration.java,v 1.4 2005/05/12 15:59:12 clq2 Exp $
 * Created on 15-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.acr.system;

import org.astrogrid.desktop.modules.ag.PortalImpl;

import java.util.Map;
import java.util.prefs.BackingStoreException;

/**Configuration component - allows other components (and the user) to set and retrieve key-value pairs, 
 * which are automatically persisted between executions.
 * @author Noel Winstanley nw@jb.man.ac.uk 15-Mar-2005
 *
 */
public interface Configuration {
    
    /** list of keys that must be present in the configuration system
     * @todo move this elsewhere
     */
    public static final String[] REQUIRED_KEYS = { "org.astrogrid.registry.query.endpoint",
            "org.astrogrid.registry.admin.endpoint",
            "jes.job.controller.endpoint", 
            PortalImpl.PORTAL_ROOT_KEY
            };

    /** Set the value of a new or existing key
     * 
     * @param key name of key
     * @param value new value of key
     * @return ignore
     */
    public abstract boolean setKey(String key, String value);
    
    /** get the value of a key
     * 
     * @param key the name of the key
     * @return the associated value, or null
     */
    public abstract String getKey(String key);

   /** list the keys present in the store
    * 
    * @return an array of key names
    * @throws BackingStoreException
    */
    public abstract String[] listKeys() throws BackingStoreException;

    /** list the contents of the store
     * 
     * @return a map of key-value pairs.
     * @throws BackingStoreException
     */
    public abstract Map list() throws BackingStoreException;
}

/* 
 $Log: Configuration.java,v $
 Revision 1.4  2005/05/12 15:59:12  clq2
 nww 1111 again

 Revision 1.2.8.1  2005/05/11 11:55:19  nw
 javadoc

 Revision 1.2  2005/04/27 13:42:41  clq2
 1082

 Revision 1.1.2.1  2005/04/25 11:18:51  nw
 split component interfaces into separate package hierarchy
 - improved documentation

 Revision 1.2  2005/04/13 12:59:12  nw
 checkin from branch desktop-nww-998

 Revision 1.1.2.2  2005/03/29 16:10:59  nw
 integration with the portal

 Revision 1.1.2.1  2005/03/18 12:09:31  nw
 got framework, builtin and system levels working.
 
 */