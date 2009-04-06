/**
 * 
 */
package org.astrogrid.desktop.modules.system.pref;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that provides viewing and editing of application preferences.
 * @todo implement test, choose, reset buttons for each item.
 * @author Noel Winstanley
 * @since Jan 9, 20072:48:57 PM
 */
public class PreferenceEditorServlet extends HttpServlet {

@Override
public void init(final ServletConfig conf) throws ServletException {
	super.init(conf);
	final List prefs = (List)conf.getServletContext().getAttribute(PREFERENCES_CONTEXT_KEY);
	arranger = (PreferencesArranger)conf.getServletContext().getAttribute(ARRANGER_CONTEXT_KEY);
	if (prefs == null || arranger == null) {
		throw new ServletException("Could not retrieve required components from servlet context");
	}
	for (final Iterator i = prefs.iterator(); i.hasNext();) {
		final Preference p = (Preference) i.next();
		prefMap.put(p.getName(), p);
	}
	
}
	protected PreferencesArranger arranger;
	/** maps from preference name to preference */
	protected Map<String, Preference> prefMap = new HashMap<String, Preference>();

	public final static String PREFERENCES_CONTEXT_KEY = "preferences";
	public final static String ARRANGER_CONTEXT_KEY = "arranger";

	// displays a form.
	@Override
    protected void doGet(final HttpServletRequest arg0, final HttpServletResponse response) throws ServletException, IOException {
		final PrintWriter out = response.getWriter();
		out.println("<html><head><title>Preferences</title></html>");
		out.println("<body><a href='./.' >up</a><h1>Preferences</h1>");
		for (final Iterator catNames = arranger.listPreferenceCategories().iterator(); catNames.hasNext(); ) {
			final String catname = (String)catNames.next();
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
	private void buildForms(final PrintWriter out, final List modulePreferences) {
		for (final Iterator i = modulePreferences.iterator(); i.hasNext(); ) {
			final Preference p= (Preference) i.next();
			if (p.isAdvanced()) {
				out.println("<div style='background:#cccccc;border:thin solid black;padding:5px;margin:2px'>");
			} else {
				out.println("<div style='border:thin solid black;padding:5px;margin:2px'>");
			}
			out.print("<form method='post' name='");
			out.print(p.getName());
			out.println("' action='./preferences'>");

			out.println("<b>" + p.getUiName() + " </b> ");

			final String[] options = p.getOptions();
			if (options.length ==0) { // free text entry
			final int sz = p.getDefaultValue().length() < 10 ? 10 : 60;
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
			final String[] alts = p.getAlternatives();
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
@Override
protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
	// process change.
	// now the response.
	final Enumeration e = request.getParameterNames();
	while (e.hasMoreElements()) {
		final String name = (String)e.nextElement();
		final Preference p = prefMap.get(name);
		final String value = request.getParameter(name);
		if (p != null && value != null) {
			p.setValue(value); // shoudl we be doing checking here?
		}
	}
	doGet(request, response);
}	
}
