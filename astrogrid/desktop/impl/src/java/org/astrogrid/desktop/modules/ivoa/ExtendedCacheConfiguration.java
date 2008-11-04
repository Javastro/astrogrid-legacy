/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import net.sf.ehcache.config.CacheConfiguration;

/**
 * Cache configuration that has additional attributes specific to AR.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 7, 20082:26:09 PM
 */
public class ExtendedCacheConfiguration extends CacheConfiguration {
    
    private boolean registrySensitive ;

    /** determine whether this cache is sensitive to changes to the registry
     * i.e. if the registry endpoint changes, should this cache be cleared.
     * @return
     */
    public final boolean isRegistrySensitive() {
        return this.registrySensitive;
    }

    public final void setRegistrySensitive(final boolean registrySensitive) {
        this.registrySensitive = registrySensitive;
    }

}
