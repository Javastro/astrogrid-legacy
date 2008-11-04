package org.astrogrid.desktop.modules.ui.scope;

import javax.swing.Icon;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.ivoa.resource.Capability;
import org.astrogrid.acr.ivoa.resource.ConeCapability;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.acr.ivoa.resource.SiapCapability;
import org.astrogrid.acr.ivoa.resource.SsapCapability;
import org.astrogrid.acr.ivoa.resource.StapCapability;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ui.voexplorer.google.CapabilityIconFactory;

/**
 * {@code CapabilityIconFactory} implementation suitable for use with a
 * {@link ScopeServicesList}.  This is expecting that every resource in the
 * table is a {@link RetrieverService}, and has a single Capability.
 * Only those capabilities of interest to the AstroScope are expected.
 *
 * @author  Mark Taylor
 * @since  21 Feb 2008
 */
public class SingleCapabilityIconFactory implements CapabilityIconFactory {

    /** Set known capability descriptions. */
    private static final Cap[] CAPS = new Cap[] {
        new Cap("Catalog cone search service", "cone16.png", ConeCapability.class), 
        new Cap("Time range access service (STAP)", "latest16.png", StapCapability.class),
        new Cap("Spectrum access service (SSAP)", "ssap16.png", SsapCapability.class),
        new Cap("Image access service (SIAP)", "siap16.png", SiapCapability.class),
    };
    private static final Log logger = LogFactory.getLog(SingleCapabilityIconFactory.class);

    public SingleCapabilityIconFactory() {
    }

    public Icon buildIcon(final Resource res) {
        if (res instanceof Service) {
            final Capability[] caps = ((Service) res).getCapabilities();
            if (caps.length != 1 && ! (res instanceof RetrieverService)) {
                logger.warn("Unexpected service - programming error?");
                return null;
            }
            else {
                final Capability cap = caps[0];
                for (int i = 0; i < CAPS.length; i++) {
                    if (CAPS[i].clazz.isAssignableFrom(cap.getClass())) {
                        return CAPS[i].icon;
                    }
                }
                return IconHelper.loadIcon("unknown_thing16.png");
            }
        }
        else {
            return null;
        }
    }

    public String getTooltip(final Icon icon) {
        for (int i = 0; i < CAPS.length; i++) {
            if (CAPS[i].icon.equals(icon)) {
                return CAPS[i].tip;
            }
        }
        return null;
    }

    /** Defines the characteristics for each known capability. */
    private static class Cap {
        final String tip;
        final Icon icon;
        final Class clazz;

        /** Constructor.
         * @param  tip   tooltip
         * @param  iconName  name of icon resource
         * @param  Capability subclass characterising this capability
         */
        Cap(final String tip, final String iconName, final Class clazz) {
            this.tip = tip;
            this.icon = IconHelper.loadIcon(iconName);
            this.clazz = clazz;
        }
    }
}
