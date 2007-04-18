/**
 * 
 */
package org.astrogrid.desktop.hivemind;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.hivemind.ErrorHandler;
import org.apache.hivemind.Registry;
import org.apache.hivemind.impl.DefaultErrorHandler;
import org.apache.hivemind.impl.RegistryBuilder;
import org.apache.hivemind.service.impl.FactoryDefault;
import org.astrogrid.acr.builtin.Shutdown;
import org.astrogrid.desktop.modules.system.pref.Preference;

/** Just lists all configuration properties, and exits.
 * @author Noel Winstanley
 * @since Jan 2, 20077:07:31 PM
 */
public class ListProperties extends Launcher {
	public void run() {
		spliceInDefaults();
		ErrorHandler err = new DefaultErrorHandler();
		RegistryBuilder rb = new RegistryBuilder();
		rb.addModuleDescriptorProvider(createModuleDescriptorProvider());
		Registry registry = rb.constructRegistry(Locale.getDefault());
		List l = registry.getConfiguration("framework.preferences");
		
		System.out.println("Application Preferences");
		System.out.println("===============");	
		//@todo use the PreferencesArranger to get a more pleasing list here..
		for (Iterator i = l.iterator(); i.hasNext();) {
			Preference p = (Preference) i.next();
			System.out.println(p.getUiName() + p.getDescription());
			System.out.println( "     " + p.getName() +  " = " + p.getValue());
		}
		
		System.out.println();
		System.out.println("System Defaults");
		System.out.println("==========");
		Launcher.defaults.list(System.out);
		
		System.out.println();
		System.out.println("Module Defaults");
		System.out.println("==========");		
		
		l = registry.getConfiguration("hivemind.FactoryDefaults");
		for (Iterator i = l.iterator(); i.hasNext();) {
			FactoryDefault element = (FactoryDefault) i.next();
			if (element.getSymbol().startsWith("java") || element.getSymbol().startsWith("hivemind")) {
				continue;
			}
			System.out.println(element.getSymbol() + " = " + element.getValue());
		}
		
		System.out.println();
		System.out.println("Any of these opotions can be overridden on the commandline by using the -D name=value flag");
		System.out.println("Furthermore, any component can be disabled by setting the property 'service.name.disabled");
		System.out.println("For example, to disable rmi access to AR, set 'system.rmi.disabled'");
			
		Shutdown sd = (Shutdown)registry.getService(Shutdown.class);
		sd.reallyHalt();
	}

}
