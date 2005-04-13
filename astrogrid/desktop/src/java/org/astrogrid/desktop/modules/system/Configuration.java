/*$Id: Configuration.java,v 1.2 2005/04/13 12:59:12 nw Exp $
 * Created on 15-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.desktop.modules.system;

import org.astrogrid.desktop.modules.ag.PortalImpl;

import java.util.Map;
import java.util.prefs.BackingStoreException;

/**@
 * @author Noel Winstanley nw@jb.man.ac.uk 15-Mar-2005
 *
 */
public interface Configuration {
    public static final String[] REQUIRED_KEYS = { "org.astrogrid.registry.query.endpoint",
            "org.astrogrid.registry.admin.endpoint",
            // don't think this one is needed."org.astrogrid.community.ident",
            "jes.job.controller.endpoint", 
            PortalImpl.PORTAL_ROOT_KEY
            //not needed either.."org.astrogrid.community.default.vospace" 
            };

    /**@@MethodDoc("setKey","set a configuration key")
     @@.return ReturnDoc("Success code",rts=BooleanResultTransformerSet.getInstance())
     @@.key ParamDoc("key","Key to set");
     @@.value ParamDoc("value","Value to set to")
     */
    public abstract boolean setKey(String key, String value);

    /**@@MethodDoc("getKey","get a value for a confuguration key")
     * @@.return ReturnDoc("Key value")
     * @@.key ParamDoc("key","key to retrive value for")     
     */
    public abstract String getKey(String key);

    /**@@MethodDoc("listKeys","list configutation keys")
     * @@.return ReturnDoc("array of keys",rts=ArrayResultTransformerSet.getInstance())
     * */
    public abstract String[] listKeys() throws BackingStoreException;

    /** @@MethodDoc("list","list configuration entries")
     * @@.return ReturnDoc("map of key, value pairs",rts=StructResultTransformerSet.getInstance())
     * @return
     * @throws BackingStoreException
     */
    public abstract Map list() throws BackingStoreException;
}

/* 
 $Log: Configuration.java,v $
 Revision 1.2  2005/04/13 12:59:12  nw
 checkin from branch desktop-nww-998

 Revision 1.1.2.2  2005/03/29 16:10:59  nw
 integration with the portal

 Revision 1.1.2.1  2005/03/18 12:09:31  nw
 got framework, builtin and system levels working.
 
 */