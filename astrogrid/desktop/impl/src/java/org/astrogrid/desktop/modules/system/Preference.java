/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

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

	
	private String name;
	private String defaultValue;
	private boolean advanced;
	private boolean requiresRestart;
	private boolean propagateToConfig;
	private String units;
	private String uiName;
	private String description;
	private String value;
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
	public String toString() {
		return value == null ? defaultValue : value;
	}
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((this.name == null) ? 0 : this.name.hashCode());
		return result;
	}
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
	 * can also be used to provide formatting hints (e.g. http://..)
	 */
	public String getUnits() {
		return this.units;
	}
	public void setUnits(String units) {
		this.units = units;
	}
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
	
	

}
