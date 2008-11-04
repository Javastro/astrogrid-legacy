/**
 * 
 */
package org.astrogrid.desktop.modules.ui.taskrunner;

import javax.swing.JMenu;

import org.astrogrid.desktop.modules.ui.UIComponent;

/** 
 * Extended {@link UIComponent} interface that provides access to a menubar menu
 * to which context-sensitive actions can be added.
 * 
 * The client of this interface (i.e. whatever is adding things to the menu) is 
 * responsible for changing their visibiliy as the context changes. one way to do this
 * would be to add a componentListener or HierarchyListener for visibility events.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Oct 6, 20072:53:55 PM
 */
public interface UIComponentWithMenu extends UIComponent {
    /** menubar menu to add context sensitive actions to. Use assuming it to already have other
     * menu entries.
     * It's this client's responsibility to manage the visibility of any menu entries it
     * adds to this menu
     */
    JMenu getContextMenu();
}