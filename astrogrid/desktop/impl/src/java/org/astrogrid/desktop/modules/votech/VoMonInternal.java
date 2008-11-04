/**
 * 
 */
package org.astrogrid.desktop.modules.votech;

import java.net.URL;

import javax.swing.Icon;

import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.system.ScheduledTask;
import org.astrogrid.desktop.modules.util.Selftest;
import org.votech.VoMon;

/** Internal interface for {@link VoMon}.
 * @author Noel Winstanley
 * @since Dec 11, 20066:27:58 PM
 */
public interface VoMonInternal extends VoMon, ScheduledTask, Selftest {
    
    /** return html-formatted details about the availability of this resource
     * 
     * @param a rsouce r
     * @return
     */
    String getTooltipInformationFor(Resource r);
    
    /** returns an icon (one of the constants in UIConstants) to 
     * represent the current status of this resource
     * @param r
     * @return an icon for Service and CeaApplication resources. for other resource types will return null;
     */
    Icon suggestIconFor(Resource r);

    /**
     * @return the endpoint used to access the vomon service
     */
    URL getEndpoint();

}
