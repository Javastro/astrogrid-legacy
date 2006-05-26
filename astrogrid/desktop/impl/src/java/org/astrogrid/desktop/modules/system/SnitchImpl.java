/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.MapUtils;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;

/** Implementation of the snitcher.
 * @author Noel Winstanley
 * @since May 19, 200612:08:33 AM
 */
public class SnitchImpl implements SnitchInternal {

	private final static String base = "http://software.astrogrid.org/snitch/";
	private final Configuration conf;
	private final UIInternal ui;
	private final boolean snitchDisabled;

	
	public SnitchImpl( UIInternal ui, Configuration conf) {
		super();
		this.ui = ui;
		this.conf = conf;
		this.snitchDisabled = conf.getKey("astrogrid.version") == null;
		// won't snitch if we're in development mode - as will give meaningless results.
		// astrogrid.version doesn't seem to be available when running from within eclipse 
		// -- will test using this.
		Map m = new HashMap();
		m.put("app", conf.getKey("astrogrid.version") + " | " + 
					(conf.getKey("hub.mode") != null ? "hub" : (
							conf.getKey("asr.mode") != null ? "asr" : (
							conf.getKey("acr.mode") != null ? "acr" : (
							conf.getKey("workbench.mode") != null ? "workbench" : 
								"unknown" )))) + " | " + 
					(System.getProperty("javawebstart.version") != null ? "webstart" : "standalone")
					);
		m.put("env",System.getProperty("java.version")
			+ " | " + System.getProperty("os.name"));
		// report startup.	
		snitch("STARTUP",m); 
	}
	
	public void snitch(String message) {
		snitch(message, MapUtils.EMPTY_MAP);
	}

	public void snitch(final String message, final Map params) {
		if (! snitchDisabled) {
		(new BackgroundWorker(ui,".") {

		protected Object construct() throws Exception {
			StringBuffer sb = new StringBuffer(base);
			sb.append(message).append('?');
			for (Iterator i = params.entrySet().iterator(); i.hasNext(); ) {
				Map.Entry e = (Map.Entry)i.next();
				sb.append(e.getKey())
					.append("=")
					.append(URLEncoder.encode(e.getValue().toString()))
					.append("&");
			}
			URL query = new URL(sb.toString());
			query.openConnection().getContent();
			return null; // job done.
		}
		protected void doError(Throwable ex) {
			// silent.
		}
	 }).start();
		}
	}
	
}
