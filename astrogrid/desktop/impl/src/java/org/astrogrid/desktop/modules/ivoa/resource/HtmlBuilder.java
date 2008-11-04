/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa.resource;

import java.net.URI;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.astrogrid.acr.ivoa.resource.ResourceName;

/** Extension of StrBuilder that provides methods for generating HTML.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 8, 200710:19:15 AM
 */
public class HtmlBuilder extends StrBuilder {

	public HtmlBuilder() {
		super(256); // fix related to 2640
		append("<html>");
	}
	// allocate more space at a time.
	public StrBuilder ensureCapacity(final int capacity) {
        if (capacity > buffer.length) {
            final char[] old = buffer;
            buffer = new char[(int) (capacity * 1.5)];
            System.arraycopy(old, 0, buffer, 0, size);
        }
        return this;
    }

	public HtmlBuilder h2(final String text) {
		append("<h2>").append(text).append("</h2>");
		return this;
	}
	
	public HtmlBuilder h3(final String text) {
		append("<h3>").append(text).append("</h3>");
		return this;
	}
	
	public HtmlBuilder h3(final String text, final Object o) {
		append("<h3>").append(text).append(" ").append(o).append("</h3>");
		return this;
	}
	
	public HtmlBuilder br() {
		append("<br>");
		return this;
	}
	
	public HtmlBuilder p() {
	    append("<p>");
	    return this;
	}
	public HtmlBuilder hr() {
		append("<hr>");
		return this;
	}
	
	public HtmlBuilder appendWrap(final Object txt, final int wrap) {
	    append(WordUtils.wrap(ObjectUtils.toString(txt),wrap,"<br>",false));
	    return this;
	}
	
	/** appends the list, prefixed with the title, if the list is non-null & length > 0 
	 * also adds br afterwards.*/
	public HtmlBuilder appendTitledSequence(final String title,final Object[] list) {
		if (list != null && list.length > 0) {
			appendLabel(title)
				.appendWithSeparators(list,", ")
				.append("<br>");
		} 
		return this;
	}
    public HtmlBuilder appendTitledSequenceNoBR(final String title,final Object[] list) {
        if (list != null && list.length > 0) {
            appendLabel(title)
                .appendWithSeparators(list,", ")
                .append("&nbsp; ");
        } 
        return this;
    }
    
    /** append text, formatted as a label */
    public HtmlBuilder appendLabel(final String text) {
        append("<cite class='label'>").append(text).append("</cite>&nbsp;");
        return this;
    }
    
	/** appends the list, prefixed with the title, if the object is non-null
	 * also adds a br afterwards */
	public HtmlBuilder appendTitledObject(final String title,final Object o) {
		if (o != null) {
			appendLabel(title)
				.append(o)
				.append("<br>");
		} 
		return this;
	}
	
	   public HtmlBuilder appendTitledObjectNoBR(final String title,final Object o) {
	        if (o != null) {
	            appendLabel(title)	              
	                .append(o)
	                .append("&nbsp; ");
	        } 
	        return this;
	    }
	   
       public HtmlBuilder appendTitledObjectNoBR(final String title,final int i) {
               appendLabel(title)                
                   .append(i)
                   .append("&nbsp; ");
           return this;
       }
       public HtmlBuilder appendTitledObjectNoBR(final String title,final double d) {
               appendLabel(title)                
                   .append(d)
                   .append("&nbsp; ");
           return this;
       }       
/** append a reouscr name, prefixed with title, if the resource name is non-null
 * adds a br afterwards
 */
	public HtmlBuilder appendTitledResourceName(final String title,final ResourceName name) {
	    if (name != null) {
	        appendLabel(title);
	        if (name.getId() != null) {
	            append("<a class='res' href='").append(name.getId()).append("'>");
	            final String v = name.getValue();
	            append(v == null  ? name.getId().toString() : v);
	            append("</a>");
	        } else {
	            append(name.getValue());
	        }
	        append("<br>");
	    }

		return this;
	}
	   public HtmlBuilder appendTitledResourceNameNoBR(final String title,final ResourceName name) {
	        if (name != null) {
	            appendLabel(title);
	            if (name.getId() != null) {
	                append("<a class='res' href='").append(name.getId()).append("'>");
	                final String v = name.getValue();
	                append(v == null ? name.getId().toString() : v);
	                append("</a>");
	            } else {
	                append(name.getValue());
	            }
	            append("&nbsp; ");
	        }

	        return this;
	    }
	/** appends a resouce name, hyperlinked if an id is provided. */
	public HtmlBuilder appendResourceName(final ResourceName name) {
	    if (name == null) {
	        return this;
	    }
	    String url = null;
	    if (name.getId() != null) {
	        url = name.getId().toString();
	    } else if (StringUtils.contains(name.getValue(),"://")) {
	        url = name.getValue(); // work=around for common misuse of resource structure.
	    }
		if (url != null) {
			append("<a class='res' href='").append(url).append("'>");
			final String v = name.getValue();
			append(v == null? url : v);
			append("</a>");
		} else {
			append(name.getValue());
		}	
		return this;
	}
	
	/** appends a list of resourcenames, formatted, prefixed with a title,
	 * with a trailing br if the list is non-null and non-empty.
	 */
	public HtmlBuilder appendTitledResourceNames(final String title, final ResourceName[] list) {
		if (list != null && list.length > 0) {
			appendLabel(title);
			for (int i = 0; i < list.length; i++) {
				appendResourceName(list[i]);
				append(" ");
			}
			append("<br>");
		} 
		return this;
	}	
    public HtmlBuilder appendTitledResourceNamesNoBR(final String title, final ResourceName[] list) {
        if (list != null && list.length > 0) {
            appendLabel(title);
            for (int i = 0; i < list.length; i++) {
                appendResourceName(list[i]);
                append(" ");
            }
        } 
        return this;
    }   
	public HtmlBuilder appendTitledURIs(final String title, final URI[] list) {
		if (list != null && list.length > 0) {
			appendLabel(title);
			for (int i = 0; i < list.length; i++) {
				appendURI(list[i]);
				append(" ");
			}
			append("<br>");
		} 
		return this;
	}	
	
	
	/** Write out a url/uri -if it's a well known scheme - http / ftp / ivo, add a hyperlink around it.
	 * @param url
	 */
	public  HtmlBuilder appendURI( final URI url) {
		if (url != null) {
		final String scheme = url.getScheme();
		// fix for BZ 1970 - odd scheme types.
		final String wrappedURL = StringUtils.replace(url.toString(),"/","/<wbr>");
		if ("http".equals(scheme) || "ftp".equals(scheme)) {
			append("<a href='").append(url).append("'>");
			append(wrappedURL).append("</a>");
		} else if ("ivo".equals(scheme)) {
            append("<a class='res' href='").append(url).append("'>");
            append(wrappedURL).append("</a>");		    
		} else {
				append(wrappedURL);
		}
		}
		return this;
	}
    public  HtmlBuilder appendURI(final String text, final URI url) {
        if (url != null) {
        final String scheme = url.getScheme();
        if ("http".equals(scheme) || "ftp".equals(scheme)) {
            append("<a href='").append(url).append("'>");
            append(text).append("</a>");
        } else if ("ivo".equals(scheme)) {
                append("<a class='res' href='").append(url).append("'>");
                append(text).append("</a>");            
        } else {
            // fix for BZ 1970 - odd scheme types.
            final String wrappedURL = StringUtils.replace(url.toString(),"/","/<wbr>");
                append("&lt;")
                .append(text)
                .append(" - ")
                .append(wrappedURL)
                .append("&gt;");
        }
        }
        return this;
    }	
  
	
	/** append o if pred is true, following with a hr */
	public HtmlBuilder conditionalAppend(final boolean pred,final Object o) {
		if (pred) {
			append(o);
			append("<hr>");
		}
		return this;
	}
	
	/** append o if non-null, following with a hr */
	public HtmlBuilder conditionalAppend(final Object o) {
		if (o != null) {
			append(o);
			append("<hr>");
		}
		return this;
	}
	
	/** write a table cell (td) */
	public HtmlBuilder td(final Object o) {
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
