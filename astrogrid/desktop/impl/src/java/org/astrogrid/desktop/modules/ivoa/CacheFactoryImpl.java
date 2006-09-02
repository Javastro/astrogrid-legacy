/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.DiskStoreConfiguration;

import org.astrogrid.acr.builtin.ShutdownListener;

/** implementation ofo the data cache - uses the ehCache libraries,
 * @author Noel Winstanley
 * @since Aug 8, 20061:17:09 AM
 */
public class CacheFactoryImpl implements ShutdownListener, CacheFactory {

	private final CacheManager manager;
	
	public CacheFactoryImpl(String workingDir) {
		Configuration conf = new Configuration();
		DiskStoreConfiguration diskStore = new DiskStoreConfiguration();
		diskStore.setPath(workingDir);
		conf.addDiskStore(diskStore);
		
		// necessarty to create a defaults section.
		CacheConfiguration defaults = new CacheConfiguration();
		defaults.setName("cache_defaults");
		defaults.setMaxElementsInMemory(10000);
		defaults.setEternal(false);
		defaults.setTimeToIdleSeconds(120);
		defaults.setTimeToLiveSeconds(120);
		defaults.setOverflowToDisk(true);
		defaults.setDiskPersistent(false);
		defaults.setDiskExpiryThreadIntervalSeconds(120);
		defaults.setMemoryStoreEvictionPolicy("LRU");
		conf.setDefaultCacheConfiguration(defaults);
		
		
		// these cache's are mostly disk based - don't want to 
		// increate the memory load too much 
		// and even fetching from disk cache is much faster than 
		// querying reg.
		
		// cache for individual resource documents.
		
		// will always hold the 200 most recently used resources.s
		CacheConfiguration resources = new CacheConfiguration() {{
			setName(RESOURCES_CACHE);
			setMaxElementsInMemory(200); // 3-4 queries? will get flooded by astroscope in one go.
			setOverflowToDisk(true);
			setDiskPersistent(true);
			setTimeToLiveSeconds(7 * 24 * 60 * 60); // 7 days
			setTimeToIdleSeconds(3 * 24 * 60 * 60); // 3 days		
			setDiskExpiryThreadIntervalSeconds(60 * 60); //1 hour
		}};
		conf.addCache(resources);
		
		// cache for registry documents - resource docs, and xquery docs
		CacheConfiguration documents = new CacheConfiguration() {{
			setName(DOCUMENTS_CACHE); 
			setMaxElementsInMemory(1); // only keep a few in memory.
			setOverflowToDisk(true);
			setDiskPersistent(true);
			setTimeToLiveSeconds(7 * 24 * 60 * 60); // 7 days
			setTimeToIdleSeconds(3 * 24 * 60 * 60); // 3 days		
			setDiskExpiryThreadIntervalSeconds(60 * 60); //1 hour
		}};
		conf.addCache(documents);
		
		// cache for bulk searches, etc. of registry objects
		// not so sure about this one really..
		CacheConfiguration bulk = new CacheConfiguration() {{
			setName(BULK_CACHE); 
			setMaxElementsInMemory(1); // keep the smallest amount in memory.
			setOverflowToDisk(true);
			setDiskPersistent(true);
			setTimeToLiveSeconds(7 * 24 * 60 * 60); // 7 days
			setTimeToIdleSeconds(3 * 24 * 60 * 60); // 3 days		
			setDiskExpiryThreadIntervalSeconds(60 * 60); // 1 hour
		}};
		conf.addCache(bulk);
		
		//disk-cache of parameterized workfows.
		CacheConfiguration pw = new CacheConfiguration() {{
			setName(PW_CACHE);
			setMaxElementsInMemory(1);
			setOverflowToDisk(true);
			setDiskPersistent(true);
			setTimeToLiveSeconds(7 * 24 * 60 * 60); // 7 days
			setTimeToIdleSeconds(3 * 24 * 60 * 60); // 3 days		
			setDiskExpiryThreadIntervalSeconds(60 * 60); //1 hour
		}};
		conf.addCache(pw);
		
		manager = new CacheManager(conf);
	}
	
	
	public CacheManager getManager() {
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


	public void flush() {
		getManager().clearAll();
	}

}
