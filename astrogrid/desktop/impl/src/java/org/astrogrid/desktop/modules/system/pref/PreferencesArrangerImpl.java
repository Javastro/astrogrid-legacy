/**
 * 
 */
package org.astrogrid.desktop.modules.system.pref;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.MultiHashMap;
import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.comparators.FixedOrderComparator;
import org.apache.commons.lang.StringUtils;

/**A small helper component that arranges the preferences contribution
 * into structures that are more user-friendly. This view can then be used tomake the 
 * preference dialogue, preference servlet, and any other preference view more
 * consistent.
 * 
 * <p>
 * maps between the impleemntation details of 'modules' to a more user-friendly
 * arrangement of preferences into 'categories'
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jan 26, 200712:25:30 PM
 */
public class PreferencesArrangerImpl implements PreferencesArranger  {

	/** sorts preferences by name
	 * @author Noel.Winstanley@manchester.ac.uk
	 * @since Jan 26, 200712:54:26 PM
	 */
	public static class PreferenceComparator implements Comparator {
		public int compare(Object arg0, Object arg1) {
			Preference a = (Preference) arg0;
			Preference b = (Preference) arg1;
			return a.getName().compareTo(b.getName());
		}
	}
	/** sorts modules into categories.
	 * @author Noel.Winstanley@manchester.ac.uk
	 * @since Jan 26, 200712:44:14 PM
	 */
	public static class CategoryNamesComparator extends FixedOrderComparator {
	    /**
         * hard-coded order. - fragile. Has to match with mapping in PerefesnsArrangerImpl constructor
         */
        public CategoryNamesComparator() {
            super(new String[]{
                    "System"
                    ,"General"
                    ,"Registry"
                        ,"Cds services"
                    ,"Network"
                    ,"Performance"
                    });
        }

	}
	
	public PreferencesArrangerImpl(List preferences) {
		if (preferences == null) {
			throw new IllegalArgumentException("Preferences List cannot be null!");
		}
		MultiMap advancedMulti = new MultiHashMap();
		MultiMap basicMulti = new MultiHashMap();
		//sort preferences by module, and then according to advancedNess
		for (Iterator i = preferences.iterator(); i.hasNext();) {
			Preference p = (Preference) i.next();
			// map names originating from implementation modules into something more userfriendly.   
			String moduleName = p.getModuleName();
			String key;
			if ("framework".equalsIgnoreCase(moduleName)) {
			    key = "system";
			} else if ("votech".equalsIgnoreCase(moduleName) || "ui".equalsIgnoreCase(moduleName) || "util".equalsIgnoreCase(moduleName) || "plastic".equalsIgnoreCase(moduleName)) {
			    key = "general";
			} else if ("ivoa".equalsIgnoreCase(moduleName)) {
			    key = "registry";
			} else if ("cds".equalsIgnoreCase(moduleName)) {
			    key = "cds services";
			} else {
			    key = moduleName;
			}
			if (p.isAdvanced()) {
				advancedMulti.put(StringUtils.capitalize(key), p);
			} else {
				basicMulti.put(StringUtils.capitalize(key),p);
			}
		}
		
		// create the result maps.
		Map basicResult = new HashMap(basicMulti.size());
		Map advancedResult = new HashMap(advancedMulti.size());
		//Comparator preferenceComparator = new PreferenceComparator();
		
		// popiulate
		for (Iterator i = basicMulti.entrySet().iterator(); i.hasNext();) {
			Map.Entry e = (Map.Entry) i.next();
			List l = new ArrayList((Collection)e.getValue());
			// sort the preferences for this category.
			// nope - decided it looks better without sorting.
			//Collections.sort(l, preferenceComparator);
			basicResult.put(e.getKey(), ListUtils.unmodifiableList(l));
		}
		for (Iterator i = advancedMulti.entrySet().iterator(); i.hasNext();) {
			Map.Entry e = (Map.Entry) i.next();
			List l = new ArrayList((Collection)e.getValue());
			// sort the preferences for this category.
			// nope. Collections.sort(l, preferenceComparator);
			advancedResult.put(e.getKey(), ListUtils.unmodifiableList(l));
		}		
		
		// freeze the result maps.
		basic = MapUtils.unmodifiableMap(basicResult);
		advanced = MapUtils.unmodifiableMap(advancedResult);
		/////////
		
		// compute a set of module names.
		Set names = new HashSet(basicMulti.keySet());
		names.addAll(advancedMulti.keySet());
		List cats = new ArrayList(names);
		// alphabetic sort, but with 'system' being first.
		Collections.sort(cats,new CategoryNamesComparator());
		categoryNames = ListUtils.unmodifiableList(cats);
	}

	/** return a list of preferences for a named category
	 * 
	 * @param categoryName
	 * @return never null. if an unknown category, or one with no preferences 
	 * at this level, returns an empty list.
	 */
	public List listBasicPreferencesForCategory(String categoryName) {
		List o = (List)basic.get(categoryName);
		return o == null ? ListUtils.EMPTY_LIST : o;
	}
	

	/** return a list of preferences for a named category.
	 * 
	 * @param categoryName
	 * @return never null. if an unknown category, or one with no preferences 
	 * at this level, returns an empty list.
	 */	
	public List listAdvancedPreferencesForCategory(String categoryName) {
		List o = (List)advanced.get(categoryName);
		return o == null ? ListUtils.EMPTY_LIST : o;		
	}
	
	/** list the preference categories, in preferred display order */
	public List listPreferenceCategories() {
		return this.categoryNames;
	}
	

private final Map basic ;
private final Map advanced ;
private final List categoryNames;

	
}
