/**
 * 
 */
package org.astrogrid.desktop.modules.ui.taskrunner;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JToggleButton;

import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.acr.ivoa.Vosi;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.ui.RegistryBrowser;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ivoa.resource.CapabilityTester;
import org.astrogrid.desktop.modules.ivoa.resource.HtmlBuilder;
import org.astrogrid.desktop.modules.ui.comp.PinnableLabel;
import org.astrogrid.desktop.modules.ui.comp.ResourceDisplayPane;
import org.astrogrid.desktop.modules.ui.taskrunner.TaskParametersForm.Model;

import ca.odell.glazedlists.CompositeList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FunctionList;

/** JEditor pane with methods for displaying help on task parameters.
 * 
 * contains logic to detect when mouse is over a parameter, and will display help 
 * for that parameter.
 * 
 * also manages 'pinning' of data.
 * @TEST
 * Also allows the user to 'pin' the help.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Oct 2, 200710:31:37 AM
 */
public class ParametersInfoPane extends ResourceDisplayPane implements MouseListener, ItemListener, FunctionList.AdvancedFunction {

    private final Model model;
    /** list of parameter pinnable labels */
    private final FunctionList paramPins;
    /** list of additional pinnable labels to manage */
    private final List additionalPins;
/**
 * construct a new info pane
 * @param model used to access the current resource metadata
 * @param allElements list of AbstractFormElements - automatically responds to 
 * list changes by registering / deregistering  the labels with this info pane.
 * @param browser 
 * @param regBrowser 
 */
    public ParametersInfoPane(Model model, EventList allElements, BrowserControl browser, RegistryBrowser regBrowser, CapabilityTester tester,Vosi vosi) {
        super(browser,regBrowser,tester, vosi);
        this.model = model;
        // using a function list means that we can register & deregister simply as the underlying parameter list changes.
        this.paramPins = new FunctionList(allElements,this); 
        this.additionalPins = new java.util.ArrayList(1);
    }
    
    private void register(JToggleButton l) {
        l.addMouseListener(this);
        l.addItemListener(this);
    }
    
    private void unregister(JToggleButton l) {
        l.removeMouseListener(this);
        l.removeItemListener(this);
        if (pinnedTo == l) {
            pinnedTo = null;
        }
    }
    
    /** manually register a pinnable label with the info pane.
     *  */
    public void registerAdditional(JToggleButton l) {
        register(l);
        additionalPins.add(l);
    }
    
    // funciton list interface - used to register / deregister parameters as the form changes.
    public void dispose(Object sourceValue, Object transformedValue) {
        // item removed
        unregister((JToggleButton)transformedValue);
    }
    
    public Object reevaluate(Object sourceValue, Object transformedValue) {
        // item changed
        unregister((JToggleButton)transformedValue);
        final PinnableLabel label = ((AbstractTaskFormElement)sourceValue).getLabel();
        register(label);
        return label;
    }
    
    public Object evaluate(Object sourceValue) {
        // item added
        final PinnableLabel label = ((AbstractTaskFormElement)sourceValue).getLabel();
        register(label);
        return label;
    }

// help display methods    
    private final HtmlBuilder sb = new HtmlBuilder();
    /**
     * @param comp
     */
    private void displayHelpForComponent(JComponent comp) {
        AbstractTaskFormElement t = (AbstractTaskFormElement)comp.getClientProperty(AbstractTaskFormElement.class);         
        if (t != null) {
            sb.clear();
            sb.append("<html>");
            final ParameterBean d = t.getDescription();
            sb.h2(d.getUiName());
            sb.append(d.getDescription()).append("<p>");
            if (d.getType() != null) {
                sb.br().append("Type : ").append(d.getType());
            }
            if (d.getUcd() != null) {
                sb.br().append("UCD : ").append(d.getUcd());
            }
            if (d.getUnits() != null) {
                sb.br().append("Units : ").append(d.getUnits());
            }           
            setText(sb.toString());
            setCaretPosition(0);
        } else {
            displayCurrentResource();
        }
    }
    /**
     * display an overview of the current resource / cea app.
     */
    public void displayCurrentResource() {
      display(model.currentResource());
    }
    /** if not null, help is pinned to this label */
    private JToggleButton pinnedTo;
// listener interface.

    // callback to display help in infoPane.
     public void mouseEntered(MouseEvent e) {
         if (pinnedTo != null) {
             return;
         }
         displayHelpForComponent((JComponent)e.getSource());
     }

    // these mouse events ignored.
     public void mouseClicked(MouseEvent e) {
     }
    public void mouseExited(MouseEvent e) {
    }
    public void mousePressed(MouseEvent e) {
    }
    public void mouseReleased(MouseEvent e) {
    }


// item listener interface - responds to 'pin' events.    
    public void itemStateChanged(ItemEvent e) {

        if (e.getStateChange() == ItemEvent.SELECTED) {
            pinnedTo = (JToggleButton)e.getSource();
            // make sure everything else is unselected.
            for(int i = 0; i < additionalPins.size(); i++) {
                JToggleButton tb = (JToggleButton)additionalPins.get(i);
                if (tb != pinnedTo && tb.isSelected()) {
                    tb.setSelected(false);                    
                }
            }
            for(int i = 0; i < paramPins.size(); i++) {
                JToggleButton tb = (JToggleButton)paramPins.get(i);
                if (tb != pinnedTo && tb.isSelected()) {
                    tb.setSelected(false);                    
                }            
            }    
            // now display help for this item.
            displayHelpForComponent(pinnedTo);
        } else if (e.getStateChange() == ItemEvent.DESELECTED
                && pinnedTo == e.getSource()) {
            pinnedTo = null;
        }
    }

    }
    
