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
import java.util.Map;
import java.util.Properties;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.astrogrid.desktop.hivemind.GenerateHivedoc;
import org.astrogrid.desktop.hivemind.Launcher;
import org.astrogrid.desktop.hivemind.ListProperties;

/** General Commandline Parser for different variants of AR
 * 
 * Usage.
 * <ol>
 * <li>Create: parser = new CmdLineParser()
 * <li>Optional - declare additional commandline args: parser.getOptions();
 * <li>Parse to create Launcher - Launcher l = parser.parse(args, "app name");
 * <br> This will return a launcher, or a specialzed subclass.
 * <li>Optional - Configure the launcher instance further, process any additional commandline args: 
 * <li>process the rest of the commandline - parser.processCommandLine(l)
 * <li>Execute the launcher - l.run() 
 * </ol>
 * @author Noel Winstanley
 * @since Apr 11, 200611:42:55 AM
 */
class CmdLineParser {
	/** Custom subclass of Launcher that just displays the commandline help and exits.
	 */
	final class ShowHelp extends Launcher {
		/**
		 * 
		 */
		private final String usage;

		/**
		 * @param usage
		 */
		public ShowHelp(String usage) {
			this.usage = usage;
		}

		public void run() {
			spliceInDefaults();
			HelpFormatter f = new HelpFormatter();
			f.printHelp(this.usage,options);						
		}
	}
	
	public CmdLineParser() {
		options = createDefaultOptions();
	}
	
	 final Options options;
	
	/** access the options - to add new flags, etc. */
	public Options getOptions() {
		return options;
	}
	private CommandLine commandLine;
	
	public CommandLine getCommandLine() {
		if (commandLine == null) {
			throw new IllegalStateException("No commandline parsed yet");
		}
		return commandLine;
	}
	
	/** set of default commandline options
	 * @todo come back to these later 
	 * @return parsed up options
	 */
	private static Options createDefaultOptions() {
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
		o.addOption(OptionBuilder.withDescription("Show help and exit").create("help"));
		o.addOption(OptionBuilder.withDescription("List configuration properties and exit").create("list"));
		o.addOption(OptionBuilder.withDescription("Generate Hivedoc and exit").create("hivedoc"));
		return o;
	}

	/** parse the commandline, and return a suitable instance of 
	 * Launcher depending on the options provided (and their correctness).
	 * After this, call {@link #processCommandLine(Launcher)}
	 * @param args commandline arguments
	 * @param usage - usage documentation string
	 * @return an instance of launcher. never null.
	 */
	public Launcher parse(final String[] args, final String usage) {
		final BasicParser parser = new BasicParser();
		try {
			commandLine = parser.parse(options,args);
			if (commandLine.hasOption("help")) {
				return new ShowHelp(usage);
			} else if (commandLine.hasOption("hivedoc")) {
				return new GenerateHivedoc();
			} else if (commandLine.hasOption("list")) {
				return new ListProperties();
			} else {
				return new Launcher();
			}
		} catch (ParseException x) {
			// set it to 'null'
			try {
			commandLine = parser.parse(options,new String[]{});
			} catch (ParseException xx) {
				throw new RuntimeException("Unexpected",xx);
			}
			System.out.println(x.getMessage());
			return new ShowHelp(usage);
		}
	}

	public void processCommandLine(Launcher l) {
		// process the debug flag first.
		//@todo remove this option - not supporting todo any more.
		if(commandLine.hasOption("debug")) {
			System.setProperty("acr.debug","true");
		}
		// process -D first.
		String[] props = commandLine.getOptionValues('D');
		if (props != null) { // irritating that this returns null..
			processPropertyKeys(props);
		}
		// now any properties files
		String[] propsFiles = commandLine.getOptionValues("propertyFile");
		if (propsFiles != null) {
			processPropertyFiles(propsFiles);
		}
		
		// now any properties URLs
		String[] propsURLs = commandLine.getOptionValues("propertyURL");
		if (propsURLs != null) {
			processPropertyURLs(propsURLs);
		}
		// now any modules in files
		String[] moduleFiles = commandLine.getOptionValues("addModule");
		if (moduleFiles != null) {
			processModuleFiles(l, moduleFiles);
		}
		
		// now any modules at URLs
		String[] moduleURLs = commandLine.getOptionValues("addModuleURL");
		if (moduleURLs != null ) {
			processModuleURLs(l, moduleURLs);		
		}
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
