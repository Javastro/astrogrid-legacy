/**
 * 
 */
package org.astrogrid.desktop.modules.system;

/* Copied from: 
 * @(#) CSH.java 1.49 - last change made 07/14/03
 *
 * Copyright (c) 1997 Sun Microsystems, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Sun
 * Microsystems, Inc. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Sun.
 *
 * SUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. SUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 */


import java.applet.Applet;
import java.awt.AWTEvent;
import java.awt.ActiveEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Image;
import java.awt.MenuComponent;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.lang.ref.WeakReference;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.WeakHashMap;

import javax.swing.Action;
import javax.swing.CellRendererPane;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.TreePath;

import org.astrogrid.desktop.icons.IconHelper;

/** lifted, adapted and cutdown from the same named class in java help.
 * A convenience class that provides simple
 * access to context-senstive help functionality. It creates a default JavaHelp
 * viewer as well as ActionListeners for "Help" keys, on-item help, and
 * help buttons.
 *
 * @author Roger D. Brinkley
 * @author Eduardo Pelegri-Llopart
 * @author Stepan Marek
 * @version	1.49	07/14/03
 *
 */
public class CSH {
    
	  /*
     * Generic displayHelp for all CSH.Display* subclasses
     * 
     * @param hb The HelpBroker to display in. Can be null but hs and 
     *		and presentation must be supplies
     * @param hs The HelpSet to display in. Can be null if hb != null
     * @param obj The object for which the help is displayed for
     * @param source The Window for focusOwner purposes
     * @param event The event that caused this action.
     */
    static void displayHelp(final HelpServerImpl hb, final Object obj, 
				     final AWTEvent event) {
	final String helpID = CSH.getHelpIDString(obj, event);
	hb.showHelpForTarget(helpID);

    }

	
    static private java.util.Map<Object, Hashtable> comps;
    static private java.util.Map<Component, CellRendererPane> parents;
    //static private java.util.Vector managers = new Vector();
    /**
     * Returns the dynamic HelpID for a object. The method passes the arguments
     * into all registered CSH manageres to obtain dynamic HelpID. If no manager
     * provides HelpID for the object, the static HelpID is returned or traverse
     * to the component's ancestors for help.
     * @exception IllegalArgumentException comp is neither <code>Component</code> nor
     * <code>MenuItem</code>.
     * @since 2.0
     */
    static String getHelpIDString(final Object comp, final AWTEvent evt) {
        
        if (comp == null) {
            return null;
        }

        String helpID = _getHelpIDString(comp);
        
        if (helpID == null) {
            helpID = getHelpIDString(getParent(comp), evt);
        }
        
        return helpID;
    }
    
    /**
     * Returns the static HelpID for given object.
     * @exception IllegalArgumentException comp is neither <code>Component</code> nor
     * <code>MenuItem</code>.
     */
    private static String _getHelpIDString(final Object comp) {
        String helpID = null;
        if (comp != null) {
            if (comp instanceof JComponent) {
                helpID = (String) ((JComponent)comp).getClientProperty("HelpID");
            } else if ((comp instanceof Component) || (comp instanceof MenuItem)) {
                if (comps != null) {
                    final Hashtable clientProps = comps.get(comp);
                    if (clientProps !=null) {
                        helpID = (String) clientProps.get("HelpID");
                    }
                }
            } else {
                throw new IllegalArgumentException("Invalid Component");
            }
        }
        return helpID;
    }
    
    /**
     * Returns ancestor for an object.
     * @exception IllegalArgumentException comp is neither <code>Component</code> nor
     * <code>MenuItem</code>.
     */
    private static Object getParent(final Object comp) {
        
        if (comp == null) {
            return null;
        }
        
        Object parent = null;
        if (comp instanceof MenuComponent) {
            parent = ((MenuComponent)comp).getParent();
        } else if (comp instanceof JPopupMenu) {
            parent = ((JPopupMenu)comp).getInvoker();
        } else if (comp instanceof Component) {
            parent = ((Component)comp).getParent();
        } else {
            throw new IllegalArgumentException("Invalid Component");
        }
        
        if (parent == null && parents != null) {
            parent = parents.get(comp);
        }
        
        return parent;
    }
        
    

    
    
    /**
     * Store HelpID String for an object.
     * @exception IllegalArgumentException comp is neither <code>Component</code> nor
     * <code>MenuItem</code>.
     */
    private static void _setHelpIDString(final Object comp, final String helpID) {
        
        if (comp instanceof JComponent) {
            
            // For JComponents just use client property
            ((JComponent)comp).putClientProperty("HelpID", helpID);
        } else if ((comp instanceof Action)) {
        	//@todo need to check that this gets propagated to it's components.
        	((Action)comp).putValue("HelpID",helpID);
        } else if ((comp instanceof Component) || (comp instanceof MenuItem)) {
            // For MenuItems and Components we have an internal Hashtable of
            // components and their properties.
            
            // Initialize as necessary
            if (comps == null) {
                comps = new WeakHashMap<Object, Hashtable>(5);
            }
            
            // See if this component has already set some client properties
            // If so update.
            // If not then create the client props (as needed) and add to
            // the internal Hashtable of components and properties
            Hashtable clientProps = comps.get(comp);
            if (clientProps != null) {
                if (helpID != null) {
                    clientProps.put("HelpID", helpID);
                } else {
                    clientProps.remove("HelpID");
                    if (clientProps.isEmpty()) {
                        comps.remove(comp);
                    }
                }
            } else {
                // Only create properties if there is a valid helpID
                if (helpID != null) {
                    clientProps = new Hashtable(2);
                    clientProps.put("HelpID", helpID);
                    comps.put(comp, clientProps);
                }
            }
            
        } else {
            throw new IllegalArgumentException("Invalid Component");
        }
    }
    

    /**
     * Sets the helpID for a Component.
     * If helpID is null this method removes the helpID from the component.
     * @exception IllegalArgumentException comp is neither <code>Component</code> nor
     */
    public static void setHelpIDString(final Component comp, final String helpID) {
        _setHelpIDString(comp, helpID);
    }
    
    
    public static void setHelpIDString(final Action act, final String helpID) {
        _setHelpIDString(act, helpID);
    }
    /**
     * Sets the helpID for a MenuItem.
     * If helpID is null, this method removes the helpID from the component.
     * @exception IllegalArgumentException comp is neither <code>Component</code> nor
     */
    public static void setHelpIDString(final MenuItem comp, final String helpID) {
        _setHelpIDString(comp, helpID);
    }
    


    /**
     * Context Sensitive Event Tracking
     *
     * Creates a new EventDispatchThread from which to dispatch events. This
     * method returns when stopModal is invoked.
     *
     * @return MouseEvent The mouse event occurred. Null if
     * cancelled on an undetermined object.
     */
    static MouseEvent getMouseEvent() {
        // Should the cursor change to a quesiton mark here or
        // require the user to change the cursor externally to this method?
        // The problem is that each component can have it's own cursor.
        // For that reason it might be better to have the user change the
        // cusor rather than us.
        
        // To track context-sensitive events get the event queue and process
        // the events the same way EventDispatchThread does. Filter out
        // ContextSensitiveEvents SelectObject & Cancel (MouseDown & ???).
        // Note: This code only handles mouse events. Accessiblity might
        // require additional functionality or event trapping
        
        // If the eventQueue can't be retrieved, the thread gets interrupted,
        // or the thread isn't a instanceof EventDispatchThread then return
        // a null as we won't be able to trap events.
        try {
            if (EventQueue.isDispatchThread()) {
                EventQueue eq = null;
                
                // Find the eventQueue. If we can't get to it then just return
                // null since we won't be able to trap any events.
                
                try {
                    eq = Toolkit.getDefaultToolkit().getSystemEventQueue();
                } catch (final Exception ee) {
                    debug(ee);
                }
                
                // Safe guard
                if (eq == null) {
                    return null;
                }
                
                int eventNumber = -1;
                
                // Process the events until an object has been selected or
                // the context-sensitive search has been canceled.
                while (true) {
                    // This is essentially the body of EventDispatchThread
                    // modified to trap context-senstive events and act
                    // appropriately
                    eventNumber++;
                    final AWTEvent event = eq.getNextEvent();
                    final Object src = event.getSource();
                    // can't call eq.dispatchEvent
                    // so I pasted it's body here
                    
                    // debug(event);
                    
                    // Not sure if I should suppress ActiveEvents or not
                    // Modal dialogs do. For now we will not suppress the
                    // ActiveEvent events
                    
                    if (event instanceof ActiveEvent) {
                        ((ActiveEvent)event).dispatch();
                        continue;
                    }
                    
                    if (src instanceof Component) {
                        // Trap the context-sensitive events here
                        if (event instanceof KeyEvent) {
                            final KeyEvent e = (KeyEvent) event;
                            // if this is the cancel key then exit
                            // otherwise pass all other keys up
                            if (e.getKeyCode() == KeyEvent.VK_CANCEL ||
                            e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                                e.consume();
                                return null;
                            } else {
                                e.consume();
                                // dispatchEvent(event);
                            }
                        } else if (event instanceof MouseEvent) {
                            final MouseEvent e = (MouseEvent) event;
                            final int eID = e.getID();
                            if ((eID == MouseEvent.MOUSE_CLICKED ||
                            eID == MouseEvent.MOUSE_PRESSED ||
                            eID == MouseEvent.MOUSE_RELEASED) &&
                            SwingUtilities.isLeftMouseButton(e)) {
                                if (eID == MouseEvent.MOUSE_CLICKED) {
                                    if (eventNumber == 0) {
                                        dispatchEvent(event);
                                        continue;
                                    }
                                }
                                e.consume();
                                return e;
                            } else {
                                e.consume();
                            }
                        } else {
                            dispatchEvent(event);
                        }
                    } else if (src instanceof MenuComponent) {
                        if (event instanceof InputEvent) {
                            ((InputEvent)event).consume();
                        }
                    } else {
                        System.err.println("unable to dispatch event: " + event);
                    }
                }
            }
        } catch(final InterruptedException e) {
            debug(e);
        }
        debug("Fall Through code");
        return null;
    }
    
    private static void dispatchEvent(final AWTEvent event) {
        final Object src = event.getSource();
        if (event instanceof ActiveEvent) {
            // This could become the sole method of dispatching in time.
            ((ActiveEvent)event).dispatch();
        } else if (src instanceof Component) {
            ((Component)src).dispatchEvent(event);
        } else if (src instanceof MenuComponent) {
            ((MenuComponent)src).dispatchEvent(event);
        } else {
            System.err.println("unable to dispatch event: " + event);
        }
    }
    
    /**
     * Gets the higest visible component in a ancestor hierarchy at
     * specific x,y coordinates
     */
    static Object getDeepestObjectAt(final Object parent, final int x, final int y) {
        if (parent instanceof Container) {
            final Container cont = (Container)parent;
            // use a copy of 1.3 Container.findComponentAt
            Component child = findComponentAt(cont, cont.getWidth(), cont.getHeight(), x, y);
            if (child != null && child != cont) {
                if (child instanceof JRootPane) {
                    final JLayeredPane lp = ((JRootPane)child).getLayeredPane();
                    final Rectangle b = lp.getBounds();
                    child = (Component)getDeepestObjectAt(lp, x - b.x, y - b.y);
                }
                if (child != null) {
                    return child;
                }
            }
        }
        // if the parent is not a Container then it might be a MenuItem.
        // But even if it isn't a MenuItem just return the parent because
        // that's a close as we can come.
        return parent;
    }
    
    private static Component findComponentAt(final Container cont, final int width, final int height, final int x, final int y) {
        synchronized (cont.getTreeLock()) {
            
            if (!((x >= 0) && (x < width) && (y >= 0) && (y < height) && cont.isVisible() && cont.isEnabled())) {
                return null;
            }
            
            final Component[] component = cont.getComponents();
            final int ncomponents = cont.getComponentCount();
            
            // Two passes: see comment in sun.awt.SunGraphicsCallback
            for (int i = 0 ; i < ncomponents ; i++) {
                Component comp = component[i];
                Rectangle rect = null;
                
                if (comp instanceof CellRendererPane) {
                    final Component c = getComponentAt((CellRendererPane)comp, x, y);
                    if (c != null) {
                        rect = getRectangleAt((CellRendererPane)comp, x, y);
                        comp = c;
                    }
                }
                
                if (comp != null && !comp.isLightweight()) {
                    if (rect == null || rect.width == 0 || rect.height == 0) {
                        rect = comp.getBounds();
                    }
                    if (comp instanceof Container) {
                        comp = findComponentAt((Container)comp, rect.width, rect.height, x - rect.x, y - rect.y);
                    } else {
                        comp = comp.getComponentAt(x - rect.x, y - rect.y);
                    }
                    if (comp != null && comp.isVisible() && comp.isEnabled()) {
                        return comp;
                    }
                }
            }
            
            for (int i = 0 ; i < ncomponents ; i++) {
                Component comp = component[i];
                Rectangle rect = null;
                
                if (comp instanceof CellRendererPane) {
                    final Component c = getComponentAt((CellRendererPane)comp, x, y);
                    if (c != null) {
                        rect = getRectangleAt((CellRendererPane)comp, x, y);
                        comp = c;
                    }
                }
                
                if (comp != null && comp.isLightweight()) {
                    if (rect == null || rect.width == 0 || rect.height == 0) {
                        rect = comp.getBounds();
                    }
                    if (comp instanceof Container) {
                        comp = findComponentAt((Container)comp, rect.width, rect.height, x - rect.x, y - rect.y);
                    } else {
                        comp = comp.getComponentAt(x - rect.x, y - rect.y);
                    }
                    if (comp != null && comp.isVisible() && comp.isEnabled()) {
                        return comp;
                    }
                }
            }
            return cont;
        }
    }
    
    /**
     * Returns the Rectangle enclosing component part that the component
     * provided by renderer will be draw into.
     */
    private static Rectangle getRectangleAt(final CellRendererPane cont, final int x, final int y) {
        Rectangle rect = null;
        final Container c = cont.getParent();
        // I can process only this four components at present time
        if (c instanceof JTable) {
            rect = getRectangleAt((JTable)c, x, y);
        } else if (c instanceof JTableHeader) {
            rect = getRectangleAt((JTableHeader)c, x, y);
        } else if (c instanceof JTree) {
            rect = getRectangleAt((JTree)c, x, y);
        } else if (c instanceof JList) {
            rect = getRectangleAt((JList)c, x, y);
        }
        return rect;
    }
    
    /**
     * Returns the Component provided by Renderer at x, y coordinates.
     */
    private static Component getComponentAt(final CellRendererPane cont, final int x, final int y) {
        Component comp = null;
        final Container c = cont.getParent();
        // I can process only this four components at present time
        if (c instanceof JTable) {
            comp = getComponentAt((JTable)c, x, y);
        } else if (c instanceof JTableHeader) {
            comp = getComponentAt((JTableHeader)c, x, y);
        } else if (c instanceof JTree) {
            comp = getComponentAt((JTree)c, x, y);
        } else if (c instanceof JList) {
            comp = getComponentAt((JList)c, x, y);
        }
        
        // store reference from comp to CellRendererPane
        // It is needed for backtrack searching of HelpSet and HelpID
        // in getHelpSet() and getHelpIDString().
        if (comp != null) {
            if (parents == null) {
                // WeakHashMap of WeakReferences
                parents = new WeakHashMap(4) {
                    @Override
                    public Object put(final Object key, final Object value) {
                        return super.put(key, new WeakReference<Object>(value));
                    }
                    @Override
                    public Object get(final Object key) {
                        final WeakReference wr = (WeakReference)super.get(key);
                        if (wr != null) {
                            return wr.get();
                        } else {
                            return null;
                        }
                    }
                };
            }
            parents.put(comp, cont);
        }
        return comp;
    }
    
    private static Rectangle getRectangleAt(final JTableHeader header, final int x, final int y) {
        Rectangle rect = null;
        try {
            final int column = header.columnAtPoint(new Point(x, y));
            rect = header.getHeaderRect(column);
        } catch (final Exception e) {
        }
        return rect;
    }
    
    private static Component getComponentAt(final JTableHeader header, final int x, final int y) {
        try {
            
            if (!(header.contains(x, y) && header.isVisible() && header.isEnabled())) {
                return null;
            }
            
            final TableColumnModel columnModel = header.getColumnModel();
            final int columnIndex = columnModel.getColumnIndexAtX(x);
            final TableColumn column = columnModel.getColumn(columnIndex);
            
            TableCellRenderer renderer = column.getHeaderRenderer();
            if (renderer == null) {
                renderer = header.getDefaultRenderer();
            }
            
            return renderer.getTableCellRendererComponent(
            header.getTable(), column.getHeaderValue(), false, false, -1, columnIndex);
            
        } catch (final Exception e) {
            // NullPointerException in case of (header == null) or (columnModel == null)
            // ArrayIndexOutOfBoundsException from getColumn(columnIndex)
        }
        return null;
    }
    
    private static Rectangle getRectangleAt(final JTable table, final int x, final int y) {
        Rectangle rect = null;
        try {
            final Point point = new Point(x, y);
            final int row = table.rowAtPoint(point);
            final int column = table.columnAtPoint(point);
            rect = table.getCellRect(row, column, true);
        } catch (final Exception e) {
        }
        return rect;
    }
    
    private static Component getComponentAt(final JTable table, final int x, final int y) {
        try {
            
            if (!(table.contains(x, y) && table.isVisible() && table.isEnabled())) {
                return null;
            }
            
            final Point point = new Point(x, y);
            final int row = table.rowAtPoint(point);
            final int column = table.columnAtPoint(point);
            
            if (table.isEditing() && table.getEditingRow() == row && table.getEditingColumn() == column) {
                // Pointed component is provided by TableCellEditor. Editor
                // component is part of component hierarchy so it is checked
                // directly in loop in findComponentAt()
                // comp = table.getEditorComponent();
                return null;
            }
            
            final TableCellRenderer renderer = table.getCellRenderer(row, column);
            return table.prepareRenderer(renderer, row, column);
            
        } catch (final Exception e) {
        }
        return null;
    }
    
    private static Rectangle getRectangleAt(final JTree tree, final int x, final int y) {
        Rectangle rect = null;
        try {
            final TreePath path = tree.getPathForLocation(x, y);
            rect = tree.getPathBounds(path);
        } catch (final Exception e) {
        }
        return rect;
    }
    
    private static Component getComponentAt(final JTree tree, final int x, final int y) {
        try {
            
            final TreePath path = tree.getPathForLocation(x, y);
            
            if (tree.isEditing() && tree.getSelectionPath() == path) {
                return null;
            }
            
            final int row = tree.getRowForPath(path);
            final Object value = path.getLastPathComponent();
            final boolean isSelected = tree.isRowSelected(row);
            final boolean isExpanded = tree.isExpanded(path);
            final boolean isLeaf = tree.getModel().isLeaf(value);
            final boolean hasFocus= tree.hasFocus() && tree.getLeadSelectionRow() == row;
            
            return tree.getCellRenderer().getTreeCellRendererComponent(
            tree, value, isSelected, isExpanded, isLeaf, row, hasFocus);
            
        } catch (final Exception e) {
            return null;
        }
    }
    
    private static Rectangle getRectangleAt(final JList list, final int x, final int y) {
        Rectangle rect = null;
        try {
            final int index = list.locationToIndex(new Point(x, y));
            rect = list.getCellBounds(index, index);
        } catch (final Exception e) {
        }
        return rect;
    }
    
    private static Component getComponentAt(final JList list, final int x, final int y) {
        try {
            
            final int index = list.locationToIndex(new Point(x, y));
            final Object value = list.getModel().getElementAt(index);
            final boolean isSelected = list.isSelectedIndex(index);
            final boolean hasFocus = list.hasFocus() && list.getLeadSelectionIndex() == index;
            
            return list.getCellRenderer().getListCellRendererComponent(
            list, value, index, isSelected, hasFocus);
        } catch (final Exception e) {
            return null;
        }
    }

    
    /**
     * An ActionListener that displays the help of the
     * object that currently has focus. This method is used
     * to enable HelpKey action listening for components other than
     * the RootPane. This listener determines if the
     * object with the current focus has a helpID. If it does, the helpID
     * is displayed,
     * otherwise the helpID on the action's source is displayed (if one exists).
     *
     */
    static class DisplayHelpFromFocus implements ActionListener {
        
        private HelpServerImpl hb = null;
         
        public DisplayHelpFromFocus(final HelpServerImpl hb) {
            if (hb == null) {
                throw new NullPointerException("hb");
            }
            this.hb = hb;
        }

        public void actionPerformed(final ActionEvent e) {
            
            final Component src = (Component) e.getSource();            
            Component comp = CSH.findFocusOwner(src);
            
            debug("focusOwner:"+comp);
            
            if (comp == null) {
                comp = src;
            }
            
	    displayHelp(hb,  comp, e);
	}	    
    }
    
    
    /**
     * Returns the popup menu which is at the root of the menu system
     * for this popup menu.
     *
     * @return the topmost grandparent <code>JPopupMenu</code>
     */
    private static JPopupMenu getRootPopupMenu(JPopupMenu popup) {
        while((popup != null) &&
        (popup.getInvoker() instanceof JMenu) &&
        (popup.getInvoker().getParent() instanceof JPopupMenu)
        ) {
            popup = (JPopupMenu) popup.getInvoker().getParent();
        }
        return popup;
    }
    
    /**
     * Returns the deepest focused Component or the deepest JPopupMenu
     * or the deepest armed MenuItem from JPopupMenu hierarchy.
     */
    private static Component findFocusOwner(final JPopupMenu popup) {
        if (popup == null) {
            return null;
        }
        synchronized (popup.getTreeLock()) {
            if (!popup.isVisible()) {
                return null;
            }
            Component comp = null;
            for (int i = 0, n = popup.getComponentCount(); i < n; i++) {
                final Component c = popup.getComponent(i);
                if (c.hasFocus()) {
                    comp = c;
                    break;
                }
                if (c instanceof JMenu && ((JMenu)c).isPopupMenuVisible()) {
                    comp = c;
                }
                if (c instanceof JMenuItem && ((JMenuItem)c).isArmed()) {
                    comp = c;
                }
            }
            if (comp instanceof JMenu) {
                comp = findFocusOwner(((JMenu)comp).getPopupMenu());
            }
            if (comp != null) {
                return comp;
            }
        }
        return popup;
    }
    
    /**
     * Returns the child component which has focus with respects
     * of PopupMenu visibility.
     */
    static Component findFocusOwner(final Component c) {
        synchronized (c.getTreeLock()) {
            
            if (c instanceof JPopupMenu) {
                return findFocusOwner(getRootPopupMenu((JPopupMenu)c));
            }
            
            if (c.hasFocus()) {
                return c;
            }
            
            if (c instanceof Container) {
                for (int i = 0, n = ((Container)c).getComponentCount(); i < n; i++) {
                    final Component focusOwner = findFocusOwner(((Container)c).getComponent(i));
                    if (focusOwner != null) {
                        return focusOwner;
                    }
                }
            }
            return null;  // Component doesn't have hasFocus().
        }
    }
    
    /**
     * An ActionListener that displays help on a
     * selected object after tracking context-sensitive events.
     * It is normally activated
     * from a button. It uses CSH.trackingCSEvents to track context-sensitive
     * events and when an object is selected it gets
     * the helpID for the object and displays the helpID in the help viewer.
     */
    static class DisplayHelpAfterTracking implements ActionListener {
        
        private HelpServerImpl hb = null;
        
        public DisplayHelpAfterTracking(final HelpServerImpl hb) {
            if (hb == null) {
                throw new NullPointerException("hb");
            }
            this.hb = hb;
        }
        
	
        public void actionPerformed(final ActionEvent e) {
            Cursor onItemCursor;
            final Cursor oldCursor;
            
//            // Get the onItemCursor
//            onItemCursor = (Cursor) UIManager.get("HelpOnItemCursor");
//            if (onItemCursor == null) {
//            	System.out.println("no help on item cursor");
//                return;
//            }
            final Toolkit tk = Toolkit.getDefaultToolkit();
            final Dimension sz = tk.getBestCursorSize(24,24);
            if (sz.width != 0) {
            	final Point point = new Point(5,2);
            	final Image img = IconHelper.loadIcon("OnItemCursor.gif").getImage();            	
            	onItemCursor = tk.createCustomCursor(img, point, "help");
            } else { // fallback
            	onItemCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
            }
            
            // change all the cursors on all windows
            final Vector<Component> topComponents = getTopContainers(e.getSource());
            cursors = null;
            
                cursors = new Hashtable<Component, Cursor>();
                final Enumeration<Component> enm = topComponents.elements();
                while (enm.hasMoreElements()) {
                    setAndStoreCursors((Container)enm.nextElement(), onItemCursor);
                }
            
            final MouseEvent event = CSH.getMouseEvent();
            debug("CSH.getMouseEvent() >>> "+event);
            
            if (event != null) {
                
                final Object obj = CSH.getDeepestObjectAt(event.getSource(), event.getX(), event.getY());
                debug("CSH.getDeepestObjectAt() >>> "+obj);
                
                if (obj != null) {

                    displayHelp(hb, 	obj, event);
		}
            }
            
            // restore the old cursors
                final Enumeration<Component> containers = topComponents.elements();
                while (containers.hasMoreElements()) {
                    resetAndRestoreCursors((Container)containers.nextElement());
                }
            
            cursors = null;
        }
        
        private Hashtable<Component, Cursor> cursors;
        
        /*
         * Get all top level containers to change it's cursors
         */
        private static Vector<Component> getTopContainers(final Object source) {
            // This method is used to obtain all top level components of application
            // for which the changing of cursor to question mark is wanted.
            // Method Frame.getFrames() is used to get list of Frames and
            // Frame.getOwnedWindows() method on elements of the list
            // returns all Windows, Dialogs etc. It works correctly in application.
            // Problem is in applets. There is no way how to get reference to applets
            // from elsewhere than applet itself. So, if request for CSH (this means
            // pressing help button or select help menu item) does't come from component
            // in a Applet, cursor for applets is not changed to question mark. Only for
            // Frames, Windows and Dialogs is cursor changed properly.
            
            final Vector<Component> containers = new Vector<Component>();
            Component topComponent = null;
            topComponent = getRoot(source);
            if (topComponent instanceof Applet) {
                try {
                    final Enumeration applets = ((Applet)topComponent).getAppletContext().getApplets();
                    while (applets.hasMoreElements()) {
                        containers.add((Component)applets.nextElement());
                    }
                } catch (final NullPointerException npe) {
                    containers.add(topComponent);
                }
            }
            final Frame frames[] = Frame.getFrames();
            for (int i = 0; i < frames.length; i++) {
                final Window[] windows = frames[i].getOwnedWindows();
                for (int j = 0; j < windows.length; j++) {
                    containers.add(windows[j]);
                }
                if (!containers.contains(frames[i])) {
                    containers.add(frames[i]);
                }
            }
            return containers;
        }
        
        
        private static Component getRoot(Object comp) {
            Object parent = comp;
            while (parent != null) {
                comp = parent;
                if (comp instanceof MenuComponent) {
                    parent = ((MenuComponent)comp).getParent();
                } else if (comp instanceof Component) {
                    if (comp instanceof Window) {
                        break;
                    }
                    if (comp instanceof Applet) {
                        break;
                    }
                    parent = ((Component)comp).getParent();
                } else {
                    break;
                }
            }
            if (comp instanceof Component) {
                return ((Component)comp);
            }
            return null;
        }
        
        
        /*
         * Set the cursor for a component and its children.
         * Store the old cursors for future resetting
         */
        private void setAndStoreCursors(final Component comp, final Cursor cursor) {
            if (comp == null) {
                return;
            }
            final Cursor compCursor = comp.getCursor();
            if (compCursor != cursor) {
                cursors.put(comp, compCursor);
                debug("set cursor on " + comp);
                comp.setCursor(cursor);
            }
            if (comp instanceof Container) {
                final Component component[] = ((Container)comp).getComponents();
                for (int i = 0 ; i < component.length; i++) {
                    setAndStoreCursors(component[i], cursor);
                }
            }
        }
        
        /*
         * Actually restore the cursor for a component and its children
         */
        private void resetAndRestoreCursors(final Component comp) {
            if (comp == null) {
                return;
            }
            final Cursor oldCursor = cursors.get(comp);
            if (oldCursor != null) {
                debug("restored cursor " + oldCursor + " on " + comp);
                comp.setCursor(oldCursor);
            }
            if (comp instanceof Container) {
                final Component component[] = ((Container)comp).getComponents();
                for (int i = 0 ; i < component.length; i++) {
                    resetAndRestoreCursors(component[i]);
                }
            }
        }
    }
    
    /**
     * An ActionListener that
     * gets the helpID for the action source and displays the helpID in the
     * help viewer.
     *
     */
    static class DisplayHelpFromSource implements ActionListener {
        
        private final HelpServerImpl hb;
        
        public DisplayHelpFromSource(final HelpServerImpl hb) {
            if (hb == null) {
                throw new NullPointerException("hb");
            }
            this.hb = hb;
        }
     
        
        public void actionPerformed(final ActionEvent e) {
	    final Object source = e.getSource();
	    displayHelp(hb,
			source,  e);
            
        }
    }
    
    /**
     * Debugging code...
     */
    
    private static final boolean debug = false;
    static void debug(final Object msg) {
        if (debug) {
            System.err.println("CSH: "+msg);
        }
    }
    
}

