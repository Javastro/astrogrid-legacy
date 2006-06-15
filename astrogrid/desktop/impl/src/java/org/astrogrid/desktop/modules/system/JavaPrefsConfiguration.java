/*$Id: JavaPrefsConfiguration.java,v 1.6 2006/06/15 09:52:48 nw Exp $
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.prefs.BackingStoreException;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hivemind.service.impl.FactoryDefault;
import org.astrogrid.Workbench;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.config.SimpleConfig;

/** Implementation of a configuration service - backed by java.util.prefs.
 * , uses defaults provided by hivemiind system (including sys.properties).
 * and keeps astrogrid's configuration system in-synch.
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Feb-2005
 */
public class JavaPrefsConfiguration implements PreferenceChangeListener,ConfigurationInternal{
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(JavaPrefsConfiguration.class);

    /** Construct a new Configuration
     * 
     */
    public JavaPrefsConfiguration(List factoryDefaults,List appDefaults) {
        super();
        this.defaults = buildProperties(factoryDefaults,appDefaults);
    }
    
    private final  Properties defaults;

    private final Properties buildProperties(final List fac,final List app) {
        Properties p = new Properties();
        for (Iterator i = fac.iterator(); i.hasNext(); ) {
            FactoryDefault d = (FactoryDefault)i.next();
            p.setProperty(d.getSymbol(),d.getValue());
        }
    
        // now system properties.
        for (Iterator i = System.getProperties().entrySet().iterator(); i.hasNext(); ) {
            Map.Entry e = (Map.Entry)i.next();
            p.setProperty((String)e.getKey(),(String)e.getValue());            
        }
        // now any overrides...
        for (Iterator i = app.iterator(); i.hasNext(); ) {
            FactoryDefault d = (FactoryDefault)i.next();
            p.setProperty(d.getSymbol(),d.getValue());
        }            
        return p;
    }
    
    
    // @todo change preference location to something more meaningful - i.e. start class of system.
    // need to take care to refer to classes that are always going to be on classpath.
    private Class preferenceClass = Workbench.class;
    
    /**
     * 
     */
    public void init() {
        userPrefs = Preferences.userNodeForPackage(preferenceClass);        
        userPrefs.addPreferenceChangeListener(this);  
        synchPrefs();
    }
    
 
    protected Preferences userPrefs;
    

    public boolean setKey(String key,String value) {
        userPrefs.put(key,value); // and simple config will be updated by callback.
        return true;
    }
    
    /**
     * @see org.astrogrid.acr.system.Configuration#removeKey(java.lang.String)
     */
    public void removeKey(String key) {
        userPrefs.remove(key); // will revert to defaults, if any.
    }
    
 
    public String getKey(String key) {
        return userPrefs.get(key,defaults.getProperty(key));
    }

    public String[] listKeys() throws ACRException {
        
        try {
            return userPrefs.keys();
        } catch (BackingStoreException e) {
            throw new ACRException(e);
        }
    }
    

    public Map list() throws ACRException {
        Map m = new HashMap();
        String[] keys;
        try {
            keys = userPrefs.keys();
        } catch (BackingStoreException e) {
            throw new ACRException(e);
        }
        for (int i = 0; i < keys.length; i++) {
            m.put(keys[i],userPrefs.get(keys[i],null));
        }
        return m;
    }

    private void synchPrefs()  {
        for (Iterator i = defaults.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry e = (Map.Entry)i.next();
            SimpleConfig.setProperty((String)e.getKey(),(String)e.getValue());
        }
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

 
    //unused

//    public void addPreferenceChangeListener(PreferenceChangeListener pcl) {
 //       this.userPrefs.addPreferenceChangeListener(pcl);
  //  }


 

    /**
     * @throws ServiceException
     * @see org.astrogrid.desktop.modules.system.ConfigurationInternal#reset()
     */
    public void reset() throws ServiceException {
        try {        
            userPrefs.removePreferenceChangeListener(this);
        userPrefs.removeNode();
        userPrefs.flush();        
        this.init();
        } catch (BackingStoreException e) {
            throw new ServiceException(e);
        }
    }

 

    /** startup configuration property - determines where to look in java prefs registry */ 
    public void setPreferenceClass(Class preferenceClass) {
        this.preferenceClass = preferenceClass;
    }

 
}


/* 
$Log: JavaPrefsConfiguration.java,v $
Revision 1.6  2006/06/15 09:52:48  nw
cleaned up

Revision 1.5  2006/04/21 13:48:12  nw
mroe code changes. organized impoerts to reduce x-package linkage.

Revision 1.4  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.3.42.3  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.3.42.2  2006/04/04 10:31:26  nw
preparing to move to mac.

Revision 1.3.42.1  2006/03/22 18:01:30  nw
merges from head, and snapshot of development

Revision 1.3  2005/11/01 09:19:46  nw
messsaging for applicaitons.

Revision 1.2  2005/08/25 16:59:58  nw
1.1-beta-3

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.6  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

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