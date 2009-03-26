/**
 * 
 */
package org.astrogrid.desktop.hivemind;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.hivemind.Registry;
import org.apache.hivemind.impl.RegistryBuilder;
import org.apache.hivemind.service.impl.FactoryDefault;
import org.astrogrid.acr.builtin.Shutdown;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.system.pref.PreferencesArranger;
import org.astrogrid.desktop.modules.system.pref.PreferencesArrangerImpl;

/** Generate Configuration documentation.
 * 
 * <p/>
 * subclass of launcher which just lists all configuration keys and then exits.
 * @author Noel Winstanley
 * @since Jan 2, 20077:07:31 PM
 */
public class ListProperties extends Launcher {
	@Override
    public void run() {
		spliceInDefaults();
		//ErrorHandler err = new DefaultErrorHandler();
		final RegistryBuilder rb = new RegistryBuilder();
		rb.addModuleDescriptorProvider(createModuleDescriptorProvider());
		final Registry registry = rb.constructRegistry(Locale.getDefault());
		final PreferencesArranger arranger = new PreferencesArrangerImpl(registry.getConfiguration("framework.preferences"));

		printHeader();
		
		for (final Iterator cats = arranger.listPreferenceCategories().iterator(); cats.hasNext(); ) {
			final String categoryName = (String)cats.next();
			System.out.println();
			System.out.println();
			printCategoryHeader(categoryName);

			for (final Iterator i = arranger.listBasicPreferencesForCategory(categoryName).iterator(); i.hasNext();) {
				System.out.println();
				final Preference p = (Preference) i.next();
				printPreference(p);
			}

			for (final Iterator i = arranger.listAdvancedPreferencesForCategory(categoryName).iterator(); i.hasNext();) {
				System.out.println();
				final Preference p = (Preference) i.next();
				printPreference(p);
			}			
		}
		
		System.out.println();
		System.out.println();
		printCategoryHeader("System defaults");
		for (final Iterator i = Launcher.defaults.entrySet().iterator(); i.hasNext();) {
			final Map.Entry e = (Map.Entry) i.next();
			System.out.println("#" + e.getKey() +  "=" + e.getValue());
		}

		System.out.println();
		System.out.println();
		printCategoryHeader("Other configuration settings");			
		for (final Iterator i = registry.getConfiguration("hivemind.FactoryDefaults").iterator(); i.hasNext();) {
			final FactoryDefault element = (FactoryDefault) i.next();
			if (element.getSymbol().startsWith("java") || element.getSymbol().startsWith("hivemind")) {
				continue;
			}
			System.out.println("#" + element.getSymbol() + "=" + element.getValue());
		}
		System.out.println();
		
		final Shutdown sd = (Shutdown)registry.getService(Shutdown.class);
		sd.reallyHalt();
	}

	/**
	 * @param categoryName
	 */
	private void printCategoryHeader(final String categoryName) {
		System.out.println("###########################");
		System.out.println("## " + categoryName);
		System.out.println("###########################");
		
	}

	private void printPreference(final Preference p) {
		System.out.println("## " + p.getUiName() + " ##");
		System.out.println("## " +  p.getDescription());
		System.out.println("## Default value: " + p.getDefaultValue());
		final String[] opts = p.getAlternatives();
		if (opts != null && opts.length > 0) {
			System.out.println("## Suggested alternatives: " + ArrayUtils.toString(opts));
		}
		System.out.println( "#" + p.getName() +  "=" + p.getValue());
	}

	private final void printHeader() {
		printCategoryHeader("Listing Configuration");
		System.out.println("#The description and default value for each configuration key are listed");
		System.out.println("#followed by the configuration key name and current value");
		System.out.println();
		System.out.println("#Any of these keys can be temporarily overridden by passing a -Dname=value to the JVM");
		System.out.println("#Alternately, save this output to a file, uncomment and edit properties,") ;
		System.out.println("and then pass it to AR using the commandline options --propertiesFile or --propertiesURL");
		System.out.println();
		System.out.println("#Furthermore, many components can be disabled by setting the property '<service.name>.disabled");
		System.out.println("#For example, to disable rmi access to AR, set system.rmi.disabled=true");
		System.out.println();
		System.out.println("#Many of these configuration keys can also be edited from within the UI (Preferences Dialogue)");
		System.out.println("#or through the HTML interface (Preferences) link. Changes made using these interfaces persist");
		System.out.println("#and take effect for subsequent AR runs");
	}
}
