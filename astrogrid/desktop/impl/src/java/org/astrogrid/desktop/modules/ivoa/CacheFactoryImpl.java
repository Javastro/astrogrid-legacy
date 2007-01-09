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
 * @modified made the caching more conservative.
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
		defaults.setMaxElementsInMemory(100);
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
		
		CacheConfiguration resources = new CacheConfiguration() {{
			setName(RESOURCES_CACHE);
			setMaxElementsInMemory(50);
			setMaxElementsOnDisk(300);
			setOverflowToDisk(true);
			setDiskPersistent(true);
			setTimeToLiveSeconds(7 * 24 * 60 * 60); // 7 days
			setTimeToIdleSeconds(3 * 24 * 60 * 60); // 3 days		
			setDiskExpiryThreadIntervalSeconds(10 * 60); // ten minutes
			
		}};
		conf.addCache(resources);
		
		// cache for registry documents - resource docs, and xquery docs
		CacheConfiguration documents = new CacheConfiguration() {{
			setName(DOCUMENTS_CACHE); 
			setMaxElementsInMemory(3); // enough for up one, down on in registry google.
			setMaxElementsOnDisk(20); // not used very often.
			setOverflowToDisk(true);
			setDiskPersistent(true);
			setTimeToLiveSeconds(7 * 24 * 60 * 60); // 7 days
			setTimeToIdleSeconds(3 * 24 * 60 * 60); // 3 days		
			setDiskExpiryThreadIntervalSeconds(10 * 60); //ten minutes
		}};
		conf.addCache(documents);
	
		// small cache of resources that have been treated as applications.
		// not entirely happy about thios one - will do for now.
		// makes workflow builder much more responsive.
		CacheConfiguration apps = new CacheConfiguration() {{
			setName(APPLICATION_RESOURCES_CACHE);
			setMaxElementsInMemory(20);
			setMaxElementsOnDisk(100);
			setOverflowToDisk(true);
			setDiskPersistent(true);
			setTimeToLiveSeconds(7 * 24 * 60 * 60); // 7 days
			setTimeToIdleSeconds(3 * 24 * 60 * 60); // 3 days		
			setDiskExpiryThreadIntervalSeconds(10 * 60); //ten minutes
		}};
		conf.addCache(apps);
		
		// cache for bulk searches, etc. of registry objects
		// not so sure about this one really..
		CacheConfiguration bulk = new CacheConfiguration() {{
			setName(BULK_CACHE); 
			setMaxElementsInMemory(1); // keep the smallest amount in memory.
			setMaxElementsOnDisk(10); // enough to keep track of the astroscope queries, etc.
			setOverflowToDisk(true);
			setDiskPersistent(true);
			setTimeToLiveSeconds(7 * 24 * 60 * 60); // 7 days
			setTimeToIdleSeconds(3 * 24 * 60 * 60); // 3 days		
			setDiskExpiryThreadIntervalSeconds(10 * 60); // ten minutes
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
			setDiskExpiryThreadIntervalSeconds(10 * 60); //ten minutes
		}};
		conf.addCache(pw);
	
		// cache of service status
		CacheConfiguration vomon = new CacheConfiguration() {{
			setName(VOMON_CACHE);
			setMaxElementsInMemory(100);
			setMaxElementsOnDisk(10000); // want to provide 'unlimited' storage for the entire document
			setOverflowToDisk(true);
			setDiskPersistent(false);  
			setTimeToLiveSeconds(2 * 60 * 60) ; // 2 hours - vomonImpl reloads every hour.
			setTimeToIdleSeconds(2 * 60 * 60); // 2 hours
			setDiskExpiryThreadIntervalSeconds(10 * 60); // ten minutes
		}};
		conf.addCache(vomon);
		
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
