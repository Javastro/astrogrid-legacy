/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.system.SystemTray;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.system.ui.UIContext;

/** Creates the most appropriate system tray implementation, and then delegates all methods to it.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Aug 23, 20077:56:32 PM
 */
public class BestSuitableSystemTray implements SystemTrayInternal {
    protected static final Log logger = LogFactory.getLog(SystemTray.class);            

    /**
     * @param cxt 
     * @param shut 
     * @param conf 
     * 
     */
    public BestSuitableSystemTray(final UIContext cxt, final Preference pref) {
        // first try to construct a java6 systray.
        SystemTrayInternal best = null;
        try {
            best = new Java6SystemTray(cxt, pref);
        } catch (final Throwable e) {            
            // fallback then.
            logger.info("Failed to create system tray - falling back");
            best = new FallbackSystemTray(cxt);
        }
        
        theTray = best;
    }
    
    private final SystemTrayInternal theTray;
//delegate methods
    public void displayErrorMessage(final String arg0, final String arg1) {
        this.theTray.displayErrorMessage(arg0, arg1);
    }

    public void displayInfoMessage(final String arg0, final String arg1) {
        this.theTray.displayInfoMessage(arg0, arg1);
    }

    public void displayWarningMessage(final String arg0, final String arg1) {
        this.theTray.displayWarningMessage(arg0, arg1);
    }

    public void run() {
        this.theTray.run();
    }

    public void startThrobbing() {
        this.theTray.startThrobbing();
    }

    public void stopThrobbing() {
        this.theTray.stopThrobbing();
    } 
   

}
