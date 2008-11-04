/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.DiskStoreConfiguration;

import org.apache.hivemind.Location;
import org.apache.hivemind.internal.Module;
import org.astrogrid.acr.builtin.ShutdownListener;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.ui.WorkerProgressReporter;

/** Implementation of the data cache - uses the <a href='http://ehcache.sourceforge.net/'>ehCache</a> libraries,
 * @author Noel Winstanley
 * @modified made the caching more conservative.
 * @since Aug 8, 20061:17:09 AM
 */
public class CacheFactoryImpl implements ShutdownListener, CacheFactoryInternal, PropertyChangeListener {

	private final CacheManager manager;
	private final List<String> registrySensitiveCacheNames = new ArrayList<String>();
	/**
	 * Construct a new cache factory
	 * @param workingDir directory to store caches in.
	 * @param caches list of cache configuraiton objets.
	 * @param endpointA a preference to listen to, and flush all registry-sensitve caches when it changes
	 * @param endpointB same as A.
	 */
	public CacheFactoryImpl(final String workingDir, final List caches, final Preference endpointA,final Preference endpointB) {
		final Configuration conf = new Configuration();
		final DiskStoreConfiguration diskStore = new DiskStoreConfiguration();
		diskStore.setPath(workingDir);
		conf.addDiskStore(diskStore);
		
		// necessarty to create a defaults section.
		final CacheConfiguration defaults = new CacheConfiguration();
		defaults.setName("cache_defaults");
		defaults.setMaxElementsInMemory(100);
		defaults.setEternal(false);
		defaults.setTimeToIdleSeconds(120);
		defaults.setTimeToLiveSeconds(120);
		defaults.setOverflowToDisk(true);
		defaults.setDiskPersistent(false);
		defaults.setDiskExpiryThreadIntervalSeconds(600);
		defaults.setMemoryStoreEvictionPolicy("LRU");
		conf.setDefaultCacheConfiguration(defaults);
		
		for (final Iterator i = caches.iterator(); i.hasNext();) {
			final ExtendedCacheConfiguration c = (ExtendedCacheConfiguration) i.next();
			conf.addCache(c);
			if (c.isRegistrySensitive()) {
			    registrySensitiveCacheNames.add(c.getName());
			}
		}

		manager = new CacheManager(conf);
		// listen for changes to these preferences.
		endpointA.addPropertyChangeListener(this);
		endpointB.addPropertyChangeListener(this);
	}
	
	
	protected CacheManager getManager() {
		return manager;
	}
	
	public void halting() {
		manager.shutdown();
	}

	public String lastChance() {
		return null;
	}


	public void flush() { 
	    getManager().clearAll();
	}

	/** returns a matching ehcache instance, or null if not found */
	public Object provideObject(final Module module, final Class expectedType, final String cacheName, final Location loc) {
		final Ehcache c =  getManager().getEhcache(cacheName);
		if (c == null) {
			throw new IllegalArgumentException("Unknown cache '" 
					+ cacheName + "' at " + module.getModuleId() + ", " + loc.getLineNumber());
					
		}
		return c;
	}

	// listen for changes to preferences - and if one heard, flush the cache.
    public void propertyChange(final PropertyChangeEvent evt) {
        for (final Iterator<String> i = registrySensitiveCacheNames.iterator(); i.hasNext(); ) {
            final Cache cache = manager.getCache(i.next());
            cache.removeAll();
        }       
    }

// scheduledTask interface. used to perform cache maintenance.
    
    public void execute(final WorkerProgressReporter reporter) {
        final String[] cacheNames = manager.getCacheNames();
        for (final String name : cacheNames) {
            final Cache cache = manager.getCache(name);
            if (! cache.getCacheConfiguration().isEternal()) {
                cache.evictExpiredElements();
            }
        }
    }


    public String getName() {
        return "Cache Maintenance";
    }


    public long getPeriod() {
        return 2 * 1000 * 60 * 60; // 2-hourly.
    }


    public Principal getPrincipal() {
        return null; // use the default session
    }




}
