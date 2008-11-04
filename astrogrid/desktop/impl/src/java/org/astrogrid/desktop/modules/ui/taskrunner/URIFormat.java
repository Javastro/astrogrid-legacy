/**
 * 
 */
package org.astrogrid.desktop.modules.ui.taskrunner;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;

/** {@link Format} for URIs.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 5, 20072:21:44 PM
 */
public class URIFormat extends Format {

	public StringBuffer format(final Object obj, final StringBuffer toAppendTo,
			final FieldPosition pos) {
		// dunno what to do with fieldPosition - assume to leave this.
		return toAppendTo.append(obj);
	}

	public Object parseObject(final String source, final ParsePosition pos) {
		try {
			return new URI(source.substring(pos.getIndex()));
		} catch (final URISyntaxException x) {
			return null;
		}
	}

	
	
	
}
