/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import javax.swing.Icon;

import org.astrogrid.acr.ivoa.Vosi;
import org.astrogrid.acr.ivoa.VosiAvailabilityBean;

/** Internal interface to the vosi component
 * provides some additional formatting functions.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 6, 20096:27:25 PM
 */
public interface VosiInternal extends Vosi {

    /** format information about this availability bean */
    String makeTooltipFor(VosiAvailabilityBean bean);
    
    /** return an icon to suggest the availability of a service
     * 
     * @param bean
     * @return an icon. if {@code bean} is null, will return a 'not known' icon
     */
    Icon suggestIconFor(VosiAvailabilityBean bean);
}
