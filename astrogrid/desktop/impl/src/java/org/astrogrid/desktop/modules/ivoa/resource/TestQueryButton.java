/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa.resource;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.astrogrid.acr.ivoa.resource.Capability;
import org.astrogrid.acr.ivoa.resource.ConeCapability;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.acr.ivoa.resource.SsapCapability;
import org.astrogrid.acr.ivoa.resource.SiapCapability;
import org.astrogrid.acr.ivoa.resource.StapCapability;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.comp.ResourceDisplayPane;

/** static button class that performs a test query.
 * 
 *  design is constrained by the requirements of being inserted into the 
 *  resource display via an 'object' html tag - hence has to get 
 *  whatever it needs though parameters, or context.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 19, 20082:50:01 PM
 */
public class TestQueryButton extends ResourceDisplayPaneEmbeddedButton implements ActionListener {
    /**
     * 
     */
    public TestQueryButton() {
        setText("Test Query");
        setIcon(IconHelper.loadIcon("unknown_thing16.png"));
        setToolTipText("Run the test query to verify that this service is functioning correctly");
        addActionListener(this);
        CSH.setHelpIDString(this,"reg.test.query");        
    }
    
    public void actionPerformed(ActionEvent e) {
            final ResourceDisplayPane displayPane = getResourceDisplayPane(e);
            Resource r = displayPane.getCurrentResource();
            if (!(r instanceof Service)) {
                throw new RuntimeException("Programming error - expected a service");                
            }
            final Capability[] capabilities = ((Service)r).getCapabilities();
            if (capabilityIndex >= capabilities.length) {
                throw new RuntimeException("Programming error - capability index out of range");
            }
            final Capability cap = capabilities[capabilityIndex];
            final UIComponent uiParent = getUIComponent(e);
            setText("Testing");
            setIcon(IconHelper.loadIcon("loader.gif"));
            (new BackgroundWorker(uiParent,"Running test query") {

                protected Object construct() throws Exception {
                    boolean b = displayPane.getCapabilityTester().testCapability(cap);
                    return Boolean.valueOf(b);
                    
                }
                protected void doFinished(Object result) {
                    if (((Boolean)result).booleanValue()) {
                        setText("Tested OK");
                        setIcon(IconHelper.loadIcon("tick16.png"));
                    } else {
                        setText("Test Failed");
                        setIcon(IconHelper.loadIcon("no16.png"));                        
                    }
                }
                protected void doError(Throwable ex) {
                    setText("Test Failed");
                    setIcon(IconHelper.loadIcon("no16.png"));                      
                    super.doError(ex);
                }
                
            }).start();
    }
    
    private int capabilityIndex;


    public final void setCapabilityIndex(String capabilityIndex) {
        this.capabilityIndex = Integer.parseInt(capabilityIndex);
    }
    

}
