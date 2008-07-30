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

/** Factory that builds a pipeline of filters, and the UI components to support them.
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 14, 20074:14:06 PM
 */
public class FilterPipelineFactory   {


	private final Preference advanced;
	private final static String SYSTEM_TOGGLE_KEY = "system.toggle.key";
    public FilterPipelineFactory(SortedList items, PipelineStrategy[] strategies, AnnotationService annotationService, Preference advanced) {
		this.advanced = advanced;
        int pipelineSize = 3;
		
		// system filters..
		systemToggle = new JCheckBoxMenuItem("Show Technical System Resources",false); 
		systemToggle.setToolTipText("Show technical system resources");	
		systemFilteredItems = new FilterList(items
				,new NotToggleMatcherEditor(systemToggle,PREFERENCES,SYSTEM_TOGGLE_KEY,new SystemFilter()));
        // incremental text field..
		SearchField sf = new SearchField("Filter results");
        sf.setToolTipText("Filter list of resources using text string");
		FilterList filteredItems = new FilterList(systemFilteredItems
				, new TextComponentMatcherEditor(sf.getWrappedDocument(), new ResourceTextFilterator(annotationService)));
		textField = sf;
		
		// collapsible filters.
		filterPane = new JCollapsiblePane();
		filterPane.setCollapsed(true);
		// create the pipeline, plumbing together the various items.
		final EventList pipeline = new BasicEventList();	
		for (int i = 0; i < pipelineSize; i++) {
			PipelineItem p = new PipelineItem(i,filteredItems,strategies,filterPane);
			pipeline.add(p);
			filteredItems = new FilterList(filteredItems,p.getItemsMatcherEditor());
		}
		totallyFilteredItems = filteredItems;

		JEventListPanel pipelineDisplay = new JEventListPanel(pipeline, new PipelineFormat());
		pipelineDisplay.setElementColumns(3);
		filterPane.add(pipelineDisplay);
		
		// create a toggle button to show / hide the collapsed pane.
		toggleButton = new ExpandCollapseButton(filterPane);
		final Action toggleAction = toggleButton.getAction();
		toggleAction.putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_B,UIComponentMenuBar.MENU_KEYMASK));
		toggleAction.putValue(Action.NAME,"Show Metadata Browser");
		toggleAction.putValue(Action.SHORT_DESCRIPTION,"Filter resource list using metadata");
		filterPane.addPropertyChangeListener("collapsed",new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if (filterPane.isCollapsed()) {
                    toggleAction.putValue(Action.NAME,"Show Metadata Browser");
		            toggleAction.putValue(Action.SHORT_DESCRIPTION,"Filter resource list using metadata");
                } else {
                    toggleAction.putValue(Action.NAME,"Hide Metadata Browser");
                    toggleAction.putValue(Action.SHORT_DESCRIPTION,"View unfiltered resource list");
                }
            }
		});
		
		// unused - alwasy show this toggle.
	//	advanced.addPropertyChangeListener(this);
	//	advanced.initializeThroughListener(this);
	}
	
	private final EventList totallyFilteredItems;
	private JCheckBoxMenuItem systemToggle;	
	private final JTextField textField;
	private final JCollapsiblePane filterPane;
	private final JToggleButton toggleButton;
    private final FilterList systemFilteredItems;
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
	public EventList getFilteredItems() {
		return totallyFilteredItems;
	}


	/** a declarative definition of an item that can be placed in a pipeline. */
	public static abstract class PipelineStrategy  {
		/** the name of this pipeline item */
		public abstract String getName();
		/** construct a view over a supplied base list.
		 * 
		 *  this should pererably be implemented as a single transformed list
		 *  If more than one transformation is required, the dispose() method
		 *  must cascade to call dispose() on all created lists.
		 *  */
		public abstract TransformedList createView(EventList base); 
		/** create a matcher for the base list, from these items */
		public abstract Matcher createMatcher(List selected);

		public final String toString() {
			return getName();
		}
		
		/** static value used to represent 'no information provided' */
		public static List NONE_PROVIDED = Collections.singletonList("<html><FONT color='#555555'>&nbsp;unknown");

	}

	/** A single stage in the pipeline.
	 * This class takes care of the display of the stage controls, 
	 * and also the plumbing of the event lists through this stage of the pipe */
	private static class PipelineItem extends JPanel implements ItemListener, PropertyChangeListener {
		private final ItemSelect itemsMatcherEditor;

		public PipelineItem(int ix,final EventList items, PipelineStrategy[] strategies, JCollapsiblePane parentPane) {
			// use position ix in the strat list  as our starting strategy.
			//@todo later load  in user's selection from preferences
			PipelineStrategy startingStrategy = strategies[ix];
			itemsMatcherEditor = new ItemSelect(startingStrategy,items);

			final JComboBox itemChooser = new JComboBox(strategies);
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
		public void itemStateChanged(ItemEvent e) { 
			if (e.getStateChange() == ItemEvent.SELECTED) {
				PipelineStrategy ps = (PipelineStrategy)e.getItem();
				if (itemsMatcherEditor.getCurrentStrategy() != ps) {
					itemsMatcherEditor.setStrategy(ps);
				}
			}
		}
		/** returns a matcherEditor which allows items which match the current
		 * selection
		 * @return
		 */
		public MatcherEditor getItemsMatcherEditor() {
			return itemsMatcherEditor;
		}
		/** called when filter pane collapses - clears any filters */
		public void propertyChange(PropertyChangeEvent evt) {
			// reset on collapsed=true
			if (evt.getNewValue().equals(Boolean.TRUE)) {
				if (itemsMatcherEditor.getJList().getSelectedIndex() != -1) {
					itemsMatcherEditor.getJList().clearSelection();
				}
			}
		}	
	}

	/** custom matcher that uses a pipelineStrategy to construct it's matcher, 
	 * and allows that pipelineStrategy to be altered. Manages sliding
	 * the strategy in and out of a jlist too.
	 * Changing the selection on the JList alters the matcher produced
	 * @author Noel.Winstanley@manchester.ac.uk
	 * @since Feb 15, 20071:53:54 PM
	 */
	public static class ItemSelect extends AbstractMatcherEditor implements ListSelectionListener, Runnable {

		public ItemSelect(PipelineStrategy mf, EventList items) {
			this.list = new JList();
			this.items = items;
			setStrategy(mf);
		}

		public PipelineStrategy getCurrentStrategy() {
			return strategy;
		}
		private final JList list;
		private PipelineStrategy strategy;
		private final EventList items;
		private TransformedList view;
		private EventList selected;

		/** to save creating a new object every time, we implement runnable ourselves.
		 * so we can pass self to invokeLater()
		 */
			public void run() {
				fireMatchAll();
			}
				
		
		final public void setStrategy(PipelineStrategy strategy) {
			this.strategy = strategy;
			// hang onto previous values.
			TransformedList oldView = view;
			EventListModel oldModel = null;
			TogglingEventSelectionModel oldSelectionModel = null;
			list.removeListSelectionListener(this); //going to get a bit 'noisy' so stop listening.
			
			if (oldView != null) { // we've got a bit of history.
				oldModel = (EventListModel)list.getModel();
				oldSelectionModel = (TogglingEventSelectionModel)list.getSelectionModel();
			}

			view = strategy.createView(items);
			EventList unq = new SortedList(new UniqueList(view));
			EventListModel listModel = new EventListModel(unq);			
			TogglingEventSelectionModel selectionModel = new TogglingEventSelectionModel(new EventSelectionModel(unq));

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
		public void valueChanged(ListSelectionEvent e) {
			if (e.getValueIsAdjusting()) {
				return; // ignore these. another one will be along in a moment..
			}
			if (selected.size() == 0 || selected.size() == view.size()) {
				SwingUtilities.invokeLater(this);
			} else {
				final Matcher m = strategy.createMatcher(selected);
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


	
	/** configures the JEventListPanel */
	private static class PipelineFormat extends JEventListPanel.AbstractFormat {
		public PipelineFormat() {
			super("0dlu, pref,4dlu","0dlu,fill:pref:grow,0dlu","4dlu","4dlu",new String[] {"2,2"}); 
		}

		public int getComponentsPerElement() {
			return 1;
		}

		public JComponent getComponent(final Object element, int component) {
			return (JComponent)element;
		}

	}
	
	
	/** customized version of glazed lists's EventSelectionModel, that changes behaviour 
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
		
		private final EventSelectionModel esm;

		public TogglingEventSelectionModel(final EventSelectionModel esm) {
			super();
			this.esm = esm;
		}

	    boolean gestureStarted = false;
	    	
		
		public void addListSelectionListener(ListSelectionListener arg0) {
			this.esm.addListSelectionListener(arg0);
		}

		public void addSelectionInterval(int arg0, int arg1) {
			this.esm.addSelectionInterval(arg0, arg1);
		}

		public void clearSelection() {
			this.esm.clearSelection();
		}


		public boolean equals(Object obj) {
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

		public int hashCode() {
			return this.esm.hashCode();
		}

		public void insertIndexInterval(int arg0, int arg1, boolean arg2) {
			this.esm.insertIndexInterval(arg0, arg1, arg2);
		}
		public boolean isSelectedIndex(int arg0) {
			return this.esm.isSelectedIndex(arg0);
		}

		public boolean isSelectionEmpty() {
			return this.esm.isSelectionEmpty();
		}

		public void removeIndexInterval(int arg0, int arg1) {
			this.esm.removeIndexInterval(arg0, arg1);
		}

		public void removeListSelectionListener(ListSelectionListener arg0) {
			this.esm.removeListSelectionListener(arg0);
		}

		public void removeSelectionInterval(int arg0, int arg1) {
			this.esm.removeSelectionInterval(arg0, arg1);
		}

		public void setAnchorSelectionIndex(int arg0) {
			this.esm.setAnchorSelectionIndex(arg0);
		}

		public void setLeadSelectionIndex(int arg0) {
			this.esm.setLeadSelectionIndex(arg0);
		}


	    public void setSelectionInterval(int index0, int index1) {
		if (isSelectedIndex(index0) && !gestureStarted) {
		    this.esm.removeSelectionInterval(index0, index1);
		}
		else {
		    this.esm.setSelectionInterval(index0, index1);
		}
		gestureStarted = true;
	    }		
		
		public void setSelectionMode(int arg0) {
			this.esm.setSelectionMode(arg0);
		}

	    public void setValueIsAdjusting(boolean isAdjusting) {
		if (isAdjusting == false) {
		    gestureStarted = false;
		}
		this.esm.setValueIsAdjusting(isAdjusting);
		
	    }			

		public String toString() {
			return this.esm.toString();
		}

		public int getSelectionMode() {
			return this.esm.getSelectionMode();
		}

		public EventList getSelected() {
			return this.esm.getSelected();
		}

		public void dispose() {
			this.esm.dispose();
		}
	}
	
	/** matcher editor which can be turned on/off by a toggle button - and records this fact in a preference object */
	private static class NotToggleMatcherEditor extends AbstractMatcherEditor implements ItemListener{
		private final Preferences preferences;
        private final String key;

        /**
		 * Construct a matcher editor which applies the supplied matcher only when the toggle is 'false'
		 * @param control
		 * @param preferences 
		 * @param matcher
		 */
		public NotToggleMatcherEditor(JCheckBoxMenuItem control, final Preferences preferences, final String key, Matcher matcher) {
		    
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

		private final Matcher realMatcher;

		public void itemStateChanged(ItemEvent e) {
		    boolean selected = e.getStateChange() == ItemEvent.SELECTED;
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
    public final FilterList getSystemFilteredItems() {
        return this.systemFilteredItems;
    }


}
