/**
 * 
 */
package org.astrogrid.desktop.modules.system.pref;

import java.util.List;

/** Arranges the set of system preferences into a structrure that
 * is more digestible to a user.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jan 26, 20071:15:00 PM
 */
public interface PreferencesArranger {

	/** return a list of preferences for a named category
	 * 
	 * @param categoryName
	 * @return never null. if an unknown category, or one with no preferences 
	 * at this level, returns an empty list.
	 */
	public List listBasicPreferencesForCategory(String categoryName);

	/** return a list of preferences for a named category.
	 * 
	 * @param categoryName
	 * @return never null. if an unknown category, or one with no preferences 
	 * at this level, returns an empty list.
	 */
	public List listAdvancedPreferencesForCategory(String categoryName);

	/** list the preference categories, in preferred display order */
	public List listPreferenceCategories();

}