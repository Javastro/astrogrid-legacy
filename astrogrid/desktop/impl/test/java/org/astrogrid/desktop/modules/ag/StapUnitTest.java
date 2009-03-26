/**
 * 
 */
package org.astrogrid.desktop.modules.ag;

import static org.easymock.EasyMock.*;

import java.net.URL;

import junit.framework.TestCase;

import org.apache.commons.vfs.FileSystemManager;
import org.astrogrid.acr.astrogrid.Stap;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.desktop.modules.system.ui.UIContext;

/** 
 * @author Noel Winstanley
 * @since Jun 13, 20067:02:10 PM
 * @TEST extend
 */
public class StapUnitTest extends TestCase {

	/*
	 * @see TestCase#setUp()
	 */
	@Override
    protected void setUp() throws Exception {
		super.setUp();
		mockReg =createMock(Registry.class);
        mockVFS = createMock(FileSystemManager.class);      
        mockCxt = createMock(UIContext.class);
		replay(mockReg,mockVFS,mockCxt);
		stap = new StapImpl(mockReg,mockVFS,mockCxt);
		url = new URL("http://www.astrogrid.org/cone");		
	}
	
	@Override
    protected void tearDown() throws Exception {
		super.tearDown();
		mockReg = null;
		stap = null;
		url = null;
		mockVFS = null;
		mockCxt = null;
	}
    protected FileSystemManager mockVFS;	
    protected UIContext mockCxt;
	protected Registry mockReg;
	protected Stap stap;
	protected URL url;

	public void testGetRegistryAdqlQuery() {
		assertNotNull(stap.getRegistryAdqlQuery());
	}
	public void testGetRegistryXQuery() {
		assertNotNull(stap.getRegistryXQuery());
	}

}
