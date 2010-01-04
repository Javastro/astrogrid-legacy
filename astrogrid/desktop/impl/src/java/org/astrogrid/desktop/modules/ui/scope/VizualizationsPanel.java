/**
 * 
 */
package org.astrogrid.desktop.modules.ui.scope;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.prefs.Preferences;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.modules.ui.UIComponentMenuBar;
import org.astrogrid.desktop.modules.ui.comp.FlipPanel;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FunctionList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;
import edu.berkeley.guir.prefuse.event.FocusEvent;
import edu.berkeley.guir.prefuse.event.FocusListener;
import edu.berkeley.guir.prefuse.focus.FocusSet;
import edu.berkeley.guir.prefuse.graph.Entity;
import edu.berkeley.guir.prefuse.graph.TreeNode;

/** Custom {@code FilePanel} for managing the different vizualizations.
 * 
 * also acts as a bridge, to keep the selection set synchrobnized between the 
 * prefuse and glazed lists worlds. 
 * 
 * There's code in here to do partial synching of selection.
 * Also the start of code to do filtering of views - hiding this for the next release thoug
 * @todo complete selection synch and filtering synch. needs an architectural rethink, and a clearer description of what I'm trying to do.
 * 
 * at the moment this implementation listens to selection changes constantly - however, we could 
 * just do a synch-of-selection when the view flips? Depends how we're doing activity selections
 * 
 * @fixme once I've implemented the resource view, should I extend the 
 * selection copying down to individual files?
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Nov 12, 200712:24:42 PM
 */
public class VizualizationsPanel extends FlipPanel implements FocusListener, ListEventListener, FunctionList.AdvancedFunction {

    private final VizualizationController viz;
    private final EventList tableSelected;
    private final FocusSet vizSelected;  
    private boolean servicesHasPrecedence = false;
    private final QueryResults queryResults;
    private final EventList displayedResources;
    private final Vizualization radialViz;
    private final Vizualization hyperbolicViz;
    private final ScopeServicesList table;
    
    private static final Preferences PREFERENCES = Preferences.userNodeForPackage(VizualizationsPanel.class);
    private static final String PREFERRED_VIEW_KEY = "preferred.view";
 
    /**
     * @param viz the controller
     * @param radialViz radial vizualization
     * @param hyperbolicViz hyperbolic vizualization
     * @param table services table
     */
    public VizualizationsPanel(final VizualizationController viz,final Vizualization radialViz, final Vizualization hyperbolicViz, final ScopeServicesList table) {
        this.viz = viz;
        this.radialViz = radialViz;
        this.hyperbolicViz = hyperbolicViz;
        this.table = table;
        this.vizSelected = viz.getVizModel().getSelectionFocusSet();   
        this.queryResults = table.getQueryResults();
        this.tableSelected = table.getCurrentResourceModel().getTogglingSelected();
      //not synching view selection at the moment... 
        //vizSelected.addFocusListener(this);
        //tableSelected.addListEventListener(this); // listen to this for selection changes
        this.displayedResources = table.getCurrentDisplayedResources();
        // create a funciton list, just for the side effect.
        new FunctionList(displayedResources, this);
        add(RADIAL_VIEW,radialViz.getDisplay());
        add(HYPERBOLIC_VIEW,hyperbolicViz.getDisplay());
        add(SERVICES_VIEW,table);
        getShowServicesFiltersAction().setEnabled(false);
        
        // load preference.
        final String view = PREFERENCES.get(PREFERRED_VIEW_KEY,SERVICES_VIEW);
        if (HYPERBOLIC_VIEW.equals(view)) {
            flipToHyperbolicView();
        } else if (SERVICES_VIEW.equals(view)) {
            flipToServicesTable();
        } else if (RADIAL_VIEW.equals(view)) {
            flipToRadialView();
        }
    }

//prefuse listener interface
 public void focusChanged(final FocusEvent arg0) {
         if (servicesHasPrecedence) {//services tab is driving.
             return; // ignore in this case.
         }
         switch(arg0.getEventType()) {
         case FocusEvent.FOCUS_ADDED:
             final Entity[] added = arg0.getAddedFoci();
             for (int i = 0; i < added.length; i++) {
                 if (added[i].getAttribute(AbstractRetriever.SERVICE_ID_ATTRIBUTE) != null) {
                     // found a service node - add the equivalent resource, and add to glazed side.
                     final Retriever r = queryResults.findRetriever((TreeNode)added[i]);
                     if (r != null) {
                         final Service s = RetrieverService.create(r);
                         if (! tableSelected.contains(s)) {
                             tableSelected.add(s); 
                         }
                     }
                 }
             }
             break;
         case FocusEvent.FOCUS_REMOVED:
             final Entity[] removed = arg0.getRemovedFoci();
             for (int i = 0; i < removed.length; i++) {
                 if (removed[i].getAttribute(AbstractRetriever.SERVICE_ID_ATTRIBUTE) != null) {
                     // found a service node - add the equivalent resource, and add to glazed side.
                     final Retriever r = queryResults.findRetriever((TreeNode)removed[i]);
                     if (r != null) {
                         final Service s = RetrieverService.create(r);
                         if (tableSelected.contains(s)) {
                             tableSelected.remove(s); 
                         }
                     }
                 }
             }               
             break;
         case FocusEvent.FOCUS_SET:
             tableSelected.clear();
             for (final Iterator i = arg0.getFocusSet().iterator(); i.hasNext();) {
                 final TreeNode t = (TreeNode)i.next();
                 if (t.getAttribute(AbstractRetriever.SERVICE_ID_ATTRIBUTE) != null) {
                     // found a service node - add the equivalent resource, and add to glazed side.
                     final Retriever r = queryResults.findRetriever(t);
                     if (r != null) {
                         final Service s = RetrieverService.create(r);
                         if (! tableSelected.contains(s)) {
                             tableSelected.add(s); 
                         }
                     }
                 }
             }               
         }
 }

// table listener interface.
 public void listChanged(final ListEvent ev) {
     if (ev.getSourceList() == tableSelected) { // selection changed event
         if (! servicesHasPrecedence) {
             return;
         }     
         clearPrefuseSelection();    
         for(int i = 0; i < tableSelected.size(); i++) {
             final RetrieverService s = (RetrieverService)tableSelected.get(i);
             final TreeNode t = queryResults.findTreeNode(s.getRetriever());
             if (t != null) {
                 DoubleClickMultiSelectFocusControl.selectSubtree(t,vizSelected);
             }
         }     
        viz.reDrawGraphs();     
     }
 }
 
/** overridden to record current view in preferences */ 
 @Override
public void setShowing(final String viewName) {
    super.setShowing(viewName);
    PREFERENCES.put(PREFERRED_VIEW_KEY,viewName);
}
/**
 * 
 */
private void clearPrefuseSelection() {
    // just synch the two views - too fiddly to compute the differences.
     // especially as prefuse might have non-service subtrees selected anyhow.
     for (final Iterator i = vizSelected.iterator(); i.hasNext(); ) {
         final TreeNode n = (TreeNode)i.next();
         n.setAttribute("selected","false");
     }
     vizSelected.clear();
}
 
//actions for use in menus. 
 protected class RadialAction extends AbstractAction {

     public RadialAction() {
         super("as Radial Graph");
         putValue(ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_1,UIComponentMenuBar.MENU_KEYMASK));
         putValue(SHORT_DESCRIPTION,"view the results as a radial graph");            
     }
     public void actionPerformed(final ActionEvent e) {
         if (servicesHasPrecedence) { // we've flipped from services view - clear selection
             clearPrefuseSelection();
             viz.reDrawGraphs();           
             getShowServicesFiltersAction().setEnabled(false);
         }
         setShowing(RADIAL_VIEW);
         servicesHasPrecedence = false;
     }
 }
 protected class HyperbolicAction extends AbstractAction {
     public HyperbolicAction() {
         super("as Hyperbolic Graph");
         putValue(ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_2,UIComponentMenuBar.MENU_KEYMASK));
         putValue(SHORT_DESCRIPTION,"view the results as a hyperbolic graph (shows all nodes, compressed towards the edges)");            
     }
     public void actionPerformed(final ActionEvent e) {
         if (servicesHasPrecedence) { // we've flipped from services view - clear selection
             clearPrefuseSelection();      
             viz.reDrawGraphs();               
             getShowServicesFiltersAction().setEnabled(false);             
         }         
         setShowing(HYPERBOLIC_VIEW);
         servicesHasPrecedence = false;
     }
 }
 protected class ServicesAction extends AbstractAction {
     public ServicesAction() {
         super("as Services Table");
         putValue(ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_3,UIComponentMenuBar.MENU_KEYMASK));
         putValue(SHORT_DESCRIPTION,"view the results in a table of queried tables");
     }
     public void actionPerformed(final ActionEvent e) {
         // select all files contents.         
         tableSelected.clear();
         if (table.getCurrentDisplayedResources().size() > 0) {
             tableSelected.add(table.getCurrentDisplayedResources().get(0));
         }
         getShowServicesFiltersAction().setEnabled(true);         
         setShowing(SERVICES_VIEW);
         servicesHasPrecedence = true;        
     }
 }

protected final Action servicesAction = new ServicesAction() ;
protected final Action radialAction = new RadialAction() ;
protected final Action hyperbolicAction = new HyperbolicAction();

public static final String RADIAL_VIEW = "radial";
public static final String HYPERBOLIC_VIEW = "hyperbolic";
public static final String SERVICES_VIEW = "services";
public final Action getServicesAction() {
    return this.servicesAction;
}
public final Action getRadialAction() {
    return this.radialAction;
}
public final Action getHyperbolicAction() {
    return this.hyperbolicAction;
}
/** attribute set when this branch is to be filtered out of the results view */
public static final String SERVICE_FILTERED_ATTR = "service-filtered";
/** a resource is removed from the list */
public void dispose(final Object sourceValue, final Object transformedValue) {
    final RetrieverService res = (RetrieverService)sourceValue;
    final TreeNode node = queryResults.findTreeNode(res.getRetriever());
    if (node != null) {
        node.setAttribute(SERVICE_FILTERED_ATTR,"true");
    }
}

// shouldn't really ever happen.
public Object reevaluate(final Object sourceValue, final Object transformedValue) {
    return null;
}

/** called when an item appears in the list */
public Object evaluate(final Object sourceValue) {
    final RetrieverService res = (RetrieverService)sourceValue;
    final TreeNode node = queryResults.findTreeNode(res.getRetriever());
    if (node != null) {        
        node.setAttribute(SERVICE_FILTERED_ATTR,"false");
    }
    return sourceValue;
}

/** access the action used to expand / contract the services tab.
 * based on the show browser action provided by the reg google pane,
 * but is enabled / disabled according to which view is shown.
 * @return
 */
public Action getShowServicesFiltersAction() {
    return table.getExpandAction();
}

/** true if currently showing the radial view */
    public boolean currentlyRadial() {
        return VizualizationsPanel.RADIAL_VIEW.equals(getShowing());
    }
    
    /** true if currently showing the hyperbolic view */
    public boolean currentlyHyperbolic() {
        return VizualizationsPanel.HYPERBOLIC_VIEW.equals(getShowing());
    }
    
    /** true is currenlty showing the services table */
    public boolean currentlyServicesTable() {
        return VizualizationsPanel.SERVICES_VIEW.equals(getShowing());
    }

    /** show the services table */
    private void flipToServicesTable() {
        getServicesAction().actionPerformed(null);               
    }
    
    /** show the radial view */
    private void flipToRadialView() {
        getRadialAction().actionPerformed(null);
    }

    /** show the hyperbolic view */
    private void flipToHyperbolicView() {
        getHyperbolicAction().actionPerformed(null);
    }  
}
