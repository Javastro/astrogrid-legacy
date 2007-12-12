/**
 * 
 */
package org.astrogrid.desktop.modules.ui.scope;

import java.io.File;
import java.net.URI;

import junit.framework.TestCase;

import org.apache.commons.io.FileUtils;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.FileUtil;
import org.apache.commons.vfs.VFS;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.fileexplorer.IconFinder;
import org.easymock.MockControl;

import edu.berkeley.guir.prefuse.focus.FocusSet;
import edu.berkeley.guir.prefuse.graph.DefaultTreeNode;
import edu.berkeley.guir.prefuse.graph.TreeNode;

/** Test some of the behaviour of the vizModel.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Dec 4, 20079:48:18 AM
 */
public class VizModelUnitTest extends TestCase {


    private DalProtocolManager dal;
    private MockControl queryMock;
    private QueryResultCollector query;
    private FileSystemManager vfs;
    private MockControl serviceMock;
    private Service service;
    private MockControl handlerMock;
    private AstroscopeTableHandler handler;
    private MockControl finderMock;
    private IconFinder finder;

    protected void setUp() throws Exception {
        super.setUp();

        dal = new DalProtocolManager();
        
        queryMock = MockControl.createControl(QueryResultCollector.class);
        query = (QueryResultCollector)queryMock.getMock();
        
        vfs = VFS.getManager();
        
        // useful for testing.
        serviceMock = MockControl.createControl(Service.class);
        service = (Service)serviceMock.getMock();
        
        handlerMock = MockControl.createControl(AstroscopeTableHandler.class);
        handler = (AstroscopeTableHandler)handlerMock.getMock();
        
        finderMock = MockControl.createControl(IconFinder.class);
        finder = (IconFinder)finderMock.getMock();
    }

    protected void tearDown() throws Exception {
        super.tearDown();

        dal = null;
        queryMock = null;
        query = null;
        vfs = null;
        serviceMock = null;
        service = null;
        handlerMock = null;
        handler = null;
        finder = null;
        finderMock = null;
    }
    private void replayAll() {
        queryMock.replay();
        serviceMock.replay();
        handlerMock.replay();
        finderMock.replay();
    }
    
    private void verifyAll() {
        queryMock.verify();
        serviceMock.verify();
        handlerMock.verify();
        finderMock.verify();
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
