/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.google;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.ui.UIComponentMenuBar;
import org.astrogrid.desktop.modules.ui.comp.ExpandCollapseButton;
import org.astrogrid.desktop.modules.ui.comp.SearchField;
import org.astrogrid.desktop.modules.votech.AnnotationService;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.TransformedList;
import ca.odell.glazedlists.UniqueList;
import ca.odell.glazedlists.matchers.AbstractMatcherEditor;
import ca.odell.glazedlists.matchers.Matcher;
import ca.odell.glazedlists.matchers.MatcherEditor;
import ca.odell.glazedlists.swing.EventListModel;
import ca.odell.glazedlists.swing.EventSelectionModel;
import ca.odell.glazedlists.swing.JEventListPanel;
import ca.odell.glazedlists.swing.TextComponentMatcherEditor;

import com.l2fprod.common.swing.JCollapsiblePane;

/** Factory that builds a pipeline of resource filters, and the UI components to support them.
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 14, 20074:14:06 PM
 */
public class FilterPipelineFactory   {


	private final Preference advanced;
	private final static String SYSTEM_TOGGLE_KEY = "system.toggle.key";
	private final static String SHOW_BROWSER_KEY = "show.browser.";
    public FilterPipelineFactory(final SortedList<Resource> items, final PipelineStrategy[] strategies, final AnnotationService annotationService, final Preference advanced) {
		this.advanced = advanced;
        final int pipelineSize = 3;
		
		// system filters..
		systemToggle = new JCheckBoxMenuItem("Show Technical System Resources",false); 
		systemToggle.setToolTipText("Show technical system resources");	
		systemFilteredItems = new FilterList<Resource>(items
				,new NotToggleMatcherEditor(systemToggle,PREFERENCES,SYSTEM_TOGGLE_KEY,new SystemFilter()));
        // incremental text field..
		final SearchField sf = new SearchField("Filter results");
        sf.setToolTipText("Filter list of resources using text string");
		FilterList<Resource> filteredItems = new FilterList<Resource>(systemFilteredItems
				, new TextComponentMatcherEditor(sf.getWrappedDocument(), new ResourceTextFilterator(annotationService)));
		textField = sf;
		
		// collapsible filters.
		filterPane = new JCollapsiblePane();
		// append the length of the strategies, so we get 2 keys -one for voexplorer, one for astroscope services view.
		filterPane.setCollapsed(PREFERENCES.getBoolean(SHOW_BROWSER_KEY + strategies.length,true));
		// create the pipeline, plumbing together the various items.
		final EventList<PipelineItem> pipeline = new BasicEventList<PipelineItem>();	
		for (int i = 0; i < pipelineSize; i++) {
			final PipelineItem p = new PipelineItem(i,filteredItems,strategies,filterPane);
			pipeline.add(p);
			filteredItems = new FilterList<Resource>(filteredItems,p.getItemsMatcherEditor());
		}
		totallyFilteredItems = filteredItems;

		final JEventListPanel<PipelineItem> pipelineDisplay = new JEventListPanel<PipelineItem>(pipeline, new PipelineFormat());
		pipelineDisplay.setElementColumns(3);
		filterPane.add(pipelineDisplay);
		
		// create a toggle button to show / hide the collapsed pane.
		toggleButton = new ExpandCollapseButton(filterPane);
		final Action toggleAction = toggleButton.getAction();
		toggleAction.putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_B,UIComponentMenuBar.MENU_KEYMASK));
		toggleAction.putValue(Action.NAME,"Show Metadata Browser");
		toggleAction.putValue(Action.SHORT_DESCRIPTION,"Filter resource list using metadata");
		filterPane.addPropertyChangeListener("collapsed",new PropertyChangeListener() {

            public void propertyChange(final PropertyChangeEvent evt) {
                if (filterPane.isCollapsed()) {
                    toggleAction.putValue(Action.NAME,"Show Metadata Browser");
		            toggleAction.putValue(Action.SHORT_DESCRIPTION,"Filter resource list using metadata");
                } else {
                    toggleAction.putValue(Action.NAME,"Hide Metadata Browser");
                    toggleAction.putValue(Action.SHORT_DESCRIPTION,"View unfiltered resource list");
                }
                
                PREFERENCES.putBoolean(SHOW_BROWSER_KEY + strategies.length,filterPane.isCollapsed());
            }
		});
		
		// unused - alwasy show this toggle.
	//	advanced.addPropertyChangeListener(this);
	//	advanced.initializeThroughListener(this);
	}
	
	private final EventList<Resource> totallyFilteredItems;
	private final JCheckBoxMenuItem systemToggle;	
	private final JTextField textField;
	private final JCollapsiblePane filterPane;
	private final JToggleButton toggleButton;
    private final FilterList<Resource> systemFilteredItems;
    private static final Preferences PREFERENCES = Preferences.userNodeForPackage(FilterPipelineFactory.class);
	
	/** toggle button to control filtering of system resources */
	public JCheckBoxMenuItem getSystemToggleButton() {
		return systemToggle;
	}
	
	
	/**
 	a text field for incremental searching.
	 */
	public JTextField  getTextField() {
		return textField;
	}

	/**
	 a toggle button, that controls display of the panel produced from <tt>getFilters()</tt>
	 */
	public JToggleButton getExpandButton() {
		return toggleButton;
	}

	/**
	 * a panel of filters
	 */
	public JCollapsiblePane getFilters() {
		return filterPane;
	}
	/** the list of filtered items */
	public EventList<Resource> getFilteredItems() {
		return totallyFilteredItems;
	}


	/** a declarative definition of an item that can be placed in the filter pipeline. */
	public static abstract class PipelineStrategy  {
		/** the name of this pipeline item */
		public abstract String getName();
		/** construct a view over a supplied base list.
		 * 
		 *  this should pererably be implemented as a single transformed list
		 *  If more than one transformation is required, the dispose() method
		 *  must cascade to call dispose() on all created lists.
		 *  */
		public abstract TransformedList<Resource,String> createView(EventList<Resource> base); 
		/** create a matcher for the base list, from these items */
		public abstract Matcher<Resource> createMatcher(List<String> selected);

		@Override
        public final String toString() {
			return getName();
		}
		
		/** static value used to represent 'no information provided' */
		public static List<String> NONE_PROVIDED = Collections.singletonList("<html><FONT color='#555555'>&nbsp;unknown");

	}

	/** A single stage in the pipeline.
	 * This class takes care of the display of the stage controls, 
	 * and also the plumbing of the event lists through this stage of the pipe */
	private static class PipelineItem extends JPanel implements ItemListener, PropertyChangeListener {
		private final ItemSelect itemsMatcherEditor;

		/** preference key foreach of  the preferred strategy */
		private final String PREFERRED_STRATEGY_KEY;

        private final JComboBox itemChooser;
		
		public PipelineItem(final int ix,final EventList<Resource> items, final PipelineStrategy[] strategies, final JCollapsiblePane parentPane) {
		    PREFERRED_STRATEGY_KEY = "preferred.strategy."
		        + strategies.length //make this key unique to different arrays of strategies
		        // or at least different by length - which is enough to differentiate between
		        // voexplorer, and the embedded version within astroscope.
		        + "."
		        + ix ; // unique key for each of the (3) pipeline items.
		    // see if the user has a preference of which strategy to use.
		    int preferredIx = PREFERENCES.getInt(PREFERRED_STRATEGY_KEY,ix);
		    if (preferredIx < 0 || preferredIx >=strategies.length) {
		        // found nonsense, fallback.
		        preferredIx = ix;
		    }
			final PipelineStrategy startingStrategy = strategies[preferredIx];
			itemsMatcherEditor = new ItemSelect(startingStrategy,items);

			itemChooser = new JComboBox(strategies);
			itemChooser.addItemListener(this);
			itemChooser.setSelectedItem(startingStrategy); // fires the event handler to initialize stuff.

			// listen to when parent pane collapses - reset selection when this happens.
			parentPane.addPropertyChangeListener("collapsed",this);
			
			// formatting code.
			setLayout(new BorderLayout());
		//	setBorder(BorderFactory.createEtchedBorder());			
			add(itemChooser,BorderLayout.NORTH);
			add(new JScrollPane(itemsMatcherEditor.getJList()),BorderLayout.CENTER);
			setPreferredSize(new Dimension(100,160));
		}
		/** called when a different strategy was selected - splice this into the pipeline. */
		public void itemStateChanged(final ItemEvent e) { 
			if (e.getStateChange() == ItemEvent.SELECTED) {
				final PipelineStrategy ps = (PipelineStrategy)e.getItem();
				if (itemsMatcherEditor.getCurrentStrategy() != ps) {
					itemsMatcherEditor.setStrategy(ps);
					PREFERENCES.putInt(PREFERRED_STRATEGY_KEY,itemChooser.getSelectedIndex());
				}
			}
		}
		/** returns a matcherEditor which allows items which match the current
		 * selection
		 * @return
		 */
		public MatcherEditor<Resource> getItemsMatcherEditor() {
			return itemsMatcherEditor;
		}
		/** called when filter pane collapses - clears any filters */
		public void propertyChange(final PropertyChangeEvent evt) {
			// reset on collapsed=true
			if (evt.getNewValue().equals(Boolean.TRUE)) {
				if (itemsMatcherEditor.getJList().getSelectedIndex() != -1) {
					itemsMatcherEditor.getJList().clearSelection();
				}
			}
		}	
	}

	/** A matcher that uses a pipelineStrategy to construct it's matcher, 
	 * and allows that pipelineStrategy to be altered. Manages sliding
	 * the strategy in and out of a jlist too.
	 * Changing the selection on the JList alters the matcher produced
	 * @author Noel.Winstanley@manchester.ac.uk
	 * @since Feb 15, 20071:53:54 PM
	 */
	public static class ItemSelect extends AbstractMatcherEditor<Resource> implements ListSelectionListener, Runnable {

		public ItemSelect(final PipelineStrategy mf, final EventList<Resource> items) {
			this.list = new JList();
			this.items = items;
			setStrategy(mf);
		}

		public PipelineStrategy getCurrentStrategy() {
			return strategy;
		}
		private final JList list;
		private PipelineStrategy strategy;
		private final EventList<Resource> items;
		private TransformedList<Resource,String> view;
		private EventList<String> selected;

		/** to save creating a new object every time, we implement runnable ourselves.
		 * so we can pass self to invokeLater()
		 */
			public void run() {
				fireMatchAll();
			}
				
		
		final public void setStrategy(final PipelineStrategy strategy) {
			this.strategy = strategy;
			// hang onto previous values.
			final TransformedList<Resource,String> oldView = view;
			EventListModel<String> oldModel = null;
			TogglingEventSelectionModel oldSelectionModel = null;
			list.removeListSelectionListener(this); //going to get a bit 'noisy' so stop listening.
			
			if (oldView != null) { // we've got a bit of history.
				oldModel = (EventListModel<String>)list.getModel();
				oldSelectionModel = (TogglingEventSelectionModel)list.getSelectionModel();
			}

			view = strategy.createView(items);
			final EventList<String> unq = new SortedList<String>(new UniqueList<String>(view));
			final EventListModel<String> listModel = new EventListModel<String>(unq);			
			final TogglingEventSelectionModel selectionModel = new TogglingEventSelectionModel(new EventSelectionModel<String>(unq));

			selectionModel.setSelectionMode(EventSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			this.selected = selectionModel.getSelected();
			
			list.setSelectionModel(selectionModel);
			list.setModel(listModel);
			// clean up.
			if (oldView != null) {
				oldView.dispose();
				oldModel.dispose(); // guarded by oldView
				oldSelectionModel.dispose(); // guarded by oldView
			}
			list.addListSelectionListener(this);
			SwingUtilities.invokeLater(this);
		}
		/** called when selection in list changes */
		public void valueChanged(final ListSelectionEvent e) {
			if (e.getValueIsAdjusting()) {
				return; // ignore these. another one will be along in a moment..
			}
			if (selected.size() == 0 || selected.size() == view.size()) {
				SwingUtilities.invokeLater(this);
			} else {
				final Matcher<Resource> m = strategy.createMatcher(selected);
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						fireChanged(m);
					}
				});				
			}
		}
		/** access the JList this ItemSelect manages */
		public JList getJList() {
			return list;
		}

	}


	
	/** Format for the {@link JEventListPanel} */
	private static class PipelineFormat extends JEventListPanel.AbstractFormat<PipelineItem> {
		public PipelineFormat() {
			super("0dlu, pref,4dlu","0dlu,fill:pref:grow,0dlu","4dlu","4dlu",new String[] {"2,2"}); 
		}

		@Override
        public int getComponentsPerElement() {
			return 1;
		}

		public JComponent getComponent(final PipelineItem element, final int component) {
			return element;
		}

	}
	
	
	/** An {@link EventSelectionModel}, that alters the behaviour 
	 * to allow selections to be more easily toggled.
	 * 
	 *@todo  it's not quite perfect - strange behaviour with 'shift', but will do for now.
	 * 
	 * Although I just want to override a single method of this class, I can't, as it's final.
	 * Instead, here I create a wrapper that delegates calls to a wraped EventSelectionModel. ho hum
	 * @author Noel.Winstanley@manchester.ac.uk
	 * @since Feb 23, 200711:13:42 PM
	 */
	public static class TogglingEventSelectionModel implements ListSelectionModel {
		
		private final EventSelectionModel<String> esm;

		public TogglingEventSelectionModel(final EventSelectionModel<String> esm) {
			super();
			this.esm = esm;
		}

	    boolean gestureStarted = false;
	    	
		
		public void addListSelectionListener(final ListSelectionListener arg0) {
			this.esm.addListSelectionListener(arg0);
		}

		public void addSelectionInterval(final int arg0, final int arg1) {
			this.esm.addSelectionInterval(arg0, arg1);
		}

		public void clearSelection() {
			this.esm.clearSelection();
		}


		@Override
        public boolean equals(final Object obj) {
			return this.esm.equals(obj);
		}

		public int getAnchorSelectionIndex() {
			return this.esm.getAnchorSelectionIndex();
		}

		public int getLeadSelectionIndex() {
			return this.esm.getLeadSelectionIndex();
		}


		public int getMaxSelectionIndex() {
			return this.esm.getMaxSelectionIndex();
		}

		public int getMinSelectionIndex() {
			return this.esm.getMinSelectionIndex();
		}

		public boolean getValueIsAdjusting() {
			return this.esm.getValueIsAdjusting();
		}

		@Override
        public int hashCode() {
			return this.esm.hashCode();
		}

		public void insertIndexInterval(final int arg0, final int arg1, final boolean arg2) {
			this.esm.insertIndexInterval(arg0, arg1, arg2);
		}
		public boolean isSelectedIndex(final int arg0) {
			return this.esm.isSelectedIndex(arg0);
		}

		public boolean isSelectionEmpty() {
			return this.esm.isSelectionEmpty();
		}

		public void removeIndexInterval(final int arg0, final int arg1) {
			this.esm.removeIndexInterval(arg0, arg1);
		}

		public void removeListSelectionListener(final ListSelectionListener arg0) {
			this.esm.removeListSelectionListener(arg0);
		}

		public void removeSelectionInterval(final int arg0, final int arg1) {
			this.esm.removeSelectionInterval(arg0, arg1);
		}

		public void setAnchorSelectionIndex(final int arg0) {
			this.esm.setAnchorSelectionIndex(arg0);
		}

		public void setLeadSelectionIndex(final int arg0) {
			this.esm.setLeadSelectionIndex(arg0);
		}


	    public void setSelectionInterval(final int index0, final int index1) {
		if (isSelectedIndex(index0) && !gestureStarted) {
		    this.esm.removeSelectionInterval(index0, index1);
		}
		else {
		    this.esm.setSelectionInterval(index0, index1);
		}
		gestureStarted = true;
	    }		
		
		public void setSelectionMode(final int arg0) {
			this.esm.setSelectionMode(arg0);
		}

	    public void setValueIsAdjusting(final boolean isAdjusting) {
		if (isAdjusting == false) {
		    gestureStarted = false;
		}
		this.esm.setValueIsAdjusting(isAdjusting);
		
	    }			

		@Override
        public String toString() {
			return this.esm.toString();
		}

		public int getSelectionMode() {
			return this.esm.getSelectionMode();
		}

		public EventList<String> getSelected() {
			return this.esm.getSelected();
		}

		public void dispose() {
			this.esm.dispose();
		}
	}
	
	/** matcher editor which can be turned on/off by a toggle button - and records this fact in a preference object */
	private static class NotToggleMatcherEditor extends AbstractMatcherEditor<Resource> implements ItemListener{
		private final Preferences preferences;
        private final String key;

        /**
		 * Construct a matcher editor which applies the supplied matcher only when the toggle is 'false'
		 * @param control
		 * @param preferences 
		 * @param matcher
		 */
		public NotToggleMatcherEditor(final JCheckBoxMenuItem control, final Preferences preferences, final String key, final Matcher<Resource> matcher) {
		    
			this.preferences = preferences;
            this.key = key;
            // load inital state of control from preferences.
            control.setSelected(preferences.getBoolean(key,control.isSelected()));
            control.addItemListener(this);
			realMatcher = matcher;
			if (! control.isSelected()) {
				fireConstrained(realMatcher);
			} else {
				fireMatchAll();
			}
		}

		private final Matcher<Resource> realMatcher;

		public void itemStateChanged(final ItemEvent e) {
		    final boolean selected = e.getStateChange() == ItemEvent.SELECTED;
		    preferences.putBoolean(key,selected);
			if (selected) {
			    fireMatchAll();
			} else {
			    fireConstrained(realMatcher);
			}
		}
		
	}
	// triggered when advanced property is flipped.
//    public void propertyChange(PropertyChangeEvent evt) {
//        if (evt.getSource() == this.advanced) {
//            if (advanced.asBoolean()) {
//                systemToggle.setVisible(true);
//            } else {
//                systemToggle.setVisible(false);
//                systemToggle.setSelected(false);
//            }
//        }
//    }


    /** Used for counting size when in 'basic' mode.
     * @return the systemFilteredItems
     */
    public final FilterList<Resource> getSystemFilteredItems() {
        return this.systemFilteredItems;
    }


}
