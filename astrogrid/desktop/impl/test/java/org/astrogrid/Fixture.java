/**
 * 
 */
package org.astrogrid;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.vfs.CacheStrategy;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.VFS;
import org.apache.commons.vfs.impl.DefaultFileReplicator;
import org.apache.commons.vfs.impl.PrivilegedFileReplicator;
import org.apache.commons.vfs.provider.ftp.FtpFileProvider;
import org.apache.commons.vfs.provider.local.DefaultLocalFileProvider;
import org.apache.commons.vfs.provider.ram.RamFileProvider;
import org.apache.commons.vfs.provider.sftp.SftpFileProvider;
import org.apache.commons.vfs.provider.temp.TemporaryFileProvider;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.desktop.alternatives.HeadlessUIComponent;
import org.astrogrid.desktop.alternatives.InThreadExecutor;
import org.astrogrid.desktop.modules.ag.vfs.DesktopFilesCache;
import org.astrogrid.desktop.modules.system.AugmentedFileContentInfoFilenameFactory;
import org.astrogrid.desktop.modules.system.BackgroundExecutor;
import org.astrogrid.desktop.modules.system.HivemindFileSystemManager;
import org.astrogrid.desktop.modules.system.QueryRespectingUrlFileProvider;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.system.ui.UIContextImpl;
import org.astrogrid.desktop.modules.ui.UIComponent;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;

import static junit.framework.Assert.*;
import static org.easymock.EasyMock.*;
/** class of static assertions and helper methods to create test fixtures.
 * @author Noel Winstanley
 * @since Jun 14, 20065:22:53 PM
 */
public class Fixture {

	/** test an object is serializable by seriazing, deserializing, and then comparing two objects for equality using the comparator 
	 * 
	 * @throws ClassNotFoundException */
	public static void assertSerializable(Object o) throws IOException, ClassNotFoundException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(os);
		oos.writeObject(o);
		oos.close();
		InputStream is = new ByteArrayInputStream(os.toByteArray());
		ObjectInputStream ois = new ObjectInputStream(is);
		Object nu = ois.readObject();
		assertNotNull(nu);
		assertEquals(o.getClass(),nu.getClass());
		assertNotSame(o,nu);
		assertEquals(o,nu);
		
	}
	
	/** create a ui context suitable for using as a test fixture.
	 * contains sufficient to run background workers.
	 * it's a nice mock, which will return an in-thread executor for calls
	 * to getExecutor(), and a list for getTasksList()
	 * @return a mock in the RECORD state. must be switched to 'replay'
	 * before use.
	 */
	public static UIContext createMockContext() {
	    Configuration configuration = createNiceMock("configuration",Configuration.class);
	    replay(configuration);
	    BackgroundExecutor be = createExecutor();
	    EventList tasks = new BasicEventList();
	    UIContext cxt = createNiceMock("context",UIContext.class);
	    UIComponent component = new HeadlessUIComponent("testUI",cxt);
	    expect(cxt.getExecutor()).andStubReturn(be);
	    expect(cxt.getTasksList()).andStubReturn(tasks);
	    expect(cxt.getConfiguration()).andStubReturn(configuration);
	    expect(cxt.findMainWindow()).andStubReturn(component);
	    return cxt;
	}
	
	/** create an executor suitable for use as a test fixture
	 * 
	 * @return an instance of inThreadExecutor - to remove concurrency from tests
	 */
	public static BackgroundExecutor createExecutor() {
	    return new InThreadExecutor();
	}
	
	/** creates a filesystem manager with the same configuration as
	 * within AR (by repeating the coding achieved in system.xml)
	 * but without any myspace extensions.
	 * This is fragile - has to be kept manually in-synch with system.xml.
	 * @return
	 * @throws IOException 
	 */
	public static FileSystemManager createVFS() throws IOException {
	    final HivemindFileSystemManager vfs = new HivemindFileSystemManager();
	    final DefaultFileReplicator replicator = new DefaultFileReplicator();
        vfs.setTemporaryFileStore(replicator );
	    vfs.addProvider("file",new DefaultLocalFileProvider());
	    vfs.addProvider("tmp",new TemporaryFileProvider());
	    vfs.addProvider("ftp",new FtpFileProvider());
	    vfs.addProvider("sftp",new SftpFileProvider());
	    vfs.addProvider("ram",new RamFileProvider());
	    
	   // operation providers - not used.	    
	  // extensionmap - not used.	    
	 // mimes  - not used.
	    
	    vfs.setCacheStrategy(CacheStrategy.MANUAL);
	    vfs.setFilesCache(new DesktopFilesCache());
	    vfs.setDefaultProvider(new QueryRespectingUrlFileProvider());
	    vfs.setReplicator(new PrivilegedFileReplicator(replicator));
	    vfs.setBaseFile(SystemUtils.getUserHome());
	    vfs.setFileContentInfoFactory(new AugmentedFileContentInfoFilenameFactory());
	    vfs.init();
	    return vfs;
	    
	}
	
	/** create a mock browser, in the 'record' state */
	public static BrowserControl createMockBrowser() {
	    return createMock("browser",BrowserControl.class);
	}
	
	
	
	/**
	 *create a temporary directory, named based on the test class.
	 * @param c the test class
	 * @return a directory that exists, and which will be deleted when VM exits.
	 * @throws IOException 
	 */
	public static File createTempDir(Class c) throws IOException {
        File bd = File.createTempFile(c.getSimpleName(),null);
        bd.delete();
        bd.mkdirs();
        assertTrue(bd.exists());
        assertTrue(bd.isDirectory());
        FileUtils.forceDeleteOnExit(bd);       
        return bd;
	}


}
