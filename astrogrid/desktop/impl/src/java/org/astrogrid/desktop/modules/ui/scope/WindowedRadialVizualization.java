package org.astrogrid.desktop.modules.ui.scope;



import java.util.Iterator;

import javax.swing.JPopupMenu;

import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.ui.UIComponent;

import edu.berkeley.guir.prefuse.Display;
import edu.berkeley.guir.prefuse.FocusManager;
import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.action.ActionSwitch;
import edu.berkeley.guir.prefuse.action.RepaintAction;
import edu.berkeley.guir.prefuse.action.animate.ColorAnimator;
import edu.berkeley.guir.prefuse.action.animate.PolarLocationAnimator;
import edu.berkeley.guir.prefuse.action.filter.Filter;
import edu.berkeley.guir.prefuse.action.filter.WindowedTreeFilter;
import edu.berkeley.guir.prefuse.activity.ActionList;
import edu.berkeley.guir.prefuse.activity.SlowInSlowOutPacer;
import edu.berkeley.guir.prefuse.event.FocusEvent;
import edu.berkeley.guir.prefuse.event.FocusListener;
import edu.berkeley.guir.prefuse.focus.DefaultFocusSet;
import edu.berkeley.guir.prefuse.graph.Entity;
import edu.berkeley.guir.prefuse.graph.Graph;
import edu.berkeley.guir.prefuse.graph.Node;
import edu.berkeley.guir.prefuse.graph.TreeNode;
import edu.berkeley.guir.prefusex.controls.DragControl;
import edu.berkeley.guir.prefusex.controls.FocusControl;
import edu.berkeley.guir.prefusex.controls.NeighborHighlightControl;
import edu.berkeley.guir.prefusex.controls.PanControl;
import edu.berkeley.guir.prefusex.controls.ToolTipControl;
import edu.berkeley.guir.prefusex.controls.ZoomControl;
import edu.berkeley.guir.prefusex.force.DragForce;
import edu.berkeley.guir.prefusex.force.ForceSimulator;
import edu.berkeley.guir.prefusex.force.NBodyForce;
import edu.berkeley.guir.prefusex.layout.ForceDirectedLayout;
import edu.berkeley.guir.prefusex.layout.RadialTreeLayout;

/**
 * Prefuse visualisation using radial Layout and a little bit of animation. Uses
 *windowing filter - displays nearer neighbours, not entire graph.
 *based on node type, adjusts the 'window' size - so we don't get an explosion of nodes on the display
 * based on edu.berkeley.guir.prefuse.demos.RadialGraphDemo
 */

public class WindowedRadialVizualization extends Vizualization {
/** Construct a new Radial
 * @param iconFinder 
     */
    public WindowedRadialVizualization(final VizualizationController vizs, final JPopupMenu menu, final UIComponent parent) {
        super("Radial", vizs);
        this.parent = parent;
        this.popup = menu;        
    }
    protected Display display;
    protected ActionList graphLayout;
    private final UIComponent parent;
    private final JPopupMenu popup;    
    
    
    
    /** returns true if this node is == to the root of one of the result branches
     */
    public boolean isPrimaryNode(final TreeNode t) {
        for (final Iterator i = vizs.getVizModel().getProtocols().iterator(); i.hasNext(); ) {
            final DalProtocol p = (DalProtocol)i.next();
            if (t == p.getPrimaryNode()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public Display getDisplay() {
            if (display == null) {
        final ItemRegistry registry = getItemRegistry();
         display = new Display(registry);
        CSH.setHelpIDString(display,"scope.viz.radial");
            graphLayout = new ActionList(registry);
            
            // two different types of filter - only vary in window size.
            final Filter[] filters = new Filter[] {
                    new WindowedTreeFilter(-2,true)
                    ,new WindowedTreeFilter(-1,true)
                    ,new WindowedTreeFilter(-4,true)
            };            
                    
            // switch between the two filters
            final ActionSwitch filterSwitch = new ActionSwitch(filters,0);
            
            // add a listener, that selects the correct filter based on the current node.
            registry.getDefaultFocusSet().addFocusListener(new FocusListener() {
                public void focusChanged(final FocusEvent event) {
                    if (event.getEventType() == FocusEvent.FOCUS_SET || event.getEventType() == FocusEvent.FOCUS_ADDED) {
                        final Entity e = event.getFirstAdded();
                        if (e instanceof TreeNode) {
                            final TreeNode t = (TreeNode)e;
                            if (isPrimaryNode(t)) {
                                filterSwitch.setSwitchValue(1);
                                return;
                            } else if(t.getChildCount() == 0) {
                                    filterSwitch.setSwitchValue(2);
                                    return;
                            }
                        }
                        // all other cases.
                       filterSwitch.setSwitchValue(0);
                    }
                }
            });
            
            graphLayout.add(filterSwitch);
            graphLayout.add(new RadialTreeLayout());
            graphLayout.add(new WindowedRadialColorFunction(3));
        
           final ActionList update = new ActionList(registry);
           update.add(new WindowedRadialColorFunction(3));
           update.add(new RepaintAction());

           
           final ActionList animate = new ActionList(registry, 1500, 20);
           animate.setPacingFunction(new SlowInSlowOutPacer());
           animate.add(new PolarLocationAnimator());
           animate.add(new ColorAnimator());
           animate.add(new RepaintAction());
           animate.alwaysRunAfter(graphLayout);
           
           // add jitter to layout nodes better. could maybe make the jitter larger - makes the nodes less
           // likely to overlap.
           
           final ForceSimulator fsim = new ForceSimulator();
           fsim.addForce(new NBodyForce(-0.1f,15f,0.5f));
           fsim.addForce(new DragForce());
           
           final ActionList forces = new ActionList(registry,1000);
           forces.add(new ForceDirectedLayout(fsim,true));
           forces.add(new RepaintAction());
           forces.alwaysRunAfter(animate);
           
           
           display.setItemRegistry(registry);
           display.setSize(400,400);
           
           //for radial tree
           display.addControlListener(new FocusControl(graphLayout));
           display.addControlListener(new FocusControl(0,FocusManager.HOVER_KEY));
           display.addControlListener(new DragControl(false,true));
           display.addControlListener(new PanControl(false));
           display.addControlListener(new ZoomControl(false));
           display.addControlListener(new ToolTipControl(AbstractRetriever.TOOLTIP_ATTRIBUTE));   
           display.addControlListener(new NeighborHighlightControl(update));
           display.addControlListener(new DoubleClickMultiSelectFocusControl(vizs));
  
//@todo renable           display.addControlListener(new SendToMenuControl(popup,parent));           
           
           registry.getFocusManager().putFocusSet(
                   FocusManager.HOVER_KEY, new DefaultFocusSet());
            }
           return display;
    }

    // refresh display when new item added
    @Override
    public void nodeAdded(final Graph arg0, final Node arg1) {
        graphLayout.runNow();
    }
    
    // refresh display when new item added
    @Override
    public void reDraw() {
        graphLayout.runNow();
    }
}