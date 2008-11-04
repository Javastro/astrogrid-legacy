/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa.resource;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.astrogrid.acr.ivoa.VosiAvailabilityBean;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.system.ProgrammerError;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.comp.ExceptionFormatter;
import org.astrogrid.desktop.modules.ui.comp.ResourceDisplayPane;

/** HTML-Button that allows user to test VOSI availability.
 * 
 * @todo - unused. should remove at some point.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 26, 200810:30:51 PM
 */
public class TestAvailabilityButton extends ResourceDisplayPaneEmbeddedButton implements ActionListener {

    /**
     * 
     */
    public TestAvailabilityButton() {
        setText("Check Service");
        setIcon(IconHelper.loadIcon("unknown_thing16.png"));        
        setToolTipText("Check the availability of the service to verify that it is functioning");
        addActionListener(this);
        CSH.setHelpIDString(this,"reg.test.availability");        
    }

    public void actionPerformed(final ActionEvent e) {
        final ResourceDisplayPane displayPane = getResourceDisplayPane(e);
        final Resource r = displayPane.getCurrentResource();
        if (!(r instanceof Service)) {
            throw new ProgrammerError("Expected a service");                
        }
        final UIComponent uiParent = getUIComponent(e);
        setText("Checking");
        setIcon(IconHelper.loadIcon("loader.gif"));        
        (new BackgroundWorker(uiParent,"Checking availability of " + r.getTitle()) {

            protected Object construct() throws Exception {
                return displayPane.getAvailabilityTester().checkAvailability(r.getId());                
            }
            protected void doFinished(final Object result) {
                final VosiAvailabilityBean b = (VosiAvailabilityBean)result;
                if (b.isAvailable()) {
                    setIcon(IconHelper.loadIcon("tick16.png"));
                    if (b.getDownAt() != null) {
                        setText("OK until " + b.getDownAt());
                    } else {
                        setText("Service OK");
                    }
                    
                } else {
                    setIcon(IconHelper.loadIcon("no16.png"));                        
                    if (b.getBackAt() != null) {
                        setText("Unavailable until " + b.getBackAt());
                    } else {
                        setText("Service Unavailable");
                    }
                }
                final StringBuilder sb = new StringBuilder("<html>");
                if (b.getUpSince()!= null) {
                    sb.append("Up Since: " +b.getUpSince());
                }
                if (b.getNotes() != null) {
                    final String[] notes = b.getNotes();
                    for (int i = 0; i < notes.length; i++) {
                        sb.append("<p>")
                            .append(notes[i])
                            .append("</p>");
                    }                 
                }
                
                setToolTipText(sb.toString());
            }
            protected void doError(final Throwable ex) {
                setText("Check Failed");
                setIcon(IconHelper.loadIcon("no16.png"));    
                setToolTipText(ExceptionFormatter.formatException(ex));
            }
        }).start();
    }
    }
