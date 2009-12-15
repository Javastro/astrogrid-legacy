/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.srql;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.EventListener;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
 * @TEST lots in there - split up and test a bit.
 */
public class SrqlQueryBuilderPanel extends JPanel  implements ObservableElementList.Connector<Clause>, ActionListener {


	static final Log logger = LogFactory
			.getLog(SrqlQueryBuilderPanel.class);

	public SrqlQueryBuilderPanel() {
		super();
		// clauses is a list that detects changes in-place to it's items.
		this.clauses = new ObservableElementList<Clause>(new BasicEventList<Clause>(),this);
		clauses.addListEventListener(new ListEventListener<Clause>() {

			public void listChanged(final ListEvent<Clause> arg0) {
				while(arg0.hasNext()) {
					arg0.next();
					if (arg0.getType() != ListEvent.UPDATE) { //i.e. is a delete or add
						switch(clauses.size()) {
						case 0:
							break;
						case 1:
							clauses.get(0).getRemoveButton().setEnabled(false);
							break;
						default:
							clauses.get(0).getRemoveButton().setEnabled(true);							
						}
					}
				}
			}
		});
		clauses.add(new Clause(this));
			
		// this panel displays contents of a list.
		final JEventListPanel<Clause> clausePanel = new JEventListPanel<Clause>(clauses, new ClauseFormat());
		this.setLayout(new BorderLayout());
		
		final JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
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
	final EventList<Clause> clauses;
	private final JComboBox  anyAll;
	private static final int ALL = 0;
	private static final int ANY = 1;
	/** access a live view of the clauses being built. */
	public EventList<Clause> getClauses() {
		return clauses;
	}
	
	/** resets hte form */
	public void reset() {
		anyAll.setSelectedIndex(0);
		clauses.clear();
		clauses.add(new Clause(this));
	}
	
	/** access the query that is represented by the current clauses */
	public SRQL getQuery() {
		final Iterator<Clause> i = clauses.iterator();
		SRQL q = i.next().getClause();
		while (i.hasNext()) {
			BinaryOperatorSRQL bin;
			if (anyAll.getSelectedIndex() == ALL) {
				bin = new AndSRQL();
			} else {
				bin = new OrSRQL();
			}
			bin.setLeft(q);
			bin.setRight(i.next().getClause());
			q = bin;
		}
		return q;
	}
	
	/** check that we can display the supplied query in this panel.
	 * any query constructed previously by this panel can be displayed in it.
	 * @param query
	 */
	public boolean canDisplayQuery(final SRQL query) {
		final PanelVerifyingVisitor v = new PanelVerifyingVisitor();
		query.accept(v);
		return v.canDisplay();
	}
	/** Examines a query and verifies it can be displayed in this panel */
	private static class PanelVerifyingVisitor implements SRQLVisitor<Void> {
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
		public Void visit(final AndSRQL q) {
			andSeen = true;
			q.getLeft().accept(this);
			q.getRight().accept(this);
			return null;
		}

		public Void visit(final OrSRQL q) {
			orSeen = true;
			q.getLeft().accept(this);
			q.getRight().accept(this);
			return null;
		}

		public Void visit(final NotSRQL q) { // can only have a single term inside a neg.
			if (! (q.getChild() instanceof TermSRQL)) {
				complexNegation = true;
			}
			return null;
		}

		public Void visit(final TermSRQL q) {// always ok
			return null;
		}

		public Void visit(final PhraseSRQL q) {// not expected at the moment.
			phraseSeen = true;
			return null;
		}

		public Void visit(final TargettedSRQL q) { // can only have a neg, or a term inside a target
			final SRQL c = q.getChild();
			if (! ( c instanceof TermSRQL || c instanceof NotSRQL) ) {
				complexTarget = true;
			}
			// check it's a target we know how to handle.
			final String target = q.getTarget();
			for (int i = 0; i < clauseTemplates.length; i++) {
				if (target.equals(clauseTemplates[i].target)) {
					return null;
				}
			}
			unknownTarget = true;
			return null;
		}

		public Void visit(final XPathSRQL q) {
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
	public void setQuery(final SRQL query) throws IllegalArgumentException {
		if (! canDisplayQuery(query)) {
			throw new IllegalArgumentException("This query has a structure or clauses that cannot be displayed in this panel - use the text form instead");
		}	
		clauses.clear();
		final PanelPopulatingVisitor vis = new PanelPopulatingVisitor();
		query.accept(vis);
	}
	/** Traverse a query and populate the builder panel from it. 
	 * doesn't work for general queries - assumes specfic structure.
	 * */
	private class PanelPopulatingVisitor implements SRQLVisitor<Void> {
		public Void visit(final AndSRQL q) {
			anyAll.setSelectedIndex(ALL);
			q.getLeft().accept(this);
			q.getRight().accept(this);
			return null;
		}

		public Void visit(final OrSRQL q) {
			anyAll.setSelectedIndex(ANY);
			q.getLeft().accept(this);
			q.getRight().accept(this);
			return null;
		}

		public Void visit(final NotSRQL q) { // if we've visited a 'not' node it means no target was defined.
			// in this case, we want the 'default' target - which is what clause is created with.
			final Clause c= new Clause(SrqlQueryBuilderPanel.this);
			c.setClause(q);
			clauses.add(c);
			return null;
		}

		public Void visit(final TermSRQL q) {  // if we've visited a 'not' node it means no target was defined.
			final Clause c= new Clause(SrqlQueryBuilderPanel.this);
			c.setClause(q);
			clauses.add(c);			
			return null;
		}

		public Void visit(final PhraseSRQL q) {
			throw new RuntimeException("Programming error - have already validated srql query, and don't expect a phrase");
		}

		public Void visit(final TargettedSRQL q) {
			// find correct clause template, instantiate a new clause based on this template, then populate using children.
			final String target = q.getTarget();
			ClauseTemplate ct = null;
			for (int i = 0; i < clauseTemplates.length; i++) {
				if (target.equals(clauseTemplates[i].target)) {
					ct= clauseTemplates[i];
				}
			}
			final Clause c = new Clause(SrqlQueryBuilderPanel.this);
			c.setSelectedItem(ct);
			c.setClause(q.getChild());
			clauses.add(c);
			return null;
		}

		public Void visit(final XPathSRQL q) {
			throw new RuntimeException("Programming error - have already validated srql query, and don't expect an xpath");
		}
	}

//END of puplic API.
	
	// connector interface - listens to changes, and triggers change events.
		public EventListener installListener(final Clause c) {
			c.addActionListener(this);
			return this; // we listen to stuff ourselves.
		}
		private ObservableElementList<Clause> l;
		public void setObservableElementList(final ObservableElementList<? extends Clause> arg0) {
			this.l = (ObservableElementList<Clause>)arg0;
		}

		public void uninstallListener(final Clause c, final EventListener arg1) {
			if (arg1 == this) {
				c.removeActionListener(this);
			}
		}
		public void actionPerformed(final ActionEvent e) {
			if (e.getSource() == anyAll) {
				// work around - need someting to notify with, but 0th element not always there to start with.
				if (l.size() > 0) {
					l.elementChanged(l.get(0));
				}
			} else {
				l.elementChanged((Clause)e.getSource()); // source is the clause Object itself.
			}
		}

	/** list of all the things possible to filter on */
	static ClauseTemplate[] clauseTemplates = new ClauseTemplate[]{
		new TextMatchTemplate("Any main field","default") {{
			setTooltip("Search in  Title, Subject, IVOA-ID, Shortname and Description fields" );
		}}
		, new TextMatchTemplate("title")
		// service capability
		, new DescribedEnumerationTemplate("Service capability","capability"){{
		    setTooltip("Search in 'Capability - Type' and 'Capability - StandardID");          	           		   
		    }
		    @Override
		    protected void populate(final List<DescribedValue> vals) {
	            vals.add(new DescribedValue("Cone","Catalog cone search service"));
	            vals.add(new DescribedValue("Image","Image access service (SIAP)"));
	            vals.add(new DescribedValue("Spectral","Spectrum access service (SSAP)"));
	            vals.add(new DescribedValue("Time","Time range access service (STAP)"));
	            vals.add(new DescribedValue("/TAP","Table access service (TAP)")); // '/' necessary to match prefix of capability url.

	            vals.add(new DescribedValue("VOSpace","VOSpace remote storage"));
	            // don't want this - no fun.
	            //vals.add(new DescribedValue("CeaCapability","Offline application server (CEA)"));
		    }
		}
		
	      , new DescribedEnumerationTemplate("Resource type","resourcetype"){{
	            setTooltip("Search in 'Resource Type''");                                 
	            }
	            @Override
	            protected void populate(final List<DescribedValue> vals) {
	                
	                vals.add(new DescribedValue("CeaApplication","Remote application (CEA)"));
	                vals.add(new DescribedValue("DataCollection",""));
	                vals.add(new DescribedValue("DataService",""));
	                vals.add(new DescribedValue("Resource",""));
	                vals.add(new DescribedValue("CatalogService",""));
	                vals.add(new DescribedValue("Registry",""));
	                vals.add(new DescribedValue("Authority",""));
	                vals.add(new DescribedValue("Organisation",""));
	                //vals.add(new DescribedValue("Application",""));
	                vals.add(new DescribedValue("Service","All services")); 	                
	            }
	        }
		// reswource type
//        , new DescribedEnumerationTemplate("Type","type"){{
//            setTooltip("Search in 'Resource Type', 'Content - Type' and 'Capability - Type'");          
//        }
//        @Override
//        protected void populate(final List<DescribedValue> vals) {
//            vals.add(new DescribedValue("Archive",""));
//            vals.add(new DescribedValue("Catalog","")); // includes catalog service.
//            vals.add(new DescribedValue("Survey",""));
//            vals.add(new DescribedValue("Simulation",""));
//            vals.add(new DescribedValue("BasicData",""));
//
// 
//            vals.add(new DescribedValue("DataCollection",""));
//            vals.add(new DescribedValue("DataService",""));
//            vals.add(new DescribedValue("TableService",""));
//            vals.add(new DescribedValue("CatalogService",""));
//            vals.add(new DescribedValue("Registry",""));
//            vals.add(new DescribedValue("Authority",""));
//            vals.add(new DescribedValue("Organisation",""));
//            vals.add(new DescribedValue("CeaCapability","Offline application server (CEA)"));
//            vals.add(new DescribedValue("Service","All services")); 
//        }}
		//
		, new TextMatchTemplate("subject")
		, new TextMatchTemplate("description")
		, new EnumerationTemplate("waveband",new String[]{"Radio","Millimeter","Infrared","Optical","UV","EUV","X-ray","Gamma-ray"})


		// publisher
		,new TextMatchTemplate("publisher")
		,new TextMatchTemplate("creator")
		, new TextMatchTemplate("Any curation field","curation")
		

		, new UcdClauseTemplate() // rename to Table Column UCD [includes | not includes]
		, new TextMatchTemplate("IVOA-ID","id")

		,new TextMatchTemplate("Any field","any")
		
		// none of the following are wanted.
		//, new TextMatchTemplate("Any column name","col")
		
//		, new EnumerationTemplate("Content level","level",new String[]{"General","Elementary Education","Middle School Education","Secondary Education","Community College","University","Research","Amateur","Informal Education"})
//		, new TextMatchTemplate("Short name","shortname")
//		, new TextMatchTemplate("Source","source")
	};
	
	/** A 'template' for one line of the search expression.
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

		public final void setTooltip(final String tooltip) {
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
		@Override
        public String toString() {
			return StringUtils.capitalize(name);
		}
		
	}
	
	/** a clause template that matches a value from an enumeration */
	static class EnumerationTemplate extends ClauseTemplate {
		/** construct an enumeration template
		 * @param name name of this clause - also used as the srql target.
		 * @param list of possible values to match against.
		 */
		public EnumerationTemplate(final String name, final String[] vals) {
			super(name);
			this.vals = vals;
		}
		/** construct an enumeration template
		 * @param name name of this clause
		 * @param target the srql target to query against.
		 * @param list of possible values to match against.
		 */
		public EnumerationTemplate(final String name, final String target,final String[] vals) {
			super(name,target);
			this.vals = vals;
		}		
		private final String[] vals;

		@Override
        SRQL constructClause(final JComponent valueField, final JComponent predicateField) {
			final TargettedSRQL t = new TargettedSRQL();
			t.setTarget(target);
			final TermSRQL ts = new TermSRQL();
			final String val = ((JComboBox)valueField).getSelectedItem().toString();
			ts.setTerm(val);
			final JComboBox predicate = (JComboBox)predicateField;
			switch (predicate.getSelectedIndex()) {
				case IS: // contains
					t.setChild(ts);
					return t;
				case ISNOT: // does not contain
					final NotSRQL s = new NotSRQL();
					s.setChild(ts);
					t.setChild(s);
					return t;
				default:
					throw new RuntimeException("Programming Error: index=" + predicate.getSelectedIndex());
			}		
		}
		@Override
        void displayClause(SRQL clause, final JComponent valueField, final JComponent predicateField) {
			final JComboBox pred = (JComboBox)predicateField;
			final JComboBox val = (JComboBox)valueField;
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
		@Override
        JComponent createPredicateField() {
			final JComboBox c = new JComboBox(predicates);
			c.setEditable(false);
			c.setSelectedIndex(0);
			return c;
		}

		@Override
        JComponent createValueField() {
			final EventList<String> l =GlazedLists.eventList(Arrays.asList(vals));
			
			final JComboBox c = new JComboBox();
			//c.setEditable(false);
			//c.setSelectedIndex(0);
			final AutoCompleteSupport<String> support = AutoCompleteSupport.install(c,l);
			c.setSelectedIndex(0);
			support.setSelectsTextOnFocusGain(true);
			support.setStrict(true);
			support.setFilterMode(TextMatcherEditor.CONTAINS);
			return c;
		}

	}
	
	/** a clause template that matches on a string value */
	static class TextMatchTemplate extends ClauseTemplate {
		public TextMatchTemplate(final String name) {
			super(name);
		}
		public TextMatchTemplate(final String name,final String target) {
			super(name,target);
		}
		private static final String[] predicates = new String[] {"contains","does not contain"};
		private static final int CONTAINS = 0;
		private static final int NOTCONTAINS = 1;
		@Override
        SRQL constructClause(final JComponent valueField, final JComponent predicateField) {
			final TargettedSRQL t = new TargettedSRQL();
			t.setTarget(target);
			final TermSRQL ts = new TermSRQL();
			final String val = ((JTextField)valueField).getText();
			ts.setTerm(val);			
			final JComboBox predicate = (JComboBox)predicateField;
			switch (predicate.getSelectedIndex()) {
				case CONTAINS:
					t.setChild(ts);
					return t;
				case NOTCONTAINS:
					final NotSRQL s = new NotSRQL();
					s.setChild(ts);
					t.setChild(s);
					return t;
				default:
					throw new RuntimeException("Programming Error: index=" + predicate.getSelectedIndex());
			}	
		}
		@Override
        void displayClause(SRQL clause, final JComponent valueField, final JComponent predicateField) {
			final JComboBox pred = (JComboBox)predicateField;
			final JTextField val = (JTextField)valueField;
			if (clause instanceof NotSRQL) {
				pred.setSelectedIndex(NOTCONTAINS);
				clause = ((NotSRQL)clause).getChild(); // traverse down.
			} else {
				pred.setSelectedIndex(CONTAINS);
			}
			val.setText(((TermSRQL)clause).getTerm());
		}	
		@Override
        JComponent createPredicateField() {
			final JComboBox c = new JComboBox(predicates);
			c.setEditable(false);
			c.setSelectedIndex(0);
			return c;
		}

		@Override
        JComponent createValueField() {
			final JTextField f = new  JTextField() {{
				getDocument().addDocumentListener(new DocumentListener() {
					
					public void changedUpdate(final DocumentEvent e) {
						fireActionPerformed();
					}
					
					public void insertUpdate(final DocumentEvent e) {
						fireActionPerformed();
					}
					
					public void removeUpdate(final DocumentEvent e) {
						fireActionPerformed();
					}
				});
			}};
			return f;
		}
	}
	
	/** Formats a Clause */
	private static class ClauseFormat extends JEventListPanel.AbstractFormat<Clause> {
		public ClauseFormat() {
			super("d","60dlu:grow,2dlu,60dlu:grow,2dlu,60dlu:grow,6dlu,d,d","0dlu","2dlu"
					,new String[]{"1,1","3,1","5,1","7,1","8,1"});//col,row for each item.
		}
		public JComponent getComponent(final Clause c, final int arg1) {
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
		@Override
        public int getComponentsPerElement() {
			return 5;
		}
	}

	/** adds tooltips to a combobox */
    static class ToolTipComboBoxRenderer extends BasicComboBoxRenderer {
        @Override
        public Component getListCellRendererComponent( final JList list, 
               final Object value, final int index, final boolean isSelected, final boolean cellHasFocus) {
          if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());      
            if (-1 < index) {
            	final String s = ((ClauseTemplate)value).getTooltip();
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

}
