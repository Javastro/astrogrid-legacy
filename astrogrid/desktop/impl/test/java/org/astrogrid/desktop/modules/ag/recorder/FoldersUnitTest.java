/**
 * 
 */
package org.astrogrid.desktop.modules.ag.recorder;

import jdbm.RecordManager;
import jdbm.btree.BTree;

import org.easymock.MockControl;

import junit.framework.TestCase;

/** Unit test for Folders.
 * @implement - quite a bit of work here.
 * @author Noel Winstanley
 * @since Jun 13, 20064:00:09 PM
 */
public class FoldersUnitTest extends TestCase {

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		recControl = MockControl.createControl(RecordManager.class);
		rec = (RecordManager)recControl.getMock();
		folders = Folders.findOrCreate(rec);
	}
	
protected void tearDown() throws Exception {
	super.tearDown();
	recControl = null;
	btree = null;
	rec = null;
	folders = null;
}	
	
	protected MockControl recControl;
	protected BTree btree;
	protected RecordManager rec;
	protected Folders folders;

}
