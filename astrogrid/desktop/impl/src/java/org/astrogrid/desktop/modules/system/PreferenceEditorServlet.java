/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MultiHashMap;
import org.apache.commons.collections.MultiMap;

/** Servlet that provides a way to view and edito configuration preferences.
 * @todo implement test, choose, reset buttons for each item.
 * @author Noel Winstanley
 * @since Jan 9, 20072:48:57 PM
 */
public class PreferenceEditorServlet extends HttpServlet {

public void init(ServletConfig conf) throws ServletException {
	super.init(conf);
	List prefs = (List)conf.getServletContext().getAttribute(PREFERENCES_CONTEXT_KEY);
	arranger = (PreferencesArranger)conf.getServletContext().getAttribute(ARRANGER_CONTEXT_KEY);
	if (prefs == null || arranger == null) {
		throw new ServletException("Could not retrieve required components from servlet context");
	}
	for (Iterator i = prefs.iterator(); i.hasNext();) {
		Preference p = (Preference) i.next();
		prefMap.put(p.getName(), p);
	}
	
}
	protected PreferencesArranger arranger;
	/** maps from preference name to preference */
	protected Map prefMap = new HashMap();

	public final static String PREFERENCES_CONTEXT_KEY = "preferences";
	public final static String ARRANGER_CONTEXT_KEY = "arranger";

	// displays a form.
	protected void doGet(HttpServletRequest arg0, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		out.println("<html><head><title>Preferences</title></html>");
		out.println("<body><a href='./.' >up</a><h1>Preferences</h1>");
		for (Iterator catNames = arranger.listPreferenceCategories().iterator(); catNames.hasNext(); ) {
			String catname = (String)catNames.next();
			out.println("<h2>" + catname + "</h2>");
			buildForms(out, arranger.listBasicPreferencesForCategory(catname));
			buildForms(out, arranger.listAdvancedPreferencesForCategory(catname));			
		}

		out.println("</body></html>");
		out.flush();
		out.close();
	}

	/**
	 * @param out
	 * @param modulePreferences
	 */
	private void buildForms(PrintWriter out, final List modulePreferences) {
		for (Iterator i = modulePreferences.iterator(); i.hasNext(); ) {
			Preference p= (Preference) i.next();
			if (p.isAdvanced()) {
				out.println("<div style='background:#cccccc;border:thin solid black;padding:5px;margin:2px'>");
			} else {
				out.println("<div style='border:thin solid black;padding:5px;margin:2px'>");
			}
			out.print("<form method='post' name='");
			out.print(p.getName());
			out.println("' action='./preferences'>");

			out.println("<b>" + p.getUiName() + " </b> ");

			String[] options = p.getOptions();
			if (options.length ==0) { // free text entry
			int sz = p.getDefaultValue().length() < 10 ? 10 : 60;
			out.print("<input type='text' size='" + sz +"' name='");
			out.print(p.getName());
			out.print("' value='");
			out.print(p.getValue());
			out.print("'>");
				if (p.getUnits() != null) {
					out.println("<i>" + p.getUnits() + "</i>");
				}
			} else { // combo box
				out.print("<select name='");
				out.print(p.getName());
				out.println("'>");
				for (int ix = 0; ix < options.length; ix++) {
					out.print("<option value='");
					out.print(options[ix]);
					out.print("' ");						
					if (options[ix].equals(p.getValue())) {
						out.print(" selected='selected' ");
					}
					out.print(">");
					out.print(options[ix]);
					out.print("</option>");
				}
				out.println("</select>");
			}

			out.println("<br>");
			out.println(p.getDescription());
			String[] alts = p.getAlternatives();
			if (alts.length != 0) {
				out.println("<br><i>Alternative suggested values: </i>");
				for (int ix = 0; ix < alts.length; ix++) {
					out.println(alts[ix]);
					out.println(", ");
				}
			}
			out.println("<br><input type='submit' value='Update'>");
			if (p.isRequiresRestart()) {
				out.println(" <i>May require restart</i>");
			}

			
			out.println("</form></div>");
		}
	}
	
	// processes updates to the form.
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	// process change.
	// now the response.
	Enumeration e = request.getParameterNames();
	while (e.hasMoreElements()) {
		String name = (String)e.nextElement();
		Preference p = (Preference)prefMap.get(name);
		String value = request.getParameter(name);
		if (p != null && value != null) {
			p.setValue(value); // shoudl we be doing checking here?
		}
	}
	doGet(request, response);
}	
}
