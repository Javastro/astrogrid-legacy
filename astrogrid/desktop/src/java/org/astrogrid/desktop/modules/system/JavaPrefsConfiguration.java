/*$Id: JavaPrefsConfiguration.java,v 1.5 2005/07/08 11:08:01 nw Exp $
 * Created on 01-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system;

import org.astrogrid.acr.system.Configuration;
import org.astrogrid.config.SimpleConfig;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.BackingStoreException;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;

/** Implementation of a configuration service - backed by java.util.prefs.
 * and keeps astrogrid's configuration system in-synch.
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Feb-2005
 * @todo rework this class, to make modular.
 */
public class JavaPrefsConfiguration implements PreferenceChangeListener, Configuration {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(JavaPrefsConfiguration.class);

    /** Construct a new Configuration
     * 
     */
    public JavaPrefsConfiguration() {
        super();
        userPrefs = Preferences.userNodeForPackage(UIImpl.class);
        userPrefs.addPreferenceChangeListener(this);  
        maybePrepopulatePrefs();
        synchPrefs();
    }
    
    /**
     * 
     */
    private void maybePrepopulatePrefs() {
        //check whether any required keys are missing - if so, fill them in.
        try {
        List keys = Arrays.asList(userPrefs.keys());
        for (int i = 0; i < REQUIRED_KEYS.length; i++) {
            String key = REQUIRED_KEYS[i];
            if (! keys.contains(key)){
                logger.info("Missing key " + key + " must be configured");
                String value = System.getProperty("configuration." + key);
                userPrefs.put(key,value == null ? "fill this in" : value);
            }
        }
        } catch (Exception e) {
            logger.error("failed to prepopulate prefs",e);
        }
    }
    protected final Preferences userPrefs;
    
    /**@@MethodDoc("setKey","set a configuration key")
     @@.return ReturnDoc("Success code",rts=BooleanResultTransformerSet.getInstance())
     @@.key ParamDoc("key","Key to set");
     @@.value ParamDoc("value","Value to set to")
     */
    public boolean setKey(String key,String value) {
        userPrefs.put(key,value); // and simple config will be updated by callback.
        return true;
    }
    
    /**
     * @see org.astrogrid.acr.system.Configuration#removeKey(java.lang.String)
     */
    public void removeKey(String key) {
        userPrefs.remove(key);
    }
    
    /**@@MethodDoc("getKey","get a value for a confuguration key")
     * @@.return ReturnDoc("Key value")
     * @@.key ParamDoc("key","key to retrive value for")     
     */
    public String getKey(String key) {
        return userPrefs.get(key,null);
    }

    /**@@MethodDoc("listKeys","list configutation keys")
     * @@.return ReturnDoc("array of keys",rts=ArrayResultTransformerSet.getInstance())
     * */
    public String[] listKeys() throws BackingStoreException {
        return userPrefs.keys();
    }
    
    /** @@MethodDoc("list","list configuration entries")
     * @@.return ReturnDoc("map of key, value pairs",rts=StructResultTransformerSet.getInstance())
     * @return
     * @throws BackingStoreException
     */
    public Map list() throws BackingStoreException {
        Map m = new HashMap();
        String[] keys = userPrefs.keys();
        for (int i = 0; i < keys.length; i++) {
            m.put(keys[i],userPrefs.get(keys[i],null));
        }
        return m;
    }
    
    /** copy preferences into Config.. 
     * @throws BackingStoreException*/
    protected void synchPrefs()  {
        try {
        String[] keys = userPrefs.keys();
        for (int i = 0; i < keys.length; i++) {
            SimpleConfig.setProperty(keys[i],userPrefs.get(keys[i],null));
        }
        } catch (BackingStoreException e) {
            logger.error("Could not load prefs into simple config",e);
        }
    }

    /**
     * @see java.util.prefs.PreferenceChangeListener#preferenceChange(java.util.prefs.PreferenceChangeEvent)
     */
    public void preferenceChange(PreferenceChangeEvent evt) {
        SimpleConfig.setProperty(evt.getKey(),evt.getNewValue());
    }

 

    public void addPreferenceChangeListener(PreferenceChangeListener pcl) {
        this.userPrefs.addPreferenceChangeListener(pcl);
    }

 
}


/* 
$Log: JavaPrefsConfiguration.java,v $
Revision 1.5  2005/07/08 11:08:01  nw
bug fixes and polishing for the workshop

Revision 1.4  2005/06/08 14:51:59  clq2
1111

Revision 1.3.8.1  2005/06/02 14:34:32  nw
first release of application launcher

Revision 1.3  2005/04/27 13:42:41  clq2
1082

Revision 1.2.2.1  2005/04/25 11:18:51  nw
split component interfaces into separate package hierarchy
- improved documentation

Revision 1.2  2005/04/13 12:59:12  nw
checkin from branch desktop-nww-998

Revision 1.1.2.2  2005/03/29 16:10:59  nw
integration with the portal

Revision 1.1.2.1  2005/03/18 12:09:31  nw
got framework, builtin and system levels working.

Revision 1.2  2005/02/22 01:10:31  nw
enough of a prototype here to do a show-n-tell on.

Revision 1.1  2005/02/21 11:25:07  nw
first add to cvs
 
*/