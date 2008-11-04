/**
 * 
 */
package org.astrogrid.desktop.modules.ui.taskrunner;

import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.desktop.modules.ui.QueryBuilderImpl;
import org.astrogrid.desktop.modules.ui.actions.BuildQueryActivity;
import org.astrogrid.workflow.beans.v1.Tool;

/** Taskrunner tweaks specific to CEA.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Oct 22, 20083:45:11 PM
 */
public class CeaTweaks implements ProtocolSpecificTweaks {

    private final CeaApplication cea;

    /**
     * @param r
     */
    public CeaTweaks(final CeaApplication r) {
        this.cea = r;
    }

    public void buildForm(final TaskRunnerImpl tr) {
        final boolean isQB = tr instanceof QueryBuilderImpl;
        (tr.new ListServicesWorker(cea.getId())).start();
        String name;
        if (isQB) { 
            name = BuildQueryActivity.findNameOfFirstADQLInterface(cea);
        } else {
            name = BuildQueryActivity.findNameOfFirstNonADQLInterface(cea);
        }
        if (name != null) {
            tr.pForm.buildForm(name,cea);
            if (isQB) {
                tr.pForm.setExpanded(true);
            }
        } else { // show what we've got then
            tr.pForm.buildForm(cea);
        }  
        tr.showADQLOnly(false);
    }

    public void buildForm(final Tool t,  final TaskRunnerImpl tr) {
        (tr.new ListServicesWorker(cea.getId())).start();
        tr.pForm.buildForm(t,t.getInterface(),cea);
        tr.showADQLOnly(false);
    }


}
