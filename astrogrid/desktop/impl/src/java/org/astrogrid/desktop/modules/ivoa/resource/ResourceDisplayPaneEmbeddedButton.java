/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa.resource;

import java.awt.Component;
import java.awt.Container;
import java.util.EventObject;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import org.astrogrid.desktop.modules.system.ProgrammerError;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.comp.ResourceDisplayPane;

/** A button designed to be embedded in the resource viewer HTML.
 * <p/>
 * As it's written as html, this obect needs to access all it's requirements through its containing class,
 * or through param tags.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 19, 20083:09:36 PM
 */
public class ResourceDisplayPaneEmbeddedButton extends JButton {

    /** pass an event object - like an action performed object.
     * use this to find the ResourceDisplayPane parent,
     * @param e
     * @return
     */
    
    
    public static ResourceDisplayPane getResourceDisplayPane(final EventObject e) {
        final Container ancestor = SwingUtilities.getAncestorNamed(ResourceDisplayPane.class.getName(),(Component)e.getSource());
        if (ancestor == null) {
            throw new ProgrammerError("No ancestor available");
        }
        if (ancestor instanceof ResourceDisplayPane) {
           return  ((ResourceDisplayPane)ancestor);          
        } else {
            throw new ProgrammerError("Ancestor is not a ResourceDisplayPane "
                    + ancestor.getClass().getName());
        }
         
    }
    
    public static UIComponent getUIComponent(final EventObject e) {
        final Container ancestor = SwingUtilities.getAncestorOfClass(UIComponent.class,(Component)e.getSource());
        if (ancestor == null) {
            throw new ProgrammerError("No ancestor available");
        }
        if (ancestor instanceof UIComponent) {
           return  ((UIComponent)ancestor);          
        } else {
            throw new ProgrammerError("Ancestor is not a ResourceDisplayPane "
                    + ancestor.getClass().getName());
        }
         
    }
}

