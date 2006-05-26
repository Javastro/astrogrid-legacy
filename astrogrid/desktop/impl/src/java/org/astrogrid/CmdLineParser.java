/**
 * 
 */
package org.astrogrid;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.hivemind.Element;
import org.apache.hivemind.ErrorHandler;
import org.apache.hivemind.impl.DefaultErrorHandler;
import org.apache.hivemind.parse.ContributionDescriptor;
import org.apache.hivemind.parse.ModuleDescriptor;
import org.astrogrid.desktop.hivemind.Launcher;

/** Sets up the default commandline parser for all the different app variants.
 * @author Noel Winstanley
 * @since Apr 11, 200611:42:55 AM
 */
class CmdLineParser {
	private CmdLineParser() {
	}
	public static Options createDefaultOptions() {
		Options o = new Options();
		
		// properties
		o.addOption(OptionBuilder.withArgName( "property=value" )
        						.hasArg()
        						.withValueSeparator()
        						.withDescription( "use value for given property" )
        						.create( "D" ));
		o.addOption(OptionBuilder.withArgName("properties file")
						.hasArg()
						.withDescription("file location of properties file")
						.create("propertyFile"));
		o.addOption(OptionBuilder.withArgName("URL")
				.hasArg()
				.withDescription("URL location of properties file")
				.create("propertyURL"));		
		
		// additional modules.
		o.addOption(OptionBuilder.withArgName("module file")
					.hasArg()
					.withDescription("file location of additional module file")
					.create("addModule"));
		o.addOption(OptionBuilder.withArgName("URL")
			.hasArg()
			.withDescription("URL location of additional module file")
			.create("addModuleURL"));
		
		// other options.
		o.addOption(OptionBuilder.withDescription("Enable Debugging").create("debug"));
		o.addOption(OptionBuilder.withDescription("Print Help").create("help"));
		o.addOption(OptionBuilder.withDescription("List available properties").create("list"));
;
		
		return o;
	}

	/** atempts to parse commandline . 
	 * if it fails, or 'help' flag is passed in, prints an error message, 
	 * followed by usage information, then returns null.
	 * @param args commandline args passed in
	 * @param usage usage information for reporting to user.
	 * @param options options to parse
	 * @return a parsed commandline, or null if failed to parse.
	 */
	public static CommandLine parse(String[] args, String usage, Options options) {
		
		try {
			//CommandLine cl = (new PosixParser()).parse(options,args);
			CommandLine cl = (new BasicParser()).parse(options,args);
			if (cl.hasOption("help")) {
				showHelp(usage,options);
				return null;
			}
			
			  return cl;
			
		} catch (ParseException x) {
			System.out.println(x.getMessage());
			showHelp(usage,options);
			return null;
		}
	}
	
	public static void showHelp(String usage, Options options) {
		HelpFormatter f = new HelpFormatter();
		f.printHelp(usage,options);
	}
	
	public static void processCommandLine(CommandLine cl,Launcher l) {
		// process the debug flag first.
		if(cl.hasOption("debug")) {
			System.setProperty("acr.debug","true");
		}
		// process -D first.
		String[] props = cl.getOptionValues('D');
		if (props != null) { // irritating that this returns null..
			processPropertyKeys(props);
		}
		// now any properties files
		String[] propsFiles = cl.getOptionValues("propertyFile");
		if (propsFiles != null) {
			processPropertyFiles(propsFiles);
		}
		
		// now any properties URLs
		String[] propsURLs = cl.getOptionValues("propertyURL");
		if (propsURLs != null) {
			processPropertyURLs(propsURLs);
		}
		// now any modules in files
		String[] moduleFiles = cl.getOptionValues("addModule");
		if (moduleFiles != null) {
			processModuleFiles(l, moduleFiles);
		}
		
		// now any modules at URLs
		String[] moduleURLs = cl.getOptionValues("addModuleURL");
		if (moduleURLs != null ) {
			processModuleURLs(l, moduleURLs);		
		}
		// once we've loaded up everything, check whether we're to just list, or to go live.
		if (cl.hasOption("list")) {
			listConfiguration(l);
			System.exit(0); // urgh. best to do this for now.
		}
	}
	/** List all the configuration properties, etc in the currently configured launcher.
	 * @param l
	 */
	private static void listConfiguration(Launcher l) {
		System.out.println("System Settings");
		Properties props = System.getProperties();
		props.list(System.out);
		System.out.println();
		System.out.println("System Defaults - can be overridden");
		Launcher.defaults.list(System.out);
		System.out.println();
		System.out.println("Module Defaults - can be overridden");
		ErrorHandler err = new DefaultErrorHandler();
		List descr = l.createModuleDescriptorProvider().getModuleDescriptors(err);
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
	/**
	 * @param l
	 * @param moduleURLs
	 */
	private static void processModuleURLs(Launcher l, String[] moduleURLs) {
		for (int i =0; i < moduleURLs.length; i++) {
			try {
				l.addModuleURL(new URL(moduleURLs[i]));
			} catch (MalformedURLException x) {
				System.err.println("Warning: Could not access module file " + moduleURLs[i]);
			}
		}
	}
	/**
	 * @param l
	 * @param moduleFiles
	 */
	private static void processModuleFiles(Launcher l, String[] moduleFiles) {
		for (int i =0; i < moduleFiles.length; i++) {
			File f = new File(moduleFiles[i]); 
			//@todo check that this takes care of expanding, relatives, etc
			if (!f.exists()) {
				System.err.println("Warning: Ignoring non-existent module file " + f);
				continue;
			}
			try {
				l.addModuleURL(f.toURI().toURL());
			} catch (MalformedURLException x) {
				System.err.println("Warning: Could not access module file " + f);
			}
		}
	}
	/**
	 * @param propsURLs
	 */
	private static void processPropertyURLs(String[] propsURLs) {
		for (int i =0; i < propsURLs.length; i++) {
			URL u = null;

			InputStream is = null;
			try {
				u = new URL(propsURLs[i]); 
				is = u.openStream();
				loadPropertiesFile(is);
			} catch (IOException e) {
				System.err.println("Warning: Failed to load properties file " + u);
				e.printStackTrace(System.err);
			} finally {
				if (is != null ) {
					try {
						is.close();
					} catch (IOException e) {
						// don't care.
					}
				}
			}
		}
	}
	/**
	 * @param propsFiles
	 */
	private static void processPropertyFiles(String[] propsFiles) {
		for (int i =0; i < propsFiles.length; i++) {
			File f = new File(propsFiles[i]); 
			//@todo check that this takes care of expanding, relatives, etc
			if (!f.exists()) {
				System.err.println("Warning: Ignoring non-existent properties file " + f);
				continue;
			}
			InputStream is = null;
			try {
				is = new FileInputStream(f);
				loadPropertiesFile(is);
			} catch (IOException e) {
				System.err.println("Warning: Failed to load properties file " + f);
				e.printStackTrace(System.err);
			} finally {
				if (is != null ) {
					try {
						is.close();
					} catch (IOException e) {
						// don't care.
					}
				}
			}
		}
	}
	/**
	 * @param props
	 */
	private static void processPropertyKeys(String[] props) {
		for (int i = 0; i < props.length; i++) {
			String[] pair = props[i].split("=");
			if (pair.length != 2) {
				System.err.println("Warning: Ignoring malformed property " + props[i]);
				continue;
			}
			System.setProperty(pair[0],pair[1]);
		}
	}
	/**
	 * @param is
	 * @throws IOException
	 */
	private static void loadPropertiesFile(InputStream is) throws IOException {
		Properties p = new Properties();
		p.load(is);
		for (Iterator it = p.entrySet().iterator(); it.hasNext(); ) {
			Map.Entry entry = (Map.Entry)it.next();
			System.setProperty(entry.getKey().toString(),entry.getValue().toString());
		}
	}
	
}
