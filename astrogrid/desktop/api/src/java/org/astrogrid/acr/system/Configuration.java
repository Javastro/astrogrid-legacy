/*$Id: Configuration.java,v 1.5 2006/02/02 14:19:47 nw Exp $
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

import org.astrogrid.acr.ACRException;

import java.util.Map;

/**Inspect and alter the configuration of the workbench and ACR.
 * 
 * Allows the user / applications to set key-value pairs,  which are automatically persisted between executions.
 * As well as alteriing configuration settings, this is also 
 * useful for storing bits of state that should persist between script invocations (e.g. preferences, window positions and sizes), plus configuration information (e.g. username, service endpoints)
 * @service system.configuration
 * 
 * @author Noel Winstanley nw@jb.man.ac.uk 15-Mar-2005
 *
 */
public interface Configuration {
    

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
 * @throws ACRException in unlikely case of the backing store being unreadable.
    */
    public abstract String[] listKeys() throws ACRException;

    /** list the contents of the store
     * 
     * @return a map of key-value pairs.
     * @throws ACRException in unlikely case of the backing store being unreadable
     */
    public abstract Map list() throws ACRException;

    /** remove a previously defined key.
     * @param string
     */
    public abstract void removeKey(String string);
}

/* 
 $Log: Configuration.java,v $
 Revision 1.5  2006/02/02 14:19:47  nw
 fixed up documentation.

 Revision 1.4  2005/11/01 09:26:29  nw
 moved required keys elsewhere

 Revision 1.3  2005/10/19 18:20:05  nw
 added fallback registry endpoint

 Revision 1.2  2005/08/12 08:45:15  nw
 souped up the javadocs

 Revision 1.1  2005/08/11 10:15:00  nw
 finished split

 Revision 1.6  2005/08/05 11:46:55  nw
 reimplemented acr interfaces, added system tests.

 Revision 1.5  2005/06/08 14:51:59  clq2
 1111

 Revision 1.2.8.2  2005/06/02 14:34:32  nw
 first release of application launcher

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