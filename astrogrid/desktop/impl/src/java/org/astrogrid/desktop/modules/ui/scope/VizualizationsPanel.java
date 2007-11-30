/**
 * 
 */
package org.astrogrid.desktop.modules.ui.scope;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.modules.ui.UIComponentMenuBar;
import org.astrogrid.desktop.modules.ui.comp.FlipPanel;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;

import edu.berkeley.guir.prefuse.event.FocusEvent;
import edu.berkeley.guir.prefuse.event.FocusListener;
import edu.berkeley.guir.prefuse.focus.FocusSet;
import edu.berkeley.guir.prefuse.graph.Entity;
import edu.berkeley.guir.prefuse.graph.TreeNode;

/** custom flip panel for managing the different vizualizations.
 * 
 * also acts as a bridge, to keep the selection set synchrobnized between the 
 * prefuse and glazed lists worlds. 
 * 
 * at the moment this implementation listens to selection changes constantly - however, we could 
 * just do a synch-of-selection when the view flips? Depends how we're doing activity selections
 * 
 * @fixme once I've implemented the resource view, should I extend the 
 * selection copying down to individual files?
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Nov 12, 200712:24:42 PM
 */
public class VizualizationsPanel extends FlipPanel implements FocusListener, ListEventListener {

    private final VizualizationManager viz;
    private final EventList tableSelected;
    private final FocusSet vizSelected;
    private final ScopeServicesList table;    
    private boolean servicesHasPrecedence = false;
    
    public VizualizationsPanel(VizualizationManager viz,Vizualization radialViz, Vizualization hyperbolicViz, ScopeServicesList table) {
        this.viz = viz;
        this.vizSelected = viz.getVizModel().getSelectionFocusSet();    
        vizSelected.addFocusListener(this);
        this.table = table;
        this.tableSelected = table.getCurrentResourceModel().getTogglingSelected();
        tableSelected.addListEventListener(this);
        add(RADIAL_VIEW,radialViz.getDisplay());
        add(HYPERBOLIC_VIEW,hyperbolicViz.getDisplay());
        add(SERVICES_VIEW,table);
    }

//prefuse listener interface
 public void focusChanged(FocusEvent arg0) {
         if (servicesHasPrecedence) {//services tab is driving.
             return; // ignore in this case.
         }
         switch(arg0.getEventType()) {
         case FocusEvent.FOCUS_ADDED:
             Entity[] added = arg0.getAddedFoci();
             for (int i = 0; i < added.length; i++) {
                 if (added[i].getAttribute(Retriever.SERVICE_ID_ATTRIBUTE) != null) {
                     // found a service node - add the equivalent resource, and add to glazed side.
                     Service s = table.findService((TreeNode)added[i]);
                     if (s != null && ! tableSelected.contains(s)) {
                         tableSelected.add(s); 
                     }
                 }
             }
             break;
         case FocusEvent.FOCUS_REMOVED:
             Entity[] removed = arg0.getRemovedFoci();
             for (int i = 0; i < removed.length; i++) {
                 if (removed[i].getAttribute(Retriever.SERVICE_ID_ATTRIBUTE) != null) {
                     // found a service node - add the equivalent resource, and add to glazed side.
                     Service s = table.findService((TreeNode)removed[i]);
                     if (s != null && tableSelected.contains(s)) {
                         tableSelected.remove(s); 
                     }
                 }
             }               
             break;
         case FocusEvent.FOCUS_SET:
             tableSelected.clear();
             for (Iterator i = arg0.getFocusSet().iterator(); i.hasNext();) {
                 TreeNode t = (TreeNode)i.next();
                 if (t.getAttribute(Retriever.SERVICE_ID_ATTRIBUTE) != null) {
                     // found a service node - add the equivalent resource, and add to glazed side.
                     Service s = table.findService(t);
                     if (s != null && ! tableSelected.contains(s)) {
                         tableSelected.add(s); 
                     }
                 }
             }               
         }
 }

// table listener interface.
 public void listChanged(ListEvent listChanges) {
     if (! servicesHasPrecedence) {
         return;
     }     
     // just synch the two views - too fiddly to compute the differences.
     // especially as prefuse might have non-service subtrees selected anyhow.
     vizSelected.clear();    
     for(int i = 0; i < tableSelected.size(); i++) {
         Service s = (Service)tableSelected.get(i);
         TreeNode t = table.findTreeNode(s);
         if (t != null) {
             DoubleClickMultiSelectFocusControl.selectSubtree(t,vizSelected);
         }
     }     
     viz.reDrawGraphs();     
 }
 
//actions for use in menus. 
 protected class RadialAction extends AbstractAction {

     public RadialAction() {
         super("as Radial Graph");
         putValue(ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_1,UIComponentMenuBar.MENU_KEYMASK));
         putValue(SHORT_DESCRIPTION,"view the results as a spoked graph");            
     }
     public void actionPerformed(ActionEvent e) {
         show(RADIAL_VIEW);
         servicesHasPrecedence = false;
     }
 }
 protected class HyperbolicAction extends AbstractAction {
     public HyperbolicAction() {
         super("as Hyperbolic Graph");
         putValue(ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_2,UIComponentMenuBar.MENU_KEYMASK));
         putValue(SHORT_DESCRIPTION,"view the results as a radial graph (shows all nodes)");            
     }
     public void actionPerformed(ActionEvent e) {
         show(HYPERBOLIC_VIEW);
         servicesHasPrecedence = false;
     }
 }
 protected class ServicesAction extends AbstractAction {
     public ServicesAction() {
         super("as Services Table");
         putValue(ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_3,UIComponentMenuBar.MENU_KEYMASK));
         putValue(SHORT_DESCRIPTION,"view the results in a table of queried tables");
     }
     public void actionPerformed(ActionEvent e) {
         show(SERVICES_VIEW);
         servicesHasPrecedence = true;
     }
 }

protected final Action servicesAction = new ServicesAction() ;
protected final Action radialAction = new RadialAction() ;
protected final Action hyperbolicAction = new HyperbolicAction();

private static final String RADIAL_VIEW = "radial";
private static final String HYPERBOLIC_VIEW = "hyperbolic";
private static final String SERVICES_VIEW = "services";
public final Action getServicesAction() {
    return this.servicesAction;
}
public final Action getRadialAction() {
    return this.radialAction;
}
public final Action getHyperbolicAction() {
    return this.hyperbolicAction;
}



}