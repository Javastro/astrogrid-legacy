/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.srql;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/** query building template for a UCD clause.
 *  loads data from ucdlist.txt
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 25, 20071:41:00 AM
 */
public final class UcdClauseTemplate extends DescribedEnumerationTemplate {

	public UcdClauseTemplate() {
		super("Any column UCD","ucd");
	} 

	protected static final String LIST_RESOURCE = "ucd.list";

	
	@Override
    protected void populate(final List l) {
		InputStream is = null;
		BufferedReader r = null;
		try {
			is = this.getClass().getResourceAsStream(LIST_RESOURCE);
			if (is == null) {
				logger.error("Could not load ucd list");
				return;
			}
			r = new BufferedReader(new InputStreamReader(is));
			String line;
			while ( (line = r.readLine()) != null) {
				final String[] strings = StringUtils.split(line.trim()," ",2); // splits on first space.
				l.add(new DescribedValue(strings[0],strings[1]));
			}
		} catch (final IOException x) {
			logger.error("Failed to read all list",x);
			return;
		} finally {
			if (is != null) {
				try {is.close();} catch (final IOException e) {}
			}
			if (r != null) {
				try {r.close();} catch (final IOException e) {}
			}
		}
		
	}

}
