/**
 * 
 */
package org.astrogrid.desktop.modules.ui.scope;

import junit.framework.TestCase;

import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.VFS;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.modules.ui.fileexplorer.IconFinder;

import edu.berkeley.guir.prefuse.focus.FocusSet;
import edu.berkeley.guir.prefuse.graph.DefaultTreeNode;
import edu.berkeley.guir.prefuse.graph.TreeNode;
import static org.easymock.EasyMock.*;

/** Test some of the behaviour of the vizModel.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Dec 4, 20079:48:18 AM
 */
public class VizModelUnitTest extends TestCase {


    private DalProtocolManager dal;
    private QueryResultCollector query;
    private FileSystemManager vfs;
    private Service service;
    private AstroscopeTableHandler handler;
    private IconFinder finder;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        dal = new DalProtocolManager();
        
        query = createMock(QueryResultCollector.class);
        
        vfs = VFS.getManager();
        
        // useful for testing.
        service = createMock(Service.class);
        
        handler = createMock(AstroscopeTableHandler.class);
        
        finder = createMock(IconFinder.class);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        dal = null;
        query = null;
        vfs = null;
        service = null;
        handler = null;
        finder = null;
    }
    private void replayAll() {
        replay(query,service,handler,finder);
    }
    
    private void verifyAll() {
        verify(query,service,handler,finder);        
    }
    
    private VizModel createModel() {
        return  new VizModel(null,dal,query,vfs,finder);
    }
    
    public void testCreation() throws Exception {
        replayAll();
        VizModel vm = createModel();
        verifyAll();
    }
    
    public void testNodeSizingMap() throws Exception {
        query.clear();
        replayAll();
        VizModel vm = createModel();
        NodeSizingMap map = vm.getNodeSizingMap();
        assertNotNull(map);
        vm.clear();
        assertSame(map,vm.getNodeSizingMap());
        verifyAll();        
    }

    
    public void testGetProtocols() throws Exception {
        replayAll();
        VizModel vm = createModel();
        assertNotNull(vm.getProtocols());
        assertSame(dal,vm.getProtocols());
        verifyAll();
    }
    

    
    public void testGetTree() throws Exception {
        replayAll();
        VizModel vm = createModel();
        assertNotNull(vm.getTree());
        verifyAll();        
    }
    
    public void testGetSelectionFocusSet() throws Exception {
        query.clear();
        replayAll();
        VizModel vm = createModel();
        FocusSet set = vm.getSelectionFocusSet();
        assertNotNull(set);
        TreeNode t = new DefaultTreeNode();
        set.add(t);
        assertTrue(set.contains(t));
        vm.clear();
        assertSame(set,vm.getSelectionFocusSet());
        assertFalse(set.contains(t));
        assertEquals(0,set.size());
        verifyAll();        
    }    
    
 
public void testGetSelectionTransferable() throws Exception {
    
}

}
