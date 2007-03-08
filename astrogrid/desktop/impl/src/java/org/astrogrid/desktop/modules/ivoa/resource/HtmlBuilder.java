/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa.resource;

import org.apache.commons.lang.text.StrBuilder;
import org.astrogrid.acr.ivoa.resource.ResourceName;

/** Helper class that extends strbuilder with methods for writing out html constructs.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 8, 200710:19:15 AM
 */
public class HtmlBuilder extends StrBuilder {

	public HtmlBuilder() {
		super();
	}

	public HtmlBuilder(int arg0) {
		super(arg0);
	}

	public HtmlBuilder(String arg0) {
		super(arg0);
	}

	public HtmlBuilder h2(String text) {
		append("<h2>").append(text).append("</h2>");
		return this;
	}
	
	public HtmlBuilder h3(String text) {
		append("<h3>").append(text).append("</h3>");
		return this;
	}
	
	public HtmlBuilder h3(String text, Object o) {
		append("<h3>").append(text).append(" ").append(o).append("</h3>");
		return this;
	}
	
	public HtmlBuilder br() {
		append("<br>");
		return this;
	}
	
	public HtmlBuilder hr() {
		append("<hr>");
		return this;
	}
	
	/** appends the list, prefixed with the title, if the list is non-null & length > 0 
	 * also adds br afterwards.*/
	public HtmlBuilder appendTitledSequence(String title,Object[] list) {
		if (list != null && list.length > 0) {
			append(title)
				.append(": ")
				.appendWithSeparators(list,", ")
				.append("<br>");
		} 
		return this;
	}

	/** appends the list, prefixed with the title, if the object is non-null
	 * also adds a br afterwards */
	public HtmlBuilder appendTitledObject(String title,Object o) {
		if (o != null) {
			append(title)
				.append(": ")
				.append(o)
				.append("<br>");
		} 
		return this;
	}
/** append a reouscr name, prefixed with title, if the resource name is non-null
 * adds a br afterwards
 */
	public HtmlBuilder appendTitledResourceName(String title,ResourceName name) {
		if (name != null) {
			append(title).append(": ");
		if (name.getId() != null) {
			append("<a href='").append(name.getId()).append("'>");
			String v = name.getValue();
			append(v == null || v.trim().length() == 0 ? name.getId().toString() : v);
			append("</a>");
		} else {
			append(name.getValue());
		}
		append("<br>");
		}
		
		return this;
	}
	/** appends a resouce name, hyperlinked if an id is provided. */
	public HtmlBuilder appendResourceName(ResourceName name) {
		if (name.getId() != null) {
			append("<a href='").append(name.getId()).append("'>");
			String v = name.getValue();
			append(v == null || v.trim().length() == 0 ? name.getId().toString() : v);
			append("</a>");
		} else {
			append(name.getValue());
		}	
		return this;
	}
	
	/** appends a list of resourcenames, formatted, prefixed with a title,
	 * with a trailing br if the list is non-null and non-empty.
	 */
	public HtmlBuilder appendTitledResourceNames(String title, ResourceName[] list) {
		if (list != null && list.length > 0) {
			append(title);
			append(": ");
			for (int i = 0; i < list.length; i++) {
				appendResourceName(list[i]);
				append(" ");
			}
			append("<br>");
		} 
		return this;
	}	
	
	
	/** append o if pred is true, following with a hr */
	public HtmlBuilder conditionalAppend(boolean pred,Object o) {
		if (pred) {
			append(o);
			append("<hr>");
		}
		return this;
	}
	
	/** append o if non-null, following with a hr */
	public HtmlBuilder conditionalAppend(Object o) {
		if (o != null) {
			append(o);
			append("<hr>");
		}
		return this;
	}
	
	/** write a table cell (td) */
	public HtmlBuilder td(Object o) {
		append("<td>");
		append(o);
		append("</td>");
		return this;
	}

	/**
	 * @param string
	 * @param facilities
	 */

	
	
	
	
}
