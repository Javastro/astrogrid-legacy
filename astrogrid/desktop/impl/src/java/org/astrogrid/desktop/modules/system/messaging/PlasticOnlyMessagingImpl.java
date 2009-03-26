/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

import java.security.Principal;

import javax.swing.JMenu;
import javax.swing.JSeparator;

import org.astrogrid.desktop.modules.system.SchedulerInternal.DelayedContinuation;
import org.joda.time.Duration;

import uk.ac.starlink.plastic.PlasticUtils;
import ca.odell.glazedlists.CompositeList;

/** Implementation of messaging that only supports plastic.
 * Relies on tupperwareImpl being around to do the heavy lifting.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 13, 20096:52:11 PM
 */
public class PlasticOnlyMessagingImpl implements Messaging {
    protected final TupperwareInternal tupperware;

    /**
     */
    public PlasticOnlyMessagingImpl(final TupperwareInternal tupp) {
        super();
        this.tupperware = tupp;
    }

    private final CompositeList<ExternalMessageTarget> targetList = new CompositeList<ExternalMessageTarget>();
    
    public CompositeList<ExternalMessageTarget> getTargetList() {
        return targetList;
    }
    
    public void populateInteropMenu(final JMenu interopMenu) {
        interopMenu.add(tupperware.connectAction());
        interopMenu.add(tupperware.disconnectAction());
        interopMenu.add(new JSeparator());
        interopMenu.add(tupperware.startInternalHubAction());
    }

    // scheduled task - run once on startup.


    public DelayedContinuation execute() {
        // connect to hubs, starting them as needed.
        if (PlasticUtils.isHubRunning()) {
            tupperware.connectAction().actionPerformed(null);
        } else {
            tupperware.startInternalHubAction().actionPerformed(null);
        }        
        return null;
    }

    public Duration getDelay() {
        return Duration.ZERO; //ASAP
    }

    public String getTitle() {
        return "Starting Messaging Subsystem";       
    }

    public Principal getPrincipal() {
        return null; // delibeaately null;
    }

}