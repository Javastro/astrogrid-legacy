/**
 * 
 */
package org.astrogrid.desktop.modules.system.pref;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.BackingStoreException;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;

import org.apache.hivemind.Location;
import org.apache.hivemind.SymbolSource;
import org.apache.hivemind.internal.Module;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.desktop.SplashWindow;
import org.astrogrid.desktop.modules.system.ConfigurationInternal;

/** Provides access to configuration keys, manages and propagates their edits.
 * <h2>Role</h2>
 * This is a tricky class, complicated by having to fit in with the ag.commons.Config
 * system (martin) used by the other AG components. Config is ok, but is tied to 
 * a config file on the classpath - doesn't provide convenient ways to display or edit
 * properties at runtime - so not well suited to client-side deployment.
 * <p />
 * This class builds on top of the previous Configuration interface, which provided 
 * a key-value interface into a property store (implemented by java.util.prefs).
 * This allowed mutability of properties, and propagated changes through to the 
 * ag.commons.Config system via a property listener. However, the properties 
 * being manipulated in the store weren't documented anywhere, making it 
 * hard to construct a UI over the top of them. Furthermore, any change to a 
 * configuration property required a restart before it took effect (as components
 * only queried the property store on startup).
 * 
 *<h2>Preference System</h2>
 * 
 * This class provides a OO layer over the key-value interface of Configuration.
 * The {@link Preference} object provides access to the key and value properties,
 * plus further fields that can be used to construct a GUI / web form / report. 
 * <p />
 * Furthermore, the preference object notifies clients (thorugh a standard property
 * change listener) whenever it's inner value is changed. This means that clients that
 * register a listener with the propery object can refresh once a property has been modified.
 * Altenately, services can just retrieve the value from the property whenever needed, 
 * without storing the value anywhere - again, this ensure the latest value is always used.
 * <p />
 * Preference objects are defined in the hivemind descriptors for each service - 
 * this acts as documentation on what configuration is possible for each service.
 * The preferred way to access a preference is through the <tt>preference:</tt>
 * ObjectProvider in the hivemind descirptor - this means a service is only passed the 
 * preferences it requires, rather than having access to the preference manager itself
 * <p />
 * However, this class also implements a hivemind symbol source - this means that
 * preference values can be accessed in the hivemind descriptors using the ${name}
 * notation - note, however, that this means that updates to these preference values
 * can't propagate to the service that's using htem - and so a restart is required.

 *<h2>Configuration System</h2>
 * This class still provides the key-value interface that was provided by Configuration.
 * This is useful api for access through AR - where passing preference objects is
 * a bit heavyweight. Modifications to preferences through the configuration key-value
 * interface cause the preference object to be updated and fire a notification.
 * <p />
 * Although the Configuration api is not the preferred way  to access
 * preferences, it is useful for code that needs to persist other small bits of state
 * information, <b>that is not user-editable, or shared between more than one component</b> 
 * (for either of these, use a preference object). Examples of valid uses include
 *  x,y positions of windows,  recent searches, previous selections in combo boxes, etc.
 *   
 *  <h2> AG Config System</h2>
 *  The previous configuration system propagated all changes through to the SimpleConfig
 *  class used by the AG delegates. This class propagates changes only for properties
 *  marked 'propagate-to-config'. This reduces the amounts of update performed, 
 *  and makes it clearer which values are being read, from where, by what. 
 *  
 *  <h2>Defaults</h2>
 *  Values from hivemind.FactoryDefaults and hivemind.ApplicationDefailts are not
 *  available through the Configuration interface now. However, all user-servicable
 *  properties have moved to Preferences. The defaults configurations store more technical 
 *  settings that generally shouldn't be fiddled with - however, these can be set via a properties file
 *  or extension hivemind descriptor
 *  
 *  Another change from the old system was that any keys set in the hivemind.factoryDefaults
 *  and hivemind.ApplicationDefaults were also copied into the ag.common.Config system.
 *  This is not the case now -  only some specified preferecnes are copied into the config system.
 *  
 *  defaults should be specified using the defaultValue property
 *  of a preference. 
 * 
 * 
 * @author Noel Winstanley
 * @since Jan 3, 20071:49:28 PM
 */
public class PreferenceManagerImpl implements  ConfigurationInternal,  PreferenceChangeListener, PropertyChangeListener {
	/**
	 * construct a new preferences manager
	 * @param prefs map of preferences to manage and persist.
	 * @param preferencesRoot a list, containing a single item, which must be a class object.
	 * (yuk) necessary to do this to fit in with hivemind. The class is used as the root under which
	 * to persist properties in the preferences store.
	 * @param sysPreferences source of system properties.
	 */
	public PreferenceManagerImpl(Map<String, Preference> prefs, List<Object> preferencesRoot, SymbolSource sysPreferences) {
	    SplashWindow.reportProgress("Loading Preferences...");
		if (prefs == null || sysPreferences == null) {
			throw new IllegalArgumentException("null parameter");
		}
		this.preferenceObjectMap = prefs;
		this.systemProperties = sysPreferences;
		if (preferencesRoot.size() < 1) {
			throw new IllegalArgumentException("No preferences root class specified");
		}
		Object clazz = preferencesRoot.get(0);
		if (! (clazz instanceof Class)) {
			throw new IllegalArgumentException("incorrect type - no root class provided:" + clazz);
		}

		preferenceRegistry = Preferences.userNodeForPackage((Class)clazz);
		// populate preferences and registry from each other
		for (Iterator<Preference> i = prefs.values().iterator(); i.hasNext(); ) {
			Preference p = i.next();
			initialize(p);
		}
		// finally listen to further changes from the store too.
		preferenceRegistry.addPreferenceChangeListener(this);
	}

	/** initializes a single preference.
	 * @param p
	 */
	private void initialize(Preference p) {
		String val = preferenceRegistry.get(p.getName(),null);
		if (val == null) {// preference is unknown to store - copy default into store
			val = p.getDefaultValue();
			preferenceRegistry.put(p.getName(), val);
		}
		
		// in all cases, val is non-null and now contains the same value as is in the store.
		
		// now system properties (anything passed in on commandline) take precedence
		// but aren't persisted - as may only be there for this session.
		//NB - this might be a problem when we do a configuration dialogue??
		String sysVal = systemProperties.valueForSymbol(p.getName());		
		
		p.setValue(sysVal != null ? sysVal : val);
		// register synch - so any further changes to this property are managed.
		p.addPropertyChangeListener(this);

		// finally, if requested, copy this properrty value into the ag.common.Config system
		if (p.isPropagateToConfig()) {
			SimpleConfig.setProperty(p.getName(),p.getValue());				
		}
	}

	protected final Map<String, Preference> preferenceObjectMap;
	protected final Preferences preferenceRegistry;
	protected final SymbolSource systemProperties;

//	configurationInternalinterface
	/** finds a named preference.
	 * @param name
	 * @return a preference object. will never return null.
	 * @throws IllegalArgumentException if named preference is not found.
	 */
	public Preference find(String name) throws IllegalArgumentException {
		if (name == null) {
			throw new IllegalArgumentException("Null preference name");
		}
		Preference o = preferenceObjectMap.get(name);
		if (o == null) {
			throw new IllegalArgumentException("Preference not found: " + name);
		}
		return o;

	}

	public void reset() throws ServiceException {
		try {        
			preferenceRegistry.clear();
			preferenceRegistry.flush();        
			// and this should fire all the events needed to keep all in synch
		} catch (BackingStoreException e) {
			throw new ServiceException(e);
		}		
	}

//	configuration interface.
	public String getKey(String key) {
		if (isPreference(key)) {
			return find(key).toString(); // this gives default values and overrides.
		} else {
			// is not a preference, must be a key cached by something else.
			return preferenceRegistry.get(key, null);
		}
	}

	public Map<String, String> list() throws ACRException {
		Map<String, String> m = new LinkedHashMap<String, String>();
		String[] keys;
		try {
			keys = preferenceRegistry.keys();
		} catch (BackingStoreException e) {
			throw new ACRException(e);
		}
		for (int i = 0; i < keys.length; i++) {
			String key = keys[i];
			m.put(key,getKey(key));
		}
		return m;
 	}

	public String[] listKeys() throws ACRException {
		try {
			return preferenceRegistry.keys();
		} catch (BackingStoreException e) {
			throw new ACRException(e);
		}
	}

	public void removeKey(String arg0) {
		if (arg0 == null) {
			return; // ignore.
		}
		preferenceRegistry.remove(arg0); // notifies of change.
	}

	public boolean setKey(String arg0, String arg1) {
		if (arg0 == null || arg1 == null ) {
			return false;
		}
		preferenceRegistry.put(arg0, arg1); // everything else notified via callbacks
		return true;
	}

//	object provider interface
	public Object provideObject(Module module, Class expectedType, String prefName, Location loc) {
		Preference o = find(prefName);
		if (expectedType.equals(String.class)) {
			return o.toString();
		} else {
			return o;
		}
	}
	private boolean isPreference(String name) {
		return preferenceObjectMap.containsKey(name);
	}

//	symbol source interface. - returns preferences, and other configuration keys.
	public String valueForSymbol(String prefName) {
		if ( isPreference(prefName) ) {
			return find(prefName).toString();
		}
		return preferenceRegistry.get(prefName,null);
	}
//	synch	 interfaces

	/** setValue() called on a preference object - map into the store. */
	public void propertyChange(PropertyChangeEvent evt) {
		Preference p = (Preference) evt.getSource();
		String val = p.getValue(); // will never be null.
		String oldVal = preferenceRegistry.get(p.getName(),null);
		if (! val.equals(oldVal)) { // a difference
			preferenceRegistry.put(p.getName(),val); // which in turn will trigger other updates.
		}
	}
	/** listens for a propery change, 
	 * map into preference object,
	 * and if it's of interest, propagates
	 * it into config.
	 */
	public void preferenceChange(PreferenceChangeEvent evt) {
		String name = evt.getKey();
		Preference p = preferenceObjectMap.get(name);
		if (p == null ) { // not a preference - just some other property.
			return;
		}
		String value = evt.getNewValue();
		if (value == null) { // indicates a 'delete'
			// probably a reset - so need to repopulate again.
			// ignore sysproperties at this point (can't make them stick, as writing
			// the standard default causes another event to be fired.)
			value = p.getDefaultValue(); // not =  getDefault(p)
		} 
		if (! value.equals(p.getValue())) { // a difference.
			p.setValue(value);  // which will fire a proprty change event.
		}
		if (p.isPropagateToConfig()) {
			SimpleConfig.setProperty(name,value); 
		}
	}




}
