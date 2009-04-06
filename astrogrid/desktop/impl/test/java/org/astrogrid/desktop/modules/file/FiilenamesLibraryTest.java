/**
 * 
 */
package org.astrogrid.desktop.modules.file;
import java.io.File;
import java.io.FileFilter;
import java.net.URI;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileSystemManager;
import org.astrogrid.Fixture;

/** Library test that explores what is returned from VFS.Filename and URI methods
 * to help decide what to expose in Names api.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Dec 31, 20083:03:33 PM
 */
public class FiilenamesLibraryTest extends TestCase {

    private FileSystemManager vfs;
    private URI uri;
    private FileName fn;
    private File file;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.vfs = Fixture.createVFS();
        final File home = new File(SystemUtils.USER_HOME);
        final File[] files = home.listFiles((FileFilter)FileFileFilter.FILE);
        file = files[0];
        this.uri = file.toURI();
        fn = vfs.resolveURI(uri.toString());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        vfs = null;
        uri = null;
        fn = null;
    }
    
    public void testURI() throws Exception {
        final Map<String, Object> map = PropertyUtils.describe(uri);
        MapUtils.debugPrint(System.err,"uri",map);
    }
    
    public void testFileName() throws Exception {
        final Map<String, Object> map = PropertyUtils.describe(fn);
        MapUtils.debugPrint(System.err,"FileName",map);
    }
    
    public void testURL() throws Exception {
        final Map<String, Object> map = PropertyUtils.describe(uri.toURL());
        MapUtils.debugPrint(System.err,"url",map);
    }
    public void testFile() throws Exception {
        final Map<String, Object> map = PropertyUtils.describe(file);
        MapUtils.debugPrint(System.err,"file",map);
    }
}
