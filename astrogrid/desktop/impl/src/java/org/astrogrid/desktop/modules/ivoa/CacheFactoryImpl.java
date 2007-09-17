/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

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
import org.apache.hivemind.service.ObjectProvider;
import org.astrogrid.acr.builtin.ShutdownListener;
import org.astrogrid.acr.ivoa.CacheFactory;

/** implementation ofo the data cache - uses the ehCache libraries,
 * @author Noel Winstanley
 * @modified made the caching more conservative.
 * @since Aug 8, 20061:17:09 AM
 */
public class CacheFactoryImpl implements ShutdownListener, CacheFactory, ObjectProvider {

	private final CacheManager manager;
	
	public CacheFactoryImpl(String workingDir, List caches) {
		Configuration conf = new Configuration();
		DiskStoreConfiguration diskStore = new DiskStoreConfiguration();
		diskStore.setPath(workingDir);
		conf.addDiskStore(diskStore);
		
		// necessarty to create a defaults section.
		CacheConfiguration defaults = new CacheConfiguration();
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
		
		for (Iterator i = caches.iterator(); i.hasNext();) {
			CacheConfiguration c = (CacheConfiguration) i.next();
			conf.addCache(c);
		}

		manager = new CacheManager(conf);
	}
	
	
	protected CacheManager getManager() {
		return manager;
	}
	
	public void halting() {
//		List keys = manager.getCache(RESOURCES_CACHE).getKeys();
//		System.out.println(keys.size());
//		Set s = new TreeSet(keys);
//		System.out.println(s.size());
//		for (Iterator i = s.iterator(); i.hasNext(); ) {
//			System.out.println(i.next());
//		}
		manager.shutdown();
	}

	public String lastChance() {
		return null;
	}


	public void flush() { // don't flush eternal caches.
		String[] cacheNames = manager.getCacheNames();
		for (int i = 0; i < cacheNames.length; i++) {
            Cache cache = manager.getCache(cacheNames[i]);
            if (! cache.isEternal()) {
                cache.removeAll();
            }
        }
	}

	/** returns a matching ehcache instance, or null if not found */
	public Object provideObject(Module module, Class expectedType, String cacheName, Location loc) {
		Ehcache c =  getManager().getEhcache(cacheName);
		if (c == null) {
			throw new IllegalArgumentException("Unknown cache '" 
					+ cacheName + "' at " + module.getModuleId() + ", " + loc.getLineNumber());
					
		}
		return c;
	}




}
