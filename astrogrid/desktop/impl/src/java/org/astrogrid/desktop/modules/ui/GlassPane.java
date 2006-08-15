/*$Id: GlassPane.java,v 1.4 2006/08/15 10:09:24 nw Exp $
 * Created on 14-Apr-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ui;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;

import javax.swing.JComponent;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This is a glass pane class that intercepts screen interactions during system busy states.
 *
 * @author Yexin Chen
 * @todo verify that this is being used correctly from uicomponent.
 */
public class GlassPane extends JComponent implements AWTEventListener
{
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(GlassPane.class);

    private Window theWindow;
    private Component activeComponent;
/**
 * GlassPane constructor comment.
 * @param Container a 
 */
protected GlassPane(Component activeComponent)
{
    // add adapters that do nothing for keyboard and mouse actions
    addMouseListener(new MouseAdapter()
    {
    });
    
    addKeyListener(new KeyAdapter()
    {
    });
    
    setActiveComponent(activeComponent);
}
/**
 * Receives all key events in the AWT and processes the ones that originated from the
 * current window with the glass pane.
 *
 * @param event the AWTEvent that was fired
 */
public void eventDispatched(AWTEvent event)
{
    Object source = event.getSource();
    
    // discard the event if its source is not from the correct type
    boolean sourceIsComponent = (event.getSource() instanceof Component);

    if ((event instanceof KeyEvent) && sourceIsComponent)
    {
        // If the event originated from the window w/glass pane, consume the event
        if ((SwingUtilities.windowForComponent((Component) source) == theWindow))
        {
            ((KeyEvent) event).consume();
        }
    }
}
/**
 * Finds the glass pane that is related to the specified component.
 * 
 * @param startComponent the component used to start the search for the glass pane
 * @param create a flag whether to create a glass pane if one does not exist
 * @return GlassPane
 */
public synchronized static GlassPane mount(Component startComponent, boolean create)
{
    RootPaneContainer aContainer = null;
    Component aComponent = startComponent;

    // Climb the component hierarchy until a RootPaneContainer is found or until the very top
    while ((aComponent.getParent() != null) && !(aComponent instanceof RootPaneContainer))
    {
        aComponent = (Component) aComponent.getParent();
    }

    // Guard against error conditions if climb search wasn't successful
    if (aComponent instanceof RootPaneContainer)
    {
        aContainer = (RootPaneContainer) aComponent;
    }

    if (aContainer != null)
    {
        // Retrieve an existing GlassPane if old one already exist or create a new one, otherwise return null
        if ((aContainer.getGlassPane() != null) && (aContainer.getGlassPane() instanceof GlassPane))
        {
            return (GlassPane) aContainer.getGlassPane();
        }
        else if (create)
        {
            GlassPane aGlassPane = new GlassPane(startComponent);
            aContainer.setGlassPane(aGlassPane);

            logger.info("GlassPane mounted on " + aContainer.getClass());
            return aGlassPane;
        }
        else
        {
            return null;
        }
    }
    else
    {
        return null;
    }
}
/**
 * Set the component that ordered-up the glass pane.
 *
 * @param aComponent the UI component that asked for the glass pane
 */
private void setActiveComponent(Component aComponent)
{
    activeComponent = aComponent;
}
/**
 * Sets the glass pane as visible or invisible. The mouse cursor will be set accordingly.
 */
public void setVisible(boolean value)
{
    if (value)
    {
        // keep track of the visible window associated w/the component
        // useful during event filtering
        if (theWindow == null)
        {
            theWindow = SwingUtilities.windowForComponent(activeComponent);
            if (theWindow == null)
            {
                if (activeComponent instanceof Window)
                {
                    theWindow = (Window) activeComponent;
                }
            }
        }

        // Sets the mouse cursor to hourglass mode
        getTopLevelAncestor().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
        activeComponent = theWindow.getFocusOwner();

        // Start receiving all events and consume them if necessary
        Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.KEY_EVENT_MASK);
        
        this.requestFocus();

        // Activate the glass pane capabilities
        super.setVisible(value);
    }
    else
    {
        // Stop receiving all events
        Toolkit.getDefaultToolkit().removeAWTEventListener(this);

        // Deactivate the glass pane capabilities
        super.setVisible(value);

        // Sets the mouse cursor back to the regular pointer
        if (getTopLevelAncestor() != null)
        {
            getTopLevelAncestor().setCursor(null);
        }
    }
}
}

/* 
$Log: GlassPane.java,v $
Revision 1.4  2006/08/15 10:09:24  nw
organized imports

Revision 1.3  2006/06/27 10:34:27  nw
logging fix.

Revision 1.2  2005/09/02 14:03:34  nw
javadocs for impl

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.3  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.2  2005/04/27 13:42:40  clq2
1082

Revision 1.1.2.1  2005/04/15 13:00:45  nw
got vospace browser working.
 
*/