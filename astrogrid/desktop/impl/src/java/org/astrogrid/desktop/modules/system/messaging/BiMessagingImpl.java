/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

import javax.swing.JMenu;
import javax.swing.JSeparator;

import org.astrogrid.desktop.modules.system.SchedulerInternal.DelayedContinuation;

/** Implementation of messaging that combines both plastic and samp.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 25, 200910:29:07 AM
 */
public class BiMessagingImpl extends PlasticOnlyMessagingImpl {

    private final Samp samp;

    /**
     * @param tupp
     */
    public BiMessagingImpl(final Samp samp, final TupperwareInternal tupp) {
        super(tupp);
        this.samp = samp; 
        
    }
    
    
    @Override
    public void populateInteropMenu(final JMenu interopMenu) {
        interopMenu.add(samp.connectAction());
        interopMenu.add(samp.disconnectAction());
        interopMenu.add(samp.startInternalHubAction());
        interopMenu.add(samp.showMonitorAction());
        interopMenu.add(new JSeparator());
        super.populateInteropMenu(interopMenu);
    }
    
    @Override
    public DelayedContinuation execute() {
        //@todo dirty hack - dunno if this is correct.
        // think where to place this.
        // should be - if not already connected? connect, if fail, start intrnal, connect again.
   //     samp.connectAction().actionPerformed(new ActionEvent(this,0,(String)samp.connectAction().getValue(Action.ACTION_COMMAND_KEY))); // can't pass null in here.

        return super.execute();
    }

}
