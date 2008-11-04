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

import javax.swing.RepaintManager;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.desktop.hivemind.GenerateHivedoc;
import org.astrogrid.desktop.hivemind.Launcher;
import org.astrogrid.desktop.hivemind.ListProperties;
import org.jdesktop.swinghelper.debug.CheckThreadViolationRepaintManager;

/** Commandline Parser for AR / VODesktop
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
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog("startup");

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
		public ShowHelp(final String usage) {
			this.usage = usage;
		}

		public void run() {
			spliceInDefaults();
			final HelpFormatter f = new HelpFormatter();
			f.printHelp(this.usage + " <options>",options);						
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
	 * @return parsed up options
	 */
	private static Options createDefaultOptions() {
		final Options o = new Options();
		o.addOption(OptionBuilder.withDescription("Shows this help and exit").create("help"));
		
		// properties
		/* Disabled - on advice from dave - use java's -D instead.
		o.addOption(OptionBuilder.withArgName( "option=value" )
        						.hasArg()
        						.withValueSeparator()
        						.withDescription( "set a configuration option (see --list for available options). " +
        								"Don't forget a space between the -D and opt=value" )
        						.create( "D" ));
		*/
		o.addOption(OptionBuilder.withDescription("Run in Headless AR-only mode (no user interface)")
		            .create("headless"));
		o.addOption(OptionBuilder.withArgName("properties file")
						.hasArg()
						.withDescription("file location of properties file")
						.create("propertyFile"));
		
		o.addOption(OptionBuilder.withDescription("List configuration properties and exit").create("list"));
				
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
		
		o.addOption(OptionBuilder.withDescription("Enable UI Debugging").create("debugEDT"));
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
			if (commandLine.hasOption("headless")) {
			    System.setProperty("java.awt.headless","true");
			}
			if (commandLine.hasOption("help")) {
				return new ShowHelp(usage);
			} else if (commandLine.hasOption("hivedoc")) {
				return new GenerateHivedoc();
			} else if (commandLine.hasOption("list")) {
				return new ListProperties();
			} else {
				return new Launcher();
			}
		} catch (final ParseException x) {
			// set it to 'null'
			try {
			commandLine = parser.parse(options,new String[]{});
			} catch (final ParseException xx) {
				throw new RuntimeException("Unexpected",xx);
			}
			logger.error("Failed to parse",x);
			return new ShowHelp(usage);
		}
	}

	public void processCommandLine(final Launcher l) {

		// process -D first.
	//	String[] props = commandLine.getOptionValues('D');
	//	if (props != null) { // irritating that this returns null..
	//		processPropertyKeys(props);
	//	}
		// now any properties files
		final String[] propsFiles = commandLine.getOptionValues("propertyFile");
		if (propsFiles != null) {
			processPropertyFiles(propsFiles);
		}
		
		// now any properties URLs
		final String[] propsURLs = commandLine.getOptionValues("propertyURL");
		if (propsURLs != null) {
			processPropertyURLs(propsURLs);
		}
		// now any modules in files
		final String[] moduleFiles = commandLine.getOptionValues("addModule");
		if (moduleFiles != null) {
			processModuleFiles(l, moduleFiles);
		}
		
		// now any modules at URLs
		final String[] moduleURLs = commandLine.getOptionValues("addModuleURL");
		if (moduleURLs != null ) {
			processModuleURLs(l, moduleURLs);		
		}
		
		// enable EDT debugging
		if (commandLine.hasOption("debugEDT")) {
		    RepaintManager.setCurrentManager(new CheckThreadViolationRepaintManager());
		}
	}
	/**
	 * @param l
	 * @param moduleURLs
	 */
	private static void processModuleURLs(final Launcher l, final String[] moduleURLs) {
		for (int i =0; i < moduleURLs.length; i++) {
			try {
				l.addModuleURL(new URL(moduleURLs[i]));
			} catch (final MalformedURLException x) {
				logger.warn("Could not access module file " + moduleURLs[i],x);
			}
		}
	}
	/**
	 * @param l
	 * @param moduleFiles
	 */
	private static void processModuleFiles(final Launcher l, final String[] moduleFiles) {
		for (int i =0; i < moduleFiles.length; i++) {
			final File f = new File(moduleFiles[i]); 
			//@todo check that this takes care of expanding, relatives, etc
			if (!f.exists()) {
				logger.warn("Ignoring non-existent module file " + f);
				continue;
			}
			try {
				l.addModuleURL(f.toURI().toURL());
			} catch (final MalformedURLException x) {
				logger.warn("Could not access module file " + f,x);
			}
		}
	}
	/**
	 * @param propsURLs
	 */
	private static void processPropertyURLs(final String[] propsURLs) {
		for (int i =0; i < propsURLs.length; i++) {
			URL u = null;

			InputStream is = null;
			try {
				u = new URL(propsURLs[i]); 
				is = u.openStream();
				loadPropertiesFile(is);
			} catch (final IOException e) {
				logger.warn("Failed to load properties file " + u,e);
			} finally {
				if (is != null ) {
					try {
						is.close();
					} catch (final IOException e) {
						// don't care.
					}
				}
			}
		}
	}
	/**
	 * @param propsFiles
	 */
	private static void processPropertyFiles(final String[] propsFiles) {
		for (int i =0; i < propsFiles.length; i++) {
			final File f = new File(propsFiles[i]); 
			//@todo check that this takes care of expanding, relatives, etc
			if (!f.exists()) {
				logger.warn("Ignoring non-existent properties file " + f);
				continue;
			}
			InputStream is = null;
			try {
				logger.info("Loading properties from " + f);
				is = new FileInputStream(f);
				loadPropertiesFile(is);
			} catch (final IOException e) {
				logger.warn("Warning: Failed to load properties file " + f,e);
			} finally {
				if (is != null ) {
					try {
						is.close();
					} catch (final IOException e) {
						// don't care.
					}
				}
			}
		}
	}

	/**
	 * @param is
	 * @throws IOException
	 */
	private static void loadPropertiesFile(final InputStream is) throws IOException {
		final Properties p = new Properties();
		p.load(is);
		for (final Iterator it = p.entrySet().iterator(); it.hasNext(); ) {
			final Map.Entry entry = (Map.Entry)it.next();
			logger.debug("'" + entry.getKey().toString() + "' := '" + entry.getValue().toString() + "'"); 
			System.setProperty(entry.getKey().toString(),entry.getValue().toString());
		}
	}
	
}
