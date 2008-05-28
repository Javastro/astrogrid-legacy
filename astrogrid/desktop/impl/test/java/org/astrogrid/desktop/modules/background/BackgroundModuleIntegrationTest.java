/**
 * 
 */
package org.astrogrid.desktop.modules.background;

import java.net.URI;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;
import org.astrogrid.desktop.modules.ag.RemoteProcessStrategy;

/**
 * @author Noel Winstanley
 * @since Jan 8, 20073:27:11 PM
 */
public class BackgroundModuleIntegrationTest extends InARTestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	public void testCEAStrategy() throws Exception {
		RemoteProcessStrategy s = (RemoteProcessStrategy)assertComponentExists(RemoteProcessStrategy.class, "background.ceaStrategy");
		try {
		s.canProcess((URI)null);
		} catch (NullPointerException e) {
		}
	}
	
	public void testTasks() throws Exception {
		TasksInternal i = (TasksInternal)assertComponentExists(TasksInternal.class, "background.tasks");
		assertNotNull(i.getAppLibrary());
	}

    public static Test suite() {
        return new ARTestSetup(new TestSuite(BackgroundModuleIntegrationTest.class));
    }
	
}
