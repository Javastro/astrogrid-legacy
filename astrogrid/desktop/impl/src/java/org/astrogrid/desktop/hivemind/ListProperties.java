/**
 * 
 */
package org.astrogrid.desktop.hivemind;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.hivemind.Element;
import org.apache.hivemind.ErrorHandler;
import org.apache.hivemind.impl.DefaultErrorHandler;
import org.apache.hivemind.parse.ContributionDescriptor;
import org.apache.hivemind.parse.ModuleDescriptor;

/** Just lists all configuration properties, and exits.
 * @author Noel Winstanley
 * @since Jan 2, 20077:07:31 PM
 */
public class ListProperties extends Launcher {
	public void run() {
		spliceInDefaults();
		
		System.out.println("System Settings");
		Properties props = System.getProperties();
		props.list(System.out);
		System.out.println();
		System.out.println("System Defaults - can be overridden");
		Launcher.defaults.list(System.out);
		System.out.println();
		System.out.println("Module Defaults - can be overridden");
		ErrorHandler err = new DefaultErrorHandler();
		List descr = createModuleDescriptorProvider().getModuleDescriptors(err);
		for (Iterator i = descr.iterator(); i.hasNext(); ) {
			ModuleDescriptor md = (ModuleDescriptor) i.next();
			if (md.getModuleId().indexOf("hivemind") != -1) {
				// skip system modules.
				continue;
			}
			System.out.println();
			System.out.println(md.getModuleId());
			for (Iterator j = md.getContributions().iterator(); j.hasNext(); ) {
				ContributionDescriptor cd = (ContributionDescriptor)j.next();
				if (cd.getConfigurationId().equals("hivemind.FactoryDefaults")) {
					for (Iterator k = cd.getElements().iterator(); k.hasNext(); ) {
						Element e= (Element)k.next();
						System.out.println(e.getAttributeValue("symbol") + "=" + e.getAttributeValue("value"));
					}
				}
			}
		}
		System.out.println();
		System.out.println("Any service can be disabled by setting the property 'service.name.disabled");
		System.out.println("For example, to disable the rmi server, set 'system.rmi.disabled'");
		
	}
}
