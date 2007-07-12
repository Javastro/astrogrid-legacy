/**
 * 
 */
package org.astrogrid.desktop.modules.ui.taskrunner;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;

/** Formatter that works with URIs.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 5, 20072:21:44 PM
 */
public class URIFormat extends Format {

	public StringBuffer format(Object obj, StringBuffer toAppendTo,
			FieldPosition pos) {
		// dunno what to do with fieldPosition - assume to leave this.
		return toAppendTo.append(obj);
	}

	public Object parseObject(String source, ParsePosition pos) {
		try {
			return new URI(source.substring(pos.getIndex()));
		} catch (URISyntaxException x) {
			return null;
		}
	}

	
	
	
}
