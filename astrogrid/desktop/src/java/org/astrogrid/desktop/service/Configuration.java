/*$Id: Configuration.java,v 1.2 2005/02/22 01:10:31 nw Exp $
 * Created on 01-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.service;

import org.astrogrid.config.SimpleConfig;
import org.astrogrid.desktop.ui.Desktop;

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

import org.astrogrid.desktop.service.conversion.*;
import org.astrogrid.desktop.service.annotation.*;

/** Implementation of a configuration service - backed by java.util.prefs.
 * and keeps astrogrid's configuration system in-synch.
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Feb-2005
 *@@ServiceDoc("configuration","manages connection settings and configuration")
 */
public class Configuration implements PreferenceChangeListener {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(Configuration.class);

    /** Construct a new Configuration
     * 
     */
    public Configuration() {
        super();
        userPrefs = Preferences.userNodeForPackage(Desktop.class);
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
    public static final String[] REQUIRED_KEYS = {
        "org.astrogrid.registry.query.endpoint",
             "org.astrogrid.registry.admin.endpoint",
             // don't think this one is needed."org.astrogrid.community.ident",
             "jes.job.controller.endpoint",
            "org.astrogrid.community.default.vospace"
    };
        
    

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
$Log: Configuration.java,v $
Revision 1.2  2005/02/22 01:10:31  nw
enough of a prototype here to do a show-n-tell on.

Revision 1.1  2005/02/21 11:25:07  nw
first add to cvs
 
*/