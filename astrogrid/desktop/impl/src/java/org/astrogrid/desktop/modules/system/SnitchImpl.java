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

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.desktop.modules.plastic.PlasticApplicationDescription;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;

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
	private final UIInternal ui;
	private final boolean snitchDisabled;

	
	public SnitchImpl( UIInternal ui,String version, String appMode, final ReportingListModel plasticList) {
		super();
		this.ui = ui;
		this.snitchDisabled = version.equals("${astrogrid.desktop.version}"); // if the key isn't available, the keyname is passed in instead.
		// this key isn't availahble when running in eclipse - so won't snitch if we're in development mode - as will give meaningless results.

		if (! snitchDisabled) {
			plasticList.addListDataListener(new ListDataListener() {

				public void contentsChanged(ListDataEvent e) {
				}

				public void intervalAdded(ListDataEvent e) {
					for (int i = e.getIndex0(); i <= e.getIndex1(); i++) {
						PlasticApplicationDescription plas = (PlasticApplicationDescription)plasticList.getElementAt(i);
						Map m = new HashMap();
						m.put("name",plas.getName());
						m.put("ops",Arrays.asList(plas.getUnderstoodMessages()));
						snitch("PLASTIC",m);	
					}
				}

				public void intervalRemoved(ListDataEvent e) {
				}
				
			});
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
