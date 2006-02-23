package org.astrogrid.desktop.modules.plastic;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.astrogrid.common.namegen.InMemoryNameGen;
import org.astrogrid.common.namegen.NameGen;
import org.votech.plastic.CommonMessageConstants;

public class ApplicationStoreTest extends TestCase {

	private ApplicationStore store;
	
	private PlasticClientProxy p1;
	private PlasticClientProxy p2;
	private PlasticClientProxy p3;
	private URI m1 = URI.create("ivo://m1");
	private URI m2 = URI.create("ivo://m2");

	private NameGen namegen = new InMemoryNameGen();

	public void setUp() {
		store = new ApplicationStore();
		List messages1 = new ArrayList();
		List messages2 = new ArrayList();
		List messages3 = CommonMessageConstants.EMPTY;
		
		messages1.add(m1);
		messages2.add(m1);
		messages2.add(m2);
		p1 = new PollingPlasticClient(namegen,"p1",messages1);
		p2 = new PollingPlasticClient(namegen,"p2",messages2);
		p3 = new PollingPlasticClient(namegen,"p3",messages3);
		store.add(p1);
		store.add(p2);
		store.add(p3);
	}


	/*
	 * Test method for 'org.astrogrid.desktop.modules.plastic.ApplicationStore.remove(PlasticClientProxy)'
	 */
/*	public void testRemovePlasticClientProxy() {
		store.remove(p2);
		PlasticClientProxy p = store.get(p2.getId());
		assertNull(p);
		p = store.get(p1.getId());
		assertNotNull(p);
	}
*/
	/*
	 * Test method for 'org.astrogrid.desktop.modules.plastic.ApplicationStore.get(URI)'
	 */
	public void testGet() {
		PlasticClientProxy p = store.get(p1.getId());
		assertEquals(p1, p);
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.plastic.ApplicationStore.remove(URI)'
	 */
	public void testRemoveURI() {
		store.remove(p1.getId());
		PlasticClientProxy p = store.get(p1.getId());
		assertNull(p);
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.plastic.ApplicationStore.getClientIdsSupportingMessage(URI, boolean, boolean)'
	 */
	public void testGetClientIdsSupportingMessage1() {
		List apps = store.getClientIdsSupportingMessage(m1, true, true);
		assertEquals(3, apps.size());
	}
	public void testGetClientIdsSupportingMessage2() {
		List apps = store.getClientIdsSupportingMessage(m2, true, true);
		assertEquals(2, apps.size());
	}
	public void testGetClientIdsSupportingMessage3() {
		List apps = store.getClientIdsSupportingMessage(m2, false, true);
		assertEquals(1, apps.size());
	}	
}
