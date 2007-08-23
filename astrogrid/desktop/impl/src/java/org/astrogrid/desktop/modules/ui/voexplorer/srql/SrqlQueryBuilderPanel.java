/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.srql;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
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
import java.util.Arrays;
import java.util.EventListener;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.text.JTextComponent;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.desktop.icons.IconHelper;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.ObservableElementList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;
import ca.odell.glazedlists.matchers.TextMatcherEditor;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
import ca.odell.glazedlists.swing.JEventListPanel;

/** A UI component that builds a SRQL query.
 * Based on a list of clauses, each of which is formed from one clausetemplate.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 24, 20071:20:52 PM
 */
public class SrqlQueryBuilderPanel extends JPanel  implements ObservableElementList.Connector, ActionListener {


	static final Log logger = LogFactory
			.getLog(SrqlQueryBuilderPanel.class);

	public SrqlQueryBuilderPanel() {
		super();
		// clauses is a list that detects changes in-place to it's items.
		this.clauses = new ObservableElementList(new BasicEventList(),this);
		clauses.addListEventListener(new ListEventListener() {

			public void listChanged(ListEvent arg0) {
				while(arg0.hasNext()) {
					arg0.next();
					if (arg0.getType() != ListEvent.UPDATE) { //i.e. is a delete or add
						switch(clauses.size()) {
						case 0:
							break;
						case 1:
							((Clause)clauses.get(0)).getRemoveButton().setEnabled(false);
							break;
						default:
							((Clause)clauses.get(0)).getRemoveButton().setEnabled(true);							
						}
					}
				}
			}
		});
		clauses.add(new Clause());
			
		// this panel displays contents of a list.
		JEventListPanel clausePanel = new JEventListPanel(clauses, new ClauseFormat());
		this.setLayout(new BorderLayout());
		
		JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
		top.add(new JLabel("Contains resources which match"));
		anyAll = new JComboBox(new String[]{"all","any"});
		anyAll.setEditable(false);
		anyAll.addActionListener(this);
		top.add(anyAll);
		top.add(new JLabel("of the following conditions:"));
		this.add(top,BorderLayout.NORTH);
		
		this.add(clausePanel,BorderLayout.CENTER);
		//@todo implement query support for this before enabling ui.
	//	JCheckBox oldResources = new JCheckBox("Include inactive and deleted Resources");
	//	this.add(oldResources,BorderLayout.SOUTH);
	}
	private final EventList clauses;
	private final JComboBox  anyAll;
	private static final int ALL = 0;
	private static final int ANY = 1;
	/** access a live view of the clauses being built. */
	public EventList getClauses() {
		return clauses;
	}
	
	/** resets hte form */
	public void reset() {
		anyAll.setSelectedIndex(0);
		clauses.clear();
		clauses.add(new Clause());
	}
	
	/** access the query that is represented by the current clauses */
	public SRQL getQuery() {
		Iterator i = clauses.iterator();
		SRQL q = ((Clause)i.next()).getClause();
		while (i.hasNext()) {
			BinaryOperatorSRQL bin;
			if (anyAll.getSelectedIndex() == ALL) {
				bin = new AndSRQL();
			} else {
				bin = new OrSRQL();
			}
			bin.setLeft(q);
			bin.setRight(((Clause)i.next()).getClause());
			q = bin;
		}
		return q;
	}
	
	/** check that we can display the supplied query in this panel.
	 * any query constructed previously by this panel can be displayed in it.
	 * @param query
	 */
	public boolean canDisplayQuery(SRQL query) {
		PanelVerifyingVisitor v = new PanelVerifyingVisitor();
		query.accept(v);
		return v.canDisplay();
	}
	/** visit a candidate query and verify it can be displayed in this panel */
	private static class PanelVerifyingVisitor implements SRQLVisitor {
		public boolean canDisplay() {
			return 
				(
						(andSeen == false && orSeen == false) // simple single-clause 
						|| (andSeen != orSeen) // no mixture of ands and ors.
				) 
				&& ! unknownXPathSeen
				&& ! complexNegation
				&& ! complexTarget && ! unknownTarget
				&& ! phraseSeen
				;
		}
		
		private boolean andSeen;
		private boolean orSeen;
		private boolean unknownXPathSeen;
		private boolean complexNegation;
		private boolean complexTarget;
		private boolean unknownTarget;
		private boolean phraseSeen;
		public Object visit(AndSRQL q) {
			andSeen = true;
			q.getLeft().accept(this);
			q.getRight().accept(this);
			return null;
		}

		public Object visit(OrSRQL q) {
			orSeen = true;
			q.getLeft().accept(this);
			q.getRight().accept(this);
			return null;
		}

		public Object visit(NotSRQL q) { // can only have a single term inside a neg.
			if (! (q.getChild() instanceof TermSRQL)) {
				complexNegation = true;
			}
			return null;
		}

		public Object visit(TermSRQL q) {// always ok
			return null;
		}

		public Object visit(PhraseSRQL q) {// not expected at the moment.
			phraseSeen = true;
			return null;
		}

		public Object visit(TargettedSRQL q) { // can only have a neg, or a term inside a target
			SRQL c = q.getChild();
			if (! ( c instanceof TermSRQL || c instanceof NotSRQL) ) {
				complexTarget = true;
			}
			// check it's a target we know how to handle.
			String target = q.getTarget();
			for (int i = 0; i < clauseTemplates.length; i++) {
				if (target.equals(clauseTemplates[i].target)) {
					return null;
				}
			}
			unknownTarget = true;
			return null;
		}

		public Object visit(XPathSRQL q) {
			unknownXPathSeen = true; // can't handle any xpath at the moment.
			return null;
		}
	}
	
	
	/** set the query that should be displayed in this panel - will configure 
	 * UI clauses so that they model this query.
	 * @param query
	 * @throws IllegalArgumentException if <tt>canDisplayQuery(query) == false</tt>
	 * precondition: {@link #canDisplayQuery(SRQL)}
	 */
	public void setQuery(SRQL query) throws IllegalArgumentException {
		if (! canDisplayQuery(query)) {
			throw new IllegalArgumentException("This query has a structure or clauses that cannot be displayed in this panel - use the text form instead");
		}	
		clauses.clear();
		PanelPopulatingVisitor vis = new PanelPopulatingVisitor();
		query.accept(vis);
	}
	/** traverse a query and populate the panel accordingly 
	 * doesn't work for general queries - assumes specfic structure.
	 * */
	private class PanelPopulatingVisitor implements SRQLVisitor {
		public Object visit(AndSRQL q) {
			anyAll.setSelectedIndex(ALL);
			q.getLeft().accept(this);
			q.getRight().accept(this);
			return null;
		}

		public Object visit(OrSRQL q) {
			anyAll.setSelectedIndex(ANY);
			q.getLeft().accept(this);
			q.getRight().accept(this);
			return null;
		}

		public Object visit(NotSRQL q) { // if we've visited a 'not' node it means no target was defined.
			// in this case, we want the 'default' target - which is what clause is created with.
			Clause c= new Clause();
			c.setClause(q);
			clauses.add(c);
			return null;
		}

		public Object visit(TermSRQL q) {  // if we've visited a 'not' node it means no target was defined.
			Clause c= new Clause();
			c.setClause(q);
			clauses.add(c);			
			return null;
		}

		public Object visit(PhraseSRQL q) {
			throw new RuntimeException("Programming error - have already validated srql query, and don't expect a phrase");
		}

		public Object visit(TargettedSRQL q) {
			// find correct clause template, instantiate a new clause based on this template, then populate using children.
			String target = q.getTarget();
			ClauseTemplate ct = null;
			for (int i = 0; i < clauseTemplates.length; i++) {
				if (target.equals(clauseTemplates[i].target)) {
					ct= clauseTemplates[i];
				}
			}
			Clause c = new Clause();
			c.setSelectedItem(ct);
			c.setClause(q.getChild());
			clauses.add(c);
			return null;
		}

		public Object visit(XPathSRQL q) {
			throw new RuntimeException("Programming error - have already validated srql query, and don't expect an xpath");
		}
	}

//END of puplic API.
	
	// connector interface - listens to changes, and triggers change events.
		public EventListener installListener(Object arg0) {
			Clause c = (Clause)arg0;
			c.addActionListener(this);
			return this; // we listen to stuff ourselves.
		}
		private ObservableElementList l;
		public void setObservableElementList(ObservableElementList arg0) {
			this.l = arg0;
		}

		public void uninstallListener(Object arg0, EventListener arg1) {
			if (arg1 == this) {
				Clause c = (Clause)arg0;
				c.removeActionListener(this);
			}
		}
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == anyAll) {
				// work around - need someting to notify with, but 0th element not always there to start with.
				if (l.size() > 0) {
					l.elementChanged(l.get(0));
				}
			} else {
				l.elementChanged(e.getSource()); // source is the clause Object itself.
			}
		}

	/** list of all the things possible to filter on */
	private static ClauseTemplate[] clauseTemplates = new ClauseTemplate[]{
		new TextMatchTemplate("Any main field","default") {{
			setTooltip("Search in  Title, Subject, Identifier, Shortname and Description fields" );
		}}
		, new TextMatchTemplate("title")
		, new TextMatchTemplate("subject")
		, new TextMatchTemplate("description")
		, new EnumerationTemplate("waveband",new String[]{"Radio","Millimeter","Infrared","Optical","UV","EUV","X-ray","Gamma-ray"})

		// I've not put in the whole list of types - andy doesn't want them all.
		// this is a mix of stuff in subject/type and @xsi:type - shortest distinct prefixes.
		, new DescribedEnumerationTemplate("Type","type"){{
			setTooltip("Search in 'Resource Type' and 'Content - Type'");			
		}
		protected void populate(List vals) {
			vals.add(new DescribedValue("Archive",""));
			vals.add(new DescribedValue("Catalog",""));
			vals.add(new DescribedValue("Survey",""));
			vals.add(new DescribedValue("Simulation",""));
			vals.add(new DescribedValue("BasicData",""));
			vals.add(new DescribedValue("Cone","Catalog cone search service"));
			vals.add(new DescribedValue("Image","Image access service (SIAP)"));
			vals.add(new DescribedValue("Spectrum","Spectrum access service (SSAP)"));
			vals.add(new DescribedValue("Time","Time Series access service (STAP)"));
			vals.add(new DescribedValue("CeaApplication","Offline application (CEA)"));
			vals.add(new DescribedValue("Registry",""));
			vals.add(new DescribedValue("Service","Other services"));			
		}}
		//@future - replace these with capabilities -
		//   @future capability - tricky. - don't know how to do this yet. - mix of capability and capabilitty type.
		// will come out with registry 1.0
		// @future also to add - validation-level 
		// @future add date-based querying.
		
		, new UcdClauseTemplate()
		, new TextMatchTemplate("Any column name","col")
		
		, new EnumerationTemplate("Content level","level",new String[]{"General","Elementary Education","Middle School Education","Secondary Education","Community College","University","Research","Amateur","Informal Education"})
		, new TextMatchTemplate("Curator","curation")
		, new TextMatchTemplate("Short name","shortname")
		, new TextMatchTemplate("Identifier","id")

		,new TextMatchTemplate("Any field","any")
	};
	
	/** represents one line in the query
	 * is a combo box itself, selecting on a the list of clause templates.
	 * also propagates events fired by sub-components - so the clauses eventlist
	 * is always notified of changes.
	 * @author Noel.Winstanley@manchester.ac.uk
	 * @since Apr 24, 20078:32:09 PM
	 */
	private class Clause extends JComboBox implements ActionListener, ItemListener{
		public Clause() {
			super(clauseTemplates);// list of possibilities is shared between all clauses.
			addItemListener(this);
			setEditable(false);
			setSelectedIndex(0);
			setCurrentTemplate((ClauseTemplate)getSelectedItem());
			setRenderer(new ToolTipComboBoxRenderer());
			// control buttons.
			 add = new JButton(IconHelper.loadIcon("editadd16.png"));
			 add.setToolTipText("Add a new clause to this query");
			 remove = new JButton(IconHelper.loadIcon("editremove16.png"));
			 remove.setToolTipText("Remove this clause from the query");
			 add.addActionListener(this);
			 remove.addActionListener(this);

		}
		// adds tooltips
		private class ToolTipComboBoxRenderer extends BasicComboBoxRenderer {
		    public Component getListCellRendererComponent( JList list, 
		           Object value, int index, boolean isSelected, boolean cellHasFocus) {
		      if (isSelected) {
		        setBackground(list.getSelectionBackground());
		        setForeground(list.getSelectionForeground());      
		        if (-1 < index) {
		        	String s = ((ClauseTemplate)value).getTooltip();
		        	list.setToolTipText(s);
		        }
		      } else {
		        setBackground(list.getBackground());
		        setForeground(list.getForeground());
		      } 
		      setFont(list.getFont());
		      setText((value == null) ? "" : value.toString());     
		      return this;
		    }  
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
		
		public void setClause(SRQL clause) {
			currentTemplate.displayClause(clause,valueField,predicateField);
		}
		
		private final JButton add;
		private final JButton remove;
		// listen to various sub-components.
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == add) {
				clauses.add(new Clause());
			} else if (e.getSource() == remove) {
				clauses.remove(this);
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
		public void itemStateChanged(ItemEvent e) {
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
		private void setCurrentTemplate(ClauseTemplate ct) {
			currentTemplate = ct;
			valueField = currentTemplate.createValueField();
			predicateField = currentTemplate.createPredicateField();
			// listen to these new fields, if possible.
			registerListenersIfPossible(valueField);
			registerListenersIfPossible(predicateField);
		}
		// uses bean introspection to find out whether the ActionListener event is supported,
		// and listen to this if present.
		private void registerListenersIfPossible(JComponent comp) {
			try {
				BeanInfo beanInfo = Introspector.getBeanInfo(comp.getClass());
				EventSetDescriptor[] esds = beanInfo.getEventSetDescriptors();
				for (int i = 0; i < esds.length; i++) {
					if (esds[i].getListenerType().equals(ActionListener.class)) {
						Method m = esds[i].getAddListenerMethod();
						m.invoke(comp,new Object[]{this});
						break; // found it - will only occur once.
					}
				}
			} catch (IntrospectionException x) {
				logger.warn("Failed To register listener",x);
			} catch (IllegalArgumentException x) {
				logger.warn("Failed To register listener",x);
			} catch (IllegalAccessException x) {
				logger.warn("Failed To register listener",x);
			} catch (InvocationTargetException x) {
				logger.warn("Failed To register listener",x);
			}
		}
	}
	
	/** definition of a 'template' - a possible search expression
	 * 
	 * names the template, and has factory methods that will create the correct
	 * form components for it. 
	 *  */
	public static abstract class ClauseTemplate {
		public ClauseTemplate(final String name) {
			this.name = name;
			this.target = name;
		}
		public ClauseTemplate(final String name, final String target) {
			super();
			this.name = name;
			this.target = target;
		}
		public final String name;
		public final String target;
		private String tooltip;

		public final String getTooltip() {
			return this.tooltip;
		}

		public final void setTooltip(String tooltip) {
			this.tooltip = tooltip;
		}
		/** create the SRQL expression represented by this clause.
		 */
		abstract SRQL constructClause(JComponent valueField, JComponent predicateField) ;
		/** display the parameter SRQL expression in these components */
		abstract void displayClause(SRQL clause,JComponent valueField, JComponent predicateField) ;
		// access a comp[onent that allows the user to select the value to match against
		abstract JComponent createValueField();
		// access a componet that allows the user to select how the match should happen
		abstract JComponent createPredicateField();
		public String toString() {
			return StringUtils.capitalize(name);
		}
		
	}
	
	/** a clause that matches a value from an enumeration */
	static class EnumerationTemplate extends ClauseTemplate {
		/** construct an enumeration template
		 * @param name name of this clause - also used as the srql target.
		 * @param list of possible values to match against.
		 */
		public EnumerationTemplate(String name, String[] vals) {
			super(name);
			this.vals = vals;
		}
		/** construct an enumeration template
		 * @param name name of this clause
		 * @param target the srql target to query against.
		 * @param list of possible values to match against.
		 */
		public EnumerationTemplate(String name, String target,String[] vals) {
			super(name,target);
			this.vals = vals;
		}		
		private final String[] vals;

		SRQL constructClause(JComponent valueField, JComponent predicateField) {
			TargettedSRQL t = new TargettedSRQL();
			t.setTarget(target);
			TermSRQL ts = new TermSRQL();
			String val = ((JComboBox)valueField).getSelectedItem().toString();
			ts.setTerm(val);
			JComboBox predicate = (JComboBox)predicateField;
			switch (predicate.getSelectedIndex()) {
				case IS: // contains
					t.setChild(ts);
					return t;
				case ISNOT: // does not contain
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
			val.setSelectedItem(((TermSRQL)clause).getTerm());
		}		
		
		protected static final String[] predicates= new String[]{"is","is not"};
		protected final static int IS = 0;
		protected final static int ISNOT = 1;
		JComponent createPredicateField() {
			JComboBox c = new JComboBox(predicates);
			c.setEditable(false);
			c.setSelectedIndex(0);
			return c;
		}

		JComponent createValueField() {
			EventList l =GlazedLists.eventList(Arrays.asList(vals));
			
			JComboBox c = new JComboBox();
			//c.setEditable(false);
			//c.setSelectedIndex(0);
			AutoCompleteSupport support = AutoCompleteSupport.install(c,l);
			c.setSelectedIndex(0);
			support.setSelectsTextOnFocusGain(true);
			support.setStrict(true);
			support.setFilterMode(TextMatcherEditor.CONTAINS);
			return c;
		}

	}
	
	/** a clause that matches on a string value */
	static class TextMatchTemplate extends ClauseTemplate {
		public TextMatchTemplate(String name) {
			super(name);
		}
		public TextMatchTemplate(String name,String target) {
			super(name,target);
		}
		private static final String[] predicates = new String[] {"contains","does not contain"};
			//@todo,"starts with","ends with","is","is not"};
		private static final int CONTAINS = 0;
		private static final int NOTCONTAINS = 1;
		SRQL constructClause(JComponent valueField, JComponent predicateField) {
			TargettedSRQL t = new TargettedSRQL();
			t.setTarget(target);
			TermSRQL ts = new TermSRQL();
			String val = ((JTextField)valueField).getText();
			ts.setTerm(val);			
			JComboBox predicate = (JComboBox)predicateField;
			switch (predicate.getSelectedIndex()) {
				case CONTAINS:
					t.setChild(ts);
					return t;
				case NOTCONTAINS:
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
			JTextField val = (JTextField)valueField;
			if (clause instanceof NotSRQL) {
				pred.setSelectedIndex(NOTCONTAINS);
				clause = ((NotSRQL)clause).getChild(); // traverse down.
			} else {
				pred.setSelectedIndex(CONTAINS);
			}
			val.setText(((TermSRQL)clause).getTerm());
		}	
		JComponent createPredicateField() {
			JComboBox c = new JComboBox(predicates);
			c.setEditable(false);
			c.setSelectedIndex(0);
			return c;
		}

		JComponent createValueField() {
			JTextField f = new  JTextField() {{
				getDocument().addDocumentListener(new DocumentListener() {
					
					public void changedUpdate(DocumentEvent e) {
						fireActionPerformed();
					}
					
					public void insertUpdate(DocumentEvent e) {
						fireActionPerformed();
					}
					
					public void removeUpdate(DocumentEvent e) {
						fireActionPerformed();
					}
				});
			}};
			return f;
		}
	}
	
	/** defines how to format a Clause in the swing panel */
	private static class ClauseFormat extends JEventListPanel.AbstractFormat {
		public ClauseFormat() {
			super("d","60dlu:grow,2dlu,60dlu:grow,2dlu,60dlu:grow,6dlu,d,d","0dlu","2dlu"
					,new String[]{"1,1","3,1","5,1","7,1","8,1"});//col,row for each item.
		}
		public JComponent getComponent(Object arg0, int arg1) {
			Clause c= (Clause)arg0;
			switch (arg1) {
			case 0: 
				return c;
			case 1:
				return c.getPredicateField();
			case 2:
				return c.getValueField();
			case 3:
				return c.getAddButton();
			case 4:
				return c.getRemoveButton();
			default:
				return new JLabel("unexpected"); // should never happen
			}
		}
		public int getComponentsPerElement() {
			return 5;
		}
	}
	
	// main method - for testing only
	public static void main(String[] args) {
		newPane();
	}
	// part of testing only.
	private static SrqlQueryBuilderPanel newPane() {
		JFrame f = new JFrame();
		final SrqlQueryBuilderPanel pan = new SrqlQueryBuilderPanel();
		f.getContentPane().add(pan);
		f.pack();
		f.setVisible(true);
		final KeywordSRQLVisitor vis = new KeywordSRQLVisitor();
		JButton b = new JButton("Clone"); // button can be used to test getting and setting of query.
		pan.add(b,BorderLayout.SOUTH);
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SRQL q = pan.getQuery();				
				SrqlQueryBuilderPanel p1 = newPane();
				p1.setQuery(q); 
			}
		});
		return pan;
	}
}
