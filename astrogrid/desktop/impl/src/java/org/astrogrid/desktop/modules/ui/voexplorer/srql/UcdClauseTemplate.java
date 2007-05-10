/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.srql;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.astrogrid.desktop.modules.ui.voexplorer.srql.SrqlQueryBuilderPanel.ClauseTemplate;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.TextFilterator;
import ca.odell.glazedlists.matchers.TextMatcherEditor;
import ca.odell.glazedlists.swing.AutoCompleteSupport;

/** query building template for a UCD clause.
 *  loads data from ucdlist.txt
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 25, 20071:41:00 AM
 */
public class UcdClauseTemplate extends DescribedEnumerationTemplate {
	/**
	 * @param name
	 */
	public UcdClauseTemplate() {
		super("Any column UCD","ucd");
	} 

	protected static final String LIST_RESOURCE = "ucd.list";

	
	protected void populate(List l) {
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
				String[] strings = StringUtils.split(line.trim()," ",2); // splits on first space.
				l.add(new DescribedValue(strings[0],strings[1]));
			}
		} catch (IOException x) {
			logger.error("Failed to read all list",x);
			return;
		} finally {
			if (is != null) {
				try {is.close();} catch (IOException e) {}
			}
			if (r != null) {
				try {r.close();} catch (IOException e) {}
			}
		}
		
	}

}
