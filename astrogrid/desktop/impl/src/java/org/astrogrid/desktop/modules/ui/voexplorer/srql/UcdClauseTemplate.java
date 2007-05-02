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
 * Has an auto-complete function, and loads data from ucdlist.txt
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 25, 20071:41:00 AM
 */
public class UcdClauseTemplate extends ClauseTemplate {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(UcdClauseTemplate.class);

	/**
	 * @param name
	 */
	public UcdClauseTemplate() {
		super("Any column UCD","ucd");
	}
	private static final String LIST_RESOURCE = "ucd.list";
	SRQL constructClause(JComponent valueField, JComponent predicateField) {
		TargettedSRQL t = new TargettedSRQL();
		t.setTarget(target);
		TermSRQL ts = new TermSRQL();
		UCD field = (UCD)((JComboBox)valueField).getSelectedItem();
		ts.setTerm(field.ucd);
		JComboBox predicate = (JComboBox)predicateField;
		switch (predicate.getSelectedIndex()) {
			case IS:
				t.setChild(ts);
				return t;
			case ISNOT:
				NotSRQL s = new NotSRQL();
				s.setChild(ts);
				t.setChild(s);
				return t;
			default:
				throw new RuntimeException("Programming Error: index=" + predicate.getSelectedIndex());
		}		
	}
	
	void displayClause(SRQL clause, JComponent valueField, JComponent predicateField) {
		JComboBox pred = (JComboBox)predicateField;
		JComboBox val = (JComboBox)valueField;
		if (clause instanceof NotSRQL) {
			pred.setSelectedIndex(ISNOT);
			clause = ((NotSRQL)clause).getChild(); // traverse down.
		} else {
			pred.setSelectedIndex(IS);
		}
		final String ucdName = ((TermSRQL)clause).getTerm();
		UCD u = new UCD(ucdName,"");
		val.setSelectedItem(u); // works because equals test only works on ucd, not on description field.
	}	

	private static final String[] predicates= new String[]{"is","is not"};
	private static final int IS = 0;
	private static final int ISNOT = 1;
	//@todo strictly speaking, this is 'contains' - need to implement is and isnot (exact matching).
	//@todo and then provide 'contains' as a separate option
	JComponent createPredicateField() {
		JComboBox c = new JComboBox(predicates);
		c.setEditable(false);
		c.setSelectedIndex(0);
		return c;
	}
	
	JComponent createValueField() {
		EventList ucds= new BasicEventList();
		populate(ucds);
		final JComboBox c = new JComboBox();
		c.setRenderer(new ListCellRenderer() {
		ListCellRenderer orig= c.getRenderer();
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			JLabel l = (JLabel)orig.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
			UCD u = (UCD)value;
			l.setText(u.toFullString());
			return l;
		}
		});
		AutoCompleteSupport support = AutoCompleteSupport.install(c,ucds);
		c.setSelectedIndex(0);
		support.setSelectsTextOnFocusGain(true);
		support.setStrict(true); 
		support.setCorrectsCase(true);
		support.setFilterMode(TextMatcherEditor.CONTAINS);
		return c;
	}
	
	/** datastructure used to represent a ucd - precomputes the formatting */
	private static final class UCD {		
		public final String ucd;
		public final String fullString;
		public String toString() {
			return ucd;
		}
		public String toFullString() {
			return fullString;
		}
		public UCD(final String ucd, final String description) {
			super();
			this.ucd = ucd;
			this.fullString =  ucd + " : " + description;
		}
		public int hashCode() {
			final int PRIME = 31;
			int result = 1;
			result = PRIME * result + ((this.ucd == null) ? 0 : this.ucd.hashCode());
			return result;
		}
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final UCD other = (UCD) obj;
			if (this.ucd == null) {
				if (other.ucd != null)
					return false;
			} else if (!this.ucd.equals(other.ucd))
				return false;
			return true;
		}
		
	}
	
	private void populate(List l) {
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
				l.add(new UCD(strings[0],strings[1]));
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
