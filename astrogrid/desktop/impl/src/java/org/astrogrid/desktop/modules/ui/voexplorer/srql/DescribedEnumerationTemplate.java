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
	// returns the list of labels to use as predicates.
	// doesn't have any affect on actual behaviour.
	protected String[] getPredicates() {
	    return new String[]{"is","is not"}; 
	}

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
		//final DescribedValue u = new DescribedValue(term,"");
		// doen't seem to work. try something else.
		//val.setSelectedItem(u); // works because equals test only works on value, not on description field.
		for (int i = 0; i < val.getItemCount(); i++) {
		    final DescribedValue v = (DescribedValue) val.getItemAt(i);
		    if (v.getValue().equalsIgnoreCase(term)) {
		        val.setSelectedIndex(i);
		        return;
		    }
		}
	}

	@Override
    SRQL constructClause(final JComponent valueField, final JComponent predicateField) {
		final TargettedSRQL t = new TargettedSRQL();
		t.setTarget(target);
		final TermSRQL ts = new TermSRQL();
		final Object selectedItem = ((JComboBox)valueField).getSelectedItem();
		if (selectedItem instanceof DescribedValue) {
		    ts.setTerm(((DescribedValue)selectedItem).getValue());
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
		final JComboBox c = new JComboBox(getPredicates());
		c.setEditable(false);
		c.setSelectedIndex(0);
		return c;
	}

	@Override
    protected JComponent createValueField() {
		final EventList<DescribedValue> vals= new BasicEventList<DescribedValue>();
		populate(vals);
		final JComboBox c = new JComboBox();
		c.setRenderer(new BasicComboBoxRenderer() {
		@Override
        public Component getListCellRendererComponent(final JList list, Object value, final int index, final boolean isSelected, final boolean cellHasFocus) {
			final DescribedValue u = (DescribedValue)value;
			value = u.getFullString();
			return super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
		}
		});
		final AutoCompleteSupport<DescribedValue> support = AutoCompleteSupport.install(c,vals);
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
	protected abstract void populate(List<DescribedValue> vals);

	/** datastructure used to represent a value - precomputes the formatting */
	public static final class DescribedValue {		
		private final String value;
		private final String displayVal;
		private final String fullString;

		/** returns the value of 'displayVal' */
        @Override
		public String toString() {
			return displayVal;
		}

		/** construct a described value
		 * 
		 * @param v the value - used in ui, and in SRQL quewry
		 * @param description a description of the value 
		 */
		public DescribedValue(final String v, final String description) {
			super();
			this.value = v;
			this.displayVal = v;
			this.fullString =  v + " : " + description;
		}
		
		/** construct a described value, using a different value in the display than in the srql
		 * 
		 * @param displayVal the value to show in ui
		 * @param v the value to use in the SRQL query
		 * @param description the description of the value.
		 */
	      public DescribedValue(final String displayVal,final String v, final String description) {
	            super();
	            this.value = v;
	            this.displayVal = displayVal;
	            this.fullString =  displayVal + " : " + description;
	        }
		@Override
        public int hashCode() {
			final int PRIME = 31;
			int result = 1;
			result = PRIME * result + ((this.getValue() == null) ? 0 : this.getValue().hashCode());
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
			if (this.getValue() == null) {
				if (other.getValue() != null) {
                    return false;
                }
			} else if (!this.getValue().equals(other.getValue())) {
                return false;
            }
			return true;
		}

        public String getValue() {
            return value;
        }

        public String getFullString() {
            return fullString;
        }
		
	}
	

}