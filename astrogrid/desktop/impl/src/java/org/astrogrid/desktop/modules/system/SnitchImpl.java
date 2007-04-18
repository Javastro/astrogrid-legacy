/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.desktop.modules.plastic.PlasticApplicationDescription;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;

/** Implementation of the snitcher.
 * @author Noel Winstanley
 * @since May 19, 200612:08:33 AM
 */
public class SnitchImpl implements SnitchInternal {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(SnitchImpl.class);

	private final static String base = "http://software.astrogrid.org/snitch/";
	private final UIContext ui;
	private final boolean snitchDisabled;

	
	public SnitchImpl( UIContext ui,String version, String appMode, final EventList plasticList, Preference doSnitch) {
		super();
		this.ui = ui;
		this.snitchDisabled = (! doSnitch.asBoolean()) 
						|| version.equals("${astrogrid.desktop.version}"); // if the key isn't available, the keyname is passed in instead.
		// this key isn't availahble when running in eclipse - so won't snitch if we're in development mode - as will give meaningless results.

		if (! snitchDisabled) {
			plasticList.addListEventListener(new ListEventListener() {

				public void listChanged(ListEvent arg0) {
					while(arg0.next()) {
						if (arg0.getType() == ListEvent.INSERT) {
							PlasticApplicationDescription plas = (PlasticApplicationDescription)plasticList.get(arg0.getIndex());
							Map m = new HashMap();
							m.put("name",plas.getName());
							m.put("ops",Arrays.asList(plas.getUnderstoodMessages()));
							snitch("PLASTIC",m);								
						}
					}
				}
			}
			);
		}
		Map m = new HashMap();
		m.put("app", version +  " | "
				+ (appMode != null ? appMode : "unknown") + " | " 
				+	(System.getProperty("javawebstart.version") != null ? "webstart" : "standalone")
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
					.append(e.getValue() != null ? URLEncoder.encode(e.getValue().toString()) : "null")
					.append("&");
			}
			URL query = new URL(sb.toString());
			query.openConnection().getContent();
			return null; // job done.
		}
		protected void doError(Throwable ex) {
			// expected.
		}
	 }).start();
		}
	}
	
}
