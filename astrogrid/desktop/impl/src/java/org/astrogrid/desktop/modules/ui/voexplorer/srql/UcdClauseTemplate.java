/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.srql;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.StringUtils;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.DescribedEnumerationTemplate.DescribedValue;

/** Query building template for a UCD clause.
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
    protected void populate(final List<DescribedValue> l) {
		InputStream is = null;
		LineIterator it = null;
		try {
			is = this.getClass().getResourceAsStream(LIST_RESOURCE);
			if (is == null) {
				logger.error("Could not load ucd list");
				return;
			}
			it = IOUtils.lineIterator(new InputStreamReader(is));
			while (it.hasNext()) {
			    final String line = it.nextLine();
				final String[] strings = StringUtils.split(line.trim()," ",2); // splits on first space.
				l.add(new DescribedValue(strings[0],strings[1]));
			}
		} finally {
			LineIterator.closeQuietly(it);
		}
		
	}

}
