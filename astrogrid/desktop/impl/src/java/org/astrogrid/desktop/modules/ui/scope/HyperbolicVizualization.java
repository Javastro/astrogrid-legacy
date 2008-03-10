package org.astrogrid.desktop.modules.ui.scope;



import java.awt.Color;
import java.awt.Cursor;
import java.awt.Paint;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;

import javax.swing.JPopupMenu;

import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.fileexplorer.IconFinder;

import edu.berkeley.guir.prefuse.AggregateItem;
import edu.berkeley.guir.prefuse.Display;
import edu.berkeley.guir.prefuse.EdgeItem;
import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.NodeItem;
import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.action.RepaintAction;
import edu.berkeley.guir.prefuse.action.assignment.ColorFunction;
import edu.berkeley.guir.prefuse.action.filter.TreeFilter;
import edu.berkeley.guir.prefuse.activity.ActionList;
import edu.berkeley.guir.prefuse.activity.ActivityMap;
import edu.berkeley.guir.prefuse.activity.SlowInSlowOutPacer;
import edu.berkeley.guir.prefuse.event.ControlAdapter;
import edu.berkeley.guir.prefuse.graph.Graph;
import edu.berkeley.guir.prefuse.graph.Node;
import edu.berkeley.guir.prefuse.graph.TreeNode;
import edu.berkeley.guir.prefuse.hyperbolictree.HyperbolicTranslation;
import edu.berkeley.guir.prefuse.hyperbolictree.HyperbolicTranslationEnd;
import edu.berkeley.guir.prefuse.hyperbolictree.HyperbolicTreeLayout;
import edu.berkeley.guir.prefuse.hyperbolictree.HyperbolicTreeMapper;
import edu.berkeley.guir.prefuse.hyperbolictree.HyperbolicVisibilityFilter;
import edu.berkeley.guir.prefuse.render.DefaultEdgeRenderer;
import edu.berkeley.guir.prefuse.render.NullRenderer;
import edu.berkeley.guir.prefuse.render.Renderer;
import edu.berkeley.guir.prefuse.render.RendererFactory;
import edu.berkeley.guir.prefuse.render.ShapeRenderer;
import edu.berkeley.guir.prefuse.util.ColorLib;
import edu.berkeley.guir.prefusex.controls.ToolTipControl;
import edu.berkeley.guir.prefusex.controls.ZoomControl;

/** vizualization that hides some of the data some of the time 
 * based on edu.berkeley.guir.prefuse.action.filter.WindowedTreeFilter
 * 
 * needs to have it's nodes spaced out more.
 * 
 * */
public class HyperbolicVizualization extends Vizualization {
        
    public HyperbolicVizualization(VizualizationController vizs, JPopupMenu popup, UIComponent parent) {
            super("Hyperbolic", vizs);    
            this.parent = parent;
            this.menu = popup;
    }
    private Display display;
    private final UIComponent parent;
    private JPopupMenu menu;
    private ActivityMap actmap; 
    private HyperbolicTranslation translation;   
    
    // refresh display when new item added to results.
    public void nodeAdded(Graph arg0, Node arg1) {
        actmap.runNow("filter");
    }
    
    public void reDraw() {
        actmap.runNow("filter");
    }
    
    public Display getDisplay() {
    
        if(display == null) {
            actmap = new ActivityMap();
            ItemRegistry registry = getItemRegistry();
            display = new Display();


           // create a null renderer for use when no label should be shown
           NullRenderer nodeRenderer2 = new NullRenderer();
           // create an edge renderer with custom curved edges
           DefaultEdgeRenderer edgeRenderer = new DefaultEdgeRenderer() {
               protected void getCurveControlPoints(EdgeItem eitem, 
                   Point2D[] cp, double x1, double y1, double x2, double y2) 
               {
                   Point2D c = eitem.getLocation();      
                   cp[0].setLocation(c);
                   cp[1].setLocation(c);
               } //
            };
           edgeRenderer.setEdgeType(DefaultEdgeRenderer.EDGE_TYPE_CURVE);
           edgeRenderer.setRenderType(ShapeRenderer.RENDER_TYPE_DRAW);
           edgeRenderer.setWeightAttributeName(Retriever.WEIGHT_ATTRIBUTE);
           edgeRenderer.setWeightType(DefaultEdgeRenderer.WEIGHT_TYPE_LINEAR);               
            
            // set the renderer factory
           registry.setRendererFactory(new DemoRendererFactory(
                getTextRenderer(), nodeRenderer2, edgeRenderer));
            
            // initialize the display
            //display.setSize(500,460);
           display.setSize(500,400);
           display.setItemRegistry(registry);
           display.setBackground(Color.WHITE);
           display.addControlListener(new DemoControl());          
           TranslateControl dragger = new TranslateControl();
           display.addMouseListener(dragger);
           display.addMouseMotionListener(dragger);
           display.addControlListener(new ZoomControl());
           display.addControlListener(new DoubleClickMultiSelectFocusControl(vizs));
           display.addControlListener(new ToolTipControl(Retriever.TOOLTIP_ATTRIBUTE));
    //@todo renable       display.addControlListener(new SendToMenuControl(menu,parent));
            
           // initialize repaint list
           ActionList repaint = new ActionList(registry);
           repaint.add(new HyperbolicTreeMapper());
           repaint.add(new HyperbolicVisibilityFilter());
           repaint.add(new RepaintAction());
           actmap.put("repaint", repaint);
           
            // initialize filter           
           ActionList filter  = new ActionList(registry);
           filter.add(new TreeFilter());
       //@todo replace treefilter with this to enable service filtereing    filter.add(new ServiceListTreeFilter());
           // still needs to be completed.
           filter.add(new HyperbolicTreeLayout());
           filter.add(new HyperbolicDemoColorFunction());
           filter.add(repaint);
           actmap.put("filter", filter);
           //graphLayout = filter;

           // intialize hyperbolic translation
           ActionList translate = new ActionList(registry);
           translation = new HyperbolicTranslation();
           translate.add(translation);
           translate.add(repaint);
           actmap.put("translate", translate);
           
           // intialize animated hyperbolic translation
           ActionList animate = new ActionList(registry, 1000, 20);
           animate.setPacingFunction(new SlowInSlowOutPacer());
           animate.add(translate);
           actmap.put("animate", animate);
           
           // intialize the end translation list
           ActionList endTranslate = new ActionList(registry);
           endTranslate.add(new HyperbolicTranslationEnd());
           actmap.put("endTranslate", endTranslate);                              
        }               
           return display;
    }
    
    
    public class TranslateControl extends MouseAdapter implements MouseMotionListener {
        boolean drag = false;
        public void mousePressed(java.awt.event.MouseEvent e) {
            translation.setStartPoint(e.getX(), e.getY());
        } //
        public void mouseDragged(java.awt.event.MouseEvent e) {
            drag = true;
            translation.setEndPoint(e.getX(), e.getY());
            actmap.runNow("translate");
        } //
        public void mouseReleased(java.awt.event.MouseEvent e) {
            if ( drag ) {
                actmap.runNow("endTranslate");
                drag = false;
            }
        } //
        public void mouseMoved(java.awt.event.MouseEvent e) {
        } //
    } // end of inner class TranslateControl
        
    public class DemoControl extends ControlAdapter {
        public void itemEntered(VisualItem item, java.awt.event.MouseEvent e) {
            e.getComponent().setCursor(
                    Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        } //
        public void itemExited(VisualItem item, java.awt.event.MouseEvent e) {
            e.getComponent().setCursor(Cursor.getDefaultCursor());
        } //        
        public void itemClicked(VisualItem item, java.awt.event.MouseEvent e) {
            if ( item instanceof NodeItem ) {
            // animate a translation when a node is clicked
            int cc = e.getClickCount();
                if ( cc == 1 && 
                		!(e.isPopupTrigger() // popup trigger detection don't seem to work. 
                		|| e.isControlDown()) 
                		|| e.getButton() == MouseEvent.BUTTON2
                		|| e.getButton() == MouseEvent.BUTTON3) {
                    ItemRegistry registry = getItemRegistry();
                    TreeNode node = (TreeNode)registry.getEntity(item);
                    if ( node != null ) {                           
                        translation.setStartPoint(e.getX(), e.getY());
                        translation.setEndPoint(e.getX(), e.getY());
                        actmap.runNow("animate");
                        actmap.runAfter("animate", "endTranslate");
                    }
                }
            }
        } //
    } // end of inner class DemoController
    
    public static class DemoRendererFactory implements RendererFactory {
        Renderer nodeRenderer1;
        Renderer nodeRenderer2;
        Renderer edgeRenderer;
        public DemoRendererFactory(Renderer nr1, Renderer nr2, Renderer er) {
            nodeRenderer1 = nr1;
            nodeRenderer2 = nr2;
            edgeRenderer = er;
        } //
        public Renderer getRenderer(VisualItem item) {
            if ( item instanceof NodeItem ) {
                NodeItem n = (NodeItem)item;
                NodeItem p = (NodeItem)n.getParent();
                
                double d = Double.MAX_VALUE;
                
                Point2D nl = n.getLocation();
                if ( p != null) {
                    d = Math.min(d,nl.distance(p.getLocation()));
                    int idx = p.getChildIndex(n);
                    NodeItem b;
                    if ( idx > 0 ) {
                        b = (NodeItem)p.getChild(idx-1);
                        d = Math.min(d,nl.distance(b.getLocation()));
                    }
                    if ( idx < p.getChildCount()-1 ) {
                        b = (NodeItem)p.getChild(idx+1);
                        d = Math.min(d,nl.distance(b.getLocation()));
                    }
                }
                if ( n.getChildCount() > 0 ) {
                    NodeItem c = (NodeItem)n.getChild(0);
                    d = Math.min(d,nl.distance(c.getLocation()));
                }
                
                if ( d > 15 ) {
                    return nodeRenderer1;
                } else {
                    return nodeRenderer2;
                }
            } else if ( item instanceof EdgeItem ) {
                return edgeRenderer;
            } else {
                return null;
            }
        } //
    } // end of inner class DemoRendererFactory
    
    public static class HyperbolicDemoColorFunction extends ColorFunction {
        int  thresh = 5;
        Color graphEdgeColor = Color.LIGHT_GRAY;
        Color selectedColor = Color.YELLOW;
        Color nodeColors[];
        Color edgeColors[];
       
        public HyperbolicDemoColorFunction() {
            nodeColors = new Color[thresh];
            edgeColors = new Color[thresh];
            for ( int i = 0; i < thresh; i++ ) {
                double frac = i / ((double)thresh);
                nodeColors[i] = ColorLib.getIntermediateColor(Color.RED, Color.BLACK, frac);
                edgeColors[i] = ColorLib.getIntermediateColor(Color.RED, Color.BLACK, frac);
            }
        } //
       
        public Paint getFillColor(VisualItem item) {
            if ( item instanceof NodeItem ) {
                String attr=  item.getAttribute("selected");                
                if (attr != null && attr.equals("true")) {
                    return selectedColor;
                } else {               
                    return Color.WHITE;
                }
            } else if ( item instanceof AggregateItem ) {
                return Color.LIGHT_GRAY;
            } else if ( item instanceof EdgeItem ) {
                return getColor(item);
            } else {
                return Color.BLACK;
            }
        } //
       
        public Paint getColor(VisualItem item) {
            if (item instanceof NodeItem) {
                 int d = ((NodeItem)item).getDepth();
                return nodeColors[Math.min(d,thresh-1)];
            } else if (item instanceof EdgeItem) {
                EdgeItem e = (EdgeItem) item;
                if ( e.isTreeEdge() ) {
                    int d, d1, d2;
                     d1 = ((NodeItem)e.getFirstNode()).getDepth();
                     d2 = ((NodeItem)e.getSecondNode()).getDepth();
                     d = Math.max(d1, d2);
                    return edgeColors[Math.min(d,thresh-1)];
                } else {
                    return graphEdgeColor;
                }
            } else {
                return Color.BLACK;
            }
        } //
    } // end of inner class DemoColorFunction
    
}