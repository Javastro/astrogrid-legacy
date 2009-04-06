/**
 * 
 */
package org.astrogrid.desktop.modules.ui.taskrunner;

import org.astrogrid.acr.ivoa.resource.TapService;
import org.astrogrid.workflow.beans.v1.Tool;

/** Taskrunner tweaks specific to TAP.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Oct 22, 20083:46:23 PM
 */
public class TapTweaks implements ProtocolSpecificTweaks {

    private final TapService r;
    private final TapCeaApplication cea; // makes tap appear like cea.

    /**
     * @param r
     */
    public TapTweaks(final TapService r) {
        this.r = r;
        cea = new TapCeaApplication(r);
    }

    public void buildForm(final TaskRunnerImpl tr) {
        tr.new VosiServiceWorker(r).start(); // populate the services list (and check for availability)
 
        tr.pForm.buildForm(cea); // only one interface at the moment.
        tr.showADQLOnly(true);
    }

    public void buildForm(final Tool t,final TaskRunnerImpl tr) {
        tr.new VosiServiceWorker(r).start(); // populate the services list (and check for availability)
        
        tr.pForm.buildForm(t,t.getInterface(),cea);
        tr.showADQLOnly(true);
    }
    



}
