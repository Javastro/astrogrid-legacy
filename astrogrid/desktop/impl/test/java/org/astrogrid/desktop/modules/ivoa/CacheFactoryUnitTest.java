/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import java.util.ArrayList;
import java.util.Collections;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.Status;

import org.apache.hivemind.Location;
import org.apache.hivemind.impl.LocationImpl;
import org.apache.hivemind.impl.ModuleImpl;
import org.apache.hivemind.internal.Module;
import org.astrogrid.desktop.modules.system.pref.Preference;

import junit.framework.TestCase;


import static org.astrogrid.Fixture.*;
/** Unit test for the cache factory.
 * tests the basic features of the cache system, rather than the configuration of caches
 * used within vodesktop.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 18, 200810:11:47 AM
 */
public class CacheFactoryUnitTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        a = new Preference();
        b = new Preference();
        basedir = createTempDir(this.getClass()).getAbsolutePath();
    }

    Preference a;
    Preference b;
    CacheFactoryImpl cf;
    String basedir;
    
    Module m = new ModuleImpl();
    Location l = new LocationImpl(null);
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        cf = null;
    }

    
    public void testEmptyCaches() throws Exception {
        cf = new CacheFactoryImpl(basedir,Collections.EMPTY_LIST,a,b);
        CacheManager manager = cf.getManager();
        assertNotNull(manager);
        assertEquals(Status.STATUS_ALIVE,manager.getStatus());
        assertEquals(0,manager.getCacheNames().length);
        
        cf.flush();
        assertNull(cf.lastChance()); // never objects to shutdown.
        cf.halting();
        
        assertEquals(Status.STATUS_SHUTDOWN,manager.getStatus());
    }
    
    public void testSingleCache() throws Exception {
        ExtendedCacheConfiguration conf = new ExtendedCacheConfiguration();
        conf.setName("testcache");
        conf.setMaxElementsInMemory(10);
        
        cf = new CacheFactoryImpl(basedir,Collections.singletonList(conf),a,b);
        CacheManager manager = cf.getManager();
        assertNotNull(manager);
        assertEquals(Status.STATUS_ALIVE,manager.getStatus());
        assertEquals(1,manager.getCacheNames().length);
        
        Cache cache = manager.getCache(conf.getName());
        assertNotNull(cache);
        assertSame(cache,cf.provideObject(m,null,conf.getName(),l));
        
        // try putting some stuff in it - this is mostly just checking hte library itself.
        assertEquals(0,cache.getSize());
        Element e = new Element("k","v");
        cache.put(e);
        assertEquals(1,cache.getSize());
        Element e1 = cache.get("k");
        assertNotNull(e1);
        assertSame(e,e1);
        assertNull(cf.lastChance()); // never objects to shutdown.        
        cf.halting();
        
        assertEquals(Status.STATUS_SHUTDOWN,manager.getStatus());
    }
    
    
    public void testRegistrySensitiveCaches() throws Exception {
        ExtendedCacheConfiguration cacheConfig = new ExtendedCacheConfiguration();
        cacheConfig.setName("testcache");
        cacheConfig.setMaxElementsInMemory(10);

        ExtendedCacheConfiguration sensitiveCacheConfig = new ExtendedCacheConfiguration();
        sensitiveCacheConfig.setName("sensitive");
        sensitiveCacheConfig.setRegistrySensitive(true);
        sensitiveCacheConfig.setMaxElementsInMemory(10);
        
        java.util.List configs = new ArrayList();
        configs.add(cacheConfig);
        configs.add(sensitiveCacheConfig);
        
        cf = new CacheFactoryImpl(basedir,configs,a,b);
        CacheManager manager = cf.getManager();
        assertNotNull(manager);
        assertEquals(Status.STATUS_ALIVE,manager.getStatus());
        assertEquals(2,manager.getCacheNames().length);
        
        Cache cache = manager.getCache(cacheConfig.getName());
        assertNotNull(cache);
        // populate this one.
        Element e = new Element("k","v");
        cache.put(e);
        assertEquals(1,cache.getSize());
        
        // do same with the sensitive one.
        Cache sensitive = manager.getCache(sensitiveCacheConfig.getName());
        assertNotNull(sensitive);
        Element e1 = new Element(1,2);
        sensitive.put(e1);
        assertEquals(1,sensitive.getSize());
        
        
        // now triger a preference change
        a.setValue("a new value");
        // expect the sensitive cache to be cleared, the other not.
        assertEquals(1,cache.getSize());        
        assertEquals(0,sensitive.getSize());
        assertNull(cf.lastChance()); // never objects to shutdown.        
        cf.halting();
        
        assertEquals(Status.STATUS_SHUTDOWN,manager.getStatus());
    }
    
}
