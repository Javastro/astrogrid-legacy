/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

import javax.swing.JMenu;
import javax.swing.JSeparator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.desktop.SplashWindow;
import org.astrogrid.desktop.modules.system.SchedulerInternal.DelayedContinuation;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.samp.client.HubConnector;
import org.astrogrid.samp.client.SampException;

import uk.ac.starlink.plastic.PlasticUtils;

/** Implementation of messaging that combines both plastic and samp.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 25, 200910:29:07 AM
 */
public class BiMessagingImpl extends PlasticOnlyMessagingImpl {
    private static final Log logger = LogFactory.getLog(BiMessagingImpl.class);
    private final Samp samp;
    private final Preference startSamp;
    private final Preference startPlastic;

    /**
     * @param tupp
     */
    public BiMessagingImpl(final Preference startSamp, final Preference startPlastic,final Samp samp, final TupperwareInternal tupp) {
        super(tupp);
        this.startSamp = startSamp;
        this.startPlastic = startPlastic;
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
        SplashWindow.reportProgress("Starting messaging...");
        // try to connect.
        final HubConnector connector = samp.getConnector();
        try {
            connector.getConnection();
        } catch (final SampException x) {
            logger.warn("SampException when attempting to connect to hub",x);
        }
        // set it to auto-connecto from now on.
        connector.setAutoconnect(10);
        if (!connector.isConnected() && startSamp.asBoolean()) {
            samp.startInternalHubAction().actionPerformed(null);
            // will auto-connect in a mo.
        }
        
        if (startPlastic.asBoolean()&& ! PlasticUtils.isHubRunning()) {
            // this action starts internal hub _and_ connects to it.
                tupperware.startInternalHubAction().actionPerformed(null);            
        } else if (PlasticUtils.isHubRunning()) {
            tupperware.connectAction().actionPerformed(null);            
        }
        return null;
    }

}
