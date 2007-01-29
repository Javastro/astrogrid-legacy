/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import uk.ac.starlink.util.DataSource;

/** Unit tests for leaf.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jan 25, 20072:20:38 PM
 */
public class MyspaceLeafUnitTest extends MyspaceNodeUnitTest {

	protected void setUp() throws Exception {
		super.setUp();
		assertTrue(msnode instanceof MyspaceLeaf) ;
		msleaf = (MyspaceLeaf)msnode;
	}
	MyspaceLeaf msleaf;
	protected void tearDown() throws Exception {
		super.tearDown();
		msleaf = null;
	}
	
	protected MyspaceNode createMyspaceNode() {
		return new MyspaceLeaf(node,branch);
	}
	
	public void testDatasource() throws Exception {
		node.readContent();
		InputStream is = new InputStream() {

			public int read() throws IOException {
				return 0;
			}
		};
		nodeControl.setReturnValue(is);
		replay();
		DataSource ds = msleaf.getDataSource();
		assertNotNull(ds);
		assertTrue(ds instanceof MyspaceDataSource);
		MyspaceDataSource mds = (MyspaceDataSource)ds;
		assertSame(is,mds.getRawInputStream());
		verify();
	}
	
	public void testGetOutputStream() throws Exception {
		node.writeContent();
		OutputStream os = new OutputStream() {

			public void write(int b) throws IOException {
			}
		};
		nodeControl.setReturnValue(os);
		replay();
		OutputStream os1 = msleaf.getOutputStream();
		assertSame(os,os1);
		verify();
	}

}
