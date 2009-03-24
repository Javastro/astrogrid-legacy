/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.desktop.modules.system.messaging.ExternalMessageTarget;
import org.astrogrid.desktop.modules.system.messaging.MessageType;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;

/** Implementation of usage reporting.
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

	
	public SnitchImpl( final UIContext ui,final String version, final String appMode, final EventList<ExternalMessageTarget> targetList, final Preference doSnitch) {
		super();
    	logger.info("AstroRuntime version: " + version + ", " + appMode);
		this.ui = ui;
		this.snitchDisabled = (! doSnitch.asBoolean()) 
						|| version.equals("${astrogrid.desktop.version}"); // if the key isn't available, the keyname is passed in instead.
		// this key isn't availahble when running in eclipse - so won't snitch if we're in development mode - as will give meaningless results.

		if (! snitchDisabled) {
			targetList.addListEventListener(new ListEventListener<ExternalMessageTarget>() {
			       
				public void listChanged(final ListEvent<ExternalMessageTarget> arg0) {
					while(arg0.next()) {
						if (arg0.getType() == ListEvent.INSERT) {
						    final ExternalMessageTarget target = targetList.get(arg0.getIndex());
							final Map m = new HashMap();
							m.put("name",target.getName());
							final List<String> messages = new ArrayList();
							for(final MessageType<?> mt : target.acceptedMessageTypes()) {
							    messages.add(mt.getClass().getName());
							}
							m.put("ops",messages);
							m.put("description",target.getDescription());
							snitch("PLASTIC",m);								
						}
					}
				}
			}
			);
		}
		final Map m = new HashMap();
		m.put("app", version
		        //+  " | "
				//+ (appMode != null ? appMode : "unknown")

				//+ " | " 
				//+	(System.getProperty("javawebstart.version") != null ? "webstart" : "standalone")
			//	+ ""
					);
		m.put("env",System.getProperty("java.version")
			+ " | " + System.getProperty("os.name"));
		// report startup.	
		snitch("STARTUP",m); 
	}
	

	
	
	public void snitch(final String message) {
		snitch(message, MapUtils.EMPTY_MAP);
	}

	public void snitch(final String message, final Map params) {
		if (! snitchDisabled) {
		(new BackgroundWorker(ui,"Reporting usage",BackgroundWorker.VERY_SHORT_TIMEOUT,Thread.MIN_PRIORITY) {

		protected Object construct() throws Exception {
			final StrBuilder sb = new StrBuilder(64);
			sb.append(base);
			sb.append(message).append('?');			
			for (final Iterator i = params.entrySet().iterator(); i.hasNext(); ) {
				final Map.Entry e = (Map.Entry)i.next();
				sb.append(e.getKey())
					.append("=")
					.append(e.getValue() != null ? URLEncoder.encode(e.getValue().toString()) : "null")
					.append("&");
			}
			final URL query = new URL(sb.toString());
			logger.debug(query);
			query.openConnection().getContent();
			return null; // job done.
		}
		protected void doError(final Throwable ex) {
			// expected.
		}
	 }).start();
		}
	}
	
}
