/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa.resource;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ivoa.VosiAvailabilityBean;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.comp.ResourceDisplayPane;

/** Button that allows user to test availability.
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

    public void actionPerformed(ActionEvent e) {
        final ResourceDisplayPane displayPane = getResourceDisplayPane(e);
        final Resource r = displayPane.getCurrentResource();
        if (!(r instanceof Service)) {
            throw new RuntimeException("Programming error - expected a service");                
        }
        final UIComponent uiParent = getUIComponent(e);
        setText("Checking");
        setIcon(IconHelper.loadIcon("loader.gif"));        
        (new BackgroundWorker(uiParent,"Checking service availability") {

            protected Object construct() throws Exception {
                return displayPane.getAvailabilityTester().getAvailability((Service)r);                
            }
            protected void doFinished(Object result) {
                VosiAvailabilityBean b = (VosiAvailabilityBean)result;
                if (b.isAvailable()) {
                    if (b.getValidTill() != null) {
                        setText("OK until " + b.getValidTill());
                    } else {
                        setText("Service OK");
                    }
                    setIcon(IconHelper.loadIcon("tick16.png"));
                    if (b.getUptime() > 0) {
                        setToolTipText("Uptime: " + b.getUptime() + "s");
                    }
                    
                } else {
                    setText("Service Unavailable");
                    setIcon(IconHelper.loadIcon("no16.png"));                        
                }
            }
            protected void doError(Throwable ex) {
                setText("Check Failed");
                setIcon(IconHelper.loadIcon("no16.png"));                      
                super.doError(ex);
            }
        }).start();
    }
}
