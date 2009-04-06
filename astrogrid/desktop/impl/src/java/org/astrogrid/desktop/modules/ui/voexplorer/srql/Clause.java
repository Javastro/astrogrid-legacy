package org.astrogrid.desktop.modules.ui.voexplorer.srql;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.text.JTextComponent;

import org.apache.commons.lang.StringUtils;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.SrqlQueryBuilderPanel.ClauseTemplate;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.SrqlQueryBuilderPanel.ToolTipComboBoxRenderer;

/** Edits one line of the search expression.
 * is a combo box itself, selecting on a the list of clause templates.
 * also propagates events fired by sub-components - so the clauses eventlist
 * is always notified of changes.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 24, 20078:32:09 PM
 */
public class Clause extends JComboBox implements ActionListener, ItemListener{
	/**
     * 
     */
    private final SrqlQueryBuilderPanel srqlQueryBuilderPanel;
    public Clause(SrqlQueryBuilderPanel srqlQueryBuilderPanel) {
		super(SrqlQueryBuilderPanel.clauseTemplates);// list of possibilities is shared between all clauses.
        this.srqlQueryBuilderPanel = srqlQueryBuilderPanel;
		addItemListener(this);
		setEditable(false);
		setSelectedIndex(0);
		setCurrentTemplate((ClauseTemplate)getSelectedItem());
		setRenderer(new SrqlQueryBuilderPanel.ToolTipComboBoxRenderer());
		// control buttons.
		 add = new JButton(IconHelper.loadIcon("editadd16.png"));
		 add.setToolTipText("Add a new clause to this query");
		 remove = new JButton(IconHelper.loadIcon("editremove16.png"));
		 remove.setToolTipText("Remove this clause from the query");
		 add.addActionListener(this);
		 remove.addActionListener(this);

	}
	// access a component that allows a predicate on the currently selected clause
	public JComponent getPredicateField() {
		return predicateField;
	}
	// access a component that allows a value to be specified that the selected clause nedds to mathc
	public JComponent getValueField() {
		return valueField;
	}
	// a component that adds a new clause to the query
	public JComponent getAddButton() {
		return add;
	}
	// a component that removes a clause from the queyr
	public JComponent getRemoveButton() {
		return remove;
	}
	/** access the srql subexpression represented by this clause */
	public SRQL getClause() {
		return currentTemplate.constructClause(valueField,predicateField);
	}
	
	public void setClause(final SRQL clause) {
		currentTemplate.displayClause(clause,valueField,predicateField);
	}
	
	private final JButton add;
	private final JButton remove;
	// listen to various sub-components.
	@Override
    public void actionPerformed(final ActionEvent e) {
		if (e.getSource() == add) {
			this.srqlQueryBuilderPanel.clauses.add(new Clause(this.srqlQueryBuilderPanel));
		} else if (e.getSource() == remove) {
			this.srqlQueryBuilderPanel.clauses.remove(this);
		} else if (e.getSource() == valueField) {
			fireActionEvent(); //escalates
		} else if (e.getSource() == predicateField) {
			fireActionEvent();
		} else {
			super.actionPerformed(e);
		}
	}
	private JComponent valueField;
	private JComponent predicateField;
	private ClauseTemplate currentTemplate;
	// detects when the combo box selection has been altered.
	public void itemStateChanged(final ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED 
				&& currentTemplate != e.getItem()) {
		    String oldText = null;
		    if (valueField instanceof JTextComponent) {
		        // save the current input, so we can set the corresponding field in the new clause.
		        oldText = ((JTextComponent)valueField).getText();
		    }
			setCurrentTemplate((ClauseTemplate)e.getItem());
			// now see if new value field is a text component..
            if (valueField instanceof JTextComponent && StringUtils.isNotBlank(oldText)) {
                // save the current input, so we can set the corresponding field in the new clause.
                ((JTextComponent)valueField).setText(oldText);
            }				
		}
	}
	private void setCurrentTemplate(final ClauseTemplate ct) {
		currentTemplate = ct;
		valueField = currentTemplate.createValueField();
		predicateField = currentTemplate.createPredicateField();
		// listen to these new fields, if possible.
		registerListenersIfPossible(valueField);
		registerListenersIfPossible(predicateField);
	}
	// uses bean introspection to find out whether the ActionListener event is supported,
	// and listen to this if present.
	private void registerListenersIfPossible(final JComponent comp) {
		try {
			final BeanInfo beanInfo = Introspector.getBeanInfo(comp.getClass());
			final EventSetDescriptor[] esds = beanInfo.getEventSetDescriptors();
			for (int i = 0; i < esds.length; i++) {
				if (esds[i].getListenerType().equals(ActionListener.class)) {
					final Method m = esds[i].getAddListenerMethod();
					m.invoke(comp,new Object[]{this});
					break; // found it - will only occur once.
				}
			}
		} catch (final IntrospectionException x) {
			SrqlQueryBuilderPanel.logger.warn("Failed To register listener",x);
		} catch (final IllegalArgumentException x) {
			SrqlQueryBuilderPanel.logger.warn("Failed To register listener",x);
		} catch (final IllegalAccessException x) {
			SrqlQueryBuilderPanel.logger.warn("Failed To register listener",x);
		} catch (final InvocationTargetException x) {
			SrqlQueryBuilderPanel.logger.warn("Failed To register listener",x);
		}
	}
}