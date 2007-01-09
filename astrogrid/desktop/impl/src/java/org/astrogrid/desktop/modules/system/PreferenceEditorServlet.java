/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that provides a way to view and edito configuration preferences.
 * @author Noel Winstanley
 * @since Jan 9, 20072:48:57 PM
 */
public class PreferenceEditorServlet extends HttpServlet {

public void init(ServletConfig conf) throws ServletException {
	super.init(conf);
	prefs = (List)conf.getServletContext().getAttribute(PREFERENCES_CONTEXT_KEY);
	if (prefs == null) {
		throw new ServletException("Could not retrieve preferences from servlet context");
	}
	// would I want to sort these preferences?
}

	protected List prefs;
	public final static String PREFERENCES_CONTEXT_KEY = "preferences";

	// displays a form.
	protected void doGet(HttpServletRequest arg0, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		out.println("<html><head><title>Preferences</title></html><body>");
		for (Iterator i = prefs.iterator(); i.hasNext(); ) {
			Preference p= (Preference) i.next();
			out.print("<form method='post' name='");
			out.print(p.getName());
			out.println("' action='.'>");
			
			out.println("<b>" + p.getUiName() + "</b> - ");
			out.println(p.getDescription());
			out.print("<br><input type='text' name='");
			out.print(p.getName());
			out.print("' value='");
			out.print(p.getValue());
			out.print("'>");
			
			out.println("<input type='submit' value='Update'>");
			out.println("</form><hr>");
			
		}
		
		out.println("</body></html>");
		out.flush();
		out.close();
	}
	
	// processes updates to the form.
	
}
