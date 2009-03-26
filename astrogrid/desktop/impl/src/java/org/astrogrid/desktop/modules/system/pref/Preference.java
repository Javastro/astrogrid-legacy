/**
 * 
 */
package org.astrogrid.desktop.modules.system.pref;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.hivemind.internal.Module;

/** Representation of a single preference value.
 * 
 * Provides access to the name, value and defaultValue of the preference.
 * where defaultValue is specified by the app-writer, and value might
 * be modified by a system property or user setting.
 * 
 * getValue() always returns the freshest value of this preference - i.e. 
 * it updates if this preference is updated in the store. Hence, it's preferable
 * to not take a local copy of the value, but instead refer to getValue() as needed.
 * 
 * ifPropagateToConfig() controls the <i>scope</i> of this preference - whether it's
 * mapped to the ag.common.Config system.
 * 
 * Also provides fields suitable for describing the preference in a form / report / UI - 
 * uiName, description, units, advanced, requiresRestart.
 * 
 * Finally, provides a notification system using standard propertyChangeListeners.
 * an event is fired wheneveer the value of the property is modifed. This is useful for 
 * long-running services which need to restart / reconfigure when a preference they
 * depend upon changes.
 * 
 * @author Noel Winstanley
 * @since Jan 3, 20073:07:04 PM
 */
public class Preference {

	private List alternatives = new ArrayList();
	private List options = new ArrayList();	
	private String name;
	private String defaultValue;
	private boolean advanced;
	private boolean requiresRestart;
	private boolean propagateToConfig;
	private String units;
	private String uiName;
	private String description;
	private String value;
	private String moduleName;
	private String helpId;
	
	
	public void setModule(Module m) {
		moduleName = m.getModuleId();
	}
	
	// only for ease of testing.
	void setModuleName(String s) {
		moduleName = s;
	}
	
	/** names the module to which this preference belongs */
	public String getModuleName() {
		return moduleName;
	}
	
	/** list possible alternative values
	 * @return an array of alternatives, or an empty array if none provided.
	 * never null. */
	public String[] getAlternatives() {
		return (String[]) alternatives.toArray(new String[alternatives.size()]);
	}
	
	public void addAlternative (String s) {
		alternatives.add(s);
	}
	
	/** list all possible alternative values, based on alternatives, default and current
	 * values
	 * @return an array of suggestions, no duplicates,where first one will be the current value
	 */
	public String[] getAllAlternatives() {
		Set s= new HashSet(); // use a set to merge recommendations, current, default.
		s.addAll(Arrays.asList(getAlternatives()));
		s.add(getDefaultValue());
		s.remove(getValue()); // if it's already there.
		List l = new ArrayList();
		l.add(getValue());
		l.addAll(s);
		return (String[]) l.toArray(new String[l.size()]);
	}
		
	/** lists permitted option values
	 * @return an array of options, or an empty array if none provided.
	 * never null. */
	public String[] getOptions() {
		return (String[]) options.toArray(new String[options.size()]);
	}
	
	public void addOption (String s) {
		options.add(s);
	}	
	
	/** true if this is considered an 'advanced' option which could be hidden from
	 * the users.
	 */
	public boolean isAdvanced() {
		return this.advanced;
	}
	public void setAdvanced(boolean advanced) {
		this.advanced = advanced;
	}
	/** humand readable description of this preference */
	public String getDescription() {
		return this.description;
	}
	public void setDescription(String decription) {
		this.description = decription;
	}
	/** default value of this preference */
	public String getDefaultValue() {
		return this.defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	/** name (key) of this preference */
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	/** true if changes to this preference may require restart before they
	 * take effect on the systm
	 */
	public boolean isRequiresRestart() {
		return this.requiresRestart;
	}
	public void setRequiresRestart(boolean restartRequired) {
		this.requiresRestart = restartRequired;
	}
	/** a nice tiutle for the preference */
	public String getUiName() {
		return this.uiName;
	}
	public void setUiName(String uiName) {
		this.uiName = uiName;
	}
	/** the value of this preference 
	 * 
	 * @return the live value of the preference - will  have updated if
	 * anything modifies the store
	 */
	public String getValue() {
		return this.value;
	}
	
	/**
	 * Fires a
	 * property change event into the specified listener. The event
	 * contains the current value of this preference.
	 * 
	 * This is a helper method that makes client code simpler - there's no need to initialize
	 * the client by querying the preference value, and then repeat the code in a listener.
	 * Instead, the code can just occur in the listener, and then on construction this
	 * method can be called to cause the client to be initialized.
	 * The event fired with have oldValue==null, and newValue == this.getValue();
	 * @param p
	 */
	public void initializeThroughListener(PropertyChangeListener p) {
		PropertyChangeEvent evt = new PropertyChangeEvent(this,"value",null,this.value);
		p.propertyChange(evt);
	}
	
	/** access the value of the preference as a boolean */
	public boolean asBoolean() {
		// 1.5 specific return Boolean.parseBoolean(getValue());
		return Boolean.valueOf(getValue()).booleanValue();
	}
	public void setValue(String nuValue) {
		if (nuValue == null || nuValue.equals(this.value)) {
			// ignore. don't want nulls, or no change.
			return;
		}
		String old = this.value;
		this.value = nuValue;
		support.firePropertyChange("value",old,this.value);
	}
	
	/** returns the value of the preference, as a string */
	@Override
    public String toString() {
		return value == null ? defaultValue : value;
	}
	@Override
    public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((this.name == null) ? 0 : this.name.hashCode());
		return result;
	}
	@Override
    public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Preference other = (Preference) obj;
		if (this.name == null) {
			if (other.name != null)
				return false;
		} else if (!this.name.equals(other.name))
			return false;
		return true;
	}
	
	private final PropertyChangeSupport support = new PropertyChangeSupport(this);
	/** register for notifications when the value of this property changes */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.support.addPropertyChangeListener(listener);
	}
	/** unregister for notifications */
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.support.removePropertyChangeListener(listener);
	}
	/** a description of the units (e.g. seconds, ) this property is expected in. 
	 * can also be set to one of the constants in this class - FILE, URL, NUMBER - in which case an editor could 
	 * be expected to provide different ui capabilities.
	 */
	public String getUnits() {
		return this.units;
	}
	public void setUnits(String units) {
		this.units = units;
	}
	
	// constants for UNITS property
	public static final String FILE = "file";
	public static final String DIRECTORY = "directory";
	public static final String URL = "url";
	public static final String NUMBER = "number";
	public static final String SECONDS = "seconds"; // treat as equivalent to number
	public static final String BOOLEAN = "boolean";
	
	/**
	 * 					true if the default value, and all modifications of this prefernece
					should be copied into the ag.commons.Config used by other 
					AG components (such as delegates).
					If false, this preference is not visible to such code.
	 */
	public boolean isPropagateToConfig() {
		return this.propagateToConfig;
	}
	public void setPropagateToConfig(boolean propagateToConfig) {
		this.propagateToConfig = propagateToConfig;
	}

	/** an optional HelpID to link to for further help */
	public String getHelpId() {
		return this.helpId;
	}

	public void setHelpId(String helpId) {
		this.helpId = helpId;
	}
	
	

}
