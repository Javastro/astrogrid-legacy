/**
 * 
 */
package org.astrogrid.desktop.modules.votech;

import java.net.URI;
import java.net.URL;

import junit.framework.TestCase;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;

import org.votech.VoMon;
import org.votech.VoMonBean;

/**
 * @author Noel Winstanley
 * @since Jan 8, 200711:57:39 PM
 * @TEST more fully.
 */
public class VoMonImplUnitTest extends TestCase {

	@Override
    protected void setUp() throws Exception {
		super.setUp();
		endpoint = VoMonImplUnitTest.class.getResource("status.xml");
		assertNotNull("status.xml not availebl",endpoint);

			final Configuration conf = new Configuration();
			CacheConfiguration defaults = new CacheConfiguration() {{

				conf.setDefaultCacheConfiguration(this);
			}};
			CacheConfiguration tmp = new CacheConfiguration() {{
				setName("test-vomon");
				setDiskPersistent(false);
				setOverflowToDisk(false);
				setMaxElementsInMemory(1000);
				setEternal(false);
				setTimeToIdleSeconds(120);
				setTimeToLiveSeconds(120);				
				conf.addCache(this);
			}};			
			manager = new CacheManager(conf);
	
		cache = manager.getEhcache("test-vomon");
		
		assertNotNull("cache is null",cache);
	}
	
	final static int refresh = 30;
	URL endpoint;
	CacheManager manager;
	Ehcache cache;
	
	@Override
    protected void tearDown() throws Exception {
		super.tearDown();
		endpoint = null;
		manager.shutdown();
		manager = null;
		cache = null;
	}

	public void testLoad() throws Exception {
		VoMonInternal mon = new VoMonImpl(endpoint.toString(),refresh,cache);
		mon.reload();
		assertTrue(cache.getSize() > 0);
		//System.out.println(cache.getKeys());
		
		assertEquals(refresh * 1000,mon.getPeriod());
	}
	
	public void testCheckAvailabilityUp() throws Exception {
		VoMon mon = new VoMonImpl(endpoint.toString(),refresh,cache);
		mon.reload();
		URI u = new URI("ivo://cadc.nrc.ca/siap/cfht");
		VoMonBean bean = mon.checkAvailability(u);
		assertNotNull(bean);
		assertEquals(u,bean.getId());
		assertEquals(5,bean.getCode());
		assertEquals("up",bean.getStatus());
		assertNotNull(bean.getTimestamp());
		assertTrue(bean.getMillis() > 0);
	}
	
	public void testCheckAvailabilityDown() throws Exception {
		VoMon mon = new VoMonImpl(endpoint.toString(),refresh,cache);;
		mon.reload();
		URI u = new URI("ivo://uk.ac.le.star/TXM");
		VoMonBean bean = mon.checkAvailability(u);
		assertNotNull(bean);
		assertEquals(u,bean.getId());
		assertEquals(4,bean.getCode());
		assertEquals("bad reply",bean.getStatus());
		assertNotNull(bean.getTimestamp());
		assertTrue(bean.getMillis() > 0);
	}
	
	public void testCheckAvailabilityUnknown() throws Exception {
		VoMon mon = new VoMonImpl(endpoint.toString(),refresh,cache);
		mon.reload();
		URI u = new URI("ivo://wfau.roe.ac.uk/unknown");
		VoMonBean bean = mon.checkAvailability(u);
		assertNull(bean);
	}
	
	public void testCheckCEAAvailability() throws Exception {
		VoMon mon = new VoMonImpl(endpoint.toString(),refresh,cache);
		mon.reload();
		URI u = new URI("ivo://uk.ac.le.star/CADC-HSTCA/images/CEA-application");
		VoMonBean[] beans = mon.checkCeaAvailability(u);
		assertNotNull(beans);
		assertEquals(3,beans.length);
		VoMonBean bean = beans[0];
		assertEquals(new URI("ivo://uk.ac.le.star/SIAP-CEC-2"),bean.getId());
		assertEquals(5,bean.getCode());
		assertEquals("up",bean.getStatus());
		assertNotNull(bean.getTimestamp());
		assertTrue(bean.getMillis() > 0);
	}	
	
	public void testCheckCEAAvailabilityUnknown() throws Exception {
		VoMon mon = new VoMonImpl(endpoint.toString(),refresh,cache);
		mon.reload();
		URI u = new URI("ivo://uk.ac.le.star/CADC-HSTCA/images/CEA-application1");
		VoMonBean[] beans = mon.checkCeaAvailability(u);
		assertNotNull(beans);
		assertEquals(0,beans.length);
		
	}	
	
	public void testCheckAvailabilityNulls() throws Exception {
		VoMon mon = new VoMonImpl(endpoint.toString(),refresh,cache);
		mon.reload();
		assertNull(mon.checkAvailability(null));
		assertNotNull(mon.checkCeaAvailability(null));
		assertEquals(0,mon.checkCeaAvailability(null).length);
		
	}
}
