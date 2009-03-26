/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.srql;

import java.awt.Component;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.SrqlQueryBuilderPanel.ClauseTemplate;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.matchers.TextMatcherEditor;
import ca.odell.glazedlists.swing.AutoCompleteSupport;

/** A {@link ClauseTemplate} that provides an enumeration of values, where
 * each value is displayed with a description.
 * Also provides auto-complete.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since May 9, 200711:25:53 AM
 */
public abstract class DescribedEnumerationTemplate extends ClauseTemplate {

	/**
	 * Logger for this class
	 */
	protected static final Log logger = LogFactory
				.getLog(UcdClauseTemplate.class);
	private static final String[] predicates = new String[]{"is","is not"};

	@Override
    protected void displayClause(SRQL clause, final JComponent valueField, final JComponent predicateField) {
		final JComboBox pred = (JComboBox)predicateField;
		final JComboBox val = (JComboBox)valueField;
		if (clause instanceof NotSRQL) {
			pred.setSelectedIndex(ISNOT);
			clause = ((NotSRQL)clause).getChild(); // traverse down.
		} else {
			pred.setSelectedIndex(IS);
		}
		final String term = ((TermSRQL)clause).getTerm();
		final DescribedValue u = new DescribedValue(term,"");
		val.setSelectedItem(u); // works because equals test only works on value, not on description field.
	}

	@Override
    SRQL constructClause(final JComponent valueField, final JComponent predicateField) {
		final TargettedSRQL t = new TargettedSRQL();
		t.setTarget(target);
		final TermSRQL ts = new TermSRQL();
		final Object selectedItem = ((JComboBox)valueField).getSelectedItem();
		if (selectedItem instanceof DescribedValue) {
		    ts.setTerm(((DescribedValue)selectedItem).value);
		} else if (selectedItem instanceof String) {
		    ts.setTerm((String)selectedItem);
		} else { // toString it
		    ts.setTerm(selectedItem == null ? "" : selectedItem.toString());
		}
		final JComboBox predicate = (JComboBox)predicateField;
		switch (predicate.getSelectedIndex()) {
			case IS:
				t.setChild(ts);
				return t;
			case ISNOT:
				final NotSRQL s = new NotSRQL();
				s.setChild(ts);
				t.setChild(s);
				return t;
			default:
				throw new RuntimeException("Programming Error: index=" + predicate.getSelectedIndex());
		}		
	}
	
	protected static final int IS = 0;
	protected static final int ISNOT = 1;


	public DescribedEnumerationTemplate(final String name, final String target) {
		super(name, target);
	}

	public DescribedEnumerationTemplate(final String name) {
		super(name);
	}

	@Override
    protected JComponent createPredicateField() {
		final JComboBox c = new JComboBox(predicates);
		c.setEditable(false);
		c.setSelectedIndex(0);
		return c;
	}

	@Override
    protected JComponent createValueField() {
		final EventList vals= new BasicEventList();
		populate(vals);
		final JComboBox c = new JComboBox();
		c.setRenderer(new BasicComboBoxRenderer() {
		@Override
        public Component getListCellRendererComponent(final JList list, Object value, final int index, final boolean isSelected, final boolean cellHasFocus) {
			final DescribedValue u = (DescribedValue)value;
			value = u.toFullString();
			return super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
		}
		});
		final AutoCompleteSupport support = AutoCompleteSupport.install(c,vals);
		c.setSelectedIndex(0);
		support.setSelectsTextOnFocusGain(true);
		support.setStrict(true); 
		support.setCorrectsCase(true);
		support.setFilterMode(TextMatcherEditor.CONTAINS);
		return c;
	}
	
	/** implement to populate the enumerated set of values.
	 * all items should be DescribedValues.
	 * @param vals
	 */
	protected abstract void populate(List vals);

	/** datastructure used to represent a value - precomputes the formatting */
	public static final class DescribedValue {		
		public final String value;
		public final String fullString;
		@Override
        public String toString() {
			return value;
		}
		public String toFullString() {
			return fullString;
		}
		public DescribedValue(final String v, final String description) {
			super();
			this.value = v;
			this.fullString =  v + " : " + description;
		}
		@Override
        public int hashCode() {
			final int PRIME = 31;
			int result = 1;
			result = PRIME * result + ((this.value == null) ? 0 : this.value.hashCode());
			return result;
		}
		@Override
        public boolean equals(final Object obj) {
			if (this == obj) {
                return true;
            }
			if (obj == null) {
                return false;
            }
			if (getClass() != obj.getClass()) {
                return false;
            }
			final DescribedValue other = (DescribedValue) obj;
			if (this.value == null) {
				if (other.value != null) {
                    return false;
                }
			} else if (!this.value.equals(other.value)) {
                return false;
            }
			return true;
		}
		
	}

}